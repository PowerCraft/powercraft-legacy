package powercraft.management;

import java.util.List;

import net.minecraft.src.ItemStack;

public interface PC_IRecipeInputInfo {

	public List<ItemStack> getExpectedInput(List<ItemStack> itemStacks);
	
}
