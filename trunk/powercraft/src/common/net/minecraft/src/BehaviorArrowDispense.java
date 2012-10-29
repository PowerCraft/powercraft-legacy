package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorArrowDispense extends BehaviorProjectileDispense
{
    /** Reference to the MinecraftServer object. */
    final MinecraftServer mcServer;

    public BehaviorArrowDispense(MinecraftServer par1)
    {
        this.mcServer = par1;
    }

    protected IProjectile func_82499_a(World par1World, IPosition par2IPosition)
    {
        EntityArrow var3 = new EntityArrow(par1World, par2IPosition.func_82615_a(), par2IPosition.func_82617_b(), par2IPosition.func_82616_c());
        var3.canBePickedUp = 1;
        return var3;
    }
}
