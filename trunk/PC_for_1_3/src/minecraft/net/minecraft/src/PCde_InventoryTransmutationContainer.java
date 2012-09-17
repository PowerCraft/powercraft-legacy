package net.minecraft.src;


import java.util.Random;


/**
 * Transmutation Container's TE
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCde_InventoryTransmutationContainer implements IInventory, PC_ISpecialAccessInventory {

	/** The TransmutaChamber's tile entity */
	protected PCde_TileEntityDeco chamber;

	/**
	 * TC
	 * 
	 * @param pCde_TileEntityDeco transmutation chamber's TE
	 */
	public PCde_InventoryTransmutationContainer(PCde_TileEntityDeco pCde_TileEntityDeco) {
		stacks = new ItemStack[SIZE];
		rand = new Random();
		this.chamber = pCde_TileEntityDeco;
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		if (stack == null) return false;
		if (stack.itemID == Item.coal.shiftedIndex && stack.getItemDamage() == 0) return true;
		if (stack.itemID == Item.diamond.shiftedIndex) return true;
		if (stack.itemID == Item.redstone.shiftedIndex) return true;
		return false;
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		return canPlayerInsertStackTo(slot, stack);
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return PC_InvUtils.addWholeItemStackToInventory(this, stack);
	}

	@Override
	public boolean canDispenseStackFrom(int i) {
		return getStackInSlot(i)!=null&&getStackInSlot(i).itemID!=Item.coal.shiftedIndex&&getStackInSlot(i).getItemDamage()==0;
	}

	private ItemStack stacks[];
	private Random rand;
	/** max allowes stack size */
	public static final int MAXSTACK = 1;
	/** # of slots */
	public static final int SIZE = 5 * 3;


	@Override
	public int getSizeInventory() {
		return SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (stacks[i] != null) {
			if (stacks[i].stackSize <= j) {
				ItemStack itemstack = stacks[i];
				stacks[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = stacks[i].splitStack(j);
			if (stacks[i].stackSize == 0) {
				stacks[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		stacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return PC_Lang.tr("tile.PCdeDecoBlock.3.name");
	}

	@Override
	public int getInventoryStackLimit() {
		return MAXSTACK;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}



	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (stacks[par1] != null) {
			ItemStack itemstack = stacks[par1];
			stacks[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public boolean needsSpecialInserter() {
		return false;
	}

	@Override
	public void onInventoryChanged() {}

	/**
	 * Transmutate the stuff!!! :D
	 */
	public void onHitByLightning() {
		int total = 15;
		int burnt = 8 + rand.nextInt(5); // max 12, min 8 -> left 3, or 7

		int maxGoodTransmutations = 2 + rand.nextInt(6);

		for (int i = 0; i < total * 2; i++) {

			int thisSlot = rand.nextInt(total);

			ItemStack stack = getStackInSlot(thisSlot);

			if (burnt-- > 0) {
				setInventorySlotContents(thisSlot, null);
				//PC_Utils.setTileEntity(null, chamber, "setSlotTo", thisSlot, 0, 0, 0);
			} else if (stack != null) {
				if (stack.itemID == Item.coal.shiftedIndex && stack.getItemDamage() == 0) {
					if (rand.nextInt(120) == 0) {
						// coal to crystal
						int r=rand.nextInt(8);
						setInventorySlotContents(thisSlot, new ItemStack(mod_PCcore.powerCrystal, 1, r));
						//PC_Utils.setTileEntity(null, chamber, "setSlotTo", thisSlot, mod_PCcore.powerCrystal.blockID, 1, r);
						maxGoodTransmutations--;
					} else if (rand.nextInt(12) != 0 && maxGoodTransmutations-- > 0) {
						// coal to diamond
						setInventorySlotContents(thisSlot, new ItemStack(Item.diamond, 1, 0));
						//PC_Utils.setTileEntity(null, chamber, "setSlotTo", thisSlot, Item.diamond.shiftedIndex, 1, 0);
					} else {
						//let the coal unchanged
					}
				} else if (stack.itemID == Item.diamond.shiftedIndex) {
					if (rand.nextInt(8) != 0) {
						// diamond to crystal
						int r=rand.nextInt(8);
						setInventorySlotContents(thisSlot, new ItemStack(mod_PCcore.powerCrystal, 1, r));
						//PC_Utils.setTileEntity(null, chamber, "setSlotTo", thisSlot, mod_PCcore.powerCrystal.blockID, 1, r);
						maxGoodTransmutations--;
					} else {
						// ta-daa, diamond disappeared!
						setInventorySlotContents(thisSlot, null);
						//PC_Utils.setTileEntity(null, chamber, "setSlotTo", thisSlot, 0, 0, 0);
					}
				} else if (stack.itemID == Item.redstone.shiftedIndex) {
					
					// diamond to crystal
					setInventorySlotContents(thisSlot, new ItemStack(Item.lightStoneDust));
					//PC_Utils.setTileEntity(null, chamber, "setSlotTo", thisSlot, Item.lightStoneDust.shiftedIndex, 1, 0);
					if(rand.nextBoolean()) maxGoodTransmutations--;

				} else {
					setInventorySlotContents(thisSlot, null);
					//PC_Utils.setTileEntity(null, chamber, "setSlotTo", thisSlot, 0, 0, 0);
				}
			}

		}
	}

}
