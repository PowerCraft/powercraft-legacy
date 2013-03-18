package codechicken.nei.recipe;

import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;

import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEICompatibility;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedRecipeHandler extends TemplateRecipeHandler
{
	public class CachedShapedRecipe extends CachedRecipe
	{
		public CachedShapedRecipe(ShapedRecipes recipe)
		{
			result = new PositionedStack(recipe.getRecipeOutput(), 119, 24);
			ingredients = new ArrayList<PositionedStack>();
			setIngredients(recipe);
		}
		
		public CachedShapedRecipe(int width, int height, Object[] items, ItemStack out)
		{
			result = new PositionedStack(out, 119, 24);
			ingredients = new ArrayList<PositionedStack>();
			setIngredients(width, height, items);
		}
		
		/**
		 * @param width
		 * @param height
		 * @param items an ItemStack[] or ItemStack[][]
		 */
		public void setIngredients(int width, int height, Object[] items)
		{
			for(int x = 0; x < width; x++)
			{
				for(int y = 0; y < height; y++)
				{
					if(items[y*width+x] == null)
					{
						continue;
					}
					PositionedStack stack = new PositionedStack(items[y*width+x], 25+x*18, 6+y*18);
					stack.setMaxSize(1);
					ingredients.add(stack);
				}
			}
		}
		
		public void setIngredients(ShapedRecipes recipe)
		{
			int width;
			int height;
			ItemStack[] items;
			try
			{
				width =  ReflectionManager.getField(ShapedRecipes.class, Integer.class, recipe, 0);
				height = ReflectionManager.getField(ShapedRecipes.class, Integer.class, recipe, 1);
				items = ReflectionManager.getField(ShapedRecipes.class, ItemStack[].class, recipe, 2);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
			
			setIngredients(width, height, items);
		}
		
		@Override
		public ArrayList<PositionedStack> getIngredients()
		{
			return getCycledIngredients(cycleticks / 20, ingredients);
		}
		
		public PositionedStack getResult()
		{
			return result;
		}
		
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack result;
	}

	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
	}
	
	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiCrafting.class;
	}

	@Override
	public String getRecipeName()
	{
		return "Shaped Crafting";
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("crafting") && getClass() == ShapedRecipeHandler.class)
		{
			@SuppressWarnings("unchecked")
			List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
			for(IRecipe irecipe : allrecipes)
			{
				CachedShapedRecipe recipe = null;
				if(irecipe instanceof ShapedRecipes)
				{
					recipe = new CachedShapedRecipe((ShapedRecipes)irecipe);
				}
				else if(NEICompatibility.hasForge && weakDependancy_Forge.recipeInstanceShaped(irecipe))
				{
					recipe = weakDependancy_Forge.getShapedRecipe(this, irecipe);
				}

				if(recipe == null)
					continue;
				
				arecipes.add(recipe);
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		@SuppressWarnings("unchecked")
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe irecipe : allrecipes)
		{
			if(NEIClientUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result))
			{
				CachedShapedRecipe recipe = null;
				if(irecipe instanceof ShapedRecipes)
				{
					recipe = new CachedShapedRecipe((ShapedRecipes)irecipe);
				}
				else if(NEICompatibility.hasForge && weakDependancy_Forge.recipeInstanceShaped(irecipe))
				{
					recipe = weakDependancy_Forge.getShapedRecipe(this, irecipe);
				}
				
				if(recipe == null)
					continue;
				
				arecipes.add(recipe);
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		@SuppressWarnings("unchecked")
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe irecipe : allrecipes)
		{
			CachedShapedRecipe recipe = null;
			if(irecipe instanceof ShapedRecipes)
			{
				recipe = new CachedShapedRecipe((ShapedRecipes)irecipe);
			}
			else if(NEICompatibility.hasForge && weakDependancy_Forge.recipeInstanceShaped(irecipe))
			{
				recipe = weakDependancy_Forge.getShapedRecipe(this, irecipe);
			}

			if(recipe == null)
				continue;
			
			if(recipe.contains(recipe.ingredients, ingredient))
			{
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public String getGuiTexture()
	{
		return "/gui/crafting.png";
	}
    
    @Override
    public String getOverlayIdentifier()
    {
        return "crafting";
    }
    
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
    {
        return super.hasOverlay(gui, container, recipe) || RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
    }
	
	public boolean isRecipe2x2(int recipe)
	{
		for(PositionedStack stack : getIngredientStacks(recipe))
		{
			if(stack.relx > 43 || stack.rely > 24)return false;
		}
		return true;
	}
}
