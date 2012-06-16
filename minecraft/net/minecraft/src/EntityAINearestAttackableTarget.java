package net.minecraft.src;

import java.util.*;

public class EntityAINearestAttackableTarget extends EntityAITarget
{
    EntityLiving targetEntity;
    Class targetClass;
    int targetChance;
    private EntityAINearestAttackableTargetSorter field_48387_g;

    public EntityAINearestAttackableTarget(EntityLiving par1EntityLiving, Class par2Class, float par3, int par4, boolean par5)
    {
        this(par1EntityLiving, par2Class, par3, par4, par5, false);
    }

    public EntityAINearestAttackableTarget(EntityLiving par1EntityLiving, Class par2Class, float par3, int par4, boolean par5, boolean par6)
    {
        super(par1EntityLiving, par3, par5, par6);
        targetClass = par2Class;
        targetDistance = par3;
        targetChance = par4;
        field_48387_g = new EntityAINearestAttackableTargetSorter(this, par1EntityLiving);
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        label0:
        {
            if (targetChance > 0 && taskOwner.getRNG().nextInt(targetChance) != 0)
            {
                return false;
            }

            if (targetClass == (net.minecraft.src.EntityPlayer.class))
            {
                EntityPlayer entityplayer = taskOwner.worldObj.getClosestVulnerablePlayerToEntity(taskOwner, targetDistance);

                if (func_48376_a(entityplayer, false))
                {
                    targetEntity = entityplayer;
                    return true;
                }

                break label0;
            }

            List list = taskOwner.worldObj.getEntitiesWithinAABB(targetClass, taskOwner.boundingBox.expand(targetDistance, 4D, targetDistance));
            Collections.sort(list, field_48387_g);
            Iterator iterator = list.iterator();
            EntityLiving entityliving;

            do
            {
                if (!iterator.hasNext())
                {
                    break label0;
                }

                Entity entity = (Entity)iterator.next();
                entityliving = (EntityLiving)entity;
            }
            while (!func_48376_a(entityliving, false));

            targetEntity = entityliving;
            return true;
        }
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        taskOwner.setAttackTarget(targetEntity);
        super.startExecuting();
    }
}
