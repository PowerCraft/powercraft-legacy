package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.block.PC_Block;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.renderer.PC_Renderer;

@PC_BlockInfo(itemBlock=PCli_ItemBlockLightningConductor.class, tileEntity=PCli_TileEntityLightningConductor.class)
public class PCli_BlockLightningConductor extends PC_Block
{
    public PCli_BlockLightningConductor(int id)
    {
        super(id, 22, Material.rock);
        setHardness(1.5F);
        setResistance(50.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public TileEntity newTileEntity(World world, int metadata) {
        if (metadata == 1)
        {
            return new PCli_TileEntityLightningConductor();
        }

        return null;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int metadata)
    {
        if (metadata == 0)
        {
            world.setBlockWithNotify(x, y + 1, z, 0);
        }
        else
        {
            world.setBlockWithNotify(x, y - 1, z, 0);
        }

        super.breakBlock(world, x, y, z, id, metadata);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }

    public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer)
    {
        float f = 1.0f / 14.0f / 2.0f;
        PC_Renderer.swapTerrain(block);
        PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);
        ValueWriting.setBlockBounds(block, 0.5f - f * 5, 0.0f, 0.5f - f * 5, 0.5f + f * 5, 0.33f, 0.5f + f * 5);
        PC_Renderer.renderInvBox(renderer, block, metadata);
        ValueWriting.setBlockBounds(block, 0.5f - f * 3, 0.33f, 0.5f - f * 3, 0.5f + f * 3, 0.66f, 0.5f + f * 3);
        PC_Renderer.renderInvBox(renderer, block, metadata);
        ValueWriting.setBlockBounds(block, 0.5f - f * 1, 0.66f, 0.5f - f * 1, 0.5f + f * 1, 1.0f, 0.5f + f * 1);
        PC_Renderer.renderInvBox(renderer, block, metadata);
        ValueWriting.setBlockBounds(block, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        PC_Renderer.resetTerrain(true);
    }

    public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer)
    {
        float f = 1.0f / 14.0f;
        PC_Renderer.tessellatorDraw();
        PC_Renderer.swapTerrain(block);
        PC_Renderer.tessellatorStartDrawingQuads();
        PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);

        if (GameInfo.getMD(world, x, y, z) == 0)
        {
        	ValueWriting.setBlockBounds(block, 0.5f - f * 5, 0.0f, 0.5f - f * 5, 0.5f + f * 5, 0.66f, 0.5f + f * 5);
            PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
            ValueWriting.setBlockBounds(block, 0.5f - f * 3, 0.66f, 0.5f - f * 3, 0.5f + f * 3, 1.0f, 0.5f + f * 3);
            PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        }
        else
        {
        	ValueWriting.setBlockBounds(block, 0.5f - f * 3, 0.0f, 0.5f - f * 3, 0.5f + f * 3, 0.33f, 0.5f + f * 3);
            PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
            ValueWriting.setBlockBounds(block, 0.5f - f * 1, 0.33f, 0.5f - f * 1, 0.5f + f * 1, 1.0f, 0.5f + f * 1);
            PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        }

        ValueWriting.setBlockBounds(block, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        PC_Renderer.tessellatorDraw();
        PC_Renderer.resetTerrain(true);
        PC_Renderer.tessellatorStartDrawingQuads();
    }
    
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_MSGRegistry.MSG_RENDER_WORLD_BLOCK:
			renderWorldBlock(world, pos.x, pos.y, pos.z, (Block)obj[0], (Integer)obj[1], obj[2]);
			break;
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
			list.add(PC_Utils.HARVEST_STOP);
			list.add(PC_Utils.BEAMTRACER_STOP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}default:
			return null;
		}
		return true;
	}
    
}
