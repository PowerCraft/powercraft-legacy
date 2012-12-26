package net.minecraft.server.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.minecraft.server.MinecraftServer;

@SideOnly(Side.SERVER)
class ServerGuiCommandListener implements ActionListener
{
    /** Text field. */
    final JTextField textField;

    /** Reference to the ServerGui object. */
    final ServerGUI mcServerGui;

    ServerGuiCommandListener(ServerGUI par1ServerGUI, JTextField par2JTextField)
    {
        this.mcServerGui = par1ServerGUI;
        this.textField = par2JTextField;
    }

    public void actionPerformed(ActionEvent par1ActionEvent)
    {
        String var2 = this.textField.getText().trim();

        if (var2.length() > 0)
        {
            ServerGUI.getDedicatedServer(this.mcServerGui).addPendingCommand(var2, MinecraftServer.getServer());
        }

        this.textField.setText("");
    }
}
