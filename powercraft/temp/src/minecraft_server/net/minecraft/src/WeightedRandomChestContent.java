package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.WeightedRandom;
import net.minecraft.src.WeightedRandomItem;

public class WeightedRandomChestContent extends WeightedRandomItem {

   private int field_76297_b;
   private int field_76298_c;
   private int field_76295_d;
   private int field_76296_e;


   public WeightedRandomChestContent(int p_i3424_1_, int p_i3424_2_, int p_i3424_3_, int p_i3424_4_, int p_i3424_5_) {
      super(p_i3424_5_);
      this.field_76297_b = p_i3424_1_;
      this.field_76298_c = p_i3424_2_;
      this.field_76295_d = p_i3424_3_;
      this.field_76296_e = p_i3424_4_;
   }

   public static void func_76293_a(Random p_76293_0_, WeightedRandomChestContent[] p_76293_1_, TileEntityChest p_76293_2_, int p_76293_3_) {
      for(int var4 = 0; var4 < p_76293_3_; ++var4) {
         WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.func_76274_a(p_76293_0_, p_76293_1_);
         int var6 = var5.field_76295_d + p_76293_0_.nextInt(var5.field_76296_e - var5.field_76295_d + 1);
         if(Item.field_77698_e[var5.field_76297_b].func_77639_j() >= var6) {
            p_76293_2_.func_70299_a(p_76293_0_.nextInt(p_76293_2_.func_70302_i_()), new ItemStack(var5.field_76297_b, var6, var5.field_76298_c));
         } else {
            for(int var7 = 0; var7 < var6; ++var7) {
               p_76293_2_.func_70299_a(p_76293_0_.nextInt(p_76293_2_.func_70302_i_()), new ItemStack(var5.field_76297_b, 1, var5.field_76298_c));
            }
         }
      }

   }

   public static void func_76294_a(Random p_76294_0_, WeightedRandomChestContent[] p_76294_1_, TileEntityDispenser p_76294_2_, int p_76294_3_) {
      for(int var4 = 0; var4 < p_76294_3_; ++var4) {
         WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.func_76274_a(p_76294_0_, p_76294_1_);
         int var6 = var5.field_76295_d + p_76294_0_.nextInt(var5.field_76296_e - var5.field_76295_d + 1);
         if(Item.field_77698_e[var5.field_76297_b].func_77639_j() >= var6) {
            p_76294_2_.func_70299_a(p_76294_0_.nextInt(p_76294_2_.func_70302_i_()), new ItemStack(var5.field_76297_b, var6, var5.field_76298_c));
         } else {
            for(int var7 = 0; var7 < var6; ++var7) {
               p_76294_2_.func_70299_a(p_76294_0_.nextInt(p_76294_2_.func_70302_i_()), new ItemStack(var5.field_76297_b, 1, var5.field_76298_c));
            }
         }
      }

   }
}
