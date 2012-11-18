package powercraft.core;

import net.minecraft.src.ItemStack;

public interface PC_IStateReportingInventory
{
    public abstract boolean isContainerEmpty();

    public abstract boolean isContainerFull();

    public abstract boolean hasContainerNoFreeSlots();

    public abstract boolean hasInventoryPlaceFor(ItemStack itemStack);

    public abstract boolean isContainerEmptyOf(ItemStack itemStack);
}
