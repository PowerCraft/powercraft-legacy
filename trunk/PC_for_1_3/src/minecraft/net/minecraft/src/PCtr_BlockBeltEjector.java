package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;


/**
 * item ejection belt
 * 
 * @author MightyPork
 */
public class PCtr_BlockBeltEjector extends BlockContainer implements PC_IBlockType, PC_IRotatedBox, PC_ISwapTerrain, ITextureProvider {

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public boolean renderItemHorizontal() {
		return true;
	}

	/**
	 * @param i block ID
	 */
	protected PCtr_BlockBeltEjector(int i) {
		super(i, new PCtr_MaterialConveyor());
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltBase.HEIGHT, 1.0F);
		blockIndexInTexture = 0;

		setStepSound(Block.soundPowderFootstep);
	}

	@Override
	public int tickRate() {
		return 1;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		if (PCtr_BeltBase.blockActivated(world, i, j, k, entityplayer)) {
			return true;
		} else {
			ItemStack ihold = entityplayer.getCurrentEquippedItem();
			if (ihold != null) {
				if (ihold.getItem() instanceof ItemBlock) {
					if (Block.blocksList[ihold.itemID] instanceof PC_IBlockType && ihold.itemID != blockID) {
						return false;
					}
				}
			}

			PC_Utils.openGres(entityplayer, PCtr_GuiEjectionBelt.class, world, i, j, k);

			return true;
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0) {
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
		return false;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return false;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		int meta = pos.getMeta(world);

		if (isPowered(world, pos)) {

			if (!PCtr_BeltBase.isActive(meta)) {
				if (!PCtr_BlockBeltEjector.dispenseStackFromNearbyMinecart(world, pos)) {
					tryToDispenseItem(world, pos);
				}
				pos.setMeta(world, PCtr_BeltBase.getActiveMeta(meta));
			}

		} else if (PCtr_BeltBase.isActive(meta)) {

			pos.setMeta(world, PCtr_BeltBase.getPassiveMeta(meta));

		}

	}

	/**
	 * Dispense item from inventory at given location onto a belt
	 * 
	 * @param world
	 * @param inventoryPos position where is the inventory
	 * @param beltPos position of the belt to place the item on
	 * @return success
	 */
	public static boolean dispenseFromInventoryAt(World world, PC_CoordI inventoryPos, PC_CoordI beltPos) {
		IInventory inventory = PC_InvUtils.getCompositeInventoryAt(world, inventoryPos);
		if (inventory == null) {
			return false;
		}
		return dispenseItemOntoBelt(world, inventoryPos, inventory, beltPos);
	}


	/**
	 * Try to dispense item from blocks around the belt.
	 * 
	 * @param world
	 * @param beltPos the belt position
	 */
	public static void tryToDispenseItem(World world, PC_CoordI beltPos) {
		int rot = PCtr_BeltBase.getRotation(beltPos.getMeta(world));

		// first try the inventory right behind this belt
		if (rot == 2 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, -1), beltPos)) {
			return;
		}
		if (rot == 3 && dispenseFromInventoryAt(world, beltPos.offset(1, 0, 0), beltPos)) {
			return;
		}
		if (rot == 0 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, 1), beltPos)) {
			return;
		}
		if (rot == 1 && dispenseFromInventoryAt(world, beltPos.offset(-1, 0, 0), beltPos)) {
			return;
		}

		// try all the other sides
		if (rot != 2 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, -1), beltPos)) {
			return;
		}
		if (rot != 3 && dispenseFromInventoryAt(world, beltPos.offset(1, 0, 0), beltPos)) {
			return;
		}
		if (rot != 0 && dispenseFromInventoryAt(world, beltPos.offset(0, 0, 1), beltPos)) {
			return;
		}
		if (rot != 1 && dispenseFromInventoryAt(world, beltPos.offset(-1, 0, 0), beltPos)) {
			return;
		}
	}

	/**
	 * Dispense item from inventory onto a belt.
	 * 
	 * @param world
	 * @param invPos pos of the inventory (for item positioning on the belt)
	 * @param inventory the iinventory to eject item from
	 * @param beltPos position of belt to place the item on
	 * @return item dispensed
	 */
	public static boolean dispenseItemOntoBelt(World world, PC_CoordI invPos, IInventory inventory, PC_CoordI beltPos) {
		ItemStack[] stacks = PCtr_BlockBeltEjector.dispenseStuffFromInventory(world, beltPos, inventory);

		if (stacks != null) {

			stacks = PC_InvUtils.groupStacks(stacks);

			for (ItemStack stack : stacks) {
				PCtr_BeltBase.createEntityItemOnBelt(world, invPos, beltPos, stack);
			}

			return true;
		}
		return false;
	}

	/**
	 * Get closest minecart and dispense stack from it onto this belt.
	 * 
	 * @param world
	 * @param beltPos
	 * @return item dispensed.
	 */
	public static boolean dispenseStackFromNearbyMinecart(World world, PC_CoordI beltPos) {
		List<Entity> hitList = world.getEntitiesWithinAABB(
				IInventory.class,
				AxisAlignedBB.getBoundingBox(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(0.6D, 0.6D,
						0.6D));

		if (hitList.size() > 0) {
			for (Entity entityWithInventory : hitList) {

				if (dispenseItemOntoBelt(world, new PC_CoordD(entityWithInventory.posX, entityWithInventory.posY, entityWithInventory.posZ).round(),
						(IInventory) entityWithInventory, beltPos)) {
					return true;
				}

			}
		}
		

		List<Entity> hitList2 = world.getEntitiesWithinAABB(
				PC_IInventoryWrapper.class,
				AxisAlignedBB.getBoundingBox(beltPos.x, beltPos.y, beltPos.z, beltPos.x + 1, beltPos.y + 1, beltPos.z + 1).expand(0.6D, 0.6D,
						0.6D));

		if (hitList2.size() > 0) {
			for (Entity entityWithInventory : hitList2) {
				if(((PC_IInventoryWrapper)entityWithInventory).getInventory() != null) {	
					if (dispenseItemOntoBelt(world, new PC_CoordD(entityWithInventory.posX, entityWithInventory.posY, entityWithInventory.posZ).round(),
							((PC_IInventoryWrapper) entityWithInventory).getInventory(), beltPos)) {
						return true;
					}
				}

			}
		}

		return false;
	}


	/**
	 * Check if the inventory is a special and needs special dispension method.
	 * Furnace, BrewStand
	 * 
	 * @param inventory
	 * @return needs special ejector
	 */
	static boolean isSpecialContainer(IInventory inventory) {
		boolean flag = false;
		flag |= inventory instanceof TileEntityFurnace;
		flag |= inventory instanceof TileEntityBrewingStand;
		return flag;
	}

	/**
	 * Dispense item from inventroy which needs special method Furnace,
	 * BrewStand
	 * 
	 * @param inventory the inventory
	 * @return stack ejected
	 */
	static ItemStack dispenseFromSpecialContainer(IInventory inventory) {
		if (inventory instanceof TileEntityFurnace) {
			ItemStack stack = inventory.getStackInSlot(2);
			if (stack != null && stack.stackSize > 0) {
				inventory.setInventorySlotContents(2, null);
				return stack;
			}
			return null;
		}

		if (inventory instanceof TileEntityBrewingStand) {

			// check if brewing finished
			if (((TileEntityBrewingStand) inventory).getBrewTime() != 0) {
				return null;
			}

			for (int i = 0; i < 4; i++) {

				ItemStack stack = inventory.getStackInSlot(i);

				// if 0-2, its potion slot. If 3, its ingredient.
				if ((i < 3 && (stack != null && stack.stackSize > 0 && stack.itemID == Item.potion.shiftedIndex && stack.getItemDamage() != 0))
						|| (i == 3 && (stack != null))) {
					inventory.setInventorySlotContents(i, null);
					return stack;
				}
			}
			return null;
		}

		return null;
	}

	/**
	 * Remove first stack from inventory
	 * 
	 * @param world the world
	 * @param beltPos position of the belt
	 * @param inventory inv
	 * @return the stack removed
	 */
	public static ItemStack[] dispenseStuffFromInventory(World world, PC_CoordI beltPos, IInventory inventory) {

		if (PCtr_BlockBeltEjector.isSpecialContainer(inventory)) {
			return new ItemStack[] { PCtr_BlockBeltEjector.dispenseFromSpecialContainer(inventory) };
		}


		PCtr_TileEntityEjectionBelt teb = (PCtr_TileEntityEjectionBelt) beltPos.getTileEntity(world);


		List<ItemStack> stacks = new ArrayList<ItemStack>();

		boolean modeStacks = teb.actionType == 0;
		boolean modeItems = teb.actionType == 1;
		boolean modeAll = teb.actionType == 2;

		if (modeAll) {
			for (int i = 0; i < inventory.getSizeInventory(); i++) {

				// fix for inventories that want to keep their stuff
				if (inventory instanceof PC_ISpecialAccessInventory) {
					if (!((PC_ISpecialAccessInventory) inventory).canDispenseStackFrom(i)) {
						continue;
					}
				}

				ItemStack inSlot = inventory.getStackInSlot(i);
				if (inSlot != null) {
					stacks.add(inSlot);
					inventory.setInventorySlotContents(i, null);
				}
			}
			return PC_InvUtils.stacksToArray(stacks);
		}

		boolean random = teb.itemSelectMode == 2;
		boolean first = teb.itemSelectMode == 0;
		boolean last = teb.itemSelectMode == 1;

		int numStacks = teb.numStacksEjected;
		int numItems = teb.numItemsEjected;

		if (modeStacks && numStacks == 0) return new ItemStack[] {};
		if (modeItems && numItems == 0) return new ItemStack[] {};

		int i = 0;

		if (first) i = 0;
		if (last) i = inventory.getSizeInventory() - 1;
		if (random) i = teb.rand.nextInt(inventory.getSizeInventory());

		int randomTries = inventory.getSizeInventory() * 2;

		while (true) {

			boolean accessDenied = false;

			// fix for inventories that want to keep their stuff
			if (inventory instanceof PC_ISpecialAccessInventory) {
				if (!((PC_ISpecialAccessInventory) inventory).canDispenseStackFrom(i)) {
					accessDenied = true;
				}
			}

			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && stack.stackSize > 0 && !accessDenied) {

				if (modeStacks) {
					if (numStacks > 0) {
						inventory.setInventorySlotContents(i, null);
						stacks.add(stack);
						numStacks--;
						if (numStacks <= 0) break;
					}
				} else if (modeItems) {
					if (numItems > 0) {
						stack = inventory.decrStackSize(i, numItems);
						numItems -= stack.stackSize;
						stacks.add(stack);
						if (numItems <= 0) break;
					}
				}

			}

			if (first) {
				i++;
				if (i >= inventory.getSizeInventory()) break;
			} else if (last) {
				i--;
				if (i < 0) break;
			} else if (random) {
				i = teb.rand.nextInt(inventory.getSizeInventory());
				randomTries--;
				if (randomTries == 0) break;
			}
		}

		return PC_InvUtils.stacksToArray(stacks);
	}



	// from interface, but also used locally
	@Override
	public int getRotation(int meta) {
		return PCtr_BeltBase.getRotation(meta);
	}

	private boolean isPowered(World world, PC_CoordI pos) {
		return pos.isPoweredIndirectly(world) || pos.offset(0, 1, 0).isPoweredIndirectly(world) || pos.offset(0, -1, 0).isPoweredIndirectly(world);
	}

	// -------------MOVEMENT------------------
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {

		PC_CoordI pos = new PC_CoordI(i, j, k);

		if (PCtr_BeltBase.isEntityIgnored(entity)) {
			return;
		}

		if (entity instanceof EntityItem) {
			PCtr_BeltBase.packItems(world, pos);
		}

		int direction = getRotation(pos.getMeta(world));

		PC_CoordI pos_leading_to = pos.copy();
		switch (direction) {
			case 0: // Z--
				pos_leading_to.z--;
				break;

			case 1: // X++
				pos_leading_to.x++;
				break;

			case 2: // Z++
				pos_leading_to.z++;
				break;

			case 3: // X--
				pos_leading_to.x--;
				break;
		}

		boolean leadsToNowhere = PCtr_BeltBase.isBlocked(world, pos_leading_to);
		leadsToNowhere = leadsToNowhere && PCtr_BeltBase.isBeyondStorageBorder(world, direction, pos, entity, PCtr_BeltBase.STORAGE_BORDER_LONG);

		// longlife!
		if (!leadsToNowhere) {
			PCtr_BeltBase.entityPreventDespawning(world, pos, true, entity);
		}



		double speed_max = PCtr_BeltBase.MAX_HORIZONTAL_SPEED;

		double boost = PCtr_BeltBase.HORIZONTAL_BOOST;

		PCtr_BeltBase.moveEntityOnBelt(world, pos, entity, true, !leadsToNowhere, direction, speed_max, boost);

	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), (j + PCtr_BeltBase.HEIGHT_COLLISION + 0.0F), (k + 1));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		float f = 0;
		f = 0.0F + PCtr_BeltBase.HEIGHT_SELECTED;
		return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
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
		if (i == 0) {
			return 1;
		}
		if (i == 1) {
			return 3;
		} else {
			return 2;
		}
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
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("BELT");

		set.add("BELT_EJECTOR");
		set.add("REDSTONE");


		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("BELT");
		return set;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCtr_TileEntityEjectionBelt();
	}


}
