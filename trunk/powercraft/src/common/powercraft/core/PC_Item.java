package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public abstract class PC_Item extends Item implements PC_ICraftingToolDisplayer {

	protected PC_Item(int id){
		super(id);
	}

	public abstract String[] getDefaultNames();

	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	public void getSubItems(int index, CreativeTabs creativeTab, List list){
		list.addAll(getItemStacks(new ArrayList<ItemStack>()));
	}
	
}
