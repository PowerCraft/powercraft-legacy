package codechicken.nei;

import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiNEIBlockIDs extends GuiNEISettings
{
	public GuiNEIBlockIDs(GuiContainer parentContainer)
	{
		super(parentContainer);
	}
	
	@Override
	public String getBackButtonName()
	{
		return "Settings";
	}
	
	@Override
	public void onBackButtonClick()
	{
        mc.displayGuiScreen(new GuiNEISettings(parentScreen));
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		if(!NEIClientConfig.canDump())
			NEIClientConfig.getSetting("ID dump.dump on load").setBooleanValue(false);
		
		updateButtonNames();
	}
}
