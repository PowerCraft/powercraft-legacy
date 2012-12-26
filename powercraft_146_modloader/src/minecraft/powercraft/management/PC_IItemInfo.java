package powercraft.management;

import java.util.List;

import net.minecraft.src.ItemStack;

public interface PC_IItemInfo {

	public PC_IModule getModule();
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList);
	
}
