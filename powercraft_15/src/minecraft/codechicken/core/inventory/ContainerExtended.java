package codechicken.core.inventory;

import java.util.List;

import codechicken.core.packet.PacketCustom;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

public abstract class ContainerExtended extends Container
{
    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer player)
    {
        if(par1 >= 0 && par1 < inventorySlots.size())
        {
            Slot slot = getSlot(par1);
            if(slot instanceof SlotHandleClicks)
                return ((SlotHandleClicks) slot).slotClick(this, player, par2, par3);
        }
        return super.slotClick(par1, par2, par3, player);
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotIndex)
    {
        ItemStack transferredStack = null;
        Slot slot = (Slot)inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            transferredStack = stack.copy();
            
            if(!doMergeStackAreas(slotIndex, stack))
                return null;

            if (stack.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
        }

        return transferredStack;
    }
    
    @Override
    public boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverse)
    {
        boolean merged = false;
        int slotIndex = reverse ? endIndex - 1 : startIndex;
        
        if(stack == null)
            return false;
        
        if (stack.isStackable())//search for stacks to increase
        {
            while (stack.stackSize > 0 && (reverse ? slotIndex >= startIndex : slotIndex < endIndex))
            {
                Slot slot = (Slot)inventorySlots.get(slotIndex);
                ItemStack slotStack = slot.getStack();

                if (slotStack != null && slotStack.itemID == stack.itemID && 
                        (!stack.getHasSubtypes() || stack.getItemDamage() == slotStack.getItemDamage()) && 
                        ItemStack.areItemStackTagsEqual(stack, slotStack))
                {
                    int totalStackSize = slotStack.stackSize + stack.stackSize;
                    int maxStackSize = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
                    if (totalStackSize <= maxStackSize)
                    {
                        stack.stackSize = 0;
                        slotStack.stackSize = totalStackSize;
                        slot.onSlotChanged();
                        merged = true;
                    }
                    else if (slotStack.stackSize < maxStackSize)
                    {
                        stack.stackSize -= maxStackSize - slotStack.stackSize;
                        slotStack.stackSize = maxStackSize;
                        slot.onSlotChanged();
                        merged = true;
                    }
                }
                
                slotIndex += reverse ? -1 : 1;
            }
        }

        if (stack.stackSize > 0)//normal transfer :)
        {
            slotIndex = reverse ? endIndex - 1 : startIndex;

            while (stack.stackSize > 0 && (reverse ? slotIndex >= startIndex : slotIndex < endIndex))
            {
                Slot slot = (Slot)this.inventorySlots.get(slotIndex);
                
                if (!slot.getHasStack() && slot.isItemValid(stack))
                {
                    int maxStackSize = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
                    if (stack.stackSize <= maxStackSize)
                    {
                        slot.putStack(stack.copy());
                        slot.onSlotChanged();
                        stack.stackSize = 0;
                        merged = true;
                    }
                    else
                    {
                        slot.putStack(stack.splitStack(maxStackSize));
                        slot.onSlotChanged();
                        merged = true;
                    }
                }
                
                slotIndex += reverse ? -1 : 1;
            }
        }

        return merged;
    }

    public boolean doMergeStackAreas(int slotIndex, ItemStack stack)
    {
        return false;
    }
    
    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        bindPlayerInventory(inventoryPlayer, 8, 84);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y)
    {
        for(int row = 0; row < 3; row++)
            for(int col = 0; col < 9; col++)
                addSlotToContainer(new Slot(inventoryPlayer, col + row * 9 + 9, x + col * 18, y + row * 18));
        for(int slot = 0; slot < 9; slot++)
            addSlotToContainer(new Slot(inventoryPlayer, slot, x + slot * 18, y + 58));
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public void sendContainerPacket(PacketCustom packet)
    {
        for(ICrafting crafting : (List<ICrafting>)crafters)
            if(crafting instanceof EntityPlayerMP)
                ((EntityPlayerMP)crafting).playerNetServerHandler.sendPacketToPlayer(packet.toPacket());
    }
    
    /**
     * May be called from a client packet handler to handle additional info
     */
    public void handleOutputPacket(PacketCustom packet)
    {
    }

    /**
     * May be called from a server packet handler to handle additional info
     */
    public void handleInputPacket(PacketCustom packet)
    {
    }
    
    /**
     * May be called from a server packet handler to handle client input
     */
    public void handleGuiChange(int ID, int value)
    {
    }
    
    @SuppressWarnings("unchecked")
    public void sendProgressBarUpdate(int barID, int value)
    {
        for(ICrafting crafting : (List<ICrafting>)crafters)
            crafting.sendProgressBarUpdate(this, barID, value);
    }
}
