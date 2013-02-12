package powercraft.hologram;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import powercraft.management.PC_RenderBlocks;
import powercraft.management.PC_Utils.GameInfo;

public class PChg_HologramRenderBlocks extends PC_RenderBlocks {

	public PChg_HologramRenderBlocks(IBlockAccess blockAccess){
		super(blockAccess);
	}

	@Override
	public boolean shouldSideBeRendered(Block block, int x, int y, int z, int dir){
		if(block!=GameInfo.getBlock(blockAccess, x, y, z)){
			return true;
		}
		return block.shouldSideBeRendered(blockAccess, x, y, z, dir);
	}
	
}
