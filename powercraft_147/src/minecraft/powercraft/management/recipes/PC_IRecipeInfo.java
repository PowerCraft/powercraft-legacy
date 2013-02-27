package powercraft.management.recipes;

import java.util.List;

import powercraft.management.PC_VecI;
import powercraft.management.item.PC_ItemStack;

public interface PC_IRecipeInfo extends PC_IRecipe {

	public PC_VecI getSize();
	public List<PC_ItemStack> getExpectedInputFor(int index);
	public int getRecipeSize();
	
}
