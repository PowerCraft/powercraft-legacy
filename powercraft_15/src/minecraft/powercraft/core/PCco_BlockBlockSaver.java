package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.utils.PC_VecI;

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
