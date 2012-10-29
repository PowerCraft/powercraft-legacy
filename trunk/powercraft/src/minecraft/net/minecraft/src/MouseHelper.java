package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.awt.Component;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class MouseHelper
{
    private Component windowComponent;

    /** Mouse delta X this frame */
    public int deltaX;

    /** Mouse delta Y this frame */
    public int deltaY;

    public MouseHelper(Component par1Component)
    {
        this.windowComponent = par1Component;
    }

    /**
     * Grabs the mouse cursor it doesn't move and isn't seen.
     */
    public void grabMouseCursor()
    {
        Mouse.setGrabbed(true);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    /**
     * Ungrabs the mouse cursor so it can be moved and set it to the center of the screen
     */
    public void ungrabMouseCursor()
    {
        Mouse.setCursorPosition(this.windowComponent.getWidth() / 2, this.windowComponent.getHeight() / 2);
        Mouse.setGrabbed(false);
    }

    public void mouseXYChange()
    {
        this.deltaX = Mouse.getDX();
        this.deltaY = Mouse.getDY();
    }
}
