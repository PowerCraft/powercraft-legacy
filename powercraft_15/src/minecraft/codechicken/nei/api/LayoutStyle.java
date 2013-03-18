package codechicken.nei.api;

import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.VisiblityData;
import codechicken.nei.forge.GuiContainerManager;

public abstract class LayoutStyle
{
	public abstract void init();
	public abstract void reset();
	public abstract void layout(GuiContainer gui, VisiblityData visibility);
	public abstract String getName();
	
	public void drawBackground(GuiContainerManager gui){};
}
