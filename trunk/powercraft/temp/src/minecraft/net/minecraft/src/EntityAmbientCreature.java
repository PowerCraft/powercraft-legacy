package net.minecraft.src;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.IAnimals;
import net.minecraft.src.World;

public abstract class EntityAmbientCreature extends EntityLiving implements IAnimals {

   public EntityAmbientCreature(World p_i5062_1_) {
      super(p_i5062_1_);
   }
}
