package net.minecraft.src;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;

public class PCtr_BlockConveyor extends Block implements PC_IBlockType, PC_IRotatedBox, PC_ISwapTerrain, ITextureProvider {
	public PCtr_EnumConv type = PCtr_EnumConv.belt;

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public boolean renderItemHorizontal() {
		return true;
	}

	protected PCtr_BlockConveyor(int i, PCtr_EnumConv type) {
		super(i, new PCtr_MaterialConveyor());
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltBase.HEIGHT, 1.0F);
		blockIndexInTexture = 0;
		this.type = type;

		setStepSound(Block.soundPowderFootstep);
	}

	@Override
	public int tickRate() {
		return 4;
	}

	@Override
	public boolean canProvidePower() {
		return type == PCtr_EnumConv.detector || type == PCtr_EnumConv.ejector || type == PCtr_EnumConv.brake;
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return PCtr_BeltBase.blockActivated(world, i, j, k, entityplayer);
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (type == PCtr_EnumConv.ejector && l > 0) {
			world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = PCtr_BeltBase.getPlacedMeta(entityliving);
		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		int meta = iblockaccess.getBlockMetadata(i, j, k);
		return PCtr_BeltBase.isActive(meta) && type == PCtr_EnumConv.detector;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return type == PCtr_EnumConv.detector && isActive(world, i, j, k) && l == 1;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		
		PC_CoordI pos = new PC_CoordI(i,j,k);
		
		if (type == PCtr_EnumConv.ejector) {
			int meta = pos.getMeta(world);

			if (pos.isPoweredDirectly(world) || pos.isPoweredIndirectly(world)
					|| pos.offset(0,-1,0).isPoweredDirectly(world) || pos.offset(0,-1,0).isPoweredIndirectly(world)) {
				if (!PCtr_BeltBase.isActive(meta)) {
					if (!dispenseStackFromMinecart(world, pos)) {
						tryToDispenseItem(world, pos);
					}
					pos.setMeta(world, PCtr_BeltBase.getActiveMeta(meta));
				}
			} else if (PCtr_BeltBase.isActive(meta)) {
				pos.setMeta(world, PCtr_BeltBase.getPassiveMeta(meta));
			}
		} else if (type == PCtr_EnumConv.detector && isActive(world, i, j, k)) {
			setStateIfEntityInteractsWithDetector(world, i, j, k);
		}
	}


	@SuppressWarnings("rawtypes")
	public void setStateIfEntityInteractsWithDetector(World world, int i, int j, int k) {
		if (type == PCtr_EnumConv.detector) {
			int meta = world.getBlockMetadata(i, j, k);
			boolean isAlreadyActive = PCtr_BeltBase.isActive(meta);
			boolean isPressed = false;
			List list = null;
			list = world
					.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool(i, j, k, (i + 1), j + 1D, (k + 1)));
			if (list.size() > 0) {
				isPressed = true;
			}

			boolean shallNotify = false;
			if (isPressed && !isAlreadyActive) { // turn on
				world.setBlockMetadataWithNotify(i, j, k, PCtr_BeltBase.getActiveMeta(meta));
				shallNotify = true;
			} else if (!isPressed && isAlreadyActive) { // turn off
				world.setBlockMetadataWithNotify(i, j, k, PCtr_BeltBase.getPassiveMeta(meta));
				shallNotify = true;
			}

			if (shallNotify) {
				world.notifyBlocksOfNeighborChange(i, j, k, blockID);
				world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);// below
				world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);// above
				world.markBlocksDirty(i, j, k, i, j, k);
				if (mod_PCcore.soundsEnabled) {
					world.playSoundEffect(i + 0.5D, j + 0.125D, k + 0.5D, "random.click", 0.15F, 0.5F);
				}
			}

			if (isPressed) {
				world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
			}
		}
		return;
	}

	// TESTS
	public static final boolean isConveyorAt(World world, int i, int j, int k) {
		return PC_BlockUtils.hasFlag(world, new PC_CoordI(i, j, k), "BELT");
	}

	public static final boolean isElevatorAt(World world, int i, int j, int k) {
		return PC_BlockUtils.hasFlag(world, new PC_CoordI(i, j, k), "LIFT");
	}

	public static final boolean isConveyorOrElevatorAt(World world, int i, int j, int k) {
		Set<String> set = PC_BlockUtils.getBlockFlags(world, new PC_CoordI(i, j, k));
		return set.contains("BELT") || set.contains("LIFT");
	}

	public boolean isActive(World world, int i, int j, int k) {
		int meta = world.getBlockMetadata(i, j, k);
		return PCtr_BeltBase.isActive(meta);
	}

	// ii jj kk are conveyor's coordinates. ijk is for inventory.
	public static boolean dispenseFromInventoryAt(World world, PC_CoordI inventoryPos, PC_CoordI beltPos) {
		IInventory inventory = PC_InvUtils.getCompositeInventoryAt(world, inventoryPos);
		if (inventory == null) { return false; }
		return dispenseItem(world, inventoryPos, inventory, beltPos);
	}

	public static void tryToDispenseItem(World world, PC_CoordI beltPos) {
		int rot = getRotation_(beltPos.getMeta(world));
		
		// first try the inventory right behind this belt
		if (rot == 2 && dispenseFromInventoryAt(world, beltPos.offset(0,0,-1), beltPos)) { return; }
		if (rot == 3 && dispenseFromInventoryAt(world, beltPos.offset(1,0,0), beltPos)) { return; }
		if (rot == 0 && dispenseFromInventoryAt(world, beltPos.offset(0,0,1), beltPos)) { return; }
		if (rot == 1 && dispenseFromInventoryAt(world, beltPos.offset(-1,0,0), beltPos)) { return; }

		// try all the other sides
		if (rot != 2 && dispenseFromInventoryAt(world, beltPos.offset(0,0,-1), beltPos)) { return; }
		if (rot != 3 && dispenseFromInventoryAt(world, beltPos.offset(1,0,0), beltPos)) { return; }
		if (rot != 0 && dispenseFromInventoryAt(world, beltPos.offset(0,0,1), beltPos)) { return; }
		if (rot != 1 && dispenseFromInventoryAt(world, beltPos.offset(-1,0,0), beltPos)) { return; }
	}



	private static boolean dispenseItem(World world, PC_CoordI invPos, IInventory inventory, PC_CoordI beltPos) {
		ItemStack stack = PC_InvUtils.dispenseFirstStack(inventory);
		
		if(stack != null){
			createEntityItemOnbelt(world, invPos, beltPos, stack);
			return true;
		}
		return false;
	}
	
	private static void createEntityItemOnbelt(World world, PC_CoordI invPos, PC_CoordI beltPos, ItemStack stack){
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

	public static boolean storeEntityItemAt(World world, PC_CoordI inventoryPos, EntityItem entity) {
		IInventory inventory = PC_InvUtils.getCompositeInventoryAt(world, inventoryPos);
		if (inventory != null && entity != null && entity.isEntityAlive()) {
			ItemStack stackToStore = entity.item;

			if (stackToStore != null && PC_InvUtils.storeItemInInventory(inventory, stackToStore)) {
				PCtr_BeltBase.soundEffectChest(world, inventoryPos);
				if (stackToStore.stackSize <= 0) {
					entity.setDead();
					stackToStore.stackSize = 0;
					return true;
				}

			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean storeEntityItemIntoMinecart(World world, PC_CoordI beltPos, EntityItem entity) {
		List<EntityMinecart> hitList = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB
				.getBoundingBoxFromPool(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(1.0D, 1.0D, 1.0D));

		if (hitList.size() > 0) {
			for (EntityMinecart cart : hitList) {
				if (cart.minecartType != 1) {
					continue;
				}

				IInventory inventory = cart;
				if (inventory != null && entity != null && entity.isEntityAlive()) {
					ItemStack stackToStore = entity.item;

					if (stackToStore != null && PC_InvUtils.storeItemInInventory(inventory, stackToStore)) {
						PCtr_BeltBase.soundEffectChest(world, beltPos);
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

	@SuppressWarnings("unchecked")
	public static boolean dispenseStackFromMinecart(World world, PC_CoordI beltPos) {
		List<EntityMinecart> hitList = world.getEntitiesWithinAABB(net.minecraft.src.EntityMinecart.class, AxisAlignedBB
				.getBoundingBoxFromPool(beltPos.x, beltPos.y, beltPos.z, beltPos.x+1, beltPos.y+1, beltPos.z+1).expand(1.0D, 1.0D, 1.0D));

		if (hitList.size() > 0) {
			for (EntityMinecart cart : hitList) {
				if (cart.minecartType != 1) {
					continue;
				}

				IInventory inventory = cart;
				if (inventory != null) {
					if (dispenseItem(world, new PC_CoordD(cart.posX, cart.posY, cart.posZ).round(), inventory, beltPos)) { return true; }
				}

			}
		}

		return false;
	}


	private boolean isBeyondStorageBorder(World world, PC_CoordI beltPos, Entity entity, float border) {
		switch (getRotation_(beltPos.getMeta(world))) {
			case 0: // Z--
				if (entity.posZ > beltPos.z + 1 - border) { return false; }
				break;

			case 1: // X++
				if (entity.posX < beltPos.x + border) { return false; }
				break;

			case 2: // Z++

				if (entity.posZ < beltPos.z + border) { return false; }

				break;

			case 3: // X--
				if (entity.posX > beltPos.x + 1 - border) { return false; }
				break;
		}
		return true;
	}

	public boolean storeNearby(World world, PC_CoordI pos, EntityItem entity) {

		if (storeEntityItemIntoMinecart(world, pos, entity)) { return true; }
		if (entity.posY > pos.y + 1 - PCtr_BeltBase.STORAGE_BORDER_V) { return false; }


		int rot = getRotation(pos.getMeta(world));


		if (isBeyondStorageBorder(world, pos, entity, PCtr_BeltBase.STORAGE_BORDER) || (isPowered(world, pos) && type == PCtr_EnumConv.brake)) {
			if (rot == 0 && storeEntityItemAt(world, pos.offset(0,0,-1), entity)) { return true; }
			if (rot == 1 && storeEntityItemAt(world, pos.offset(1,0,0), entity)) { return true; }
			if (rot == 2 && storeEntityItemAt(world, pos.offset(0,0,1), entity)) { return true; }
			if (rot == 3 && storeEntityItemAt(world, pos.offset(-1,0,0), entity)) { return true; }
			
			if (rot != 0 && rot != 2 && storeEntityItemAt(world, pos.offset(0,0,-1), entity)) { return true; }
			if (rot != 1 && rot != 3 && storeEntityItemAt(world, pos.offset(1,0,0), entity)) { return true; }
			if (rot != 2 && rot != 0 && storeEntityItemAt(world, pos.offset(0,0,1), entity)) { return true; }
			if (rot != 3 && rot != 1 && storeEntityItemAt(world, pos.offset(-1,0,0), entity)) { return true; }

			if (storeEntityItemAt(world, pos.offset(0,1,0), entity)) { return true; }
		
			// store under belt if not roaster.
			if (PC_BlockUtils.hasFlag(world, pos.offset(0,-1,0), "ROASTER")) {
				if (storeEntityItemAt(world, pos.offset(0,-1,0), entity)) { return true; }
			}
		}
		return false;
	}

	public boolean storeAllSides(World world, PC_CoordI pos, EntityItem entity) {

		if (storeEntityItemIntoMinecart(world, pos, entity)) return true;

		if (storeEntityItemAt(world, pos.offset(0,0,-1), entity)) return true;
		if (storeEntityItemAt(world, pos.offset(0,0,1), entity)) return true;
		if (storeEntityItemAt(world, pos.offset(-1,0,0), entity)) return true;
		if (storeEntityItemAt(world, pos.offset(1,0,0), entity)) return true;

		if (storeEntityItemAt(world, pos.offset(0,1,0), entity)) return true;

		if (!PC_BlockUtils.hasFlag(world, pos.offset(0,-1,0), "ROASTER") && storeEntityItemAt(world, pos.offset(0,-1,0), entity)) return true;
		return false;
	}

	// FILL CAULDRON
	private boolean try2fillCauldron(World world, int i, int j, int k) {
		if (world.getBlockId(i, j, k) == Block.cauldron.blockID && world.getBlockMetadata(i, j, k) < 3) {
			world.setBlockMetadataWithNotify(i, j, k, 3);
			return true;
		}
		return false;
	}

	public void fillCauldron(World world, int i, int j, int k, EntityItem entity) {
		if (entity == null || entity.item == null || entity.item.itemID != Item.bucketWater.shiftedIndex) { return; }

		while (true) {
			if (try2fillCauldron(world, i, j, k - 1)) {
				break;
			}
			if (try2fillCauldron(world, i, j, k + 1)) {
				break;
			}
			if (try2fillCauldron(world, i - 1, j, k)) {
				break;
			}
			if (try2fillCauldron(world, i + 1, j, k)) {
				break;
			}
			if (try2fillCauldron(world, i, j - 1, k)) {
				break;
			}
			if (try2fillCauldron(world, i, j + 1, k)) {
				break;
			}
			return;
		}

		entity.item.itemID = Item.bucketEmpty.shiftedIndex;
	}

	// FILL BUCKET
	private boolean try2fillBucket(World world, int i, int j, int k) {
		if (world.getBlockId(i, j, k) == Block.waterStill.blockID || world.getBlockId(i, j, k) == Block.waterMoving.blockID
				&& world.getBlockMetadata(i, j, k) == 0) {
			world.setBlockWithNotify(i, j, k, 0);
			return true;
		}
		return false;
	}

	public void fillBucket(World world, int i, int j, int k, EntityItem entity) {
		if (entity == null || entity.item == null || entity.item.itemID != Item.bucketEmpty.shiftedIndex) { return; }

		while (true) {
			// low sides

			if (try2fillBucket(world, i, j - 1, k - 1)) {
				break;
			}
			if (try2fillBucket(world, i, j - 1, k + 1)) {
				break;
			}
			if (try2fillBucket(world, i - 1, j - 1, k)) {
				break;
			}
			if (try2fillBucket(world, i + 1, j - 1, k)) {
				break;
			}

			// direct sides
			if (try2fillBucket(world, i, j, k - 1)) {
				break;
			}
			if (try2fillBucket(world, i, j, k + 1)) {
				break;
			}
			if (try2fillBucket(world, i - 1, j, k)) {
				break;
			}
			if (try2fillBucket(world, i + 1, j, k)) {
				break;
			}

			// above and below
			if (try2fillBucket(world, i, j - 1, k)) {
				break;
			}
			if (try2fillBucket(world, i, j + 1, k)) {
				break;
			}
			return;
		}

		entity.item.itemID = Item.bucketWater.shiftedIndex;
	}

	// FILL BOTTLE
	private boolean try2fillBottle(World world, int i, int j, int k) {
		if (world.getBlockId(i, j, k) == Block.cauldron.blockID && world.getBlockMetadata(i, j, k) > 0) {
			// decrease water amount
			int meta = world.getBlockMetadata(i, j, k);
			world.setBlockMetadataWithNotify(i, j, k, meta - 1);
			return true;
		}
		return false;
	}

	public void fillBottle(World world, int i, int j, int k, EntityItem entity) {
		if (entity == null || entity.item == null || entity.item.itemID != Item.glassBottle.shiftedIndex) { return; }

		while (true) {
			if (try2fillBottle(world, i, j, k - 1)) {
				break;
			}
			if (try2fillBottle(world, i, j, k + 1)) {
				break;
			}
			if (try2fillBottle(world, i - 1, j, k)) {
				break;
			}
			if (try2fillBottle(world, i + 1, j, k)) {
				break;
			}
			if (try2fillBottle(world, i, j - 1, k)) {
				break;
			}
			if (try2fillBottle(world, i, j + 1, k)) {
				break;
			}
			return;
		}

		// spawn new bottle of water

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
	}

	public static boolean isBlocked(World world, int i, int j, int k) {
		boolean isWall = !world.isAirBlock(i, j, k) && !isConveyorOrElevatorAt(world, i, j, k);

		if (isWall) {
			Block block = Block.blocksList[world.getBlockId(i, j, k)];
			if (block != null) {
				if (!block.blockMaterial.blocksMovement()) {
					isWall = false;// flower, redstone...
				}
			}
		}
		return isWall;
	}

	// from interface, but also used locally
	@Override
	public int getRotation(int meta) {
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

	// static one, for compatibility.
	public static int getRotation_(int meta) {
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

	// reduce number of entityitems.
	@SuppressWarnings("unchecked")
	public static void packItems(World world, PC_CoordI pos) {
		List<EntityItem> items = world.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class,
				AxisAlignedBB.getBoundingBoxFromPool(pos.x, pos.y, pos.z, pos.x+1, pos.y+1, pos.z+1));
		if (items.size() < 5) { return; }

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

	private boolean isPowered(World world, PC_CoordI pos) {
		return pos.isPoweredIndirectly(world) || pos.offset(0,1,0).isPoweredIndirectly(world) || pos.offset(0,-1,0).isPoweredIndirectly(world);
	}

	// -------------MOVEMENT------------------
	@SuppressWarnings("rawtypes")
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		
		PC_CoordI pos = new PC_CoordI(i,j,k);

		if (!entity.isEntityAlive()) { return; }
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) { return; }
		if (entity instanceof EntityFX) { return; }

		if (entity instanceof EntityItem) {
			packItems(world, pos);
		}

		// detector activated
		if (!isActive(world, i, j, k) && type == PCtr_EnumConv.detector) {
			setStateIfEntityInteractsWithDetector(world, i, j, k);
		}

		if (entity instanceof EntityItem && type != PCtr_EnumConv.ejector && type != PCtr_EnumConv.speedy) {
			fillCauldron(world, i, j, k, (EntityItem) entity);
			fillBucket(world, i, j, k, (EntityItem) entity);
			fillBottle(world, i, j, k, (EntityItem) entity);
			if (storeNearby(world, pos, (EntityItem) entity)) { return; }
		}

		if (!entity.isEntityAlive()) { return; }

		// brake activated
		boolean halted = isPowered(world, pos) && type == PCtr_EnumConv.brake;

		if (halted) {
			if (entity instanceof EntityMinecart && halted) {
				entity.motionX *= 0.2D;
				entity.motionZ *= 0.2D;
			} else {
				entity.motionX *= 0.6D;
				entity.motionZ *= 0.6D;
			}
		}

		// speed limit
		if (entity instanceof EntityItem || entity instanceof EntityXPOrb) {
			entity.stepHeight = 0.25F;
			if (entity.motionX > PCtr_BeltBase.MAX_HORIZONTAL_SPEED) {
				entity.motionX *= 0.6D;
			}
			if (entity.motionX < -PCtr_BeltBase.MAX_HORIZONTAL_SPEED) {
				entity.motionX *= 0.6D;
			}
			if (entity.motionZ > PCtr_BeltBase.MAX_HORIZONTAL_SPEED) {
				entity.motionZ *= 0.6D;
			}
			if (entity.motionZ < -PCtr_BeltBase.MAX_HORIZONTAL_SPEED) {
				entity.motionZ *= 0.6D;
			}
			if (entity.motionY > 0.3) {
				entity.motionY *= 0.3;
			}
		}

		int meta = getRotation(world.getBlockMetadata(i, j, k));

		// get redir
		int redir = 0;
		if (type == PCtr_EnumConv.redirector && isPowered(world, pos)) {
			switch (meta) {
				case 0: // '\0' Z--
					if (isConveyorOrElevatorAt(world, i + 1, j, k)) {
						redir = -1;
					} else if (isConveyorOrElevatorAt(world, i - 1, j, k)) {
						redir = 1;
					}
					break;
				case 1: // '\001' X++
					if (isConveyorOrElevatorAt(world, i, j, k + 1)) {
						redir = -1;
					} else if (isConveyorOrElevatorAt(world, i, j, k - 1)) {
						redir = 1;
					}
					break;

				// 6,7
				case 2: // '\0' Z++
					if (isConveyorOrElevatorAt(world, i - 1, j, k)) {
						redir = -1;
					} else if (isConveyorOrElevatorAt(world, i + 1, j, k)) {
						redir = 1;
					}
					break;

				case 3: // '\001' X--
					if (isConveyorOrElevatorAt(world, i, j, k - 1)) {
						redir = -1;
					} else if (isConveyorOrElevatorAt(world, i, j, k + 1)) {
						redir = 1;
					}
					break;
			}
		}

		if (type == PCtr_EnumConv.redirector && redir == 0) { // not powered
			switch (meta) {
				case 0: // '\0' Z--
					if (isConveyorOrElevatorAt(world, i + 1, j, k) && isConveyorOrElevatorAt(world, i - 1, j, k)
							&& !isConveyorOrElevatorAt(world, i, j, k - 1)) {
						redir = 1;
					}
					break;
				case 1: // '\001' X++
					if (isConveyorOrElevatorAt(world, i, j, k + 1) && isConveyorOrElevatorAt(world, i, j, k - 1)
							&& !isConveyorOrElevatorAt(world, i + 1, j, k)) {
						redir = 1;
					}
					break;

				case 2: // '\0' Z++
					if (isConveyorOrElevatorAt(world, i - 1, j, k) && isConveyorOrElevatorAt(world, i + 1, j, k)
							&& !isConveyorOrElevatorAt(world, i, j, k + 1)) {
						redir = 1;
					}
					break;

				case 3: // '\001' X--
					if (isConveyorOrElevatorAt(world, i, j, k - 1) && isConveyorOrElevatorAt(world, i, j, k + 1)
							&& !isConveyorOrElevatorAt(world, i - 1, j, k)) {
						redir = 1;
					}
					break;
			}
		}

		int i2, j2, k2;
		i2 = i;
		j2 = j;
		k2 = k;
		switch (meta) {
			case 0: // Z--
				k2--;
				break;

			case 1: // X++
				i2++;
				break;

			// 6,7
			case 2: // Z++
				k2++;
				break;

			case 3: // X--
				i2--;
				break;
		}

		boolean leadsToNowhere = isBlocked(world, i2, j2, k2);
		leadsToNowhere = leadsToNowhere && isBeyondStorageBorder(world, pos, entity, PCtr_BeltBase.STORAGE_BORDER_LONG);

		// longlife!
		if (entity instanceof EntityItem) {
			if (!halted && !leadsToNowhere) {
				((EntityItem) entity).delayBeforeCanPickup = 7;
			}
			if (((EntityItem) entity).age >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, j + 1, k + 1))
						.size() < 40) {
					((EntityItem) entity).age = 4000;
				}
			}
		}

		if (entity instanceof EntityXPOrb && !leadsToNowhere) {
			if (((EntityXPOrb) entity).xpOrbAge >= 5000) {
				if (world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, j + 1, k + 1))
						.size() < 40) {
					((EntityXPOrb) entity).xpOrbAge = 4000;
				}
			}
		}

		// sound effect
		if (!halted && !leadsToNowhere && world.rand.nextInt(35) == 0) {
			List list = world.getEntitiesWithinAABBExcludingEntity(entity,
					AxisAlignedBB.getBoundingBoxFromPool(i, j, k, i + 1, j + 1, k + 1));
			if (list.size() == 0) {
				PCtr_BeltBase.soundEffectBelt(world, pos);
			} else {
				if (world.rand.nextInt(10) == 0) {
					PCtr_BeltBase.soundEffectBelt(world, pos);
				}
			}
		}

		double TMP_MAX_HORIZONTAL_SPEED = PCtr_BeltBase.MAX_HORIZONTAL_SPEED;
		if (type == PCtr_EnumConv.brake) {
			TMP_MAX_HORIZONTAL_SPEED *= 0.6D;
		}
		if (type == PCtr_EnumConv.speedy) {
			TMP_MAX_HORIZONTAL_SPEED *= 2.0D;
		}

		double TMP_HORIZONTAL_BOOST = PCtr_BeltBase.HORIZONTAL_BOOST;
		if (type == PCtr_EnumConv.brake) {
			TMP_HORIZONTAL_BOOST *= 0.6D;
		}
		if (type == PCtr_EnumConv.speedy) {
			TMP_HORIZONTAL_BOOST *= 2.0D;
		}

		double TMP_BORDERS = PCtr_BeltBase.BORDERS;
		double TMP_BORDER_BOOST = PCtr_BeltBase.BORDER_BOOST;

		// Z--
		if ((meta == 0 && redir == 0) || (meta == 1 && redir == 1) || (meta == 3 && redir == -1)) {
			if (entity.motionZ >= -TMP_MAX_HORIZONTAL_SPEED && !halted && !leadsToNowhere) {
				entity.motionZ -= TMP_HORIZONTAL_BOOST;
			}
			if (entity.posX > i + (1D - TMP_BORDERS)) {
				entity.motionX -= TMP_BORDER_BOOST;
			}
			if (entity.posX < i + TMP_BORDERS) {
				entity.motionX += TMP_BORDER_BOOST;
			}

			return;
		}

		// X++
		if ((meta == 1 && redir == 0) || (meta == 0 && redir == -1) || (meta == 2 && redir == 1)) {
			if (entity.motionX <= TMP_MAX_HORIZONTAL_SPEED && !halted && !leadsToNowhere) {
				entity.motionX += TMP_HORIZONTAL_BOOST;
			}
			if (entity.posZ > k + TMP_BORDERS) {
				entity.motionZ -= TMP_BORDER_BOOST;
			}
			if (entity.posZ < k + (1D - TMP_BORDERS)) {
				entity.motionZ += TMP_BORDER_BOOST;
			}

			return;
		}

		// Z++
		if ((meta == 2 && redir == 0) || (meta == 1 && redir == -1) || (meta == 3 && redir == 1)) {
			if (entity.motionZ <= TMP_MAX_HORIZONTAL_SPEED && !halted && !leadsToNowhere) {
				entity.motionZ += TMP_HORIZONTAL_BOOST;
			}
			if (entity.posX > i + (1D - TMP_BORDERS)) {
				entity.motionX -= TMP_BORDER_BOOST;
			}
			if (entity.posX < i + TMP_BORDERS) {
				entity.motionX += TMP_BORDER_BOOST;
			}

			return;
		}

		// X--
		if ((meta == 3 && redir == 0) || (meta == 0 && redir == 1) || (meta == 2 && redir == -1)) {
			if (entity.motionX >= -TMP_MAX_HORIZONTAL_SPEED && !halted && !leadsToNowhere) {
				entity.motionX -= TMP_HORIZONTAL_BOOST; /* entity.motionY+=0.1; */
			}// ok
			if (entity.posZ > k + TMP_BORDERS) {
				entity.motionZ -= TMP_BORDER_BOOST;
			}
			if (entity.posZ < k + (1D - TMP_BORDERS)) {
				entity.motionZ += TMP_BORDER_BOOST;
			}

			return;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBoxFromPool(i, 0.0F + j, k, (i + 1), (j + PCtr_BeltBase.HEIGHT_COLLISION + 0.0F), (k + 1));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		float f = 0;
		f = 0.0F + PCtr_BeltBase.HEIGHT_SELECTED;
		return AxisAlignedBB.getBoundingBoxFromPool(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + PCtr_BeltBase.HEIGHT, 1.0F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 0.6F, 1.0F);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (i == 0) { return 1; }
		if (i == 1) {
			return getIndexTop(j);
		} else {
			return 2;
		}
	}

	public int getIndexTop(int meta) {
		if (type == PCtr_EnumConv.belt) { return 0; }
		if (type == PCtr_EnumConv.speedy) { return 4; }
		if (type == PCtr_EnumConv.ejector) { return 3; }
		if (type == PCtr_EnumConv.detector) { return 6; }
		if (type == PCtr_EnumConv.brake) { return 5; }
		if (type == PCtr_EnumConv.redirector) { return 8; }
		return 0;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return l != 1;
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return getBlockTextureFromSideAndMetadata(l, iblockaccess.getBlockMetadata(i, j, k));
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.rotatedBoxRenderer;
	}

	@Override
	public String getTerrainFile() {
		return mod_PCtransport.getTerrainFile();
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int getMobilityFlag() {
		return 0;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("TRANSLUCENT");
		set.add("BELT");

		switch (type) {

			case belt:
				set.add("BELT_NORMAL");
				break;

			case speedy:
				set.add("BELT_SPEEDY");
				break;

			case ejector:
				set.add("BELT_BRAKE");
				set.add("REDSTONE");

				break;

			case detector:
				set.add("BELT_DETECTOR");
				set.add("REDSTONE");
				break;

			case redirector:
				set.add("BELT_REDIRECTOR");
				set.add("REDSTONE");
				break;
		}

		return set;
	}

	@Override
	public Set<String> getItemFlags(int damage) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		return set;
	}
}
