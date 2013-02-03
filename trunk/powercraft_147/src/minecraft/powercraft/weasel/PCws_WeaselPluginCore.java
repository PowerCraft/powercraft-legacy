package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_IThreadJob;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_ThreadJob;
import powercraft.management.PC_ThreadManager;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginCore extends PCws_WeaselPlugin implements PCws_IWeaselEngine, PC_IThreadJob {
	
	private static final String default_program = 
			"# *** Weasel powered Microcontroller ***\n"+
			"# onPortChange(port) is called when neighbor of port device changes.\n" +
			"# Use variables F,L,R,B,U,D to access sides.\n" +
			"\n\n"+
			"function onPortChange(port){\n"+
			"  \n"+
			"}\n";	
	
	/** The Weasel Engine */
	private CorePluginProvider defaultProvider;
	private WeaselEngine weasel;
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	private String program = default_program;
	private int sleepTimer = 0;
	private boolean stop;
	
	public PCws_WeaselPluginCore(){
		defaultProvider = new CorePluginProvider();
		defaultProvider.registerMethod("restart", "restartDevice", this);
		defaultProvider.registerMethod("reset", "restartDevice", this);
		defaultProvider.registerMethod("sleep", this);
		defaultProvider.registerMethod("bell", this);
		defaultProvider.registerVariable("front", this);
		defaultProvider.registerVariable("f", "front", this);
		defaultProvider.registerVariable("back", this);
		defaultProvider.registerVariable("b", "back", this);
		defaultProvider.registerVariable("left", this);
		defaultProvider.registerVariable("l", "left", this);
		defaultProvider.registerVariable("right", this);
		defaultProvider.registerVariable("r", "right", this);
		defaultProvider.registerVariable("top", this);
		defaultProvider.registerVariable("up", "top", this);
		defaultProvider.registerVariable("u", "top", this);
		defaultProvider.registerVariable("bottom", this);
		defaultProvider.registerVariable("down", "bottom", this);
		defaultProvider.registerVariable("d", "bottom", this);
		if(ModuleInfo.getModule("Logic")!=null){
			defaultProvider.registerMethod("world.isDay", "isDay", this);
			defaultProvider.registerMethod("world.isNight", "isNight", this);
			defaultProvider.registerMethod("world.isRaining", "isRaining", this);
			defaultProvider.registerMethod("world.isThundering", "isThundering", this);
		}
		weasel = new WeaselEngine(defaultProvider);
	}
	
	@Override
	public WeaselFunctionManager makePluginProvider(){
		WeaselFunctionManager fp = new WeaselFunctionManager();
		fp.registerMethod("restart", "restartDevice", this);
		fp.registerMethod("reset", "restartDevice", this);
		fp.registerMethod("sleep", this);
		fp.registerMethod("bell", this);
		fp.registerVariable("front", this);
		fp.registerVariable("f", "front", this);
		fp.registerVariable("back", this);
		fp.registerVariable("b", "back", this);
		fp.registerVariable("left", this);
		fp.registerVariable("l", "left", this);
		fp.registerVariable("right", this);
		fp.registerVariable("r", "right", this);
		fp.registerVariable("top", this);
		fp.registerVariable("up", "top", this);
		fp.registerVariable("u", "top", this);
		fp.registerVariable("bottom", this);
		fp.registerVariable("down", "bottom", this);
		fp.registerVariable("d", "bottom", this);
		return fp;
	}
	
	@Override
	protected PCws_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		program = tag.getString("program");
		SaveHandler.loadFromNBT(tag, "engine", weasel);
		sleepTimer = tag.getInteger("sleep");
		stop = tag.getBoolean("stop");
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		tag.setString("program", program);
		SaveHandler.saveToNBT(tag, "engine", weasel);
		tag.setInteger("sleep", sleepTimer);
		tag.setBoolean("stop", stop);
		return tag;
	}

	@Override
	public boolean doesProvideFunctionOnEngine(String functionName) {
		return weasel.instructionList.hasFunctionForExternalCall(functionName);
	}

	@Override
	public void callFunctionOnEngine(String functionName, WeaselObject... args) {
		if(hasError()||stop)
			return;
		if(sleepTimer<=0){
			try{
				int state = weasel.callFunctionExternal(functionName, (Object[])args);
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

	@Override
	public void update() {
		if(externalCallsWaiting==null)
			externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
		if(sleepTimer<=0){
			if(!hasError() && !stop){
				PC_ThreadManager.addJob(new PC_ThreadJob(this));
			}
		}else{
			sleepTimer--;
		}
		PCws_TileEntityWeasel te = getTE();
		if(te!=null){
			if(te.getData("stackSize")==null || (Integer)te.getData("stackSize") != weasel.dataStack.get().size() + weasel.systemStack.get().size())
				te.setData("stackSize", weasel.dataStack.get().size() + weasel.systemStack.get().size());
			if(te.getData("variableCount")==null || (Integer)te.getData("variableCount") != weasel.variables.get().size() + weasel.globals.get().size())
				te.setData("variableCount", weasel.variables.get().size() + weasel.globals.get().size());
		}
		needsSave();
	}

	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {
		tileEntityWeasel.setData("stackSize", weasel.dataStack.get().size() + weasel.systemStack.get().size());
		tileEntityWeasel.setData("variableCount", weasel.variables.get().size() + weasel.globals.get().size());
		if(getNetwork()==null){
			tileEntityWeasel.setData("networkMemberCount", 0);
		}else{
			tileEntityWeasel.setData("networkMemberCount", getNetwork().size());
		}
		tileEntityWeasel.setData("instructionCount", weasel.instructionList.list.size());
	}
	
	@Override
	public void getClientMsg(String msg, Object obj) {
		if(msg.equalsIgnoreCase("launch")){
			restartDevice();
			program = (String)obj;
			try {
				List<Instruction> list = WeaselEngine.compileProgram(program);
				weasel.insertNewProgram(list);
			} catch (SyntaxError e) {
				e.printStackTrace();
			}
		}else if(msg.equalsIgnoreCase("restart")){
			restartDevice();
		}else if(msg.equalsIgnoreCase("stop")){
			stop = true;
			setData("isRunning", false);
		}else{
			super.getClientMsg(msg, obj);
		}
		PCws_TileEntityWeasel te = getTE();
		te.setData("stackSize", weasel.dataStack.get().size() + weasel.systemStack.get().size());
		te.setData("variableCount", weasel.variables.get().size() + weasel.globals.get().size());
		if(getNetwork()==null){
			te.setData("networkMemberCount", 0);
		}else{
			te.setData("networkMemberCount", getNetwork().size());
		}
		te.setData("instructionCount", weasel.instructionList.list.size());
		needsSave();
	}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		PCws_TileEntityWeasel te = getTE();
		te.setData("program", program);
		te.setData("keywords", PCws_WeaselHighlightHelper.weasel(defaultProvider, weasel));
		te.setData("isRunning", !stop);
		te.setData("stackSize", weasel.dataStack.get().size() + weasel.systemStack.get().size());
		te.setData("variableCount", weasel.variables.get().size() + weasel.globals.get().size());
		if(getNetwork()==null){
			te.setData("networkMemberCount", 0);
		}else{
			te.setData("networkMemberCount", getNetwork().size());
		}
		te.setData("instructionCount", weasel.instructionList.list.size());
		Gres.openGres("WeaselCore", player, getTE());
	}
	
	//Weasel-Functions START

	@Override
	public void restart() {
		externalCallsWaiting.clear();
		sleepTimer = 0;
		stop = false;
		setData("isRunning", true);
		weasel.restartProgramClearGlobals();
	}
	
	public void sleep(WeaselEngine engine){
		sleep(engine, 1);
	}
	
	public void sleep(WeaselEngine engine, int length){
		sleepTimer = length;
		engine.requestPause();
	}
	
	public void bell(){
		bell(1);
	}
	
	public void bell(double d){
		if(getTE()!=null){
			getTE().call("bell", d);
		}
	}
	
	public void front(boolean state){
		setOutport(3, state);
	}
	
	public boolean front(){
		return getInport(3);
	}
	
	public void back(boolean state){
		setOutport(0, state);
	}
	
	public boolean back(){
		return getInport(0);
	}
	
	public void left(boolean state){
		setOutport(1, state);
	}
	
	public boolean left(){
		return getInport(1);
	}
	
	public void right(boolean state){
		setOutport(2, state);
	}
	
	public boolean right(){
		return getInport(2);
	}
	
	public void top(boolean state){
		setOutport(4, state);
	}
	
	public boolean top(){
		return getInport(4);
	}
	
	public void bottom(boolean state){
		setOutport(5, state);
	}
	
	public boolean bottom(){
		return getInport(5);
	}
	
	public boolean isDay(){
		return getWorld().isDaytime();
	}
	
	public boolean isNight(){
		return !getWorld().isDaytime();
	}
	
	public boolean isRaining(){
		return getWorld().isDaytime();
	}
	
	public boolean isThundering(){
		return getWorld().isThundering();
	}
	
	//Weasel-Functions END
	
	public class CorePluginProvider extends WeaselFunctionManager{
		
		@Override
		public WeaselObject call(WeaselEngine engine, String name, boolean var, WeaselObject... args) throws WeaselRuntimeException {
			try{
				return super.call(engine, name, var, args);
			}catch(WeaselRuntimeException e){
				try{
					return PCws_WeaselManager.getGlobalFunctionManager().call(engine, name, var, args);
				}catch(WeaselRuntimeException e1){
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
	public WeaselEngine getEngine() {
		return weasel;
	}
	
	@Override
	public void doJob() {
		weasel.setStatementsToRun(500);
		try{
			while(weasel.getStatementsToRun()>0 && !weasel.isProgramFinished){
				while(externalCallsWaiting.size()>0) {
					int state = weasel.callFunctionExternal(externalCallsWaiting.get(0).a, externalCallsWaiting.get(0).b);
					
					if(state != 0) {
						externalCallsWaiting.remove(0);
					}
				}
				if(!weasel.run())
					break;
			}
		} catch (WeaselRuntimeException wre) {
			setError(wre.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			setError(e.getMessage());
		}
	}
	
}
