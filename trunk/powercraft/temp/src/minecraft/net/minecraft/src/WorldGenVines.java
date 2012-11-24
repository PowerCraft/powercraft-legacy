package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.Direction;
import net.minecraft.src.Facing;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class WorldGenVines extends WorldGenerator {

   public boolean func_76484_a(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
      int var6 = p_76484_3_;

      for(int var7 = p_76484_5_; p_76484_4_ < 128; ++p_76484_4_) {
         if(p_76484_1_.func_72799_c(p_76484_3_, p_76484_4_, p_76484_5_)) {
            for(int var8 = 2; var8 <= 5; ++var8) {
               if(Block.field_71998_bu.func_71850_a_(p_76484_1_, p_76484_3_, p_76484_4_, p_76484_5_, var8)) {
                  p_76484_1_.func_72961_c(p_76484_3_, p_76484_4_, p_76484_5_, Block.field_71998_bu.field_71990_ca, 1 << Direction.field_71579_d[Facing.field_71588_a[var8]]);
                  break;
               }
            }
         } else {
            p_76484_3_ = var6 + p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
            p_76484_5_ = var7 + p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
         }
      }

      return true;
   }
}
