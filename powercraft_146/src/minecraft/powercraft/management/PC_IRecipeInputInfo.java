package powercraft.management;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface PC_IRecipeInputInfo {

	public List<ItemStack> getExpectedInput(List<ItemStack> itemStacks);
	
}
