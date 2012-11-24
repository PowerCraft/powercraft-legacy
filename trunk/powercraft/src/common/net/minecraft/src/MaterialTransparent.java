package net.minecraft.src;

public class MaterialTransparent extends Material
{
    public MaterialTransparent(MapColor par1MapColor)
    {
        super(par1MapColor);
        this.setGroundCover();
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
