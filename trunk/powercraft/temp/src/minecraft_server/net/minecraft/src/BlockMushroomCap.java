package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockMushroomCap extends Block {

   private int field_72197_a;


   public BlockMushroomCap(int p_i3958_1_, Material p_i3958_2_, int p_i3958_3_, int p_i3958_4_) {
      super(p_i3958_1_, p_i3958_3_, p_i3958_2_);
      this.field_72197_a = p_i3958_4_;
   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      return p_71858_2_ == 10 && p_71858_1_ > 1?this.field_72059_bZ - 1:(p_71858_2_ >= 1 && p_71858_2_ <= 9 && p_71858_1_ == 1?this.field_72059_bZ - 16 - this.field_72197_a:(p_71858_2_ >= 1 && p_71858_2_ <= 3 && p_71858_1_ == 2?this.field_72059_bZ - 16 - this.field_72197_a:(p_71858_2_ >= 7 && p_71858_2_ <= 9 && p_71858_1_ == 3?this.field_72059_bZ - 16 - this.field_72197_a:((p_71858_2_ == 1 || p_71858_2_ == 4 || p_71858_2_ == 7) && p_71858_1_ == 4?this.field_72059_bZ - 16 - this.field_72197_a:((p_71858_2_ == 3 || p_71858_2_ == 6 || p_71858_2_ == 9) && p_71858_1_ == 5?this.field_72059_bZ - 16 - this.field_72197_a:(p_71858_2_ == 14?this.field_72059_bZ - 16 - this.field_72197_a:(p_71858_2_ == 15?this.field_72059_bZ - 1:this.field_72059_bZ)))))));
   }

   public int func_71925_a(Random p_71925_1_) {
      int var2 = p_71925_1_.nextInt(10) - 7;
      if(var2 < 0) {
         var2 = 0;
      }

      return var2;
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Block.field_72109_af.field_71990_ca + this.field_72197_a;
   }

   @SideOnly(Side.CLIENT)
   public int func_71922_a(World p_71922_1_, int p_71922_2_, int p_71922_3_, int p_71922_4_) {
      return Block.field_72109_af.field_71990_ca + this.field_72197_a;
   }
}
