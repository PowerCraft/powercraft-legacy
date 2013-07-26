package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCws_ItemTransistor extends PC_Item {

	public PCws_ItemTransistor(int id) {
		super(id, "transistor");
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Transistor"));
		return names;
	}

	
	
}
