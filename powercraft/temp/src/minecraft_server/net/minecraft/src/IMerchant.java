package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MerchantRecipe;
import net.minecraft.src.MerchantRecipeList;

public interface IMerchant {

   void func_70932_a_(EntityPlayer var1);

   EntityPlayer func_70931_l_();

   MerchantRecipeList func_70934_b(EntityPlayer var1);

   @SideOnly(Side.CLIENT)
   void func_70930_a(MerchantRecipeList var1);

   void func_70933_a(MerchantRecipe var1);
}
