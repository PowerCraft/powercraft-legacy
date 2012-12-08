package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public final class ThreadShutdown extends Thread
{
    public void run()
    {
        Minecraft.stopIntegratedServer();
    }
}
