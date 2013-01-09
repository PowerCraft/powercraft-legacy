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
	
	public static PC_Struct2<List<ItemStack>, Float> getSmeltingResult(World world, PC_InventoryFurnace inv, ItemStack fuel){
		for(PC_IFurnaceRecipe recipe:recipes){
			PC_Struct2<List<ItemStack>, Float> ret = recipe.getOutput(world, inv, fuel);
			if(ret!=null)
				return ret;
		}
		return null;
	}
	
}
