package codechicken.core.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderEngine;

public class GuiWidget extends Gui
{
    public GuiScreen parentScreen;
    public RenderEngine renderEngine;
    public FontRenderer fontRenderer;
    
    public int x;
    public int y;
    public int width;
    public int height;
    
    public GuiWidget(int x, int y, int width, int height)
    {
        setSize(x, y, width, height);
    }
    
    public void setSize(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean pointInside(int px, int py)
    {
        return px >= x && px < x + width && py >= y && py < y + height;
    }

    public static void sendAction(GuiScreen screen, String actionCommand, Object... params)
    {
        if(actionCommand != null && screen instanceof IGuiActionListener)
            ((IGuiActionListener) screen).actionPerformed(actionCommand, params);
    }

    public void mouseClicked(int x, int y, int button)
    {
    }

    public void mouseMovedOrUp(int x, int y, int button)
    {
    }
    
    public void mouseDragged(int x, int y, int button, long time)
    {
    }
    
    public void update()
    {
    }

    public void draw(int mousex, int mousey, float frame)
    {
    }

    public void keyTyped(char c, int keycode)
    {
    }
    
    public void onAdded(GuiScreen s)
    {
        parentScreen = s;
        renderEngine = s.mc.renderEngine;
        fontRenderer = s.fontRenderer;
    }
}
