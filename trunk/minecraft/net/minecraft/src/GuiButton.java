package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui
{
    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;

    /** ID for this control. */
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean drawButton;

    public GuiButton(int par1, int par2, int par3, String par4Str)
    {
        this(par1, par2, par3, 200, 20, par4Str);
    }

    public GuiButton(int par1, int par2, int par3, int par4, int par5, String par6Str)
    {
        width = 200;
        height = 20;
        enabled = true;
        drawButton = true;
        id = par1;
        xPosition = par2;
        yPosition = par3;
        width = par4;
        height = par5;
        displayString = par6Str;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean par1)
    {
        byte byte0 = 1;

        if (!enabled)
        {
            byte0 = 0;
        }
        else if (par1)
        {
            byte0 = 2;
        }

        return byte0;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (!drawButton)
        {
            return;
        }

        FontRenderer fontrenderer = par1Minecraft.fontRenderer;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean flag = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
        int i = getHoverState(flag);
        drawTexturedModalRect(xPosition, yPosition, 0, 46 + i * 20, width / 2, height);
        drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + i * 20, width / 2, height);
        mouseDragged(par1Minecraft, par2, par3);
        int j = 0xe0e0e0;

        if (!enabled)
        {
            j = 0xffa0a0a0;
        }
        else if (flag)
        {
            j = 0xffffa0;
        }

        drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, j);
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft minecraft, int i, int j)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int i, int j)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return enabled && drawButton && par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
    }
}
