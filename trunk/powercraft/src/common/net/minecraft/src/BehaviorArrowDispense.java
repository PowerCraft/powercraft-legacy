package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorArrowDispense extends BehaviorProjectileDispense
{
    final MinecraftServer mcServer;

    public BehaviorArrowDispense(MinecraftServer par1)
    {
        this.mcServer = par1;
    }

    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        EntityArrow var3 = new EntityArrow(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
        var3.canBePickedUp = 1;
        return var3;
    }
}
