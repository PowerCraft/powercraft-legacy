package cpw.mods.fml.common.registry;

import net.minecraft.src.Entity;

public interface IThrowableEntity
{
    public Entity getThrower();

    public void setThrower(Entity entity);
}
