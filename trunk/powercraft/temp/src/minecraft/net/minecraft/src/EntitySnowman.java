package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityAIArrowAttack;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityGolem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.IMob;
import net.minecraft.src.IRangedAttackMob;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {

   public EntitySnowman(World p_i3522_1_) {
      super(p_i3522_1_);
      this.field_70750_az = "/mob/snowman.png";
      this.func_70105_a(0.4F, 1.8F);
      this.func_70661_as().func_75491_a(true);
      this.field_70714_bg.func_75776_a(1, new EntityAIArrowAttack(this, 0.25F, 20, 10.0F));
      this.field_70714_bg.func_75776_a(2, new EntityAIWander(this, 0.2F));
      this.field_70714_bg.func_75776_a(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.field_70714_bg.func_75776_a(4, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 16.0F, 0, true, false, IMob.field_82192_a));
   }

   public boolean func_70650_aV() {
      return true;
   }

   public int func_70667_aM() {
      return 4;
   }

   public void func_70636_d() {
      super.func_70636_d();
      if(this.func_70026_G()) {
         this.func_70097_a(DamageSource.field_76369_e, 1);
      }

      int var1 = MathHelper.func_76128_c(this.field_70165_t);
      int var2 = MathHelper.func_76128_c(this.field_70161_v);
      if(this.field_70170_p.func_72807_a(var1, var2).func_76743_j() > 1.0F) {
         this.func_70097_a(DamageSource.field_76370_b, 1);
      }

      for(var1 = 0; var1 < 4; ++var1) {
         var2 = MathHelper.func_76128_c(this.field_70165_t + (double)((float)(var1 % 2 * 2 - 1) * 0.25F));
         int var3 = MathHelper.func_76128_c(this.field_70163_u);
         int var4 = MathHelper.func_76128_c(this.field_70161_v + (double)((float)(var1 / 2 % 2 * 2 - 1) * 0.25F));
         if(this.field_70170_p.func_72798_a(var2, var3, var4) == 0 && this.field_70170_p.func_72807_a(var2, var4).func_76743_j() < 0.8F && Block.field_72037_aS.func_71930_b(this.field_70170_p, var2, var3, var4)) {
            this.field_70170_p.func_72859_e(var2, var3, var4, Block.field_72037_aS.field_71990_ca);
         }
      }

   }

   protected int func_70633_aT() {
      return Item.field_77768_aD.field_77779_bT;
   }

   protected void func_70628_a(boolean p_70628_1_, int p_70628_2_) {
      int var3 = this.field_70146_Z.nextInt(16);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.func_70025_b(Item.field_77768_aD.field_77779_bT, 1);
      }

   }

   public void func_82196_d(EntityLiving p_82196_1_) {
      EntitySnowball var2 = new EntitySnowball(this.field_70170_p, this);
      double var3 = p_82196_1_.field_70165_t - this.field_70165_t;
      double var5 = p_82196_1_.field_70163_u + (double)p_82196_1_.func_70047_e() - 1.100000023841858D - var2.field_70163_u;
      double var7 = p_82196_1_.field_70161_v - this.field_70161_v;
      float var9 = MathHelper.func_76133_a(var3 * var3 + var7 * var7) * 0.2F;
      var2.func_70186_c(var3, var5 + (double)var9, var7, 1.6F, 12.0F);
      this.func_85030_a("random.bow", 1.0F, 1.0F / (this.func_70681_au().nextFloat() * 0.4F + 0.8F));
      this.field_70170_p.func_72838_d(var2);
   }
}
