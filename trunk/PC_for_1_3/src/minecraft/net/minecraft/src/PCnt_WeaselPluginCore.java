package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.src.PClo_RadioBus.IRadioDevice;
import net.minecraft.src.PCnt_WeaselManager.NetworkMember;
import net.minecraft.src.PCnt_WeaselManager.WeaselNetwork;
import weasel.Calc;
import weasel.IWeaselHardware;
import weasel.WeaselEngine;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.lang.InstructionFunction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


/**
 * The weasel CPU
 * 
 * @author MightyPork
 */
public class PCnt_WeaselPluginCore extends PCnt_WeaselPlugin implements IWeaselHardware, IRadioDevice {

	private boolean connectedToRadioBus = false;
	protected int sleepTimer = 0;	

	private int initialSleep = 5;

	/**
	 * Set this flag to pause weasel execution.
	 */
	public boolean paused = true;

	/**
	 * Flag set if this device is halted.
	 */
	public boolean halted = false;

	private WeaselNetwork providedNetwork = null;

	/**
	 * Rename the network, and reconnect all members.
	 * 
	 * @param newName new network name.
	 */
	public void renameNetwork(String newName) {
		if (isMaster() && providedNetwork != null) {
			getNetManager().renameNetwork(getNetworkName(), newName);
		}
	}

	//@formatter:off	
	private static final String default_program = 
			"# *** Weasel powered Microcontroller ***\n"+
			"# update() is called when neighbor block changes.\n" +
			"# Use variables F,L,R,B,U,D to access sides.\n" +
			"\n\n"+
			"function update(){\n"+
			"  \n"+
			"}\n";	
	//@formatter:on


	/** Weasel's program source code */
	public String program = default_program;

	/** The Weasel Engine */
	private WeaselEngine weasel = null;

	/** Error in the weasel execution */
	private String weaselError = null;



	/**
	 * CORE plugin
	 * 
	 * @param tew weasel tile entity
	 */
	public PCnt_WeaselPluginCore(PCnt_TileEntityWeasel tew) {
		super(tew);
		if(isMaster()) setMemberName("CORE");
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.CORE;
	}

	@Override
	public boolean onClick(EntityPlayer player) {
		if (player.isSneaking()) {
			//TODO PC_Utils.openGres(player, new PCnt_GuiWeaselCoreProgramBig(this));
		} else {
			//TODO PC_Utils.openGres(player, new PCnt_GuiWeaselCoreProgram(this));
		}
		return true;
	}

	@Override
	public boolean updateTick() {

		if (!connectedToRadioBus) {
			getRadioManager().connectToRedstoneBus(this);
		}

		if (isMaster() && providedNetwork == null) {

			// do not call setNetworkName, it doesn't exist yet and setNetworkName tries to connect
			String netname = Calc.generateUniqueName();

			providedNetwork = getNetManager().createNetwork(netname);
			setNetworkNameAndConnect(netname);

		}


		if (initialSleep > 0) {
			initialSleep--;
			return false;
		}


		if (weaselError == null) {
			initWeaselIfNull();
			if (!weasel.isProgramFinished && !paused && !halted) {

				if (sleepTimer > 0) {
					sleepTimer--;
					return false;
				}

				try {
					initWeaselIfNull();
					
					if(externalCallsWaiting.size()>0) {
						int state = weasel.callFunctionExternal(externalCallsWaiting.get(0).a, externalCallsWaiting.get(0).b);
						
						if(state != 0) {
							externalCallsWaiting.remove(0);
						}
					}
					
					weasel.run(500);
				} catch (WeaselRuntimeException wre) {
					setError(wre.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					setError(e.getMessage());
				}
				return true;
			}
		}


		return false;
	}

	private void setError(String message) {
		this.weaselError = message;
	}
	
	private ArrayList<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();

	@Override
	public void onRedstoneSignalChanged() {
		callFunctionOnEngine("update", new Object[] {});
	}

	@Override
	public String getError() {
		return this.weaselError;
	}

	/**
	 * @return true if has error
	 */
	@Override
	public boolean hasError() {
		return weaselError != null;
	}


	@Override
	protected PCnt_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		program = tag.getString("program");

		if (program.equals("")) program = default_program;
		
		weasel = (WeaselEngine) PC_Utils.loadFromNBT(tag, "Weasel", new WeaselEngine(this));

		weaselError = tag.getString("weaselError");
		if (weaselError.equals("")) weaselError = null;

		NBTTagList list = tag.getTagList("wradio");

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound ct = (NBTTagCompound) list.tagAt(i);
			weaselRadioSignals.put(ct.getString("C"), ct.getBoolean("S"));
		}

		paused = tag.getBoolean("Paused");
		halted = tag.getBoolean("Halted");
		sleepTimer = tag.getInteger("Sleep") + 3;
		
		if(isMaster()) {
			NBTTagCompound networkTag = tag.getCompoundTag("NetworkData");
			// network name was already read by superclass.
			getNetManager().registerNetwork(getNetworkName(), providedNetwork = new WeaselNetwork().readFromNBT(networkTag));
			registerToNetwork();
		}
		
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("program", program);
		tag.setString("weaselError", (weaselError == null ? "" : weaselError));
		initWeaselIfNull();
		
		PC_Utils.saveToNBT(tag, "Weasel", weasel);

		//direct radio signals			
		NBTTagList list = new NBTTagList();
		for (Entry<String, Boolean> entry : weaselRadioSignals.entrySet()) {
			NBTTagCompound ct = new NBTTagCompound();
			ct.setString("C", entry.getKey());
			ct.setBoolean("S", entry.getValue());
			list.appendTag(ct);
		}

		tag.setTag("wradio", list);

		tag.setBoolean("Paused", paused);
		tag.setBoolean("Halted", halted);
		tag.setInteger("Sleep", sleepTimer);		

		// network name will be saved by superclass		
		if (isMaster() && providedNetwork != null) {
			PC_Utils.saveToNBT(tag, "NetworkData", providedNetwork);
		}

		return tag;
	}

	@Override
	public boolean doesTransmitOnChannel(String channel) {
		return weaselRadioSignals.containsKey(channel) && weaselRadioSignals.get(channel) == true;
	}

	/**
	 * Save the programmable gate's source code in this tile entity.
	 * 
	 * @param program the source code
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	/**
	 * if weasel is null, create it.
	 */
	protected void initWeaselIfNull() {
		if (weasel == null) {

			weasel = new WeaselEngine(this);
			try {

				if (program.equals("")) {
					program = default_program;
				}

				setAndStartNewProgram(program);

			} catch (SyntaxError e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Set program to the engine and start it.
	 * 
	 * @param program the source code
	 * @throws SyntaxError
	 */
	public void setAndStartNewProgram(String program) throws SyntaxError {
		setProgram(program);

		PC_Logger.finest("\n## Launching new program in Weasel.");

		initWeaselIfNull();

		restartAllNetworkDevices();
		restartDevice();
		List<Instruction> list = WeaselEngine.compileProgram(program);
		
		if (getNetwork() != null) {
			for (NetworkMember member : getNetwork().getMembers().values()) {
				if (member != null && member != this && member instanceof PCnt_WeaselPluginDiskDrive) {
					List<Instruction> lib = ((PCnt_WeaselPluginDiskDrive)member).getAllLibraryInstructions();
					if(lib.size()>0) {
						list.addAll(lib);
					}
				}
			}
		}
		
		weasel.insertNewProgram(list);
		halted = false;
		paused = false;
		weaselError = null;


//		try {
//			if (world() != null) weasel.run(500);
//		} catch (WeaselRuntimeException e) {
//			weaselError = e.getMessage();
//		}

	}

	/**
	 * Force restart program, but preserve source and instruction list.
	 */
	public void restartAllNetworkDevices() {
		
		if (!isMaster() || getNetwork() == null) {
			restartDevice();
			return;
		}

		for (NetworkMember member : getNetwork().getMembers().values()) {
			if (member != null && member != this && member instanceof PCnt_WeaselPlugin) ((PCnt_WeaselPlugin) member).restartDevice();
		}

	}

	/**
	 * Stop program, clear outputs, prepare for new program execution.
	 */
	public void stopProgram() {
		restartAllNetworkDevices();
		halted = true;
		paused = false;
	}

	/**
	 * Check program for errors.
	 * 
	 * @param program the source code
	 * @throws SyntaxError
	 */
	public void checkProgramForErrors(String program) throws SyntaxError {
		List<Instruction> list = WeaselEngine.compileProgram(program);
		System.out.println("## Program check.");
		System.out.println();
		for (Instruction i : list) {
			System.out.println(i);
		}
	}


	private Map<String, Boolean> weaselRadioSignals = new HashMap<String, Boolean>(0);

	private static Random rand = new Random();


	@Override
	public boolean doesProvideFunction(String functionName) {

		// functions from the network are already added by getProvidedFunctionNames
		if (getProvidedFunctionNames().contains(functionName)) return true;

		return false;

	}

	private class BadFunc extends Exception {}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {

		if (halted || paused) return null;
		
		boolean nonet = false;
		if(functionName.startsWith(getName()) && functionName.contains(".")) {
			try {
				functionName = functionName.substring(functionName.indexOf(".")+1);
				nonet = true;
			}catch(Throwable t) {}
		}

		if (functionName.equals("bell")) {
			world().playSoundEffect(coord().x + 0.5D, coord().y + 0.5D, coord().z + 0.5D, "random.orb", 0.8F,
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			world().spawnParticle("note", coord().x + 0.5D, coord().y + 0.3D, coord().z + 0.5D, (functionName.length() * (3 + args.length)) / 24D,
					0.0D, 0.0D);
			return null;
		}


		try {
			return fnAmbient(engine, functionName, args);
		} catch (BadFunc e) {}

		try {
			return fnBus(engine, functionName, args);
		} catch (BadFunc e) {}

		if (functionName.equals("rebuild")) {
			try {
				setAndStartNewProgram(program);
			} catch (SyntaxError e) {
				e.printStackTrace();
				this.weaselError = e.getMessage();
			}
			engine.requestPause();
			return null;

		}
		
		if(!halted && !hasError()) {
			List<Instruction> il = weasel.instructionList.list;
			for(Instruction in: il) {
				if(in instanceof InstructionFunction) {
					if(((InstructionFunction)in).getFunctionName().equals(functionName)) {
						callFunctionOnEngine(functionName, (Object[])args);
						return null;
					}
				}
			}
		}

		WeaselNetwork net = getNetwork();
		if (!nonet && net != null && asker == null) {
			for (NetworkMember member : net.getMembers().values()) {
				if (member == null || member == this || member.getAsker() == member) continue;
				member.setAsker(this);
				if (member.doesProvideFunction(functionName)) {
					WeaselObject obj =  member.callProvidedFunction(engine, functionName, args);
					member.setAsker(null);
					return obj;
				}
				member.setAsker(null);
			}
		}


		throw new WeaselRuntimeException("Invalid call of function " + functionName);
	}

	/**
	 * try to execute a BUS function
	 * 
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnBus(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {

		if (functionName.equals("nset")) {

			getNetManager().setGlobalVariable(Calc.toString(args[0].get()), args[1]);
			return null;

		} else if (functionName.equals("nget")) {

			return getNetManager().getGlobalVariable(Calc.toString(args[0].get()));

		} else if (functionName.equals("nhas")) {

			return new WeaselBoolean(getNetManager().hasGlobalVariable(Calc.toString(args[0].get())));

		} else if (functionName.equals("rx")) {
			//receive
			return new WeaselBoolean(mod_PClogic.RADIO.getChannelState(Calc.toString(args[0].get())));

		} else if (functionName.equals("tx")) {
			//send
			weaselRadioSignals.put((String) args[0].get(), Calc.toBoolean(args[1].get()));
			return null;

		} else if (functionName.equals("isConnected")) {
			
			return new WeaselBoolean(getNetwork().getMembers().containsKey(Calc.toString(args[0].get())));
			
		} else if (functionName.equals("hasDisk")) {
			boolean found = false;
			fl: for (NetworkMember member : getNetwork().getMembers().values()) {
				if (member != null && member != this && member instanceof PCnt_WeaselPluginDiskDrive) {
					if(((PCnt_WeaselPluginDiskDrive)member).getDiskNames().contains(Calc.toString(args[0].get()))){
						found = true;
						break fl;
					}
				}
			}
			return new WeaselBoolean(found);
			
		} else if (functionName.equals("diskType")) {
			String type = null;
			fl: for (NetworkMember member : getNetwork().getMembers().values()) {
				if (member != null && member != this && member instanceof PCnt_WeaselPluginDiskDrive) {
					if(((PCnt_WeaselPluginDiskDrive)member).getDiskNames().contains(Calc.toString(args[0].get()))){
						type = ((PCnt_WeaselPluginDiskDrive)member).getDiskType(Calc.toString(args[0].get()));
						break fl;
					}
				}
			}
			if(type == null) type = "null";
			
			return new WeaselString(type);
			
		} else if (functionName.equals("libCanDo")) {
			boolean has = false;
			for (NetworkMember member : getNetwork().getMembers().values()) {
				//if(member != null && member instanceof PClo_WeaselPlugin && ((PClo_WeaselPlugin) member).isMaster()) continue;
				if (member != null && member != this && member instanceof PCnt_WeaselPluginDiskDrive) {
					has |= ((PCnt_WeaselPluginDiskDrive)member).hasDiskLibFunction(Calc.toString(args[0].get()));
				}
			}
			
			return new WeaselBoolean(has);			
		}

		throw new BadFunc();

	}



	/**
	 * Try to execute a function which works with the environment or surrounding
	 * blocks.
	 * 
	 * @param engine the engine
	 * @param functionName function name
	 * @param args arguments given
	 * @return return value
	 * @throws BadFunc not supported by this method
	 */
	private WeaselObject fnAmbient(WeaselEngine engine, String functionName, WeaselObject[] args) throws BadFunc {

		if (functionName.equals("empty")) {
			return this.chestEmptyTest(args);
		} else if (functionName.equals("full")) {
			return this.chestFullTest(args);

		} else if (functionName.equals("sleep")) {
			if (args.length == 0) {
				sleepTimer = 1;
				engine.requestPause();
				return null;
			}

			sleepTimer = (Integer) args[0].get();
			if (sleepTimer < 0) sleepTimer = 0;
			engine.requestPause();
			return null;

		} else if (functionName.equals("isDay")) {
			return new WeaselBoolean(world().isDaytime());
		} else if (functionName.equals("idNight")) {
			return new WeaselBoolean(!world().isDaytime());
		} else if (functionName.equals("isRaining")) {
			return new WeaselBoolean(world().isRaining());

		} else {
			throw new BadFunc();
		}

	}



	@Override
	public WeaselObject getVariable(String name) {
		
		boolean nonet = false;
		if(name.startsWith(getName()) && name.contains(".")) {
			try {
				name = name.substring(name.indexOf(".")+1);
				nonet = true;
			}catch(Throwable t) {}
		}

		if (halted || paused) return null;
		
		WeaselObject obj = null;

		if (name.equals("B") || name.equals("back")) {
			obj = new WeaselBoolean(getInport("B"));
		} else if (name.equals("L") || name.equals("left")) {
			obj = new WeaselBoolean(getInport("L"));
		} else if (name.equals("R") || name.equals("right")) {
			obj = new WeaselBoolean(getInport("R"));
		} else if (name.equals("F") || name.equals("front")) {
			obj = new WeaselBoolean(getInport("F"));
		} else if (name.equals("U") || name.equals("up") || name.equals("top")) {
			obj = new WeaselBoolean(getInport("U"));
		} else if (name.equals("D") || name.equals("down") || name.equals("bottom")) {
			obj = new WeaselBoolean(getInport("D"));
		} else if(!nonet && asker == null){
			WeaselNetwork net = getNetwork();
			if (net != null) {
				for (NetworkMember member : net.getMembers().values()) {
					if (member == null || member == this || member == asker || member.getAsker() == this) continue;
					//if(member instanceof PClo_WeaselPlugin && ((PClo_WeaselPlugin) member).isMaster()) continue;
					member.setAsker(this);
					obj = member.getVariable(name);
					member.setAsker(null);
					if (obj != null) break;
				}
			}
		}

		return obj;
	}

	@Override
	public void setVariable(String name, Object object) {
		
		boolean nonet = false;
		if(name.startsWith(getName()) && name.contains(".")) {
			try {
				name = name.substring(name.indexOf(".")+1);
				nonet = true;
			}catch(Throwable t) {}
		}

		if (halted || paused) return;

		boolean setval = Calc.toBoolean(object);

		if (name.equals("B") || name.equals("back")) {
			setOutport("B", setval);
		} else if (name.equals("L") || name.equals("left")) {
			setOutport("L", setval);
		} else if (name.equals("R") || name.equals("right")) {
			setOutport("R", setval);
		} else if (name.equals("F") || name.equals("front")) {
			setOutport("F", setval);
		} else if (name.equals("U") || name.equals("up") || name.equals("top")) {
			setOutport("U", setval);
		} else if (name.equals("D") || name.equals("down") || name.equals("bottom")) {
			setOutport("D", setval);
		} else if(!nonet && asker == null) {
			WeaselNetwork net = getNetwork();
			if (net != null) {
				for (NetworkMember member : net.getMembers().values()) {
					if (member == null || member == this || member == asker || member.getAsker() == member || member.getAsker() == this) continue;
					//if(member instanceof PClo_WeaselPlugin && ((PClo_WeaselPlugin) member).isMaster()) continue;
					member.setAsker(this);
					if (member.getVariable(name) != null) {
						member.setVariable(name, object);
						member.setAsker(null);
						break;
					}
					member.setAsker(null);
				}
			}
		}

	}

	@Override
	public List<String> getProvidedFunctionNames() {
		
		if(asker == this) return new ArrayList<String>();
		
		//if(isMaster()) System.out.println("master, my name is "+getName());		
		List<String> list = new ArrayList<String>();

		list.add("bell");

		list.add("isDay");
		list.add("isNight");

		list.add("isRaining");
		list.add("empty");
		list.add("full");
		list.add("sleep");

		list.add("nget");
		list.add("nset");
		list.add("nhas");
		
		list.add("rebuild");
		
		list.add("isConnected");
		list.add("hasDisk");
		list.add("diskType");
		list.add("libCanDo");

		list.add("rx");
		list.add("tx");
		
		List<String> secondary = new ArrayList<String>();
		for(String s: list) {
			secondary.add(getName()+"."+s);
		}
		list.addAll(secondary);
		
		if(!halted && !hasError()) {
			List<Instruction> il = weasel.instructionList.list;
			for(Instruction in: il) {
				if(in instanceof InstructionFunction) {
					list.add(getName()+"."+((InstructionFunction)in).getFunctionName());
					//if(isMaster()) System.out.println("Adding function "+getName()+"."+((InstructionFunction)in).getFunctionName());
				}
			}
		}

		WeaselNetwork net = getNetwork();
		if (asker == null && net != null) {
			for (NetworkMember member : net.getMembers().values()) {
				if (member == null || member == this || member == asker || member.getAsker() == member || member.getAsker() == this) continue;
				//if(member instanceof PClo_WeaselPlugin && ((PClo_WeaselPlugin) member).isMaster()) continue;
				
				member.setAsker(this);
				list.addAll(member.getProvidedFunctionNames());
				member.setAsker(null);
			}
		}
		return list;
	}

	@Override
	public List<String> getProvidedVariableNames() {

		if(asker == this) return new ArrayList<String>();

		List<String> list = new ArrayList<String>();
		list.add("B");
		list.add("L");
		list.add("R");
		list.add("F");
		list.add("U");
		list.add("D");

		list.add("back");
		list.add("left");
		list.add("right");
		list.add("front");
		list.add("up");
		list.add("down");
		list.add("top");
		list.add("bottom");
		
		List<String> secondary = new ArrayList<String>();
		for(String s: list) {
			secondary.add(getName()+"."+s);
		}
		list.addAll(secondary);

		WeaselNetwork net = getNetwork();
		if (asker == null && net != null) {
			for (NetworkMember member : net.getMembers().values()) {
				if (member == null || member == this || member == asker || member.getAsker() == member || member.getAsker() == this) continue;
				//if(member instanceof PClo_WeaselPlugin && ((PClo_WeaselPlugin) member).isMaster()) continue;
				member.setAsker(this);
				list.addAll(member.getProvidedVariableNames());
				member.setAsker(null);
			}
		}

		return list;

	}


	@Override
	public void onDeviceDestroyed() {
		getRadioManager().disconnectFromRedstoneBus(this);
		if(isMaster()) getNetManager().destroyNetwork(getNetworkName());
	}

	@Override
	public boolean isMaster() {
		return true;
	}

	@Override
	protected void onNetworkChanged() {
		// impossible, this is the master
	}

	@Override
	public WeaselEngine getWeaselEngine() {
		initWeaselIfNull();
		return weasel;
	}

	@Override
	public void callFunctionOnEngine(String function, Object... args) {
		if (hasError() || paused || halted) {
			return;
		}
		if(initialSleep > 0) {
			externalCallsWaiting.add(new PC_Struct2<String, Object[]>(function, args));
			return;
		}
		try {
			initWeaselIfNull();
			int state = weasel.callFunctionExternal(function, args);
			if(state == -1 || state == 1) return;	
			externalCallsWaiting.add(new PC_Struct2<String, Object[]>(function, args));
			
		} catch (WeaselRuntimeException wre) {
			weaselError = wre.getMessage();
		}
	}

	@Override
	public void restartDevice() {
		initWeaselIfNull();
		weasel.restartProgramClearGlobals();
		weaselError = null;
		weaselRadioSignals.clear();
		paused = false;
		halted = false;
		sleepTimer = 0;
		externalCallsWaiting.clear();
		resetOutport();
	}

	@Override
	public void onBlockPlaced(EntityLiving entityliving) {}

	@Override
	public void onRandomDisplayTick(Random random) {}

}
