package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.IEntitySelector;

final class EntityWitherAttackFilter implements IEntitySelector {

   public boolean func_82704_a(Entity p_82704_1_) {
      return p_82704_1_ instanceof EntityLiving && ((EntityLiving)p_82704_1_).func_70668_bt() != EnumCreatureAttribute.UNDEAD;
   }
}
