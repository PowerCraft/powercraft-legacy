package powercraft.weasel;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;

import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

public class PCws_ItemTransistor extends PC_Item {

	public PCws_ItemTransistor(int id) {
		super(id, 4);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Transistor", null));
			return names;
		}
		return null;
	}

}
