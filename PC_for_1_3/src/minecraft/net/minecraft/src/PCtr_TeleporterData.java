package net.minecraft.src;

public class PCtr_TeleporterData implements PC_INBT {

	public PC_CoordI pos;
	public String defaultTarget, name;

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
		PC_Utils.loadFromNBT(tag, "pos", pos);
		
		PCtr_TileEntityTeleporter tet = (PCtr_TileEntityTeleporter)PC_Utils.mc().theWorld.getBlockTileEntity(pos.x, pos.y, pos.z);
		if(tet!=null)
			tet.td = this;
		return this;
	}

}
