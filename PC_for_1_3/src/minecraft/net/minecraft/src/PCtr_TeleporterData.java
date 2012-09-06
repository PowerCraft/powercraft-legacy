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
	
	public PCtr_TeleporterData(){
		System.out.println("new PCtr_TeleporterData");
		PC_Utils.registerDataMemory(this);
		PCtr_TeleporterHelper.teleporterData.add(this);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		PC_Utils.saveToNBT(tag, "pos", pos);
		tag.setString("name", name);
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		System.out.println("Load Data");
		PC_Utils.loadFromNBT(tag, "pos", pos);
		name = tag.getString("name");
		PCtr_TileEntityTeleporter tet = PCtr_TeleporterHelper.getTeleporterAt(pos.x, pos.y, pos.z);
		if(tet!=null)
			tet.td = this;
		return this;
	}

	public void remove(){
		PCtr_TeleporterHelper.teleporter.remove(this);
		PC_Utils.unRegisterDataMemory(this);
	}
	
}
