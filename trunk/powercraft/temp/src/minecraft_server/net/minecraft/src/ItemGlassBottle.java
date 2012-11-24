package net.minecraft.src;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class ItemGlassBottle extends Item {

   public ItemGlassBottle(int p_i3622_1_) {
      super(p_i3622_1_);
      this.func_77637_a(CreativeTabs.field_78038_k);
   }

   public ItemStack func_77659_a(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
      MovingObjectPosition var4 = this.func_77621_a(p_77659_2_, p_77659_3_, true);
      if(var4 == null) {
         return p_77659_1_;
      } else {
         if(var4.field_72313_a == EnumMovingObjectType.TILE) {
            int var5 = var4.field_72311_b;
            int var6 = var4.field_72312_c;
            int var7 = var4.field_72309_d;
            if(!p_77659_2_.func_72962_a(p_77659_3_, var5, var6, var7)) {
               return p_77659_1_;
            }

            if(!p_77659_3_.func_82247_a(var5, var6, var7, var4.field_72310_e, p_77659_1_)) {
               return p_77659_1_;
            }

            if(p_77659_2_.func_72803_f(var5, var6, var7) == Material.field_76244_g) {
               --p_77659_1_.field_77994_a;
               if(p_77659_1_.field_77994_a <= 0) {
                  return new ItemStack(Item.field_77726_bs);
               }

               if(!p_77659_3_.field_71071_by.func_70441_a(new ItemStack(Item.field_77726_bs))) {
                  p_77659_3_.func_71021_b(new ItemStack(Item.field_77726_bs.field_77779_bT, 1, 0));
               }
            }
         }

         return p_77659_1_;
      }
   }
}
