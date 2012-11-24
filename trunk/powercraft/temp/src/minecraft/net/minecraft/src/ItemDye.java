package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.BlockDirectional;
import net.minecraft.src.BlockLog;
import net.minecraft.src.BlockMushroom;
import net.minecraft.src.BlockSapling;
import net.minecraft.src.BlockStem;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class ItemDye extends Item {

   public static final String[] field_77860_a = new String[]{"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
   public static final int[] field_77859_b = new int[]{1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 2651799, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};


   public ItemDye(int p_i3645_1_) {
      super(p_i3645_1_);
      this.func_77627_a(true);
      this.func_77656_e(0);
      this.func_77637_a(CreativeTabs.field_78035_l);
   }

   @SideOnly(Side.CLIENT)
   public int func_77617_a(int p_77617_1_) {
      int var2 = MathHelper.func_76125_a(p_77617_1_, 0, 15);
      return this.field_77791_bV + var2 % 8 * 16 + var2 / 8;
   }

   public String func_77667_c(ItemStack p_77667_1_) {
      int var2 = MathHelper.func_76125_a(p_77667_1_.func_77960_j(), 0, 15);
      return super.func_77658_a() + "." + field_77860_a[var2];
   }

   public boolean func_77648_a(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
      if(!p_77648_2_.func_82247_a(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_)) {
         return false;
      } else {
         int var11;
         int var12;
         int var13;
         if(p_77648_1_.func_77960_j() == 15) {
            var11 = p_77648_3_.func_72798_a(p_77648_4_, p_77648_5_, p_77648_6_);
            if(var11 == Block.field_71987_y.field_71990_ca) {
               if(!p_77648_3_.field_72995_K) {
                  ((BlockSapling)Block.field_71987_y).func_72269_c(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_3_.field_73012_v);
                  --p_77648_1_.field_77994_a;
               }

               return true;
            }

            if(var11 == Block.field_72109_af.field_71990_ca || var11 == Block.field_72103_ag.field_71990_ca) {
               if(!p_77648_3_.field_72995_K && ((BlockMushroom)Block.field_71973_m[var11]).func_72271_c(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_3_.field_73012_v)) {
                  --p_77648_1_.field_77994_a;
               }

               return true;
            }

            if(var11 == Block.field_71999_bt.field_71990_ca || var11 == Block.field_71996_bs.field_71990_ca) {
               if(p_77648_3_.func_72805_g(p_77648_4_, p_77648_5_, p_77648_6_) == 7) {
                  return false;
               }

               if(!p_77648_3_.field_72995_K) {
                  ((BlockStem)Block.field_71973_m[var11]).func_72264_l(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_);
                  --p_77648_1_.field_77994_a;
               }

               return true;
            }

            if(var11 > 0 && Block.field_71973_m[var11] instanceof BlockCrops) {
               if(p_77648_3_.func_72805_g(p_77648_4_, p_77648_5_, p_77648_6_) == 7) {
                  return false;
               }

               if(!p_77648_3_.field_72995_K) {
                  ((BlockCrops)Block.field_71973_m[var11]).func_72272_c_(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_);
                  --p_77648_1_.field_77994_a;
               }

               return true;
            }

            if(var11 == Block.field_72086_bP.field_71990_ca) {
               if(!p_77648_3_.field_72995_K) {
                  p_77648_3_.func_72921_c(p_77648_4_, p_77648_5_, p_77648_6_, 8 | BlockDirectional.func_72217_d(p_77648_3_.func_72805_g(p_77648_4_, p_77648_5_, p_77648_6_)));
                  --p_77648_1_.field_77994_a;
               }

               return true;
            }

            if(var11 == Block.field_71980_u.field_71990_ca) {
               if(!p_77648_3_.field_72995_K) {
                  --p_77648_1_.field_77994_a;

                  label133:
                  for(var12 = 0; var12 < 128; ++var12) {
                     var13 = p_77648_4_;
                     int var14 = p_77648_5_ + 1;
                     int var15 = p_77648_6_;

                     for(int var16 = 0; var16 < var12 / 16; ++var16) {
                        var13 += field_77697_d.nextInt(3) - 1;
                        var14 += (field_77697_d.nextInt(3) - 1) * field_77697_d.nextInt(3) / 2;
                        var15 += field_77697_d.nextInt(3) - 1;
                        if(p_77648_3_.func_72798_a(var13, var14 - 1, var15) != Block.field_71980_u.field_71990_ca || p_77648_3_.func_72809_s(var13, var14, var15)) {
                           continue label133;
                        }
                     }

                     if(p_77648_3_.func_72798_a(var13, var14, var15) == 0) {
                        if(field_77697_d.nextInt(10) != 0) {
                           if(Block.field_71962_X.func_71854_d(p_77648_3_, var13, var14, var15)) {
                              p_77648_3_.func_72832_d(var13, var14, var15, Block.field_71962_X.field_71990_ca, 1);
                           }
                        } else if(field_77697_d.nextInt(3) != 0) {
                           if(Block.field_72097_ad.func_71854_d(p_77648_3_, var13, var14, var15)) {
                              p_77648_3_.func_72859_e(var13, var14, var15, Block.field_72097_ad.field_71990_ca);
                           }
                        } else if(Block.field_72107_ae.func_71854_d(p_77648_3_, var13, var14, var15)) {
                           p_77648_3_.func_72859_e(var13, var14, var15, Block.field_72107_ae.field_71990_ca);
                        }
                     }
                  }
               }

               return true;
            }
         } else if(p_77648_1_.func_77960_j() == 3) {
            var11 = p_77648_3_.func_72798_a(p_77648_4_, p_77648_5_, p_77648_6_);
            var12 = p_77648_3_.func_72805_g(p_77648_4_, p_77648_5_, p_77648_6_);
            if(var11 == Block.field_71951_J.field_71990_ca && BlockLog.func_72141_e(var12) == 3) {
               if(p_77648_7_ == 0) {
                  return false;
               }

               if(p_77648_7_ == 1) {
                  return false;
               }

               if(p_77648_7_ == 2) {
                  --p_77648_6_;
               }

               if(p_77648_7_ == 3) {
                  ++p_77648_6_;
               }

               if(p_77648_7_ == 4) {
                  --p_77648_4_;
               }

               if(p_77648_7_ == 5) {
                  ++p_77648_4_;
               }

               if(p_77648_3_.func_72799_c(p_77648_4_, p_77648_5_, p_77648_6_)) {
                  var13 = Block.field_71973_m[Block.field_72086_bP.field_71990_ca].func_85104_a(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, 0);
                  p_77648_3_.func_72832_d(p_77648_4_, p_77648_5_, p_77648_6_, Block.field_72086_bP.field_71990_ca, var13);
                  if(!p_77648_2_.field_71075_bZ.field_75098_d) {
                     --p_77648_1_.field_77994_a;
                  }
               }

               return true;
            }
         }

         return false;
      }
   }

   public boolean func_77646_a(ItemStack p_77646_1_, EntityLiving p_77646_2_) {
      if(p_77646_2_ instanceof EntitySheep) {
         EntitySheep var3 = (EntitySheep)p_77646_2_;
         int var4 = BlockCloth.func_72238_e_(p_77646_1_.func_77960_j());
         if(!var3.func_70892_o() && var3.func_70896_n() != var4) {
            var3.func_70891_b(var4);
            --p_77646_1_.field_77994_a;
         }

         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public void func_77633_a(int p_77633_1_, CreativeTabs p_77633_2_, List p_77633_3_) {
      for(int var4 = 0; var4 < 16; ++var4) {
         p_77633_3_.add(new ItemStack(p_77633_1_, 1, var4));
      }

   }

}
