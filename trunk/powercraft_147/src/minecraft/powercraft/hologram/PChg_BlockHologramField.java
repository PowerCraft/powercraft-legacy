package powercraft.hologram;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_IItemInfo;
import powercraft.management.registry.PC_ItemRegistry;
import powercraft.management.registry.PC_KeyRegistry;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.renderer.PC_Renderer;

@PC_BlockInfo(tileEntity=PChg_TileEntityHologramField.class)
public class PChg_BlockHologramField extends PC_Block implements PC_IItemInfo {

	public PChg_BlockHologramField(int id) {
		super(id, 0, Material.ground);
		setHardness(0.5F);
        setResistance(0.5F);
        setStepSound(Block.soundGlassFootstep);
        setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
        return null;
    }
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PChg_TileEntityHologramField();
	}

	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float par7, float par8, float par9)
    {
        ItemStack ihold = entityplayer.getCurrentEquippedItem();

        PChg_TileEntityHologramField tileentity = GameInfo.getTE(world, i, j, k);
        
        if (ihold != null)
        {
            if (ihold.getItem() == PC_ItemRegistry.getPCItemByName("PCco_ItemActivator"))
            {
                
                if (PC_KeyRegistry.isPlacingReversed(entityplayer))
                {
                    switch(l){
                    case 0:l=1;break;
                    case 1:l=0;break;
                    case 2:l=3;break;
                    case 3:l=2;break;
                    case 4:l=5;break;
                    case 5:l=4;break;
                    }
                }

                if (tileentity != null)
                {
                	PC_VecI coordOffset = tileentity.getOffset();
                	if(coordOffset==null)
                		coordOffset = new PC_VecI();
                	if(!world.isRemote){
	                    switch (l)
	                    {
	                        case 0:
	                            coordOffset.y+=2;
	                            break;
	
	                        case 1:
	                            coordOffset.y-=2;
	                            break;
	                            
	                        case 2:
	                            coordOffset.z-=2;
	                            break;
	
	                        case 3:
	                            coordOffset.z+=2;
	                            break;
	
	                        case 4:
	                            coordOffset.x-=2;
	                            break;
	
	                        case 5:
	                            coordOffset.x+=2;
	                            break;
	                    }
	                    
	                    coordOffset.x = MathHelper.clamp_int(coordOffset.x, -100, 100);
	                    coordOffset.y = MathHelper.clamp_int(coordOffset.y+tileentity.yCoord, 0, world.getActualHeight())-tileentity.yCoord;
	                    coordOffset.z = MathHelper.clamp_int(coordOffset.z, -100, 100);
	                    tileentity.setOffset(coordOffset);
                	}
                }

                return true;
            }
        }
        
        return false;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer){
		float s = 1.0f/16.0f;
		PC_Renderer.resetTerrain(true);
		ValueWriting.setBlockBounds(Block.blockSteel, 0, 0, 0, s*15, s*5, s);
    	PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);
    	ValueWriting.setBlockBounds(Block.blockSteel, s*15, 0, 0, s*16, s*5, s*15);
    	PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);
    	ValueWriting.setBlockBounds(Block.blockSteel, s, 0, s*15, s*16, s*5, s*16);
    	PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);
    	ValueWriting.setBlockBounds(Block.blockSteel, 0, 0, s, s, s*5, s*16);
    	PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);
    	ValueWriting.setBlockBounds(Block.blockSteel, s, 0, s, s*15, s, s*15);
    	PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);
    	ValueWriting.setBlockBounds(Block.blockSteel, 0, 0, 0, 1, 1, 1);
    	PC_Renderer.swapTerrain(block);    	
    	ValueWriting.setBlockBounds(block, s*3, s*6, s*3, s*13, s*16, s*13);
    	PC_Renderer.renderInvBox(renderer, block, 0);
    	ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
    }
	
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		float s = 1.0f/16.0f;
		PC_Renderer.tessellatorDraw();
        PC_Renderer.resetTerrain(true);
        PC_Renderer.tessellatorStartDrawingQuads();
		ValueWriting.setBlockBounds(Block.blockSteel, 0, 0, 0, s*15, s*5, s);
		PC_Renderer.renderStandardBlock(renderer, Block.blockSteel, x, y, z);
    	ValueWriting.setBlockBounds(Block.blockSteel, s*15, 0, 0, s*16, s*5, s*15);
    	PC_Renderer.renderStandardBlock(renderer, Block.blockSteel, x, y, z);
    	ValueWriting.setBlockBounds(Block.blockSteel, s, 0, s*15, s*16, s*5, s*16);
    	PC_Renderer.renderStandardBlock(renderer, Block.blockSteel, x, y, z);
    	ValueWriting.setBlockBounds(Block.blockSteel, 0, 0, s, s, s*5, s*16);
    	PC_Renderer.renderStandardBlock(renderer, Block.blockSteel, x, y, z);
    	ValueWriting.setBlockBounds(Block.blockSteel, s, 0, s, s*15, s, s*15);
    	PC_Renderer.renderStandardBlock(renderer, Block.blockSteel, x, y, z);
    	ValueWriting.setBlockBounds(Block.blockSteel, 0, 0, 0, 1, 1, 1);
    	PC_Renderer.tessellatorDraw();
    	PC_Renderer.swapTerrain(block);
        PC_Renderer.tessellatorStartDrawingQuads();
        ValueWriting.setBlockBounds(block, s*3, s*6, s*3, s*13, s*16, s*13);
        PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
    	ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
    	PC_Renderer.tessellatorDraw();
    	PC_Renderer.resetTerrain(true);
        PC_Renderer.tessellatorStartDrawingQuads();
    }
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			return "Hologram Field";
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
