package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class EntitySenses
{
    EntityLiving entityObj;
    List field_75524_b = new ArrayList();
    List field_75525_c = new ArrayList();

    public EntitySenses(EntityLiving par1EntityLiving)
    {
        this.entityObj = par1EntityLiving;
    }

    /**
     * Clears canSeeCachePositive and canSeeCacheNegative.
     */
    public void clearSensingCache()
    {
        this.field_75524_b.clear();
        this.field_75525_c.clear();
    }

    /**
     * Checks, whether 'our' entity can see the entity given as argument (true) or not (false), caching the result.
     */
    public boolean canSee(Entity par1Entity)
    {
        if (this.field_75524_b.contains(par1Entity))
        {
            return true;
        }
        else if (this.field_75525_c.contains(par1Entity))
        {
            return false;
        }
        else
        {
            this.entityObj.worldObj.theProfiler.startSection("canSee");
            boolean var2 = this.entityObj.canEntityBeSeen(par1Entity);
            this.entityObj.worldObj.theProfiler.endSection();

            if (var2)
            {
                this.field_75524_b.add(par1Entity);
            }
            else
            {
                this.field_75525_c.add(par1Entity);
            }

            return var2;
        }
    }
}
