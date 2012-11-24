package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public interface IBlockAccess
{
    int getBlockId(int var1, int var2, int var3);

    TileEntity getBlockTileEntity(int var1, int var2, int var3);

    @SideOnly(Side.CLIENT)

    int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4);

    @SideOnly(Side.CLIENT)
    float getBrightness(int var1, int var2, int var3, int var4);

    @SideOnly(Side.CLIENT)

    float getLightBrightness(int var1, int var2, int var3);

    int getBlockMetadata(int var1, int var2, int var3);

    Material getBlockMaterial(int var1, int var2, int var3);

    @SideOnly(Side.CLIENT)

    boolean isBlockOpaqueCube(int var1, int var2, int var3);

    boolean isBlockNormalCube(int var1, int var2, int var3);

    @SideOnly(Side.CLIENT)

    boolean isAirBlock(int var1, int var2, int var3);

    @SideOnly(Side.CLIENT)

    BiomeGenBase getBiomeGenForCoords(int var1, int var2);

    @SideOnly(Side.CLIENT)

    int getHeight();

    @SideOnly(Side.CLIENT)

    boolean extendedLevelsInChunkCache();

    @SideOnly(Side.CLIENT)

    boolean doesBlockHaveSolidTopSurface(int var1, int var2, int var3);

    Vec3Pool getWorldVec3Pool();

    boolean isBlockProvidingPowerTo(int var1, int var2, int var3, int var4);
}
