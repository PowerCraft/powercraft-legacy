package powercraft.light;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Utils;

@PC_BlockInfo(name="Lightning Conductor", itemBlock=PCli_ItemBlockLightningConductor.class, tileEntity=PCli_TileEntityLightningConductor.class)
public class PCli_BlockLightningConductor extends PC_Block
{
    public PCli_BlockLightningConductor(int id)
    {
        super(id, Material.rock, "lightingconductor");
        setHardness(1.5F);
        setResistance(50.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int metadata)
    {
        if (metadata == 0)
        {
        	PC_Utils.setBID(world, x, y + 1, z, 0, 1);
        }
        else
        {
        	PC_Utils.setBID(world, x, y - 1, z, 0, 0);
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

    @Override
    public boolean renderInventoryBlock(int metadata, Object renderer)
    {
        float f = 1.0f / 14.0f / 2.0f;
        PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);
        setBlockBounds(0.5f - f * 5, 0.0f, 0.5f - f * 5, 0.5f + f * 5, 0.33f, 0.5f + f * 5);
        PC_Renderer.renderInvBox(renderer, this, metadata);
        setBlockBounds(0.5f - f * 3, 0.33f, 0.5f - f * 3, 0.5f + f * 3, 0.66f, 0.5f + f * 3);
        PC_Renderer.renderInvBox(renderer, this, metadata);
        setBlockBounds(0.5f - f * 1, 0.66f, 0.5f - f * 1, 0.5f + f * 1, 1.0f, 0.5f + f * 1);
        PC_Renderer.renderInvBox(renderer, this, metadata);
        setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        return true;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer)
    {
        float f = 1.0f / 14.0f;
        PC_Renderer.tessellatorDraw();
        PC_Renderer.tessellatorStartDrawingQuads();
        PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);

        if (PC_Utils.getMD(world, x, y, z) == 0)
        {
        	setBlockBounds(0.5f - f * 5, 0.0f, 0.5f - f * 5, 0.5f + f * 5, 0.66f, 0.5f + f * 5);
            PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
            setBlockBounds(0.5f - f * 3, 0.66f, 0.5f - f * 3, 0.5f + f * 3, 1.0f, 0.5f + f * 3);
            PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
        }
        else
        {
        	setBlockBounds(0.5f - f * 3, 0.0f, 0.5f - f * 3, 0.5f + f * 3, 0.33f, 0.5f + f * 3);
            PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
            setBlockBounds(0.5f - f * 1, 0.33f, 0.5f - f * 1, 0.5f + f * 1, 1.0f, 0.5f + f * 1);
            PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
        }

        setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        PC_Renderer.tessellatorDraw();
        PC_Renderer.tessellatorStartDrawingQuads();
        return true;
    }
    
}
