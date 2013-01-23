package codechicken.core.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

public class SlotDummy extends Slot
{
    public final int stackLimit;
    public SlotDummy(IInventory inv, int slot, int x, int y)
    {
        this(inv, slot, x, y, 64);
    }
    
    public SlotDummy(IInventory inv, int slot, int x, int y, int limit)
    {
        super(inv, slot, x, y);
        stackLimit = limit;
    }
    
    public void slotClick(ItemStack stack, int button, boolean shift)
    {
        if(stack != null && !stack.equals(getStack()))
        {
            int quantity = Math.min(stack.stackSize, stackLimit);
            if(shift)
                quantity = stackLimit;
            if(button == 1)
                quantity = 1;
            putStack(InventoryUtils.copyStack(stack, quantity));
        }
        else if(getStack() != null)
        {
            int inc = button == 1 ? -1 : 1;
            if(shift)
                inc *= 16;
            ItemStack tstack = getStack();
            int quantity = tstack.stackSize+InventoryUtils.incrStackSize(tstack, inc);
            if(quantity <= 0)
                putStack(null);
            else
                putStack(InventoryUtils.copyStack(tstack, quantity));
        }            
    }
    
    @Override
    public void putStack(ItemStack stack)
    {
        if(stack != null && stack.stackSize > stackLimit)
            stack = InventoryUtils.copyStack(stack, stackLimit);
        super.putStack(stack);
    }
}
