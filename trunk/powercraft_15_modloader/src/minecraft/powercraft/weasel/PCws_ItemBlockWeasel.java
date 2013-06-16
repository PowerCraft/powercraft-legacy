package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.ItemStack;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;

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
	public String getUnlocalizedName(ItemStack itemStack) {
		return getUnlocalizedName() + "." + PCws_WeaselManager.getPluginInfo(itemStack.getItemDamage()).getKey();
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		for(Entry<Integer, PCws_WeaselPluginInfo>e:PCws_WeaselManager.getPluginInfoMap().entrySet()){
			names.add(new LangEntry(getUnlocalizedName() + "." + e.getValue().getKey(), e.getValue().getDefaultName()));
		}
		return names;
	}
	
	

}
