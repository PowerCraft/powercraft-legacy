package net.minecraft.src;

public class EntityTrackerNonliving
{
    public final BaseMod mod;
    public final Class entityClass;
    public final int id;
    public final int viewDistance;
    public final int updateFrequency;
    public final boolean trackMotion;

    public EntityTrackerNonliving(BaseMod var1, Class var2, int var3, int var4, int var5, boolean var6)
    {
        this.mod = var1;
        this.entityClass = var2;
        this.id = var3;
        this.viewDistance = var4;
        this.updateFrequency = var5;
        this.trackMotion = var6;
    }
}
