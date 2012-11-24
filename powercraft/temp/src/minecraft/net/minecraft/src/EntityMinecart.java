package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class EntityMinecart extends Entity implements IInventory {

   protected ItemStack[] field_70501_d;
   protected int field_70502_e;
   protected boolean field_70499_f;
   public int field_70505_a;
   public double field_70503_b;
   public double field_70504_c;
   protected final IUpdatePlayerListBox field_82344_g;
   protected boolean field_82345_h;
   protected static final int[][][] field_70500_g = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
   protected int field_70510_h;
   protected double field_70511_i;
   protected double field_70509_j;
   protected double field_70514_an;
   protected double field_70512_ao;
   protected double field_70513_ap;
   @SideOnly(Side.CLIENT)
   protected double field_70508_aq;
   @SideOnly(Side.CLIENT)
   protected double field_70507_ar;
   @SideOnly(Side.CLIENT)
   protected double field_70506_as;


   public EntityMinecart(World p_i3541_1_) {
      super(p_i3541_1_);
      this.field_70501_d = new ItemStack[36];
      this.field_70502_e = 0;
      this.field_70499_f = false;
      this.field_82345_h = true;
      this.field_70156_m = true;
      this.func_70105_a(0.98F, 0.7F);
      this.field_70129_M = this.field_70131_O / 2.0F;
      this.field_82344_g = p_i3541_1_ != null?p_i3541_1_.func_82735_a(this):null;
   }

   protected boolean func_70041_e_() {
      return false;
   }

   protected void func_70088_a() {
      this.field_70180_af.func_75682_a(16, new Byte((byte)0));
      this.field_70180_af.func_75682_a(17, new Integer(0));
      this.field_70180_af.func_75682_a(18, new Integer(1));
      this.field_70180_af.func_75682_a(19, new Integer(0));
   }

   public AxisAlignedBB func_70114_g(Entity p_70114_1_) {
      return p_70114_1_.func_70104_M()?p_70114_1_.field_70121_D:null;
   }

   public AxisAlignedBB func_70046_E() {
      return null;
   }

   public boolean func_70104_M() {
      return true;
   }

   public EntityMinecart(World p_i3542_1_, double p_i3542_2_, double p_i3542_4_, double p_i3542_6_, int p_i3542_8_) {
      this(p_i3542_1_);
      this.func_70107_b(p_i3542_2_, p_i3542_4_ + (double)this.field_70129_M, p_i3542_6_);
      this.field_70159_w = 0.0D;
      this.field_70181_x = 0.0D;
      this.field_70179_y = 0.0D;
      this.field_70169_q = p_i3542_2_;
      this.field_70167_r = p_i3542_4_;
      this.field_70166_s = p_i3542_6_;
      this.field_70505_a = p_i3542_8_;
   }

   public double func_70042_X() {
      return (double)this.field_70131_O * 0.0D - 0.30000001192092896D;
   }

   public boolean func_70097_a(DamageSource p_70097_1_, int p_70097_2_) {
      if(!this.field_70170_p.field_72995_K && !this.field_70128_L) {
         if(this.func_85032_ar()) {
            return false;
         } else {
            this.func_70494_i(-this.func_70493_k());
            this.func_70497_h(10);
            this.func_70018_K();
            this.func_70492_c(this.func_70491_i() + p_70097_2_ * 10);
            if(p_70097_1_.func_76346_g() instanceof EntityPlayer && ((EntityPlayer)p_70097_1_.func_76346_g()).field_71075_bZ.field_75098_d) {
               this.func_70492_c(100);
            }

            if(this.func_70491_i() > 40) {
               if(this.field_70153_n != null) {
                  this.field_70153_n.func_70078_a(this);
               }

               this.func_70106_y();
               this.func_70054_a(Item.field_77773_az.field_77779_bT, 1, 0.0F);
               if(this.field_70505_a == 1) {
                  EntityMinecart var3 = this;

                  for(int var4 = 0; var4 < var3.func_70302_i_(); ++var4) {
                     ItemStack var5 = var3.func_70301_a(var4);
                     if(var5 != null) {
                        float var6 = this.field_70146_Z.nextFloat() * 0.8F + 0.1F;
                        float var7 = this.field_70146_Z.nextFloat() * 0.8F + 0.1F;
                        float var8 = this.field_70146_Z.nextFloat() * 0.8F + 0.1F;

                        while(var5.field_77994_a > 0) {
                           int var9 = this.field_70146_Z.nextInt(21) + 10;
                           if(var9 > var5.field_77994_a) {
                              var9 = var5.field_77994_a;
                           }

                           var5.field_77994_a -= var9;
                           EntityItem var10 = new EntityItem(this.field_70170_p, this.field_70165_t + (double)var6, this.field_70163_u + (double)var7, this.field_70161_v + (double)var8, new ItemStack(var5.field_77993_c, var9, var5.func_77960_j()));
                           float var11 = 0.05F;
                           var10.field_70159_w = (double)((float)this.field_70146_Z.nextGaussian() * var11);
                           var10.field_70181_x = (double)((float)this.field_70146_Z.nextGaussian() * var11 + 0.2F);
                           var10.field_70179_y = (double)((float)this.field_70146_Z.nextGaussian() * var11);
                           this.field_70170_p.func_72838_d(var10);
                        }
                     }
                  }

                  this.func_70054_a(Block.field_72077_au.field_71990_ca, 1, 0.0F);
               } else if(this.field_70505_a == 2) {
                  this.func_70054_a(Block.field_72051_aB.field_71990_ca, 1, 0.0F);
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   @SideOnly(Side.CLIENT)
   public void func_70057_ab() {
      this.func_70494_i(-this.func_70493_k());
      this.func_70497_h(10);
      this.func_70492_c(this.func_70491_i() + this.func_70491_i() * 10);
   }

   public boolean func_70067_L() {
      return !this.field_70128_L;
   }

   public void func_70106_y() {
      if(this.field_82345_h) {
         for(int var1 = 0; var1 < this.func_70302_i_(); ++var1) {
            ItemStack var2 = this.func_70301_a(var1);
            if(var2 != null) {
               float var3 = this.field_70146_Z.nextFloat() * 0.8F + 0.1F;
               float var4 = this.field_70146_Z.nextFloat() * 0.8F + 0.1F;
               float var5 = this.field_70146_Z.nextFloat() * 0.8F + 0.1F;

               while(var2.field_77994_a > 0) {
                  int var6 = this.field_70146_Z.nextInt(21) + 10;
                  if(var6 > var2.field_77994_a) {
                     var6 = var2.field_77994_a;
                  }

                  var2.field_77994_a -= var6;
                  EntityItem var7 = new EntityItem(this.field_70170_p, this.field_70165_t + (double)var3, this.field_70163_u + (double)var4, this.field_70161_v + (double)var5, new ItemStack(var2.field_77993_c, var6, var2.func_77960_j()));
                  if(var2.func_77942_o()) {
                     var7.field_70294_a.func_77982_d((NBTTagCompound)var2.func_77978_p().func_74737_b());
                  }

                  float var8 = 0.05F;
                  var7.field_70159_w = (double)((float)this.field_70146_Z.nextGaussian() * var8);
                  var7.field_70181_x = (double)((float)this.field_70146_Z.nextGaussian() * var8 + 0.2F);
                  var7.field_70179_y = (double)((float)this.field_70146_Z.nextGaussian() * var8);
                  this.field_70170_p.func_72838_d(var7);
               }
            }
         }
      }

      super.func_70106_y();
      if(this.field_82344_g != null) {
         this.field_82344_g.func_73660_a();
      }

   }

   public void func_71027_c(int p_71027_1_) {
      this.field_82345_h = false;
      super.func_71027_c(p_71027_1_);
   }

   public void func_70071_h_() {
      if(this.field_82344_g != null) {
         this.field_82344_g.func_73660_a();
      }

      if(this.func_70496_j() > 0) {
         this.func_70497_h(this.func_70496_j() - 1);
      }

      if(this.func_70491_i() > 0) {
         this.func_70492_c(this.func_70491_i() - 1);
      }

      if(this.field_70163_u < -64.0D) {
         this.func_70076_C();
      }

      if(this.func_70490_h() && this.field_70146_Z.nextInt(4) == 0) {
         this.field_70170_p.func_72869_a("largesmoke", this.field_70165_t, this.field_70163_u + 0.8D, this.field_70161_v, 0.0D, 0.0D, 0.0D);
      }

      if(this.field_70170_p.field_72995_K) {
         if(this.field_70510_h > 0) {
            double var45 = this.field_70165_t + (this.field_70511_i - this.field_70165_t) / (double)this.field_70510_h;
            double var46 = this.field_70163_u + (this.field_70509_j - this.field_70163_u) / (double)this.field_70510_h;
            double var5 = this.field_70161_v + (this.field_70514_an - this.field_70161_v) / (double)this.field_70510_h;
            double var7 = MathHelper.func_76138_g(this.field_70512_ao - (double)this.field_70177_z);
            this.field_70177_z = (float)((double)this.field_70177_z + var7 / (double)this.field_70510_h);
            this.field_70125_A = (float)((double)this.field_70125_A + (this.field_70513_ap - (double)this.field_70125_A) / (double)this.field_70510_h);
            --this.field_70510_h;
            this.func_70107_b(var45, var46, var5);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
         } else {
            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
         }

      } else {
         this.field_70169_q = this.field_70165_t;
         this.field_70167_r = this.field_70163_u;
         this.field_70166_s = this.field_70161_v;
         this.field_70181_x -= 0.03999999910593033D;
         int var1 = MathHelper.func_76128_c(this.field_70165_t);
         int var2 = MathHelper.func_76128_c(this.field_70163_u);
         int var3 = MathHelper.func_76128_c(this.field_70161_v);
         if(BlockRail.func_72180_d_(this.field_70170_p, var1, var2 - 1, var3)) {
            --var2;
         }

         double var4 = 0.4D;
         double var6 = 0.0078125D;
         int var8 = this.field_70170_p.func_72798_a(var1, var2, var3);
         if(BlockRail.func_72184_d(var8)) {
            this.field_70143_R = 0.0F;
            Vec3 var9 = this.func_70489_a(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            int var10 = this.field_70170_p.func_72805_g(var1, var2, var3);
            this.field_70163_u = (double)var2;
            boolean var11 = false;
            boolean var12 = false;
            if(var8 == Block.field_71954_T.field_71990_ca) {
               var11 = (var10 & 8) != 0;
               var12 = !var11;
            }

            if(((BlockRail)Block.field_71973_m[var8]).func_72183_n()) {
               var10 &= 7;
            }

            if(var10 >= 2 && var10 <= 5) {
               this.field_70163_u = (double)(var2 + 1);
            }

            if(var10 == 2) {
               this.field_70159_w -= var6;
            }

            if(var10 == 3) {
               this.field_70159_w += var6;
            }

            if(var10 == 4) {
               this.field_70179_y += var6;
            }

            if(var10 == 5) {
               this.field_70179_y -= var6;
            }

            int[][] var13 = field_70500_g[var10];
            double var14 = (double)(var13[1][0] - var13[0][0]);
            double var16 = (double)(var13[1][2] - var13[0][2]);
            double var18 = Math.sqrt(var14 * var14 + var16 * var16);
            double var20 = this.field_70159_w * var14 + this.field_70179_y * var16;
            if(var20 < 0.0D) {
               var14 = -var14;
               var16 = -var16;
            }

            double var22 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            this.field_70159_w = var22 * var14 / var18;
            this.field_70179_y = var22 * var16 / var18;
            double var24;
            double var26;
            if(this.field_70153_n != null) {
               var24 = this.field_70153_n.field_70159_w * this.field_70153_n.field_70159_w + this.field_70153_n.field_70179_y * this.field_70153_n.field_70179_y;
               var26 = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;
               if(var24 > 1.0E-4D && var26 < 0.01D) {
                  this.field_70159_w += this.field_70153_n.field_70159_w * 0.1D;
                  this.field_70179_y += this.field_70153_n.field_70179_y * 0.1D;
                  var12 = false;
               }
            }

            if(var12) {
               var24 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
               if(var24 < 0.03D) {
                  this.field_70159_w *= 0.0D;
                  this.field_70181_x *= 0.0D;
                  this.field_70179_y *= 0.0D;
               } else {
                  this.field_70159_w *= 0.5D;
                  this.field_70181_x *= 0.0D;
                  this.field_70179_y *= 0.5D;
               }
            }

            var24 = 0.0D;
            var26 = (double)var1 + 0.5D + (double)var13[0][0] * 0.5D;
            double var28 = (double)var3 + 0.5D + (double)var13[0][2] * 0.5D;
            double var30 = (double)var1 + 0.5D + (double)var13[1][0] * 0.5D;
            double var32 = (double)var3 + 0.5D + (double)var13[1][2] * 0.5D;
            var14 = var30 - var26;
            var16 = var32 - var28;
            double var34;
            double var36;
            if(var14 == 0.0D) {
               this.field_70165_t = (double)var1 + 0.5D;
               var24 = this.field_70161_v - (double)var3;
            } else if(var16 == 0.0D) {
               this.field_70161_v = (double)var3 + 0.5D;
               var24 = this.field_70165_t - (double)var1;
            } else {
               var34 = this.field_70165_t - var26;
               var36 = this.field_70161_v - var28;
               var24 = (var34 * var14 + var36 * var16) * 2.0D;
            }

            this.field_70165_t = var26 + var14 * var24;
            this.field_70161_v = var28 + var16 * var24;
            this.func_70107_b(this.field_70165_t, this.field_70163_u + (double)this.field_70129_M, this.field_70161_v);
            var34 = this.field_70159_w;
            var36 = this.field_70179_y;
            if(this.field_70153_n != null) {
               var34 *= 0.75D;
               var36 *= 0.75D;
            }

            if(var34 < -var4) {
               var34 = -var4;
            }

            if(var34 > var4) {
               var34 = var4;
            }

            if(var36 < -var4) {
               var36 = -var4;
            }

            if(var36 > var4) {
               var36 = var4;
            }

            this.func_70091_d(var34, 0.0D, var36);
            if(var13[0][1] != 0 && MathHelper.func_76128_c(this.field_70165_t) - var1 == var13[0][0] && MathHelper.func_76128_c(this.field_70161_v) - var3 == var13[0][2]) {
               this.func_70107_b(this.field_70165_t, this.field_70163_u + (double)var13[0][1], this.field_70161_v);
            } else if(var13[1][1] != 0 && MathHelper.func_76128_c(this.field_70165_t) - var1 == var13[1][0] && MathHelper.func_76128_c(this.field_70161_v) - var3 == var13[1][2]) {
               this.func_70107_b(this.field_70165_t, this.field_70163_u + (double)var13[1][1], this.field_70161_v);
            }

            if(this.field_70153_n != null) {
               this.field_70159_w *= 0.996999979019165D;
               this.field_70181_x *= 0.0D;
               this.field_70179_y *= 0.996999979019165D;
            } else {
               if(this.field_70505_a == 2) {
                  double var38 = this.field_70503_b * this.field_70503_b + this.field_70504_c * this.field_70504_c;
                  if(var38 > 1.0E-4D) {
                     var38 = (double)MathHelper.func_76133_a(var38);
                     this.field_70503_b /= var38;
                     this.field_70504_c /= var38;
                     double var40 = 0.04D;
                     this.field_70159_w *= 0.800000011920929D;
                     this.field_70181_x *= 0.0D;
                     this.field_70179_y *= 0.800000011920929D;
                     this.field_70159_w += this.field_70503_b * var40;
                     this.field_70179_y += this.field_70504_c * var40;
                  } else {
                     this.field_70159_w *= 0.8999999761581421D;
                     this.field_70181_x *= 0.0D;
                     this.field_70179_y *= 0.8999999761581421D;
                  }
               }

               this.field_70159_w *= 0.9599999785423279D;
               this.field_70181_x *= 0.0D;
               this.field_70179_y *= 0.9599999785423279D;
            }

            Vec3 var52 = this.func_70489_a(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            if(var52 != null && var9 != null) {
               double var39 = (var9.field_72448_b - var52.field_72448_b) * 0.05D;
               var22 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
               if(var22 > 0.0D) {
                  this.field_70159_w = this.field_70159_w / var22 * (var22 + var39);
                  this.field_70179_y = this.field_70179_y / var22 * (var22 + var39);
               }

               this.func_70107_b(this.field_70165_t, var52.field_72448_b, this.field_70161_v);
            }

            int var51 = MathHelper.func_76128_c(this.field_70165_t);
            int var53 = MathHelper.func_76128_c(this.field_70161_v);
            if(var51 != var1 || var53 != var3) {
               var22 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
               this.field_70159_w = var22 * (double)(var51 - var1);
               this.field_70179_y = var22 * (double)(var53 - var3);
            }

            double var41;
            if(this.field_70505_a == 2) {
               var41 = this.field_70503_b * this.field_70503_b + this.field_70504_c * this.field_70504_c;
               if(var41 > 1.0E-4D && this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 0.001D) {
                  var41 = (double)MathHelper.func_76133_a(var41);
                  this.field_70503_b /= var41;
                  this.field_70504_c /= var41;
                  if(this.field_70503_b * this.field_70159_w + this.field_70504_c * this.field_70179_y < 0.0D) {
                     this.field_70503_b = 0.0D;
                     this.field_70504_c = 0.0D;
                  } else {
                     this.field_70503_b = this.field_70159_w;
                     this.field_70504_c = this.field_70179_y;
                  }
               }
            }

            if(var11) {
               var41 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
               if(var41 > 0.01D) {
                  double var43 = 0.06D;
                  this.field_70159_w += this.field_70159_w / var41 * var43;
                  this.field_70179_y += this.field_70179_y / var41 * var43;
               } else if(var10 == 1) {
                  if(this.field_70170_p.func_72809_s(var1 - 1, var2, var3)) {
                     this.field_70159_w = 0.02D;
                  } else if(this.field_70170_p.func_72809_s(var1 + 1, var2, var3)) {
                     this.field_70159_w = -0.02D;
                  }
               } else if(var10 == 0) {
                  if(this.field_70170_p.func_72809_s(var1, var2, var3 - 1)) {
                     this.field_70179_y = 0.02D;
                  } else if(this.field_70170_p.func_72809_s(var1, var2, var3 + 1)) {
                     this.field_70179_y = -0.02D;
                  }
               }
            }
         } else {
            if(this.field_70159_w < -var4) {
               this.field_70159_w = -var4;
            }

            if(this.field_70159_w > var4) {
               this.field_70159_w = var4;
            }

            if(this.field_70179_y < -var4) {
               this.field_70179_y = -var4;
            }

            if(this.field_70179_y > var4) {
               this.field_70179_y = var4;
            }

            if(this.field_70122_E) {
               this.field_70159_w *= 0.5D;
               this.field_70181_x *= 0.5D;
               this.field_70179_y *= 0.5D;
            }

            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            if(!this.field_70122_E) {
               this.field_70159_w *= 0.949999988079071D;
               this.field_70181_x *= 0.949999988079071D;
               this.field_70179_y *= 0.949999988079071D;
            }
         }

         this.func_70017_D();
         this.field_70125_A = 0.0F;
         double var47 = this.field_70169_q - this.field_70165_t;
         double var48 = this.field_70166_s - this.field_70161_v;
         if(var47 * var47 + var48 * var48 > 0.001D) {
            this.field_70177_z = (float)(Math.atan2(var48, var47) * 180.0D / 3.141592653589793D);
            if(this.field_70499_f) {
               this.field_70177_z += 180.0F;
            }
         }

         double var49 = (double)MathHelper.func_76142_g(this.field_70177_z - this.field_70126_B);
         if(var49 < -170.0D || var49 >= 170.0D) {
            this.field_70177_z += 180.0F;
            this.field_70499_f = !this.field_70499_f;
         }

         this.func_70101_b(this.field_70177_z, this.field_70125_A);
         List var15 = this.field_70170_p.func_72839_b(this, this.field_70121_D.func_72314_b(0.20000000298023224D, 0.0D, 0.20000000298023224D));
         if(var15 != null && !var15.isEmpty()) {
            for(int var50 = 0; var50 < var15.size(); ++var50) {
               Entity var17 = (Entity)var15.get(var50);
               if(var17 != this.field_70153_n && var17.func_70104_M() && var17 instanceof EntityMinecart) {
                  var17.func_70108_f(this);
               }
            }
         }

         if(this.field_70153_n != null && this.field_70153_n.field_70128_L) {
            if(this.field_70153_n.field_70154_o == this) {
               this.field_70153_n.field_70154_o = null;
            }

            this.field_70153_n = null;
         }

         if(this.field_70502_e > 0) {
            --this.field_70502_e;
         }

         if(this.field_70502_e <= 0) {
            this.field_70503_b = this.field_70504_c = 0.0D;
         }

         this.func_70498_d(this.field_70502_e > 0);
      }
   }

   @SideOnly(Side.CLIENT)
   public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_) {
      int var9 = MathHelper.func_76128_c(p_70495_1_);
      int var10 = MathHelper.func_76128_c(p_70495_3_);
      int var11 = MathHelper.func_76128_c(p_70495_5_);
      if(BlockRail.func_72180_d_(this.field_70170_p, var9, var10 - 1, var11)) {
         --var10;
      }

      int var12 = this.field_70170_p.func_72798_a(var9, var10, var11);
      if(!BlockRail.func_72184_d(var12)) {
         return null;
      } else {
         int var13 = this.field_70170_p.func_72805_g(var9, var10, var11);
         if(((BlockRail)Block.field_71973_m[var12]).func_72183_n()) {
            var13 &= 7;
         }

         p_70495_3_ = (double)var10;
         if(var13 >= 2 && var13 <= 5) {
            p_70495_3_ = (double)(var10 + 1);
         }

         int[][] var14 = field_70500_g[var13];
         double var15 = (double)(var14[1][0] - var14[0][0]);
         double var17 = (double)(var14[1][2] - var14[0][2]);
         double var19 = Math.sqrt(var15 * var15 + var17 * var17);
         var15 /= var19;
         var17 /= var19;
         p_70495_1_ += var15 * p_70495_7_;
         p_70495_5_ += var17 * p_70495_7_;
         if(var14[0][1] != 0 && MathHelper.func_76128_c(p_70495_1_) - var9 == var14[0][0] && MathHelper.func_76128_c(p_70495_5_) - var11 == var14[0][2]) {
            p_70495_3_ += (double)var14[0][1];
         } else if(var14[1][1] != 0 && MathHelper.func_76128_c(p_70495_1_) - var9 == var14[1][0] && MathHelper.func_76128_c(p_70495_5_) - var11 == var14[1][2]) {
            p_70495_3_ += (double)var14[1][1];
         }

         return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
      }
   }

   public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
      int var7 = MathHelper.func_76128_c(p_70489_1_);
      int var8 = MathHelper.func_76128_c(p_70489_3_);
      int var9 = MathHelper.func_76128_c(p_70489_5_);
      if(BlockRail.func_72180_d_(this.field_70170_p, var7, var8 - 1, var9)) {
         --var8;
      }

      int var10 = this.field_70170_p.func_72798_a(var7, var8, var9);
      if(BlockRail.func_72184_d(var10)) {
         int var11 = this.field_70170_p.func_72805_g(var7, var8, var9);
         p_70489_3_ = (double)var8;
         if(((BlockRail)Block.field_71973_m[var10]).func_72183_n()) {
            var11 &= 7;
         }

         if(var11 >= 2 && var11 <= 5) {
            p_70489_3_ = (double)(var8 + 1);
         }

         int[][] var12 = field_70500_g[var11];
         double var13 = 0.0D;
         double var15 = (double)var7 + 0.5D + (double)var12[0][0] * 0.5D;
         double var17 = (double)var8 + 0.5D + (double)var12[0][1] * 0.5D;
         double var19 = (double)var9 + 0.5D + (double)var12[0][2] * 0.5D;
         double var21 = (double)var7 + 0.5D + (double)var12[1][0] * 0.5D;
         double var23 = (double)var8 + 0.5D + (double)var12[1][1] * 0.5D;
         double var25 = (double)var9 + 0.5D + (double)var12[1][2] * 0.5D;
         double var27 = var21 - var15;
         double var29 = (var23 - var17) * 2.0D;
         double var31 = var25 - var19;
         if(var27 == 0.0D) {
            p_70489_1_ = (double)var7 + 0.5D;
            var13 = p_70489_5_ - (double)var9;
         } else if(var31 == 0.0D) {
            p_70489_5_ = (double)var9 + 0.5D;
            var13 = p_70489_1_ - (double)var7;
         } else {
            double var33 = p_70489_1_ - var15;
            double var35 = p_70489_5_ - var19;
            var13 = (var33 * var27 + var35 * var31) * 2.0D;
         }

         p_70489_1_ = var15 + var27 * var13;
         p_70489_3_ = var17 + var29 * var13;
         p_70489_5_ = var19 + var31 * var13;
         if(var29 < 0.0D) {
            ++p_70489_3_;
         }

         if(var29 > 0.0D) {
            p_70489_3_ += 0.5D;
         }

         return this.field_70170_p.func_82732_R().func_72345_a(p_70489_1_, p_70489_3_, p_70489_5_);
      } else {
         return null;
      }
   }

   protected void func_70014_b(NBTTagCompound p_70014_1_) {
      p_70014_1_.func_74768_a("Type", this.field_70505_a);
      if(this.field_70505_a == 2) {
         p_70014_1_.func_74780_a("PushX", this.field_70503_b);
         p_70014_1_.func_74780_a("PushZ", this.field_70504_c);
         p_70014_1_.func_74777_a("Fuel", (short)this.field_70502_e);
      } else if(this.field_70505_a == 1) {
         NBTTagList var2 = new NBTTagList();

         for(int var3 = 0; var3 < this.field_70501_d.length; ++var3) {
            if(this.field_70501_d[var3] != null) {
               NBTTagCompound var4 = new NBTTagCompound();
               var4.func_74774_a("Slot", (byte)var3);
               this.field_70501_d[var3].func_77955_b(var4);
               var2.func_74742_a(var4);
            }
         }

         p_70014_1_.func_74782_a("Items", var2);
      }

   }

   protected void func_70037_a(NBTTagCompound p_70037_1_) {
      this.field_70505_a = p_70037_1_.func_74762_e("Type");
      if(this.field_70505_a == 2) {
         this.field_70503_b = p_70037_1_.func_74769_h("PushX");
         this.field_70504_c = p_70037_1_.func_74769_h("PushZ");
         this.field_70502_e = p_70037_1_.func_74765_d("Fuel");
      } else if(this.field_70505_a == 1) {
         NBTTagList var2 = p_70037_1_.func_74761_m("Items");
         this.field_70501_d = new ItemStack[this.func_70302_i_()];

         for(int var3 = 0; var3 < var2.func_74745_c(); ++var3) {
            NBTTagCompound var4 = (NBTTagCompound)var2.func_74743_b(var3);
            int var5 = var4.func_74771_c("Slot") & 255;
            if(var5 >= 0 && var5 < this.field_70501_d.length) {
               this.field_70501_d[var5] = ItemStack.func_77949_a(var4);
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public float func_70053_R() {
      return 0.0F;
   }

   public void func_70108_f(Entity p_70108_1_) {
      if(!this.field_70170_p.field_72995_K) {
         if(p_70108_1_ != this.field_70153_n) {
            if(p_70108_1_ instanceof EntityLiving && !(p_70108_1_ instanceof EntityPlayer) && !(p_70108_1_ instanceof EntityIronGolem) && this.field_70505_a == 0 && this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 0.01D && this.field_70153_n == null && p_70108_1_.field_70154_o == null) {
               p_70108_1_.func_70078_a(this);
            }

            double var2 = p_70108_1_.field_70165_t - this.field_70165_t;
            double var4 = p_70108_1_.field_70161_v - this.field_70161_v;
            double var6 = var2 * var2 + var4 * var4;
            if(var6 >= 9.999999747378752E-5D) {
               var6 = (double)MathHelper.func_76133_a(var6);
               var2 /= var6;
               var4 /= var6;
               double var8 = 1.0D / var6;
               if(var8 > 1.0D) {
                  var8 = 1.0D;
               }

               var2 *= var8;
               var4 *= var8;
               var2 *= 0.10000000149011612D;
               var4 *= 0.10000000149011612D;
               var2 *= (double)(1.0F - this.field_70144_Y);
               var4 *= (double)(1.0F - this.field_70144_Y);
               var2 *= 0.5D;
               var4 *= 0.5D;
               if(p_70108_1_ instanceof EntityMinecart) {
                  double var10 = p_70108_1_.field_70165_t - this.field_70165_t;
                  double var12 = p_70108_1_.field_70161_v - this.field_70161_v;
                  Vec3 var14 = this.field_70170_p.func_82732_R().func_72345_a(var10, 0.0D, var12).func_72432_b();
                  Vec3 var15 = this.field_70170_p.func_82732_R().func_72345_a((double)MathHelper.func_76134_b(this.field_70177_z * 3.1415927F / 180.0F), 0.0D, (double)MathHelper.func_76126_a(this.field_70177_z * 3.1415927F / 180.0F)).func_72432_b();
                  double var16 = Math.abs(var14.func_72430_b(var15));
                  if(var16 < 0.800000011920929D) {
                     return;
                  }

                  double var18 = p_70108_1_.field_70159_w + this.field_70159_w;
                  double var20 = p_70108_1_.field_70179_y + this.field_70179_y;
                  if(((EntityMinecart)p_70108_1_).field_70505_a == 2 && this.field_70505_a != 2) {
                     this.field_70159_w *= 0.20000000298023224D;
                     this.field_70179_y *= 0.20000000298023224D;
                     this.func_70024_g(p_70108_1_.field_70159_w - var2, 0.0D, p_70108_1_.field_70179_y - var4);
                     p_70108_1_.field_70159_w *= 0.949999988079071D;
                     p_70108_1_.field_70179_y *= 0.949999988079071D;
                  } else if(((EntityMinecart)p_70108_1_).field_70505_a != 2 && this.field_70505_a == 2) {
                     p_70108_1_.field_70159_w *= 0.20000000298023224D;
                     p_70108_1_.field_70179_y *= 0.20000000298023224D;
                     p_70108_1_.func_70024_g(this.field_70159_w + var2, 0.0D, this.field_70179_y + var4);
                     this.field_70159_w *= 0.949999988079071D;
                     this.field_70179_y *= 0.949999988079071D;
                  } else {
                     var18 /= 2.0D;
                     var20 /= 2.0D;
                     this.field_70159_w *= 0.20000000298023224D;
                     this.field_70179_y *= 0.20000000298023224D;
                     this.func_70024_g(var18 - var2, 0.0D, var20 - var4);
                     p_70108_1_.field_70159_w *= 0.20000000298023224D;
                     p_70108_1_.field_70179_y *= 0.20000000298023224D;
                     p_70108_1_.func_70024_g(var18 + var2, 0.0D, var20 + var4);
                  }
               } else {
                  this.func_70024_g(-var2, 0.0D, -var4);
                  p_70108_1_.func_70024_g(var2 / 4.0D, 0.0D, var4 / 4.0D);
               }
            }

         }
      }
   }

   public int func_70302_i_() {
      return 27;
   }

   public ItemStack func_70301_a(int p_70301_1_) {
      return this.field_70501_d[p_70301_1_];
   }

   public ItemStack func_70298_a(int p_70298_1_, int p_70298_2_) {
      if(this.field_70501_d[p_70298_1_] != null) {
         ItemStack var3;
         if(this.field_70501_d[p_70298_1_].field_77994_a <= p_70298_2_) {
            var3 = this.field_70501_d[p_70298_1_];
            this.field_70501_d[p_70298_1_] = null;
            return var3;
         } else {
            var3 = this.field_70501_d[p_70298_1_].func_77979_a(p_70298_2_);
            if(this.field_70501_d[p_70298_1_].field_77994_a == 0) {
               this.field_70501_d[p_70298_1_] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack func_70304_b(int p_70304_1_) {
      if(this.field_70501_d[p_70304_1_] != null) {
         ItemStack var2 = this.field_70501_d[p_70304_1_];
         this.field_70501_d[p_70304_1_] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void func_70299_a(int p_70299_1_, ItemStack p_70299_2_) {
      this.field_70501_d[p_70299_1_] = p_70299_2_;
      if(p_70299_2_ != null && p_70299_2_.field_77994_a > this.func_70297_j_()) {
         p_70299_2_.field_77994_a = this.func_70297_j_();
      }

   }

   public String func_70303_b() {
      return "container.minecart";
   }

   public int func_70297_j_() {
      return 64;
   }

   public void func_70296_d() {}

   public boolean func_70085_c(EntityPlayer p_70085_1_) {
      if(this.field_70505_a == 0) {
         if(this.field_70153_n != null && this.field_70153_n instanceof EntityPlayer && this.field_70153_n != p_70085_1_) {
            return true;
         }

         if(!this.field_70170_p.field_72995_K) {
            p_70085_1_.func_70078_a(this);
         }
      } else if(this.field_70505_a == 1) {
         if(!this.field_70170_p.field_72995_K) {
            p_70085_1_.func_71007_a(this);
         }
      } else if(this.field_70505_a == 2) {
         ItemStack var2 = p_70085_1_.field_71071_by.func_70448_g();
         if(var2 != null && var2.field_77993_c == Item.field_77705_m.field_77779_bT) {
            if(--var2.field_77994_a == 0) {
               p_70085_1_.field_71071_by.func_70299_a(p_70085_1_.field_71071_by.field_70461_c, (ItemStack)null);
            }

            this.field_70502_e += 3600;
         }

         this.field_70503_b = this.field_70165_t - p_70085_1_.field_70165_t;
         this.field_70504_c = this.field_70161_v - p_70085_1_.field_70161_v;
      }

      return true;
   }

   @SideOnly(Side.CLIENT)
   public void func_70056_a(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_) {
      this.field_70511_i = p_70056_1_;
      this.field_70509_j = p_70056_3_;
      this.field_70514_an = p_70056_5_;
      this.field_70512_ao = (double)p_70056_7_;
      this.field_70513_ap = (double)p_70056_8_;
      this.field_70510_h = p_70056_9_ + 2;
      this.field_70159_w = this.field_70508_aq;
      this.field_70181_x = this.field_70507_ar;
      this.field_70179_y = this.field_70506_as;
   }

   @SideOnly(Side.CLIENT)
   public void func_70016_h(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
      this.field_70508_aq = this.field_70159_w = p_70016_1_;
      this.field_70507_ar = this.field_70181_x = p_70016_3_;
      this.field_70506_as = this.field_70179_y = p_70016_5_;
   }

   public boolean func_70300_a(EntityPlayer p_70300_1_) {
      return this.field_70128_L?false:p_70300_1_.func_70068_e(this) <= 64.0D;
   }

   public boolean func_70490_h() {
      return (this.field_70180_af.func_75683_a(16) & 1) != 0;
   }

   protected void func_70498_d(boolean p_70498_1_) {
      if(p_70498_1_) {
         this.field_70180_af.func_75692_b(16, Byte.valueOf((byte)(this.field_70180_af.func_75683_a(16) | 1)));
      } else {
         this.field_70180_af.func_75692_b(16, Byte.valueOf((byte)(this.field_70180_af.func_75683_a(16) & -2)));
      }

   }

   public void func_70295_k_() {}

   public void func_70305_f() {}

   public void func_70492_c(int p_70492_1_) {
      this.field_70180_af.func_75692_b(19, Integer.valueOf(p_70492_1_));
   }

   public int func_70491_i() {
      return this.field_70180_af.func_75679_c(19);
   }

   public void func_70497_h(int p_70497_1_) {
      this.field_70180_af.func_75692_b(17, Integer.valueOf(p_70497_1_));
   }

   public int func_70496_j() {
      return this.field_70180_af.func_75679_c(17);
   }

   public void func_70494_i(int p_70494_1_) {
      this.field_70180_af.func_75692_b(18, Integer.valueOf(p_70494_1_));
   }

   public int func_70493_k() {
      return this.field_70180_af.func_75679_c(18);
   }

}
