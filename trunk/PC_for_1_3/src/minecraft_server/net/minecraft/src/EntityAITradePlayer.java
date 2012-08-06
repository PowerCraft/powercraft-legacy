package net.minecraft.src;

public class EntityAITradePlayer extends EntityAIBase
{
    private EntityVillager field_75276_a;

    public EntityAITradePlayer(EntityVillager par1EntityVillager)
    {
        this.field_75276_a = par1EntityVillager;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.field_75276_a.isEntityAlive())
        {
            return false;
        }
        else if (this.field_75276_a.isInWater())
        {
            return false;
        }
        else if (!this.field_75276_a.onGround)
        {
            return false;
        }
        else if (this.field_75276_a.velocityChanged)
        {
            return false;
        }
        else
        {
            EntityPlayer var1 = this.field_75276_a.getCustomer();
            return var1 == null ? false : (this.field_75276_a.getDistanceSqToEntity(var1) > 16.0D ? false : var1.craftingInventory instanceof Container);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_75276_a.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.field_75276_a.setCustomer((EntityPlayer)null);
    }
}
