package powercraft.management.recipes;

import java.util.List;

import powercraft.management.PC_ItemStack;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface PC_IFurnaceRecipe extends PC_IRecipeInfo {

	public List<PC_ItemStack> getRecipeOutput();
	public int getSmeltTime();
	boolean matches(InventoryCrafting inventoryCrafting, World world, ItemStack fuel);
	
}
