package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@SideOnly(Side.SERVER)
class ServerGuiFocusAdapter extends FocusAdapter
{
    /** Reference to the ServerGui object. */
    final ServerGUI mcServerGui;

    ServerGuiFocusAdapter(ServerGUI par1ServerGUI)
    {
        this.mcServerGui = par1ServerGUI;
    }

    public void focusGained(FocusEvent par1FocusEvent) {}
}
