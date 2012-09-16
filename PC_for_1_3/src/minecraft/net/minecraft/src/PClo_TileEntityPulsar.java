package net.minecraft.src;


/**
 * Redstone Pulsar Tile Entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityPulsar extends PC_TileEntity implements PC_IPacketSetter{


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
		if (paused||worldObj.isRemote) {
			return;
		}

		boolean change=false;
		
		if (delayTimer < 0 && !active) {
			active = true;
			updateBlock();
			change=true;
		}

		delayTimer++;

		if (delayTimer >= holdtime && active) {
			active = false;
			updateBlock();
			change=true;
		}
		
		if(change)
			PC_Utils.setTileEntity(null, this, "active", active, "change");
		
		if (delayTimer >= delay) {
			delayTimer = -1;
		}

	}

	/**
	 * Notify block change.
	 */
	public void updateBlock() {
		worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, mod_PClogic.pulsar.blockID, 1);
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
	public boolean active = false;
	/** makes sound */
	public boolean silent = false;

	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("holdTime")){
				setHoldTime((Integer)o[p++]);
			}else if(var.equals("delayTime")){
				setDelay((Integer)o[p++]);
			}else if(var.equals("silent")){
				setSilent((Boolean)o[p++]);
			}else if(var.equals("changeDelay")){
				changeDelay((Integer)o[p++]);
			}else if(var.equals("holdtime")){
				setHoldTime((int)(Integer)o[p++]);
			}else if(var.equals("active")){
				active = (Boolean)o[p++];
			}else if(var.equals("paused")){
				paused = (Boolean)o[p++];
			}else if(var.equals("change")){
				if (worldObj.isRemote){
					if (!silent && mod_PCcore.soundsEnabled && worldObj.isRemote) {
						worldObj.playSound(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.click", 1.0F, 1.0F);
						System.out.println("Sound...");
					}
				}
			}
		}
	}

	@Override
	public Object[] get() {
		return new Object[]{
			"holdTime", holdtime,
			"delayTime", delay,
			"silent", silent,
			"holdtime", holdtime,
			"active", active,
			"paused", paused
		};
	}
	
	
}
