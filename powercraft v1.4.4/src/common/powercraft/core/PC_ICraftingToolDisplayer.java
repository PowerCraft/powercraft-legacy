package powercraft.core;

import java.util.List;

import net.minecraft.src.ItemStack;

public interface PC_ICraftingToolDisplayer {

	public String getCraftingToolModule();
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList);
	
}
