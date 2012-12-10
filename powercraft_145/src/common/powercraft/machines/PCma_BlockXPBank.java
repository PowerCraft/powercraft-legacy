package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_IBlockRenderer;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;

public class PCma_BlockXPBank extends PC_Block implements PC_IBlockRenderer, PC_ICraftingToolDisplayer
{
    public PCma_BlockXPBank(int id)
    {
        super(id, Material.ground);
        setStepSound(Block.soundPowderFootstep);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(6.0F);
        setResistance(100.0F);
        setLightValue(0.5F);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public String getDefaultName()
    {
        return "XP Bank";
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new PCma_TileEntityXPBank();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return true;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int s, int m)
    {
        return 24;
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
        ItemStack ihold = entityplayer.getCurrentEquippedItem();

        if (ihold != null)
        {
            if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID)
            {
                return false;
            }
        }

        PC_Utils.openGres("XPBank", entityplayer, i, j, k);
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
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return ((PCma_TileEntityXPBank) iblockaccess.getBlockTileEntity(i, j, k)).getXP() > 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(IBlockAccess world, int x, int y, int z, int r)
    {
        return isPoweringTo(world, x, y, z, r);
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
    public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer)
    {
        PC_Renderer.renderInvBox(renderer, block, metadata);
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        Block.obsidian.setBlockBounds(0.0F, 0.3F, 0.0F, 1.0F, 0.8F, 1.0F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 0.2F, 0.3F, 0.2F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        Block.obsidian.setBlockBounds(0.8F, 0.0F, 0.0F, 1.0F, 0.3F, 0.2F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.8F, 0.2F, 0.3F, 1.0F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        Block.obsidian.setBlockBounds(0.8F, 0.0F, 0.8F, 1.0F, 0.3F, 1.0F);
        PC_Renderer.renderInvBox(renderer, Block.obsidian, 0);
        Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer)
    {
        PC_Renderer.swapTerrain(block);
        int xp = ((PCma_TileEntityXPBank) world.getBlockTileEntity(x, y, z)).getXP();
        setBlockBounds(0.15F, 0.29F - 0.2F * calculateHeightMultiplier(xp), 0.15F, 0.85F, 0.71F + 0.2F * calculateHeightMultiplier(xp), 0.85F);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        Block.obsidian.setBlockBounds(0.0F, 0.3F, 0.0F, 1.0F, 0.7F, 1.0F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 0.15F, 0.3F, 0.15F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        Block.obsidian.setBlockBounds(0.85F, 0.0F, 0.0F, 1.0F, 0.3F, 0.15F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.85F, 0.15F, 0.3F, 1.0F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        Block.obsidian.setBlockBounds(0.85F, 0.0F, 0.85F, 1.0F, 0.3F, 1.0F);
        PC_Renderer.renderStandardBlock(renderer, Block.obsidian, x, y, z);
        Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public String getCraftingToolModule()
    {
        return mod_PowerCraftMachines.getInstance().getNameWithoutPowerCraft();
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this));
        return arrayList;
    }
    
    @Override
   	public List<String> getBlockFlags(World world, PC_CoordI pos, List<String> list) {

   		list.add(PC_Utils.NO_HARVEST);
   		list.add(PC_Utils.NO_PICKUP);
   		list.add(PC_Utils.HARVEST_STOP);
   		return list;
   	}

   	@Override
   	public List<String> getItemFlags(ItemStack stack, List<String> list) {
   		list.add(PC_Utils.NO_BUILD);
   		return list;
   	}
    
}
