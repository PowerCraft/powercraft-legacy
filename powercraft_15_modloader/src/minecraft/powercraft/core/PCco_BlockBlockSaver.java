package powercraft.core;

import java.util.List;

import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;

@PC_BlockInfo(name="Block Saver", itemBlock=PCco_ItemBlockBlockSaver.class)
public class PCco_BlockBlockSaver extends PC_Block implements PC_IItemInfo {

	public PCco_BlockBlockSaver(int id) {
		super(id, Material.wood);
	}

	public int getRenderType(){
        return 22;
    }

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		return arrayList;
	}

	@Override
	public boolean showInCraftingTool() {
		return false;
	}
	
}
