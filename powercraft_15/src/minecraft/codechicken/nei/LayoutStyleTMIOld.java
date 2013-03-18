package codechicken.nei;

import codechicken.nei.forge.GuiContainerManager;

public class LayoutStyleTMIOld extends LayoutStyleDefault
{
	int stateButtonCount;
	int clickButtonCount;

	@Override
	public String getName()
	{
		return "Old TMI Layout";
	}
	
	@Override
	public void init()
	{
		LayoutManager.delete.icon = new Image(24, 12, 12, 12);		
		LayoutManager.creative.icon = new Image(12, 12, 12, 12);
		LayoutManager.creative.icon2 = new Image(36, 12, 12, 12);        
		LayoutManager.rain.icon = new Image(0, 12, 12, 12);        
		LayoutManager.magnet.icon = new Image(60, 24, 12, 12);        
		LayoutManager.timeButtons[0].icon = new Image(12, 24, 12, 12);
		LayoutManager.timeButtons[1].icon = new Image(0, 24, 12, 12);
		LayoutManager.timeButtons[2].icon = new Image(24, 24, 12, 12);
		LayoutManager.timeButtons[3].icon = new Image(36, 24, 12, 12);        
		LayoutManager.heal.icon = new Image(48, 24, 12, 12);
	}
	
	@Override
	public void reset()
	{
		stateButtonCount = clickButtonCount = 0;
	}

	@Override
	public void layoutButton(Button button, GuiContainerManager gui)
	{
        int offsetx = 2;
        int offsety = 2;
		
        if((button.state & 0x4) != 0)
		{
			button.x = offsetx + stateButtonCount*22;
			button.y = offsety;
			stateButtonCount++;
		}
        else
        {
			button.x = offsetx + (clickButtonCount%4)*22;
			button.y = offsety + (1+clickButtonCount/4)*17;
			clickButtonCount++;
        }
		
		button.height = 14;
		button.setOwnWidth(gui);
	}
	
	@Override
	public void drawBackground(GuiContainerManager gui)
	{
		if(clickButtonCount == 0 && stateButtonCount == 0)
			return;
		
		int maxx = Math.max(stateButtonCount, clickButtonCount);
		if(maxx > 4)maxx = 4;
		int maxy = clickButtonCount == 0 ? 1 : (clickButtonCount / 4 + 2);
		
		gui.drawRect(0, 0, 2+22*maxx, 1+maxy*17, 0xFF000000);
	}
}
