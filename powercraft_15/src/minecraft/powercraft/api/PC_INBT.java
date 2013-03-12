package powercraft.api;

import net.minecraft.nbt.NBTTagCompound;

public interface PC_INBT<t extends PC_INBT> {

	public t readFromNBT(NBTTagCompound nbttag);

	public NBTTagCompound writeToNBT(NBTTagCompound nbttag);

}
