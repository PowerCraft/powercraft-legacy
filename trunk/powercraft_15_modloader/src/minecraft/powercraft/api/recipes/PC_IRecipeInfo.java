package powercraft.api.recipes;

import java.util.List;

import powercraft.api.PC_VecI;
import powercraft.api.item.PC_ItemStack;

public interface PC_IRecipeInfo extends PC_IRecipe {

	public PC_VecI getSize();
	public List<PC_ItemStack> getExpectedInputFor(int index);
	public int getPCRecipeSize();
	
}
