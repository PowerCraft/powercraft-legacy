package powercraft.management;

import java.util.List;

public interface PC_IRecipeInfo {

	public PC_VecI getSize();
	public List<PC_ItemStack> getExpectedInputFor(int index);
	public int getRecipeSize();
	public boolean canBeCrafted();
	
}
