package powercraft.core;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public interface PC_IActivatorListener {

	/**
	 * Listener for activation crystal's onItemUse method.
	 * 
	 * @param stack itemstack of the activator
	 * @param player the player who clicked it
	 * @param world current world
	 * @param pos click position
	 * @return true if something happened and other listeners shouldn't be
	 *         called.
	 */
	public abstract boolean onActivatorUsedOnBlock(ItemStack stack, EntityPlayer player, World world, PC_CoordI pos);
	
}
