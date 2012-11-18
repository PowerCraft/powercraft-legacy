package net.minecraftforge.common;

import net.minecraft.src.Entity;

public interface IThrowableEntity
{
    public Entity getThrower();

    public void setThrower(Entity entity);
}
