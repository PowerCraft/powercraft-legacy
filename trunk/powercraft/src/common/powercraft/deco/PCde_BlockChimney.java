package powercraft.deco;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_IBlockRenderer;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;

public class PCde_BlockChimney extends PC_Block implements PC_IBlockRenderer {

	public PCde_BlockChimney(int id) {
		super(id, 0, Material.rock, false);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getDefaultName() {
		return null;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCde_TileEntityChimney();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		final float px = 0.0625F;
		float w = px * 3;

		Block bl = null;
		if (metadata == 0) bl = Block.cobblestone;
		if (metadata == 1) bl = Block.brick;
		if (metadata == 2) bl = Block.stoneBrick;
		if (bl == null) return;

		bl.setBlockBounds(0, 0, 0, 1, 1, w);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		bl.setBlockBounds(1 - w, 0, w, 1, 1, 1 - w);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		bl.setBlockBounds(0, 0, 1 - w, 1, 1, 1);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		bl.setBlockBounds(0, 0, w, w, 1, 1 - w);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		bl.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		
		final float px = 0.0625F;
		float w = px * 3;

		int metadata = PC_Utils.getMD(world, x, y, z);
		
		Block bl = null;
		if (metadata == 0) bl = Block.cobblestone;
		if (metadata == 1) bl = Block.brick;
		if (metadata == 2) bl = Block.stoneBrick;
		if (bl == null) return;

		bl.setBlockBounds(0, 0, 0, 1, 1, w);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);

		bl.setBlockBounds(1 - w, 0, w, 1, 1, 1 - w);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);

		bl.setBlockBounds(0, 0, 1 - w, 1, 1, 1);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);

		bl.setBlockBounds(0, 0, w, w, 1, 1 - w);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);
		
		bl.setBlockBounds(0, 0, 0, 1, 1, 1);
		
	}
	
}
