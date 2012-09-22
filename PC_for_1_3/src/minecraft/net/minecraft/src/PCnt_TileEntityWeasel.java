package net.minecraft.src;



/**
 * Weasel device tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCnt_TileEntityWeasel extends PC_TileEntity {

	/**
	 * TEW
	 */
	public PCnt_TileEntityWeasel() {}

	@Override
	public boolean canUpdate() {
		return true;
	}

	private int id;
	private PCnt_WeaselPlugin plugin;
	private int type;

	private int updateIgnoreCounter = 10;
	private long lastUpdateAbsoluteTime = 0;

	/**
	 * Set to true if the entity is removed. Saves resources.
	 */
	public boolean zombie = false; // set true if this tile entity was already destroyed

	/** number of updates to skip before check. */
	public int PRESCALLER = 1;

	@Override
	public void updateEntity() {
		if (zombie || worldObj == null) {
			return;
		}
		// fix for double updates
		long t = System.currentTimeMillis();
		if (t - lastUpdateAbsoluteTime < 5) {
			return;
		}
		lastUpdateAbsoluteTime = t;

		if (updateIgnoreCounter-- <= 0) {
			updateIgnoreCounter = PRESCALLER;
		} else {
			return;
		}

		if (plugin != null) {
			if (plugin.update()) {
				worldObj.getChunkFromBlockCoords(xCoord, zCoord).setChunkModified();
			}
		}
	}



	/**
	 * Call weasel's function update
	 */
	public void onDirectPinChanged() {
		if (plugin != null) plugin.onNeighborBlockChanged();
	}



	@Override
	public void readFromNBT(NBTTagCompound maintag) {
		super.readFromNBT(maintag);

		//if (maintag.getBoolean("nullplugin")) {
		//	return;
		//}

		type = maintag.getInteger("type");
		id =  maintag.getInteger("myid");
		
		getPlugin();
		
		//plugin = PCnt_WeaselPlugin.getPluginForType(this, type);
		//if (plugin != null) plugin.readFromNBT(maintag.getCompoundTag("Plugin"));
	}

	@Override
	public void writeToNBT(NBTTagCompound maintag) {
		super.writeToNBT(maintag);
		//if (plugin == null) {
		//	maintag.setBoolean("nullplugin", true);
		//} else {
		maintag.setInteger("type", plugin.getType());
		maintag.setInteger("myid", plugin.getID());
		//	maintag.setCompoundTag("Plugin", plugin.writeToNBT(new NBTTagCompound()));
	//	}
	}

	/**
	 * @return weasel plugin
	 */
	public PCnt_WeaselPlugin getPlugin() {
		if(plugin==null){
			plugin = (PCnt_WeaselPlugin)PCnt_WeaselManager.getPlugin(id);
			if(plugin==null){
				plugin = PCnt_WeaselPlugin.getPluginForType(this, type);
			}
		}
		return plugin;
	}

	/**
	 * @return ID type of the weasel plugin
	 */
	public int getType() {
		if (plugin == null) return -1;
		return plugin.getType();
	}

	/**
	 * Set type of weasel plugin (called when the block is placed)
	 * 
	 * @param type the type ID
	 */
	public void setType(int type) {
		this.type = type;
		PCnt_WeaselManager.removePlugin(plugin);
		plugin = PCnt_WeaselPlugin.getPluginForType(this, type);
	}

	@Override
	public void onBlockPickup() {
		if (plugin != null) {
			PCnt_WeaselManager.removePlugin(plugin);
			plugin.onBlockPickup();
		}
	}

	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("id")){
				id = (Integer)o[p++];
			}else if(var.equals("type")){
				setType((Integer)o[p++]);
			}
		}
	}

	@Override
	public Object[] get() {
		return new Object[]{
				"id", id,
				"type", type
		};
	}

}
