package powercraft.weasel;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

public class PCws_ItemBlockWeasel extends PC_ItemBlock {

	public PCws_ItemBlockWeasel(int id) {
		super(id);
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		for(Entry<Integer, PCws_WeaselPluginInfo>e:PCws_WeaselManager.getPluginInfoMap().entrySet()){
			arrayList.add(new ItemStack(this, 1, e.getKey()));
		}
		return arrayList;
	}
	
	@Override
	public String getItemNameIS(ItemStack itemStack) {
		return getItemName() + "." + PCws_WeaselManager.getPluginInfo(itemStack.getItemDamage()).getKey();
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			for(Entry<Integer, PCws_WeaselPluginInfo>e:PCws_WeaselManager.getPluginInfoMap().entrySet()){
				names.add(new PC_Struct3<String, String, String[]>(getItemName() + "." + e.getValue().getKey(), e.getValue().getDefaultName(), null));
			}
			return names;
		default:
			return null;
		}
		//return true;
	}

}
