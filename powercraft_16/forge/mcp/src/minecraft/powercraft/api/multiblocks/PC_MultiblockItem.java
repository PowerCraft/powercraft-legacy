package powercraft.api.multiblocks;


import java.lang.reflect.Constructor;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Logger;
import powercraft.api.PC_Utils;
import powercraft.api.items.PC_Item;
import powercraft.api.registries.PC_MultiblockRegistry;


public abstract class PC_MultiblockItem extends PC_Item {

	public PC_MultiblockItem(int id) {

		super(id);
		PC_MultiblockRegistry.registerMultiblockTileEntity(this, getTileEntityClass());
	}


	public abstract Class<? extends PC_MultiblockTileEntity> getTileEntityClass();


	public abstract PC_MultiblockType getMultiblockType();


	public PC_MultiblockTileEntity getTileEntity(ItemStack itemStack) {
		Class<? extends PC_MultiblockTileEntity> c = getTileEntityClass();
		try {
			Constructor<? extends PC_MultiblockTileEntity> constr = c.getConstructor(ItemStack.class);
			try{
				return constr.newInstance(itemStack);
			}catch(Exception e){
				e.printStackTrace();
				PC_Logger.severe("Faild to generate multiblock tile entity");
			}
			return null;
		} catch (Exception e) {}
		try {
			return c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Faild to generate multiblock tile entity");
		}
		return null;
	}


	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xHit, float yHit,
			float zHit) {

		Block block = PC_Utils.getBlock(world, x, y, z);
		boolean replaceAble = false;
		if (block instanceof PC_BlockMultiblock) {
			int ret = handleMultiblockClick(itemStack, entityPlayer, world, x, y, z, side, xHit, yHit, zHit, false);
			if (ret != -1) {
				return ret != 0;
			}
		}
		if (block == Block.snow && (world.getBlockMetadata(x, y, z) & 7) < 1) {
			side = 1;
			replaceAble = true;
		} else if (block != Block.vine && block != Block.tallGrass && block != Block.deadBush
				&& (block == null || !block.isBlockReplaceable(world, x, y, z))) {
			switch (side) {
				case 0:
					y--;
					break;
				case 1:
					y++;
					break;
				case 2:
					z--;
					break;
				case 3:
					z++;
					break;
				case 4:
					x--;
					break;
				case 5:
					x++;
					break;
				default:
					break;
			}
			block = PC_Utils.getBlock(world, x, y, z);
			if ((block == Block.snow && (world.getBlockMetadata(x, y, z) & 7) < 1) || !(block != Block.vine && block != Block.tallGrass && block != Block.deadBush
					&& (block == null || !block.isBlockReplaceable(world, x, y, z)))) {
				replaceAble = true;
			}
		} else {
			replaceAble = true;
		}
		switch (side) {
			case 0:
				yHit = 1;
				break;
			case 1:
				yHit = 0;
				break;
			case 2:
				zHit = 1;
				break;
			case 3:
				zHit = 0;
				break;
			case 4:
				xHit = 1;
				break;
			case 5:
				xHit = 0;
				break;
			default:
				break;
		}
		if (block == null || replaceAble) {
			world.setBlock(x, y, z, PC_BlockMultiblock.block.blockID);
			block = PC_Utils.getBlock(world, x, y, z);
		}
		if (block instanceof PC_BlockMultiblock) {
			int ret = handleMultiblockClick(itemStack, entityPlayer, world, x, y, z, side, xHit, yHit, zHit, true);
			if (ret != -1) {
				return ret != 0;
			}
		}
		return false;
	}


	@SuppressWarnings("unused")
	public int handleMultiblockClick(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xHit,
			float yHit, float zHit, boolean secoundTry) {

		PC_TileEntityMultiblock tileEntityMultiblock = PC_Utils.getTE(world, x, y, z);
		switch (getMultiblockType()) {
			case CENTER:
				if (tileEntityMultiblock.setMultiblockTileEntity(PC_MultiblockIndex.CENTER, getTileEntity(itemStack))) {
					itemStack.stackSize--;
					return 1;
				}
				return -1;
			case CORNER:

				break;
			case EDGE:

				break;
			case FACE: {
				PC_Direction dir = PC_Direction.getOrientation(side);
				PC_Direction[] dirs;
				float hit1;
				float hit2;
				float hit3;
				final float a = 0.5f-2f/16f;
				switch(dir){
				case DOWN:
				case UP:
					hit1 = xHit;
					hit2 = zHit;
					hit3 = yHit;
					dirs = new PC_Direction[]{PC_Direction.WEST, PC_Direction.EAST, PC_Direction.NORTH, PC_Direction.SOUTH};
					break;
				case EAST:
				case WEST:
					hit1 = yHit;
					hit2 = zHit;
					hit3 = xHit;
					dirs = new PC_Direction[]{PC_Direction.DOWN, PC_Direction.UP, PC_Direction.NORTH, PC_Direction.SOUTH};
					break;
				case NORTH:
				case SOUTH:
					hit1 = xHit;
					hit2 = yHit;
					hit3 = zHit;
					dirs = new PC_Direction[]{PC_Direction.WEST, PC_Direction.EAST, PC_Direction.DOWN, PC_Direction.UP};
					break;
				default:
					return 0;
				}
				hit1 = 0.5f-hit1;
				hit2 = 0.5f-hit2;
				if(hit3==0 || hit3==1){
					if(!secoundTry)
						return -1;
					dir = dir.getOpposite();
				}
				if(Math.abs(hit1)>a || Math.abs(hit2)>a){
					if(Math.abs(hit1)>Math.abs(hit2)){
						if(hit1>0){
							dir = dirs[0];
						}else{
							dir = dirs[1];
						}
					}else{
						if(hit2>0){
							dir = dirs[2];
						}else{
							dir = dirs[3];
						}
					}
				}
				if (dir!=null && tileEntityMultiblock.setMultiblockTileEntity(PC_MultiblockIndex.FACEINDEXFORDIR[dir.ordinal()], getTileEntity(itemStack))) {
					itemStack.stackSize--;
					return 1;
				}
				return 0;
			}
			default:
				break;
		}
		return 0;
	}


	public abstract void loadMultiblockItem();

}
