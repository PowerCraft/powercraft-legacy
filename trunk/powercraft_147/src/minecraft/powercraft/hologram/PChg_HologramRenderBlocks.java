package powercraft.hologram;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_Block;
import powercraft.management.PC_RenderBlocks;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.registry.PC_MSGRegistry;

public class PChg_HologramRenderBlocks extends PC_RenderBlocks {

	public PChg_HologramRenderBlocks(IBlockAccess blockAccess){
		super(blockAccess);
	}

	@Override
	public boolean shouldSideBeRendered(Block block, int x, int y, int z, int dir){
		if(block!=GameInfo.getBlock(blockAccess, x, y, z)){
			if (block instanceof PC_Block && dir == 1 && block.getRenderType() == PC_Renderer.getRendererID(true) && ((PC_Block)block).msg(PC_MSGRegistry.MSG_ROTATION, GameInfo.getMD(blockAccess, x, y, z))!=null)
				return false;
			return true;
		}
		return block.shouldSideBeRendered(blockAccess, x, y, z, dir);
	}
	
}
