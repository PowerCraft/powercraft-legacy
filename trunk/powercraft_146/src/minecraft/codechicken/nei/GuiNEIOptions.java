package codechicken.nei;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiNEIOptions extends GuiScreen
{
	public GuiNEIOptions(GuiContainer parentContainer)
	{
	    parentScreen = parentContainer;
	}
	
	@SuppressWarnings("unchecked")
	public void initGui()
	{
	    controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, "Done"));
	    addBackButton();
	    updateButtonNames();
	}
	
	public abstract void updateButtonNames();
	
	@SuppressWarnings("unchecked")
	public void addBackButton()
	{
	    controlList.add(new GuiButton(201, width / 2 - 100, height / 6 + 145, getBackButtonName()));
	}
	
	public abstract String getBackButtonName();
	
	public abstract void onBackButtonClick();

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id == 200)
	    {
	        mc.displayGuiScreen(parentScreen);
	        parentScreen.refresh();
	    }
	    else if(guibutton.id == 201)
	    {
	    	onBackButtonClick();
	    }
	}
	
	protected void keyTyped(char keyChar, int keyID)
	{
		if(keyID == 1)
		{
	        mc.displayGuiScreen(parentScreen);
			parentScreen.refresh();
		}
		super.keyTyped(keyChar, keyID);
		updateButtonNames();
	}
	
	public void drawScreen(int i, int j, float f)
	{
	    drawDefaultBackground();	    
	    super.drawScreen(i, j, f);
	}
	
	public GuiContainer parentScreen;
}
