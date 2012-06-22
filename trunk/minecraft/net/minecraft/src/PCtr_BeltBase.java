package net.minecraft.src;

public class PCtr_BeltBase {

	public static final double MAX_HORIZONTAL_SPEED = 0.4D;
	public static final double HORIZONTAL_BOOST = 0.11D;
	public static final double BORDERS = 0.35D;
	public static final double BORDER_BOOST = 0.063D;
	
	public static final float HEIGHT = 0.0625F;
	public static final float HEIGHT_COLLISION = HEIGHT - 0.0125F;
	public static final float HEIGHT_SELECTED = HEIGHT;
	
	/**
	 * Default blockActivated handler for all conveyor types
	 * @param world the world
	 * @param i x
	 * @param j y
	 * @param k z
	 * @param entityplayer the player who clicked
	 * @return event consumed
	 */
	public static boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		// minecart placing.
		ItemStack stack = entityplayer.getCurrentEquippedItem();
		if (stack == null) { return false; }

		Item equip_item = stack.getItem();

		if (equip_item instanceof ItemMinecart) {
			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntityMinecart(world, i + 0.5F, j + 0.5F, k + 0.5F, ((ItemMinecart) equip_item).minecartType));
			}
			if (!ModLoader.getMinecraftInstance().playerController.isInCreativeMode()) {
				entityplayer.inventory.decrStackSize(entityplayer.inventory.currentItem, 1);
			}
			return true;
		}
		return false;
	}

	
	/**
	 * Get metadata of a placed covneyor
	 * @param player
	 * @return
	 */
	public static int getPlacedMeta(EntityLiving player){
		int l = MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 2.5D) & 3;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}

		if (l == 2) {
			l = 8;
		}
		if (l == 3) {
			l = 9;
		}
		
		return l;
	}
	
	
	// UTILS
	/**
	 * Modify metadata to mark that belt is "active"
	 * @param meta
	 * @return new meta
	 */
	public static int getActiveMeta(int meta) {
		switch (meta) {
			case 0:
				return 6;
			case 1:
				return 7;
			case 8:
				return 14;
			case 9:
				return 15;
		}
		return meta;
	}
	
	/**
	 * Modify metadata to mark that belt is "passive"
	 * @param meta
	 * @return new meta
	 */
	public static int getPassiveMeta(int meta) {
		switch (meta) {
			case 6:
				return 0;
			case 7:
				return 1;
			case 14:
				return 8;
			case 15:
				return 9;
		}
		return meta;
	}
}
