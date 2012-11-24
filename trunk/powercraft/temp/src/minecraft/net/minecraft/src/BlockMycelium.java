package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockMycelium extends Block {

   protected BlockMycelium(int p_i3973_1_) {
      super(p_i3973_1_, Material.field_76247_b);
      this.field_72059_bZ = 77;
      this.func_71907_b(true);
      this.func_71849_a(CreativeTabs.field_78030_b);
   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      return p_71858_1_ == 1?78:(p_71858_1_ == 0?2:77);
   }

   @SideOnly(Side.CLIENT)
   public int func_71895_b(IBlockAccess p_71895_1_, int p_71895_2_, int p_71895_3_, int p_71895_4_, int p_71895_5_) {
      if(p_71895_5_ == 1) {
         return 78;
      } else if(p_71895_5_ == 0) {
         return 2;
      } else {
         Material var6 = p_71895_1_.func_72803_f(p_71895_2_, p_71895_3_ + 1, p_71895_4_);
         return var6 != Material.field_76259_v && var6 != Material.field_76258_w?77:68;
      }
   }

   public void func_71847_b(World p_71847_1_, int p_71847_2_, int p_71847_3_, int p_71847_4_, Random p_71847_5_) {
      if(!p_71847_1_.field_72995_K) {
         if(p_71847_1_.func_72957_l(p_71847_2_, p_71847_3_ + 1, p_71847_4_) < 4 && Block.field_71971_o[p_71847_1_.func_72798_a(p_71847_2_, p_71847_3_ + 1, p_71847_4_)] > 2) {
            p_71847_1_.func_72859_e(p_71847_2_, p_71847_3_, p_71847_4_, Block.field_71979_v.field_71990_ca);
         } else if(p_71847_1_.func_72957_l(p_71847_2_, p_71847_3_ + 1, p_71847_4_) >= 9) {
            for(int var6 = 0; var6 < 4; ++var6) {
               int var7 = p_71847_2_ + p_71847_5_.nextInt(3) - 1;
               int var8 = p_71847_3_ + p_71847_5_.nextInt(5) - 3;
               int var9 = p_71847_4_ + p_71847_5_.nextInt(3) - 1;
               int var10 = p_71847_1_.func_72798_a(var7, var8 + 1, var9);
               if(p_71847_1_.func_72798_a(var7, var8, var9) == Block.field_71979_v.field_71990_ca && p_71847_1_.func_72957_l(var7, var8 + 1, var9) >= 4 && Block.field_71971_o[var10] <= 2) {
                  p_71847_1_.func_72859_e(var7, var8, var9, this.field_71990_ca);
               }
            }
         }

      }
   }

   @SideOnly(Side.CLIENT)
   public void func_71862_a(World p_71862_1_, int p_71862_2_, int p_71862_3_, int p_71862_4_, Random p_71862_5_) {
      super.func_71862_a(p_71862_1_, p_71862_2_, p_71862_3_, p_71862_4_, p_71862_5_);
      if(p_71862_5_.nextInt(10) == 0) {
         p_71862_1_.func_72869_a("townaura", (double)((float)p_71862_2_ + p_71862_5_.nextFloat()), (double)((float)p_71862_3_ + 1.1F), (double)((float)p_71862_4_ + p_71862_5_.nextFloat()), 0.0D, 0.0D, 0.0D);
      }

   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Block.field_71979_v.func_71885_a(0, p_71885_2_, p_71885_3_);
   }
}
