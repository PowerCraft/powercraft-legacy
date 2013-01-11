package codechicken.nei;

import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.nei.forge.GuiContainerManager;

public abstract class Button extends Widget
{
    public Button(String s)
    {
        label = s;
    }
    
    public Button()
    {
        label = "";
    }

	public int contentWidth(GuiContainerManager gui)
    {
        int textw = getRenderIcon() == null ? gui.getStringWidth(label) : getRenderIcon().width;
        if(NEIClientConfig.getLayoutStyle() == 0)
        {
        	textw+=4;
        }
        else if((state & 0x4) != 0)
        {
            textw += stateOff.width;
        }
        return textw;
    }

    public void setOwnWidth(GuiContainerManager gui)
    {
        width = contentWidth(gui) + getMargin();
    }

    public int getMargin()
    {
        return 2;
    }
    
    public void draw(GuiContainerManager gui, int mousex, int mousey)
    {
    	boolean mouseover = contains(mousex, mousey);
        Image renderIcon = getRenderIcon();
        if(NEIClientConfig.getLayoutStyle() == 1)//TMI
        {
            int cwidth = contentWidth(gui);
            int textx = x + (width - cwidth) / 2;
            int texty = y + (height - 8) / 2;
            
            gui.drawRect(x, y, width, height, contains(mousex, mousey) ? 0xee401008 : 0xee000000);
        	
            if(renderIcon == null)
            {
                gui.drawText(textx, texty, label, -1);
            }
            else
            {
                int icony = y + (height - renderIcon.height) / 2;
                LayoutManager.drawIcon(gui.window, textx, icony, renderIcon);
                if((state & 0x3) == 2)
                	gui.drawRect(textx, icony, renderIcon.width, renderIcon.height, 0x80000000);
            
	            if((state & 0x4) != 0)
	            {
	                Image stateimage;
	                if((state & 0x3) == 1)
	    	        	stateimage = stateOn;
	    	        else if((state & 0x3) == 2)
	    	        	stateimage = stateDisabled;
	    	        else
	    	        	stateimage = stateOff;
	                LayoutManager.drawIcon(gui.window, textx+renderIcon.width, icony, stateimage);
	            }
            }
        }
        else
        {
            GL11.glDisable(2896 /*GL_LIGHTING*/);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        
	        int tex;
	        if((state & 0x3) == 1)
	        	tex = 2;
	        else if((state & 0x3) == 2)
	        	tex = 0;
	        else
	        	tex = 1;
	        LayoutManager.drawButtonBackground(gui, x, y, width, height, true, tex);
	        
	        if(renderIcon == null)
	        {
		        if(mouseover && (state & 0x3) != 2 || (state & 0x3) == 1)
		        {
		        	gui.drawTextCentered(label, x + width / 2, y + (height - 8) / 2, 0xffffa0);
		        } 
		        else if((state & 0x3) == 2)
		        {
		        	gui.drawTextCentered(label, x + width / 2, y + (height - 8) / 2, 0x601010);
		        }
		        else
		        {
		        	gui.drawTextCentered(label, x + width / 2, y + (height - 8) / 2, 0xe0e0e0);
		        }
	        }
	        else
            {
	        	if(!iconHighlight)
		        {
		        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		        }
	            int iconx = x + (width - icon.width) / 2;
	            int icony = y + (height - icon.height) / 2;
	            LayoutManager.drawIcon(gui.window, iconx, icony, renderIcon);
            }
        }
        
    }

	public boolean handleClick(int i, int j, int k)
    {
        if(k == 1 || k == 0)
        	if(onButtonPress(k == 1))
        		NEIClientUtils.mc().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        return true;
    }
	
	public abstract boolean onButtonPress(boolean rightclick);

	public Image getRenderIcon()
	{
		return icon;
	}
    
	@Override
	public List<String> handleTooltip(int mx, int my, List<String> tooltip)
	{
		if(!contains(mx, my))
			return tooltip;
		
		String tip = getButtonTip();
		if(tip != null)
			tooltip.add(tip);
		return tooltip;
	}
	
    public String getButtonTip()
	{
		return null;
	}

	public String label;
    //public String identifier;
    public Image icon;
    public boolean iconHighlight;
    
    /**
     * 0x4 = state flag, as opposed to 1 click
     * 0 = normal
     * 1 = on
     * 2 = disabled
     */
    public int state;
    
    public static final Image stateOff = new Image(48, 0, 8, 12);
    public static final Image stateOn = new Image(56, 0, 8, 12);
    public static final Image stateDisabled = new Image(64, 0, 8, 12);
}
