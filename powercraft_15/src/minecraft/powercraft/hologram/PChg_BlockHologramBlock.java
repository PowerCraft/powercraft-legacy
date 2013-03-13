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
import powercraft.api.PC_MathHelper;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;

@PC_BlockInfo(itemBlock=PChg_ItemBlockHologramBlock.class, tileEntity=PChg_TileEntityHologramBlock.class)
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
		return GameInfo.<PChg_TileEntityHologramBlock>getTE(world, x, y, z).getContainingBlock();
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PChg_TileEntityHologramBlock();
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itemStack){
        int l = ((PC_MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

        if (entityliving instanceof EntityPlayer && PC_KeyRegistry.isPlacingReversed(((EntityPlayer)entityliving)))
        {
            l = ValueWriting.reverseSide(l);
        }

        ValueWriting.setMD(world, x, y, z, l);
        onNeighborBlockChange(world, x, y, z, 0);
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
    	int meta = GameInfo.getMD(world, x, y, z);
    	
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !GameInfo.isCreative(player)){
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
    
    public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer){
    	PC_Renderer.renderInvBox(renderer, block, metadata);
    }

    public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
    	Block containingBlock = getContainingBlock(world, x, y, z);
    	PC_Renderer.tessellatorDraw();
    	PC_Renderer.resetTerrain(true);
    	PC_Renderer.tessellatorStartDrawingQuads();
        PC_Renderer.renderBlockByRenderType(renderer, containingBlock, x, y, z);
        PC_Renderer.tessellatorDraw();
        PC_Renderer.tessellatorStartDrawingQuads();
    }
    
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}
		case PC_MSGRegistry.MSG_ROTATION:
			return getRotation_static((Integer)obj[0]);
		case PC_MSGRegistry.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_MSGRegistry.MSG_RENDER_WORLD_BLOCK:
			renderWorldBlock(world, pos.x, pos.y, pos.z, (Block)obj[0], (Integer)obj[1], obj[2]);
			break;
		default:
			return null;
		}
		return true;
	}

}
