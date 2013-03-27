package powercraft.api.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import powercraft.api.PC_VecI;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_3DRecipe;
import powercraft.api.recipes.PC_I3DRecipeHandler;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_IRecipeInfo;
import powercraft.api.recipes.PC_ShapedRecipes;
import powercraft.api.recipes.PC_ShapelessRecipes;
import powercraft.api.recipes.PC_SmeltRecipe;
import powercraft.api.reflect.PC_ReflectHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class PC_RecipeRegistry {

	private static List<PC_SmeltRecipe> smeltings = new ArrayList<PC_SmeltRecipe>();
	private static List<PC_3DRecipe> recipes3d = new ArrayList<PC_3DRecipe>();
	
	public static void register(PC_IRecipe recipe){
		if(recipe instanceof IRecipe){
			GameRegistry.addRecipe((IRecipe)recipe);
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
		FurnaceRecipes.smelting().addSmelting(isInput.itemID,
				isInput.getItemDamage(), isOutput, smeltRecipe.getExperience());
	}
	
	private static IRecipe resolveForgeRecipe(IRecipe recipe) {
		Object[] o;
		boolean isShaped;
		if (recipe instanceof ShapedOreRecipe) {
			o =  PC_ReflectHelper.getValue(ShapedOreRecipe.class, recipe, 3, Object[].class);
			int width = PC_ReflectHelper.getValue(ShapedOreRecipe.class, recipe, 4, int.class);
			int height = PC_ReflectHelper.getValue(ShapedOreRecipe.class, recipe, 5, int.class);
			List<PC_ItemStack> list[][] = new List[width][height];
			int i = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (i < o.length) {
						list[x][y] = new ArrayList<PC_ItemStack>();
						if (o[i] instanceof ItemStack) {
							list[x][y].add(new PC_ItemStack(
									(ItemStack) o[i]));
						} else if (o[i] instanceof List) {
							for (ItemStack is : (List<ItemStack>) o[i]) {
								list[x][y].add(new PC_ItemStack(is));
							}
						}
					}
					i++;
				}
			}
			return new PC_ShapedRecipes(new PC_ItemStack(
					recipe.getRecipeOutput()), new PC_VecI(width, height),
					list);
		} else if (recipe instanceof ShapelessOreRecipe) {
			o =  PC_ReflectHelper.getValue(ShapelessOreRecipe.class, recipe, 1, List.class).toArray(new Object[0]);
			List<PC_ItemStack> list[] = new List[o.length];
			for (int i = 0; i < o.length; i++) {
				list[i] = new ArrayList<PC_ItemStack>();
				if (o[i] instanceof ItemStack) {
					list[i].add(new PC_ItemStack((ItemStack) o[i]));
				} else if (o[i] instanceof List) {
					for (ItemStack is : (List<ItemStack>) o[i]) {
						list[i].add(new PC_ItemStack(is));
					}
				}
			}
			return new PC_ShapelessRecipes(new PC_ItemStack(
					recipe.getRecipeOutput()), list);
		} else {
			return null;
		}
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
					if (recipe instanceof ShapedOreRecipe
							|| recipe instanceof ShapelessOreRecipe) {
						ret.add(resolveForgeRecipe(recipe));
					} else {
						ret.add(recipe);
					}
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
				|| FurnaceRecipes.smelting().getSmeltingResult(itemstack) == null) {
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
			Map<List<Integer>, ItemStack> map2 = PC_ReflectHelper.getValue(FurnaceRecipes.class, FurnaceRecipes.smelting(), 3, Map.class);
			for (Entry<List<Integer>, ItemStack> e : map2.entrySet()) {
				if (e.getValue().isItemEqual(itemstack)) {
					l.add(new ItemStack((int) e.getKey().get(0), 1, e
							.getKey().get(1)));
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
					if (i < ri.getPCRecipeSize()) {
						list[x][y] = ri.getExpectedInputFor(i);
					}
					i++;
				}
			}
		} else if (recipe instanceof ShapedRecipes) {
			int sizeX = PC_ReflectHelper.getValue(ShapedRecipes.class, recipe, 0, int.class);
			int sizeY = PC_ReflectHelper.getValue(ShapedRecipes.class, recipe, 1, int.class);
			ItemStack[] stacks = PC_ReflectHelper.getValue(ShapedRecipes.class, recipe, 2, ItemStack[].class);
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
			List<ItemStack> stacks = PC_ReflectHelper.getValue(ShapelessRecipes.class, recipe, 1, List.class);
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
		return TileEntityFurnace.getItemBurnTime(itemstack);
	}

	public static ItemStack getSmeltingResult(ItemStack item) {
		return FurnaceRecipes.smelting().getSmeltingResult(item);
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
			
			Map map = PC_ReflectHelper.getValue(FurnaceRecipes.class, smlt, 3, HashMap.class);
			map.remove(Arrays.asList(Integer.valueOf(is.itemID), Integer.valueOf(is.getItemDamage())));
			
			map = PC_ReflectHelper.getValue(FurnaceRecipes.class, smlt, 2, Map.class);
			map.remove(Integer.valueOf(is.itemID));
			
			map = PC_ReflectHelper.getValue(FurnaceRecipes.class, smlt, 4, HashMap.class);
			map.remove(Arrays.asList(Integer.valueOf(is.itemID), Integer.valueOf(is.getItemDamage())));

		}
	}
	
	public static void loadSmeltRecipes(){
		for(PC_SmeltRecipe smelting:smeltings){
			ItemStack isInput = smelting.getInput().toItemStack();
			ItemStack isOutput = smelting.getResult().toItemStack();
			FurnaceRecipes.smelting().addSmelting(isInput.itemID,
					isInput.getItemDamage(), isOutput, smelting.getExperience());
		}
	}
	
}
