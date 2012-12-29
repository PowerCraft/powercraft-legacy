package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.Gres;
import weasel.WeaselEngine;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginCore extends PCws_WeaselPlugin {
	
	/** The Weasel Engine */
	private WeaselEngine weasel = new WeaselEngine(this);
	private List<PC_Struct2<String, Object[]>> externalCallsWaiting = new ArrayList<PC_Struct2<String,Object[]>>();
	
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
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void syncWithClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		Gres.openGres("WeaselCore", player, getPos().x, getPos().y, getPos().z);
	}	
	
}
