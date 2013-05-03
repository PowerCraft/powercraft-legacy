package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Laser", tileEntity=PCli_TileEntityLaser.class, canPlacedRotated=true)
public class PCli_BlockLaser extends PC_Block implements PC_IItemInfo
{
    public PCli_BlockLaser(int id)
    {
        super(id, Material.ground, "laser");
        setStepSound(Block.soundMetalFootstep);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(0.7F);
        setResistance(10.0F);
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

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(i, j, k, (double) i + 1, (double) j + 0.7F, (double) k + 1);
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.3F, 0.3F, 0.2F, 0.7F, 0.7F, 0.8F);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        PCli_TileEntityLaser te = PC_Utils.getTE(world, i, j, k);
        
        if (te != null && te.getItemStack()!=null){
        	ItemStack ihold = entityplayer.getCurrentEquippedItem();
        	if (ihold == null || ihold.getItem().itemID == Item.stick.itemID) {
        		if(!PC_Utils.isCreative(entityplayer)){
        			PC_Utils.dropItemStack(world, i, j, k, te.getItemStack().toItemStack());
	        	}
        		te.setItemStack(null);
		    }
        }
        return false;
    }

    @Override
    public boolean canProvidePower(){
        return true;
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player)
    {
        if (!PC_Utils.isCreative(player))
        {
            PCli_TileEntityLaser te = PC_Utils.getTE(world, x, y, z);

            if (te != null && te.getItemStack()!=null)
            {
                PC_Utils.dropItemStack(world, x, y, z, te.getItemStack().toItemStack());
            }
        }
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int par5, int par6)
    {
        PC_Utils.hugeUpdate(world, i, j, k);
        super.breakBlock(world, i, j, k, par5, par6);
    }
    
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
		PCli_TileEntityLaser te = PC_Utils.getTE(world, x, y, z);
	        
        if(te!=null){
        	te.setKiller(PC_BlockRegistry.isBlock(world, new PC_VecI(x, y-1, z), "PCma_BlockRoaster"));
        	boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z);
        	te.setPowered(powered);
        }
        
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	public boolean renderInventoryBlock(int metadata, Object renderer) {
		PC_Renderer.renderInvBox(renderer, this, metadata);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// cobble body
		
		PC_Utils.setBlockBounds(Block.cobblestone, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		PC_Renderer.renderInvBox(renderer, Block.cobblestone, 0);
		PC_Utils.setBlockBounds(Block.cobblestone, 0.4F, 0.2F, 0.4F, 0.6F, 0.3F, 0.6F);
		PC_Renderer.renderInvBox(renderer, Block.cobblestone, 0);
		// reset
		PC_Utils.setBlockBounds(Block.cobblestone, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		
		return true;
	}

	
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}
	
}
