package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.Explosion;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockTNT extends Block {

   public BlockTNT(int p_i4011_1_, int p_i4011_2_) {
      super(p_i4011_1_, p_i4011_2_, Material.field_76262_s);
      this.func_71849_a(CreativeTabs.field_78028_d);
   }

   public int func_71851_a(int p_71851_1_) {
      return p_71851_1_ == 0?this.field_72059_bZ + 2:(p_71851_1_ == 1?this.field_72059_bZ + 1:this.field_72059_bZ);
   }

   public void func_71861_g(World p_71861_1_, int p_71861_2_, int p_71861_3_, int p_71861_4_) {
      super.func_71861_g(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_);
      if(p_71861_1_.func_72864_z(p_71861_2_, p_71861_3_, p_71861_4_)) {
         this.func_71898_d(p_71861_1_, p_71861_2_, p_71861_3_, p_71861_4_, 1);
         p_71861_1_.func_72859_e(p_71861_2_, p_71861_3_, p_71861_4_, 0);
      }

   }

   public void func_71863_a(World p_71863_1_, int p_71863_2_, int p_71863_3_, int p_71863_4_, int p_71863_5_) {
      if(p_71863_5_ > 0 && Block.field_71973_m[p_71863_5_].func_71853_i() && p_71863_1_.func_72864_z(p_71863_2_, p_71863_3_, p_71863_4_)) {
         this.func_71898_d(p_71863_1_, p_71863_2_, p_71863_3_, p_71863_4_, 1);
         p_71863_1_.func_72859_e(p_71863_2_, p_71863_3_, p_71863_4_, 0);
      }

   }

   public int func_71925_a(Random p_71925_1_) {
      return 1;
   }

   public void func_71867_k(World p_71867_1_, int p_71867_2_, int p_71867_3_, int p_71867_4_) {
      if(!p_71867_1_.field_72995_K) {
         EntityTNTPrimed var5 = new EntityTNTPrimed(p_71867_1_, (double)((float)p_71867_2_ + 0.5F), (double)((float)p_71867_3_ + 0.5F), (double)((float)p_71867_4_ + 0.5F));
         var5.field_70516_a = p_71867_1_.field_73012_v.nextInt(var5.field_70516_a / 4) + var5.field_70516_a / 8;
         p_71867_1_.func_72838_d(var5);
      }
   }

   public void func_71898_d(World p_71898_1_, int p_71898_2_, int p_71898_3_, int p_71898_4_, int p_71898_5_) {
      if(!p_71898_1_.field_72995_K) {
         if((p_71898_5_ & 1) == 1) {
            EntityTNTPrimed var6 = new EntityTNTPrimed(p_71898_1_, (double)((float)p_71898_2_ + 0.5F), (double)((float)p_71898_3_ + 0.5F), (double)((float)p_71898_4_ + 0.5F));
            p_71898_1_.func_72838_d(var6);
            p_71898_1_.func_72956_a(var6, "random.fuse", 1.0F, 1.0F);
         }

      }
   }

   public boolean func_71903_a(World p_71903_1_, int p_71903_2_, int p_71903_3_, int p_71903_4_, EntityPlayer p_71903_5_, int p_71903_6_, float p_71903_7_, float p_71903_8_, float p_71903_9_) {
      if(p_71903_5_.func_71045_bC() != null && p_71903_5_.func_71045_bC().field_77993_c == Item.field_77709_i.field_77779_bT) {
         this.func_71898_d(p_71903_1_, p_71903_2_, p_71903_3_, p_71903_4_, 1);
         p_71903_1_.func_72859_e(p_71903_2_, p_71903_3_, p_71903_4_, 0);
         return true;
      } else {
         return super.func_71903_a(p_71903_1_, p_71903_2_, p_71903_3_, p_71903_4_, p_71903_5_, p_71903_6_, p_71903_7_, p_71903_8_, p_71903_9_);
      }
   }

   public void func_71869_a(World p_71869_1_, int p_71869_2_, int p_71869_3_, int p_71869_4_, Entity p_71869_5_) {
      if(p_71869_5_ instanceof EntityArrow && !p_71869_1_.field_72995_K) {
         EntityArrow var6 = (EntityArrow)p_71869_5_;
         if(var6.func_70027_ad()) {
            this.func_71898_d(p_71869_1_, p_71869_2_, p_71869_3_, p_71869_4_, 1);
            p_71869_1_.func_72859_e(p_71869_2_, p_71869_3_, p_71869_4_, 0);
         }
      }

   }

   public boolean func_85103_a(Explosion p_85103_1_) {
      return false;
   }
}
