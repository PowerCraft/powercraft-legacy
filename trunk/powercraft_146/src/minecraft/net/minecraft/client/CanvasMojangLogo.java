package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;

@SideOnly(Side.CLIENT)
class CanvasMojangLogo extends Canvas
{
    /** BufferedImage object containing the Majong logo. */
    private BufferedImage logo;

    public CanvasMojangLogo()
    {
        try
        {
            this.logo = ImageIO.read(PanelCrashReport.class.getResource("/gui/crash_logo.png"));
        }
        catch (IOException var2)
        {
            ;
        }

        byte var1 = 100;
        this.setPreferredSize(new Dimension(var1, var1));
        this.setMinimumSize(new Dimension(var1, var1));
    }

    public void paint(Graphics par1Graphics)
    {
        super.paint(par1Graphics);
        par1Graphics.drawImage(this.logo, this.getWidth() / 2 - this.logo.getWidth() / 2, 32, (ImageObserver)null);
    }
}
