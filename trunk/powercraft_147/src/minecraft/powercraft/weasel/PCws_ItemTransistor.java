package powercraft.weasel;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCws_ItemTransistor extends PC_Item {

	public PCws_ItemTransistor(int id) {
		super(id, 4);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Transistor"));
			return names;
		}
		return null;
	}

}
