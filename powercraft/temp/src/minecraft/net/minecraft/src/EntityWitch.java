package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityAIArrowAttack;
import net.minecraft.src.EntityAIHurtByTarget;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.IRangedAttackMob;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class EntityWitch extends EntityMob implements IRangedAttackMob {

   private static final int[] field_82199_d = new int[]{Item.field_77751_aT.field_77779_bT, Item.field_77747_aY.field_77779_bT, Item.field_77767_aC.field_77779_bT, Item.field_77728_bu.field_77779_bT, Item.field_77729_bt.field_77779_bT, Item.field_77677_M.field_77779_bT, Item.field_77669_D.field_77779_bT, Item.field_77669_D.field_77779_bT};
   private int field_82200_e = 0;


   public EntityWitch(World p_i5066_1_) {
      super(p_i5066_1_);
      this.field_70750_az = "/mob/villager/witch.png";
      this.field_70697_bw = 0.25F;
      this.field_70714_bg.func_75776_a(1, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(2, new EntityAIArrowAttack(this, this.field_70697_bw, 60, 10.0F));
      this.field_70714_bg.func_75776_a(2, new EntityAIWander(this, this.field_70697_bw));
      this.field_70714_bg.func_75776_a(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(3, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_70096_w().func_75682_a(21, Byte.valueOf((byte)0));
   }

   protected String func_70639_aQ() {
      return "mob.witch.idle";
   }

   protected String func_70621_aR() {
      return "mob.witch.hurt";
   }

   protected String func_70673_aS() {
      return "mob.witch.death";
   }

   public void func_82197_f(boolean p_82197_1_) {
      this.func_70096_w().func_75692_b(21, Byte.valueOf((byte)(p_82197_1_?1:0)));
   }

   public boolean func_82198_m() {
      return this.func_70096_w().func_75683_a(21) == 1;
   }

   public int func_70667_aM() {
      return 26;
   }

   public boolean func_70650_aV() {
      return true;
   }

   public void func_70636_d() {
      if(!this.field_70170_p.field_72995_K) {
         if(this.func_82198_m()) {
            if(this.field_82200_e-- <= 0) {
               this.func_82197_f(false);
               ItemStack var1 = this.func_70694_bm();
               this.func_70062_b(0, (ItemStack)null);
               if(var1 != null && var1.field_77993_c == Item.field_77726_bs.field_77779_bT) {
                  List var2 = Item.field_77726_bs.func_77832_l(var1);
                  if(var2 != null) {
                     Iterator var3 = var2.iterator();

                     while(var3.hasNext()) {
                        PotionEffect var4 = (PotionEffect)var3.next();
                        this.func_70690_d(new PotionEffect(var4));
                     }
                  }
               }
            }
         } else {
            short var5 = -1;
            if(this.field_70146_Z.nextFloat() < 0.15F && this.func_70027_ad() && !this.func_70644_a(Potion.field_76426_n)) {
               var5 = 16307;
            } else if(this.field_70146_Z.nextFloat() < 0.05F && this.field_70734_aK < this.func_70667_aM()) {
               var5 = 16341;
            } else if(this.field_70146_Z.nextFloat() < 0.25F && this.func_70638_az() != null && !this.func_70644_a(Potion.field_76424_c) && this.func_70638_az().func_70068_e(this) > 121.0D) {
               var5 = 16274;
            } else if(this.field_70146_Z.nextFloat() < 0.25F && this.func_70638_az() != null && !this.func_70644_a(Potion.field_76424_c) && this.func_70638_az().func_70068_e(this) > 121.0D) {
               var5 = 16274;
            }

            if(var5 > -1) {
               this.func_70062_b(0, new ItemStack(Item.field_77726_bs, 1, var5));
               this.field_82200_e = this.func_70694_bm().func_77988_m();
               this.func_82197_f(true);
            }
         }

         if(this.field_70146_Z.nextFloat() < 7.5E-4F) {
            this.field_70170_p.func_72960_a(this, (byte)15);
         }
      }

      super.func_70636_d();
   }

   protected int func_70672_c(DamageSource p_70672_1_, int p_70672_2_) {
      p_70672_2_ = super.func_70672_c(p_70672_1_, p_70672_2_);
      if(p_70672_1_.func_76346_g() == this) {
         p_70672_2_ = 0;
      }

      if(p_70672_1_.func_82725_o()) {
         p_70672_2_ = (int)((double)p_70672_2_ * 0.15D);
      }

      return p_70672_2_;
   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte p_70103_1_) {
      if(p_70103_1_ == 15) {
         for(int var2 = 0; var2 < this.field_70146_Z.nextInt(35) + 10; ++var2) {
            this.field_70170_p.func_72869_a("witchMagic", this.field_70165_t + this.field_70146_Z.nextGaussian() * 0.12999999523162842D, this.field_70121_D.field_72337_e + 0.5D + this.field_70146_Z.nextGaussian() * 0.12999999523162842D, this.field_70161_v + this.field_70146_Z.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
         }
      } else {
         super.func_70103_a(p_70103_1_);
      }

   }

   public float func_70616_bs() {
      float var1 = super.func_70616_bs();
      if(this.func_82198_m()) {
         var1 *= 0.75F;
      }

      return var1;
   }

   protected void func_70628_a(boolean p_70628_1_, int p_70628_2_) {
      int var3 = this.field_70146_Z.nextInt(3) + 1;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = this.field_70146_Z.nextInt(3);
         int var6 = field_82199_d[this.field_70146_Z.nextInt(field_82199_d.length)];
         if(p_70628_2_ > 0) {
            var5 += this.field_70146_Z.nextInt(p_70628_2_ + 1);
         }

         for(int var7 = 0; var7 < var5; ++var7) {
            this.func_70025_b(var6, 1);
         }
      }

   }

   public void func_82196_d(EntityLiving p_82196_1_) {
      if(!this.func_82198_m()) {
         EntityPotion var2 = new EntityPotion(this.field_70170_p, this, 32732);
         var2.field_70125_A -= -20.0F;
         double var3 = p_82196_1_.field_70165_t + p_82196_1_.field_70159_w - this.field_70165_t;
         double var5 = p_82196_1_.field_70163_u + (double)p_82196_1_.func_70047_e() - 1.100000023841858D - this.field_70163_u;
         double var7 = p_82196_1_.field_70161_v + p_82196_1_.field_70179_y - this.field_70161_v;
         float var9 = MathHelper.func_76133_a(var3 * var3 + var7 * var7);
         if(var9 >= 8.0F && !p_82196_1_.func_70644_a(Potion.field_76421_d)) {
            var2.func_82340_a(32698);
         } else if(p_82196_1_.func_70630_aN() >= 8 && !p_82196_1_.func_70644_a(Potion.field_76436_u)) {
            var2.func_82340_a(32660);
         } else if(var9 <= 3.0F && !p_82196_1_.func_70644_a(Potion.field_76437_t) && this.field_70146_Z.nextFloat() < 0.25F) {
            var2.func_82340_a(32696);
         }

         var2.func_70186_c(var3, var5 + (double)(var9 * 0.2F), var7, 0.75F, 8.0F);
         this.field_70170_p.func_72838_d(var2);
      }
   }

}
