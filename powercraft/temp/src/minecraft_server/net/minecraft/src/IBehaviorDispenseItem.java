package net.minecraft.src;

import net.minecraft.src.BehaviorDispenseItemProvider;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.ItemStack;

public interface IBehaviorDispenseItem {

   IBehaviorDispenseItem field_82483_a = new BehaviorDispenseItemProvider();


   ItemStack func_82482_a(IBlockSource var1, ItemStack var2);

}
