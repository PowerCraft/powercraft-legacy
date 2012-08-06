package net.minecraft.src;


import java.util.List;
import java.util.Set;


/**
 * Class providing common methods and constants to all kinds of conveyor belts.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCtr_BeltBase {

	/**
	 * Max horizontal speed of an item on a belt. Items going slower are
	 * accelerated.
	 */
	public static final double MAX_HORIZONTAL_SPEED = 0.5F; //0.4D;
	/**
	 * Velocity increment added to item on belt, if it is going slower than
	 * MAX_HORIZONTAL_SPEED
	 */
	public static final double HORIZONTAL_BOOST = 0.14D; //0.11D;
	/** Items this far from belt sides get BORDER_BOOST to stay in the center. */
	public static final double BORDERS = 0.35D;
	/**
	 * Velocity increment added to item when it gets close to a border, in order
	 * to keep it on the belt.
	 */
	public static final double BORDER_BOOST = 0.063D;
	/** belts' height in units. 0.0625F = one pixel in vanilla textures. */
	public static final float HEIGHT = 0.0625F;
	/**
	 * collision box height - must be smaller than HEIGHT to let the items
	 * collide -> get moved
	 */
	public static final float HEIGHT_COLLISION = HEIGHT;// - 0.0125F;
	/**
	 * selection box height. Affects only the wireframe, does not allow better
	 * selection.
	 */
	public static final float HEIGHT_SELECTED = HEIGHT;
	/** How far from start of belt the tiem must be to be stored. */
	public static final float STORAGE_BORDER = 0.5F;
	/** Long storage border */
	public static final float STORAGE_BORDER_LONG = 0.8F;
	/**
	 * Vertical storage border - how high above belt the item must be to get
	 * stored.
	 */
	public static final float STORAGE_BORDER_V = 0.6F;


	/**
	 * Default blockActivated handler for all conveyor types
	 * 
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
		if (stack == null) {
			return false;
		}

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
	 * 
	 * @param player
	 * @return placed meta
	 */
	public static int getPlacedMeta(EntityLiving player) {
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
	 * 
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
	 * 
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


	/**
	 * make chest insertion sound
	 * 
	 * @param world
	 * @param pos
	 */
	public static void soundEffectChest(World world, PC_CoordI pos) {
		if (mod_PCcore.soundsEnabled) {
			world.playSoundEffect(pos.x + 0.5D, pos.y + 0.5D, pos.z + 0.5D, "random.pop", (world.rand.nextFloat() + 0.7F) / 5.0F,
					0.5F + world.rand.nextFloat() * 0.3F);
		}
	}


	/**
	 * make belt sound
	 * 
	 * @param world the world
	 * @param pos belt pos
	 */
	public static void soundEffectBelt(World world, PC_CoordI pos) {
		if (mod_PCcore.soundsEnabled) {
			world.playSoundEffect(pos.x + 0.5D, pos.y + 0.625D, pos.z + 0.5D, "random.wood click", (world.rand.nextFloat() + 0.2F) / 10.0F,
					1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.6F);
		}
	}


	/**
	 * Is belt meta in "active" state?
	 * 
	 * @param meta
	 * @return is active
	 */
	public static boolean isActive(int meta) {
		return meta == getActiveMeta(meta);
	}


	/**
	 * Get unified rotation number form belt's meta.
	 * 
	 * @param meta belt meta
	 * @return the rotation 0-3
	 */
	public static int getRotation(int meta) {
		switch (meta) {
			case 0:
			case 6:
				return 0;
			case 1:
			case 7:
				return 1;
			case 8:
			case 14:
				return 2;
			case 9:
			case 15:
				return 3;
		}
		return 0;
	}


	/**
	 * Check if block is blocked, items can't be pushed into it
	 * 
	 * @param world
	 * @param blockPos
	 * @return is blocked.
	 */
	public static boolean isBlocked(World world, PC_CoordI blockPos) {
		boolean isWall = !world.isAirBlock(blockPos.x, blockPos.y, blockPos.z) && !isTransporterAt(world, blockPos);

		if (isWall) {
			Block block = Block.blocksList[blockPos.getId(world)];
			if (block != null) {
				if (!block.blockMaterial.blocksMovement()) {
					isWall = false;// flower, redstone...
				}
			}
		}
		return isWall;
	}



	// TESTS
	/**
	 * Is there a belt at...?
	 * 
	 * @param world the world
	 * @param pos position to check
	 * @return is a belt
	 */
	public static boolean isConveyorAt(World world, PC_CoordI pos) {
		return PC_BlockUtils.hasFlag(world, pos, "BELT");
	}


	/**
	 * is there belt or lift at...?
	 * 
	 * @param world the world
	 * @param pos position to check
	 * @return is a belt or an elevator
	 */
	public static boolean isTransporterAt(World world, PC_CoordI pos) {
		Set<String> set = PC_BlockUtils.getBlockFlags(world, pos);
		return set.contains("BELT") || set.contains("LIFT");
	}


	/**
	 * Try to store entity item into inventory at this position, if there is
	 * any.
	 * 
	 * @param world the world
	 * @param inventoryPos position to store at
	 * @param entity the entity item
	 * @return stored completely
	 */
	public static boolean storeEntityItemAt(World world, PC_CoordI inventoryPos, EntityItem entity) {
		IInventory inventory = PC_InvUtils.getCompositeInventoryAt(world, inventoryPos);
		if (inventory != null && entity != null && entity.isEntityAlive()) {
			ItemStack stackToStore = entity.item;

			if (stackToStore != null && PC_InvUtils.storeItemInInventory(inventory, stackToStore)) {
				soundEffectChest(world, inventoryPos);
				if (stackToStore.stackSize <= 0) {
					entity.setDead();
					stackToStore.stackSize = 0;
					return true;
				}

			}
		}
		return false;
	}


	// reduce number of entityitems.
	/**
	 * Pack tiems at position to smallest possible number og EntityItems. To
	 * reduce lag.
	 * 
	 * @param world
	 * @param pos
	 */
	public static void packItems(World world, PC_CoordI pos) {
		List<EntityItem> items = world.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class,
				AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1));
		if (items.size() < 5) {
			return;
		}

		// do packing!
		nextItem:
		for (EntityItem item1 : items) {

			if (item1 == null || item1.isDead || item1.item == null) {
				continue nextItem;
			}
			if (item1.item.stackSize < 1) {
				item1.setDead();
				continue nextItem;
			}
			if (item1.item.isItemStackDamageable()) {
				continue nextItem;
			} // damageable.
			if (item1.item.isItemEnchanted()) {
				continue nextItem;
			} // enchanted
			if (!item1.item.isStackable()) {
				continue nextItem;
			} // not stackable.
				// stack stackables up to 255 (byte)

			ItemStack stackTarget = item1.item;

			if (stackTarget.stackSize == stackTarget.getMaxStackSize()) {
				continue nextItem;
			}

			for (EntityItem item2 : items) {

				if (item2.isDead) {
					continue nextItem;
				}
				ItemStack stackAdded = item2.item;

				if (item2 == item1) {
					continue;
				}
				if (stackTarget.isItemEqual(stackAdded)) {

					if (stackTarget.stackSize < stackTarget.getMaxStackSize()) {
						int sizeRemain = stackTarget.getMaxStackSize() - stackTarget.stackSize;
						if (sizeRemain >= stackAdded.stackSize) {
							stackTarget.stackSize += stackAdded.stackSize;
							item2.setDead();
						} else {
							stackTarget.stackSize = stackTarget.getMaxStackSize();
							stackAdded.stackSize -= sizeRemain;
							continue nextItem;
						}
					}
				}
			} // inner for
		}
	}


	/**
	 * Store item into a nearby minecraft. Distance can be up to +-1block
	 * 
	 * @param world the world
	 * @param beltPos belt position the item is on
	 * @param entity the item entity
	 * @return stored completely
	 */
	public static boolean storeItemIntoMinecart(World world, PC_CoordI beltPos, EntityItem entity) {
		List<EntityMinecart> hitList = world.getEntitiesWithinAABB(
				EntityMinecart.class,
				AxisAlignedBB.getBoundingBox(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(1.0D, 1.0D,
						1.0D));

		if (hitList.size() > 0) {
			for (EntityMinecart cart : hitList) {
				if (cart == null || cart.minecartType != 1) {
					continue;
				}

				IInventory inventory = cart;
				if (entity != null && entity.isEntityAlive()) {
					ItemStack stackToStore = entity.item;

					if (stackToStore != null && PC_InvUtils.storeItemInInventory(inventory, stackToStore)) {
						soundEffectChest(world, beltPos);
						if (stackToStore.stackSize <= 0) {
							entity.setDead();
							stackToStore.stackSize = 0;
							return true;
						}
					}
				}

			}
		}

		return false;
	}


	/**
	 * Do special item action. This means: fill bucket with water, fill cauldron
	 * from bucket, fill empty bottle from cauldron.
	 * 
	 * @param world
	 * @param beltPos
	 * @param entity
	 */
	public static void doSpecialItemAction(World world, PC_CoordI beltPos, EntityItem entity) {
		if (entity == null || entity.item == null) {
			return;
		}
		boolean flag = false;
		flag |= entity.item.itemID == Item.bucketWater.shiftedIndex;
		flag |= entity.item.itemID == Item.bucketEmpty.shiftedIndex;
		flag |= entity.item.itemID == Item.glassBottle.shiftedIndex;
		if (!flag) {
			return;
		}

		do {


			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(0, 0, 1), entity)) {
				break;
			}
			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(0, 0, -1), entity)) {
				break;
			}
			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(1, 0, 0), entity)) {
				break;
			}
			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(-1, 0, 0), entity)) {
				break;
			}


			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(0, -1, 1), entity)) {
				break;
			}
			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(0, -1, -1), entity)) {
				break;
			}
			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(1, -1, 0), entity)) {
				break;
			}
			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(-1, -1, 0), entity)) {
				break;
			}


			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(0, 1, 0), entity)) {
				break;
			}
			if (PCtr_BeltBase.doSpecialItemAction_do(world, beltPos.offset(0, -1, 0), entity)) {
				break;
			}

		} while (false);
	}


	private static boolean doSpecialItemAction_do(World world, PC_CoordI pos, EntityItem entity) {

		if (entity.item.itemID == Item.bucketWater.shiftedIndex) {
			if (pos.getId(world) == Block.cauldron.blockID && pos.getMeta(world) < 3) {
				pos.setMeta(world, 3);
				entity.item.itemID = Item.bucketEmpty.shiftedIndex;
				return true;
			}
		}

		if (entity.item.itemID == Item.bucketEmpty.shiftedIndex) {
			if (pos.getId(world) == Block.waterStill.blockID || pos.getId(world) == Block.waterMoving.blockID && pos.getMeta(world) == 0) {
				pos.setBlock(world, 0, 0);
				entity.item.itemID = Item.bucketWater.shiftedIndex;
				return true;
			}
		}

		if (entity.item.itemID == Item.glassBottle.shiftedIndex) {
			if (pos.getId(world) == Block.cauldron.blockID && pos.getId(world) > 0) {
				// decrease water amount
				int meta = pos.getMeta(world);
				pos.setMeta(world, meta - 1);

				EntityItem entity2 = new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Item.potion.shiftedIndex, 1, 0));

				entity2.motionX = entity.motionX;
				entity2.motionY = entity.motionY;
				entity2.motionZ = entity.motionZ;
				entity2.delayBeforeCanPickup = 7;
				world.spawnEntityInWorld(entity2);

				entity.item.stackSize--;

				if (entity.item.stackSize <= 0) {
					entity.item.stackSize = 0;
					entity.setDead();
				}

				return true;
			}
		}

		return false;

	}

	/**
	 * Prevent entity item and XP orb despawning
	 * 
	 * @param world the world
	 * @param pos belt position
	 * @param preventPickup prevent item immediate pickup
	 * @param entity the entity
	 */
	public static void entityPreventDespawning(World world, PC_CoordI pos, boolean preventPickup, Entity entity) {
		if (entity instanceof EntityItem) {
			if (preventPickup) {
				((EntityItem) entity).delayBeforeCanPickup = 7;
			}
			if (((EntityItem) entity).age >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null,
						AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1)).size() < 40) {
					((EntityItem) entity).age = 4000;
				}
			}
		}

		if (entity instanceof EntityXPOrb) {
			if (((EntityXPOrb) entity).xpOrbAge >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null,
						AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1)).size() < 40) {
					((EntityXPOrb) entity).xpOrbAge = 4000;
				}
			}
		}
	}

	/**
	 * Move entity on belt
	 * 
	 * @param world the world
	 * @param pos belt position
	 * @param entity the moved entity
	 * @param bordersEnabled enable border correction (keep items on belt)
	 * @param motionEnabled enable item movement forwards
	 * @param moveDirection the belt's direction, or a direction chosen for this
	 *            entity. Z-, X+, Z+, X-
	 * @param max_horizontal_speed maximal speed on belt, items will be
	 *            accelerated and slowed down to achieve this speed.
	 * @param horizontal_boost velocity increment added when entity reaches
	 *            border, to keep it on the belt.
	 */
	public static void moveEntityOnBelt(World world, PC_CoordI pos, Entity entity, boolean bordersEnabled, boolean motionEnabled, int moveDirection,
			double max_horizontal_speed, double horizontal_boost) {

		// sound effect
		if (motionEnabled && world.rand.nextInt(35) == 0) {
			@SuppressWarnings("rawtypes")
			List list = world.getEntitiesWithinAABBExcludingEntity(entity,
					AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1));
			if (world.rand.nextInt(list.size() + 1) == 0) {
				PCtr_BeltBase.soundEffectBelt(world, pos);
			}
		}

		if (entity instanceof EntityPlayer) {
			if (((EntityPlayer) entity).inventory.armorItemInSlot(0) != null) {
				if (((EntityPlayer) entity).inventory.armorItemInSlot(0).itemID == mod_PCtransport.slimeboots.shiftedIndex) {
					return;
				}
			}
		}

		if (entity instanceof EntityItem) {
			if (entity.motionY > 0.2F) entity.motionY /= 3F;
		}

		// speed limit, stick to belt
		if (entity instanceof EntityItem || entity instanceof EntityXPOrb) {
			if (entity.motionY > 0.2) {
				entity.motionY -= 0.1;
			}
		}

		//unstuck
		if (entity.stepHeight <= 0.15F) entity.stepHeight = 0.25F;

		entity.motionZ = MathHelper.clamp_float((float) entity.motionZ, (float) -max_horizontal_speed, (float) max_horizontal_speed);
		entity.motionX = MathHelper.clamp_float((float) entity.motionX, (float) -max_horizontal_speed, (float) max_horizontal_speed);


		switch (moveDirection) {


			case 0: // Z--
				if (entity.motionZ >= -max_horizontal_speed && motionEnabled) {
					entity.motionZ -= horizontal_boost;
				}
				if (bordersEnabled) {
					if (entity.posX > pos.x + (1D - BORDERS)) {
						entity.motionX -= BORDER_BOOST;
					}

					if (entity.posX < pos.x + BORDERS) {
						entity.motionX += BORDER_BOOST;
					}
				}

				break;

			case 1: // X++
				if (entity.motionX <= max_horizontal_speed && motionEnabled) {
					entity.motionX += horizontal_boost;
				}
				if (bordersEnabled) {
					if (entity.posZ > pos.z + BORDERS) {
						entity.motionZ -= BORDER_BOOST;
					}

					if (entity.posZ < pos.z + (1D - BORDERS)) {
						entity.motionZ += BORDER_BOOST;
					}
				}
				break;

			case 2: // Z++
				if (entity.motionZ <= max_horizontal_speed && motionEnabled) {
					entity.motionZ += horizontal_boost;
				}
				if (bordersEnabled) {
					if (entity.posX > pos.x + (1D - BORDERS)) {
						entity.motionX -= BORDER_BOOST;
					}

					if (entity.posX < pos.x + BORDERS) {
						entity.motionX += BORDER_BOOST;
					}
				}
				break;

			case 3: // X--
				if (entity.motionX >= -max_horizontal_speed && motionEnabled) {
					entity.motionX -= horizontal_boost; /* entity.motionY+=0.1; */
				}
				if (bordersEnabled) {
					if (entity.posZ > pos.z + BORDERS) {
						entity.motionZ -= BORDER_BOOST;
					}

					if (entity.posZ < pos.z + (1D - BORDERS)) {
						entity.motionZ += BORDER_BOOST;
					}
				}
				break;
		}
	}


	/**
	 * Check if entity should be ignored by transporters.
	 * 
	 * @param entity the entity
	 * @return ignored.
	 */
	public static boolean isEntityIgnored(Entity entity) {
		if (entity == null) {
			return true;
		}
		if (!entity.isEntityAlive()) {
			return true;
		}
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
			return true;
		}
		if (entity instanceof EntityDiggingFX) {
			return false;
		}
		if (entity instanceof EntityFX) {
			return true;
		}
		return false;
	}


	/**
	 * Try to store item to all sides
	 * 
	 * @param world the world
	 * @param pos belt / elevator pos
	 * @param entity the entity
	 * @return stored completely.
	 */
	public static boolean storeAllSides(World world, PC_CoordI pos, EntityItem entity) {

		if (storeItemIntoMinecart(world, pos, entity)) {
			return true;
		}

		if (storeEntityItemAt(world, pos.offset(0, 0, -1), entity)) {
			return true;
		}
		if (storeEntityItemAt(world, pos.offset(0, 0, 1), entity)) {
			return true;
		}
		if (storeEntityItemAt(world, pos.offset(-1, 0, 0), entity)) {
			return true;
		}
		if (storeEntityItemAt(world, pos.offset(1, 0, 0), entity)) {
			return true;
		}

		if (storeEntityItemAt(world, pos.offset(0, 1, 0), entity)) {
			return true;
		}

		if (storeEntityItemAt(world, pos.offset(0, -1, 0), entity)) {
			return true;
		}
		return false;
	}


	/**
	 * check if item is beyond storage border.
	 * 
	 * @param world the world
	 * @param rotation belt rotation
	 * @param beltPos belt coord
	 * @param entity the checked item entity
	 * @param border border size 0-1
	 * @return is beyond
	 */
	public static boolean isBeyondStorageBorder(World world, int rotation, PC_CoordI beltPos, Entity entity, float border) {
		switch (rotation) {
			case 0: // Z--
				if (entity.posZ > beltPos.z + 1 - border) {
					return false;
				}
				break;

			case 1: // X++
				if (entity.posX < beltPos.x + border) {
					return false;
				}
				break;

			case 2: // Z++

				if (entity.posZ < beltPos.z + border) {
					return false;
				}

				break;

			case 3: // X--
				if (entity.posX > beltPos.x + 1 - border) {
					return false;
				}
				break;
		}
		return true;
	}


	/**
	 * Create entity item on belt
	 * 
	 * @param world the world
	 * @param invPos position of inventory this item was taken from
	 * @param beltPos the belt pos
	 * @param stack stack to put in the item
	 */
	public static void createEntityItemOnBelt(World world, PC_CoordI invPos, PC_CoordI beltPos, ItemStack stack) {
		EntityItem item = new EntityItem(world, beltPos.x + 0.5D, beltPos.y + 0.3D, beltPos.z + 0.5D, stack);
		item.motionX = 0.0D;
		item.motionY = 0.0D;
		item.motionZ = 0.0D;

		PC_CoordD vector = PC_CoordI.getVector(beltPos, invPos);
		item.posX += 0.43D * vector.x;
		item.posZ += 0.43D * vector.z;

		item.delayBeforeCanPickup = 7;
		world.spawnEntityInWorld(item);
	}


	/**
	 * Store an item nearby this conveyor position
	 * 
	 * @param world the world
	 * @param pos belt position
	 * @param entity the entity to store
	 * @param ignoreStorageBorder ignore storage border?
	 * @return stored completely
	 */
	public static boolean storeNearby(World world, PC_CoordI pos, EntityItem entity, boolean ignoreStorageBorder) {

		if (storeItemIntoMinecart(world, pos, entity)) {
			return true;
		}
		if (!ignoreStorageBorder && entity.posY > pos.y + 1 - STORAGE_BORDER_V) {
			return false;
		}

		int rot = getRotation(pos.getMeta(world));


		if (isBeyondStorageBorder(world, rot, pos, entity, STORAGE_BORDER) || ignoreStorageBorder) {
			if (rot == 0 && storeEntityItemAt(world, pos.offset(0, 0, -1), entity)) {
				return true;
			}
			if (rot == 1 && storeEntityItemAt(world, pos.offset(1, 0, 0), entity)) {
				return true;
			}
			if (rot == 2 && storeEntityItemAt(world, pos.offset(0, 0, 1), entity)) {
				return true;
			}
			if (rot == 3 && storeEntityItemAt(world, pos.offset(-1, 0, 0), entity)) {
				return true;
			}

			if (rot != 0 && rot != 2 && storeEntityItemAt(world, pos.offset(0, 0, -1), entity)) {
				return true;
			}
			if (rot != 1 && rot != 3 && storeEntityItemAt(world, pos.offset(1, 0, 0), entity)) {
				return true;
			}
			if (rot != 2 && rot != 0 && storeEntityItemAt(world, pos.offset(0, 0, 1), entity)) {
				return true;
			}
			if (rot != 3 && rot != 1 && storeEntityItemAt(world, pos.offset(-1, 0, 0), entity)) {
				return true;
			}

			if (storeEntityItemAt(world, pos.offset(0, 1, 0), entity)) {
				return true;
			}

			// store under belt if not roaster.
			if (!PC_BlockUtils.hasFlag(world, pos.offset(0, -1, 0), "ROASTER")) {
				if (storeEntityItemAt(world, pos.offset(0, -1, 0), entity)) {
					return true;
				}
			}
		}
		return false;
	}
}
