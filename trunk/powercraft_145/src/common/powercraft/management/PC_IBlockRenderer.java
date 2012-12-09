package powercraft.management;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;

public interface PC_IBlockRenderer {

	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer);

	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer);

}
