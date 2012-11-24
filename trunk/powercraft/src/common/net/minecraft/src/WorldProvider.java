package net.minecraft.src;

import net.minecraftforge.client.SkyProvider;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public abstract class WorldProvider
{
    public World worldObj;
    public WorldType terrainType;
    public String field_82913_c;

    public WorldChunkManager worldChunkMgr;

    public boolean isHellWorld = false;

    public boolean hasNoSky = false;

    public float[] lightBrightnessTable = new float[16];

    public int dimensionId = 0;

    private float[] colorsSunriseSunset = new float[4];

    public final void registerWorld(World par1World)
    {
        this.worldObj = par1World;
        this.terrainType = par1World.getWorldInfo().getTerrainType();
        this.field_82913_c = par1World.getWorldInfo().func_82571_y();
        this.registerWorldChunkManager();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable()
    {
        float var1 = 0.0F;

        for (int var2 = 0; var2 <= 15; ++var2)
        {
            float var3 = 1.0F - (float)var2 / 15.0F;
            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }
    }

    protected void registerWorldChunkManager()
    {
        worldChunkMgr = terrainType.getChunkManager(worldObj);
    }

    public IChunkProvider getChunkProvider()
    {
        return terrainType.getChunkGenerator(worldObj, field_82913_c);
    }

    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
        int var3 = this.worldObj.getFirstUncoveredBlock(par1, par2);
        return var3 == Block.grass.blockID;
    }

    public float calculateCelestialAngle(long par1, float par3)
    {
        int var4 = (int)(par1 % 24000L);
        float var5 = ((float)var4 + par3) / 24000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        float var6 = var5;
        var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

    @SideOnly(Side.CLIENT)
    public int getMoonPhase(long par1, float par3)
    {
        return (int)(par1 / 24000L) % 8;
    }

    public boolean isSurfaceWorld()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    public float[] calcSunriseSunsetColors(float par1, float par2)
    {
        float var3 = 0.4F;
        float var4 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) - 0.0F;
        float var5 = -0.0F;

        if (var4 >= var5 - var3 && var4 <= var5 + var3)
        {
            float var6 = (var4 - var5) / var3 * 0.5F + 0.5F;
            float var7 = 1.0F - (1.0F - MathHelper.sin(var6 * (float)Math.PI)) * 0.99F;
            var7 *= var7;
            this.colorsSunriseSunset[0] = var6 * 0.3F + 0.7F;
            this.colorsSunriseSunset[1] = var6 * var6 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = var6 * var6 * 0.0F + 0.2F;
            this.colorsSunriseSunset[3] = var7;
            return this.colorsSunriseSunset;
        }
        else
        {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)

    public Vec3 getFogColor(float par1, float par2)
    {
        float var3 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

        if (var3 < 0.0F)
        {
            var3 = 0.0F;
        }

        if (var3 > 1.0F)
        {
            var3 = 1.0F;
        }

        float var4 = 0.7529412F;
        float var5 = 0.84705883F;
        float var6 = 1.0F;
        var4 *= var3 * 0.94F + 0.06F;
        var5 *= var3 * 0.94F + 0.06F;
        var6 *= var3 * 0.91F + 0.09F;
        return this.worldObj.getWorldVec3Pool().getVecFromPool((double)var4, (double)var5, (double)var6);
    }

    public boolean canRespawnHere()
    {
        return true;
    }

    public static WorldProvider getProviderForDimension(int par0)
    {
        return DimensionManager.createProviderFor(par0);
    }

    @SideOnly(Side.CLIENT)

    public float getCloudHeight()
    {
        return 128.0F;
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return true;
    }

    public ChunkCoordinates getEntrancePortalLocation()
    {
        return null;
    }

    public int getAverageGroundLevel()
    {
        return this.terrainType.getMinimumSpawnHeight(this.worldObj);
    }

    @SideOnly(Side.CLIENT)

    public boolean getWorldHasVoidParticles()
    {
        return this.terrainType.hasVoidParticles(this.hasNoSky);
    }

    @SideOnly(Side.CLIENT)

    public double getVoidFogYFactor()
    {
        return this.terrainType.voidFadeMagnitude();
    }

    @SideOnly(Side.CLIENT)

    public boolean doesXZShowFog(int par1, int par2)
    {
        return false;
    }

    public abstract String getDimensionName();

    private SkyProvider skyProvider = null;

    public void setDimension(int dim)
    {
        this.dimensionId = dim;
    }

    public String getSaveFolder()
    {
        return (dimensionId == 0 ? null : "DIM" + dimensionId);
    }

    public String getWelcomeMessage()
    {
        if (this instanceof WorldProviderEnd)
        {
            return "Entering the End";
        }
        else if (this instanceof WorldProviderHell)
        {
            return "Entering the Nether";
        }

        return null;
    }

    public String getDepartMessage()
    {
        if (this instanceof WorldProviderEnd)
        {
            return "Leaving the End";
        }
        else if (this instanceof WorldProviderHell)
        {
            return "Leaving the Nether";
        }

        return null;
    }

    public double getMovementFactor()
    {
        if (this instanceof WorldProviderHell)
        {
            return 8.0;
        }

        return 1.0;
    }

    @SideOnly(Side.CLIENT)
    public SkyProvider getSkyProvider()
    {
        return this.skyProvider;
    }

    @SideOnly(Side.CLIENT)
    public void setSkyProvider(SkyProvider skyProvider)
    {
        this.skyProvider = skyProvider;
    }

    public ChunkCoordinates getRandomizedSpawnPoint()
    {
        ChunkCoordinates var5 = new ChunkCoordinates(this.worldObj.getSpawnPoint());
        boolean isAdventure = worldObj.getWorldInfo().getGameType() == EnumGameType.ADVENTURE;
        int spawnFuzz = terrainType.getSpawnFuzz();
        int spawnFuzzHalf = spawnFuzz / 2;

        if (!hasNoSky && !isAdventure)
        {
            var5.posX += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
            var5.posZ += this.worldObj.rand.nextInt(spawnFuzz) - spawnFuzzHalf;
            var5.posY = this.worldObj.getTopSolidOrLiquidBlock(var5.posX, var5.posZ);
        }

        return var5;
    }

    public BiomeGenBase getBiomeGenForCoords(int x, int z)
    {
        return worldObj.getBiomeGenForCoordsBody(x, z);
    }

    public boolean isDaytime()
    {
        return worldObj.skylightSubtracted < 4;
    }

    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
    {
        return worldObj.getSkyColorBody(cameraEntity, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public Vec3 drawClouds(float partialTicks)
    {
        return worldObj.drawCloudsBody(partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return worldObj.getStarBrightnessBody(par1);
    }

    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
    {
        worldObj.spawnHostileMobs = allowHostile;
        worldObj.spawnPeacefulMobs = allowPeaceful;
    }

    public void calculateInitialWeather()
    {
        worldObj.calculateInitialWeatherBody();
    }

    public void updateWeather()
    {
        worldObj.updateWeatherBody();
    }

    public void toggleRain()
    {
        worldObj.worldInfo.setRainTime(1);
    }

    public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
    {
        return worldObj.canBlockFreezeBody(x, y, z, byWater);
    }

    public boolean canSnowAt(int x, int y, int z)
    {
        return worldObj.canSnowAtBody(x, y, z);
    }

    public void setWorldTime(long time)
    {
        worldObj.worldInfo.setWorldTime(time);
    }

    public long getSeed()
    {
        return worldObj.worldInfo.getSeed();
    }

    public long getWorldTime()
    {
        return worldObj.worldInfo.getWorldTime();
    }

    public ChunkCoordinates getSpawnPoint()
    {
        WorldInfo info = worldObj.worldInfo;
        return new ChunkCoordinates(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ());
    }

    public void setSpawnPoint(int x, int y, int z)
    {
        worldObj.worldInfo.setSpawnPosition(x, y, z);
    }

    public boolean canMineBlock(EntityPlayer player, int x, int y, int z)
    {
        return worldObj.canMineBlockBody(player, x, y, z);
    }

    public boolean isBlockHighHumidity(int x, int y, int z)
    {
        return worldObj.getBiomeGenForCoords(x, z).isHighHumidity();
    }

    public int getHeight()
    {
        return 256;
    }

    public int getActualHeight()
    {
        return hasNoSky ? 128 : 256;
    }

    public double getHorizon()
    {
        return worldObj.worldInfo.getTerrainType().getHorizon(worldObj);
    }

    public void resetRainAndThunder()
    {
        worldObj.worldInfo.setRainTime(0);
        worldObj.worldInfo.setRaining(false);
        worldObj.worldInfo.setThunderTime(0);
        worldObj.worldInfo.setThundering(false);
    }

    public boolean canDoLightning(Chunk chunk)
    {
        return true;
    }

    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return true;
    }
}
