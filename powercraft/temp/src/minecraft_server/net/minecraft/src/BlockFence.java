package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockFence extends Block {

   public BlockFence(int p_i3947_1_, int p_i3947_2_) {
      super(p_i3947_1_, p_i3947_2_, Material.field_76245_d);
      this.func_71849_a(CreativeTabs.field_78031_c);
   }

   public BlockFence(int p_i3948_1_, int p_i3948_2_, Material p_i3948_3_) {
      super(p_i3948_1_, p_i3948_2_, p_i3948_3_);
      this.func_71849_a(CreativeTabs.field_78031_c);
   }

   public AxisAlignedBB func_71872_e(World p_71872_1_, int p_71872_2_, int p_71872_3_, int p_71872_4_) {
      boolean var5 = this.func_72250_d(p_71872_1_, p_71872_2_, p_71872_3_, p_71872_4_ - 1);
      boolean var6 = this.func_72250_d(p_71872_1_, p_71872_2_, p_71872_3_, p_71872_4_ + 1);
      boolean var7 = this.func_72250_d(p_71872_1_, p_71872_2_ - 1, p_71872_3_, p_71872_4_);
      boolean var8 = this.func_72250_d(p_71872_1_, p_71872_2_ + 1, p_71872_3_, p_71872_4_);
      float var9 = 0.375F;
      float var10 = 0.625F;
      float var11 = 0.375F;
      float var12 = 0.625F;
      if(var5) {
         var11 = 0.0F;
      }

      if(var6) {
         var12 = 1.0F;
      }

      if(var7) {
         var9 = 0.0F;
      }

      if(var8) {
         var10 = 1.0F;
      }

      return AxisAlignedBB.func_72332_a().func_72299_a((double)((float)p_71872_2_ + var9), (double)p_71872_3_, (double)((float)p_71872_4_ + var11), (double)((float)p_71872_2_ + var10), (double)((float)p_71872_3_ + 1.5F), (double)((float)p_71872_4_ + var12));
   }

   public void func_71902_a(IBlockAccess p_71902_1_, int p_71902_2_, int p_71902_3_, int p_71902_4_) {
      boolean var5 = this.func_72250_d(p_71902_1_, p_71902_2_, p_71902_3_, p_71902_4_ - 1);
      boolean var6 = this.func_72250_d(p_71902_1_, p_71902_2_, p_71902_3_, p_71902_4_ + 1);
      boolean var7 = this.func_72250_d(p_71902_1_, p_71902_2_ - 1, p_71902_3_, p_71902_4_);
      boolean var8 = this.func_72250_d(p_71902_1_, p_71902_2_ + 1, p_71902_3_, p_71902_4_);
      float var9 = 0.375F;
      float var10 = 0.625F;
      float var11 = 0.375F;
      float var12 = 0.625F;
      if(var5) {
         var11 = 0.0F;
      }

      if(var6) {
         var12 = 1.0F;
      }

      if(var7) {
         var9 = 0.0F;
      }

      if(var8) {
         var10 = 1.0F;
      }

      this.func_71905_a(var9, 0.0F, var11, var10, 1.0F, var12);
   }

   public boolean func_71926_d() {
      return false;
   }

   public boolean func_71886_c() {
      return false;
   }

   public boolean func_71918_c(IBlockAccess p_71918_1_, int p_71918_2_, int p_71918_3_, int p_71918_4_) {
      return false;
   }

   public int func_71857_b() {
      return 11;
   }

   public boolean func_72250_d(IBlockAccess p_72250_1_, int p_72250_2_, int p_72250_3_, int p_72250_4_) {
      int var5 = p_72250_1_.func_72798_a(p_72250_2_, p_72250_3_, p_72250_4_);
      if(var5 != this.field_71990_ca && var5 != Block.field_71993_bv.field_71990_ca) {
         Block var6 = Block.field_71973_m[var5];
         return var6 != null && var6.field_72018_cp.func_76218_k() && var6.func_71886_c()?var6.field_72018_cp != Material.field_76266_z:false;
      } else {
         return true;
      }
   }

   public static boolean func_72249_c(int p_72249_0_) {
      return p_72249_0_ == Block.field_72031_aZ.field_71990_ca || p_72249_0_ == Block.field_72098_bB.field_71990_ca;
   }

   @SideOnly(Side.CLIENT)
   public boolean func_71877_c(IBlockAccess p_71877_1_, int p_71877_2_, int p_71877_3_, int p_71877_4_, int p_71877_5_) {
      return true;
   }
}
