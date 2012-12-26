package powercraft.management;

import net.minecraft.src.NBTTagCompound;

public interface PC_INBT {

	public void readFromNBT(NBTTagCompound nbttag);
	public void writeToNBT(NBTTagCompound nbttag);

}
