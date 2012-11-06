package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public abstract class PC_Item extends Item implements PC_ICraftingToolDisplayer {

	private String craftingToolModule;
	private boolean canSetTextureFile = true;
	
	protected PC_Item(int id){
		super(id);
	}

	public PC_Item(int id, boolean canSetTextureFile) {
		super(id);
		this.canSetTextureFile = canSetTextureFile;
	}

	public abstract String[] getDefaultNames();

	public String getCraftingToolModule() {
		return craftingToolModule;
	}

	public void setCraftingToolModule(String module){
		craftingToolModule = module;
	}
	
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	public void getSubItems(int index, CreativeTabs creativeTab, List list){
		list.addAll(getItemStacks(new ArrayList<ItemStack>()));
	}

	@Override
	public void setTextureFile(String texture) {
		if(canSetTextureFile)
			super.setTextureFile(texture);
	}
	
	
	
}
