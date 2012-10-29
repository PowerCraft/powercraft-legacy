package net.minecraft.src;

import java.util.List;

public interface ICrafting
{
    void sendContainerAndContentsToPlayer(Container var1, List var2);

    /**
     * inform the player of a change in a single slot
     */
    void updateCraftingInventorySlot(Container var1, int var2, ItemStack var3);

    /**
     * send information about the crafting inventory to the client(currently only for furnace times)
     */
    void updateCraftingInventoryInfo(Container var1, int var2, int var3);
}
