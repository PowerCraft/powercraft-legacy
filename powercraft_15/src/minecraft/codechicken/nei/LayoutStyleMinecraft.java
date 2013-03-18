package codechicken.nei;

import codechicken.nei.forge.GuiContainerManager;

public class LayoutStyleMinecraft extends LayoutStyleDefault
{
	int stateButtonCount;
	int clickButtonCount;

	@Override
	public String getName()
	{
		return "Minecraft Layout";
	}
	
	@Override
	public void init()
	{
		LayoutManager.delete.icon = new Image(144, 12, 12, 12);		
		LayoutManager.creative.icon = new Image(132, 12, 12, 12);
		LayoutManager.creative.icon2 = new Image(156, 12, 12, 12);
		LayoutManager.rain.icon = new Image(120, 12, 12, 12);        
		LayoutManager.magnet.icon = new Image(180, 24, 12, 12);        
		LayoutManager.timeButtons[0].icon = new Image(132, 24, 12, 12);
		LayoutManager.timeButtons[1].icon = new Image(120, 24, 12, 12);
		LayoutManager.timeButtons[2].icon = new Image(144, 24, 12, 12);
		LayoutManager.timeButtons[3].icon = new Image(156, 24, 12, 12);        
		LayoutManager.heal.icon = new Image(168, 24, 12, 12);
	}
	
	@Override
	public void reset()
	{
		stateButtonCount = clickButtonCount = 0;
	}

	@Override
	public void layoutButton(Button button, GuiContainerManager gui)
	{
		boolean edgeAlign = NEIClientConfig.getBooleanSetting("options.edge-align buttons");
        int offsetx = edgeAlign ? 0 : 6;
        int offsety = edgeAlign ? 0 : 3;
		
        if((button.state & 0x4) != 0)
		{
			button.x = offsetx + stateButtonCount*20;
			button.y = offsety;
			stateButtonCount++;
		}
        else
        {
			button.x = offsetx + (clickButtonCount%4)*20;
			button.y = offsety + (1+clickButtonCount/4)*18;
			clickButtonCount++;
        }
		
		button.height = 17;
		button.setOwnWidth(gui);
	}
}
