package net.minecraft.entity.ai;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;

class EntityAIAvoidEntitySelector implements IEntitySelector
{
    final EntityAIAvoidEntity field_98219_c;

    EntityAIAvoidEntitySelector(EntityAIAvoidEntity par1EntityAIAvoidEntity)
    {
        this.field_98219_c = par1EntityAIAvoidEntity;
    }

    /**
     * Return whether the specified entity is applicable to this filter.
     */
    public boolean isEntityApplicable(Entity par1Entity)
    {
        return par1Entity.isEntityAlive() && EntityAIAvoidEntity.func_98217_a(this.field_98219_c).getEntitySenses().canSee(par1Entity);
    }
}
