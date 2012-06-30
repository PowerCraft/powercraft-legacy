package net.minecraft.src;


/**
 * Radio Tile Entity (both TX and RX)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityRadio extends PC_TileEntity {

	/** Device channel */
	public String channel = mod_PClogic.default_radio_channel;
	/** Device type, 0=TX, 1=RX */
	public int type = 0; // 0=tx, 1=rx
	/** Device active flag */
	public boolean active = false;
	/** Dimension of the device (nether, world, end) */
	public int dim = 0;


	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("channel", channel);
		nbttagcompound.setInteger("type", type);
		nbttagcompound.setBoolean("active", active);
		nbttagcompound.setInteger("dim", dim);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		channel = nbttagcompound.getString("channel");
		type = nbttagcompound.getInteger("type");
		active = nbttagcompound.getBoolean("active");
		dim = nbttagcompound.getInteger("dim");
	}


	private boolean registered = false;


	@Override
	public void updateEntity() {
		if (!registered) {
			if (type == 1) {
				PC_Logger.finest("Radio receiver at [" + xCoord + ";" + yCoord + ";" + zCoord + "] registers to RadioManager");
				PClo_RadioManager.registerReceiver(dim, new PC_CoordI(xCoord, yCoord, zCoord), channel);
			} else {
				PC_Logger.finest("Radio transmitter at [" + xCoord + ";" + yCoord + ";" + zCoord + "] registers to RadioManager with signal state = " + active);
				PClo_RadioManager.setTransmitterState(dim, new PC_CoordI(xCoord, yCoord, zCoord), channel, active);
			}
			registered = true;
		}
	}

	/**
	 * Notify block change.
	 */
	public void updateBlock() {
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType().blockID);
		worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
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
	 * Set device type
	 * 
	 * @param typeindex 0=gold TX, 1=iron RX
	 */
	public void setType(int typeindex) {
		type = typeindex;
	}

	/**
	 * @return is this device transmitter
	 */
	public boolean isTransmitter() {
		return type == 0;
	}

	/**
	 * @return is this device receiver
	 */
	public boolean isReceiver() {
		return type == 1;
	}

	/**
	 * @return is the radio device active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set "active" flag and send update to radio manager
	 * 
	 * @param act is active
	 */
	public void setStateWithNotify(boolean act) {
		if (isTransmitter() && active != act) {
			PClo_RadioManager.setTransmitterState(dim, new PC_CoordI(xCoord, yCoord, zCoord), channel, act);
		}
		active = act;
	}

	/**
	 * @return radio channel assigned to this entity
	 */
	public String getChannel() {
		return channel;
	}
}
