package net.minecraft.src;

class PlayerUsageSnooperThread extends Thread
{
    final PlayerUsageSnooper field_52012_a;

    PlayerUsageSnooperThread(PlayerUsageSnooper par1PlayerUsageSnooper, String par2Str)
    {
        super(par2Str);
        field_52012_a = par1PlayerUsageSnooper;
    }

    public void run()
    {
        PostHttp.sendPost(PlayerUsageSnooper.getServerURL(field_52012_a), PlayerUsageSnooper.getDataMap(field_52012_a), true);
    }
}
