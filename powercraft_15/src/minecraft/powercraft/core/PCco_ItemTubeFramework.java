package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.structure.PC_ItemStructure;
import powercraft.api.structure.PC_StructureType;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PCco_ItemTubeFramework extends PC_ItemStructure {

	public PCco_ItemTubeFramework(int id) {
		super(id, "Framework", "Framework");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public PC_StructureType getStructureType() {
		return PC_StructureType.FRAMEWORK;
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Framework"));
		return names;
	}

	@Override
	public boolean canStructureConnectTo(IBlockAccess world, int x, int y, int z, ItemStack tube, PC_Direction dir) {
		PC_VecI offset = dir.getOffset();
		x += offset.x;
		y += offset.y;
		z += offset.z;
		Block block = PC_Utils.getBlock(world, x, y, z);
		if(block!=null)
			return block.isOpaqueCube() || block == PCco_App.tube ;
		return false;
	}

	
	
}
