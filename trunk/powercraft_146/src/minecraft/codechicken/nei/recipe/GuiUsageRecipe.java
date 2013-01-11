package codechicken.nei.recipe;

import java.util.ArrayList;

import codechicken.nei.NEIClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiUsageRecipe extends GuiRecipe
{
	private GuiUsageRecipe(GuiContainer prevgui, ArrayList<IUsageHandler> handlers)
	{
		super(prevgui);
		currenthandlers = handlers;
	}

	public static void registerUsageHandler(IUsageHandler handler)
	{
		for(IUsageHandler handler1 : usagehandlers)
		{
			if(handler1.getClass() == handler.getClass())
				return;
		}
		
		usagehandlers.add(handler);
	}
	
	public static boolean openRecipeGui(String inputId, Object... ingredients)
	{
		Minecraft mc = NEIClientUtils.mc();
		if(!(mc.currentScreen instanceof GuiContainer))
			return false;
		GuiContainer prevscreen = (GuiContainer) mc.currentScreen;
		
		ArrayList<IUsageHandler> handlers = new ArrayList<IUsageHandler>();
		for(IUsageHandler usagehandler : usagehandlers)
		{
			IUsageHandler handler = usagehandler.getUsageHandler(inputId, ingredients);
			if(handler.numRecipes() > 0)
				handlers.add(handler);
		}
		if(handlers.isEmpty())
			return false;

		mc.currentScreen = null;
		mc.displayGuiScreen(new GuiUsageRecipe(prevscreen, handlers));
		return true;
	}
	
	public ArrayList<? extends IRecipeHandler> getCurrentRecipeHandlers()
	{
		return currenthandlers;
	}
	
	private ArrayList<IUsageHandler> currenthandlers;
	
	private static ArrayList<IUsageHandler> usagehandlers = new ArrayList<IUsageHandler>();
}
