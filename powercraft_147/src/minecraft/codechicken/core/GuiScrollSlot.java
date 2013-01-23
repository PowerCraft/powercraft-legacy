package codechicken.core;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public abstract class GuiScrollSlot extends Gui
{
    public GuiScrollSlot(IGuiIndirectButtons parent)
    {
    	parentscreen = parent;
    }
    
    public void setSize(int x, int y, int w, int h)
    {
    	this.x = x;
    	this.y = y;
    	this.width = w;
    	this.height = h;
    	
    	this.contentx = x + 3;
    	this.contenty = y + 2;
    	this.contentheight = height - 2;
    }
    
    public void setContentSize(int x, int y, int h)
    {
    	this.contentx = x;
    	this.contenty = y;
    	this.contentheight = h;
    }
    
    public void registerButtons(GuiButton up, GuiButton down, GuiButton activate)
    {
    	scrollupbutton = up;
        scrolldownbutton = down;
        activatebutton = activate;
    }

    public abstract int getSlotHeight();

    protected abstract int getNumSlots();

    public abstract void selectNext();
    
    public abstract void selectPrev();

    protected abstract void slotClicked(int slot, boolean doubleclick);

    protected abstract boolean isSlotSelected(int slot);

    protected abstract void drawSlot(int slot, int x, int y, boolean selected);

    protected void unfocus()
    {
    	
    }
    
    public void setFocused(boolean focus)
    {
    	if(!focus)
    	{
    		unfocus();
    	}
    	focused = focus;
    }
    
    public void scrollUp()
    {
    	percentscrolled -= 0.05;
    	calculatePercentScrolled();
    }
    
    public void scrollDown()
    {
    	percentscrolled += 0.05;
    	calculatePercentScrolled();
    }
    
    public int getContentHeight()
    {
        return getNumSlots() * getSlotHeight();
    }

    public int getClickedSlot(int mousey)
    {
    	int startheight = contenty - (int)((getContentHeight() - contentheight) * percentscrolled + 0.5);
    	for(int slot = 0; slot < getNumSlots(); slot++)
    	{
    		int sloty = startheight + slot * getSlotHeight();
    		if(mousey >= sloty && mousey < sloty + getSlotHeight())
    		{
    			return slot;
    		}
    	}
    	return -1;
    }
    
    public int getScrollBarWidth()
    {
    	return 5;
    }
    
    public int getScrollBarHeight()
    {
    	int sbarh = (int)((contentheight  / (float)getContentHeight()) * height);
    	if(sbarh > height)
    	{
    		return height;
    	}
    	else if(sbarh < height / 15)
    	{
    		return height / 15;
    	}
    	else
    	{
    		return sbarh;
    	}
    }
    
    public void calculatePercentScrolled()
    {
		int barempty = height - getScrollBarHeight();
    	if(scrollclicky >= 0)
    	{
    		int scrolldiff = scrollmousey - scrollclicky;
    		percentscrolled = scrolldiff / (float)barempty + scrollpercent;
    	}
    	if(percentscrolled < 0)
    	{
    		percentscrolled = 0;
    	}
    	if(percentscrolled > 1)
    	{
    		percentscrolled = 1;
    	}
    	int sbary = y + (int)(barempty * percentscrolled + 0.5);
    	percentscrolled = (sbary - y) / (float) barempty;
    }
    
    public void showSlot(int slot)
    {
		int startheight = contenty - (int)((getContentHeight() - contentheight) * percentscrolled + 0.5);
    	int sloty = startheight + slot * getSlotHeight();
    	
    	while(sloty > contenty + contentheight - getSlotHeight())
    	{
    		scrollDown();
    		startheight = contenty - (int)((getContentHeight() - contentheight) * percentscrolled + 0.5);
    		sloty = startheight + slot * getSlotHeight();
    	}
    	while(sloty < contenty)
    	{
    		scrollUp();
    		startheight = contenty - (int)((getContentHeight() - contentheight) * percentscrolled + 0.5);
    		sloty = startheight + slot * getSlotHeight();
    	}
    }

    public void processMouse(int mousex, int mousey)
    {
		if(scrollclicky >= 0)
    	{
    		int scrolldiff = mousey - scrollclicky;
    		int barupallowed = (int)((height - getScrollBarHeight()) * scrollpercent + 0.5);
    		int bardownallowed = (height - getScrollBarHeight()) - barupallowed;
    		
    		if(-scrolldiff > barupallowed)
    		{
    			scrollmousey = scrollclicky - barupallowed;
    		}
    		else if(scrolldiff > bardownallowed)
    		{
    			scrollmousey = scrollclicky + bardownallowed;
    		}
    		else
    		{
    			scrollmousey = mousey;
    		}
    		
    		calculatePercentScrolled();
    	}
    }
    
    public void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(scrollupbutton != null && guibutton.id == scrollupbutton.id)
        {
        	scrollUp();
        }
        else if(scrolldownbutton != null && guibutton.id == scrolldownbutton.id)
        {
        	scrollDown();
        }
    }
    
    public void mouseClicked(int mousex, int mousey, int button)
    {
    	boolean needsfocus = (mousex >= x && mousex < x + width &&
    			mousey >= y && mousey <= y + height);
    	if(needsfocus != focused)
    	{
    		setFocused(needsfocus);
    	}
    	
    	if(button != 0)
    	{
    		return;
    	}
    	
    	int barempty = height - getScrollBarHeight();
    	int sbarx = x + width - getScrollBarWidth();
    	int sbary = y + (int)(barempty * percentscrolled + 0.5);
    	
    	if(getScrollBarHeight() < height && //the scroll bar can move (not full length)
    			mousex >= sbarx && mousex <= x + width &&
    			mousey >= y && mousey <= y + height)//in the scroll pane
    	{
    		if(mousey < sbary)
    		{
    			percentscrolled = (mousey - y) / (float)barempty;
    			calculatePercentScrolled();
    		}
    		else if(mousey > sbary + getScrollBarHeight())
    		{
    			percentscrolled = (mousey - y - getScrollBarHeight() + 1) / (float)barempty;
    			calculatePercentScrolled();
    		}
    		else
    		{
	    		scrollclicky = mousey;
	    		scrollpercent = percentscrolled;
	    		scrollmousey = mousey;
    		}
    	}
    	else if(mousex >= contentx && mousex < sbarx &&
    			mousey >= contenty && mousey <= contenty + contentheight)//in the box
    	{
    		int slot = getClickedSlot(mousey);
    		if(slot == lastslotclicked && System.currentTimeMillis() - lastslotclicktime < 500)
    		{
    			slotClicked(slot, true);
    		}
    		else
    		{
    			slotClicked(slot, false);
    		}
    		
    		lastslotclicked = slot;
    		lastslotclicktime = System.currentTimeMillis();
    	}
    }
    
    public void mouseMovedOrUp(int mousex, int mousey, int button)
    {
    	if(scrollclicky >= 0 && button == 0)//we were scrolling and we released mouse
    	{
    		scrollclicky = -1;
    	}
    }
    
    public void keyTyped(char c, int keycode)
    {
    	if(!focused)
    	{
    		return;
    	}
		if(keycode == Keyboard.KEY_UP)
		{
			selectPrev();
		}
		if(keycode == Keyboard.KEY_DOWN)
		{
			selectNext();
		}
		if(keycode == Keyboard.KEY_RETURN && activatebutton != null)
		{
			parentscreen.buttonPressed(activatebutton);
		}
    }
    
    public void drawSlotBox()
    {
        drawRect(x, y, x + width, y + height, 0xff000000);
    }
    
    public void drawBackground()
    {
    	
    }
    
    public void drawOverlay()
    {
    	//outlines
    	drawRect(x, y - 1, x + width, y, 0xffa0a0a0);//top
    	drawRect(x, y + height, x + width, y + height + 1, 0xffa0a0a0);//bottom
    	drawRect(x - 1, y - 1, x, y + height + 1, 0xffa0a0a0);//left
    	drawRect(x + width, y - 1, x + width + 1, y + height + 1, 0xffa0a0a0);//right
    }
    
    public void drawScrollBar()
    {
    	int sbarx = x + width - getScrollBarWidth();
    	int sbary = y + (int)((height - getScrollBarHeight()) * percentscrolled + 0.5);
    	
    	drawRect(sbarx - 1, y, sbarx, y + height, 0xFF808080);//lineguide
    	
    	drawRect(sbarx, sbary, x + width, sbary + getScrollBarHeight(), 0xFFE0E0E0);//scrollbar
    }
    
    public void drawSlots()
    {
    	int startheight = contenty - (int)((getContentHeight() - contentheight) * percentscrolled + 0.5);
    	for(int slot = 0; slot < getNumSlots(); slot++)
    	{
    		int sloty = startheight + slot * getSlotHeight();
    		if(sloty > contenty - getSlotHeight() && sloty < contenty + contentheight)
    		{
    			drawSlot(slot, contentx, sloty, isSlotSelected(slot));
    		}
    	}
    }
    
    public void drawScreen(int mousex, int mousey, float partialframe)
    {
    	processMouse(mousex, mousey);
    	drawBackground();
    	drawSlotBox();
    	drawSlots();
    	drawScrollBar();
    	drawOverlay();
    }
    
    protected GuiButton scrollupbutton;
    protected GuiButton scrolldownbutton;
    protected GuiButton activatebutton;
    protected IGuiIndirectButtons parentscreen;
    
    protected int scrollclicky = -1;
    protected float scrollpercent;
    protected int scrollmousey;
    protected float percentscrolled;
    
    protected int lastslotclicked = -1;
    protected long lastslotclicktime;
    
    public boolean focused;
    
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    protected int contentx;
    protected int contenty;
    protected int contentheight;
}
