package codechicken.nei;

import codechicken.nei.forge.GuiContainerManager;

public class SearchField extends TextField
{
	public SearchField(String ident)
	{
		super(ident);
	}
	
	@Override
	public void drawBox(GuiContainerManager gui)
	{
		if(NEIClientConfig.getBooleanSetting("options.searchinventories"))
			gui.drawGradientRect(x, y, width, height, 0xFFFFFF00, 0xFFC0B000);
		else
			gui.drawRect(x, y, width, height, 0xffA0A0A0);
		gui.drawRect(x + 1, y + 1, width - 2, height - 2, 0xFF000000);
	}
	
	@Override
	public boolean handleClick(int mousex, int mousey, int button)
	{
		if(button == 0)
		{
			if(focused() && (System.currentTimeMillis() - lastclicktime < 500))//double click
			{
				NEIClientConfig.toggleBooleanSetting("options.searchinventories");
			}
			lastclicktime = System.currentTimeMillis();
		}
		return super.handleClick(mousex, mousey, button);
	}
	
	@Override
	public void onTextChange()
	{
		NEIClientConfig.setSearchExpression(text);
		ItemList.updateSearch();
	}
	
	long lastclicktime;
}
