package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class WorldProviderHell extends WorldProvider
{
    public void registerWorldChunkManager()
    {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
        this.isHellWorld = true;
        this.hasNoSky = true;
        this.dimensionId = -1;
    }

    @SideOnly(Side.CLIENT)

    public Vec3 getFogColor(float par1, float par2)
    {
        return this.worldObj.getWorldVec3Pool().getVecFromPool(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
    }

    protected void generateLightBrightnessTable()
    {
        float var1 = 0.1F;

        for (int var2 = 0; var2 <= 15; ++var2)
        {
            float var3 = 1.0F - (float)var2 / 15.0F;
            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }
    }

    public IChunkProvider getChunkProvider()
    {
        return new ChunkProviderHell(this.worldObj, this.worldObj.getSeed());
    }

    public boolean isSurfaceWorld()
    {
        return false;
    }

    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
        return false;
    }

    public float calculateCelestialAngle(long par1, float par3)
    {
        return 0.5F;
    }

    public boolean canRespawnHere()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)

    public boolean doesXZShowFog(int par1, int par2)
    {
        return true;
    }

    public String getDimensionName()
    {
        return "Nether";
    }
}
