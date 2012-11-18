package cpw.mods.fml.common;

public enum Side
{
    CLIENT, SERVER, BUKKIT;

    public boolean isServer()
    {
        return !isClient();
    }

    public boolean isClient()
    {
        return this == CLIENT;
    }
}