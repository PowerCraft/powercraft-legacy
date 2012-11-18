package net.minecraftforge.common;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecart;

public interface IMinecartCollisionHandler
{
    public void onEntityCollision(EntityMinecart cart, Entity other);

    public AxisAlignedBB getCollisionBox(EntityMinecart cart, Entity other);

    public AxisAlignedBB getMinecartCollisionBox(EntityMinecart cart);

    public AxisAlignedBB getBoundingBox(EntityMinecart cart);
}
