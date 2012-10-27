package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class CallableModded implements Callable
{
    /** Gets if Minecraft is Modded. */
    final Minecraft minecraftModded;

    public CallableModded(Minecraft par1Minecraft)
    {
        this.minecraftModded = par1Minecraft;
    }

    /**
     * Returns "Definitely" if the brand is not vanilla, "Very likely" if there are no signers for the Minecraft class,
     * otherwise "Probably not".
     */
    public String getModded()
    {
        String var1 = ClientBrandRetriever.getClientModName();
        return !var1.equals("vanilla") ? "Definitely; \'" + var1 + "\'" : (Minecraft.class.getSigners() == null ? "Very likely" : "Probably not");
    }

    public Object call()
    {
        return this.getModded();
    }
}
