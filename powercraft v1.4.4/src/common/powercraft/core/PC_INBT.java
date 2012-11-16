package powercraft.core;

import net.minecraft.src.NBTTagCompound;

public interface PC_INBT {
	/**
	 * Write this instance into NBT compound tag.<br>
	 * The given tag is entirely yours.
	 * 
	 * @param tag tag to write into
	 * @return the tag it saved into
	 */
	public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);

	/**
	 * Load data from NBT compound tag.<br>
	 * The given tag is usually what you saved using writeToNBT.
	 * 
	 * @param tag the tag to load from
	 * @return the loaded object
	 */
	public abstract PC_INBT readFromNBT(NBTTagCompound tag);
}
