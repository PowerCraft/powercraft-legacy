package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.Canvas;
import net.minecraft.client.MinecraftApplet;

@SideOnly(Side.CLIENT)
public class CanvasMinecraftApplet extends Canvas
{
    /** Reference to the MinecraftApplet object. */
    final MinecraftApplet mcApplet;

    public CanvasMinecraftApplet(MinecraftApplet par1MinecraftApplet)
    {
        this.mcApplet = par1MinecraftApplet;
    }

    public synchronized void addNotify()
    {
        super.addNotify();
        this.mcApplet.startMainThread();
    }

    public synchronized void removeNotify()
    {
        this.mcApplet.shutdown();
        super.removeNotify();
    }
}
