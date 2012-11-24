package net.minecraft.src;

import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MapData;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.World;

public class RecipesMapExtending extends ShapedRecipes {

   public RecipesMapExtending() {
      super(3, 3, new ItemStack[]{new ItemStack(Item.field_77759_aK), new ItemStack(Item.field_77759_aK), new ItemStack(Item.field_77759_aK), new ItemStack(Item.field_77759_aK), new ItemStack(Item.field_77744_bd, 0, -1), new ItemStack(Item.field_77759_aK), new ItemStack(Item.field_77759_aK), new ItemStack(Item.field_77759_aK), new ItemStack(Item.field_77759_aK)}, new ItemStack(Item.field_82801_bO, 0, 0));
   }

   public boolean func_77569_a(InventoryCrafting p_77569_1_, World p_77569_2_) {
      if(!super.func_77569_a(p_77569_1_, p_77569_2_)) {
         return false;
      } else {
         ItemStack var3 = null;

         for(int var4 = 0; var4 < p_77569_1_.func_70302_i_() && var3 == null; ++var4) {
            ItemStack var5 = p_77569_1_.func_70301_a(var4);
            if(var5 != null && var5.field_77993_c == Item.field_77744_bd.field_77779_bT) {
               var3 = var5;
            }
         }

         if(var3 == null) {
            return false;
         } else {
            MapData var6 = Item.field_77744_bd.func_77873_a(var3, p_77569_2_);
            return var6 == null?false:var6.field_76197_d < 4;
         }
      }
   }

   public ItemStack func_77572_b(InventoryCrafting p_77572_1_) {
      ItemStack var2 = null;

      for(int var3 = 0; var3 < p_77572_1_.func_70302_i_() && var2 == null; ++var3) {
         ItemStack var4 = p_77572_1_.func_70301_a(var3);
         if(var4 != null && var4.field_77993_c == Item.field_77744_bd.field_77779_bT) {
            var2 = var4;
         }
      }

      var2 = var2.func_77946_l();
      var2.field_77994_a = 1;
      if(var2.func_77978_p() == null) {
         var2.func_77982_d(new NBTTagCompound());
      }

      var2.func_77978_p().func_74757_a("map_is_scaling", true);
      return var2;
   }
}
