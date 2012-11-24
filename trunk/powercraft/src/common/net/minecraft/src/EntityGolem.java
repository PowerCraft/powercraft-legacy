package net.minecraft.src;

public abstract class EntityGolem extends EntityCreature implements IAnimals
{
    public EntityGolem(World par1World)
    {
        super(par1World);
    }

    protected void fall(float par1) {}

    protected String getLivingSound()
    {
        return "none";
    }

    protected String getHurtSound()
    {
        return "none";
    }

    protected String getDeathSound()
    {
        return "none";
    }

    public int getTalkInterval()
    {
        return 120;
    }

    protected boolean canDespawn()
    {
        return false;
    }
}
