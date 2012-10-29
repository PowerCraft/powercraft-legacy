package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.applet.Applet;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class MinecraftFakeLauncher extends Applet implements AppletStub
{
    final Map field_74534_a;

    public MinecraftFakeLauncher(Map par1Map)
    {
        this.field_74534_a = par1Map;
    }

    public void appletResize(int par1, int par2) {}

    public boolean isActive()
    {
        return true;
    }

    public URL getDocumentBase()
    {
        try
        {
            return new URL("http://www.minecraft.net/game/");
        }
        catch (MalformedURLException var2)
        {
            var2.printStackTrace();
            return null;
        }
    }

    public String getParameter(String par1Str)
    {
        if (this.field_74534_a.containsKey(par1Str))
        {
            return (String)this.field_74534_a.get(par1Str);
        }
        else
        {
            System.err.println("Client asked for parameter: " + par1Str);
            return null;
        }
    }
}
