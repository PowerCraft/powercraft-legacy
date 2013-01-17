package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.WeaselEngine;
import weasel.WeaselFunctionProvider;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginCore extends PCws_WeaselPlugin {
	
	private static final String default_program = 
			"# *** Weasel powered Microcontroller ***\n"+
			"# onPortChange(port) is called when neighbor of port device changes.\n" +
			"# Use variables F,L,R,B,U,D to access sides.\n" +
			"\n\n"+
			"function onPortChange(port){\n"+
			"  \n"+
			"}\n";	
	
	/** The Weasel Engine */
	private CorePluginProvider defaultProvider = new CorePluginProvider();
	private WeaselEngine weasel = new WeaselEngine(defaultProvider);
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	private String program = default_program;
	private int sleepTimer = 0;
	private boolean stop;
	
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
				/*if(getNetwork()!=null){
					for(PCws_WeaselPlugin plugin:getNetwork()){
						if(plugin instanceof PCws_WeaselPluginDiskDrive){
							((PCws_WeaselPluginDiskDrive)plugin).getAllLibraryInstructions();
						}
					}
				}*/
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
	
	public class CorePluginProvider extends WeaselFunctionProvider{
		
		@Override
		public WeaselObject call(WeaselEngine engine, String name, WeaselObject... args) throws WeaselRuntimeException {
			try{
				return super.call(engine, name, args);
			}catch(WeaselRuntimeException e){
				if(getNetwork()==null){
					throw e;
				}else{
					return getNetwork().getFunctionHandler().call(engine, name, args);
				}
			}
		}

		@Override
		public boolean doesProvideFunction(String name) {
			if(super.doesProvideFunction(name))
				return true;
			if(getNetwork()==null)
				return false;
			return getNetwork().getFunctionHandler().doesProvideFunction(name);
		}

		@Override
		public List<String> getProvidedFunctionNames() {
			List<String> list = super.getProvidedFunctionNames();
			if(getNetwork()!=null){
				list.addAll(getNetwork().getFunctionHandler().getProvidedFunctionNames());
			}
			return list;
		}
		
	}
	
}
