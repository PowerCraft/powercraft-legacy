package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.BlockSourceImpl;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBehaviorDispenseItem;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.IPosition;
import net.minecraft.src.IRegistry;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PositionImpl;
import net.minecraft.src.RegistryDefaulted;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.World;

public class BlockDispenser extends BlockContainer {

   public static final IRegistry field_82527_a = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
   private Random field_72284_a = new Random();


   protected BlockDispenser(int p_i3938_1_) {
      super(p_i3938_1_, Material.field_76246_e);
      this.field_72059_bZ = 45;
      this.func_71849_a(CreativeTabs.field_78028_d);
   }

   public int func_71859_p_() {
      return 4;
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Block.field_71958_P.field_71990_ca;
   }

   public void func_71861_g(World p_71861_1_, int p_71861_2_, int p_71861_3_, int p_71861_4_) {
      super.func_71861_g(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_);
      this.func_72280_l(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_);
   }

   private void func_72280_l(World p_72280_1_, int p_72280_2_, int p_72280_3_, int p_72280_4_) {
      if(!p_72280_1_.field_72995_K) {
         int var5 = p_72280_1_.func_72798_a(p_72280_2_, p_72280_3_, p_72280_4_ - 1);
         int var6 = p_72280_1_.func_72798_a(p_72280_2_, p_72280_3_, p_72280_4_ + 1);
         int var7 = p_72280_1_.func_72798_a(p_72280_2_ - 1, p_72280_3_, p_72280_4_);
         int var8 = p_72280_1_.func_72798_a(p_72280_2_ + 1, p_72280_3_, p_72280_4_);
         byte var9 = 3;
         if(Block.field_71970_n[var5] && !Block.field_71970_n[var6]) {
            var9 = 3;
         }

         if(Block.field_71970_n[var6] && !Block.field_71970_n[var5]) {
            var9 = 2;
         }

         if(Block.field_71970_n[var7] && !Block.field_71970_n[var8]) {
            var9 = 5;
         }

         if(Block.field_71970_n[var8] && !Block.field_71970_n[var7]) {
            var9 = 4;
         }

         p_72280_1_.func_72921_c(p_72280_2_, p_72280_3_, p_72280_4_, var9);
      }
   }

   @SideOnly(Side.CLIENT)
   public int func_71895_b(IBlockAccess p_71895_1_, int p_71895_2_, int p_71895_3_, int p_71895_4_, int p_71895_5_) {
      if(p_71895_5_ == 1) {
         return this.field_72059_bZ + 17;
      } else if(p_71895_5_ == 0) {
         return this.field_72059_bZ + 17;
      } else {
         int var6 = p_71895_1_.func_72805_g(p_71895_2_, p_71895_3_, p_71895_4_);
         return p_71895_5_ == var6?this.field_72059_bZ + 1:this.field_72059_bZ;
      }
   }

   public int func_71851_a(int p_71851_1_) {
      return p_71851_1_ == 1?this.field_72059_bZ + 17:(p_71851_1_ == 0?this.field_72059_bZ + 17:(p_71851_1_ == 3?this.field_72059_bZ + 1:this.field_72059_bZ));
   }

   public boolean func_71903_a(World p_71903_1_, int p_71903_2_, int p_71903_3_, int p_71903_4_, EntityPlayer p_71903_5_, int p_71903_6_, float p_71903_7_, float p_71903_8_, float p_71903_9_) {
      if(p_71903_1_.field_72995_K) {
         return true;
      } else {
         TileEntityDispenser var10 = (TileEntityDispenser)p_71903_1_.func_72796_p(p_71903_2_, p_71903_3_, p_71903_4_);
         if(var10 != null) {
            p_71903_5_.func_71006_a(var10);
         }

         return true;
      }
   }

   private void func_82526_n(World p_82526_1_, int p_82526_2_, int p_82526_3_, int p_82526_4_) {
      BlockSourceImpl var5 = new BlockSourceImpl(p_82526_1_, p_82526_2_, p_82526_3_, p_82526_4_);
      TileEntityDispenser var6 = (TileEntityDispenser)var5.func_82619_j();
      if(var6 != null) {
         int var7 = var6.func_70361_i();
         if(var7 < 0) {
            p_82526_1_.func_72926_e(1001, p_82526_2_, p_82526_3_, p_82526_4_, 0);
         } else {
            ItemStack var8 = var6.func_70301_a(var7);
            IBehaviorDispenseItem var9 = (IBehaviorDispenseItem)field_82527_a.func_82594_a(var8.func_77973_b());
            if(var9 != IBehaviorDispenseItem.field_82483_a) {
               ItemStack var10 = var9.func_82482_a(var5, var8);
               var6.func_70299_a(var7, var10.field_77994_a == 0?null:var10);
            }
         }

      }
   }

   public void func_71863_a(World p_71863_1_, int p_71863_2_, int p_71863_3_, int p_71863_4_, int p_71863_5_) {
      if(p_71863_5_ > 0 && Block.field_71973_m[p_71863_5_].func_71853_i()) {
         boolean var6 = p_71863_1_.func_72864_z(p_71863_2_, p_71863_3_, p_71863_4_) || p_71863_1_.func_72864_z(p_71863_2_, p_71863_3_ + 1, p_71863_4_);
         if(var6) {
            p_71863_1_.func_72836_a(p_71863_2_, p_71863_3_, p_71863_4_, this.field_71990_ca, this.func_71859_p_());
         }
      }

   }

   public void func_71847_b(World p_71847_1_, int p_71847_2_, int p_71847_3_, int p_71847_4_, Random p_71847_5_) {
      if(!p_71847_1_.field_72995_K && (p_71847_1_.func_72864_z(p_71847_2_, p_71847_3_, p_71847_4_) || p_71847_1_.func_72864_z(p_71847_2_, p_71847_3_ + 1, p_71847_4_))) {
         this.func_82526_n(p_71847_1_, p_71847_2_, p_71847_3_, p_71847_4_);
      }

   }

   public TileEntity func_72274_a(World p_72274_1_) {
      return new TileEntityDispenser();
   }

   public void func_71860_a(World p_71860_1_, int p_71860_2_, int p_71860_3_, int p_71860_4_, EntityLiving p_71860_5_) {
      int var6 = MathHelper.func_76128_c((double)(p_71860_5_.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
      if(var6 == 0) {
         p_71860_1_.func_72921_c(p_71860_2_, p_71860_3_, p_71860_4_, 2);
      }

      if(var6 == 1) {
         p_71860_1_.func_72921_c(p_71860_2_, p_71860_3_, p_71860_4_, 5);
      }

      if(var6 == 2) {
         p_71860_1_.func_72921_c(p_71860_2_, p_71860_3_, p_71860_4_, 3);
      }

      if(var6 == 3) {
         p_71860_1_.func_72921_c(p_71860_2_, p_71860_3_, p_71860_4_, 4);
      }

   }

   public void func_71852_a(World p_71852_1_, int p_71852_2_, int p_71852_3_, int p_71852_4_, int p_71852_5_, int p_71852_6_) {
      TileEntityDispenser var7 = (TileEntityDispenser)p_71852_1_.func_72796_p(p_71852_2_, p_71852_3_, p_71852_4_);
      if(var7 != null) {
         for(int var8 = 0; var8 < var7.func_70302_i_(); ++var8) {
            ItemStack var9 = var7.func_70301_a(var8);
            if(var9 != null) {
               float var10 = this.field_72284_a.nextFloat() * 0.8F + 0.1F;
               float var11 = this.field_72284_a.nextFloat() * 0.8F + 0.1F;
               float var12 = this.field_72284_a.nextFloat() * 0.8F + 0.1F;

               while(var9.field_77994_a > 0) {
                  int var13 = this.field_72284_a.nextInt(21) + 10;
                  if(var13 > var9.field_77994_a) {
                     var13 = var9.field_77994_a;
                  }

                  var9.field_77994_a -= var13;
                  EntityItem var14 = new EntityItem(p_71852_1_, (double)((float)p_71852_2_ + var10), (double)((float)p_71852_3_ + var11), (double)((float)p_71852_4_ + var12), new ItemStack(var9.field_77993_c, var13, var9.func_77960_j()));
                  if(var9.func_77942_o()) {
                     var14.field_70294_a.func_77982_d((NBTTagCompound)var9.func_77978_p().func_74737_b());
                  }

                  float var15 = 0.05F;
                  var14.field_70159_w = (double)((float)this.field_72284_a.nextGaussian() * var15);
                  var14.field_70181_x = (double)((float)this.field_72284_a.nextGaussian() * var15 + 0.2F);
                  var14.field_70179_y = (double)((float)this.field_72284_a.nextGaussian() * var15);
                  p_71852_1_.func_72838_d(var14);
               }
            }
         }
      }

      super.func_71852_a(p_71852_1_, p_71852_2_, p_71852_3_, p_71852_4_, p_71852_5_, p_71852_6_);
   }

   public static IPosition func_82525_a(IBlockSource p_82525_0_) {
      EnumFacing var1 = EnumFacing.func_82600_a(p_82525_0_.func_82620_h());
      double var2 = p_82525_0_.func_82615_a() + 0.6D * (double)var1.func_82601_c();
      double var4 = p_82525_0_.func_82617_b();
      double var6 = p_82525_0_.func_82616_c() + 0.6D * (double)var1.func_82599_e();
      return new PositionImpl(var2, var4, var6);
   }

}
