package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityWither;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySkull;
import net.minecraft.src.World;

public class BlockSkull extends BlockContainer {

   protected BlockSkull(int p_i5106_1_) {
      super(p_i5106_1_, Material.field_76265_p);
      this.field_72059_bZ = 104;
      this.func_71905_a(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
   }

   public int func_71857_b() {
      return -1;
   }

   public boolean func_71926_d() {
      return false;
   }

   public boolean func_71886_c() {
      return false;
   }

   public void func_71902_a(IBlockAccess p_71902_1_, int p_71902_2_, int p_71902_3_, int p_71902_4_) {
      int var5 = p_71902_1_.func_72805_g(p_71902_2_, p_71902_3_, p_71902_4_) & 7;
      switch(var5) {
      case 1:
      default:
         this.func_71905_a(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
         break;
      case 2:
         this.func_71905_a(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
         break;
      case 3:
         this.func_71905_a(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
         break;
      case 4:
         this.func_71905_a(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
         break;
      case 5:
         this.func_71905_a(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
      }

   }

   public AxisAlignedBB func_71872_e(World p_71872_1_, int p_71872_2_, int p_71872_3_, int p_71872_4_) {
      this.func_71902_a(p_71872_1_, p_71872_2_, p_71872_3_, p_71872_4_);
      return super.func_71872_e(p_71872_1_, p_71872_2_, p_71872_3_, p_71872_4_);
   }

   public void func_71860_a(World p_71860_1_, int p_71860_2_, int p_71860_3_, int p_71860_4_, EntityLiving p_71860_5_) {
      int var6 = MathHelper.func_76128_c((double)(p_71860_5_.field_70177_z * 4.0F / 360.0F) + 2.5D) & 3;
      p_71860_1_.func_72921_c(p_71860_2_, p_71860_3_, p_71860_4_, var6);
   }

   public TileEntity func_72274_a(World p_72274_1_) {
      return new TileEntitySkull();
   }

   @SideOnly(Side.CLIENT)
   public int func_71922_a(World p_71922_1_, int p_71922_2_, int p_71922_3_, int p_71922_4_) {
      return Item.field_82799_bQ.field_77779_bT;
   }

   public int func_71873_h(World p_71873_1_, int p_71873_2_, int p_71873_3_, int p_71873_4_) {
      TileEntity var5 = p_71873_1_.func_72796_p(p_71873_2_, p_71873_3_, p_71873_4_);
      return var5 != null && var5 instanceof TileEntitySkull?((TileEntitySkull)var5).func_82117_a():super.func_71873_h(p_71873_1_, p_71873_2_, p_71873_3_, p_71873_4_);
   }

   public int func_71899_b(int p_71899_1_) {
      return p_71899_1_;
   }

   public void func_71914_a(World p_71914_1_, int p_71914_2_, int p_71914_3_, int p_71914_4_, int p_71914_5_, float p_71914_6_, int p_71914_7_) {}

   public void func_71846_a(World p_71846_1_, int p_71846_2_, int p_71846_3_, int p_71846_4_, int p_71846_5_, EntityPlayer p_71846_6_) {
      if(p_71846_6_.field_71075_bZ.field_75098_d) {
         p_71846_5_ |= 8;
         p_71846_1_.func_72921_c(p_71846_2_, p_71846_3_, p_71846_4_, p_71846_5_);
      }

      super.func_71846_a(p_71846_1_, p_71846_2_, p_71846_3_, p_71846_4_, p_71846_5_, p_71846_6_);
   }

   public void func_71852_a(World p_71852_1_, int p_71852_2_, int p_71852_3_, int p_71852_4_, int p_71852_5_, int p_71852_6_) {
      if(!p_71852_1_.field_72995_K) {
         if((p_71852_6_ & 8) == 0) {
            ItemStack var7 = new ItemStack(Item.field_82799_bQ.field_77779_bT, 1, this.func_71873_h(p_71852_1_, p_71852_2_, p_71852_3_, p_71852_4_));
            TileEntitySkull var8 = (TileEntitySkull)p_71852_1_.func_72796_p(p_71852_2_, p_71852_3_, p_71852_4_);
            if(var8.func_82117_a() == 3 && var8.func_82120_c() != null && var8.func_82120_c().length() > 0) {
               var7.func_77982_d(new NBTTagCompound());
               var7.func_77978_p().func_74778_a("SkullOwner", var8.func_82120_c());
            }

            this.func_71929_a(p_71852_1_, p_71852_2_, p_71852_3_, p_71852_4_, var7);
         }

         super.func_71852_a(p_71852_1_, p_71852_2_, p_71852_3_, p_71852_4_, p_71852_5_, p_71852_6_);
      }
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return Item.field_82799_bQ.field_77779_bT;
   }

   public void func_82529_a(World p_82529_1_, int p_82529_2_, int p_82529_3_, int p_82529_4_, TileEntitySkull p_82529_5_) {
      if(p_82529_5_.func_82117_a() == 1 && p_82529_3_ >= 2 && p_82529_1_.field_73013_u > 0) {
         int var6 = Block.field_72013_bc.field_71990_ca;

         int var7;
         EntityWither var8;
         int var9;
         for(var7 = -2; var7 <= 0; ++var7) {
            if(p_82529_1_.func_72798_a(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7) == var6 && p_82529_1_.func_72798_a(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7 + 1) == var6 && p_82529_1_.func_72798_a(p_82529_2_, p_82529_3_ - 2, p_82529_4_ + var7 + 1) == var6 && p_82529_1_.func_72798_a(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7 + 2) == var6 && this.func_82528_d(p_82529_1_, p_82529_2_, p_82529_3_, p_82529_4_ + var7, 1) && this.func_82528_d(p_82529_1_, p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 1, 1) && this.func_82528_d(p_82529_1_, p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 2, 1)) {
               p_82529_1_.func_72881_d(p_82529_2_, p_82529_3_, p_82529_4_ + var7, 8);
               p_82529_1_.func_72881_d(p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 1, 8);
               p_82529_1_.func_72881_d(p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 2, 8);
               p_82529_1_.func_72822_b(p_82529_2_, p_82529_3_, p_82529_4_ + var7, 0);
               p_82529_1_.func_72822_b(p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 1, 0);
               p_82529_1_.func_72822_b(p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 2, 0);
               p_82529_1_.func_72822_b(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7, 0);
               p_82529_1_.func_72822_b(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7 + 1, 0);
               p_82529_1_.func_72822_b(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7 + 2, 0);
               p_82529_1_.func_72822_b(p_82529_2_, p_82529_3_ - 2, p_82529_4_ + var7 + 1, 0);
               if(!p_82529_1_.field_72995_K) {
                  var8 = new EntityWither(p_82529_1_);
                  var8.func_70012_b((double)p_82529_2_ + 0.5D, (double)p_82529_3_ - 1.45D, (double)(p_82529_4_ + var7) + 1.5D, 90.0F, 0.0F);
                  var8.field_70761_aq = 90.0F;
                  var8.func_82206_m();
                  p_82529_1_.func_72838_d(var8);
               }

               for(var9 = 0; var9 < 120; ++var9) {
                  p_82529_1_.func_72869_a("snowballpoof", (double)p_82529_2_ + p_82529_1_.field_73012_v.nextDouble(), (double)(p_82529_3_ - 2) + p_82529_1_.field_73012_v.nextDouble() * 3.9D, (double)(p_82529_4_ + var7 + 1) + p_82529_1_.field_73012_v.nextDouble(), 0.0D, 0.0D, 0.0D);
               }

               p_82529_1_.func_72851_f(p_82529_2_, p_82529_3_, p_82529_4_ + var7, 0);
               p_82529_1_.func_72851_f(p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 1, 0);
               p_82529_1_.func_72851_f(p_82529_2_, p_82529_3_, p_82529_4_ + var7 + 2, 0);
               p_82529_1_.func_72851_f(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7, 0);
               p_82529_1_.func_72851_f(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7 + 1, 0);
               p_82529_1_.func_72851_f(p_82529_2_, p_82529_3_ - 1, p_82529_4_ + var7 + 2, 0);
               p_82529_1_.func_72851_f(p_82529_2_, p_82529_3_ - 2, p_82529_4_ + var7 + 1, 0);
               return;
            }
         }

         for(var7 = -2; var7 <= 0; ++var7) {
            if(p_82529_1_.func_72798_a(p_82529_2_ + var7, p_82529_3_ - 1, p_82529_4_) == var6 && p_82529_1_.func_72798_a(p_82529_2_ + var7 + 1, p_82529_3_ - 1, p_82529_4_) == var6 && p_82529_1_.func_72798_a(p_82529_2_ + var7 + 1, p_82529_3_ - 2, p_82529_4_) == var6 && p_82529_1_.func_72798_a(p_82529_2_ + var7 + 2, p_82529_3_ - 1, p_82529_4_) == var6 && this.func_82528_d(p_82529_1_, p_82529_2_ + var7, p_82529_3_, p_82529_4_, 1) && this.func_82528_d(p_82529_1_, p_82529_2_ + var7 + 1, p_82529_3_, p_82529_4_, 1) && this.func_82528_d(p_82529_1_, p_82529_2_ + var7 + 2, p_82529_3_, p_82529_4_, 1)) {
               p_82529_1_.func_72881_d(p_82529_2_ + var7, p_82529_3_, p_82529_4_, 8);
               p_82529_1_.func_72881_d(p_82529_2_ + var7 + 1, p_82529_3_, p_82529_4_, 8);
               p_82529_1_.func_72881_d(p_82529_2_ + var7 + 2, p_82529_3_, p_82529_4_, 8);
               p_82529_1_.func_72822_b(p_82529_2_ + var7, p_82529_3_, p_82529_4_, 0);
               p_82529_1_.func_72822_b(p_82529_2_ + var7 + 1, p_82529_3_, p_82529_4_, 0);
               p_82529_1_.func_72822_b(p_82529_2_ + var7 + 2, p_82529_3_, p_82529_4_, 0);
               p_82529_1_.func_72822_b(p_82529_2_ + var7, p_82529_3_ - 1, p_82529_4_, 0);
               p_82529_1_.func_72822_b(p_82529_2_ + var7 + 1, p_82529_3_ - 1, p_82529_4_, 0);
               p_82529_1_.func_72822_b(p_82529_2_ + var7 + 2, p_82529_3_ - 1, p_82529_4_, 0);
               p_82529_1_.func_72822_b(p_82529_2_ + var7 + 1, p_82529_3_ - 2, p_82529_4_, 0);
               if(!p_82529_1_.field_72995_K) {
                  var8 = new EntityWither(p_82529_1_);
                  var8.func_70012_b((double)(p_82529_2_ + var7) + 1.5D, (double)p_82529_3_ - 1.45D, (double)p_82529_4_ + 0.5D, 0.0F, 0.0F);
                  var8.func_82206_m();
                  p_82529_1_.func_72838_d(var8);
               }

               for(var9 = 0; var9 < 120; ++var9) {
                  p_82529_1_.func_72869_a("snowballpoof", (double)(p_82529_2_ + var7 + 1) + p_82529_1_.field_73012_v.nextDouble(), (double)(p_82529_3_ - 2) + p_82529_1_.field_73012_v.nextDouble() * 3.9D, (double)p_82529_4_ + p_82529_1_.field_73012_v.nextDouble(), 0.0D, 0.0D, 0.0D);
               }

               p_82529_1_.func_72851_f(p_82529_2_ + var7, p_82529_3_, p_82529_4_, 0);
               p_82529_1_.func_72851_f(p_82529_2_ + var7 + 1, p_82529_3_, p_82529_4_, 0);
               p_82529_1_.func_72851_f(p_82529_2_ + var7 + 2, p_82529_3_, p_82529_4_, 0);
               p_82529_1_.func_72851_f(p_82529_2_ + var7, p_82529_3_ - 1, p_82529_4_, 0);
               p_82529_1_.func_72851_f(p_82529_2_ + var7 + 1, p_82529_3_ - 1, p_82529_4_, 0);
               p_82529_1_.func_72851_f(p_82529_2_ + var7 + 2, p_82529_3_ - 1, p_82529_4_, 0);
               p_82529_1_.func_72851_f(p_82529_2_ + var7 + 1, p_82529_3_ - 2, p_82529_4_, 0);
               return;
            }
         }
      }

   }

   private boolean func_82528_d(World p_82528_1_, int p_82528_2_, int p_82528_3_, int p_82528_4_, int p_82528_5_) {
      if(p_82528_1_.func_72798_a(p_82528_2_, p_82528_3_, p_82528_4_) != this.field_71990_ca) {
         return false;
      } else {
         TileEntity var6 = p_82528_1_.func_72796_p(p_82528_2_, p_82528_3_, p_82528_4_);
         return var6 != null && var6 instanceof TileEntitySkull?((TileEntitySkull)var6).func_82117_a() == p_82528_5_:false;
      }
   }
}
