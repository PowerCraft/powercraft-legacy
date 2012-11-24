package net.minecraft.src;

public class EntityGiantZombie extends EntityMob
{
    public EntityGiantZombie(World par1World)
    {
        super(par1World);
        this.texture = "/mob/zombie.png";
        this.moveSpeed = 0.5F;
        this.yOffset *= 6.0F;
        this.setSize(this.width * 6.0F, this.height * 6.0F);
    }

    public int getMaxHealth()
    {
        return 100;
    }

    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return this.worldObj.getLightBrightness(par1, par2, par3) - 0.5F;
    }

    public int getAttackStrength(Entity par1Entity)
    {
        return 50;
    }
}
