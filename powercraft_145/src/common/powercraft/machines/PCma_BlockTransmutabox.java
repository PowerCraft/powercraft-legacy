package powercraft.machines;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_VecI;

public class PCma_BlockTransmutabox extends PC_Block implements PC_IItemInfo
{
    public PCma_BlockTransmutabox()
    {
        super(0, Material.rock);
        setHardness(1.5F);
        setResistance(50.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void receivePower(World world, int x, int y, int z, float power)
    {
        PCma_TileEntityTransmutabox te = PC_Utils.getTE(world, x, y, z, blockID);

        if (te != null && power > 200)
        {
            te.change((int)(power - 100) / 10);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PCma_TileEntityTransmutabox();
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
    public boolean onBlockActivated(World world, int x, int y,
            int z, EntityPlayer player, int par6, float par7,
            float par8, float par9)
    {
        PC_Utils.openGres("Transmutabox", player, x, y, z);
        return true;
    }

    public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer)
    {
        PC_Renderer.swapTerrain(block);
        block.setBlockBounds(0.1f, 0.1f, 0.1f, 0.9f, 0.9f, 0.9f);
        PC_Renderer.renderInvBox(renderer, block, metadata);
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.2f, 0.2f, 0.2f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0.8f, 0.0f, 0.0f, 1.0f, 0.2f, 0.2f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0.8f, 0.8f, 0.0f, 1.0f, 1.0f, 0.2f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0.8f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0.0f, 0.8f, 0.8f, 0.2f, 1.0f, 1.0f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0.0f, 0.0f, 0.8f, 0.2f, 0.2f, 1.0f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0.0f, 0.8f, 0.0f, 0.2f, 1.0f, 0.2f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0.8f, 0.0f, 0.8f, 1.0f, 0.2f, 1.0f);
        PC_Renderer.renderInvBoxWithTexture(renderer, block, 1);
        block.setBlockBounds(0, 0, 0, 1, 1, 1);
        PC_Renderer.resetTerrain(true);
    }

    public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer)
    {
        PC_Renderer.tessellatorDraw();
        PC_Renderer.swapTerrain(block);
        PC_Renderer.tessellatorStartDrawingQuads();
        block.setBlockBounds(0.1f, 0.1f, 0.1f, 0.9f, 0.9f, 0.9f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.2f, 0.2f, 0.2f);
        block.blockIndexInTexture = 1;
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.8f, 0.0f, 0.0f, 1.0f, 0.2f, 0.2f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.8f, 0.8f, 0.0f, 1.0f, 1.0f, 0.2f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.8f, 0.8f, 0.8f, 1.0f, 1.0f, 1.0f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.0f, 0.8f, 0.8f, 0.2f, 1.0f, 1.0f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.8f, 0.2f, 0.2f, 1.0f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.0f, 0.8f, 0.0f, 0.2f, 1.0f, 0.2f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.8f, 0.0f, 0.8f, 1.0f, 0.2f, 1.0f);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        block.blockIndexInTexture = 0;
        PC_Renderer.tessellatorDraw();
        PC_Renderer.resetTerrain(true);
        PC_Renderer.tessellatorStartDrawingQuads();
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }

	@Override
	public Object msg(World world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Transmutabox";
		case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[1];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}case PC_Utils.MSG_RENDER_INVENTORY_BLOCK:{
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			return true;
		}case PC_Utils.MSG_RENDER_WORLD_BLOCK:{
			renderWorldBlock((IBlockAccess)obj[0], (Integer)obj[1], (Integer)obj[2], (Integer)obj[3], (Block)obj[4], (Integer)obj[5], obj[6]);
			return true;
		}case PC_Utils.MSG_CAN_RECIVE_POWER:{
			return true;
		}case PC_Utils.MSG_RECIVE_POWER:{
			receivePower(world, (Integer)obj[0], (Integer)obj[1], (Integer)obj[2], (Float)obj[3]);
		}
		}
		return null;
	}
    
}
