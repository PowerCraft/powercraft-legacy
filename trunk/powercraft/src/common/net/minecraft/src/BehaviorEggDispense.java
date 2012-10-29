package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorEggDispense extends BehaviorProjectileDispense
{
    final MinecraftServer field_82503_b;

    public BehaviorEggDispense(MinecraftServer par1MinecraftServer)
    {
        this.field_82503_b = par1MinecraftServer;
    }

    protected IProjectile func_82499_a(World par1World, IPosition par2IPosition)
    {
        return new EntityEgg(par1World, par2IPosition.func_82615_a(), par2IPosition.func_82617_b(), par2IPosition.func_82616_c());
    }
}
