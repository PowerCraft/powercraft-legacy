package powercraft.weasel;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_INBT;

public class PCws_WeaselPlugin implements PC_INBT<PCws_WeaselPlugin> {

	private int id;
	private int networkID;
	
	protected PCws_WeaselPlugin(){
		id = PCws_WeaselManager.registerPlugin(this);
	}
	
	@Override
	public PCws_WeaselPlugin readFromNBT(NBTTagCompound nbttag) {
		PCws_WeaselManager.removePlugin(this);
		id = nbttag.getInteger("id");
		PCws_WeaselManager.registerPlugin(this, id);
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setInteger("id", id);
		return nbttag;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void removeFormNetwork() {
		// TODO Auto-generated method stub
		
	}

}
