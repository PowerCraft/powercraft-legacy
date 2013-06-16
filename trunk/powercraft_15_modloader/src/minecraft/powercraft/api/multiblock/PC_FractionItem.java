package powercraft.api.multiblock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public abstract class PC_FractionItem extends PC_Item {
	
	protected PC_FractionItem(int id, String textureName, String... textureNames) {
		super(id, textureName, textureNames);
	}

	protected abstract PC_FractionBlock getFractionBlock(ItemStack itemStack);
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int dir, float xHit, float yHit, float zHit) {
		int blockID = PC_Utils.getBID(world, x, y, z);
		int metadata = PC_Utils.getMD(world, x, y, z);
		Block block = Block.blocksList[blockID];
		
		PC_Direction pcDir = PC_Direction.getFromMCDir(dir);
		
		if (blockID == Block.snow.blockID && (metadata & 7) < 1) {
			dir = 1;
			pcDir = PC_Direction.TOP;
		} else if (!PC_Utils.isBlockReplaceable(world, x, y, z) && !(block instanceof PC_BlockMultiblock)) {
			
			PC_VecI offset=null;
			
			if(block instanceof PC_Block){
				offset = ((PC_Block) block).moveBlockTryToPlaceAt(world, x, y, z, pcDir, xHit, yHit, zHit, itemStack, entityPlayer);
			}
			
			if(offset==null){
				offset = pcDir.getOffset();
			}
			
			x += offset.x;
			y += offset.y;
			z += offset.z;
			
		}
		
		if (itemStack.stackSize == 0) {
			return false;
		} 
		
		blockID = PC_Utils.getBID(world, x, y, z);
		metadata = PC_Utils.getMD(world, x, y, z);
		block = Block.blocksList[blockID];
		
		if(block==null){
			PC_Utils.setBID(world, x,  y, z, PC_BlockMultiblock.instance.blockID);
			blockID = PC_Utils.getBID(world, x, y, z);
			metadata = PC_Utils.getMD(world, x, y, z);
			block = Block.blocksList[blockID];
		}
		
		TileEntity te = PC_Utils.getTE(world, x, y, z); 
		
		if(te instanceof PC_IMultiblock){
			PC_IMultiblock multiblock = (PC_IMultiblock)te;
			if(!world.isRemote && multiblock.addFraction(itemStack, pcDir, xHit, yHit, zHit, getFractionBlock(itemStack))){
				itemStack.stackSize--;
				return true;
			}
		}
		
		return false;
		
	}

}
