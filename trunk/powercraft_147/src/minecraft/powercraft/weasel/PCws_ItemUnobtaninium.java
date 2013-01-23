package powercraft.weasel;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

public class PCws_ItemUnobtaninium extends PC_Item {

	public PCws_ItemUnobtaninium(int id) {
		super(id, 3);
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Ingot Unobtaninium", null));
			return names;
		}
		return null;
	}

}
