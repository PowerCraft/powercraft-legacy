package powercraft.api.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface PC_INBTSaveable {

	public abstract void loadFromNBT(World world, int x, int y, int z,
			NBTTagCompound nbtTagCompound);

	public abstract void saveToNBT(World world, int x, int y, int z,
			NBTTagCompound nbtTagCompound);

}