package powercraft.deco;

import java.util.Random;

import net.minecraft.block.Block;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.block.PC_Block;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.tileentity.PC_TileEntity;

public class PCde_TileEntityChimney extends PC_TileEntity {

	private static Random rand = new Random();
	
	@Override
	public void updateEntity() {
		
		if (rand.nextInt(6) == 0) {
			tryToSmoke();
		}

	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return false
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	// chimney

	private void doSmoke() {
		for (int l = 0; l < 12; l++) {
			double smI = xCoord + rand.nextFloat() * 0.4F + 0.2F;
			double smJ = yCoord + 0.4F + rand.nextFloat() * 0.6F;
			double smK = zCoord + rand.nextFloat() * 0.4F + 0.2F;

			ValueWriting.spawnParticle("EntitySmokeFX", worldObj, smI, smJ, smK, 0.0D, 0.0D, 0.0D, 2.0F);
			
		}
	}

	private boolean doesBlockSmoke(PC_VecI pos) {
		Block block = GameInfo.getBlock(worldObj, pos);
		if (block == Block.stoneOvenActive) return true;
		if (block == Block.fire) return true;
		if (block instanceof PC_Block){
			Object o = ((PC_Block) block).msg(PC_MSGRegistry.MSG_DOES_SMOKE, worldObj, pos);
			if(o instanceof Boolean){
				return (Boolean)o;
			}
		}
		return false;
	}

	private boolean isBlockLitFurnace(PC_VecI pos) {
		return GameInfo.getBID(worldObj, pos) == Block.stoneOvenActive.blockID;
	}

	private boolean isBlockChimney(PC_VecI pos) {
		if (GameInfo.getBID(worldObj, pos) == PCde_App.chimney.blockID) {
			return true;
		}
		return false;
	}

	private void tryToSmoke() {
		if(!worldObj.isRemote)
			return;
		if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord)) { //test if air is above chimney	    

			PC_VecI cursor = getCoord().copy();

			boolean smoke = false;

			cursor.y++;
			while (cursor.y > 0) {
				cursor.y--;

				if (isBlockChimney(cursor)) {
					smoke |= isBlockLitFurnace(cursor.offset(-1, 0, 0));
					smoke |= isBlockLitFurnace(cursor.offset(1, 0, 0));
					smoke |= isBlockLitFurnace(cursor.offset(0, 0, -1));
					smoke |= isBlockLitFurnace(cursor.offset(0, 0, 1));
					if (smoke) break;
					continue;
				} else {
					// no more chimney. check what is underneath.

					// smoke source directly here
					if (doesBlockSmoke(cursor)) {
						smoke = true;
						break;
					}

					//a block under
					if (doesBlockSmoke(cursor.offset(0, -1, 0))) {
						smoke = true;
						break;
					}

					// smoke sources by side
					smoke |= doesBlockSmoke(cursor.offset(-1, 0, 0));
					smoke |= doesBlockSmoke(cursor.offset(1, 0, 0));
					smoke |= doesBlockSmoke(cursor.offset(0, 0, -1));
					smoke |= doesBlockSmoke(cursor.offset(0, 0, 1));
					if (smoke) break;

					// smoke sources by corner
					smoke |= doesBlockSmoke(cursor.offset(1, 0, -1));
					smoke |= doesBlockSmoke(cursor.offset(-1, 0, 1));
					smoke |= doesBlockSmoke(cursor.offset(-1, 0, -1));
					smoke |= doesBlockSmoke(cursor.offset(1, 0, 1));
					if (smoke) break;

					// under by side
					smoke |= doesBlockSmoke(cursor.offset(-1, -1, 0));
					smoke |= doesBlockSmoke(cursor.offset(1, -1, 0));
					smoke |= doesBlockSmoke(cursor.offset(0, -1, -1));
					smoke |= doesBlockSmoke(cursor.offset(0, -1, 1));
					if (smoke) break;

					//under by corner
					smoke |= doesBlockSmoke(cursor.offset(1, -1, -1));
					smoke |= doesBlockSmoke(cursor.offset(-1, -1, 1));
					smoke |= doesBlockSmoke(cursor.offset(-1, -1, -1));
					smoke |= doesBlockSmoke(cursor.offset(1, -1, 1));
					if (smoke) break;
				}
			}

			if (smoke) {
				doSmoke();
			}

		}
	}
	
}
