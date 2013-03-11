package net.minecraft.entity.ai;

import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

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

    public World func_98271_a()
    {
        return this.field_98296_a.worldObj;
    }

    public int func_98275_b()
    {
        return MathHelper.floor_double(this.field_98296_a.posX);
    }

    public int func_98274_c()
    {
        return MathHelper.floor_double(this.field_98296_a.posY);
    }

    public int func_98266_d()
    {
        return MathHelper.floor_double(this.field_98296_a.posZ);
    }
}
