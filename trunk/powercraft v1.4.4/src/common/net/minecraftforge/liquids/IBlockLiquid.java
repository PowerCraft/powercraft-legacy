package net.minecraftforge.liquids;

import net.minecraft.src.NBTTagCompound;

public interface IBlockLiquid extends ILiquid
{
    public enum BlockType
    {
        NONE,

        VANILLA,

        FINITE;
    }

    public boolean willGenerateSources();

    public int getFlowDistance();

    public byte[] getLiquidRGB();

    public String getLiquidBlockTextureFile();

    public NBTTagCompound getLiquidProperties();
}
