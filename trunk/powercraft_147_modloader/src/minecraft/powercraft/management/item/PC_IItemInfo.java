package powercraft.management.item;

import java.util.List;

import net.minecraft.src.ItemStack;
import powercraft.management.PC_IModule;

public interface PC_IItemInfo {

	public PC_IModule getModule();
	
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList);
	
	public boolean showInCraftingTool();
	
}
