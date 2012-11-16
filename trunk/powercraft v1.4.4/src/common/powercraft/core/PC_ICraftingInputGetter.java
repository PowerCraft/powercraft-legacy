package powercraft.core;

import java.util.List;

import net.minecraft.src.ItemStack;

public interface PC_ICraftingInputGetter {

	public List<ItemStack> getExpectedInput(List<ItemStack> itemStacks);
	
}
