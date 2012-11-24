package net.minecraft.src;

public interface IBehaviorDispenseItem
{
    IBehaviorDispenseItem itemDispenseBehaviorProvider = new BehaviorDispenseItemProvider();

    ItemStack dispense(IBlockSource var1, ItemStack var2);
}
