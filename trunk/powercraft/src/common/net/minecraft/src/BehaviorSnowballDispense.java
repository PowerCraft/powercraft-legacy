package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorSnowballDispense extends BehaviorProjectileDispense
{
    /** Instance of MinecraftServer. */
    final MinecraftServer theServer;

    public BehaviorSnowballDispense(MinecraftServer par1)
    {
        this.theServer = par1;
    }

    protected IProjectile func_82499_a(World par1World, IPosition par2IPosition)
    {
        return new EntitySnowball(par1World, par2IPosition.func_82615_a(), par2IPosition.func_82617_b(), par2IPosition.func_82616_c());
    }
}
