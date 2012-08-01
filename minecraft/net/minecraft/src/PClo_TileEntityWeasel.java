package net.minecraft.src;


/**
 * Weasel device tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityWeasel extends PC_TileEntity {

	/**
	 * TEW
	 */
	public PClo_TileEntityWeasel() {}

	@Override
	public boolean canUpdate() {
		return true;
	}

	private PClo_WeaselPlugin plugin;


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

		if (maintag.getBoolean("nullplugin")) {
			return;
		}

		int type = maintag.getInteger("type");

		plugin = PClo_WeaselPlugin.getPluginForType(this, type);
		if (plugin != null) plugin.readFromNBT(maintag.getCompoundTag("Plugin"));
	}

	@Override
	public void writeToNBT(NBTTagCompound maintag) {
		super.writeToNBT(maintag);
		if (plugin == null) {
			maintag.setBoolean("nullplugin", true);
		} else {
			maintag.setInteger("type", plugin.getType());
			maintag.setCompoundTag("Plugin", plugin.writeToNBT(new NBTTagCompound()));
		}
	}

	/**
	 * @return weasel plugin
	 */
	public PClo_WeaselPlugin getPlugin() {
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
		plugin = PClo_WeaselPlugin.getPluginForType(this, type);
	}

	@Override
	public void onBlockPickup() {
		if (plugin != null) {
			plugin.onBlockPickup();
		}
	}

}
