package powercraft.management;

import powercraft.management.PC_Utils.ModuleInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

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
		return new ItemStack(ModuleInfo.getPCItemByName("PCco_ItemActivator"));
	}
	
}
