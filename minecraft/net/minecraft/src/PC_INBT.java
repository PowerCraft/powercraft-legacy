package net.minecraft.src;

/**
 * Interface for classes that can load and save to NBT.<br>
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public interface PC_INBT {
	/**
	 * Write this instance into NBT compound tag.<br>
	 * The given tag is entirely yours.
	 * 
	 * @param tag tag to write into
	 */
	public abstract void writeToNBT(NBTTagCompound tag);

	/**
	 * Load data from NBT compound tag.<br>
	 * The given tag is usually what you saved using writeToNBT.
	 * 
	 * @param tag
	 */
	public abstract void readFromNBT(NBTTagCompound tag);
}
