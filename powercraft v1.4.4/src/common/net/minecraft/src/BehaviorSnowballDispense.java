package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorSnowballDispense extends BehaviorProjectileDispense
{
    final MinecraftServer theServer;

    public BehaviorSnowballDispense(MinecraftServer par1)
    {
        this.theServer = par1;
    }

    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        return new EntitySnowball(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
    }
}
