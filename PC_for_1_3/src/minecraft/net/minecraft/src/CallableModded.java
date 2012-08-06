package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;

public class CallableModded implements Callable
{
    /** Gets if Minecraft is Modded. */
    final Minecraft minecraftModded;

    public CallableModded(Minecraft par1Minecraft)
    {
        this.minecraftModded = par1Minecraft;
    }

    public String func_74415_a()
    {
        String var1 = ClientBrandRetriever.getClientModName();
        return !var1.equals("vanilla") ? "Definitely; \'" + var1 + "\'" : (Minecraft.class.getClassLoader().getResource("META-INF/MOJANG_C.DSA") == null ? "Very likely" : "Probably not");
    }

    public Object call()
    {
        return this.func_74415_a();
    }
}
