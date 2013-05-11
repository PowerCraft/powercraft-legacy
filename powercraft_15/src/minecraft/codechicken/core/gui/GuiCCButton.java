package codechicken.core.gui;

import org.lwjgl.opengl.GL11;

public class GuiCCButton extends GuiWidget
{    
    public String text;
    public String actionCommand;
    private boolean isEnabled = true;
    public boolean drawButton = true;
    
    public GuiCCButton(int x, int y, int width, int height, String text)
    {
        super(x, y, width, height);
        this.text = text;
    }
    
    public void setText(String s)
    {
        text = s;
    }
    
    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean b)
    {
        isEnabled = b;
    }
    
    @Override
    public void mouseClicked(int x, int y, int button)
    {
        if(isEnabled && pointInside(x, y))
            sendAction(parentScreen, actionCommand, button);
    }
    
    @Override
    public void draw(int mousex, int mousey, float frame)
    {
        if(!drawButton)
            return;
        
        renderEngine.bindTexture("/gui/gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean mouseover = pointInside(mousex, mousey);
        int state = !isEnabled ? 0 : mouseover ? 2 : 1;
        drawTexturedModalRect(x, y, 0, 46 + state * 20, width / 2, height / 2);//top left
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46 + state * 20, width / 2, height / 2);//top right
        drawTexturedModalRect(x, y + height / 2, 0, 46 + state * 20 + 20 - height / 2, width / 2, height / 2);//bottom left
        drawTexturedModalRect(x + width / 2, y + height / 2, 200 - width / 2, 46 + state * 20 + 20 - height / 2, width / 2, height / 2);//bottom right
        
        int textcolour = !isEnabled ? 0xffa0a0a0 : mouseover ? 0xffffa0 : 0xe0e0e0;
        drawCenteredString(fontRenderer, text, x + width / 2, y + (height - 8) / 2, textcolour);
    }

    public GuiCCButton setActionCommand(String string)
    {
        actionCommand = string;
        return this;
    }
}
