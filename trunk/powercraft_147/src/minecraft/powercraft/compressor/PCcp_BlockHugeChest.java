package powercraft.compressor;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_Block;
import powercraft.management.PC_VecI;

public class PCcp_BlockHugeChest extends PC_Block {

	public PCcp_BlockHugeChest(int id) {
		super(id, 12, Material.ground);
		
	}
	
	

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
