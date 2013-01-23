package powercraft.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_ShapelessRecipes;
import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class PC_ShapelessRecipeHandler extends TemplateRecipeHandler {

	public int[][] stackorder = new int[][]{
			{0,0},
			{1,0},
			{0,1},
			{1,1},
			{0,2},
			{1,2},
			{2,0},
			{2,1},
			{2,2}};
		
		public class CachedShapelessRecipe extends CachedRecipe
		{
		    public CachedShapelessRecipe()
	        {
	            ingredients = new ArrayList<PositionedStack>();
	        }
		    
			public CachedShapelessRecipe(ItemStack output)
			{
			    this();
			    setResult(output);
			}

	        public CachedShapelessRecipe(PC_ShapelessRecipes recipe)
			{
				this(recipe.getRecipeOutput());
				int size = recipe.getRecipeSize();
				List<Object> items = new ArrayList<Object>();
				for(int i=0; i<size; i++){
					List<PC_ItemStack> l = recipe.getExpectedInputFor(i);
					if(l!=null){
						if(l.size()==1){
							items.add(l.get(0).toItemStack());
						}else if(l.size()>1){
							List<ItemStack>l2 = new ArrayList<ItemStack>();
							for(PC_ItemStack is:l){
								l2.add(is.toItemStack());
							}
							items.add(l2);
						}
					}
				}
				setIngredients(items);
			}
			
			public CachedShapelessRecipe(Object[] input, ItemStack output)
			{
				this(Arrays.asList(input), output);
			}
			
			public CachedShapelessRecipe(List<?> input, ItemStack output)
			{
				this(output);
				setIngredients(input);
			}

			public void setIngredients(List<?> items)
			{
			    ingredients.clear();
				for(int ingred = 0; ingred < items.size(); ingred++)
				{
					PositionedStack stack = new PositionedStack(items.get(ingred), 25 + stackorder[ingred][0] * 18, 6 + stackorder[ingred][1] * 18);
					stack.setMaxSize(1);
					ingredients.add(stack);
				}
			}
			
			@SuppressWarnings("unchecked")
			public void setIngredients(ShapelessRecipes recipe)
			{
				ArrayList<ItemStack> items;
				try
				{
					items = ReflectionManager.getField(ShapelessRecipes.class, ArrayList.class, recipe, 1);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return;
				}
				
				setIngredients(items);
			}
	        
	        public void setResult(ItemStack output)
	        {
	            result = new PositionedStack(output, 119, 24);
	        }	
			
			@Override
			public ArrayList<PositionedStack> getIngredients()
			{
				return getCycledIngredients(cycleticks / 20, ingredients);
			}
			
			@Override
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
		
		public String getRecipeName()
		{
			return "PC Shapeless Crafting";
		}
		
		@Override
		public void loadCraftingRecipes(String outputId, Object... results)
		{
			if(outputId.equals("crafting") && getClass() == PC_ShapelessRecipeHandler.class)
			{
				@SuppressWarnings("unchecked")
				List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
				for(IRecipe irecipe : allrecipes)
				{
					CachedShapelessRecipe recipe = null;
					if(irecipe instanceof PC_ShapelessRecipes)
					{
						recipe = new CachedShapelessRecipe((PC_ShapelessRecipes)irecipe);
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
					CachedShapelessRecipe recipe = null;
					if(irecipe instanceof PC_ShapelessRecipes)
					{
						recipe = new CachedShapelessRecipe((PC_ShapelessRecipes)irecipe);
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
				CachedShapelessRecipe recipe = null;
				if(irecipe instanceof PC_ShapelessRecipes)
				{
					recipe = new CachedShapelessRecipe((PC_ShapelessRecipes)irecipe);
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
			return getIngredientStacks(recipe).size() <= 4;
		}
	
}
