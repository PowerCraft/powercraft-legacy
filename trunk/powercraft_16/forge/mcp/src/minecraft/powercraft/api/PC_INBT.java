/**
 * 
 */
package powercraft.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * Indicates that this Object can be saved to an NBTTagCompound
 * 
 * YOU HAVE TO MAKE THE CONSTRUCTOR(NBTTagCompound) to load the object again
 * 
 * @author XOR
 *
 */
public interface PC_INBT {

	/**
	 * 
	 * function to save this object
	 * 
	 * @param tag save the data into this
	 */
	void saveToNBT(NBTTagCompound tag);

	
	
}
