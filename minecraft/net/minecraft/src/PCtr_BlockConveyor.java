package net.minecraft.src;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;

public class PCtr_BlockConveyor extends Block implements PC_IBlockType, PC_IRotatedBox, PC_ISwapTerrain, ITextureProvider {
	public PCtr_BeltType type = PCtr_BeltType.belt;

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public boolean renderItemHorizontal() {
		return true;
	}

	protected PCtr_BlockConveyor(int i, PCtr_BeltType type) {
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
		return type == PCtr_BeltType.detector || type == PCtr_BeltType.ejector || type == PCtr_BeltType.brake;
	}

	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return PCtr_BeltBase.blockActivated(world, i, j, k, entityplayer);
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (type == PCtr_BeltType.ejector && l > 0) {
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
		return PCtr_BeltBase.isActive(meta) && type == PCtr_BeltType.detector;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return type == PCtr_BeltType.detector && isActive(world, i, j, k) && l == 1;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		if (type == PCtr_BeltType.ejector) {
			int meta = pos.getMeta(world);

			if (pos.isPoweredDirectly(world) || pos.isPoweredIndirectly(world) || pos.offset(0, -1, 0).isPoweredDirectly(world)
					|| pos.offset(0, -1, 0).isPoweredIndirectly(world)) {
				if (!PCtr_BeltBase.isActive(meta)) {
					if (!PCtr_BlockConveyor.dispenseStackFromNearbyMinecart(world, pos)) {
						tryToDispenseItem(world, pos);
					}
					pos.setMeta(world, PCtr_BeltBase.getActiveMeta(meta));
				}
			} else if (PCtr_BeltBase.isActive(meta)) {
				pos.setMeta(world, PCtr_BeltBase.getPassiveMeta(meta));
			}
		} else if (type == PCtr_BeltType.detector && isActive(world, i, j, k)) {
			setStateIfEntityInteractsWithDetector(world, i, j, k);
		}
	}


	@SuppressWarnings("rawtypes")
	public void setStateIfEntityInteractsWithDetector(World world, int i, int j, int k) {
		if (type == PCtr_BeltType.detector) {
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
		int rot = PCtr_BeltBase.getRotation(beltPos.getMeta(world));

		// first try the inventory right behind this belt
		if (rot == 2 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, -1), beltPos)) { return; }
		if (rot == 3 && dispenseFromInventoryAt(world, beltPos.offset(1, 0, 0), beltPos)) { return; }
		if (rot == 0 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, 1), beltPos)) { return; }
		if (rot == 1 && dispenseFromInventoryAt(world, beltPos.offset(-1, 0, 0), beltPos)) { return; }

		// try all the other sides
		if (rot != 2 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, -1), beltPos)) { return; }
		if (rot != 3 && dispenseFromInventoryAt(world, beltPos.offset(1, 0, 0), beltPos)) { return; }
		if (rot != 0 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, 1), beltPos)) { return; }
		if (rot != 1 && dispenseFromInventoryAt(world, beltPos.offset(-1, 0, 0), beltPos)) { return; }
	}



	static boolean dispenseItem(World world, PC_CoordI invPos, IInventory inventory, PC_CoordI beltPos) {
		ItemStack stack = PC_InvUtils.dispenseFirstStack(inventory);

		if (stack != null) {
			createEntityItemOnBelt(world, invPos, beltPos, stack);
			return true;
		}
		return false;
	}

	private static void createEntityItemOnBelt(World world, PC_CoordI invPos, PC_CoordI beltPos, ItemStack stack) {
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

	private boolean isBeyondStorageBorder(World world, PC_CoordI beltPos, Entity entity, float border) {
		switch (PCtr_BeltBase.getRotation(beltPos.getMeta(world))) {
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

		if (PCtr_BeltBase.storeItemIntoMinecart(world, pos, entity)) { return true; }
		if (entity.posY > pos.y + 1 - PCtr_BeltBase.STORAGE_BORDER_V) { return false; }


		int rot = PCtr_BeltBase.getRotation(pos.getMeta(world));


		if (isBeyondStorageBorder(world, pos, entity, PCtr_BeltBase.STORAGE_BORDER)
				|| (isPowered(world, pos) && type == PCtr_BeltType.brake)) {
			if (rot == 0 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0, 0, -1), entity)) { return true; }
			if (rot == 1 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(1, 0, 0), entity)) { return true; }
			if (rot == 2 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0, 0, 1), entity)) { return true; }
			if (rot == 3 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(-1, 0, 0), entity)) { return true; }

			if (rot != 0 && rot != 2 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0, 0, -1), entity)) { return true; }
			if (rot != 1 && rot != 3 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(1, 0, 0), entity)) { return true; }
			if (rot != 2 && rot != 0 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0, 0, 1), entity)) { return true; }
			if (rot != 3 && rot != 1 && PCtr_BeltBase.storeEntityItemAt(world, pos.offset(-1, 0, 0), entity)) { return true; }

			if (PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0, 1, 0), entity)) { return true; }

			// store under belt if not roaster.
			if (PC_BlockUtils.hasFlag(world, pos.offset(0, -1, 0), "ROASTER")) {
				if (PCtr_BeltBase.storeEntityItemAt(world, pos.offset(0, -1, 0), entity)) { return true; }
			}
		}
		return false;
	}

	// from interface, but also used locally
	@Override
	public int getRotation(int meta) {
		return PCtr_BeltBase.getRotation(meta);
	}

	private boolean isPowered(World world, PC_CoordI pos) {
		return pos.isPoweredIndirectly(world) || pos.offset(0, 1, 0).isPoweredIndirectly(world)
				|| pos.offset(0, -1, 0).isPoweredIndirectly(world);
	}

	// -------------MOVEMENT------------------
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		if(PCtr_BeltBase.isEntityIgnored(entity)) return;

		if (entity instanceof EntityItem) PCtr_BeltBase.packItems(world, pos);

		// detector activated
		if (!isActive(world, i, j, k) && type == PCtr_BeltType.detector) {
			setStateIfEntityInteractsWithDetector(world, i, j, k);
		}

		if (entity instanceof EntityItem && type != PCtr_BeltType.ejector && type != PCtr_BeltType.speedy) {
			PCtr_BeltBase.doSpecialItemAction(world, pos, (EntityItem) entity);
			if (storeNearby(world, pos, (EntityItem) entity)) { return; }
		}

		if (!entity.isEntityAlive()) { return; }

		// brake activated
		boolean halted = isPowered(world, pos) && type == PCtr_BeltType.brake;

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
			if (entity.motionY > 0.2) {
				entity.motionY -= 0.1;
			}
		}

		int meta = getRotation(world.getBlockMetadata(i, j, k));

		// get redir
		int redir = 0;
		if (type == PCtr_BeltType.redirector && isPowered(world, pos)) {
			switch (meta) {
				case 0: // '\0' Z--
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(1, 0, 0))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(world, pos.offset(-1, 0, 0))) {
						redir = 1;
					}
					break;
				case 1: // '\001' X++
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, 1))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, -1))) {
						redir = 1;
					}
					break;

				// 6,7
				case 2: // '\0' Z++
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(-1, 0, 0))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(world, pos.offset(1, 0, 0))) {
						redir = 1;
					}
					break;

				case 3: // '\001' X--
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, -1))) {
						redir = -1;
					} else if (PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, 1))) {
						redir = 1;
					}
					break;
			}
		}

		if (type == PCtr_BeltType.redirector && redir == 0) { // not powered
			switch (meta) {
				case 0: // '\0' Z--
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(1, 0, 0))
							&& PCtr_BeltBase.isTransporterAt(world, pos.offset(-1, 0, 0))
							&& !PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, -1))) {
						redir = 1;
					}
					break;
				case 1: // '\001' X++
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, 1))
							&& PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, -1))
							&& !PCtr_BeltBase.isTransporterAt(world, pos.offset(1, 0, 0))) {
						redir = 1;
					}
					break;

				case 2: // '\0' Z++
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(-1, 0, 0))
							&& PCtr_BeltBase.isTransporterAt(world, pos.offset(1, 0, 0))
							&& !PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, 1))) {
						redir = 1;
					}
					break;

				case 3: // '\001' X--
					if (PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, -1))
							&& PCtr_BeltBase.isTransporterAt(world, pos.offset(0, 0, 1))
							&& !PCtr_BeltBase.isTransporterAt(world, pos.offset(-1, 0, 0))) {
						redir = 1;
					}
					break;
			}
		}
		
		redir = -redir;
		
		int direction = meta + redir;
		if(direction == -1) direction = 3;
		if(direction == 4) direction = 0;

		PC_CoordI pos_leading_to = pos.copy();
		switch (meta) {
			case 0: // Z--
				pos_leading_to.z--;
				break;

			case 1: // X++
				pos_leading_to.x++;
				break;

			// 6,7
			case 2: // Z++
				pos_leading_to.z++;
				break;

			case 3: // X--
				pos_leading_to.x--;
				break;
		}

		boolean leadsToNowhere = PCtr_BeltBase.isBlocked(world, pos_leading_to);
		leadsToNowhere = leadsToNowhere && isBeyondStorageBorder(world, pos, entity, PCtr_BeltBase.STORAGE_BORDER_LONG);

		// longlife!
		if(!leadsToNowhere){
			PCtr_BeltBase.entityPreventDespawning(world, pos, !halted, entity);
		}



		double speed_max = PCtr_BeltBase.MAX_HORIZONTAL_SPEED;
		if (type == PCtr_BeltType.brake) {
			speed_max *= 0.6D;
		}
		if (type == PCtr_BeltType.speedy) {
			speed_max *= 2.0D;
		}

		double boost = PCtr_BeltBase.HORIZONTAL_BOOST;
		if (type == PCtr_BeltType.brake) {
			boost *= 0.6D;
		}
		if (type == PCtr_BeltType.speedy) {
			boost *= 2.0D;
		}
		
		PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, !halted && !leadsToNowhere, direction, speed_max, boost);

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
		if (type == PCtr_BeltType.belt) { return 0; }
		if (type == PCtr_BeltType.speedy) { return 4; }
		if (type == PCtr_BeltType.ejector) { return 3; }
		if (type == PCtr_BeltType.detector) { return 6; }
		if (type == PCtr_BeltType.brake) { return 5; }
		if (type == PCtr_BeltType.redirector) { return 8; }
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

	/**
	 * Get closest minecraft and dispense stack from it onto this belt.
	 * @param world
	 * @param beltPos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean dispenseStackFromNearbyMinecart(World world, PC_CoordI beltPos) {
		List<EntityMinecart> hitList = world.getEntitiesWithinAABB(
				net.minecraft.src.EntityMinecart.class,
				AxisAlignedBB.getBoundingBoxFromPool(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(
						1.0D, 1.0D, 1.0D));
	
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
}
