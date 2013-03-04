package powercraft.management.item;

import java.util.List;

import net.minecraft.src.ItemStack;
import powercraft.launcher.PC_ModuleObject;
import powercraft.management.PC_IModule;

public interface PC_IItemInfo {

	public PC_ModuleObject getModule();
	
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList);
	
	public boolean showInCraftingTool();
	
}
