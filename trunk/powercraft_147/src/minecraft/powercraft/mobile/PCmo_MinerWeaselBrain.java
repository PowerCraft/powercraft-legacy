package powercraft.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Color;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Inventory;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecI;
import powercraft.management.gres.PC_GresTextEditMultiline.Keyword;
import powercraft.management.registry.PC_RecipeRegistry;
import powercraft.mobile.PCmo_Command.ParseException;
import powercraft.mobile.PCmo_EntityMiner.Agree;
import powercraft.weasel.PCws_IWeaselEngine;
import powercraft.weasel.PCws_IWeaselNetworkDevice;
import powercraft.weasel.PCws_WeaselHighlightHelper;
import powercraft.weasel.PCws_WeaselManager;
import powercraft.weasel.PCws_WeaselNetwork;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.exception.WeaselRuntimeExceptionFunctionNotExist;
import weasel.lang.Instruction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


public class PCmo_MinerWeaselBrain  implements PCmo_IMinerBrain, PCws_IWeaselNetworkDevice, PCws_IWeaselEngine {

	private static final String default_program = 
			"# *** Weasel powered Miner ***\n";
	
	private PCmo_EntityMiner miner;
	/** weasel engine */
	private MinerProvider functionProvider; 
	private WeaselEngine engine;
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	private int sleep = 0;
	private String program = default_program;
	private String error;
	private boolean stop=false;
	private static Random rand = new Random();
	/** Displayed text. "\n" is a newline. */
	private String text = "";
	
	private int networkID = -1;
	private int id;
	
	
	/**  */
	private List<String> userInput = new ArrayList<String>();
	
	public PCmo_MinerWeaselBrain(PCmo_EntityMiner miner, boolean server) {
		if(server){
			id = PCws_WeaselManager.registerPlugin(this);
		}else{
			id=-1;
		}
		functionProvider = new MinerProvider();
		registerFunctions(functionProvider);
		engine = new WeaselEngine(functionProvider);
		this.miner = miner;
		miner.setInfo("text", "");
		miner.setInfo("color", new PC_Color(1, 1, 1));
		miner.setInfo("deviceName", Calc.generateUniqueName());
		miner.setInfo("networkName", "");
	}
	
	@Override
	public String getScriptName(){
		return "Weasel";
	}
	
	@Override
	public void setProgram(String prog){
		program = prog;
	}
	
	@Override
	public String getProgram(){
		return program;
	}
	
	@Override
	public void restart() {
		error = null;
		engine.restartProgramClearGlobals();
	}

	@Override
	public void launch() throws ParseException {
		restart();
		try {
			List<Instruction> list = WeaselEngine.compileProgram(program);
			engine.insertNewProgram(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage());
		}
	}

	private void setError(String error){
		this.error = error;
	}
	
	@Override
	public boolean hasError() {
		return error != null;
	}

	@Override
	public String getError() {
		return error;
	}

	@Override
	public void run() {
		if (!hasError()) {
			if(!miner.isMiningInProgress())
				return;
			engine.setStatementsToRun(500);
			try {

				if (sleep > 0) {
					sleep--;
					return;
				}
				while(engine.getStatementsToRun()>0  && !engine.isProgramFinished){
					if(!engine.run())
						break;
				}
			} catch (WeaselRuntimeException wre) {
				wre.printStackTrace();
				setError(wre.getMessage());
			}
		}
	}

	@Override
	public List<Keyword> getKeywords() {
		return PCws_WeaselHighlightHelper.weasel(functionProvider, engine);
	}
	
	@Override
	public PCmo_IMinerBrain readFromNBT(NBTTagCompound nbttag) {
		if(id!=-1){
			PCws_WeaselManager.removePlugin(this);
			id = nbttag.getInteger("id");
			PCws_WeaselManager.registerPlugin(this, id);
		}
		networkID = nbttag.getInteger("networkID");
		miner.setInfo("deviceName", nbttag.getString("name"));
		SaveHandler.loadFromNBT(nbttag, "engine", engine);
		sleep = nbttag.getInteger("sleep");
		program = nbttag.getString("program");
		if(nbttag.hasKey("error"))
			error = nbttag.getString("error");
		if(getNetwork()!=null){
			getNetwork().registerMember(this);
		}
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setInteger("id", id);
		nbttag.setInteger("networkID", networkID);
		nbttag.setString("name", (String)miner.getInfo("deviceName"));
		SaveHandler.saveToNBT(nbttag, "engine", engine);
		nbttag.setInteger("sleep", sleep);
		nbttag.setString("program", program);
		if(error!=null)
			nbttag.setString("error", error);
		return nbttag;
	}

	@Override
	public void compileProgram(String text) throws Exception {
		WeaselEngine.compileProgram(text);
	}

	public boolean doesProvideFunctionOnEngine(String functionName) {
		return engine.instructionList.hasFunctionForExternalCall(functionName);
	}
	
	public void callFunctionOnEngine(String functionName, WeaselObject... args) {
		if(hasError()||stop)
			return;
		if(sleep<=0){
			try{
				int state = engine.callFunctionExternal(functionName, (Object[])args);
				if(state == -1 || state == 1) return;	
			} catch (WeaselRuntimeException wre) {
				setError(wre.getMessage());
				return;	
			} catch (Exception e) {
				e.printStackTrace();
				setError(e.getMessage());
				return;	
			}
		}
		externalCallsWaiting.add(new PC_Struct2<String, Object[]>(functionName, args));
	}
	
	/**
	 * Add text to the terminal, if too long remove oldest.
	 * @param text
	 */
	public void addText(String text) {
		this.text += text.replace("\\n", "\n");
		if (countIn(this.text, '\n') > 60) {
			while (countIn(this.text, '\n') > 60) {
				this.text = this.text.substring(this.text.indexOf('\n') + 1);
			}
		}
		miner.setInfo("text", this.text);
	}
	
	private int countIn(String str, char c) {
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				counter++;
			}
		}
		return counter;
	}
	
	public void removeFromNetwork(){
		PCws_WeaselNetwork oldNetwork = getNetwork();
		if(oldNetwork!=null){
			oldNetwork.removeMember(this);
			miner.setInfo("networkName", "");
		}
	}
	
	public void connectToNetwork(PCws_WeaselNetwork network){
		removeFromNetwork();
		network.registerMember(this);
		miner.setInfo("networkName", network.getName());
		miner.setInfo("color", network.getColor());
	}
	
	@Override
	public void msg(Object[] obj) {
		if("input".equals(obj[0])){
			String input = (String)obj[1];
			input = input.trim();
			if (input.length() > 0) {
				userInput.add(input);
				addText("> " + input + "\n");
				if(doesProvideFunctionOnEngine("termIn")){
					callFunctionOnEngine("termIn", new WeaselString(getName()), new WeaselString(input));
				}
			}
			if (userInput.size() > 16) {
				userInput.remove(0);
			}
		}else if(obj[0].equals("deviceRename")){
			if(getNetwork()!=null){
				getNetwork().renameMember(this, (String)obj[1]);
			}
			miner.setInfo("deviceName", (String)obj[1]);
		}else if(obj[0].equals("networkJoin")){
			if(((String) obj[1]).equals("")){
				removeFromNetwork();
			}else{
				connectToNetwork(PCws_WeaselManager.getNetwork((String) obj[1]));
			}
		}else if(obj[0].equals("networkRename")){
			if(getNetwork()==null){
				connectToNetwork(new PCws_WeaselNetwork());
			}
			getNetwork().setName((String) obj[1]);
		}else if(obj[0].equals("networkNew")){
			connectToNetwork(new PCws_WeaselNetwork());
			getNetwork().setName((String) obj[1]);
		}else if(obj[0].equals("networkColor")){
			if(getNetwork()!=null){
				getNetwork().setColor((PC_Color)obj[1]);
			}
		}
	}

	@Override
	public WeaselEngine getEngine() {
		return engine;
	}

	@Override
	public void setNetwork(int i) {
		networkID = i;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public WeaselFunctionManager makePluginProvider() {
		WeaselFunctionManager fp = new WeaselFunctionManager();
		registerFunctions(fp);
		return fp;
	}

	@Override
	public void setNetworkName(String name) {
		miner.setInfo("networkName", name);
	}

	@Override
	public void setNetworkColor(PC_Color color) {
		miner.setInfo("color", color);
	}

	@Override
	public PCws_WeaselNetwork getNetwork() {
		if(networkID==-1)
			return null;
		return PCws_WeaselManager.getNetwork(networkID);
	}
	
	public class MinerProvider extends WeaselFunctionManager{
		
		@Override
		public WeaselObject call(WeaselEngine engine, String name, boolean var, WeaselObject... args) throws WeaselRuntimeException {
			try{
				return super.call(engine, name, var, args);
			}catch(WeaselRuntimeExceptionFunctionNotExist e){
				try{
					return PCws_WeaselManager.getGlobalFunctionManager().call(engine, name, var, args);
				}catch(WeaselRuntimeExceptionFunctionNotExist e1){
					if(getNetwork()==null){
						throw e1;
					}else{
						return getNetwork().getFunctionHandler().call(engine, name, var, args);
					}
				}
			}
		}

		@Override
		public boolean doesProvideFunction(String name) {
			if(super.doesProvideFunction(name))
				return true;
			if(PCws_WeaselManager.getGlobalFunctionManager().doesProvideFunction(name))
				return true;
			if(getNetwork()==null)
				return false;
			return getNetwork().getFunctionHandler().doesProvideFunction(name);
		}
		
		@Override
		public List<String> getProvidedFunctionNames() {
			List<String> list = super.getProvidedFunctionNames();
			list.addAll(PCws_WeaselManager.getGlobalFunctionManager().getProvidedFunctionNames());
			if(getNetwork()!=null){
				list.addAll(getNetwork().getFunctionHandler().getProvidedFunctionNames());
			}
			return list;
		}
		
		@Override
		public List<String> getProvidedVariableNames() {
			List<String> list = super.getProvidedVariableNames();
			if(getNetwork()!=null){
				list.addAll(getNetwork().getFunctionHandler().getProvidedVariableNames());
			}
			return list;
		}
		
	}

	@Override
    public String getName() {
            return (String)miner.getInfo("deviceName");
    }

    @Override
    public void onOpenGui() {
        miner.setInfo("deviceNames", PCws_WeaselManager.getAllPluginNames());
        miner.setInfo("networkNames", PCws_WeaselManager.getAllNetworkNames());
        miner.setInfo("text", text);
        if(getNetwork()!=null){
                miner.setInfo("networkName", getNetwork().getName());
        }
    }
    
    public void registerFunctions(WeaselFunctionManager fp){
		fp.registerMethod("run", "runOldScript", this);
		fp.registerMethod("do", "runOldScript", this);
		
		fp.registerMethod("forward", "forward", this);
		fp.registerMethod("go", "forward", this);
		fp.registerMethod("fw", "forward", this);
		
		fp.registerMethod("backward", "backward", this);
		fp.registerMethod("back", "backward", this);
		fp.registerMethod("bw", "backward", this);
		
		fp.registerMethod("up", this);
		
		fp.registerMethod("down", this);
		
		fp.registerMethod("left", this);
		
		fp.registerMethod("right", this);
		
		fp.registerMethod("turn", this);

		fp.registerMethod("xplus", "xplus", this);
		fp.registerMethod("east", "xplus", this);

		fp.registerMethod("xminus", "xminus", this);
		fp.registerMethod("west", "xminus", this);

		fp.registerMethod("zplus", "zplus", this);
		fp.registerMethod("south", "zplus", this);

		fp.registerMethod("zminus", "zminus", this);
		fp.registerMethod("north", "zminus", this);
		
		fp.registerMethod("deposit", "deposit", this);
		fp.registerMethod("depo", "deposit", this);
		fp.registerMethod("store", "deposit", this);
		
		fp.registerMethod("countStacks", "countStacks", this);
		fp.registerMethod("stacks", "countStacks", this);

		fp.registerMethod("countItems", "countItems", this);
		fp.registerMethod("items", "countItems", this);
		
		fp.registerMethod("countEmpty", "countEmpty", this);

		fp.registerMethod("isFull", "isFull", this);
		fp.registerMethod("full", "isFull", this);
		
		fp.registerMethod("isEmpty", "isEmpty", this);
		fp.registerMethod("empty", "isEmpty", this);
		
		fp.registerMethod("countFuel", "countFuel", this);
		fp.registerMethod("fuel", "countFuel", this);

		fp.registerMethod("destroyMiner", "destroyMiner", this);

		fp.registerMethod("sleep", "sleep", this);
		
		fp.registerMethod("bell", "bell", this);

		fp.registerMethod("isDay", "isDay", this);

		fp.registerMethod("isNight", "isNight", this);

		fp.registerMethod("isRaining", "isRaining", this);
		
		fp.registerMethod("term", "term_Input", this);
		
		fp.registerMethod("idMatch", "idMatch", this);
		fp.registerMethod("ideq", "idMatch", this);

		fp.registerMethod("getBlock", "getBlockAt", this);
		fp.registerMethod("blockAt", "getBlockAt", this);
		fp.registerMethod("getId", "getBlockAt", this);
		fp.registerMethod("idAt", "getBlockAt", this);
		
		fp.registerMethod("setBlock", "setBlockAt", this);
		fp.registerMethod("place", "setBlockAt", this);
		
		fp.registerMethod("canHarvest", "canHarvest", this);
		
		fp.registerMethod("sortInventory", "sortInventory", this);
		fp.registerMethod("sortInv", "sortInventory", this);
		fp.registerMethod("cleanup", "sortInventory", this);

		fp.registerMethod("destroyItems", "destroyItems", this);
		fp.registerMethod("burnItems", "destroyItems", this);
		fp.registerMethod("destroy", "destroyItems", this);
		fp.registerMethod("burn", "destroyItems", this);

		fp.registerMethod("destroyKeep", "destroyKeep", this);
		fp.registerMethod("burnKeep", "destroyKeep", this);

		fp.registerMethod("depoKeep", "depoKeep", this);
		fp.registerMethod("storeKeep", "depoKeep", this);
		
		fp.registerMethod("hasOpt", "hasOption", this);
		fp.registerMethod("hasCap", "hasOption", this);
		fp.registerMethod("can", "hasOption", this);

		fp.registerMethod("cap", "setOption", this);
		fp.registerMethod("opt", "setOption", this);
		
		fp.registerMethod("capOn", "activateOption", this);
		fp.registerMethod("optOn", "activateOption", this);
		
		fp.registerMethod("capOff", "deactivateOption", this);
		fp.registerMethod("optOff", "deactivateOption", this);
		
		fp.registerMethod("clearCap", "deactivateAllOptions", this);
		fp.registerMethod("clearOpt", "deactivateAllOptions", this);
		fp.registerMethod("resetCap", "deactivateAllOptions", this);
		fp.registerMethod("resetOpt", "deactivateAllOptions", this);
		
		
		
		
		WeaselFunctionManager term = new WeaselFunctionManager();
		term.registerMethod("clear", "term_clear", this);
		term.registerMethod("cls", "term_clear", this);
		term.registerMethod("print", "term_print", this);
		term.registerMethod("out", "term_print", this);
		
		term.registerMethod("hasInput", "term_hasInput", this);

		term.registerMethod("getInput", "term_Input", this);
		term.registerMethod("in", "term_Input", this);
		
		fp.registerFunctionProvider("term", term);

		
		fp.registerVariable("pos.x", "posX", this);
		fp.registerVariable("pos.y", "posY", this);
		fp.registerVariable("pos.z", "posZ", this);

		fp.registerVariable("dir", "dirAxis", this);
		fp.registerVariable("dir.axis", "dirAxis", this);
		fp.registerVariable("axis", "dirAxis", this);

		fp.registerVariable("dir.compass", "dirCompass", this);
		fp.registerVariable("compass", "dirCompass", this);
		
		fp.registerVariable("dir.angle", "dirAngle", this);
		fp.registerVariable("angle", "dirAngle", this);

		fp.registerVariable("level", "minerLevel", this);

		fp.registerVariable("term.text", "term_text", this);
		fp.registerVariable("term.txt", "term_text", this);
	}
    
    public void runOldScript(WeaselEngine engine, String script) {
    	runOldScript(engine, script, 1);
    }
    
    public void runOldScript(WeaselEngine engine, String script, int times) {
    	for (int i = 0; i < times; i++) {
			try {
				miner.appendCode(script);
			} catch (ParseException e) {
				throw new WeaselRuntimeException(e);
			}
		}
		engine.requestPause();
    }
    
    public void forward(WeaselEngine engine){
    	forward(engine, 1);
    }
    
    public void forward(WeaselEngine engine, int times) {

		// spaces are for safety - when there are two numbers next to each other.
		try {
			miner.appendCode(" " + times + " ");
		} catch (ParseException e) {
			throw new WeaselRuntimeException(e);
		}
		engine.requestPause();
    }
    
    public void backward(WeaselEngine engine){
    	backward(engine, 1);
    }
    
    public void backward(WeaselEngine engine, int times){
    	times = -times;
		// spaces are for safety - when there are two numbers next to each other.
		try {
			miner.appendCode(" " + times + " ");
		} catch (ParseException e) {
			throw new WeaselRuntimeException(e);
		}
		engine.requestPause();
    }
    
    public void up(WeaselEngine engine){
    	up(engine, 1);
    }
    
    public void up(WeaselEngine engine, int times){
		for (int i = 0; i < times; i++) {
			try {
				miner.appendCode("U");
			} catch (ParseException e) {
				throw new WeaselRuntimeException(e);
			}
		}
		engine.requestPause();
    }
	
    public void down(WeaselEngine engine){
    	down(engine, 1);
    }
    
    public void down(WeaselEngine engine, int times){
    	for (int i = 0; i < times; i++) {
			try {
				miner.appendCode("D");
			} catch (ParseException e) {
				throw new WeaselRuntimeException(e);
			}
		}
		engine.requestPause();
    }
    
    public void left(WeaselEngine engine){
    	left(engine, 1);
    }
    
    public void left(WeaselEngine engine, int times){
		boolean R = times < 0;
		if (R) times = -times;
		for (int i = 0; i < times; i++) {
			try {
				miner.appendCode(R ? "R" : "L");
			} catch (ParseException e) {
				throw new WeaselRuntimeException(e);
			}
		}
		engine.requestPause();
    }
	
    public void right(WeaselEngine engine){
    	right(engine, 1);
    }
    
    public void right(WeaselEngine engine, int times){
    	boolean L = times < 0;
		if (L) times = -times;
		for (int i = 0; i < times; i++) {
			try {
				miner.appendCode(L ? "L" : "R");
			} catch (ParseException e) {
				throw new WeaselRuntimeException(e);
			}
		}
		engine.requestPause();
    }
    
    public void turn(WeaselEngine engine){
    	turn(engine, 2);
    }
    
    public void turn(WeaselEngine engine, int times){
    	right(engine, times);
    }

    public void xplus(WeaselEngine engine){
    	try {
			miner.appendCode("E");
		} catch (ParseException e) {
			throw new WeaselRuntimeException(e);
		}
		engine.requestPause();
    }
    
    public void xminus(WeaselEngine engine){
    	try {
			miner.appendCode("W");
		} catch (ParseException e) {
			throw new WeaselRuntimeException(e);
		}
		engine.requestPause();
    }

    public void zplus(WeaselEngine engine){
    	try {
			miner.appendCode("S");
		} catch (ParseException e) {
			throw new WeaselRuntimeException(e);
		}
		engine.requestPause();
    }
    
    public void zminus(WeaselEngine engine){
    	try {
			miner.appendCode("N");
		} catch (ParseException e) {
			throw new WeaselRuntimeException(e);
		}
		engine.requestPause();
    }

    public void deposit(WeaselObject...args){
    	if (args.length == 0) {
			miner.cargo.depositToNearbyChest(false, null);
		} else {
			int num = 0;
			if (args[0] instanceof WeaselDouble) {
				num = Calc.toInteger(args[0]);

				if (args.length == 1) {
					final int id = num;
					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(false, new Agree() {
						@Override
						public boolean agree(ItemStack stack) {
							return stack.itemID == id;
						}
					});
					return;
				}

			} else {
				if (args.length == 1) {

					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(false, new Agree() {
						private WeaselObject obj;
						
						public Agree set(WeaselObject obj) {
							this.obj = obj;
							return this;
						}
						
						@Override
						public boolean agree(ItemStack stack) {
							return miner.matchStackIdentifier(obj, stack);
						}
					}.set(args[0]));

					return;
				}

				num = -1;
			}

			// num = count, others are types.
			miner.cargo.depositToNearbyChest(false, new Agree() {
				
				private WeaselObject args[];
				private int n = 0;
				
				public Agree set(int n, WeaselObject args[]) {
					this.n = n;
					this.args = args;
					return this;
				}
				@Override
				public boolean agree(ItemStack stack) {
					if (agree_do(stack)) {
						if (n > 0) n--;
						return true;
					} else
						return false;
				}

				private boolean agree_do(ItemStack stack) {
					if (n > 0 || n == -1) {
						for (int i = 1; i < args.length; i++) {
							WeaselObject arg = args[i];
							if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) return true;
						}
					}
					return false;
				}

			}.set(num, args));
		}
    }
	
    public int countStacks(WeaselObject...args){
    	int cnt = 0;
		oo:
		for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
			ItemStack stack = miner.cargo.getStackInSlot(i);
			if(args.length==0){
				if(stack!=null){
					cnt++;
				}
			}else{
				for (int j = 0; j < args.length; j++) {
					WeaselObject arg = args[j];
					if (stack == null) continue oo;
					if (arg instanceof WeaselDouble) {
						if (stack.itemID == Calc.toInteger(arg)) {
							cnt++;
							continue oo;
						}
					} else {
						if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) {
							cnt++;
							continue oo;
						}
					}
				}
			}
		}

		return cnt;
    }
	
    public int countItems(WeaselObject...args){
    	int cnt = 0;
		oo:
		for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
			ItemStack stack = miner.cargo.getStackInSlot(i);
			if(args.length==0){
				if(stack!=null){
					cnt += stack.stackSize;
				}
			}else{
				for (int j = 0; j < args.length; j++) {
					WeaselObject arg = args[j];
					if (stack == null) continue oo;
					if (arg instanceof WeaselDouble) {
						if (stack.itemID == Calc.toInteger(arg)) {
							cnt += stack.stackSize;
							continue oo;
						}
					} else {
						if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) {
							cnt += stack.stackSize;
							continue oo;
						}
					}
				}
			}
		}

		return cnt;
    }
    
    public int countEmpty(){
    	int cnt = 0;
		for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
			ItemStack stack = miner.cargo.getStackInSlot(i);
			if (stack == null) cnt++;
		}

		return cnt;
    }

    public boolean isFull(){
    	return Inventory.hasInventoryNoFreeSlots(miner.cargo);
    }
    
    public boolean isFull(boolean str){
		if (str) return Inventory.isInventoryFull(miner.cargo);
		return Inventory.hasInventoryNoFreeSlots(miner.cargo);
    }
    
    public boolean isEmpty(){
    	return Inventory.isInventoryEmpty(miner.cargo);
    }
    
    public int countFuel(){
    	int cnt = 0;
		for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
			ItemStack stack = miner.cargo.getStackInSlot(i);
			if (stack == null) continue;
			if (stack.itemID != Item.bucketLava.itemID || !miner.getFlag(PCmo_EntityMiner.cobbleMake)) {
				cnt += PC_RecipeRegistry.getFuelValue(stack) * PCmo_EntityMiner.FUEL_STRENGTH * stack.stackSize;
			}
		}

		return cnt + miner.st.fuelBuffer;
    }

    public void destroyMiner(WeaselEngine engine){
    	miner.turnIntoBlocks();
    	if(this.engine==engine){
    		engine.requestPause();
    	}
    }
    
    public void sleep(WeaselEngine engine){
    	sleep(engine, 1);
    }
    
    public void sleep(WeaselEngine engine, int time){
    	sleep += time;
		engine.requestPause();
    }
    
    public void bell(){
		bell(1);
	}
	
	public void bell(double d){
		miner.worldObj.playSoundEffect(miner.posX + 1D, miner.posY + 2D, miner.posZ + 1D, "random.orb", 0.8F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		miner.worldObj.spawnParticle("note", miner.posX, miner.posY + 1.5D, miner.posZ, d, 0.0D, 0.0D);
	}

    public boolean isDay(){
    	return miner.worldObj.isDaytime();
    }
    
    public boolean isNight(){
    	return !miner.worldObj.isDaytime();
    }
    
    public boolean isRaining(){
    	return miner.worldObj.isRaining();
    }
    
    public boolean idMatch(int id1, WeaselObject arg){

		ItemStack stack = new ItemStack(id1, 1, 0);

		if (stack.itemID == 0) return arg instanceof WeaselDouble && Calc.toInteger(arg) == 0;

		if (stack.getItem() == null) throw new WeaselRuntimeException(arg + " is not a valid block/item ID.");

		return (PCmo_EntityMiner.matchStackIdentifier(arg, stack));
    }
    
    public int getBlockAt(String side){
		char sid = side.charAt(0);
		String num = side.substring(1);

		return GameInfo.getBID(miner.worldObj, miner.getCoordOnSide(sid, Integer.valueOf(num)));
    }

    public boolean setBlockAt(String side, Object id){
		char sid = side.charAt(0);
		String num = side.substring(1);
		String str = "";

		int numid = -1;

		if (id instanceof WeaselDouble) {
			numid = Calc.toInteger(id);
		}

		if (id instanceof WeaselString) {
			numid = -2;
			str = Calc.toString(id);
		}

		if (numid == -1) throw new WeaselRuntimeException(id + " is not a valid block id or group.");

		PC_VecI pos = miner.getCoordOnSide(sid, Integer.valueOf(num));
		if (pos == null) {
			throw new WeaselRuntimeException("setBlockAt(): " + side + " is not a valid side [FBLRUD][1234] or [ud][12].");
		}

		if (str.equals("BUILDING_BLOCK") || str.equals("BLOCK")) {
			ItemStack placed = miner.cargo.getBlockForBuilding();
			if (placed == null) {
				return false;
			} else {
				if (!placed.tryPlaceItemIntoWorld(miner.fakePlayer, miner.worldObj, pos.x, pos.y + 1, pos.z, 0, 0.0f, 0.0f, 0.0f)) {
					Inventory.addItemStackToInventory(miner.cargo, placed);
				} else {
					return true;
				}
			}
		}
		
		if (numid == -2) {
			
		}

		if (numid != -2) {
			for (int i = 0; i < miner.cargo.getSizeInventory(); i++) {
				ItemStack stack = miner.cargo.getStackInSlot(i);
				if (stack == null) continue;

				if (stack.itemID == numid) {
					ItemStack placed = miner.cargo.decrStackSize(i, 1);
					if (!placed.tryPlaceItemIntoWorld(miner.fakePlayer, miner.worldObj, pos.x, pos.y + 1, pos.z, 0, 0.0f, 0.0f, 0.0f)) {
						Inventory.addItemStackToInventory(miner.cargo, placed);
					} else {
						return true;
					}
				}
			}
			
			if(numid == Block.cobblestone.blockID && miner.canMakeCobble()) {
				return (new ItemStack(Block.cobblestone)).tryPlaceItemIntoWorld(miner.fakePlayer, 
						miner.worldObj, pos.x, pos.y + 1, pos.z, 0, 0.0f, 0.0f, 0.0f);		
			}
				
		}

		return false;
    }

    public boolean canHarvest(String side){
		char sid = side.charAt(0);
		String num = side.substring(1);

		PC_VecI pos = miner.getCoordOnSide(sid, Integer.valueOf(num));
		return miner.canHarvestBlockWithCurrentLevel(pos, GameInfo.getBID(miner.worldObj, pos));
    }
 
    public void sortInventory(){
    	miner.cargo.order();
    }
    
    public void destroyItems(WeaselObject...args){
    	if (args.length == 0) {
			miner.cargo.depositToNearbyChest(true, null);
		} else {
			int num = 0;
			if (args[0] instanceof WeaselDouble) {
				num = Calc.toInteger(args[0]);

				if (args.length == 1) {
					final int id = num;
					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(true, new Agree() {
						@Override
						public boolean agree(ItemStack stack) {
							return stack.itemID == id;
						}
					});
					return;
				}

			} else {
				if (args.length == 1) {

					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(true, new Agree() {
						private WeaselObject obj;
						
						public Agree set(WeaselObject obj) {
							this.obj = obj;
							return this;
						}
						
						@Override
						public boolean agree(ItemStack stack) {
							return miner.matchStackIdentifier(obj, stack);
						}
					}.set(args[0]));

					return;
				}

				num = -1;
			}

			// num = count, others are types.
			miner.cargo.depositToNearbyChest(true, new Agree() {
				
				private WeaselObject args[];
				private int n = 0;
				
				public Agree set(int n, WeaselObject args[]) {
					this.n = n;
					this.args = args;
					return this;
				}
				@Override
				public boolean agree(ItemStack stack) {
					if (agree_do(stack)) {
						if (n > 0) n--;
						return true;
					} else
						return false;
				}

				private boolean agree_do(ItemStack stack) {
					if (n > 0 || n == -1) {
						for (int i = 1; i < args.length; i++) {
							WeaselObject arg = args[i];
							if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) return true;
						}
					}
					return false;
				}

			}.set(num, args));
		}
    }
    
    public void destroyKeep(WeaselObject...args){
    	if (args.length == 0) {
			throw new WeaselRuntimeException("depoKeep needs at least 1 argument, 0 given.");
		} else {
			int num = 0;
			if (args[0] instanceof WeaselDouble) {
				num = Calc.toInteger(args[0]);
				if (args.length == 1) {
					final int id = num;
					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(true, new Agree() {
						@Override
						public boolean agree(ItemStack stack) {
							return stack.itemID != id;
						}
					});
					return;
				}
			} else {

				if (args.length == 1) {

					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(true, new Agree() {
						private WeaselObject obj;
						
						public Agree set(WeaselObject obj) {
							this.obj = obj;
							return this;
						}
						
						@Override
						public boolean agree(ItemStack stack) {
							return !agree_do(stack);
						}

						private boolean agree_do(ItemStack stack) {
							return PCmo_EntityMiner.matchStackIdentifier(obj, stack);
						}
					}.set(args[0]));

					return;
				}// end of "len 1 string"

				num = -1;
			}

			// num = count, others are types.
			miner.cargo.depositToNearbyChest(true, new Agree() {
				
				private WeaselObject args[];
				private int n = 0;
				
				public Agree set(int n, WeaselObject args[]) {
					this.n = n;
					this.args = args;
					return this;
				}

				@Override
				public boolean agree(ItemStack stack) {
					if (agree_do(stack)) {
						if (n == -1 || n > 0) {
							if (n != -1) n--;
							return false;
						} else {
							return true;
						}
					} else
						return true;
				}

				private boolean agree_do(ItemStack stack) {
					if (n > 0 || n == -1) {
						for (int i = 1; i < args.length; i++) {
							WeaselObject arg = args[i];
							if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) return true;
						}
					}
					return false;
				}

			}.set(num, args));
		}
    }

    public void depoKeep(WeaselObject...args){
    	if (args.length == 0) {
			throw new WeaselRuntimeException("depoKeep needs at least 1 argument, 0 given.");
		} else {
			int num = 0;
			if (args[0] instanceof WeaselDouble) {
				num = Calc.toInteger(args[0]);
				if (args.length == 1) {
					final int id = num;
					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(false, new Agree() {
						@Override
						public boolean agree(ItemStack stack) {
							return stack.itemID != id;
						}
					});
					return;
				}
			} else {

				if (args.length == 1) {

					// if args length == 1, then this is type, not amount
					miner.cargo.depositToNearbyChest(false, new Agree() {
						private WeaselObject obj;
						
						public Agree set(WeaselObject obj) {
							this.obj = obj;
							return this;
						}
						
						@Override
						public boolean agree(ItemStack stack) {
							return !agree_do(stack);
						}

						private boolean agree_do(ItemStack stack) {
							return PCmo_EntityMiner.matchStackIdentifier(obj, stack);
						}
					}.set(args[0]));

					return;
				}// end of "len 1 string"

				num = -1;
			}

			// num = count, others are types.
			miner.cargo.depositToNearbyChest(false, new Agree() {
				
				private WeaselObject args[];
				private int n = 0;
				
				public Agree set(int n, WeaselObject args[]) {
					this.n = n;
					this.args = args;
					return this;
				}

				@Override
				public boolean agree(ItemStack stack) {
					if (agree_do(stack)) {
						if (n == -1 || n > 0) {
							if (n != -1) n--;
							return false;
						} else {
							return true;
						}
					} else
						return true;
				}

				private boolean agree_do(ItemStack stack) {
					if (n > 0 || n == -1) {
						for (int i = 1; i < args.length; i++) {
							WeaselObject arg = args[i];
							if (PCmo_EntityMiner.matchStackIdentifier(arg, stack)) return true;
						}
					}
					return false;
				}

			}.set(num, args));
		}
    }
    
    public boolean hasOption(String capname){
		if (capname.equals("KEEP_FUEL")) {
			return true;
		}
		if (capname.equals("TORCHES")) {
			return miner.st.level >= PCmo_EntityMiner.LTORCH;
		}
		if (capname.equals("TORCH_FLOOR")) {
			return miner.st.level >= PCmo_EntityMiner.LTORCH;
		}
		if (capname.equals("COMPRESS")) {
			return miner.st.level >= PCmo_EntityMiner.LCOMPRESS;
		}
		if (capname.equals("MINING")) {
			return true;
		}
		if (capname.equals("BRIDGE")) {
			return miner.st.level >= PCmo_EntityMiner.LBRIDGE;
		}
		if (capname.equals("LAVA")) {
			return miner.st.level >= PCmo_EntityMiner.LLAVA;
		}
		if (capname.equals("WATER")) {
			return miner.st.level >= PCmo_EntityMiner.LWATER;
		}
		if (capname.equals("COBBLE")) {
			return miner.st.level >= PCmo_EntityMiner.LCOBBLE;
		}
		throw new WeaselRuntimeException("hasOption(): Unknown option " + capname);
    }
    
    public void setOption(boolean flag, String...args){
    	System.out.println("setOption");
    	for (int i = 0; i < args.length; i++) {

			String capname = args[i];
			int argl = args.length;

			if (capname.equals("KEEP_FUEL")) {
				miner.setFlag(PCmo_EntityMiner.keepAllFuel, flag);
			}else if (capname.equals("COBBLE")) {
				miner.setFlag(PCmo_EntityMiner.cobbleMake, flag);
			}else if (capname.equals("TORCHES")) {
				miner.setFlag(PCmo_EntityMiner.torches, flag);
			}else if (capname.equals("TORCH_FLOOR")) {
				miner.setFlag(PCmo_EntityMiner.torchesOnlyOnFloor, flag);
			}else if (capname.equals("COMPRESS")) {
				miner.setFlag(PCmo_EntityMiner.compressBlocks, flag);
			}else if (capname.equals("MINING")) {
				miner.setFlag(PCmo_EntityMiner.miningEnabled, flag);
			}else if (capname.equals("BRIDGE")) {						
				miner.setFlag(PCmo_EntityMiner.bridgeEnabled, flag);
			}else if (capname.equals("LAVA")) {
				miner.setFlag(PCmo_EntityMiner.lavaFillingEnabled, flag);
			}else if (capname.equals("WATER")) {
				miner.setFlag(PCmo_EntityMiner.waterFillingEnabled, flag);
			}else if (capname.equals("TUNNEL")) {
				miner.setFlag(PCmo_EntityMiner.airFillingEnabled, flag);
			}else{
				throw new WeaselRuntimeException("setOption(): Unknown option " + capname);
			}
		}
    }
    
    public void activateOption(String...args){
    	setOption(true, args);
    }
    
    public void deactivateOption(String...args){
    	setOption(false, args);
    }
    
    public void deactivateAllOptions(){
    	miner.setFlag(PCmo_EntityMiner.airFillingEnabled, false);
		miner.setFlag(PCmo_EntityMiner.bridgeEnabled, false);
		miner.setFlag(PCmo_EntityMiner.compressBlocks, false);
		miner.setFlag(PCmo_EntityMiner.keepAllFuel, false);
		miner.setFlag(PCmo_EntityMiner.lavaFillingEnabled, false);
		miner.setFlag(PCmo_EntityMiner.miningEnabled, false);
		miner.setFlag(PCmo_EntityMiner.torches, false);
		miner.setFlag(PCmo_EntityMiner.torchesOnlyOnFloor, false);
		miner.setFlag(PCmo_EntityMiner.waterFillingEnabled, false);
		miner.setFlag(PCmo_EntityMiner.cobbleMake, false);
    }
	
    public void term_clear(){
    	text = "";
		userInput.clear();
		miner.setInfo("text", text);
    }
	
    public void term_print(String text){
    	addText(text + "\n");
    }
	
    public boolean term_hasInput(){
    	return userInput.size() > 0;
    }
    
    public String term_Input(){
    	if (userInput.size() == 0) return "";
		String s = userInput.get(0);
		userInput.remove(0);
		return s;
    }

    public int posX(){
    	return (int)Math.round(miner.posX);
    }
	
    public int posY(){
    	return (int)Math.round(miner.posY);
    }
    
    public int posZ(){
    	return (int)Math.round(miner.posZ);
    }
    
	public String dirAxis(){
	    int rot = miner.getRotationRounded();
		return rot == 0 ? "x-" : rot == 90 ? "z-" : rot == 180 ? "x+" : "z+";
	}
	
	public String dirCompass(){
		int rot = miner.getRotationRounded();
		return rot == 0 ? "W" : rot == 90 ? "N" : rot == 180 ? "E" : "S";
	}
	
	public int dirAngle(){
		return miner.getRotationRounded();
	}
	
	public int minerLevel(){
		miner.updateLevel();
		return miner.st.level;
	}
	
	public String term_text(){
		return text;
	}
	
	public void term_text(String text){
		this.text = "";
		addText(text);
	}
    
}
