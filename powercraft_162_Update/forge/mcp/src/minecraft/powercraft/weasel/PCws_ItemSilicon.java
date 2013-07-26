package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.PC_OreDictionary;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCws_ItemSilicon extends PC_Item {

	public PCws_ItemSilicon(int id) {
		super(id, "ingotsilicon");
		setCreativeTab(CreativeTabs.tabMaterials);
		PC_OreDictionary.register("ingotSilicon", new PC_ItemStack(this));
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Ingot Silicon"));
		return names;
	}

}
