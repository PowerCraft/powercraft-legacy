package powercraft.management;

import net.minecraft.nbt.NBTTagCompound;

public interface PC_IDataHandler {

	public void load(NBTTagCompound nbtTag);
	public NBTTagCompound save(NBTTagCompound nbtTag);
	public boolean needSave();
	public void reset();
	
}
