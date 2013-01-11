package codechicken.nei.api;

import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.client.gui.inventory.GuiContainer;

import codechicken.nei.NEIChestGuiHandler;
import codechicken.nei.NEICreativeGuiHandler;

public class GuiInfo
{	
	public static LinkedList<INEIGuiHandler> guiHandlers = new LinkedList<INEIGuiHandler>();
	
	public static void load()
	{
		API.registerNEIGuiHandler(new NEICreativeGuiHandler());
		API.registerNEIGuiHandler(new NEIChestGuiHandler());
	}

	public static void clearGuiHandlers()
	{
		for(Iterator<INEIGuiHandler> iterator = guiHandlers.iterator(); iterator.hasNext();)
			if(iterator.next() instanceof GuiContainer)
				iterator.remove();
	}
}
