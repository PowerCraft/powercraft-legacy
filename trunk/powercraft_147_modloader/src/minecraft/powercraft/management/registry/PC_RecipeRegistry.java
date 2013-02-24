package powercraft.management.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ShapedRecipes;
import net.minecraft.src.ShapelessRecipes;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.World;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_VecI;
import powercraft.management.recipes.PC_3DRecipe;
import powercraft.management.recipes.PC_I3DRecipeHandler;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_IRecipeInfo;
import powercraft.management.recipes.PC_SmeltRecipe;
import powercraft.management.reflect.PC_ReflectHelper;

public class PC_RecipeRegistry {

	private static List<PC_SmeltRecipe> smeltings = new ArrayList<PC_SmeltRecipe>();
	private static List<PC_3DRecipe> recipes3d = new ArrayList<PC_3DRecipe>();
	
	public static void register(PC_IRecipe recipe){
		if(recipe instanceof IRecipe){
			CraftingManager.getInstance().getRecipeList().add((IRecipe)recipe);
		}else if(recipe instanceof PC_3DRecipe){
			add3DRecipe((PC_3DRecipe)recipe);
		}else if(recipe instanceof PC_SmeltRecipe){
			addSmeltingRecipes((PC_SmeltRecipe)recipe);
		}
	}
	
	public static void add3DRecipe(PC_3DRecipe recipe){
		recipes3d.add(recipe);
	}
	
	public static void add3DRecipe(PC_I3DRecipeHandler obj, Object...o){
		recipes3d.add(new PC_3DRecipe(obj, o));
	}
	
	public static void addSmeltingRecipes(PC_SmeltRecipe smeltRecipe) {
		smeltings.add(smeltRecipe);
		ItemStack isInput = smeltRecipe.getInput().toItemStack();
		ItemStack isOutput = smeltRecipe.getResult().toItemStack();
		FurnaceRecipes.smelting().addSmelting(isInput.itemID, isOutput, smeltRecipe.getExperience());
	}

	public static List<IRecipe> getRecipesForProduct(ItemStack prod) {
		List<IRecipe> recipes = new ArrayList<IRecipe>(CraftingManager
				.getInstance().getRecipeList());
		List<IRecipe> ret = new ArrayList<IRecipe>();

		for (IRecipe recipe : recipes) {
			try {
				if (recipe.getRecipeOutput().isItemEqual(prod)
						|| (recipe.getRecipeOutput().itemID == prod.itemID && prod
								.getItemDamage() == -1)) {

					ret.add(recipe);
				}
			} catch (NullPointerException npe) {
				continue;
			}
		}

		return ret;
	}
	
	public static boolean isFuel(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}
		return getFuelValue(itemstack) > 0;
	}

	public static boolean isSmeltable(ItemStack itemstack) {
		if (itemstack == null
				|| FurnaceRecipes.smelting().getSmeltingResult(itemstack.itemID) == null) {
			return false;
		}

		return true;
	}
	
	public static List<ItemStack> getFeedstock(ItemStack itemstack) {
		List<ItemStack> l = new ArrayList<ItemStack>();
		if (itemstack != null) {
			Map<Integer, ItemStack> map = FurnaceRecipes.smelting()
					.getSmeltingList();
			for (Entry<Integer, ItemStack> e : map.entrySet()) {
				if (e.getValue().isItemEqual(itemstack)) {
					l.add(new ItemStack((int) e.getKey(), 1, 0));
				}
			}
		}
		return l;
	}
	
	public static List<PC_ItemStack>[][] getExpectedInput(IRecipe recipe,
			int width, int hight) {
		List<PC_ItemStack>[][] list;
		if (recipe instanceof PC_IRecipeInfo) {
			PC_IRecipeInfo ri = (PC_IRecipeInfo) recipe;
			PC_VecI size = ri.getSize();
			if (size != null) {
				if (width == -1)
					width = size.x;
				if (hight == -1)
					hight = size.y;
				if (size.x > width || size.y > hight)
					return null;
			} else {
				int rsize = recipe.getRecipeSize();
				if (width == -1)
					width = rsize;
				if (hight == -1)
					hight = 1;
				if (hight * width < rsize || rsize == 0)
					return null;
				size = new PC_VecI(width, hight);
			}
			list = new List[width][hight];
			int i = 0;
			for (int y = 0; y < size.y; y++) {
				for (int x = 0; x < size.x; x++) {
					if (i < ri.getRecipeSize()) {
						list[x][y] = ri.getExpectedInputFor(i);
					}
					i++;
				}
			}
		} else if (recipe instanceof ShapedRecipes) {
			int sizeX = (Integer) PC_ReflectHelper.getValue(
					ShapedRecipes.class, recipe, 0);
			int sizeY = (Integer) PC_ReflectHelper.getValue(
					ShapedRecipes.class, recipe, 1);
			ItemStack[] stacks = (ItemStack[]) PC_ReflectHelper.getValue(ShapedRecipes.class, recipe, 2);
			if (width == -1)
				width = sizeX;
			if (hight == -1)
				hight = sizeY;
			if (sizeX > width || sizeY > hight)
				return null;
			list = new List[width][hight];
			int i = 0;
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					if (i < stacks.length) {
						if (stacks[i] != null) {
							list[x][y] = new ArrayList<PC_ItemStack>();
							list[x][y].add(new PC_ItemStack(stacks[i]));
						}
					}
					i++;
				}
			}
		} else if (recipe instanceof ShapelessRecipes) {
			List<ItemStack> stacks = ((List<ItemStack>) PC_ReflectHelper.getValue(ShapelessRecipes.class, recipe, 1));
			if (width == -1)
				width = stacks.size();
			if (hight == -1)
				hight = 1;
			if (hight * width < stacks.size())
				return null;
			list = new List[width][hight];
			int i = 0;
			for (int y = 0; y < hight; y++) {
				for (int x = 0; x < width; x++) {
					if (i < stacks.size()) {
						list[x][y] = new ArrayList<PC_ItemStack>();
						list[x][y].add(new PC_ItemStack(stacks.get(i)));
					}
				}
			}
		} else {
			return null;
		}
		return list;
	}

	public static int getFuelValue(ItemStack itemstack) {
		return (int) (TileEntityFurnace.getItemBurnTime(itemstack));
	}

	public static ItemStack getSmeltingResult(ItemStack item) {
		return FurnaceRecipes.smelting().getSmeltingResult(item.itemID);
	}
	
	public static boolean searchRecipe3DAndDo(EntityPlayer entityplayer, World world, PC_VecI pos){
		for(PC_3DRecipe recipe:recipes3d){
			if(recipe.isStruct(entityplayer, world, pos)){
				return true;
			}
		}
		return false;
	}
	
	public static void unloadSmeltRecipes(){
		FurnaceRecipes smlt = FurnaceRecipes.smelting();
		for(PC_SmeltRecipe smelting:smeltings){
			ItemStack is = smelting.getInput().toItemStack();
			smlt.getSmeltingList().remove(Integer.valueOf(is.itemID));
			int idx = 2; // ValueWriting.getFieldIDByName(FurnaceRecipes.class,
						// "experienceList");
			if (idx != -1) {
				Object o = PC_ReflectHelper.getValue(FurnaceRecipes.class,
						smlt, idx);
				if (o instanceof Map) {
					Map map = (Map) o;
					map.remove(Integer.valueOf(is.itemID));
				}
			}
		}
	}
	
	public static void loadSmeltRecipes(){
		for(PC_SmeltRecipe smelting:smeltings){
			ItemStack isInput = smelting.getInput().toItemStack();
			ItemStack isOutput = smelting.getResult().toItemStack();
			FurnaceRecipes.smelting().addSmelting(isInput.itemID, isOutput, smelting.getExperience());
		}
	}
	
}
