package powercraft.management;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class PC_SlotNoPickup extends Slot implements PC_ISlotWithBackground
{
    public ItemStack shownStack;

    public PC_SlotNoPickup()
    {
        super(new IInventory()
        {
            @Override
            public void setInventorySlotContents(int i, ItemStack itemstack) {}
            @Override
            public void openChest() {}
            @Override
            public void onInventoryChanged() {}
            @Override
            public boolean isUseableByPlayer(EntityPlayer entityplayer)
            {
                return true;
            }
            @Override
            public ItemStack getStackInSlotOnClosing(int i)
            {
                return null;
            }
            @Override
            public ItemStack getStackInSlot(int i)
            {
                return null;
            }
            @Override
            public int getSizeInventory()
            {
                return 1;
            }
            @Override
            public int getInventoryStackLimit()
            {
                return 0;
            }
            @Override
            public String getInvName()
            {
                return "FAKE";
            }
            @Override
            public ItemStack decrStackSize(int i, int j)
            {
                return null;
            }
            @Override
            public void closeChest() {}
        }, 0, 0, 0);
    }
    
    public PC_SlotNoPickup(IInventory inv, int slot)
    {
    	super(inv, slot, 0, 0);
    }
    
    @Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		return false;
	}

	@Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    @Override
    public ItemStack getBackgroundStack()
    {
        return shownStack;
    }

    @Override
    public Slot setBackgroundStack(ItemStack stack)
    {
        shownStack = stack;
        return this;
    }

    @Override
    public boolean renderTooltipWhenEmpty()
    {
        return true;
    }

    @Override
    public boolean renderGrayWhenEmpty()
    {
        return false;
    }
}