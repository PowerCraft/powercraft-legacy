package powercraft.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryLargeChest;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagByte;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagInt;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityBrewingStand;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.World;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.inventory.PC_IInventoryWrapper;
import powercraft.management.recipes.PC_ISpecialAccessInventory;
import powercraft.management.recipes.PC_IStateReportingInventory;

public class PC_InvUtils
{
    public static IInventory getCompositeInventoryAt(IBlockAccess world, PC_VecI pos)
    {
        TileEntity te = GameInfo.getTE(world, pos);

        if (te == null)
        {
            return null;
        }

        if (te instanceof PC_IInventoryWrapper)
        {
            return ((PC_IInventoryWrapper) te).getInventory();
        }

        if (!(te instanceof IInventory))
        {
            return null;
        }

        IInventory inv = (IInventory) te;
        int id = GameInfo.getBID(world, pos);

        if (id == Block.chest.blockID)
        {
            if (GameInfo.getBID(world, pos.offset(-1, 0, 0)) == Block.chest.blockID)
            {
                inv = new InventoryLargeChest("Large chest", (IInventory) GameInfo.getTE(world, pos.offset(-1, 0, 0)), inv);
            }

            if (GameInfo.getBID(world, pos.offset(1, 0, 0)) == Block.chest.blockID)
            {
                inv = new InventoryLargeChest("Large chest", inv, (IInventory) GameInfo.getTE(world, pos.offset(1, 0, 0)));
            }

            if (GameInfo.getBID(world, pos.offset(0, 0, -1)) == Block.chest.blockID)
            {
                inv = new InventoryLargeChest("Large chest", (IInventory) GameInfo.getTE(world, pos.offset(0, 0, -1)), inv);
            }

            if (GameInfo.getBID(world, pos.offset(0, 0, 1)) == Block.chest.blockID)
            {
                inv = new InventoryLargeChest("Large chest", inv, (IInventory) GameInfo.getTE(world, pos.offset(0, 0, 1)));
            }
        }

        return inv;
    }

    public static boolean storeItemInSlot(IInventory inventory, ItemStack stackToStore, int slot)
    {
        if (stackToStore == null || stackToStore.stackSize == 0)
        {
            return false;
        }

        if (inventory instanceof PC_ISpecialAccessInventory && !((PC_ISpecialAccessInventory) inventory).canMachineInsertStackTo(slot, stackToStore))
        {
            return false;
        }

        ItemStack destination = inventory.getStackInSlot(slot);

        if (destination == null)
        {
            int numStored = stackToStore.stackSize;
            numStored = Math.min(numStored, stackToStore.getMaxStackSize());
            numStored = Math.min(numStored, inventory.getInventoryStackLimit());
            destination = stackToStore.splitStack(numStored);
            inventory.setInventorySlotContents(slot, destination);
            return true;
        }

        if (destination.itemID == stackToStore.itemID && destination.isStackable()
                && (!destination.getHasSubtypes() || destination.getItemDamage() == stackToStore.getItemDamage())
                && destination.stackSize < inventory.getInventoryStackLimit())
        {
            int numStored = stackToStore.stackSize;
            numStored = Math.min(numStored, destination.getMaxStackSize() - destination.stackSize);
            numStored = Math.min(numStored, inventory.getInventoryStackLimit() - destination.stackSize);
            destination.stackSize += numStored;
            stackToStore.stackSize -= numStored;
            return (numStored > 0);
        }

        return false;
    }

    public static boolean addItemStackToInventory(IInventory inv, ItemStack itemstack)
    {
        if (!itemstack.isItemDamaged())
        {
            int i;

            do
            {
                i = itemstack.stackSize;
                itemstack.stackSize = storePartialItemStack(inv, itemstack);
            }
            while (itemstack.stackSize > 0 && itemstack.stackSize < i);

            return itemstack.stackSize < i;
        }

        int j = getFirstEmptySlot(inv, itemstack);

        if (j >= 0)
        {
            inv.setInventorySlotContents(j, ItemStack.copyItemStack(itemstack));
            itemstack.stackSize = 0;
            return true;
        }

        return false;
    }

    public static boolean addWholeItemStackToInventory(IInventory inv, ItemStack itemstack)
    {
        if (!itemstack.isItemDamaged())
        {
            int oldSize;

            do
            {
                oldSize = itemstack.stackSize;
                itemstack.stackSize = storePartialItemStack(inv, itemstack);
            }
            while (itemstack.stackSize > 0 && itemstack.stackSize < oldSize);

            return itemstack.stackSize == 0;
        }

        int emptySlot = getFirstEmptySlot(inv, itemstack);

        if (emptySlot >= 0)
        {
            inv.setInventorySlotContents(emptySlot, ItemStack.copyItemStack(itemstack));
            itemstack.stackSize = 0;
            return true;
        }

        return false;
    }

    private static int getStackWithFreeSpace(IInventory inv, ItemStack itemstack)
    {
        for (int slot = 0; slot < inv.getSizeInventory(); slot++)
        {
            if (inv instanceof PC_ISpecialAccessInventory)
            {
                if (!((PC_ISpecialAccessInventory) inv).canMachineInsertStackTo(slot, itemstack))
                {
                    continue;
                }
            }

            ItemStack stackAt = inv.getStackInSlot(slot);

            if (stackAt != null && stackAt.itemID == itemstack.itemID && stackAt.isStackable() && stackAt.stackSize < stackAt.getMaxStackSize()
                    && stackAt.stackSize < inv.getInventoryStackLimit()
                    && (!stackAt.getHasSubtypes() || stackAt.getItemDamage() == itemstack.getItemDamage()))
            {
                return slot;
            }
        }

        return -1;
    }

    private static int storePartialItemStack(IInventory inv, ItemStack itemstack)
    {
        int id = itemstack.itemID;
        int size = itemstack.stackSize;

        if (itemstack.getMaxStackSize() == 1)
        {
            int firstEmpty = getFirstEmptySlot(inv, itemstack);

            if (firstEmpty < 0)
            {
                return size;
            }

            if (inv.getStackInSlot(firstEmpty) == null)
            {
                inv.setInventorySlotContents(firstEmpty, ItemStack.copyItemStack(itemstack));
            }

            return 0;
        }

        int targetSlot = getStackWithFreeSpace(inv, itemstack);

        if (targetSlot < 0)
        {
            targetSlot = getFirstEmptySlot(inv, itemstack);
        }

        if (targetSlot < 0)
        {
            return size;
        }

        ItemStack is = inv.getStackInSlot(targetSlot);

        if (is == null)
        {
            is = new ItemStack(id, 0, itemstack.getItemDamage());
            inv.setInventorySlotContents(targetSlot, is);
        }

        int canStore = size;

        if (canStore > is.getMaxStackSize() - is.stackSize)
        {
            canStore = is.getMaxStackSize() - is.stackSize;
        }

        if (canStore > inv.getInventoryStackLimit() - is.stackSize)
        {
            canStore = inv.getInventoryStackLimit() - is.stackSize;
        }

        if (canStore == 0)
        {
            return size;
        }
        else
        {
            size -= canStore;
            is.stackSize += canStore;
            return size;
        }
    }

    private static int getFirstEmptySlot(IInventory inv, ItemStack stackInserted)
    {
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            if (inv.getStackInSlot(i) == null)
            {
                if (inv instanceof PC_ISpecialAccessInventory)
                {
                    if (!((PC_ISpecialAccessInventory) inv).canMachineInsertStackTo(i, stackInserted))
                    {
                        continue;
                    }
                }

                return i;
            }
        }

        return -1;
    }

    public static boolean storeItemInInventory(IInventory inventory, ItemStack stack)
    {
        if (inventory instanceof TileEntityFurnace)
        {
            if (GameInfo.isSmeltable(stack))
            {
                return PC_InvUtils.storeItemInSlot(inventory, stack, 0);
            }
            else if (GameInfo.isFuel(stack))
            {
                return PC_InvUtils.storeItemInSlot(inventory, stack, 1);
            }
            else
            {
                return false;
            }
        }

        if (inventory instanceof TileEntityBrewingStand)
        {
            if (stack.itemID == Item.potion.itemID)
            {
                if (PC_InvUtils.storeItemInSlot(inventory, stack, 0))
                {
                    return true;
                }

                if (PC_InvUtils.storeItemInSlot(inventory, stack, 1))
                {
                    return true;
                }

                if (PC_InvUtils.storeItemInSlot(inventory, stack, 2))
                {
                    return true;
                }

                return false;
            }
            else
            {
                if (stack.getItem().isPotionIngredient())
                {
                    return PC_InvUtils.storeItemInSlot(inventory, stack, 3);
                }

                return false;
            }
        }

        if (inventory instanceof PC_ISpecialAccessInventory)
        {
            boolean result = ((PC_ISpecialAccessInventory) inventory).insertStackIntoInventory(stack);
            return result;
        }

        return PC_InvUtils.addItemStackToInventory(inventory, stack);
    }

    public static boolean isInventoryFull(IInventory inv)
    {
        if (inv == null)
        {
            return false;
        }

        if (inv instanceof PC_IStateReportingInventory)
        {
            return ((PC_IStateReportingInventory) inv).isContainerFull();
        }

        if (inv instanceof TileEntityFurnace)
        {
            return inv.getStackInSlot(1) != null
                    && inv.getStackInSlot(1).stackSize == Math.min(inv.getInventoryStackLimit(), inv.getStackInSlot(1).getMaxStackSize());
        }

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            if (inv.getStackInSlot(i) == null
                    || inv.getStackInSlot(i).stackSize < Math.min(inv.getInventoryStackLimit(), inv.getStackInSlot(i).getMaxStackSize()))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean hasInventoryNoFreeSlots(IInventory inv)
    {
        if (inv == null)
        {
            return false;
        }

        if (inv instanceof PC_IStateReportingInventory)
        {
            return ((PC_IStateReportingInventory) inv).hasContainerNoFreeSlots();
        }

        if (inv instanceof TileEntityFurnace)
        {
            return inv.getStackInSlot(1) != null;
        }

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            if (inv.getStackInSlot(i) == null)
            {
                return false;
            }
        }

        return true;
    }

    public static boolean isInventoryEmpty(IInventory inv)
    {
        if (inv == null)
        {
            return true;
        }

        if (inv instanceof PC_IStateReportingInventory)
        {
            return ((PC_IStateReportingInventory) inv).isContainerEmpty();
        }

        if (inv instanceof TileEntityFurnace)
        {
            return inv.getStackInSlot(1) == null;
        }

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            if (inv.getStackInSlot(i) != null)
            {
                return false;
            }
        }

        return true;
    }

    public static boolean moveStacksForce(IInventory from, IInventory to)
    {
        int copied = Math.min(from.getSizeInventory(), to.getSizeInventory());

        for (int i = 0; i < copied; i++)
        {
            to.setInventorySlotContents(i, from.getStackInSlot(i));
            from.setInventorySlotContents(i, null);
        }

        return from.getSizeInventory() <= to.getSizeInventory();
    }

    public static void moveStacks(IInventory from, IInventory to)
    {
        for (int i = 0; i < from.getSizeInventory(); i++)
        {
            if (from.getStackInSlot(i) != null)
            {
                addItemStackToInventory(to, from.getStackInSlot(i));

                if (from.getStackInSlot(i) != null && from.getStackInSlot(i).stackSize <= 0)
                {
                    from.setInventorySlotContents(i, null);
                }
            }
        }
    }

    public static int getPlayerArmourValue(EntityPlayerSP player)
    {
        return player.inventory.getTotalArmorValue();
    }

    public static int getFuelValue(ItemStack itemstack, double strength)
    {
        return (int)(TileEntityFurnace.getItemBurnTime(itemstack) * strength);
    }

    public static int countPowerCrystals(IInventory inventory)
    {
        boolean[] foundTable = { false, false, false, false, false, false, false, false };

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).itemID == ModuleInfo.getPCObjectIDByName("PCco_BlockPowerCrystal"))
            {
                foundTable[PC_MathHelper.clamp_int(inventory.getStackInSlot(i).getItemDamage(), 0, 7)] = true;
            }
        }

        int cnt = 0;

        for (int i = 0; i < 8; i++)
        {
            if (foundTable[i])
            {
                cnt++;
            }
        }

        return cnt;
    }

    public static ItemStack[] groupStacks(ItemStack[] input)
    {
        List<ItemStack> list = stacksToList(input);
        groupStacks(list);
        return stacksToArray(list);
    }

    public static void groupStacks(List<ItemStack> input)
    {
        if (input == null)
        {
            return;
        }

        for (ItemStack st1 : input)
        {
            if (st1 != null)
            {
                for (ItemStack st2 : input)
                {
                    if (st2 != null && st2.isItemEqual(st1))
                    {
                        int movedToFirst = Math.min(st2.stackSize, st1.getItem().getItemStackLimit() - st1.stackSize);

                        if (movedToFirst <= 0)
                        {
                            break;
                        }

                        st1.stackSize += movedToFirst;
                        st2.stackSize -= movedToFirst;
                    }
                }
            }
        }

        ArrayList<ItemStack> copy = new ArrayList<ItemStack>(input);

        for (int i = copy.size() - 1; i >= 0; i--)
        {
            if (copy.get(i) == null || copy.get(i).stackSize <= 0)
            {
                input.remove(i);
            }
        }
    }

    public static List<ItemStack> stacksToList(ItemStack[] stacks)
    {
        ArrayList<ItemStack> myList = new ArrayList<ItemStack>();
        Collections.addAll(myList, stacks);
        return myList;
    }

    public static ItemStack[] stacksToArray(List<ItemStack> stacks)
    {
        return stacks.toArray(new ItemStack[stacks.size()]);
    }

    public static void loadInventoryFromNBT(NBTTagCompound outerTag, String invTagName, IInventory inventory)
    {
        NBTTagList nbttaglist = outerTag.getTagList(invTagName);

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            NBTBase nbtTag = nbttagcompound1.getTag("Slot");
            int j = -1;
            if(nbtTag instanceof NBTTagByte){
            	j = ((NBTTagByte) nbtTag).data & 0xff;
            }else if(nbtTag instanceof NBTTagInt){
           	 	j = ((NBTTagInt) nbtTag).data;
            }

            if (j >= 0 && j < inventory.getSizeInventory())
            {
                inventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
    }

    public static void saveInventoryToNBT(NBTTagCompound outerTag, String invTagName, IInventory inventory)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (inventory.getStackInSlot(i) != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setInteger("Slot", i);
                inventory.getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        outerTag.setTag(invTagName, nbttaglist);
    }

    public static void dropInventoryContents(IInventory inventory, World world, PC_VecI pos)
    {
        Random random = new Random();

        if (inventory != null)
        {
            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
            	if(inventory instanceof PC_ISpecialAccessInventory){
            		if(!((PC_ISpecialAccessInventory) inventory).canDropStackFrom(i))
            			continue;
            	}
                ItemStack itemstack = inventory.getStackInSlot(i);
                ValueWriting.dropItemStack(world, itemstack, pos);
            }
        }
    }

    public static boolean hasInventoryPlaceFor(IInventory inv, ItemStack itemStack)
    {
        if (inv == null)
        {
            return false;
        }

        if (inv instanceof PC_IStateReportingInventory)
        {
            return ((PC_IStateReportingInventory) inv).hasInventoryPlaceFor(itemStack);
        }

        if (inv instanceof TileEntityFurnace)
        {
            return inv.getStackInSlot(1) == null || (inv.getStackInSlot(1).isItemEqual(itemStack)
                    && inv.getStackInSlot(1).stackSize == Math.min(inv.getInventoryStackLimit(), inv.getStackInSlot(1).getMaxStackSize()));
        }

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            if (inv.getStackInSlot(i) == null || (inv.getStackInSlot(i).isItemEqual(itemStack)
                    && inv.getStackInSlot(i).stackSize < Math.min(inv.getInventoryStackLimit(), inv.getStackInSlot(i).getMaxStackSize())))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isInventoryEmptyOf(IInventory inv, ItemStack itemStack)
    {
        if (inv == null)
        {
            return true;
        }

        if (inv instanceof PC_IStateReportingInventory)
        {
            return ((PC_IStateReportingInventory) inv).isContainerEmptyOf(itemStack);
        }

        if (inv instanceof TileEntityFurnace)
        {
            return inv.getStackInSlot(1) == null || !inv.getStackInSlot(1).isItemEqual(itemStack);
        }

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).isItemEqual(itemStack))
            {
                return false;
            }
        }

        return true;
    }
    
    public static int insetItemTo(ItemStack itemstack, IInventory inv, int start, int end){
    	ItemStack is = itemstack.copy();
    	for(int i=start; i<end; i++){
    		ItemStack isis = inv.getStackInSlot(i);
    		if(isis == null){
    			inv.setInventorySlotContents(i, itemstack.copy());
    			itemstack.stackSize = 0;
    		}else if(isis.isItemEqual(itemstack)){
    			int maxToInsert = Math.min(isis.getMaxStackSize(), inv.getInventoryStackLimit())-isis.stackSize;
    			if(maxToInsert>0){
    				if(maxToInsert>itemstack.stackSize){
    					maxToInsert = itemstack.stackSize;
    				}
    				isis.stackSize += maxToInsert;
    				itemstack.stackSize -= maxToInsert;
    			}
    		}
    		if(itemstack.stackSize==0)
    			break;
    	}
    	return itemstack.stackSize;
    }
    
}