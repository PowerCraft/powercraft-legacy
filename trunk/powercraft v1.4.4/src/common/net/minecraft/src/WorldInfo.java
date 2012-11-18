package net.minecraft.src;

import java.util.Map;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class WorldInfo
{
    private long randomSeed;
    private WorldType terrainType;
    private String field_82576_c;

    private int spawnX;

    private int spawnY;

    private int spawnZ;

    private long dayTime;

    private long worldTime;

    private long lastTimePlayed;

    private long sizeOnDisk;
    private NBTTagCompound playerTag;
    private int dimension;

    private String levelName;

    private int saveVersion;

    private boolean raining;

    private int rainTime;

    private boolean thundering;

    private int thunderTime;

    private EnumGameType theGameType;

    private boolean mapFeaturesEnabled;

    private boolean hardcore;
    private boolean allowCommands;
    private boolean initialized;
    private GameRules theGameRules;
    private Map<String, NBTBase> additionalProperties;

    protected WorldInfo()
    {
        this.terrainType = WorldType.DEFAULT;
        this.field_82576_c = "";
        this.theGameRules = new GameRules();
    }

    public WorldInfo(NBTTagCompound par1NBTTagCompound)
    {
        this.terrainType = WorldType.DEFAULT;
        this.field_82576_c = "";
        this.theGameRules = new GameRules();
        this.randomSeed = par1NBTTagCompound.getLong("RandomSeed");

        if (par1NBTTagCompound.hasKey("generatorName"))
        {
            String var2 = par1NBTTagCompound.getString("generatorName");
            this.terrainType = WorldType.parseWorldType(var2);

            if (this.terrainType == null)
            {
                this.terrainType = WorldType.DEFAULT;
            }
            else if (this.terrainType.isVersioned())
            {
                int var3 = 0;

                if (par1NBTTagCompound.hasKey("generatorVersion"))
                {
                    var3 = par1NBTTagCompound.getInteger("generatorVersion");
                }

                this.terrainType = this.terrainType.getWorldTypeForGeneratorVersion(var3);
            }

            if (par1NBTTagCompound.hasKey("generatorOptions"))
            {
                this.field_82576_c = par1NBTTagCompound.getString("generatorOptions");
            }
        }

        this.theGameType = EnumGameType.getByID(par1NBTTagCompound.getInteger("GameType"));

        if (par1NBTTagCompound.hasKey("MapFeatures"))
        {
            this.mapFeaturesEnabled = par1NBTTagCompound.getBoolean("MapFeatures");
        }
        else
        {
            this.mapFeaturesEnabled = true;
        }

        this.spawnX = par1NBTTagCompound.getInteger("SpawnX");
        this.spawnY = par1NBTTagCompound.getInteger("SpawnY");
        this.spawnZ = par1NBTTagCompound.getInteger("SpawnZ");
        this.dayTime = par1NBTTagCompound.getLong("Time");

        if (par1NBTTagCompound.hasKey("DayTime"))
        {
            this.worldTime = par1NBTTagCompound.getLong("DayTime");
        }
        else
        {
            this.worldTime = this.dayTime;
        }

        this.lastTimePlayed = par1NBTTagCompound.getLong("LastPlayed");
        this.sizeOnDisk = par1NBTTagCompound.getLong("SizeOnDisk");
        this.levelName = par1NBTTagCompound.getString("LevelName");
        this.saveVersion = par1NBTTagCompound.getInteger("version");
        this.rainTime = par1NBTTagCompound.getInteger("rainTime");
        this.raining = par1NBTTagCompound.getBoolean("raining");
        this.thunderTime = par1NBTTagCompound.getInteger("thunderTime");
        this.thundering = par1NBTTagCompound.getBoolean("thundering");
        this.hardcore = par1NBTTagCompound.getBoolean("hardcore");

        if (par1NBTTagCompound.hasKey("initialized"))
        {
            this.initialized = par1NBTTagCompound.getBoolean("initialized");
        }
        else
        {
            this.initialized = true;
        }

        if (par1NBTTagCompound.hasKey("allowCommands"))
        {
            this.allowCommands = par1NBTTagCompound.getBoolean("allowCommands");
        }
        else
        {
            this.allowCommands = this.theGameType == EnumGameType.CREATIVE;
        }

        if (par1NBTTagCompound.hasKey("Player"))
        {
            this.playerTag = par1NBTTagCompound.getCompoundTag("Player");
            this.dimension = this.playerTag.getInteger("Dimension");
        }

        if (par1NBTTagCompound.hasKey("GameRules"))
        {
            this.theGameRules.readGameRulesFromNBT(par1NBTTagCompound.getCompoundTag("GameRules"));
        }
    }

    public WorldInfo(WorldSettings par1WorldSettings, String par2Str)
    {
        this.terrainType = WorldType.DEFAULT;
        this.field_82576_c = "";
        this.theGameRules = new GameRules();
        this.randomSeed = par1WorldSettings.getSeed();
        this.theGameType = par1WorldSettings.getGameType();
        this.mapFeaturesEnabled = par1WorldSettings.isMapFeaturesEnabled();
        this.levelName = par2Str;
        this.hardcore = par1WorldSettings.getHardcoreEnabled();
        this.terrainType = par1WorldSettings.getTerrainType();
        this.field_82576_c = par1WorldSettings.func_82749_j();
        this.allowCommands = par1WorldSettings.areCommandsAllowed();
        this.initialized = false;
    }

    public WorldInfo(WorldInfo par1WorldInfo)
    {
        this.terrainType = WorldType.DEFAULT;
        this.field_82576_c = "";
        this.theGameRules = new GameRules();
        this.randomSeed = par1WorldInfo.randomSeed;
        this.terrainType = par1WorldInfo.terrainType;
        this.field_82576_c = par1WorldInfo.field_82576_c;
        this.theGameType = par1WorldInfo.theGameType;
        this.mapFeaturesEnabled = par1WorldInfo.mapFeaturesEnabled;
        this.spawnX = par1WorldInfo.spawnX;
        this.spawnY = par1WorldInfo.spawnY;
        this.spawnZ = par1WorldInfo.spawnZ;
        this.dayTime = par1WorldInfo.dayTime;
        this.worldTime = par1WorldInfo.worldTime;
        this.lastTimePlayed = par1WorldInfo.lastTimePlayed;
        this.sizeOnDisk = par1WorldInfo.sizeOnDisk;
        this.playerTag = par1WorldInfo.playerTag;
        this.dimension = par1WorldInfo.dimension;
        this.levelName = par1WorldInfo.levelName;
        this.saveVersion = par1WorldInfo.saveVersion;
        this.rainTime = par1WorldInfo.rainTime;
        this.raining = par1WorldInfo.raining;
        this.thunderTime = par1WorldInfo.thunderTime;
        this.thundering = par1WorldInfo.thundering;
        this.hardcore = par1WorldInfo.hardcore;
        this.allowCommands = par1WorldInfo.allowCommands;
        this.initialized = par1WorldInfo.initialized;
        this.theGameRules = par1WorldInfo.theGameRules;
    }

    public NBTTagCompound getNBTTagCompound()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.updateTagCompound(var1, this.playerTag);
        return var1;
    }

    public NBTTagCompound cloneNBTCompound(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagCompound var2 = new NBTTagCompound();
        this.updateTagCompound(var2, par1NBTTagCompound);
        return var2;
    }

    private void updateTagCompound(NBTTagCompound par1NBTTagCompound, NBTTagCompound par2NBTTagCompound)
    {
        par1NBTTagCompound.setLong("RandomSeed", this.randomSeed);
        par1NBTTagCompound.setString("generatorName", this.terrainType.getWorldTypeName());
        par1NBTTagCompound.setInteger("generatorVersion", this.terrainType.getGeneratorVersion());
        par1NBTTagCompound.setString("generatorOptions", this.field_82576_c);
        par1NBTTagCompound.setInteger("GameType", this.theGameType.getID());
        par1NBTTagCompound.setBoolean("MapFeatures", this.mapFeaturesEnabled);
        par1NBTTagCompound.setInteger("SpawnX", this.spawnX);
        par1NBTTagCompound.setInteger("SpawnY", this.spawnY);
        par1NBTTagCompound.setInteger("SpawnZ", this.spawnZ);
        par1NBTTagCompound.setLong("Time", this.dayTime);
        par1NBTTagCompound.setLong("DayTime", this.worldTime);
        par1NBTTagCompound.setLong("SizeOnDisk", this.sizeOnDisk);
        par1NBTTagCompound.setLong("LastPlayed", System.currentTimeMillis());
        par1NBTTagCompound.setString("LevelName", this.levelName);
        par1NBTTagCompound.setInteger("version", this.saveVersion);
        par1NBTTagCompound.setInteger("rainTime", this.rainTime);
        par1NBTTagCompound.setBoolean("raining", this.raining);
        par1NBTTagCompound.setInteger("thunderTime", this.thunderTime);
        par1NBTTagCompound.setBoolean("thundering", this.thundering);
        par1NBTTagCompound.setBoolean("hardcore", this.hardcore);
        par1NBTTagCompound.setBoolean("allowCommands", this.allowCommands);
        par1NBTTagCompound.setBoolean("initialized", this.initialized);
        par1NBTTagCompound.setCompoundTag("GameRules", this.theGameRules.writeGameRulesToNBT());

        if (par2NBTTagCompound != null)
        {
            par1NBTTagCompound.setCompoundTag("Player", par2NBTTagCompound);
        }
    }

    public long getSeed()
    {
        return this.randomSeed;
    }

    public int getSpawnX()
    {
        return this.spawnX;
    }

    public int getSpawnY()
    {
        return this.spawnY;
    }

    public int getSpawnZ()
    {
        return this.spawnZ;
    }

    public long getWorldTotalTime()
    {
        return this.dayTime;
    }

    public long getWorldTime()
    {
        return this.worldTime;
    }

    @SideOnly(Side.CLIENT)
    public long getSizeOnDisk()
    {
        return this.sizeOnDisk;
    }

    public NBTTagCompound getPlayerNBTTagCompound()
    {
        return this.playerTag;
    }

    public int getDimension()
    {
        return this.dimension;
    }

    @SideOnly(Side.CLIENT)

    public void setSpawnX(int par1)
    {
        this.spawnX = par1;
    }

    @SideOnly(Side.CLIENT)

    public void setSpawnY(int par1)
    {
        this.spawnY = par1;
    }

    public void func_82572_b(long par1)
    {
        this.dayTime = par1;
    }

    @SideOnly(Side.CLIENT)

    public void setSpawnZ(int par1)
    {
        this.spawnZ = par1;
    }

    public void setWorldTime(long par1)
    {
        this.worldTime = par1;
    }

    public void setSpawnPosition(int par1, int par2, int par3)
    {
        this.spawnX = par1;
        this.spawnY = par2;
        this.spawnZ = par3;
    }

    public String getWorldName()
    {
        return this.levelName;
    }

    public void setWorldName(String par1Str)
    {
        this.levelName = par1Str;
    }

    public int getSaveVersion()
    {
        return this.saveVersion;
    }

    public void setSaveVersion(int par1)
    {
        this.saveVersion = par1;
    }

    @SideOnly(Side.CLIENT)

    public long getLastTimePlayed()
    {
        return this.lastTimePlayed;
    }

    public boolean isThundering()
    {
        return this.thundering;
    }

    public void setThundering(boolean par1)
    {
        this.thundering = par1;
    }

    public int getThunderTime()
    {
        return this.thunderTime;
    }

    public void setThunderTime(int par1)
    {
        this.thunderTime = par1;
    }

    public boolean isRaining()
    {
        return this.raining;
    }

    public void setRaining(boolean par1)
    {
        this.raining = par1;
    }

    public int getRainTime()
    {
        return this.rainTime;
    }

    public void setRainTime(int par1)
    {
        this.rainTime = par1;
    }

    public EnumGameType getGameType()
    {
        return this.theGameType;
    }

    public boolean isMapFeaturesEnabled()
    {
        return this.mapFeaturesEnabled;
    }

    public void setGameType(EnumGameType par1EnumGameType)
    {
        this.theGameType = par1EnumGameType;
    }

    public boolean isHardcoreModeEnabled()
    {
        return this.hardcore;
    }

    public WorldType getTerrainType()
    {
        return this.terrainType;
    }

    public void setTerrainType(WorldType par1WorldType)
    {
        this.terrainType = par1WorldType;
    }

    public String func_82571_y()
    {
        return this.field_82576_c;
    }

    public boolean areCommandsAllowed()
    {
        return this.allowCommands;
    }

    public boolean isInitialized()
    {
        return this.initialized;
    }

    public void setServerInitialized(boolean par1)
    {
        this.initialized = par1;
    }

    public GameRules getGameRulesInstance()
    {
        return this.theGameRules;
    }

    public void func_85118_a(CrashReportCategory par1CrashReportCategory)
    {
        par1CrashReportCategory.addCrashSectionCallable("Level seed", new CallableLevelSeed(this));
        par1CrashReportCategory.addCrashSectionCallable("Level generator", new CallableLevelGenerator(this));
        par1CrashReportCategory.addCrashSectionCallable("Level generator options", new CallableLevelGeneratorOptions(this));
        par1CrashReportCategory.addCrashSectionCallable("Level spawn location", new CallableLevelSpawnLocation(this));
        par1CrashReportCategory.addCrashSectionCallable("Level time", new CallableLevelTime(this));
        par1CrashReportCategory.addCrashSectionCallable("Level dimension", new CallableLevelDimension(this));
        par1CrashReportCategory.addCrashSectionCallable("Level storage version", new CallableLevelStorageVersion(this));
        par1CrashReportCategory.addCrashSectionCallable("Level weather", new CallableLevelWeather(this));
        par1CrashReportCategory.addCrashSectionCallable("Level game mode", new CallableLevelGamemode(this));
    }

    static WorldType func_85132_a(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.terrainType;
    }

    static boolean func_85128_b(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.mapFeaturesEnabled;
    }

    static String func_85130_c(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.field_82576_c;
    }

    static int func_85125_d(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.spawnX;
    }

    static int func_85124_e(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.spawnY;
    }

    static int func_85123_f(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.spawnZ;
    }

    static long func_85126_g(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.dayTime;
    }

    static long func_85129_h(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.worldTime;
    }

    static int func_85122_i(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.dimension;
    }

    static int func_85121_j(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.saveVersion;
    }

    static int func_85119_k(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.rainTime;
    }

    static boolean func_85127_l(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.raining;
    }

    static int func_85133_m(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.thunderTime;
    }

    static boolean func_85116_n(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.thundering;
    }

    static EnumGameType func_85120_o(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.theGameType;
    }

    static boolean func_85117_p(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.hardcore;
    }

    static boolean func_85131_q(WorldInfo par0WorldInfo)
    {
        return par0WorldInfo.allowCommands;
    }

    public void setAdditionalProperties(Map<String, NBTBase> additionalProperties)
    {
        if (this.additionalProperties == null)
        {
            this.additionalProperties = additionalProperties;
        }
    }

    public NBTBase getAdditionalProperty(String additionalProperty)
    {
        return this.additionalProperties != null ? this.additionalProperties.get(additionalProperty) : null;
    }
}
