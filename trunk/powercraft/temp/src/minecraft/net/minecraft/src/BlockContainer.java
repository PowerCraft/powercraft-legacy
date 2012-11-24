package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public abstract class BlockContainer extends Block {

   protected BlockContainer(int p_i3943_1_, Material p_i3943_2_) {
      super(p_i3943_1_, p_i3943_2_);
      this.field_72025_cg = true;
   }

   protected BlockContainer(int p_i3944_1_, int p_i3944_2_, Material p_i3944_3_) {
      super(p_i3944_1_, p_i3944_2_, p_i3944_3_);
      this.field_72025_cg = true;
   }

   public void func_71861_g(World p_71861_1_, int p_71861_2_, int p_71861_3_, int p_71861_4_) {
      super.func_71861_g(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_);
      p_71861_1_.func_72837_a(p_71861_2_, p_71861_3_, p_71861_4_, this.func_72274_a(p_71861_1_));
   }

   public void func_71852_a(World p_71852_1_, int p_71852_2_, int p_71852_3_, int p_71852_4_, int p_71852_5_, int p_71852_6_) {
      super.func_71852_a(p_71852_1_, p_71852_2_, p_71852_3_, p_71852_4_, p_71852_5_, p_71852_6_);
      p_71852_1_.func_72932_q(p_71852_2_, p_71852_3_, p_71852_4_);
   }

   public abstract TileEntity func_72274_a(World var1);

   public void func_71883_b(World p_71883_1_, int p_71883_2_, int p_71883_3_, int p_71883_4_, int p_71883_5_, int p_71883_6_) {
      super.func_71883_b(p_71883_1_, p_71883_2_, p_71883_3_, p_71883_4_, p_71883_5_, p_71883_6_);
      TileEntity var7 = p_71883_1_.func_72796_p(p_71883_2_, p_71883_3_, p_71883_4_);
      if(var7 != null) {
         var7.func_70315_b(p_71883_5_, p_71883_6_);
      }

   }
}
