package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.BorderLayout;
import java.awt.Canvas;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import org.lwjgl.LWJGLException;

@SideOnly(Side.CLIENT)
public class MinecraftAppletImpl extends Minecraft
{
    /** Reference to the main frame, in this case, the applet window itself. */
    final MinecraftApplet mainFrame;

    public MinecraftAppletImpl(MinecraftApplet par1MinecraftApplet, Canvas par2Canvas, MinecraftApplet par3MinecraftApplet, int par4, int par5, boolean par6)
    {
        super(par2Canvas, par3MinecraftApplet, par4, par5, par6);
        this.mainFrame = par1MinecraftApplet;
    }

    public void displayCrashReportInternal(CrashReport par1CrashReport)
    {
        this.mainFrame.removeAll();
        this.mainFrame.setLayout(new BorderLayout());
        this.mainFrame.add(new PanelCrashReport(par1CrashReport), "Center");
        this.mainFrame.validate();
    }

    /**
     * Starts the game: initializes the canvas, the title, the settings, etcetera.
     */
    public void startGame() throws LWJGLException
    {
        this.mcDataDir = getMinecraftDir();
        this.gameSettings = new GameSettings(this, this.mcDataDir);

        if (this.gameSettings.field_92119_C > 0 && this.gameSettings.field_92118_B > 0 && this.mainFrame.getParent() != null && this.mainFrame.getParent().getParent() != null)
        {
            this.mainFrame.getParent().getParent().setSize(this.gameSettings.field_92118_B, this.gameSettings.field_92119_C);
        }

        super.startGame();
    }
}
