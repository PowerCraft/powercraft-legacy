package powercraft.management.item;

import java.util.List;

import powercraft.management.PC_IModule;

import net.minecraft.item.ItemStack;

public interface PC_IItemInfo {

	public PC_IModule getModule();
	
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList);
	
	public boolean showInCraftingTool();
	
}
