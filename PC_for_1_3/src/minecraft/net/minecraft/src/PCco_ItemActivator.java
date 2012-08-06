package net.minecraft.src;


import java.util.HashSet;


/**
 * Activation Crystal item.<br>
 * This item has various uses: opens spawner GUI, converts blocks to miner and
 * to fishing machine.<br>
 * Community modules can use it for their own uses.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_ItemActivator extends Item {
	/**
	 * @param i ID
	 */
	public PCco_ItemActivator(int i) {
		super(i);
		setMaxDamage(100);
		setMaxStackSize(1);
	}

	@Override
	public boolean tryPlaceIntoWorld(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10) {

		for (PC_IActivatorListener listener : listeners) {

			if (listener.onActivatorUsedOnBlock(itemstack, entityplayer, world, new PC_CoordI(i, j, k))) {
				return true;
			}

		}
		return false;
	}

	private static HashSet<PC_IActivatorListener> listeners = new HashSet<PC_IActivatorListener>();

	/**
	 * Register another listener to this item.<br>
	 * Call the corresponding method on PC_Module to register custom listener.
	 * 
	 * @param listener the listener
	 */
	public static void registerListener(PC_IActivatorListener listener) {
		listeners.add(listener);
	}
}
