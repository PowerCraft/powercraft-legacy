package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;



/**
 * Automatic workbench's tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_TileEntityAutomaticWorkbench extends PC_TileEntity implements IInventory, PC_IStateReportingInventory, PC_ISpecialAccessInventory {
	private static Container fakeContainer = new PCma_ContainerFake();

	/**
	 * tile entity aw
	 */
	public PCma_TileEntityAutomaticWorkbench() {
		actContents = new ItemStack[18];
	}

	/**
	 * Get crafting grid representing the Storage part of inventory
	 * 
	 * @param container gui container
	 * @return the corresponding crafting grid
	 */
	private InventoryCrafting getStorageAsCraftingGrid(Container container) {
		if (container == null) {
			container = fakeContainer;
		}
		InventoryCrafting craftGrid = new InventoryCrafting(container, 3, 3);
		for (int n = 0; n < 9; n++) {
			craftGrid.setInventorySlotContents(n, getStackInSlot(n));
		}
		return craftGrid;
	}

	/**
	 * Get crafting grid representing the Recipe part of inventory
	 * 
	 * @param container gui container
	 * @return the corresponding crafting grid
	 */
	private InventoryCrafting getRecipeAsCraftingGrid(Container container) {
		if (container == null) {
			container = fakeContainer;
		}
		InventoryCrafting craftGrid = new InventoryCrafting(container, 3, 3);
		for (int n = 9; n < 18; n++) {
			craftGrid.setInventorySlotContents(n - 9, getStackInSlot(n));
		}
		return craftGrid;
	}

	/**
	 * @return does crafting of Recipe Grid output the same as crafting of Storage Grid?
	 */
	private boolean areProductsMatching() {
		ItemStack recipe = getRecipeProduct();
		ItemStack storage = getStorageProduct();
		if (recipe == null || storage == null) { return false; }
		return storage.isStackEqual(recipe);
	}

	/**
	 * @return recipe product of the storage crafting grid
	 */
	public ItemStack getStorageProduct() {
		ItemStack product = CraftingManager.getInstance().findMatchingRecipe(getStorageAsCraftingGrid(null));
		if (product != null) { return product.copy(); }
		return null;
	}

	/**
	 * @return recipe product of the recipe crafting grid
	 */
	public ItemStack getRecipeProduct() {
		ItemStack product = CraftingManager.getInstance().findMatchingRecipe(getRecipeAsCraftingGrid(null));
		if (product != null) { return product.copy(); }
		return null;
	}

	@Override
	public boolean isContainerEmpty() {
		for (int i = 0; i < 9; i++) {
			if (getStackInSlot(i) != null) { return false; }
		}
		return true;
	}

	@Override
	public boolean isContainerFull() {
		for (int i = 0; i < 9; i++) {
			if (getStackInSlot(i) == null && getStackInSlot(i + 9) != null) { return false; }
		}
		return true;
	}

	private boolean redstoneActivatedMode() {
		return worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == Block.stoneBrick.blockID;
	}

	// special things.
	@Override
	public void onInventoryChanged() {}

	/**
	 * Reorder contents of the storage grid to fill as many slots a spossible
	 */
	public void reorderACT() {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for (int i = 0; i < 9; i++) {
			ItemStack stack = getStackInSlot(i);
			setInventorySlotContents(i, null);
			if (stack == null) {
				continue;
			}
			for (int j = i + 1; j < 9; j++) {
				ItemStack stack2 = getStackInSlot(j);
				if (stack2 == null) {
					continue;
				}

				if (stack.itemID == stack2.itemID && (!stack.getHasSubtypes() || stack.getItemDamage() == stack2.getItemDamage())) {
					stack.stackSize += stack2.stackSize;
					setInventorySlotContents(j, null);
				}
			}
			stacks.add(stack);
		}

		for (ItemStack stack : stacks) {
			insertStackIntoInventory(stack);

			// drop the remainder
			if (stack.stackSize > 0) {
				int itemX = xCoord;
				int itemY = yCoord;
				int itemZ = zCoord;
				int orientation = getBlockMetadata();
				switch (orientation) {
					case 0:
						itemZ++;
						break;
					case 1:
						itemX--;
						break;
					case 2:
						itemZ--;
						break;
					case 3:
						itemX++;
						break;
				}

				// dropping is correctly sized stacks
				while (stack.stackSize > 0) {
					int batchSize = Math.min(stack.stackSize, stack.getMaxStackSize());
					ItemStack batch = stack.splitStack(batchSize);
					EntityItem drop = new EntityItem(worldObj, itemX + 0.5D, itemY + 0.5D, itemZ + 0.5D, batch);
					drop.motionX = 0.0D;
					drop.motionY = 0.0D;
					drop.motionZ = 0.0D;
					worldObj.spawnEntityInWorld(drop);
				}
			}
		}

		onInventoryChanged();
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		if (stack == null) { return false; }

		// prepare table of matching slots.
		boolean[] matching = new boolean[9];
		for (int i = 0; i < 9; i++) {
			if (getStackInSlot(i + 9) == null) {
				continue;
			}
			ItemStack storageStack = getStackInSlot(i);
			matching[i] = (stack.isItemEqual(getStackInSlot(i + 9)) && (storageStack == null || storageStack.stackSize <= storageStack
					.getMaxStackSize()));
		}

		// cycle through slots until they are full or stack is empty.
		boolean end = false;
		boolean storedSomething = false;
		while (!end) {
			storedSomething = false;
			for (int i = 0; i < 9; i++) {

				if (!matching[i]) {
					continue;
				}
				ItemStack storageStack = getStackInSlot(i);
				matching[i] = (storageStack == null || storageStack.stackSize < storageStack.getMaxStackSize());

				// good slot
				if (matching[i]) {

					if (storageStack == null) {
						setInventorySlotContents(i, stack.splitStack(1));
					} else {
						storageStack.stackSize++;
						stack.stackSize--;
					}
					storedSomething = true;
				}

				if (stack.stackSize <= 0) {
					end = true;
				}

				if (end) {
					break;
				}
			}

			if (end || !storedSomething) {
				break;
			}
		}
		
		orderAndCraft();

		return storedSomething; //stack.stackSize <= 0;
	}

	/**
	 * Reorder ACT and do crafting
	 */
	public void orderAndCraft() {
		reorderACT();
		if (!redstoneActivatedMode()) {
			doCrafting();
		}
		reorderACT(); // dispense buckets etc.
	}

	/**
	 * Do crafting
	 */
	public void doCrafting() {
		ItemStack currentStack = null;

		boolean forceEject = false;
		while (areProductsMatching()) {
			if (currentStack == null) {
				currentStack = getStorageProduct();
				decrementStorage();
			} else {
				if (currentStack.stackSize + getStorageProduct().stackSize >= currentStack.getMaxStackSize()) {
					forceEject = true;
				} else {
					currentStack.stackSize += getStorageProduct().stackSize;
					decrementStorage();
				}
			}

			if (currentStack != null) {
				if ((forceEject && currentStack.stackSize > 0) || currentStack.stackSize >= currentStack.getMaxStackSize()
						|| redstoneActivatedMode()) {
					dispenseItem(currentStack);
					currentStack = null;
					if (redstoneActivatedMode()) { return; }
				}
			}
		}

		if (currentStack != null) {
			dispenseItem(currentStack);
		}
	}

	/**
	 * remove one from each storage slot
	 */
	public void decrementStorage() {
		for (int i = 0; i < 9; i++) {
			if (actContents[i] != null) {
				if (actContents[i].getItem().hasContainerItem()) {
					setInventorySlotContents(i, new ItemStack(actContents[i].getItem().getContainerItem()));
				} else {
					actContents[i].stackSize--;
					if (actContents[i].stackSize <= 0) {
						actContents[i] = null;
					}
				}
			}
		}
	}

	/**
	 * Remove one from each recipe slot
	 */
	public void decrementRecipe() {
		for (int i = 9; i < 18; i++) {
			if (actContents[i] != null) {
				if (actContents[i].getItem().hasContainerItem()) {
					setInventorySlotContents(i, new ItemStack(actContents[i].getItem().getContainerItem()));
				} else {
					actContents[i].stackSize--;
					if (actContents[i].stackSize <= 0) {
						actContents[i] = null;
					}
				}
			}
		}
	}

	/**
	 * Do dispense item
	 * 
	 * @param stack2drop stack to drop
	 * @return something dispensed
	 */
	private boolean dispenseItem(ItemStack stack2drop) {
		if (stack2drop == null || stack2drop.stackSize <= 0) { return false; }

		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		int i1 = 0;
		int j1 = 0;

		switch (meta) {
			case 0:
				j1 = 1;
				break;
			case 1:
				i1 = -1;
				break;
			case 2:
				j1 = -1;
				break;
			case 3:
				i1 = 1;
				break;
		}

		double d = xCoord + i1 * 0.59999999999999998D + 0.5D;
		double d1 = yCoord + 0.5D;
		double d2 = zCoord + j1 * 0.59999999999999998D + 0.5D;
		double d3 = worldObj.rand.nextDouble() * 0.02000000000000001D + 0.05000000000000001D;

		EntityItem entityitem = new EntityItem(worldObj, d, d1 - 0.29999999999999999D, d2, stack2drop.copy());
		entityitem.motionX = i1 * d3;
		entityitem.motionY = 0.05000000298023221D;
		entityitem.motionZ = j1 * d3;
		worldObj.spawnEntityInWorld(entityitem);
		if (mod_PCcore.soundsEnabled) {
			worldObj.playAuxSFX(1000, xCoord, yCoord, zCoord, 0);
		}
		return true;
	}



	@Override
	public int getSizeInventory() {
		return 18;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return actContents[i];
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {
		reorderACT();
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (actContents[i] != null) {
			if (actContents[i].stackSize <= j) {
				ItemStack itemstack = actContents[i];
				actContents[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = actContents[i].splitStack(j);
			if (actContents[i].stackSize == 0) {
				actContents[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		actContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Automatic Workbench";
	}

	/**
	 * Forge method - receives update ticks
	 * 
	 * @return false
	 */
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		actContents = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < actContents.length) {
				actContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < actContents.length; i++) {
			if (actContents[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				actContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	private ItemStack actContents[];

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) { return false; }
		return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	};

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (actContents[par1] != null) {
			ItemStack itemstack = actContents[par1];
			actContents[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public boolean canInsertStackTo(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return slot < 9;
	}

	@Override
	public boolean needsSpecialInserter() {
		return true;
	}
}
