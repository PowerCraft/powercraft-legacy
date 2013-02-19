package powercraft.deco;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCde_BlockChimney extends PC_Block {

	public PCde_BlockChimney(int id) {
		super(id, 0, Material.rock, false);
		setHardness(1.5F);
		setResistance(50.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCde_TileEntityChimney();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		final float px = 0.0625F;
		float w = px * 3;

		Block bl = null;
		if (metadata == 0) bl = Block.cobblestone;
		if (metadata == 1) bl = Block.brick;
		if (metadata == 2) bl = Block.stoneBrick;
		if (bl == null) return;

		ValueWriting.setBlockBounds(bl, 0, 0, 0, 1, 1, w);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		ValueWriting.setBlockBounds(bl, 1 - w, 0, w, 1, 1, 1 - w);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		ValueWriting.setBlockBounds(bl, 0, 0, 1 - w, 1, 1, 1);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		ValueWriting.setBlockBounds(bl, 0, 0, w, w, 1, 1 - w);
		PC_Renderer.renderInvBox(renderer, bl, 0);
		ValueWriting.setBlockBounds(bl, 0, 0, 0, 1, 1, 1);
	}

	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		
		final float px = 0.0625F;
		float w = px * 3;

		int metadata = GameInfo.getMD(world, x, y, z);
		
		Block bl = null;
		if (metadata == 0) bl = Block.cobblestone;
		if (metadata == 1) bl = Block.brick;
		if (metadata == 2) bl = Block.stoneBrick;
		if (bl == null) return;

		ValueWriting.setBlockBounds(bl, 0, 0, 0, 1, 1, w);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);

		ValueWriting.setBlockBounds(bl, 1 - w, 0, w, 1, 1, 1 - w);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);

		ValueWriting.setBlockBounds(bl, 0, 0, 1 - w, 1, 1, 1);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);

		ValueWriting.setBlockBounds(bl, 0, 0, w, w, 1, 1 - w);
		PC_Renderer.renderStandardBlock(renderer, bl, x, y, z);
		
		ValueWriting.setBlockBounds(bl, 0, 0, 0, 1, 1, 1);
		
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_Utils.MSG_RENDER_WORLD_BLOCK:
			renderWorldBlock(world, pos.x, pos.y, pos.z, (Block)obj[0], (Integer)obj[1], obj[2]);
			break;
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
			list.add(PC_Utils.PASSIVE);
			list.add(PC_Utils.BEAMTRACER_STOP);
			return list;
		}
		case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}
		default:
			return null;
		}
		return true;
	}
	
}
