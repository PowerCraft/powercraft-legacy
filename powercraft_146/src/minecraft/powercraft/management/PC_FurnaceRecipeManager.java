package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PC_FurnaceRecipeManager {

	private static List<PC_IFurnaceRecipe> recipes = new ArrayList<PC_IFurnaceRecipe>();
	
	public static void addRecipe(PC_IFurnaceRecipe recipe){
		recipes.add(recipe);
	}
	
	public static PC_Struct2<List<PC_ItemStack>, Integer> getSmeltingResult(World world, InventoryCrafting inventoryCrafting, ItemStack fuel){
		for(PC_IFurnaceRecipe recipe:recipes){
			if(recipe.matches(inventoryCrafting, world, fuel)){
				return new PC_Struct2<List<PC_ItemStack>, Integer>(recipe.getRecipeOutput(), recipe.getSmeltTime());
			}
		}
		return null;
	}
	
}
