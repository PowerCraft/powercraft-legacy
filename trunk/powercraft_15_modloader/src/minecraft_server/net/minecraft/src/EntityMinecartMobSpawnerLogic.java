package net.minecraft.src;

class EntityMinecartMobSpawnerLogic extends MobSpawnerBaseLogic
{
    final EntityMinecartMobSpawner field_98296_a;

    EntityMinecartMobSpawnerLogic(EntityMinecartMobSpawner par1EntityMinecartMobSpawner)
    {
        this.field_98296_a = par1EntityMinecartMobSpawner;
    }

    public void func_98267_a(int par1)
    {
        this.field_98296_a.worldObj.setEntityState(this.field_98296_a, (byte)par1);
    }

    public World getSpawnerWorld()
    {
        return this.field_98296_a.worldObj;
    }

    public int getSpawnerX()
    {
        return MathHelper.floor_double(this.field_98296_a.posX);
    }

    public int getSpawnerY()
    {
        return MathHelper.floor_double(this.field_98296_a.posY);
    }

    public int getSpawnerZ()
    {
        return MathHelper.floor_double(this.field_98296_a.posZ);
    }
}
