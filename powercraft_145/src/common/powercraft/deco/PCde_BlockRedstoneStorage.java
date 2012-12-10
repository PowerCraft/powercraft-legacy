package powercraft.deco;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCde_BlockRedstoneStorage extends PC_Block implements PC_IItemInfo {
	private boolean wiresProvidePower = true;
	private Set blocksNeedingUpdate = new HashSet();
	
	public PCde_BlockRedstoneStorage() {
		super(129, Material.rock);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	@Override
	public Object msg(World world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
			list.add(PC_Utils.PASSIVE);
			list.add(PC_Utils.HARVEST_STOP);
			return list;
		}
		case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Redstone Storage";
		}
		return null;
	}
	
}
