package powercraft.hologram;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_RenderBlocks;
import powercraft.api.renderer.PC_Renderer;

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
