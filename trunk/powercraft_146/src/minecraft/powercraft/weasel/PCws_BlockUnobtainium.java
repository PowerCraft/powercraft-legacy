package powercraft.weasel;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_Block;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCws_BlockUnobtainium extends PC_Block {

	public PCws_BlockUnobtainium(int id) {
		super(id, 2, Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Unobtainium";
		}
		return null;
	}

}
