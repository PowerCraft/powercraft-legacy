package powercraft.api;

import net.minecraft.src.NBTTagCompound;

public interface PC_IDataHandler {

	public void load(NBTTagCompound nbtTag);

	public NBTTagCompound save(NBTTagCompound nbtTag);

	public boolean needSave();

	public void reset();

}
