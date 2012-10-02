package net.minecraft.src;

import java.util.List;
import java.util.Random;

import net.minecraft.src.PCnt_WeaselManager_UNUSED.NetworkMember;

import weasel.WeaselEngine;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public abstract class PCnt_WeaselPlugin implements PC_INBTWD {

	private int id;
	private PCnt_TileEntityWeasel tileEntity;
	private boolean needsSave = false;
	private String name;
	protected PCnt_WeaselNetwork network;
	/** who asked for functions. */
	protected PCnt_WeaselPlugin asker;	
	private int world;
	private PC_CoordI coord = new PC_CoordI(0,0,0);
	private boolean[] weaselOutport = { false, false, false, false, false, false };
	private boolean[] weaselInport = { false, false, false, false, false, false };
	private boolean[] nextUpdateIgnore =  { false, false, false, false, false, false };
	/** RNG */
	protected static Random rand = new Random();
	
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
		if(tew!=null){
			world = tileEntity.worldObj.worldInfo.getDimension();
			coord.setTo(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		}
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
				return new PCnt_WeaselPluginCore();
			case PCnt_WeaselType.SLAVE:
				return new PCnt_WeaselPluginSlave();
			case PCnt_WeaselType.PORT:
				return new PCnt_WeaselPluginPort();
			case PCnt_WeaselType.DISPLAY:
				return new PCnt_WeaselPluginDisplay();
			case PCnt_WeaselType.SPEAKER:
				return new PCnt_WeaselPluginSpeaker();
			case PCnt_WeaselType.TOUCHSCREEN:
				return new PCnt_WeaselPluginTouchscreen();
			case PCnt_WeaselType.DISK_MANAGER:
				return new PCnt_WeaselPluginDiskManager();
			case PCnt_WeaselType.DISK_DRIVE:
				return new PCnt_WeaselPluginDiskDrive();
			case PCnt_WeaselType.TERMINAL:
				return new PCnt_WeaselPluginTerminal();
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
				return new PCnt_WeaselPluginCore(id);
			case PCnt_WeaselType.SLAVE:
				return new PCnt_WeaselPluginSlave(id);
			case PCnt_WeaselType.PORT:
				return new PCnt_WeaselPluginPort(id);
			case PCnt_WeaselType.DISPLAY:
				return new PCnt_WeaselPluginDisplay(id);
			case PCnt_WeaselType.SPEAKER:
				return new PCnt_WeaselPluginSpeaker(id);
			case PCnt_WeaselType.TOUCHSCREEN:
				return new PCnt_WeaselPluginTouchscreen(id);
			case PCnt_WeaselType.DISK_MANAGER:
				return new PCnt_WeaselPluginDiskManager(id);
			case PCnt_WeaselType.DISK_DRIVE:
				return new PCnt_WeaselPluginDiskDrive(id);
			case PCnt_WeaselType.TERMINAL:
				return new PCnt_WeaselPluginTerminal(id);
		}

		return null;
	}
	
	public void update(){
		
	}
	
	public abstract int getType();
	
	public void remove(){
		
	}
	
	public void onNeighborBlockChanged() {
		
	}
	
	public PCnt_WeaselNetwork getNetwork() {
		return network;
	}
	
	public void setNetworkNameAndConnect(String network) {
	}

	public void removeFormNetwork() {
	}
	
	public boolean onClick(EntityPlayer player) {
		return false;
	}

	public void onRandomDisplayTick(Random random) {
	}

	public void onBlockPlaced(EntityLiving entityliving) {
	}

	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 0.5F, 1 };
	}

	public void onBlockRemoval() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		onNameChanged();
	}
	
	protected void onNameChanged(){
		
	}
	
	public boolean doesProvideFunction(String functionName){
		return false;
	}
	
	public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		return null;
	}
	
	public WeaselObject getVariable(String name) {
		return null;
	}
	
	public void setVariable(String name, Object object) {}
	
	public List<String> getProvidedFunctionNames() {
		return null;
	}
	
	public List<String> getProvidedVariableNames() {
		return null;
	}
	
	protected boolean updateTick() {
		return false;
	}
	
	public void onRedstoneSignalChanged() {}
	
	public String getError() {
		return null;
	}
	
	public boolean hasError() {
		return false;
	}
	
	public boolean isMaster() {
		return false;
	}
	
	public WeaselEngine getWeaselEngine() {
		return null;
	}
	
	protected void onNetworkChanged() {}

	protected void onDeviceDestroyed() {}

	public void callFunctionOnEngine(String function, Object... args) {}
	
	protected PCnt_WeaselPlugin readPluginFromNBT(NBTTagCompound tag){
		return this;
	}
	
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag){
		return tag;
	}
	
	public void restartDevice(){}
	
	public void set(Object[] o){}
	
	public Object[] get(){
		return null;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("myID", id);
		tag.setInteger("world", world);
		PC_Utils.saveToNBT(tag, "coord", coord);
		writePluginToNBT(tag);
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		id = tag.getInteger("myID");
		world = tag.getInteger("world");
		PC_Utils.loadFromNBT(tag, "coord", coord);
		readPluginFromNBT(tag);
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
	
	protected void notifyBlockChange(){
		
	}

	public PC_Color getColor() {
		if(network!=null)
			return network.getColor();
		return new PC_Color(0.3, 0.3, 0.3);
	}
	
	public void setAsker(PCnt_WeaselPlugin nm) {
		this.asker = nm;
	}
	
	public PCnt_WeaselPlugin getAsker() {
		return asker;
	}
	
	protected World world(){
		return PC_Utils.mcs().worldServerForDimension(world);
	}
	
	protected PC_CoordI coord(){
		return coord;
	}
	
	/**
	 * Get outport for the block. This is called by the Block directly.
	 * 
	 * @return array of booleans
	 */
	public final boolean[] getWeaselOutputStates() {
		//@formatter:off		
		// It works. Better not to change this.
		return new boolean[] {
				weaselOutport[1],
				weaselOutport[0],
				weaselOutport[2],
				weaselOutport[3],
				weaselOutport[4],
				weaselOutport[5]
		};		
		//@formatter:on
	}
	/**
	 * Get redstone state on given port
	 * 
	 * @param port port name (F,L,R,B,U,D)
	 * @return rs state
	 */
	protected final boolean getInport(String port) {
		if (port.equals("B")) return weaselInport[0];
		if (port.equals("L")) return weaselInport[1];
		if (port.equals("R")) return weaselInport[2];
		if (port.equals("F")) return weaselInport[3];
		if (port.equals("U")) return weaselInport[4];
		if (port.equals("D")) return weaselInport[5];
		return false;
	}

	/**
	 * Get redstone state sending to given port
	 * 
	 * @param port port name (B,L,R,F,U,D)
	 * @return rs state
	 */
	protected final boolean getOutport(String port) {
		if (port.equals("B")) return weaselOutport[0];
		if (port.equals("L")) return weaselOutport[1];
		if (port.equals("R")) return weaselOutport[2];
		if (port.equals("F")) return weaselOutport[3];
		if (port.equals("U")) return weaselOutport[4];
		if (port.equals("D")) return weaselOutport[5];
		return false;
	}

	/**
	 * Set redstone state on given port
	 * 
	 * @param port port name (F,L,R,B,U,D)
	 * @param state rs state
	 */
	protected final void setOutport(String port, boolean state) {
		boolean old = getOutport(port);

		if (port.equals("B")) weaselOutport[0] = state;
		if (port.equals("L")) weaselOutport[1] = state;
		if (port.equals("R")) weaselOutport[2] = state;
		if (port.equals("F")) weaselOutport[3] = state;
		if (port.equals("U")) weaselOutport[4] = state;
		if (port.equals("D")) weaselOutport[5] = state;

		nextUpdateIgnore[0] = port.equals("B");
		nextUpdateIgnore[1] = port.equals("L");
		nextUpdateIgnore[2] = port.equals("R");
		nextUpdateIgnore[3] = port.equals("F");
		nextUpdateIgnore[4] = port.equals("U");
		nextUpdateIgnore[5] = port.equals("D");		

		if (state != old) {
			notifyBlockChange();
			refreshInport();
		}
	}


	// Inventory status detection

	/**
	 * Test if there is a full chest on given side.
	 * 
	 * @param args the arguments given (can be: String side, Boolean strict
	 *            (optional))
	 * @return is full
	 */
	protected WeaselObject chestFullTest(WeaselObject... args) {
		int rotation = coord().getMeta(world()) & 3;

		String side = "F";
		boolean strict = false;

		if (args.length > 0) {
			if (args[0] instanceof WeaselString) {
				side = (String) args[0].get();
			}
			if (args[0] instanceof WeaselBoolean) {
				strict = (Boolean) args[0].get();
			}
		}
		if (args.length > 1) {
			strict = (Boolean) args[1].get();
		}

		if (side.equals("U") || side.equals("T")) {
			return new WeaselBoolean(isFullChestAt(coord().offset(0, 1, 0), strict));
		}

		if (side.equals("D")) {
			return new WeaselBoolean(isFullChestAt(coord().offset(0, -1, 0), strict));
		}

		if (side.equals("B")) rotation = rotation + 0;
		if (side.equals("F")) rotation = rotation + 2;
		if (side.equals("L")) rotation = rotation + 1;
		if (side.equals("R")) rotation = rotation + 3;

		if (rotation > 3) rotation = rotation % 4;

		return new WeaselBoolean(isChestFull(rotation, strict));
	}

	/**
	 * Test if there is an empty chest on given side.
	 * 
	 * @param args the arguments given (can be: String side)
	 * @return is empty
	 */
	protected WeaselObject chestEmptyTest(WeaselObject... args) {

		int rotation = coord().getMeta(world()) & 3;

		String side = "F";

		if (args.length == 1) {
			if (args[0] instanceof WeaselString) {
				side = (String) args[0].get();
			}
		}

		if (side.equals("U") || side.equals("T")) {
			return new WeaselBoolean(isEmptyChestAt(coord().offset(0, 1, 0)));
		}

		if (side.equals("D")) {
			return new WeaselBoolean(isEmptyChestAt(coord().offset(0, -1, 0)));
		}

		if (side.equals("B")) rotation = rotation + 0;
		if (side.equals("F")) rotation = rotation + 2;
		if (side.equals("L")) rotation = rotation + 1;
		if (side.equals("R")) rotation = rotation + 3;
		if (rotation > 3) rotation = rotation % 4;

		return new WeaselBoolean(isChestEmpty(rotation));

	}

	/**
	 * Check if chest on given side is full
	 * 
	 * @param side meta of the gate, rotation
	 * @param allSlotsFull require that all slots are full. otherwise also
	 *            partially used slots are treated as used.
	 * @return true if chest is full.
	 */
	private final boolean isChestFull(int side, boolean allSlotsFull) {

		if (side == 0) {
			return isFullChestAt(coord().offset(0, 0, 1), allSlotsFull);
		}
		if (side == 1) {
			return isFullChestAt(coord().offset(-1, 0, 0), allSlotsFull);
		}
		if (side == 2) {
			return isFullChestAt(coord().offset(0, 0, -1), allSlotsFull);
		}
		if (side == 3) {
			return isFullChestAt(coord().offset(1, 0, 0), allSlotsFull);
		}
		return false;
	}

	/**
	 * Check if chest on given side is empty
	 * 
	 * @param side metadata of the gate
	 * @return true if chest is empty.
	 */
	private final boolean isChestEmpty(int side) {
		if (side == 0) {
			return isEmptyChestAt(coord().offset(0, 0, 1));
		}
		if (side == 1) {
			return isEmptyChestAt(coord().offset(-1, 0, 0));
		}
		if (side == 2) {
			return isEmptyChestAt(coord().offset(0, 0, -1));
		}
		if (side == 3) {
			return isEmptyChestAt(coord().offset(1, 0, 0));
		}
		return true;
	}

	/**
	 * Check if the chest at given coords is empty
	 * 
	 * @param pos chest pos
	 * @return is full
	 */
	private final boolean isEmptyChestAt(PC_CoordI pos) {

		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(world(), pos);
		if (invAt != null) return PC_InvUtils.isInventoryEmpty(invAt);

		List<IInventory> list = world().getEntitiesWithinAABB(IInventory.class,
				AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			return PC_InvUtils.isInventoryEmpty(list.get(0));
		}

		List<PC_IInventoryWrapper> list2 = world().getEntitiesWithinAABB(PC_IInventoryWrapper.class,
				AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			if(list2.get(0).getInventory() != null) {
				return PC_InvUtils.isInventoryEmpty(list2.get(0).getInventory());
			}
		}

		return false;

	}

	/**
	 * Check if the chest at given coords is full
	 * 
	 * @param pos chest pos
	 * @param allSlotsFull strict check for full slots
	 * @return is full
	 */
	private boolean isFullChestAt(PC_CoordI pos, boolean allSlotsFull) {

		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(world(), pos);
		if (invAt != null) {
			if (allSlotsFull) {
				return PC_InvUtils.isInventoryFull(invAt);
			} else {
				return PC_InvUtils.hasInventoryNoFreeSlots(invAt);
			}
		}

		List<IInventory> list = world().getEntitiesWithinAABB(IInventory.class,
				AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			if (allSlotsFull) {
				return PC_InvUtils.isInventoryFull(list.get(0));
			} else {
				return PC_InvUtils.hasInventoryNoFreeSlots(list.get(0));
			}
		}

		List<PC_IInventoryWrapper> list2 = world().getEntitiesWithinAABB(PC_IInventoryWrapper.class,
				AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			if (allSlotsFull) {
				if(list2.get(0).getInventory() != null) {
					return PC_InvUtils.isInventoryEmpty(list2.get(0).getInventory());
				}
			} else {
				if(list2.get(0).getInventory() != null) {
					return PC_InvUtils.isInventoryEmpty(list2.get(0).getInventory());
				}
			}
		}

		return false;
	}
	
	/**
	 * Update buffered input port
	 */
	protected final void refreshInport() {
		weaselInport = PCnt_BlockWeasel.getWeaselInputStates(world(), coord());
	}
	
	/**
	 * Set all output ports to false
	 */
	protected final void resetOutport() {
		boolean wasOn = false;
		for (int i = 0; i < weaselOutport.length; i++) {
			wasOn |= weaselOutport[i];
			weaselOutport[i] = false;
			nextUpdateIgnore[i] = false;
		}
		refreshInport();
	}
	
}
