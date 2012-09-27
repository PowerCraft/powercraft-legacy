package net.minecraft.src;



/**
 * Radio Tile Entity (both TX and RX)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityRadio extends PC_TileEntity {

	/** Device channel */
	private String channel = mod_PClogic.default_radio_channel;
	/** Device type, 0=TX, 1=RX */
	public int type = 0; // 0=tx, 1=rx
	/** Device active flag */
	public boolean active = false;
	/** Dimension of the device (nether, world, end) */
	public int dim = 0;
	/** Hide the label */
	public boolean hideLabel = false;


	/** Render a smaller model */
	public boolean renderMicro = false;


	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("channel", channel);
		nbttagcompound.setInteger("type", type);
		nbttagcompound.setBoolean("active", active);
		nbttagcompound.setInteger("dim", dim);
		nbttagcompound.setBoolean("NoLabel", hideLabel);
		nbttagcompound.setBoolean("Micro", renderMicro);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		channel = nbttagcompound.getString("channel");
		type = nbttagcompound.getInteger("type");
		active = nbttagcompound.getBoolean("active");
		dim = nbttagcompound.getInteger("dim");
		hideLabel = nbttagcompound.getBoolean("NoLabel");
		renderMicro = nbttagcompound.getBoolean("Micro");
	}

	@Override
	public void updateEntity() {
		if(isReceiver()){
			boolean newstate = mod_PClogic.RADIO.getChannelState(channel);
			if (active != newstate) {
				active = newstate;
				PC_Utils.setTileEntity(null, this, "active", active);
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockType().blockID, 1);
				updateBlock();
			}
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
		return !worldObj.isRemote && isReceiver();
	}

	/**
	 * Set device type
	 * 
	 * @param typeindex 0=gold TX, 1=iron RX
	 */
	public void setType(int typeindex) {
		if(type==0 && type!=typeindex){
			mod_PClogic.RADIO.transmitterOff(channel);
		}
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
	public void setTransmitterState(boolean act) {
		if(active != act){
			active = act;
			if(act && type==0 && !worldObj.isRemote){
				mod_PClogic.RADIO.transmitterOn(channel);
			}else if(type==0 && !worldObj.isRemote){
				mod_PClogic.RADIO.transmitterOff(channel);
			}
		}
	}

	/**
	 * @return radio channel assigned to this entity
	 */
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		if(!this.channel.equals(channel)){
			if(this.isActive()&&this.isTransmitter())
				mod_PClogic.RADIO.transmitterOff(this.channel);
			this.channel = channel;
		}
	}
	
	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("type")){
				this.setType((Integer)o[p++]);
			}else if(var.equals("channel")){
				setChannel((String)o[p++]);
			}else if(var.equals("active")){
				active = !(Boolean)o[p++];
			}else if(var.equals("dim")){
				this.dim = (Integer)o[p++];
			}else if(var.equals("hideLabel")){
				this.hideLabel = (Boolean)o[p++];
			}else if(var.equals("renderMicro")){
				this.renderMicro = (Boolean)o[p++];
			}
		}
		setTransmitterState(!active);
	}

	@Override
	public Object[] get() {
		Object[] o = new Object[12];
		o[0] = "type";
		o[1] = type;
		o[2] = "channel";
		o[3] = channel;
		o[4] = "active";
		o[5] = active;
		o[6] = "dim";
		o[7] = dim;
		o[8] = "hideLabel";
		o[9] = hideLabel;
		o[10] = "renderMicro";
		o[11] = renderMicro;
		return o;
	}
}
