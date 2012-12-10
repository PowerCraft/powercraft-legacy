package powercraft.deco;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;

public class PCde_BlockChimney extends PC_Block {

	public PCde_BlockChimney() {
		super(0, Material.rock, false);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
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
	
	@Override
	public List<String> getBlockFlags(World world, PC_VecI pos, List<String> list) {

		list.add(PC_Utils.NO_HARVEST);
		list.add(PC_Utils.NO_PICKUP);
		list.add(PC_Utils.PASSIVE);
		list.add(PC_Utils.BEAMTRACER_STOP);
		return list;
	}

	@Override
	public List<String> getItemFlags(ItemStack stack, List<String> list) {
		list.add(PC_Utils.NO_BUILD);
		return list;
	}
	
}
