package net.minecraft.src;

public class MaterialPortal extends Material
{
    public MaterialPortal(MapColor par1MapColor)
    {
        super(par1MapColor);
    }

    public boolean isSolid()
    {
        return false;
    }

    public boolean getCanBlockGrass()
    {
        return false;
    }

    public boolean blocksMovement()
    {
        return false;
    }
}
