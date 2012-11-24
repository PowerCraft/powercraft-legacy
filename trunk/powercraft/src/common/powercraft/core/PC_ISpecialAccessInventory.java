package powercraft.core;

import net.minecraft.src.ItemStack;

public interface PC_ISpecialAccessInventory
{
    public boolean insertStackIntoInventory(ItemStack stack);

    public boolean needsSpecialInserter();

    public boolean canPlayerInsertStackTo(int slot, ItemStack stack);

    public boolean canMachineInsertStackTo(int slot, ItemStack stack);

    public boolean canDispenseStackFrom(int slot);
}
