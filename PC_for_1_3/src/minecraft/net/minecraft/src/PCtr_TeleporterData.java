package net.minecraft.src;

public class PCtr_TeleporterData implements PC_INBT {

	public PC_CoordI pos = new PC_CoordI();
	public String defaultTarget = null, name = "";

	public boolean lastActiveState = false;

	public boolean items = true;
	public boolean animals = true;
	public boolean monsters = true;
	public boolean players = true;
	public boolean sneakTrigger = false;
	public String direction = "N";
	public boolean hideLabel = false;

	public int dimension;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		PC_Utils.saveToNBT(tag, "pos", pos);
		
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		System.out.println("Load Data");
		PC_Utils.loadFromNBT(tag, "pos", pos);
		return this;
	}

}
