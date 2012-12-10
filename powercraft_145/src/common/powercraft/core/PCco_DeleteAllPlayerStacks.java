package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Utils;

public class PCco_DeleteAllPlayerStacks implements PC_IPacketHandler
{
    @Override
    public boolean handleIncomingPacket(EntityPlayer player, Object[] o)
    {
    	
    	final int craftingTool = PC_Utils.getPCObjectIDByName("PCco_ItemCraftingTool");
    	
        if ("Delete".equals(o[0]))
        {
            IInventory inv = player.inventory;

            for (int i = 0; i < inv.getSizeInventory() - 4; i++)
            {
                ItemStack stack = inv.getStackInSlot(i);

                if (stack != null)
                {
                    if (stack.itemID != craftingTool)
                    {
                        inv.decrStackSize(i, inv.getStackInSlot(i).stackSize);
                    }
                }
            }
        }
        else
        {
            InventoryPlayer inv = player.inventory;
            List<ItemStack> stacks = new ArrayList<ItemStack>();

            for (int i = 0; i < inv.getSizeInventory() - 4; i++)
            {
                ItemStack stack = inv.getStackInSlot(i);

                if (stack != null)
                {
                    inv.setInventorySlotContents(i, null);
                    stacks.add(stack);
                }
            }

            if (stacks.size() == 0)
            {
                return false;
            }

            PC_InvUtils.groupStacks(stacks);
            List<ItemStack> sorted = new ArrayList<ItemStack>();

            while (stacks.size() > 0)
            {
                ItemStack lowest = null;
                int indexLowest = -1;

                for (int i = 0; i < stacks.size(); i++)
                {
                    ItemStack checked = stacks.get(i);

                    if (checked == null)
                    {
                        indexLowest = i;
                        break;
                    }

                    if (lowest == null
                            || (checked.itemID == craftingTool && lowest.itemID != craftingTool)
                            || ((lowest.itemID * 32000 * 64 + lowest.getItemDamage() * 64 + lowest.stackSize) > (checked.itemID * 32000 * 64
                                    + checked.getItemDamage() * 64 + checked.stackSize) && lowest.itemID != craftingTool))
                    {
                        lowest = checked;
                        indexLowest = i;
                    }
                }

                if (lowest != null)
                {
                    sorted.add(stacks.remove(indexLowest));
                }
            }

            for (ItemStack stack : sorted)
            {
                inv.addItemStackToInventory(stack);
            }
        }

        return false;
    }
}
