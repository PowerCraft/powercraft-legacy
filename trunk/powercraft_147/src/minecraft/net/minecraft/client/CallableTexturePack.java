package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
public class CallableTexturePack implements Callable
{
    final Minecraft field_90051_a;

    public CallableTexturePack(Minecraft par1Minecraft)
    {
        this.field_90051_a = par1Minecraft;
    }

    public String func_90050_a()
    {
        return this.field_90051_a.gameSettings.skin;
    }

    public Object call()
    {
        return this.func_90050_a();
    }
}
