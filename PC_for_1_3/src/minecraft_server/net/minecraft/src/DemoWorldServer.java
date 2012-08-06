package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class DemoWorldServer extends WorldServer
{
    private static final long field_73072_L = (long)"North Carolina".hashCode();
    public static final WorldSettings field_73071_a = (new WorldSettings(field_73072_L, EnumGameType.SURVIVAL, true, false, WorldType.DEFAULT)).enableBonusChest();

    public DemoWorldServer(MinecraftServer par1MinecraftServer, ISaveHandler par2ISaveHandler, String par3Str, int par4, Profiler par5Profiler)
    {
        super(par1MinecraftServer, par2ISaveHandler, par3Str, par4, field_73071_a, par5Profiler);
    }
}
