package net.minecraft.src;

/**
 * Class providing common methods and constants to all kinds of conveyor belts.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PCtr_BeltBase {

	/** Max horizontal speed of an item on a belt. Items going slower are accelerated. */
	public static final double MAX_HORIZONTAL_SPEED = 0.4D;
	/** Velocity increment added to item on belt, if it is going slower than MAX_HORIZONTAL_SPEED */
	public static final double HORIZONTAL_BOOST = 0.11D;
	/** Items this far from belt sides get BORDER_BOOST to stay in the center. */
	public static final double BORDERS = 0.35D;
	/** Velocity increment added to item when it gets close to a border, in order to keep it on the belt. */
	public static final double BORDER_BOOST = 0.063D;
	
	/** belts' height in units. 0.0625F = one pixel in vanilla textures. */
	public static final float HEIGHT = 0.0625F;
	/** collision box height - must be smaller than HEIGHT to let the items collide -> get moved */
	public static final float HEIGHT_COLLISION = HEIGHT;// - 0.0125F;
	/** selection box height. Affects only the wireframe, does not allow better selection. */
	public static final float HEIGHT_SELECTED = HEIGHT;
	

	public static final float STORAGE_BORDER = 0.5F;
	
	public static final float STORAGE_BORDER_LONG = 0.8F;
	
	/** Vertical storage border - how high above belt the item must be to get stored. */
	public static final float STORAGE_BORDER_V = 0.6F;
	
	
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


	public static void soundEffectChest(World world, PC_CoordI pos) {
		if (mod_PCcore.soundsEnabled) {
			world.playSoundEffect(pos.x + 0.5D, pos.y + 0.5D, pos.z + 0.5D, "random.pop", (world.rand.nextFloat() + 0.7F) / 5.0F,
					0.5F + world.rand.nextFloat() * 0.3F);
		}
	}


	public static void soundEffectBelt(World world, PC_CoordI pos) {
		if (mod_PCcore.soundsEnabled) {
			world.playSoundEffect(pos.x + 0.5D, pos.y + 0.625D, pos.z + 0.5D, "random.wood click", (world.rand.nextFloat() + 0.2F) / 10.0F,
					1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.6F);
		}
	}


	public static boolean isActive(int meta) {
		return meta == getActiveMeta(meta);
	}
}
