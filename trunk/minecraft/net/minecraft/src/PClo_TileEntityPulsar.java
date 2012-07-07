package net.minecraft.src;


/**
 * Redstone Pulsar Tile Entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityPulsar extends PC_TileEntity {


	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("timer", (short) delayTimer);
		nbttagcompound.setShort("delay", (short) delay);
		nbttagcompound.setShort("holdtime", (short) holdtime);
		nbttagcompound.setBoolean("on", active);
		nbttagcompound.setBoolean("paused", paused);
		nbttagcompound.setBoolean("silent", silent);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		delayTimer = nbttagcompound.getShort("timer");
		delay = nbttagcompound.getShort("delay");
		holdtime = nbttagcompound.getShort("holdtime");
		active = nbttagcompound.getBoolean("on");
		paused = nbttagcompound.getBoolean("paused");
		silent = nbttagcompound.getBoolean("silent");
		if (delay < 2) {
			delay = 2;
		}
		if (holdtime < 1 || holdtime >= delay - 1) {
			holdtime = (delay > 6 ? 3 : 1);
		}
	}

	/**
	 * Set delay to
	 * 
	 * @param i ticks
	 */
	public void setDelay(int i) {
		delay = i;
		if (delay <= 3) {
			delay = 3;
		}
		if (holdtime < 1 || holdtime >= delay - 1) {
			holdtime = (delay > 6 ? 3 : 1);
		}
	}

	/**
	 * Set hold time to
	 * 
	 * @param i ticks
	 */
	public void setHoldTime(int i) {
		holdtime = i;
		if (holdtime < 1 || holdtime >= delay - 1) {
			holdtime = (delay > 6 ? 3 : 1);
		}
	}

	/**
	 * Add delay increment, check and fix invalid value.
	 * 
	 * @param i increment
	 */
	public void changeDelay(int i) {
		delay += i;
		if (delay <= 3) {
			delay = 3;
		}
	}

	/**
	 * Show current delay in chat.
	 */
	public void printDelay() {
		PC_Utils.chatMsg(PC_Lang.tr("pc.pulsar.clickMsg", new String[] { delay + "", (delay / 20D) + "" }), true);
	}

	/**
	 * Show curretn delay in chat, include remaining ticks.
	 */
	public void printDelayTime() {
		PC_Utils.chatMsg(PC_Lang.tr("pc.pulsar.clickMsgTime", new String[] { delay + "", (delay / 20D) + "", (delay - delayTimer) + "" }), true);
	}

	@Override
	public void updateEntity() {
		if (paused) {
			return;
		}

		if (delayTimer < 0) {
			active = true;
			updateBlock();
		}

		delayTimer++;

		if (delayTimer >= holdtime && active) {
			active = false;
			updateBlock();
		}
		if (delayTimer >= delay) {
			if (!silent && mod_PCcore.soundsEnabled) {
				worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.click", 0.15F, 0.6F);
			}
			delayTimer = -1;
		}

	}

	/**
	 * Notify block change.
	 */
	public void updateBlock() {
		worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, mod_PClogic.pulsar.blockID, 1);

//		
//		worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
//		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
//		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return true
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	/**
	 * Set "silent" glag - no sound from ticking.
	 * 
	 * @param flag
	 */
	public void setSilent(boolean flag) {
		silent = flag;
	}

	/** is the pulsar paused? */
	public boolean paused = false;
	/** time till change */
	public int delayTimer = 0;
	/** delay in ticks */
	public int delay = 10;
	/** hold time in ticks */
	public int holdtime = 4;
	/** status whether pulsar is active */
	public boolean active;
	/** makes sound */
	public boolean silent;
}
