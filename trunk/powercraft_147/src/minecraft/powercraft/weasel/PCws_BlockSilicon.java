package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_VecI;
import powercraft.management.block.PC_BlockOre;
import powercraft.management.registry.PC_MSGRegistry;

public class PCws_BlockSilicon extends PC_BlockOre {
	
	public PCws_BlockSilicon(int id) {
		super(id, "Silicon", 1, 10, 10, 30, 2, Material.rock);
		setHardness(0.7F);
		setResistance(10.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Silicon";
		}
		return null;
	}

}
