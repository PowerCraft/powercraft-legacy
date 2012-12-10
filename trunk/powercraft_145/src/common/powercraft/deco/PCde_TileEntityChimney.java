package powercraft.deco;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntitySmokeFX;
import net.minecraft.src.ModLoader;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

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

			PC_Utils.spawnParticle("EntitySmokeFX", worldObj, smI, smJ, smK, 0.0D, 0.0D, 0.0D, 2.0F);
			
		}
	}

	private boolean doesBlockSmoke(PC_CoordI pos) {
		int id = pos.getId(worldObj);
		if (id == Block.stoneOvenActive.blockID) return true;
		if (id == Block.fire.blockID) return true;

		if (PC_Utils.hasFlag(worldObj, pos, "SMOKE")) return true;
		return false;
	}

	private boolean doesBlockSmokeOpenly(PC_CoordI pos) {
		int id = pos.getId(worldObj);
		if (id == Block.fire.blockID) return true;

		if (PC_Utils.hasFlag(worldObj, pos, "SMOKE")) return true;
		return false;
	}

	private boolean isBlockLitFurnace(PC_CoordI pos) {
		return pos.getId(worldObj) == Block.stoneOvenActive.blockID;
	}

	private boolean isBlockChimney(PC_CoordI pos) {
		if (pos.getId(worldObj) == PCde_App.chimney.blockID) {
			return true;
		}
		return false;
	}

	private void tryToSmoke() {
		if(!worldObj.isRemote)
			return;
		if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord)) { //test if air is above chimney	    

			PC_CoordI cursor = getCoord().copy();

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
					smoke |= doesBlockSmokeOpenly(cursor.offset(-1, 0, 0));
					smoke |= doesBlockSmokeOpenly(cursor.offset(1, 0, 0));
					smoke |= doesBlockSmokeOpenly(cursor.offset(0, 0, -1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(0, 0, 1));
					if (smoke) break;

					// smoke sources by corner
					smoke |= doesBlockSmokeOpenly(cursor.offset(1, 0, -1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(-1, 0, 1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(-1, 0, -1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(1, 0, 1));
					if (smoke) break;

					// under by side
					smoke |= doesBlockSmokeOpenly(cursor.offset(-1, -1, 0));
					smoke |= doesBlockSmokeOpenly(cursor.offset(1, -1, 0));
					smoke |= doesBlockSmokeOpenly(cursor.offset(0, -1, -1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(0, -1, 1));
					if (smoke) break;

					//under by corner
					smoke |= doesBlockSmokeOpenly(cursor.offset(1, -1, -1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(-1, -1, 1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(-1, -1, -1));
					smoke |= doesBlockSmokeOpenly(cursor.offset(1, -1, 1));
					if (smoke) break;
				}
			}

			if (smoke) {
				doSmoke();
			}

		}
	}
	
}
