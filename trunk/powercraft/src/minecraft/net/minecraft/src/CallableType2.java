package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class CallableType2 implements Callable
{
    /** Gets Client Server type. */
    final Minecraft minecraftServerType2;

    public CallableType2(Minecraft par1Minecraft)
    {
        this.minecraftServerType2 = par1Minecraft;
    }

    public String getType()
    {
        return "Client";
    }

    public Object call()
    {
        return this.getType();
    }
}
