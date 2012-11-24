package net.minecraft.src;

import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public interface IRecipe {

   boolean func_77569_a(InventoryCrafting var1, World var2);

   ItemStack func_77572_b(InventoryCrafting var1);

   int func_77570_a();

   ItemStack func_77571_b();
}
