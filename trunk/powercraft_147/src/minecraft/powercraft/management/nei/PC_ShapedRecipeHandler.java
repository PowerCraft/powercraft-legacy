package powercraft.management.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_ShapedRecipes;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class PC_ShapedRecipeHandler extends TemplateRecipeHandler {

	public class CachedShapedRecipe extends CachedRecipe
	{
		public CachedShapedRecipe(PC_ShapedRecipes recipe)
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
		
		public void setIngredients(PC_ShapedRecipes recipe)
		{
			int width;
			int height;
			Object[] items;
			try
			{
				width = recipe.getSize().x;
				height = recipe.getSize().y;
				items = new Object[width*height];
				for(int i=0; i<items.length; i++){
					List<PC_ItemStack> l = recipe.getExpectedInputFor(i);
					if(l!=null){
						if(l.size()==1){
							items[i] = l.get(0).toItemStack();
						}else if(l.size()>1){
							List<ItemStack>l2 = new ArrayList<ItemStack>();
							for(PC_ItemStack is:l){
								l2.add(is.toItemStack());
							}
							items[i] = l2;
						}
					}
				}
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
		return "PC Shaped Crafting";
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("crafting") && getClass() == PC_ShapedRecipeHandler.class)
		{
			@SuppressWarnings("unchecked")
			List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
			for(IRecipe irecipe : allrecipes)
			{
				CachedShapedRecipe recipe = null;
				if(irecipe instanceof PC_ShapedRecipes)
				{
					if(irecipe.getRecipeOutput()!=null){
						recipe = new CachedShapedRecipe((PC_ShapedRecipes)irecipe);
					}
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
				if(irecipe instanceof PC_ShapedRecipes)
				{
					if(irecipe.getRecipeOutput()!=null){
						recipe = new CachedShapedRecipe((PC_ShapedRecipes)irecipe);
					}
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
			if(irecipe instanceof PC_ShapedRecipes)
			{
				if(irecipe.getRecipeOutput()!=null){
					recipe = new CachedShapedRecipe((PC_ShapedRecipes)irecipe);
				}
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
