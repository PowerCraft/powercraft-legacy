package codechicken.nei;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.forge.GuiContainerManager;

public class UnusedBlock implements ItemPanelObject
{
	public UnusedBlock(int id)
	{
		blockID = id;
	}
	
	int blockID;

	@Override
	public void draw(GuiContainerManager gui, int x, int y)
	{
    	gui.drawItem(x, y, new ItemStack(Block.vine));
    	gui.drawTextCentered(x, y, 16, 16, ""+blockID, 0xFFFFFF);
	}

	@Override
	public List<String> handleTooltip(List<String> tooltip)
	{
		tooltip.add("Unused BlockID: "+blockID);
		return tooltip;
	}
}
