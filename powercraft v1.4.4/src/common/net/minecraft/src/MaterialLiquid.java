package net.minecraft.src;

public class MaterialLiquid extends Material
{
    public MaterialLiquid(MapColor par1MapColor)
    {
        super(par1MapColor);
        this.setGroundCover();
        this.setNoPushMobility();
    }

    public boolean isLiquid()
    {
        return true;
    }

    public boolean blocksMovement()
    {
        return false;
    }

    public boolean isSolid()
    {
        return false;
    }
}
