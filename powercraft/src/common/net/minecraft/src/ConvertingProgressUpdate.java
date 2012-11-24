package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.server.MinecraftServer;

public class ConvertingProgressUpdate implements IProgressUpdate
{
    private long field_82309_b;

    final MinecraftServer mcServer;

    public ConvertingProgressUpdate(MinecraftServer par1MinecraftServer)
    {
        this.mcServer = par1MinecraftServer;
        this.field_82309_b = System.currentTimeMillis();
    }

    public void displayProgressMessage(String par1Str) {}

    public void setLoadingProgress(int par1)
    {
        if (System.currentTimeMillis() - this.field_82309_b >= 1000L)
        {
            this.field_82309_b = System.currentTimeMillis();
            MinecraftServer.logger.info("Converting... " + par1 + "%");
        }
    }

    @SideOnly(Side.CLIENT)

    public void resetProgressAndMessage(String par1Str) {}

    @SideOnly(Side.CLIENT)

    public void onNoMoreProgress() {}

    public void resetProgresAndWorkingMessage(String par1Str) {}
}
