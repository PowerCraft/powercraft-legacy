package powercraft.core;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;

@PC_BlockInfo(itemBlock=PCco_ItemBlockBlockSaver.class)
public class PCco_BlockBlockSaver extends PC_Block {

	public PCco_BlockBlockSaver(int id) {
		super(id, Material.wood, "blocksaver");
	}

	public int getRenderType(){
        return 22;
    }
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		return null;
	}

}
