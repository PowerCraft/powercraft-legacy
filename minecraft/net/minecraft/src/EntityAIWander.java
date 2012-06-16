package net.minecraft.src;

import java.util.Random;

public class EntityAIWander extends EntityAIBase
{
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private float speed;

    public EntityAIWander(EntityCreature par1EntityCreature, float par2)
    {
        entity = par1EntityCreature;
        speed = par2;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (entity.getAge() >= 100)
        {
            return false;
        }

        if (entity.getRNG().nextInt(120) != 0)
        {
            return false;
        }

        Vec3D vec3d = RandomPositionGenerator.func_48622_a(entity, 10, 7);

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            xPosition = vec3d.xCoord;
            yPosition = vec3d.yCoord;
            zPosition = vec3d.zCoord;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
    }
}
