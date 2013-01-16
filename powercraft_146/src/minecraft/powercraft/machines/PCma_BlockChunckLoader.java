package powercraft.machines;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_Block;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCma_BlockChunckLoader extends PC_Block {

	public PCma_BlockChunckLoader(int id) {
		super(id, textureIndex, Material.iron);
		
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "ChunckLoader";
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}
		}
		return null;
	}

}
