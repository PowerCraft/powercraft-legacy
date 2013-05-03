package powercraft.hologram;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Hologram", itemBlock=PChg_ItemBlockHologramBlock.class, tileEntity=PChg_TileEntityHologramBlock.class, canPlacedRotated=true)
public class PChg_BlockHologramBlock extends PC_Block {

	public PChg_BlockHologramBlock(int id) {
		super(id, Material.ground, "hologram");
		setLightOpacity(255);
	}
	
	@Override
	public boolean showInCraftingTool() {
		return false;
	}
	
	public Block getContainingBlock(IBlockAccess world, int x, int y, int z){
		return PC_Utils.<PChg_TileEntityHologramBlock>getTE(world, x, y, z).getContainingBlock();
	}

	public static int getRotation_static(int meta){
        return meta & 0x3;
    }
    
    @Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return getContainingBlock(world, x, y, z).getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
        return null;
    }
    
    @Override
    public int idDropped(int i, Random random, int j){
        return -1;
    }

    @Override
    public int quantityDropped(Random random){
        return 0;
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
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z){
        
    	Block b = getContainingBlock(world, x, y, z);
    	int meta = PC_Utils.getMD(world, x, y, z);
    	
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !PC_Utils.isCreative(player)){
        	ItemStack itemStack = new ItemStack(this);
        	NBTTagCompound nbtTag = itemStack.getTagCompound();
    		if(nbtTag==null)
    			nbtTag = new NBTTagCompound();
    		NBTTagCompound nbtTag2 = new NBTTagCompound();
    		ItemStack itemBlock;
    		if(Item.itemsList[b.blockID].getHasSubtypes()){
    			itemBlock = new ItemStack(b, 1, meta);
    		}else{
    			itemBlock = new ItemStack(b);
    		}
    		itemBlock.writeToNBT(nbtTag2);
    		nbtTag.setCompoundTag("Item", nbtTag2);
    		itemStack.setTagCompound(nbtTag);
            dropBlockAsItem_do(world, x, y, z, itemStack);
        }

        return remove;
    }
    
	@Override
    public boolean renderInventoryBlock(int metadata, Object renderer){
    	PC_Renderer.renderInvBox(renderer, this, metadata);
    	return true;
    }

	@Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
    	Block containingBlock = getContainingBlock(world, x, y, z);
    	if(!containingBlock.hasTileEntity()){
	        PC_Renderer.renderBlockByRenderType(renderer, containingBlock, x, y, z);
    	}else{
    		PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
    	}
        return true;
    }

}
