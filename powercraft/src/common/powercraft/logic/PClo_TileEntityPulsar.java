package powercraft.logic;

import net.minecraft.src.NBTTagCompound;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PClo_TileEntityPulsar extends PC_TileEntity {
	/** time till change */
	private int delayTimer = 0;
	/** delay in ticks */
	private int delay = 10;
	/** hold time in ticks */
	private int holdtime = 4;
	/** status whether pulsar is active */
	private boolean active = false;
	/** is the pulsar paused? */
	private boolean paused = false;
	/** makes sound */
	private boolean silent = false;
	
	public PClo_TileEntityPulsar(){
		
	}
	
	public void setTimes(int delay, int holdTime){
		this.delay = delay;
		if(this.delay<2)
			this.delay = 2;
		this.holdtime = holdTime;
		if(this.holdtime>=this.delay)
			this.holdtime = this.delay-1;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("delayTimer", delayTimer);
		nbttagcompound.setInteger("delay", delay);
		nbttagcompound.setInteger("holdtime", holdtime);
		nbttagcompound.setBoolean("active", active);
		nbttagcompound.setBoolean("paused", paused);
		nbttagcompound.setBoolean("silent", silent);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		delayTimer = nbttagcompound.getInteger("delayTimer");
		delay = nbttagcompound.getInteger("delay");
		holdtime = nbttagcompound.getInteger("holdtime");
		active = nbttagcompound.getBoolean("active");
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
	 * Show current delay in chat.
	 */
	public void printDelay() {
		PC_Utils.chatMsg(PC_Utils.tr("pc.pulsar.clickMsg", new String[] { delay + "", PC_Utils.ticksToSecs(delay) + "" }), true);
	}

	/**
	 * Show curretn delay in chat, include remaining ticks.
	 */
	public void printDelayTime() {
		PC_Utils.chatMsg(PC_Utils.tr("pc.pulsar.clickMsgTime", new String[] { delay + "", PC_Utils.ticksToSecs(delay) + "", (delay - delayTimer) + "" }), true);
	}

	public boolean isActive() {
		return active;
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
			PC_PacketHandler.setTileEntity(this, "active", active, "change");
		
		if (delayTimer >= delay) {
			delayTimer = -1;
		}

	}

	/**
	 * Notify block change.
	 */
	public void updateBlock() {
		if(worldObj!=null)
			worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, mod_PowerCraftLogic.pulsar.blockID, 1);
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

	public boolean isSilent() {
		return silent;
	}

	public int getDelay() {
		return delay;
	}
	
	public int getHold() {
		return holdtime;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean paused){
		PC_PacketHandler.setTileEntity(this, "paused", paused);
		this.paused = paused;
	}
	
	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("hold")){
				holdtime = (Integer)o[p++];
			}else if(var.equals("delay")){
				delay = (Integer)o[p++];
			}else if(var.equals("silent")){
				silent = (Boolean)o[p++];
			}else if(var.equals("active")){
				active = (Boolean)o[p++];
			}else if(var.equals("paused")){
				paused = (Boolean)o[p++];
			}else if(var.equals("change")){
				if (worldObj.isRemote){
					if (!silent) {
						PC_Utils.playSound(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.click", 1.0F, 1.0F);
					}
					worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
				}
			}
		}
	}

	@Override
	public Object[] getData() {
		return new Object[]{
			"hold", holdtime,
			"delay", delay,
			"silent", silent,
			"active", active,
			"paused", paused
		};
	}
	
}
