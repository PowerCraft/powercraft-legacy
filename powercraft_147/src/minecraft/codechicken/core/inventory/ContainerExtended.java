package codechicken.core.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
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
            if(slot instanceof SlotDummy)
            {
                ((SlotDummy) slot).slotClick(player.inventory.getItemStack(), par2, par3 == 1);
                return null;
            }
        }
        return super.slotClick(par1, par2, par3, player);
    }
}
