package powercraft.weasel;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.PC_OreDictionary;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCws_ItemSilicon extends PC_Item {

	public PCws_ItemSilicon(int id) {
		super(id, 3);
		setCreativeTab(CreativeTabs.tabMaterials);
		PC_OreDictionary.register("ingotSilicon", new PC_ItemStack(this));
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Ingot Silicon"));
			return names;
		}
		return null;
	}

}
