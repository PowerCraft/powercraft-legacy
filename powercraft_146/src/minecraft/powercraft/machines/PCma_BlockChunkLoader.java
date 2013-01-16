package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_I3DRecipeHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;
import powercraft.management.PC_Utils.ValueWriting;

public class PCma_BlockChunkLoader extends PC_Block implements PC_I3DRecipeHandler {

	public PCma_BlockChunkLoader(int id) {
		super(id, 49, Material.glass, false);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCma_TileEntityChunkLoader();
	}

	public int quantityDropped(Random par1Random){
        return 0;
    }
	
	public boolean isOpaqueCube(){
        return false;
    }
	
	public boolean renderAsNormalBlock(){
        return false;
    }
	
	@Override
	public boolean foundStructAt(World world, PC_Struct2<PC_VecI, Integer> structStart) {
		PC_VecI mid = structStart.a.offset(1, 0, 1);
		if(!world.canBlockSeeTheSky(mid.x, mid.y, mid.z))
			return false;
		ValueWriting.setBID(world, mid, blockID, 0);
		return true;
	}
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Chunk Loader";
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
		}case PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL:
			return true;
		}
		return null;
	}

}
