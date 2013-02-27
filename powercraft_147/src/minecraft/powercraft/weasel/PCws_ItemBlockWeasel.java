package powercraft.weasel;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import powercraft.management.block.PC_ItemBlock;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_MSGRegistry;

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
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			for(Entry<Integer, PCws_WeaselPluginInfo>e:PCws_WeaselManager.getPluginInfoMap().entrySet()){
				names.add(new LangEntry(getItemName() + "." + e.getValue().getKey(), e.getValue().getDefaultName()));
			}
			return names;
		default:
			return null;
		}
		//return true;
	}

}
