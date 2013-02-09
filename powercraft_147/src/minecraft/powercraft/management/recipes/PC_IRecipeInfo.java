package powercraft.management.recipes;

import java.util.List;

import powercraft.management.PC_ItemStack;
import powercraft.management.PC_VecI;

import net.minecraft.item.ItemStack;

public interface PC_IRecipeInfo {

	public PC_VecI getSize();
	public List<PC_ItemStack> getExpectedInputFor(int index);
	public int getRecipeSize();
	public boolean canBeCrafted();
	
}
