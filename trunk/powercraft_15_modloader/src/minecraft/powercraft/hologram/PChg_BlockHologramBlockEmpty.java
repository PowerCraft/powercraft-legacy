package powercraft.hologram;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Utils;

@PC_BlockInfo(name="Hologramblock", itemBlock=PChg_ItemBlockHologramBlockEmpty.class)
public class PChg_BlockHologramBlockEmpty extends PC_Block {

	public PChg_BlockHologramBlockEmpty(int id) {
		super(id, Material.ground, "hologram");
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
	
	@Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
    	final Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
    	for(int xOff=-1;xOff<2;xOff++){
    		for(int yOff=-1;yOff<2;yOff++){
    			for(int zOff=-1;zOff<2;zOff++){
    				if((zOff|xOff|yOff)==0) continue;
    				int bid = PC_Utils.getBID(world, xOff+x, yOff+y, zOff+z);
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
    	PC_Renderer.tessellatorStartDrawingQuads();
    	if(fittingID==PChg_App.hologramBlockEmpty.blockID||(PC_ClientUtils.mc().thePlayer.getCurrentEquippedItem()!=null && PC_ClientUtils.mc().thePlayer.getCurrentEquippedItem().itemID==Item.stick.itemID)){
    		PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
    	}else{
    		if(!renderingBlock.hasTileEntity()){
    			PC_Renderer.renderBlockByRenderType(renderer, renderingBlock, x, y, z);	
    		}else{
    			PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
    		}
    	}
        PC_Renderer.tessellatorDraw();
        PC_Renderer.tessellatorStartDrawingQuads();
        return true;
    }

}
