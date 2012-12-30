package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.WeaselEngine;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCws_WeaselPluginCore extends PCws_WeaselPlugin {
	
	private static final String default_program = 
			"# *** Weasel powered Microcontroller ***\n"+
			"# update() is called when neighbor block changes.\n" +
			"# Use variables F,L,R,B,U,D to access sides.\n" +
			"\n\n"+
			"function onPortChange(port){\n"+
			"  \n"+
			"}\n";	
	
	/** The Weasel Engine */
	private WeaselEngine weasel = new WeaselEngine(this);
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	private String program = default_program;
	private int sleepTimer = 0;
	private boolean stop;
	
	@Override
	protected PCws_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		weasel = new WeaselEngine(this);
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
	protected List<String> getProvidedPluginFunctionNames() {
		List<String> l = new ArrayList<String>();
		l.add("sleep");
		l.add("bell");
		l.add("console.print");
		l.add("network.get");
		l.add("network.set");
		l.add("network.has");
		l.add("network.isConnected");
		l.add("network.getDiskCount");
		l.add("global.get");
		l.add("global.set");
		l.add("global.has");
		l.add("world.isDay");
		l.add("world.isNight");
		l.add("world.isRaining");
		return l;
	}

	@Override
	protected WeaselObject callProvidedPluginFunction(WeaselEngine engine,
			String functionName, WeaselObject[] args) {
		if(functionName.equals("sleep")){
			if(args.length!=0){
				sleepTimer = (Integer) args[0].get();
				if (sleepTimer < 0) sleepTimer = 0;
			}else{
				sleepTimer = 1;
			}
			engine.requestPause();
		}else if(functionName.equals("bell")){
			if(getTE()!=null){
				PC_PacketHandler.setTileEntity(getTE(), "msg", "bell", (functionName.length() * (3 + args.length)) / 24D);
			}
		}else if(functionName.equals("console.print")){
			System.out.println(args[0].toString());
		}else if(functionName.equals("network.get")){
			if(getNetwork()!=null)
				return getNetwork().getLocalVariable((String)args[0].get());
		}else if(functionName.equals("network.set")){
			if(getNetwork()!=null)
				getNetwork().setLocalVariable((String)args[0].get(), args[1]);
		}else if(functionName.equals("network.has")){
			if(getNetwork()==null)
				return new WeaselBoolean(false);
			return new WeaselBoolean(getNetwork().getLocalVariable((String)args[0].get()) != null);
		}else if(functionName.equals("network.isConnected")){
			if(getNetwork()==null)
				return new WeaselBoolean(false);
			return new WeaselBoolean(getNetwork().getMember((String)args[0].get()));
		}else if(functionName.equals("network.getDiskCount")){
			if(getNetwork()==null)
				return new WeaselInteger(0);
			return new WeaselInteger(0);
		}else if(functionName.equals("network.getDiskType")){
			if(getNetwork()==null)
				return new WeaselString("null");
			return new WeaselString("null");
		}else if(functionName.equals("global.get")){
			return PCws_WeaselManager.getGlobalVariable((String)args[0].get());
		}else if(functionName.equals("global.set")){
			PCws_WeaselManager.setGlobalVariable((String)args[0].get(), args[1]);
		}else if(functionName.equals("global.has")){
			return new WeaselBoolean(PCws_WeaselManager.hasGlobalVariable((String)args[0].get()));
		}else if(functionName.equals("world.isDay")){
			return new WeaselBoolean(getWorld().isDaytime());	
		}else if(functionName.equals("world.isNight")){
			return new WeaselBoolean(!getWorld().isDaytime());	
		}else if(functionName.equals("world.isRaining")){
			return new WeaselBoolean(getWorld().isRaining());		
		}else{
			throw new WeaselRuntimeException("Invalid call of function " + functionName);
		}
		return new WeaselNull();
	}

	@Override
	protected List<String> getProvidedPluginVariableNames() {
		List<String> l = new ArrayList<String>();
		l.add("b");
		l.add("back");
		l.add("l");
		l.add("left");
		l.add("r");
		l.add("right");
		l.add("f");
		l.add("front");
		l.add("u");
		l.add("up");
		l.add("top");
		l.add("d");
		l.add("down");
		l.add("bottom");
		return l;
	}

	@Override
	protected void setPluginVariable(String name, Object value) {
		setOutport(portToNum(name), ((WeaselBoolean)value).get());
	}

	@Override
	protected WeaselObject getPluginVariable(String name) {
		return new WeaselBoolean(getInport(portToNum(name)));
	}

	@Override
	public void update() {
		if(sleepTimer<=0){
			if(!hasError() && !stop){
				try{
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
			restartDivice();
			program = (String)obj;
			try {
				weasel.insertNewProgram(WeaselEngine.compileProgram(program));
			} catch (SyntaxError e) {
				e.printStackTrace();
			}
		}else if(msg.equalsIgnoreCase("restart")){
			restartDivice();
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
	}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		PCws_TileEntityWeasel te = getTE();
		te.setData("program", program);
		te.setData("keywords", PCws_WeaselHighlightHelper.weasel(this, weasel));
		te.setData("isRunning", !stop);
		te.setData("stackSize", weasel.dataStack.get().size() + weasel.systemStack.get().size());
		te.setData("variableCount", weasel.variables.get().size() + weasel.globals.get().size());
		if(getNetwork()==null){
			te.setData("networkMemberCount", 0);
		}else{
			te.setData("networkMemberCount", getNetwork().size());
		}
		te.setData("instructionCount", weasel.instructionList.list.size());
		Gres.openGres("WeaselCore", player, getPos().x, getPos().y, getPos().z);
	}

	@Override
	public void restart() {
		externalCallsWaiting.clear();
		sleepTimer = 0;
		stop = false;
		setData("isRunning", true);
		weasel.restartProgramClearGlobals();
	}
	
}
