package net.minecraft.world.storage;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerFileData
{
    /**
     * Writes the player data to disk from the specified PlayerEntityMP.
     */
    void writePlayerData(EntityPlayer var1);

    /**
     * Reads the player data from disk into the specified PlayerEntityMP.
     */
    void readPlayerData(EntityPlayer var1);

    /**
     * Returns an array of usernames for which player.dat exists for.
     */
    String[] getAvailablePlayerDat();
}
