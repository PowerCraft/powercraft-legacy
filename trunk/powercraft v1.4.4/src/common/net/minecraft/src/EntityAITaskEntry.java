package net.minecraft.src;

public class EntityAITaskEntry
{
    public EntityAIBase action;

    public int priority;

    final EntityAITasks tasks;

    public EntityAITaskEntry(EntityAITasks par1EntityAITasks, int par2, EntityAIBase par3EntityAIBase)
    {
        this.tasks = par1EntityAITasks;
        this.priority = par2;
        this.action = par3EntityAIBase;
    }
}
