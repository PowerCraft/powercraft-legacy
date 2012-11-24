package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityCommandBlock;
import net.minecraft.src.World;

public class BlockCommandBlock extends BlockContainer {

   public BlockCommandBlock(int p_i5102_1_) {
      super(p_i5102_1_, 184, Material.field_76243_f);
   }

   public TileEntity func_72274_a(World p_72274_1_) {
      return new TileEntityCommandBlock();
   }

   public void func_71863_a(World p_71863_1_, int p_71863_2_, int p_71863_3_, int p_71863_4_, int p_71863_5_) {
      if(!p_71863_1_.field_72995_K) {
         boolean var6 = p_71863_1_.func_72864_z(p_71863_2_, p_71863_3_, p_71863_4_);
         int var7 = p_71863_1_.func_72805_g(p_71863_2_, p_71863_3_, p_71863_4_);
         boolean var8 = (var7 & 1) != 0;
         if(var6 && !var8) {
            p_71863_1_.func_72881_d(p_71863_2_, p_71863_3_, p_71863_4_, var7 | 1);
            p_71863_1_.func_72836_a(p_71863_2_, p_71863_3_, p_71863_4_, this.field_71990_ca, this.func_71859_p_());
         } else if(!var6 && var8) {
            p_71863_1_.func_72881_d(p_71863_2_, p_71863_3_, p_71863_4_, var7 & -2);
         }
      }

   }

   public void func_71847_b(World p_71847_1_, int p_71847_2_, int p_71847_3_, int p_71847_4_, Random p_71847_5_) {
      TileEntity var6 = p_71847_1_.func_72796_p(p_71847_2_, p_71847_3_, p_71847_4_);
      if(var6 != null && var6 instanceof TileEntityCommandBlock) {
         ((TileEntityCommandBlock)var6).func_82351_a(p_71847_1_);
      }

   }

   public int func_71859_p_() {
      return 1;
   }

   public boolean func_71903_a(World p_71903_1_, int p_71903_2_, int p_71903_3_, int p_71903_4_, EntityPlayer p_71903_5_, int p_71903_6_, float p_71903_7_, float p_71903_8_, float p_71903_9_) {
      TileEntityCommandBlock var10 = (TileEntityCommandBlock)p_71903_1_.func_72796_p(p_71903_2_, p_71903_3_, p_71903_4_);
      if(var10 != null) {
         p_71903_5_.func_71014_a(var10);
      }

      return true;
   }
}
