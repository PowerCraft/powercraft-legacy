package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class EntityLargeFireball extends EntityFireball {

   public EntityLargeFireball(World p_i5067_1_) {
      super(p_i5067_1_);
   }

   @SideOnly(Side.CLIENT)
   public EntityLargeFireball(World p_i5068_1_, double p_i5068_2_, double p_i5068_4_, double p_i5068_6_, double p_i5068_8_, double p_i5068_10_, double p_i5068_12_) {
      super(p_i5068_1_, p_i5068_2_, p_i5068_4_, p_i5068_6_, p_i5068_8_, p_i5068_10_, p_i5068_12_);
   }

   public EntityLargeFireball(World p_i5069_1_, EntityLiving p_i5069_2_, double p_i5069_3_, double p_i5069_5_, double p_i5069_7_) {
      super(p_i5069_1_, p_i5069_2_, p_i5069_3_, p_i5069_5_, p_i5069_7_);
   }

   protected void func_70227_a(MovingObjectPosition p_70227_1_) {
      if(!this.field_70170_p.field_72995_K) {
         if(p_70227_1_.field_72308_g != null) {
            p_70227_1_.field_72308_g.func_70097_a(DamageSource.func_76362_a(this, this.field_70235_a), 6);
         }

         this.field_70170_p.func_72885_a((Entity)null, this.field_70165_t, this.field_70163_u, this.field_70161_v, 1.0F, true, this.field_70170_p.func_82736_K().func_82766_b("mobGriefing"));
         this.func_70106_y();
      }

   }
}
