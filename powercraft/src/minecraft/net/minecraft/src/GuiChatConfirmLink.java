package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
class GuiChatConfirmLink extends GuiConfirmOpenLink
{
    /** Instance of ChatClickData. */
    final ChatClickData theChatClickData;

    final GuiChat chatGui;

    GuiChatConfirmLink(GuiChat par1GuiChat, GuiScreen par2GuiScreen, String par3Str, int par4, ChatClickData par5ChatClickData)
    {
        super(par2GuiScreen, par3Str, par4);
        this.chatGui = par1GuiChat;
        this.theChatClickData = par5ChatClickData;
    }

    /**
     * Copies the link to the system clipboard.
     */
    public void copyLinkToClipboard()
    {
        setClipboardString(this.theChatClickData.getClickedUrl());
    }
}
