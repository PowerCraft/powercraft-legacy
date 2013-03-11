package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.Inventory;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.inventory.PC_ISpecialAccessInventory;
import powercraft.api.inventory.PC_IStateReportingInventory;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.tileentity.PC_TileEntity;

public class PCma_TileEntityAutomaticWorkbench extends PC_TileEntity implements IInventory, PC_IStateReportingInventory, PC_ISpecialAccessInventory
{
	
    private static class ContainerFake extends Container
    {
        public ContainerFake() {}

        @Override
        public boolean canInteractWith(EntityPlayer entityplayer)
        {
            return true;
        }

        @Override
        public void onCraftMatrixChanged(IInventory iinventory) {}

        @Override
        public void onCraftGuiClosed(EntityPlayer entityplayer) {}
    }

    private static Container fakeContainer = new ContainerFake();

    private ItemStack actContents[] = new ItemStack[18];
    
    @PC_ClientServerSync
    public boolean redstoneActivated;

    public boolean isRedstoneActivated(){
    	return redstoneActivated;
    }
    
    public void setRedstoneActivated(boolean state){
    	if(redstoneActivated!=state){
    		redstoneActivated = state;
    		notifyChanges("redstoneActivated");
    	}
    }
    
    private InventoryCrafting getStorageAsCraftingGrid(Container container)
    {
        if (container == null)
        {
            container = fakeContainer;
        }

        InventoryCrafting craftGrid = new InventoryCrafting(container, 3, 3);

        for (int n = 0; n < 9; n++)
        {
            craftGrid.setInventorySlotContents(n, getStackInSlot(n));
        }

        return craftGrid;
    }

    private InventoryCrafting getRecipeAsCraftingGrid(Container container)
    {
        if (container == null)
        {
            container = fakeContainer;
        }

        InventoryCrafting craftGrid = new InventoryCrafting(container, 3, 3);

        for (int n = 9; n < 18; n++)
        {
            craftGrid.setInventorySlotContents(n - 9, getStackInSlot(n));
        }

        return craftGrid;
    }

    private boolean areProductsMatching()
    {
        ItemStack recipe = getRecipeProduct();
        ItemStack storage = getStorageProduct();

        if (recipe == null || storage == null)
        {
            return false;
        }

        return ItemStack.areItemStacksEqual(storage, recipe);
    }

    public ItemStack getStorageProduct()
    {
        ItemStack product = CraftingManager.getInstance().findMatchingRecipe(getStorageAsCraftingGrid(null), worldObj);

        if (product != null)
        {
            return product.copy();
        }

        return null;
    }

    public ItemStack getRecipeProduct()
    {
        ItemStack product = CraftingManager.getInstance().findMatchingRecipe(getRecipeAsCraftingGrid(null), worldObj);

        if (product != null)
        {
            return product.copy();
        }

        return null;
    }

    @Override
    public boolean isContainerEmpty()
    {
        for (int i = 0; i < 9; i++)
        {
            if (getStackInSlot(i) != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isContainerFull()
    {
        for (int i = 0; i < 9; i++)
        {
            if (getStackInSlot(i) == null && getStackInSlot(i + 9) != null)
            {
                return false;
            }
            else if (getStackInSlot(i) != null && getStackInSlot(i + 9) != null)
            {
                if (getStackInSlot(i).stackSize < Math.min(getStackInSlot(i).getMaxStackSize(), getInventoryStackLimit()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean hasContainerNoFreeSlots()
    {
        for (int i = 0; i < 9; i++)
        {
            if (getStackInSlot(i) == null && getStackInSlot(i + 9) != null)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean hasInventoryPlaceFor(ItemStack itemStack)
    {
        for (int i = 0; i < 9; i++)
        {
            if (getStackInSlot(i) == null || (getStackInSlot(i).isItemEqual(itemStack) && getStackInSlot(i).stackSize < Math.min(getInventoryStackLimit(), getStackInSlot(i).getMaxStackSize())))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isContainerEmptyOf(ItemStack itemStack)
    {
        for (int i = 0; i < 9; i++)
        {
            if (getStackInSlot(i) != null && !getStackInSlot(i).isItemEqual(itemStack))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onInventoryChanged() {}

    public void reorderACT()
    {
    	if(worldObj.isRemote)
    		return;
        List<ItemStack> stacks = new ArrayList<ItemStack>();

        for (int i = 0; i < 9; i++)
        {
            ItemStack stack = getStackInSlot(i);
            setInventorySlotContents(i, null);

            if (stack == null)
            {
                continue;
            }

            for (int j = i + 1; j < 9; j++)
            {
                ItemStack stack2 = getStackInSlot(j);

                if (stack2 == null)
                {
                    continue;
                }

                if (stack.itemID == stack2.itemID && (!stack.getHasSubtypes() || stack.getItemDamage() == stack2.getItemDamage()))
                {
                    stack.stackSize += stack2.stackSize;
                    setInventorySlotContents(j, null);
                }
            }

            stacks.add(stack);
        }

        for (ItemStack stack : stacks)
        {
            insertStackIntoInventory_do(stack);

            if (stack.stackSize > 0)
            {
                int itemX = xCoord;
                int itemY = yCoord;
                int itemZ = zCoord;
                int orientation = getBlockMetadata();

                switch (orientation)
                {
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

                while (stack.stackSize > 0)
                {
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
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        boolean flag = insertStackIntoInventory_do(stack);
        orderAndCraft();
        return flag;
    }

    private boolean insertStackIntoInventory_do(ItemStack stack)
    {
        if (stack == null)
        {
            return false;
        }

        boolean[] matching = new boolean[9];

        for (int i = 0; i < 9; i++)
        {
            if (getStackInSlot(i + 9) == null)
            {
                continue;
            }

            ItemStack storageStack = getStackInSlot(i);
            matching[i] = (stack.isItemEqual(getStackInSlot(i + 9)) && (storageStack == null || storageStack.stackSize <= storageStack
                    .getMaxStackSize()));
        }

        boolean end = false;
        boolean storedSomething = false;

        while (!end)
        {
            storedSomething = false;

            for (int i = 0; i < 9; i++)
            {
                if (!matching[i])
                {
                    continue;
                }

                ItemStack storageStack = getStackInSlot(i);
                matching[i] = (storageStack == null || storageStack.stackSize < storageStack.getMaxStackSize());

                if (matching[i])
                {
                    if (storageStack == null)
                    {
                        setInventorySlotContents(i, stack.splitStack(1));
                    }
                    else
                    {
                        storageStack.stackSize++;
                        stack.stackSize--;
                    }

                    storedSomething = true;
                }

                if (stack.stackSize <= 0)
                {
                    end = true;
                }

                if (end)
                {
                    break;
                }
            }

            if (end || !storedSomething)
            {
                break;
            }
        }

        return storedSomething;
    }

    public void orderAndCraft()
    {
    	if(worldObj.isRemote)
    		return;
        reorderACT();

        if (!isRedstoneActivated())
        {
            doCrafting();
        }

        reorderACT();
    }

    public void doCrafting()
    {
        ItemStack currentStack = null;
        boolean needsSound = false;
        boolean forceEject = false;

        while (areProductsMatching())
        {
            if (currentStack == null)
            {
                currentStack = getStorageProduct();
                decrementStorage();
            }
            else
            {
                if (currentStack.stackSize + getStorageProduct().stackSize >= currentStack.getMaxStackSize())
                {
                    forceEject = true;
                }
                else
                {
                    currentStack.stackSize += getStorageProduct().stackSize;
                    decrementStorage();
                }
            }

            if (currentStack != null)
            {
                if ((forceEject && currentStack.stackSize > 0) || currentStack.stackSize >= currentStack.getMaxStackSize() || isRedstoneActivated())
                {
                    dispenseItem(currentStack);
                    currentStack = null;
                    needsSound = true;

                    if (isRedstoneActivated())
                    {
                        makeSound();
                        return;
                    }
                }
            }
        }

        if (currentStack != null)
        {
            dispenseItem(currentStack);
            needsSound = true;
        }

        if (needsSound)
        {
            makeSound();
        }
    }

    public void decrementStorage()
    {
        for (int i = 0; i < 9; i++)
        {
        	if (actContents[i] != null)
            {
            	actContents[i].stackSize--;
            	
                if (actContents[i].getItem().hasContainerItem()){
                	ItemStack con = GameInfo.getContainerItemStack(actContents[i]);
                	if (con.isItemStackDamageable() && con.getItemDamage() > con.getMaxDamage()){
                		con = null;
                	}
                	if(con != null){
                		if(actContents[i].getItem().doesContainerItemLeaveCraftingGrid(actContents[i])){
                			dispenseItem(con);
                		}else{
                			if (actContents[i].stackSize <= 0) {
                            	actContents[i] = con;
                            }else{
                            	dispenseItem(con);
                            }
                		}
                	}
                }

                if (actContents[i].stackSize <= 0) {
                	actContents[i] = null;
                }
            }
        }
    }

    public void decrementRecipe()
    {
        for (int i = 9; i < 18; i++)
        {
            if (actContents[i] != null)
            {
            	actContents[i].stackSize--;
            	
                if (actContents[i].getItem().hasContainerItem()){
                	ItemStack con = GameInfo.getContainerItemStack(actContents[i]);
                	if (con.isItemStackDamageable() && con.getItemDamage() > con.getMaxDamage()){
                		con = null;
                	}
                	if(con != null){
                		if(actContents[i].getItem().doesContainerItemLeaveCraftingGrid(actContents[i])){
                			dispenseItem(con);
                		}else{
                			if (actContents[i].stackSize <= 0) {
                            	actContents[i] = con;
                            }else{
                            	dispenseItem(con);
                            }
                		}
                	}
                }

                if (actContents[i].stackSize <= 0) {
                	actContents[i] = null;
                }
            }
        }
    }

    private boolean dispenseItem(ItemStack stack2drop)
    {
        if (stack2drop == null || stack2drop.stackSize <= 0)
        {
            return false;
        }
        if(worldObj.isRemote)
    		return true;
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        int i1 = 0;
        int j1 = 0;

        switch (meta)
        {
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

        double d = xCoord + i1 * 1.0D + 0.5D;
        double d1 = yCoord + 0.5D;
        double d2 = zCoord + j1 * 1.0D + 0.5D;
        double d3 = worldObj.rand.nextDouble() * 0.02000000000000001D + 0.05000000000000001D;
        EntityItem entityitem = new EntityItem(worldObj, d, d1 - 0.29999999999999999D, d2, stack2drop.copy());
        entityitem.motionX = i1 * d3;
        entityitem.motionY = 0.05000000298023221D;
        entityitem.motionZ = j1 * d3;

        if (!worldObj.isRemote)
        {
            worldObj.spawnEntityInWorld(entityitem);
        }

        return true;
    }

    private void makeSound()
    {
        if (PC_SoundRegistry.isSoundEnabled())
        {
            worldObj.playAuxSFX(1000, xCoord, yCoord, zCoord, 0);
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 18;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return actContents[i];
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest()
    {
        reorderACT();
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (actContents[i] != null)
        {
            if (actContents[i].stackSize <= j)
            {
                ItemStack itemstack = actContents[i];
                actContents[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = actContents[i].splitStack(j);

            if (actContents[i].stackSize == 0)
            {
                actContents[i] = null;
            }

            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        actContents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Automatic Workbench";
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        Inventory.loadInventoryFromNBT(nbttagcompound, "Items", this);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        Inventory.saveInventoryToNBT(nbttagcompound, "Items", this);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }

        return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (actContents[par1] != null)
        {
            ItemStack itemstack = actContents[par1];
            actContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return slot < 9;
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return true;
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        return false;
    }
    
    @Override
	protected void onCall(String key, Object value) {
		if(key.equals("orderAndCraft")){
			orderAndCraft();
		}
	}

	@Override
	public boolean canDropStackFrom(int slot) {
		return true;
	}

	@Override
	public int getSlotStackLimit(int slotIndex) {
		return getInventoryStackLimit();
	}

	@Override
	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer) {
		return true;
	}
    
}
