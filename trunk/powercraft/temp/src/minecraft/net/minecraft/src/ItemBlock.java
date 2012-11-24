package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemBlock extends Item {

   private int field_77885_a;


   public ItemBlock(int p_i3690_1_) {
      super(p_i3690_1_);
      this.field_77885_a = p_i3690_1_ + 256;
      this.func_77665_c(Block.field_71973_m[p_i3690_1_ + 256].func_71851_a(2));
   }

   public int func_77883_f() {
      return this.field_77885_a;
   }

   public boolean func_77648_a(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
      int var11 = p_77648_3_.func_72798_a(p_77648_4_, p_77648_5_, p_77648_6_);
      if(var11 == Block.field_72037_aS.field_71990_ca) {
         p_77648_7_ = 1;
      } else if(var11 != Block.field_71998_bu.field_71990_ca && var11 != Block.field_71962_X.field_71990_ca && var11 != Block.field_71961_Y.field_71990_ca) {
         if(p_77648_7_ == 0) {
            --p_77648_5_;
         }

         if(p_77648_7_ == 1) {
            ++p_77648_5_;
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
      }

      if(p_77648_1_.field_77994_a == 0) {
         return false;
      } else if(!p_77648_2_.func_82247_a(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_)) {
         return false;
      } else if(p_77648_5_ == 255 && Block.field_71973_m[this.field_77885_a].field_72018_cp.func_76220_a()) {
         return false;
      } else if(p_77648_3_.func_72931_a(this.field_77885_a, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_, p_77648_2_)) {
         Block var12 = Block.field_71973_m[this.field_77885_a];
         int var13 = this.func_77647_b(p_77648_1_.func_77960_j());
         int var14 = Block.field_71973_m[this.field_77885_a].func_85104_a(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, var13);
         if(p_77648_3_.func_72832_d(p_77648_4_, p_77648_5_, p_77648_6_, this.field_77885_a, var14)) {
            if(p_77648_3_.func_72798_a(p_77648_4_, p_77648_5_, p_77648_6_) == this.field_77885_a) {
               Block.field_71973_m[this.field_77885_a].func_71860_a(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_2_);
               Block.field_71973_m[this.field_77885_a].func_85105_g(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, var14);
            }

            p_77648_3_.func_72908_a((double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), var12.field_72020_cn.func_82593_b(), (var12.field_72020_cn.func_72677_b() + 1.0F) / 2.0F, var12.field_72020_cn.func_72678_c() * 0.8F);
            --p_77648_1_.field_77994_a;
         }

         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean func_77884_a(World p_77884_1_, int p_77884_2_, int p_77884_3_, int p_77884_4_, int p_77884_5_, EntityPlayer p_77884_6_, ItemStack p_77884_7_) {
      int var8 = p_77884_1_.func_72798_a(p_77884_2_, p_77884_3_, p_77884_4_);
      if(var8 == Block.field_72037_aS.field_71990_ca) {
         p_77884_5_ = 1;
      } else if(var8 != Block.field_71998_bu.field_71990_ca && var8 != Block.field_71962_X.field_71990_ca && var8 != Block.field_71961_Y.field_71990_ca) {
         if(p_77884_5_ == 0) {
            --p_77884_3_;
         }

         if(p_77884_5_ == 1) {
            ++p_77884_3_;
         }

         if(p_77884_5_ == 2) {
            --p_77884_4_;
         }

         if(p_77884_5_ == 3) {
            ++p_77884_4_;
         }

         if(p_77884_5_ == 4) {
            --p_77884_2_;
         }

         if(p_77884_5_ == 5) {
            ++p_77884_2_;
         }
      }

      return p_77884_1_.func_72931_a(this.func_77883_f(), p_77884_2_, p_77884_3_, p_77884_4_, false, p_77884_5_, (Entity)null);
   }

   public String func_77667_c(ItemStack p_77667_1_) {
      return Block.field_71973_m[this.field_77885_a].func_71917_a();
   }

   public String func_77658_a() {
      return Block.field_71973_m[this.field_77885_a].func_71917_a();
   }

   @SideOnly(Side.CLIENT)
   public CreativeTabs func_77640_w() {
      return Block.field_71973_m[this.field_77885_a].func_71882_w();
   }

   @SideOnly(Side.CLIENT)
   public void func_77633_a(int p_77633_1_, CreativeTabs p_77633_2_, List p_77633_3_) {
      Block.field_71973_m[this.field_77885_a].func_71879_a(p_77633_1_, p_77633_2_, p_77633_3_);
   }
}
