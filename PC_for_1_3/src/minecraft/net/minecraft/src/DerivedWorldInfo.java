package net.minecraft.src;

public class DerivedWorldInfo extends WorldInfo
{
    private final WorldInfo field_76115_a;

    public DerivedWorldInfo(WorldInfo par1WorldInfo)
    {
        this.field_76115_a = par1WorldInfo;
    }

    /**
     * Gets the NBTTagCompound for the worldInfo
     */
    public NBTTagCompound getNBTTagCompound()
    {
        return this.field_76115_a.getNBTTagCompound();
    }

    /**
     * Creates a new NBTTagCompound for the world, with the given NBTTag as the "Player"
     */
    public NBTTagCompound cloneNBTCompound(NBTTagCompound par1NBTTagCompound)
    {
        return this.field_76115_a.cloneNBTCompound(par1NBTTagCompound);
    }

    /**
     * Returns the seed of current world.
     */
    public long getSeed()
    {
        return this.field_76115_a.getSeed();
    }

    /**
     * Returns the x spawn position
     */
    public int getSpawnX()
    {
        return this.field_76115_a.getSpawnX();
    }

    /**
     * Return the Y axis spawning point of the player.
     */
    public int getSpawnY()
    {
        return this.field_76115_a.getSpawnY();
    }

    /**
     * Returns the z spawn position
     */
    public int getSpawnZ()
    {
        return this.field_76115_a.getSpawnZ();
    }

    /**
     * Get current world time
     */
    public long getWorldTime()
    {
        return this.field_76115_a.getWorldTime();
    }

    public long getSizeOnDisk()
    {
        return this.field_76115_a.getSizeOnDisk();
    }

    /**
     * Returns the player's NBTTagCompound to be loaded
     */
    public NBTTagCompound getPlayerNBTTagCompound()
    {
        return this.field_76115_a.getPlayerNBTTagCompound();
    }

    public int getDimension()
    {
        return this.field_76115_a.getDimension();
    }

    /**
     * Get current world name
     */
    public String getWorldName()
    {
        return this.field_76115_a.getWorldName();
    }

    /**
     * Returns the save version of this world
     */
    public int getSaveVersion()
    {
        return this.field_76115_a.getSaveVersion();
    }

    /**
     * Return the last time the player was in this world.
     */
    public long getLastTimePlayed()
    {
        return this.field_76115_a.getLastTimePlayed();
    }

    /**
     * Returns true if it is thundering, false otherwise.
     */
    public boolean isThundering()
    {
        return this.field_76115_a.isThundering();
    }

    /**
     * Returns the number of ticks until next thunderbolt.
     */
    public int getThunderTime()
    {
        return this.field_76115_a.getThunderTime();
    }

    /**
     * Returns true if it is raining, false otherwise.
     */
    public boolean isRaining()
    {
        return this.field_76115_a.isRaining();
    }

    /**
     * Return the number of ticks until rain.
     */
    public int getRainTime()
    {
        return this.field_76115_a.getRainTime();
    }

    /**
     * Gets the GameType.
     */
    public EnumGameType getGameType()
    {
        return this.field_76115_a.getGameType();
    }

    /**
     * Set the x spawn position to the passed in value
     */
    public void setSpawnX(int par1) {}

    /**
     * Sets the y spawn position
     */
    public void setSpawnY(int par1) {}

    /**
     * Set the z spawn position to the passed in value
     */
    public void setSpawnZ(int par1) {}

    /**
     * Set current world time
     */
    public void setWorldTime(long par1) {}

    /**
     * Sets the spawn zone position. Args: x, y, z
     */
    public void setSpawnPosition(int par1, int par2, int par3) {}

    public void setWorldName(String par1Str) {}

    /**
     * Sets the save version of the world
     */
    public void setSaveVersion(int par1) {}

    /**
     * Sets whether it is thundering or not.
     */
    public void setThundering(boolean par1) {}

    /**
     * Defines the number of ticks until next thunderbolt.
     */
    public void setThunderTime(int par1) {}

    /**
     * Sets whether it is raining or not.
     */
    public void setRaining(boolean par1) {}

    /**
     * Sets the number of ticks until rain.
     */
    public void setRainTime(int par1) {}

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    public boolean isMapFeaturesEnabled()
    {
        return this.field_76115_a.isMapFeaturesEnabled();
    }

    /**
     * Returns true if hardcore mode is enabled, otherwise false
     */
    public boolean isHardcoreModeEnabled()
    {
        return this.field_76115_a.isHardcoreModeEnabled();
    }

    public WorldType getTerrainType()
    {
        return this.field_76115_a.getTerrainType();
    }

    public void setTerrainType(WorldType par1WorldType) {}

    /**
     * Returns true if commands are allowed on this World.
     */
    public boolean areCommandsAllowed()
    {
        return this.field_76115_a.areCommandsAllowed();
    }

    /**
     * Returns true if the World is initialized.
     */
    public boolean isInitialized()
    {
        return this.field_76115_a.isInitialized();
    }

    /**
     * Sets the initialization status of the World.
     */
    public void setServerInitialized(boolean par1) {}
}
