package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class CreativeCrafting implements ICrafting
{
    private final Minecraft mc;

    public CreativeCrafting(Minecraft par1)
    {
        this.mc = par1;
    }

    public void sendContainerAndContentsToPlayer(Container par1Container, List par2List) {}

    /**
     * inform the player of a change in a single slot
     */
    public void updateCraftingInventorySlot(Container par1Container, int par2, ItemStack par3ItemStack)
    {
        this.mc.playerController.sendSlotPacket(par3ItemStack, par2);
    }

    /**
     * send information about the crafting inventory to the client(currently only for furnace times)
     */
    public void updateCraftingInventoryInfo(Container par1Container, int par2, int par3) {}
}
