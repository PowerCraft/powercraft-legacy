package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorExpBottleDispense extends BehaviorProjectileDispense
{
    /** For checking if Minecraft Server is modded. */
    final MinecraftServer minecraftServerIsServerModded;

    public BehaviorExpBottleDispense(MinecraftServer par1)
    {
        this.minecraftServerIsServerModded = par1;
    }

    protected IProjectile func_82499_a(World par1World, IPosition par2IPosition)
    {
        return new EntityExpBottle(par1World, par2IPosition.func_82615_a(), par2IPosition.func_82617_b(), par2IPosition.func_82616_c());
    }

    protected float func_82498_a()
    {
        return super.func_82498_a() * 0.5F;
    }

    protected float func_82500_b()
    {
        return super.func_82500_b() * 1.25F;
    }
}
