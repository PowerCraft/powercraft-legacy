package powercraft.hologram;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_ClientUtils;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;

@PC_BlockInfo(itemBlock=PChg_ItemBlockHologramBlockEmpty.class)
public class PChg_BlockHologramBlockEmpty extends PC_Block {

	public PChg_BlockHologramBlockEmpty(int id) {
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
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
    public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
    	final Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
    	for(int xOff=-1;xOff<2;xOff++){
    		for(int yOff=-1;yOff<2;yOff++){
    			for(int zOff=-1;zOff<2;zOff++){
    				if((zOff|xOff|yOff)==0) continue;
    				int bid = GameInfo.getBID(world, xOff+x, yOff+y, zOff+z);
    				if(!map.containsKey(bid)){
    					map.put(bid, 1);
    				}else{
    					map.put(bid, map.get(bid)+1);
    				}
    			}
    		}
    	}
    	int maxCount=0, fittingID=0;
    	for(Entry<Integer, Integer> entry:map.entrySet()){
    		if(entry.getValue()>maxCount && entry.getKey()!=0){
    			maxCount=entry.getValue();
    			fittingID=entry.getKey();
    		}
    	}
    	if(fittingID==0) fittingID = PChg_App.hologramBlockEmpty.blockID;

    	Block renderingBlock = Block.blocksList[fittingID];
    	PC_Renderer.tessellatorDraw();
    	if(fittingID==PChg_App.hologramBlockEmpty.blockID||(PC_ClientUtils.mc().thePlayer.getCurrentEquippedItem()!=null && PC_ClientUtils.mc().thePlayer.getCurrentEquippedItem().itemID==Item.stick.itemID)){
    		PC_Renderer.swapTerrain(this);
        	PC_Renderer.tessellatorStartDrawingQuads();
    		PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
    	}else{
        	PC_Renderer.resetTerrain(true);
        	PC_Renderer.tessellatorStartDrawingQuads();
            PC_Renderer.renderBlockByRenderType(renderer, renderingBlock, x, y, z);	
    	}
        PC_Renderer.tessellatorDraw();
        PC_Renderer.resetTerrain(true);
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
		}case PC_MSGRegistry.MSG_RENDER_WORLD_BLOCK:
			renderWorldBlock(world, pos.x, pos.y, pos.z, (Block)obj[0], (Integer)obj[1], obj[2]);
			break;
		default:
			return null;
		}
		return true;
	}

}
