package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_GresTextEditMultiline.Keyword;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.Gres;
import weasel.WeaselEngine;
import weasel.exception.SyntaxError;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginCore extends PCws_WeaselPlugin {
	
	private static final String default_program = 
			"# *** Weasel powered Microcontroller ***\n"+
			"# update() is called when neighbor block changes.\n" +
			"# Use variables F,L,R,B,U,D to access sides.\n" +
			"\n\n"+
			"function update(){\n"+
			"  \n"+
			"}\n";	
	
	/** The Weasel Engine */
	private WeaselEngine weasel = new WeaselEngine(this);
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	private String program = default_program;
	
	public PCws_WeaselPluginCore(){
		connectToNetwork(new PCws_WeaselNetwork());
	}
	
	public PCws_WeaselPluginCore(NBTTagCompound nbttag){
		super(nbttag);
	}
	
	@Override
	public boolean doesProvideFunctionOnEngine(String functionName) {
		return weasel.doesProvideFunction(functionName);
	}

	@Override
	public void callFunctionOnEngine(String functionName, WeaselObject... args) {
		int state = weasel.callFunctionExternal(functionName, (Object[])args);
		if(state == -1 || state == 1) return;	
		externalCallsWaiting.add(new PC_Struct2<String, Object[]>(functionName, args));
	}

	
	
	@Override
	public List<String> getProvidedFunctionNames() {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getProvidedVariableNames() {
		return new ArrayList<String>();
	}

	@Override
	public void update() {
		weasel.run(500);
	}

	@Override
	public void syncWithClient() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getClientMsg(String msg, Object obj) {
		if(msg.equalsIgnoreCase("launch")){
			program = (String)obj;
			try {
				weasel.insertNewProgram(WeaselEngine.compileProgram(program));
			} catch (SyntaxError e) {
				e.printStackTrace();
			}
		}else if(msg.equalsIgnoreCase("restart")){
			weasel.restartProgramClearGlobals();
		}else if(msg.equalsIgnoreCase("stop")){
			weasel.isProgramFinished = true;
		}else{
			super.getClientMsg(msg, obj);
		}
	}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		PCws_TileEntityWeasel te = getTE();
		te.setData("program", program);
		te.setData("keywords", PCws_WeaselHighlightHelper.weasel(this, weasel));
		te.setData("isRunning", false);
		Gres.openGres("WeaselCore", player, getPos().x, getPos().y, getPos().z);
	}	
	
}
