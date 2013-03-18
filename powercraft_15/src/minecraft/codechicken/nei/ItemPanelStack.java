package codechicken.nei;

import java.util.List;

import net.minecraft.item.ItemStack;
import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.forge.GuiContainerManager;

public class ItemPanelStack implements ItemPanelObject
{
	ItemStack item;
	
	public ItemPanelStack(ItemStack itemstack)
	{
		item = itemstack;
	}
	
	@Override
	public void draw(GuiContainerManager gui, int x, int y)
	{
        gui.drawItem(x, y, (ItemStack) item);
	}

	@Override
	public List<String> handleTooltip(List<String> tooltip)
	{
		return tooltip;
	}
}
