package net.minecraft.src;

public class EntityAIOpenDoor extends EntityAIDoorInteract
{
    boolean field_75361_i;
    int field_75360_j;

    public EntityAIOpenDoor(EntityLiving par1EntityLiving, boolean par2)
    {
        super(par1EntityLiving);
        this.theEntity = par1EntityLiving;
        this.field_75361_i = par2;
    }

    public boolean continueExecuting()
    {
        return this.field_75361_i && this.field_75360_j > 0 && super.continueExecuting();
    }

    public void startExecuting()
    {
        this.field_75360_j = 20;
        this.targetDoor.onPoweredBlockChange(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, true);
    }

    public void resetTask()
    {
        if (this.field_75361_i)
        {
            this.targetDoor.onPoweredBlockChange(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, false);
        }
    }

    public void updateTask()
    {
        --this.field_75360_j;
        super.updateTask();
    }
}
