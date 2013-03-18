package codechicken.nei.recipe;

import java.util.ArrayList;

import codechicken.nei.NEIClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiCraftingRecipe extends GuiRecipe
{
	private GuiCraftingRecipe(GuiContainer prevgui, ArrayList<ICraftingHandler> handlers)
	{
		super(prevgui);
		currenthandlers = handlers;
	}
	
	public static void registerRecipeHandler(ICraftingHandler handler)
	{
		for(ICraftingHandler handler1 : craftinghandlers)
		{
			if(handler1.getClass() == handler.getClass())
				return;
		}
		
		craftinghandlers.add(handler);
	}
	
	public static boolean openRecipeGui(String outputId, Object... results)
	{
		Minecraft mc = NEIClientUtils.mc();
		if(!(mc.currentScreen instanceof GuiContainer))
			return false;
		GuiContainer prevscreen = (GuiContainer) mc.currentScreen;
		
		ArrayList<ICraftingHandler> handlers = new ArrayList<ICraftingHandler>();
		for(ICraftingHandler craftinghandler : craftinghandlers)
		{
			ICraftingHandler handler = craftinghandler.getRecipeHandler(outputId, results);
			if(handler.numRecipes() > 0)
				handlers.add(handler);
		}
		if(handlers.isEmpty())
			return false;

		mc.currentScreen = null;
		mc.displayGuiScreen(new GuiCraftingRecipe(prevscreen, handlers));
		return true;
	}
	
	public ArrayList<? extends IRecipeHandler> getCurrentRecipeHandlers()
	{
		return currenthandlers;
	}
	
	private ArrayList<ICraftingHandler> currenthandlers;
	
	private static ArrayList<ICraftingHandler> craftinghandlers = new ArrayList<ICraftingHandler>();
}
