package powercraft.management;

import java.util.List;

import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public interface PC_IFurnaceRecipe extends PC_IRecipeInfo {

	public List<PC_ItemStack> getRecipeOutput();
	public int getSmeltTime();
	boolean matches(InventoryCrafting inventoryCrafting, World world, ItemStack fuel);
	
}
