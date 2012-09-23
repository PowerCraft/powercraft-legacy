package net.minecraft.src;

import java.util.Random;

public abstract class PCnt_WeaselPlugin implements PC_INBTWD {

	private int id;
	private PCnt_TileEntityWeasel tileEntity;
	private boolean needsSave = false;
	private String name;
	private PCnt_WeaselNetwork network;
	
	public PCnt_WeaselPlugin(){
		id = PCnt_WeaselManager.getFreeID();
		PCnt_WeaselManager.registerPlugin(this);
	}
	
	public PCnt_WeaselPlugin(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
	public PCnt_TileEntityWeasel getTileEntity(){
		return tileEntity;
	}
	
	public PCnt_WeaselPlugin setTileEntity(PCnt_TileEntityWeasel tew){
		tileEntity = tew;
		return this;
	}

	/**
	 * Create new instance of a plugin described by given plugin ID.
	 * 
	 * @param tew tile entity weasel
	 * @param type the plugin ID
	 * @return the new plugin, or null if id was invalid
	 */
	public static PCnt_WeaselPlugin getPluginForType(int type) {

		switch (type) {
			case PCnt_WeaselType.CORE:
				//return new PCnt_WeaselPluginCore_UNUSED(tew);
			case PCnt_WeaselType.SLAVE:
				//return new PCnt_WeaselPluginSlave_UNUSED(tew);
			case PCnt_WeaselType.PORT:
				//return new PCnt_WeaselPluginPort_UNUSED(tew);
			case PCnt_WeaselType.DISPLAY:
				//return new PCnt_WeaselPluginDisplay_UNUSED(tew);
			case PCnt_WeaselType.SPEAKER:
				//return new PCnt_WeaselPluginSpeaker_UNUSED(tew);
			case PCnt_WeaselType.TOUCHSCREEN:
				//return new PCnt_WeaselPluginTouchscreen_UNUSED(tew);
			case PCnt_WeaselType.DISK_MANAGER:
				//return new PCnt_WeaselPluginDiskManager_UNUSED(tew);
			case PCnt_WeaselType.DISK_DRIVE:
				//return new PCnt_WeaselPluginDiskDrive_UNUSED(tew);
			case PCnt_WeaselType.TERMINAL:
				//return new PCnt_WeaselPluginTerminal_UNUSED(tew);
		}

		return null;
	}
	
	/**
	 * Create new instance of a plugin described by given plugin ID.
	 * 
	 * @param tew tile entity weasel
	 * @param type the plugin ID
	 * @param id the plugin ID
	 * @return the new plugin, or null if id was invalid
	 */
	public static PCnt_WeaselPlugin getPluginForType(int type, int id) {

		switch (type) {
			case PCnt_WeaselType.CORE:
				//return new PCnt_WeaselPluginCore_UNUSED(tew);
			case PCnt_WeaselType.SLAVE:
				//return new PCnt_WeaselPluginSlave_UNUSED(tew);
			case PCnt_WeaselType.PORT:
				//return new PCnt_WeaselPluginPort_UNUSED(tew);
			case PCnt_WeaselType.DISPLAY:
				//return new PCnt_WeaselPluginDisplay_UNUSED(tew);
			case PCnt_WeaselType.SPEAKER:
				//return new PCnt_WeaselPluginSpeaker_UNUSED(tew);
			case PCnt_WeaselType.TOUCHSCREEN:
				//return new PCnt_WeaselPluginTouchscreen_UNUSED(tew);
			case PCnt_WeaselType.DISK_MANAGER:
				//return new PCnt_WeaselPluginDiskManager_UNUSED(tew);
			case PCnt_WeaselType.DISK_DRIVE:
				//return new PCnt_WeaselPluginDiskDrive_UNUSED(tew);
			case PCnt_WeaselType.TERMINAL:
				//return new PCnt_WeaselPluginTerminal_UNUSED(tew);
		}

		return null;
	}
	
	public void onBlockPickup(){
		
	}
	
	public void update(){
		
	}
	
	public abstract int getType();
	
	public void remove(){
		
	}
	
	public void onNeighborBlockChanged() {
		
	}
	
	public boolean[] getWeaselOutputStates() {
		return new boolean[]{false, false, false, false, false, false};
	}
	
	public PCnt_WeaselNetwork getNetwork() {
		return network;
	}
	
	public void setNetworkNameAndConnect(String network) {
		// TODO Auto-generated method stub
		
	}

	public void removeFormNetwork() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onClick(EntityPlayer player) {
		return false;
	}

	public boolean isMaster() {
		return false;
	}

	public void onRandomDisplayTick(Random random) {
	}

	public boolean hasError() {
		return false;
	}

	public void onBlockPlaced(EntityLiving entityliving) {
	}

	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 0.5F, 1 };
	}

	public void onBlockRemoval() {
	}
	
	public Object getName() {
		return name;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("myID", id);
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		id = tag.getInteger("myID");
		return this;
	}

	@Override
	public boolean needsSave() {
		boolean ns = needsSave;
		needsSave = false;
		return ns;
	}
	
	public PCnt_WeaselPlugin needSave(){
		needsSave = true;
		return this;
	}
	
}
