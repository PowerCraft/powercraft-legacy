package powercraft.core;

import net.minecraft.src.NBTTagCompound;

public interface PC_INBT
{
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);

    public abstract PC_INBT readFromNBT(NBTTagCompound tag);
}
