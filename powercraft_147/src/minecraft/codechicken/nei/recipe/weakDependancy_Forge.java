package codechicken.nei.recipe;

import java.util.ArrayList;
import java.util.List;

import codechicken.core.ReflectionManager;
import codechicken.nei.SpawnerRenderer;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.ShapelessRecipeHandler.CachedShapelessRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class weakDependancy_Forge
{
	public static void addMobSpawnerRenderer()
	{
		MinecraftForgeClient.registerItemRenderer(Block.mobSpawner.blockID, new SpawnerRenderer());
	}

	public static boolean recipeInstanceShaped(IRecipe irecipe)
	{
		return irecipe instanceof ShapedOreRecipe;
	}

	public static boolean recipeInstanceShapeless(IRecipe irecipe)
	{
		return irecipe instanceof ShapelessOreRecipe;
	}

	public static CachedShapedRecipe getShapedRecipe(ShapedRecipeHandler handler, IRecipe irecipe)
	{
		ShapedOreRecipe recipe = (ShapedOreRecipe)irecipe;
		
		int width;
		int height;
		Object[] items;
		try
		{
			width = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 4);
			height = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 5);
			items = ReflectionManager.getField(ShapedOreRecipe.class, Object[].class, recipe, 3);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] instanceof List && ((List<?>)items[i]).isEmpty())//ore handler, no ores
				return null;
		}
		
		return handler.new CachedShapedRecipe(width, height, items, recipe.getRecipeOutput());
	}

	public static CachedShapelessRecipe getShapelessRecipe(ShapelessRecipeHandler handler, IRecipe irecipe)
	{
		ShapelessOreRecipe recipe = (ShapelessOreRecipe)irecipe;

		ArrayList<?> items;
		try
		{
			items = ReflectionManager.getField(ShapelessOreRecipe.class, ArrayList.class, recipe, 1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		for(int i = 0; i < items.size(); i++)
		{
			if(items.get(i) instanceof List && ((List<?>)items.get(i)).isEmpty())//ore handler, no ores
				return null;
		}
		
		return handler.new CachedShapelessRecipe(items, recipe.getRecipeOutput());
	}

}
