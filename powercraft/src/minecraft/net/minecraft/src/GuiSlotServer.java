package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiSlotServer extends GuiSlot
{
    /** Instance to the GUI this list is on. */
    final GuiMultiplayer parentGui;

    public GuiSlotServer(GuiMultiplayer par1GuiMultiplayer)
    {
        super(par1GuiMultiplayer.mc, par1GuiMultiplayer.width, par1GuiMultiplayer.height, 32, par1GuiMultiplayer.height - 64, 36);
        this.parentGui = par1GuiMultiplayer;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return GuiMultiplayer.func_74006_a(this.parentGui).countServers() + GuiMultiplayer.func_74003_b(this.parentGui).size() + 1;
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
        if (par1 < GuiMultiplayer.func_74006_a(this.parentGui).countServers() + GuiMultiplayer.func_74003_b(this.parentGui).size())
        {
            int var3 = GuiMultiplayer.func_74020_c(this.parentGui);
            GuiMultiplayer.func_74015_a(this.parentGui, par1);
            boolean var4 = GuiMultiplayer.func_74020_c(this.parentGui) >= 0 && GuiMultiplayer.func_74020_c(this.parentGui) < this.getSize();
            boolean var5 = GuiMultiplayer.func_74020_c(this.parentGui) < GuiMultiplayer.func_74006_a(this.parentGui).countServers();
            GuiMultiplayer.getButtonSelect(this.parentGui).enabled = var4;
            GuiMultiplayer.getButtonEdit(this.parentGui).enabled = var5;
            GuiMultiplayer.getButtonDelete(this.parentGui).enabled = var5;

            if (par2 && var4)
            {
                GuiMultiplayer.func_74008_b(this.parentGui, par1);
            }
            else if (var5 && GuiScreen.isShiftKeyDown() && var3 >= 0 && var3 < GuiMultiplayer.func_74006_a(this.parentGui).countServers())
            {
                GuiMultiplayer.func_74006_a(this.parentGui).swapServers(var3, GuiMultiplayer.func_74020_c(this.parentGui));
            }
        }
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return par1 == GuiMultiplayer.func_74020_c(this.parentGui);
    }

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * 36;
    }

    protected void drawBackground()
    {
        this.parentGui.drawDefaultBackground();
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        if (par1 < GuiMultiplayer.func_74006_a(this.parentGui).countServers())
        {
            this.func_77247_d(par1, par2, par3, par4, par5Tessellator);
        }
        else if (par1 < GuiMultiplayer.func_74006_a(this.parentGui).countServers() + GuiMultiplayer.func_74003_b(this.parentGui).size())
        {
            this.func_77248_b(par1, par2, par3, par4, par5Tessellator);
        }
        else
        {
            this.func_77249_c(par1, par2, par3, par4, par5Tessellator);
        }
    }

    private void func_77248_b(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        LanServer var6 = (LanServer)GuiMultiplayer.func_74003_b(this.parentGui).get(par1 - GuiMultiplayer.func_74006_a(this.parentGui).countServers());
        this.parentGui.drawString(this.parentGui.fontRenderer, StatCollector.translateToLocal("lanServer.title"), par2 + 2, par3 + 1, 16777215);
        this.parentGui.drawString(this.parentGui.fontRenderer, var6.func_77487_a(), par2 + 2, par3 + 12, 8421504);

        if (this.parentGui.mc.gameSettings.hideServerAddress)
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, "(Hidden)", par2 + 2, par3 + 12 + 11, 3158064);
        }
        else
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, var6.func_77488_b(), par2 + 2, par3 + 12 + 11, 3158064);
        }
    }

    private void func_77249_c(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        this.parentGui.drawCenteredString(this.parentGui.fontRenderer, StatCollector.translateToLocal("lanServer.scanning"), this.parentGui.width / 2, par3 + 1, 16777215);
        String var6;

        switch (GuiMultiplayer.func_74010_g(this.parentGui) / 3 % 4)
        {
            case 0:
            default:
                var6 = "O o o";
                break;
            case 1:
            case 3:
                var6 = "o O o";
                break;
            case 2:
                var6 = "o o O";
        }

        this.parentGui.drawCenteredString(this.parentGui.fontRenderer, var6, this.parentGui.width / 2, par3 + 12, 8421504);
    }

    private void func_77247_d(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        ServerData var6 = GuiMultiplayer.func_74006_a(this.parentGui).getServerData(par1);

        synchronized (GuiMultiplayer.func_74011_h())
        {
            if (GuiMultiplayer.func_74012_i() < 5 && !var6.field_78841_f)
            {
                var6.field_78841_f = true;
                var6.pingToServer = -2L;
                var6.serverMOTD = "";
                var6.populationInfo = "";
                GuiMultiplayer.func_74021_j();
                (new ThreadPollServers(this, var6)).start();
            }
        }

        this.parentGui.drawString(this.parentGui.fontRenderer, var6.serverName, par2 + 2, par3 + 1, 16777215);
        this.parentGui.drawString(this.parentGui.fontRenderer, var6.serverMOTD, par2 + 2, par3 + 12, 8421504);
        this.parentGui.drawString(this.parentGui.fontRenderer, var6.populationInfo, par2 + 215 - this.parentGui.fontRenderer.getStringWidth(var6.populationInfo), par3 + 12, 8421504);

        if (this.parentGui.mc.gameSettings.hideServerAddress)
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, "(Hidden)", par2 + 2, par3 + 12 + 11, 3158064);
        }
        else
        {
            this.parentGui.drawString(this.parentGui.fontRenderer, var6.serverIP, par2 + 2, par3 + 12 + 11, 3158064);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.parentGui.mc.renderEngine.bindTexture(this.parentGui.mc.renderEngine.getTexture("/gui/icons.png"));
        String var9 = "";
        byte var7;
        int var8;

        if (var6.field_78841_f && var6.pingToServer != -2L)
        {
            var7 = 0;
            boolean var12 = false;

            if (var6.pingToServer < 0L)
            {
                var8 = 5;
            }
            else if (var6.pingToServer < 150L)
            {
                var8 = 0;
            }
            else if (var6.pingToServer < 300L)
            {
                var8 = 1;
            }
            else if (var6.pingToServer < 600L)
            {
                var8 = 2;
            }
            else if (var6.pingToServer < 1000L)
            {
                var8 = 3;
            }
            else
            {
                var8 = 4;
            }

            if (var6.pingToServer < 0L)
            {
                var9 = "(no connection)";
            }
            else
            {
                var9 = var6.pingToServer + "ms";
            }
        }
        else
        {
            var7 = 1;
            var8 = (int)(Minecraft.getSystemTime() / 100L + (long)(par1 * 2) & 7L);

            if (var8 > 4)
            {
                var8 = 8 - var8;
            }

            var9 = "Polling..";
        }

        this.parentGui.drawTexturedModalRect(par2 + 205, par3, 0 + var7 * 10, 176 + var8 * 8, 10, 8);
        byte var10 = 4;

        if (this.mouseX >= par2 + 205 - var10 && this.mouseY >= par3 - var10 && this.mouseX <= par2 + 205 + 10 + var10 && this.mouseY <= par3 + 8 + var10)
        {
            GuiMultiplayer.func_74009_a(this.parentGui, var9);
        }
    }
}
