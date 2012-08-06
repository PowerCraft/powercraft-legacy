package net.minecraft.src;

import net.minecraft.client.Minecraft;

class NetClientWebTextures extends GuiScreen
{
    final String field_74244_a;

    /** Initialises Web Textures? */
    final NetClientHandler netClientHandlerWebTextures;

    NetClientWebTextures(NetClientHandler par1NetClientHandler, String par2Str)
    {
        this.netClientHandlerWebTextures = par1NetClientHandler;
        this.field_74244_a = par2Str;
    }

    public void confirmClicked(boolean par1, int par2)
    {
        this.mc = Minecraft.getMinecraft();

        if (this.mc.getServerData() != null)
        {
            this.mc.getServerData().setAcceptsTextures(par1);
            ServerList.func_78852_b(this.mc.getServerData());
        }

        if (par1)
        {
            this.mc.texturePackList.requestDownloadOfTexture(this.field_74244_a);
        }

        this.mc.displayGuiScreen((GuiScreen)null);
    }
}
