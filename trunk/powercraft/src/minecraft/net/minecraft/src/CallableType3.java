package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class CallableType3 implements Callable
{
    /** Gets Intergated Server type. */
    final IntegratedServer minecraftServerType3;

    CallableType3(IntegratedServer par1IntegratedServer)
    {
        this.minecraftServerType3 = par1IntegratedServer;
    }

    public String getType()
    {
        return "Integrated Server";
    }

    public Object call()
    {
        return this.getType();
    }
}
