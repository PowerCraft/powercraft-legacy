package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorEggDispense extends BehaviorProjectileDispense
{
    final MinecraftServer mcServer;

    public BehaviorEggDispense(MinecraftServer par1MinecraftServer)
    {
        this.mcServer = par1MinecraftServer;
    }

    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        return new EntityEgg(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
    }
}
