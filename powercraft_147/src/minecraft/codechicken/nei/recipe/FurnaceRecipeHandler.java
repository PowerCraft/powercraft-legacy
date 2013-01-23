package codechicken.nei.recipe;

import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceRecipeHandler extends TemplateRecipeHandler
{
	public class SmeltingPair extends CachedRecipe
	{
		public SmeltingPair(ItemStack ingred, ItemStack result)
		{
			ingred.stackSize = 1;
			this.ingred = new PositionedStack(ingred, 51, 6);
			this.result = new PositionedStack(result, 111, 24);
		}
		
		public PositionedStack getIngredient()
		{
			int cycle = cycleticks / 48;
			if(ingred.item.getItemDamage() == -1)
			{
				PositionedStack stack = ingred.copy();
				int maxDamage = 0;
				do
				{
					maxDamage++;
					stack.item.setItemDamage(maxDamage);
				}
				while(NEIClientUtils.isValidItem(stack.item));
				
				stack.item.setItemDamage(cycle % maxDamage);
				return stack;
			}
			return ingred;
		}
		
		public PositionedStack getResult()
		{
			return result;
		}
		
		public PositionedStack getOtherStack() 
		{
			return afuels.get((cycleticks/48) % afuels.size()).stack;
		}
		
		PositionedStack ingred;
		PositionedStack result;
	}
	
	public static class FuelPair
	{
		public FuelPair(ItemStack ingred, int burnTime)
		{
			this.stack = new PositionedStack(ingred, 51, 42);
			this.burnTime = burnTime;
		}
		
		public PositionedStack stack;
		public int burnTime;
	}
	
	public static ArrayList<FuelPair> afuels;
	public static TreeSet<Integer> efuels;

	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel"));
		transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "smelting"));
	}
	
	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiFurnace.class;
	}
	
	@Override
	public String getRecipeName()
	{
		return "Smelting";
	}

	@Override
	@SuppressWarnings("unchecked")
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("smelting") && getClass() == FurnaceRecipeHandler.class)//don't want subclasses getting a hold of this
		{
			HashMap<Integer, ItemStack> recipes;
			HashMap<List<Integer>, ItemStack> metarecipes = null;
			try
			{
				recipes = (HashMap<Integer, ItemStack>)  ReflectionManager.getField(FurnaceRecipes.class, HashMap.class, FurnaceRecipes.smelting(), 1);
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
				arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey(), 1, -1), item));
			}
			if(metarecipes == null)return;
			for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
			{
				ItemStack item = recipe.getValue();
				arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
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
			if(NEIClientUtils.areStacksSameType(item, result))
			{
				arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey(), 1, -1), item));
			}
		}
		if(metarecipes == null)return;
		for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
		{
			ItemStack item = recipe.getValue();
			if(NEIClientUtils.areStacksSameType(item, result))
			{
				arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients)
	{
		if(inputId.equals("fuel") && getClass() == FurnaceRecipeHandler.class)//don't want subclasses getting a hold of this
		{
			loadCraftingRecipes("smelting");
		}
		else
		{
			super.loadUsageRecipes(inputId, ingredients);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		HashMap<Integer, ItemStack> recipes;
		HashMap<List<Integer>, ItemStack> metarecipes = null;
		try
		{
			recipes = (HashMap<Integer, ItemStack>)  ReflectionManager.getField(FurnaceRecipes.class, HashMap.class, FurnaceRecipes.smelting(), 1);
			try
			{
				metarecipes = (HashMap<List<Integer>, ItemStack>) ReflectionManager.getField(FurnaceRecipes.class, HashMap.class, FurnaceRecipes.smelting(), 3);
			}
			catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
		{
			ItemStack item = recipe.getValue();
			if(ingredient.itemID == recipe.getKey())
			{
				arecipes.add(new SmeltingPair(ingredient, item));
			}
		}
		if(metarecipes == null)return;
		for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
		{
			ItemStack item = recipe.getValue();
			if(ingredient.itemID == recipe.getKey().get(0) && ingredient.getItemDamage() == recipe.getKey().get(1))
			{
				arecipes.add(new SmeltingPair(ingredient, item));
			}
		}
	}
	
	@Override
	public String getGuiTexture()
	{
		return "/gui/furnace.png";
	}
	
	public void drawExtras(GuiContainerManager gui, int recipe)
	{
		drawProgressBar(gui, 51, 25, 176, 0, 14, 14, 48, 7);
		drawProgressBar(gui, 74, 23, 176, 14, 24, 16, 48, 0);
	}
	
	private static void removeFuels()
	{
		efuels = new TreeSet<Integer>();
		efuels.add(Block.mushroomCapBrown.blockID);
		efuels.add(Block.mushroomCapRed.blockID);
		efuels.add(Block.signPost.blockID);
		efuels.add(Block.signWall.blockID);
		efuels.add(Block.doorWood.blockID);
		efuels.add(Block.lockedChest.blockID);
	}
	
	private static void findFuels()
	{
		Method getBurnTime;
		try
		{
			getBurnTime = TileEntityFurnace.class.getDeclaredMethod("getItemBurnTime", ItemStack.class);
			getBurnTime.setAccessible(true);
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
			return;
		}
		catch(NoSuchMethodException e)
		{
			try
			{
				getBurnTime = TileEntityFurnace.class.getDeclaredMethod("a", ItemStack.class);
				getBurnTime.setAccessible(true);
			}
			catch(SecurityException e1)
			{
				e1.printStackTrace();
				return;
			}
			catch(NoSuchMethodException e1)
			{
				e1.printStackTrace();
				return;
			}
		}
		
		TileEntityFurnace afurnace = new TileEntityFurnace();		
		afuels = new ArrayList<FuelPair>();
		for(Item item : Item.itemsList)
		{
			if(item != null && !efuels.contains(item.itemID))
			{
				int burnTime;
				try
				{
					burnTime = (Integer) getBurnTime.invoke(afurnace, new ItemStack(item, 1));
				}
				catch(Exception e)
				{
					e.printStackTrace();
					continue;
				}
				if(burnTime > 0)
				{
					afuels.add(new FuelPair(new ItemStack(item, 1), burnTime));
					for(int i = 1; i < 16; i++)
					{
						ItemStack stack = new ItemStack(item, 1, i);
						if(!NEIClientUtils.isValidItem(stack))
						{
							break;
						}
						afuels.add(new FuelPair(stack, burnTime));
					}
				}
			}
		}
	}
		
	@Override
	public String getOverlayIdentifier()
	{
		return "smelting";
	}
	
	static
	{
		removeFuels();
		findFuels();
	}
}
