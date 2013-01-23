package codechicken.nei.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.ItemStack;

public class FuelRecipeHandler extends FurnaceRecipeHandler
{
	public class CachedFuelRecipe extends CachedRecipe
	{
		public FuelPair fuel;
		
		public CachedFuelRecipe(FuelPair fuel)
		{
			this.fuel = fuel;
		}
		
		@Override
		public PositionedStack getIngredient()
		{
			return mfurnace.get(cycleticks / 48 % mfurnace.size()).ingred;
		}
		
		@Override
		public PositionedStack getResult()
		{
			return mfurnace.get(cycleticks / 48 % mfurnace.size()).result;
		}
		
		@Override
		public PositionedStack getOtherStack()
		{
			return fuel.stack;
		}
	}
	
	public FuelRecipeHandler()
	{
		super();
		loadAllSmelting();
	}
	
	public String getRecipeName()
	{
		return "Fuel";
	}
	
	@SuppressWarnings("unchecked")
	public void loadAllSmelting()
	{
		if(mfurnace != null)//already loaded;
			return;
		
		mfurnace = new ArrayList<FurnaceRecipeHandler.SmeltingPair>();
		
		HashMap<Integer, ItemStack> recipes;
		HashMap<List<Integer>, ItemStack> metarecipes = null;
		try
		{
			recipes = (HashMap<Integer, ItemStack>) ReflectionManager.getField(FurnaceRecipes.class, HashMap.class, FurnaceRecipes.smelting(), 1);
			try
			{
				metarecipes = ReflectionManager.getField(FurnaceRecipes.class, HashMap.class, FurnaceRecipes.smelting(), 3);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
		{
			ItemStack item = recipe.getValue();
			int ingred = recipe.getKey();
			mfurnace.add(new SmeltingPair(new ItemStack(ingred, 1, 0), item));
			for(int i = 1; i < 16; i++)
			{
				ItemStack stack = new ItemStack(ingred, 1, i);
				if(!NEIClientUtils.isValidItem(stack))
				{
					break;
				}
				mfurnace.add(new SmeltingPair(stack, item));
			}
		}
		if(metarecipes == null)return;
		for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
		{
			ItemStack item = recipe.getValue();
			mfurnace.add(new SmeltingPair(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
		}
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("fuel") && getClass() == FuelRecipeHandler.class)
		{
			for(FuelPair fuel : afuels)
			{
				arecipes.add(new CachedFuelRecipe(fuel));
			}
		}
	}
	
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for(FuelPair fuel : afuels)
		{
			if(NEIClientUtils.areStacksSameTypeCrafting(ingredient, fuel.stack.item))
			{
				arecipes.add(new CachedFuelRecipe(fuel));
			}
		}
	}
	
	public String getOverlayIdentifier()
	{
		return "fuel";
	}
	
	@Override
	public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
	{
		CachedFuelRecipe crecipe = (CachedFuelRecipe)arecipes.get(recipe);
		FuelPair fuel = crecipe.fuel;
		float burnTime = fuel.burnTime / 200F;
		
		if(gui.isMouseOver(fuel.stack, recipe) && burnTime < 1)
		{
			burnTime = 1F/burnTime;
			String burnString = Float.toString(burnTime);
			if(burnTime == Math.round(burnTime))
				burnString = Integer.toString((int)burnTime);
			
			burnString = burnString + " required";
			
			currenttip.add(burnString);
		}
		else if((gui.isMouseOver(crecipe.getResult(), recipe) || gui.isMouseOver(crecipe.getIngredient(), recipe)) && burnTime > 1)
		{
			String burnString = Float.toString(burnTime);
			if(burnTime == Math.round(burnTime))
				burnString = Integer.toString((int)burnTime);

			burnString = burnString + (gui.isMouseOver(crecipe.getResult(), recipe) ? " produced" : " processed");
			
			currenttip.add(burnString);
		}
		
		return currenttip;
	}
	
	public static ArrayList<SmeltingPair> mfurnace;
}
