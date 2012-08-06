package net.minecraft.src;

public interface IServer
{
    int getOrSetIntProperty(String var1, int var2);

    String getOrSetProperty(String var1, String var2);

    void setArbitraryProperty(String var1, Object var2);

    void saveSettingsToFile();

    String getSettingsFilePath();

    String getHostName();

    /**
     * never used. Can not be called "getServerPort" is already taken
     */
    int getMyServerPort();

    /**
     * minecraftServer.getMOTD is used in 2 places instead (it is a non-virtual function which returns the same thing)
     */
    String getServerMOTD();

    String getMinecraftVersion();

    int getPlayerListSize();

    int getMaxPlayers();

    String[] getAllUsernames();

    String getFolderName();

    /**
     * rename this when a patch comes out which uses it
     */
    String returnAnEmptyString();

    String executeCommand(String var1);

    boolean doLogInfoEvent();

    void logInfoMessage(String var1);

    void logWarningMessage(String var1);

    void logSevereEvent(String var1);

    void logInfoEvent(String var1);
}
