package codechicken.core.inventory;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiWidget;
import codechicken.core.gui.IGuiActionListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiContainerWidget extends GuiContainer implements IGuiActionListener
{    
    public ArrayList<GuiWidget> widgets = new ArrayList<GuiWidget>();
    
    public GuiContainerWidget(Container inventorySlots)
    {
        this(inventorySlots, 176, 166);
    }
    
    public GuiContainerWidget(Container inventorySlots, int xSize, int ySize)
    {
        super(inventorySlots);
        this.xSize = xSize;
        this.ySize = ySize;
    }
    
    @Override
    public void setWorldAndResolution(Minecraft mc, int i, int j)
    {
        boolean init = this.mc == null;
        super.setWorldAndResolution(mc, i, j);
        if(init)
            addWidgets();
    }
    
    public void add(GuiWidget widget)
    {
        widgets.add(widget);
        widget.onAdded(this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mousex, int mousey)
    {
        GL11.glTranslated(guiLeft, guiTop, 0);
        drawBackground();
        for(GuiWidget widget : widgets)
            widget.draw(mousex-guiLeft, mousey-guiTop, f);
        
        GL11.glTranslated(-guiLeft, -guiTop, 0);
    }
    
    public void drawBackground()
    {
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        for(GuiWidget widget : widgets)
            widget.mouseClicked(x-guiLeft, y-guiTop, button);
    }
    
    @Override
    protected void mouseMovedOrUp(int x, int y, int button)
    {
        super.mouseMovedOrUp(x, y, button);
        for(GuiWidget widget : widgets)
            widget.mouseMovedOrUp(x-guiLeft, y-guiTop, button);
    }
    
    @Override
    protected void func_85041_a(int x, int y, int button, long time)
    {
        super.func_85041_a(x, y, button, time);
        for(GuiWidget widget : widgets)
            widget.mouseDragged(x-guiLeft, y-guiTop, button, time);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        if(mc.currentScreen == this)
            for(GuiWidget widget : widgets)
                widget.update();
    }
    
    @Override
    public void keyTyped(char c, int keycode)
    {
        super.keyTyped(c, keycode);
        for(GuiWidget widget : widgets)
            widget.keyTyped(c, keycode);        
    }
    
    @Override
    public void actionPerformed(String ident, Object... params)
    {
    }

    public void addWidgets()
    {
        // TODO Auto-generated method stub
        
    }
}
