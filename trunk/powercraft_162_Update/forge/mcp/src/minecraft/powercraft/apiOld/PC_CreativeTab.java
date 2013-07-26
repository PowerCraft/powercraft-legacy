package powercraft.apiOld;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import powercraft.api.registry.PC_ItemRegistry;

public class PC_CreativeTab extends CreativeTabs {

	public PC_CreativeTab() {
		super("Power Craft");
	}

	@Override
	public String getTranslatedTabLabel() {
		return "Power Craft";
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(
				PC_ItemRegistry.getPCItemByName("PCco_ItemActivator"));
	}

}
