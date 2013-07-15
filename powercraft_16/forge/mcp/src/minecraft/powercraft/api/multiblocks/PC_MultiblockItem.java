package powercraft.api.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import powercraft.api.PC_Logger;
import powercraft.api.PC_Utils;
import powercraft.api.items.PC_Item;

public abstract class PC_MultiblockItem extends PC_Item {

	public PC_MultiblockItem(int id) {
		super(id);
		PC_MultiblockRegistry.registerMultiblockTileEntity(this, getTileEntityClass());
	}
	
	public abstract Class<? extends PC_MultiblockTileEntity> getTileEntityClass();

	public abstract PC_MultiblockType getMultiblockType();
	
	public PC_MultiblockTileEntity getTileEntity(ItemStack itemStack) {
		try {
			return getTileEntityClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Faild to generate multiblock tile entity");
		}
		return null; 
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit) {
		Block block = PC_Utils.getBlock(world, x, y, z);
		if(block instanceof PC_BlockMultiblock){
			int ret = handleMultiblockClick(itemStack, entityPlayer, world, x, y, z, side, xHit, yHit, zHit);
			if(ret!=-1){
				return ret!=0;
			}
		}
		if (block == Block.snow && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            side = 1;
        }else if (block != Block.vine && block != Block.tallGrass && block != Block.deadBush && (block == null || !block.isBlockReplaceable(world, x, y, z))){
        	switch(side){
        	case 0:y--;break;
        	case 1:y++;break;
        	case 2:z--;break;
        	case 3:z++;break;
        	case 4:x--;break;
        	case 5:x++;break;
        	}
        }
		block = PC_Utils.getBlock(world, x, y, z);
		if(block==null){
			world.setBlock(x, y, z, PC_BlockMultiblock.block.blockID);
			block = PC_Utils.getBlock(world, x, y, z);
		}
		if(block instanceof PC_BlockMultiblock){
			int ret = handleMultiblockClick(itemStack, entityPlayer, world, x, y, z, side, xHit, yHit, zHit);
			if(ret!=-1){
				return ret!=0;
			}
		}
		return false;
	}
	
	private int handleMultiblockClick(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit){
		PC_TileEntityMultiblock tileEntityMultiblock = PC_Utils.getTE(world, x, y, z);
		switch(getMultiblockType()){
		case CENTER:
			if(tileEntityMultiblock.setCenter(getTileEntity(itemStack))){
				itemStack.stackSize--;
				return 1;
			}
			return -1;
		case CORNER:
			
			break;
		case EDGE:
			
			break;
		case FACE:
			
			break;
		default:
			break;
		}
		return 0;
	}

	public abstract void loadMultiblockItem();
	
}
