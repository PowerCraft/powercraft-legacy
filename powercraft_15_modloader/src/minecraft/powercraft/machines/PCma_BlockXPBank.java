package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Utils;

@PC_BlockInfo(name="XP Bank", tileEntity=PCma_TileEntityXPBank.class)
public class PCma_BlockXPBank extends PC_Block implements PC_IItemInfo
{
    public PCma_BlockXPBank(int id)
    {
        super(id, Material.ground, "xpbank");
        setStepSound(Block.soundPowderFootstep);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(6.0F);
        setResistance(100.0F);
        setLightValue(0.5F);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    private float calculateHeightMultiplier(int xp)
    {
        return Math.min(xp / 500F, 1F);
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.9F, 0.8F);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {

        PC_GresRegistry.openGres("XPBank", entityplayer, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
        return true;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 1;
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public void onBlockHarvested(World world, int i, int j, int k, int par5, EntityPlayer player)
    {
        try
        {
            ((PCma_TileEntityXPBank) world.getBlockTileEntity(i, j, k)).withdrawXP(player);
        }
        catch (NullPointerException npe) {}
    }

    @Override
    public boolean renderInventoryBlock(int metadata, Object renderer)
    {
        PC_Renderer.renderInvBox(renderer, this, metadata);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.3F, 0.0F, 1.0F, 0.8F, 1.0F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.0F, 0.0F, 0.2F, 0.3F, 0.2F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        PC_Utils.setBlockBounds(Block.obsidian, 0.8F, 0.0F, 0.0F, 1.0F, 0.3F, 0.2F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.0F, 0.8F, 0.2F, 0.3F, 1.0F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        PC_Utils.setBlockBounds(Block.obsidian, 0.8F, 0.0F, 0.8F, 1.0F, 0.3F, 1.0F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer)
    {
        int xp = ((PCma_TileEntityXPBank) world.getBlockTileEntity(x, y, z)).getXP();
        setBlockBounds(0.15F, 0.29F - 0.2F * calculateHeightMultiplier(xp), 0.15F, 0.85F, 0.71F + 0.2F * calculateHeightMultiplier(xp), 0.85F);
        PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.3F, 0.0F, 1.0F, 0.7F, 1.0F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.0F, 0.0F, 0.15F, 0.3F, 0.15F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        PC_Utils.setBlockBounds(Block.obsidian, 0.85F, 0.0F, 0.0F, 1.0F, 0.3F, 0.15F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.0F, 0.85F, 0.15F, 0.3F, 1.0F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        PC_Utils.setBlockBounds(Block.obsidian, 0.85F, 0.0F, 0.85F, 1.0F, 0.3F, 1.0F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        PC_Utils.setBlockBounds(Block.obsidian, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }
   	
}
