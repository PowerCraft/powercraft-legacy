package powercraft.light;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_IBlockRenderer;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class PCli_BlockLightningConductor extends PC_Block implements PC_IBlockRenderer {

	public PCli_BlockLightningConductor(int id) {
		super(id, 22, Material.rock);
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
	public TileEntity createNewTileEntity(World world, int metadata) {
		if(metadata==1)
			return new PCli_TileEntityLightningConductor();
		return null;
	}

	@Override
	public boolean canBeHarvest() {
		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int metadata){
		if(metadata==0){
			world.setBlockWithNotify(x, y+1, z, 0);
		}else{
			world.setBlockWithNotify(x, y-1, z, 0);
		}
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		float f = 1.0f/14.0f/2.0f;
		PC_Renderer.swapTerrain(block);
		PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);
		block.setBlockBounds(0.5f-f*5, 0.0f, 0.5f-f*5, 0.5f+f*5, 0.33f, 0.5f+f*5);
		PC_Renderer.renderInvBox(renderer, block, metadata);
		block.setBlockBounds(0.5f-f*3, 0.33f, 0.5f-f*3, 0.5f+f*3, 0.66f, 0.5f+f*3);
		PC_Renderer.renderInvBox(renderer, block, metadata);
		block.setBlockBounds(0.5f-f*1, 0.66f, 0.5f-f*1, 0.5f+f*1, 1.0f, 0.5f+f*1);
		PC_Renderer.renderInvBox(renderer, block, metadata);
		block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		float f = 1.0f/14.0f;
		PC_Renderer.tessellatorDraw();
		PC_Renderer.swapTerrain(block);
		PC_Renderer.tessellatorStartDrawingQuads();
		PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);
		if(PC_Utils.getMD(world, x, y, z)==0){
			block.setBlockBounds(0.5f-f*5, 0.0f, 0.5f-f*5, 0.5f+f*5, 0.66f, 0.5f+f*5);
			PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
			block.setBlockBounds(0.5f-f*3, 0.66f, 0.5f-f*3, 0.5f+f*3, 1.0f, 0.5f+f*3);
			PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		}else{
			block.setBlockBounds(0.5f-f*3, 0.0f, 0.5f-f*3, 0.5f+f*3, 0.33f, 0.5f+f*3);
			PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
			block.setBlockBounds(0.5f-f*1, 0.33f, 0.5f-f*1, 0.5f+f*1, 1.0f, 0.5f+f*1);
			PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		}
		block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		PC_Renderer.tessellatorDraw();
		PC_Renderer.resetTerrain(true);
		PC_Renderer.tessellatorStartDrawingQuads();
	}
	
}
