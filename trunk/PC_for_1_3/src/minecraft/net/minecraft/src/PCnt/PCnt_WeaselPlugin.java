package net.minecraft.src.PCnt;



import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_Color;
import net.minecraft.src.PC_CoordI;
import net.minecraft.src.PC_IInventoryWrapper;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.PC_InvUtils;
import net.minecraft.src.PC_Logger;
import net.minecraft.src.PClo_RadioBus;
import net.minecraft.src.World;
import net.minecraft.src.mod_PClogic;
import net.minecraft.src.PCnt.PCnt_NetManager.NetworkMember;
import net.minecraft.src.PCnt.PCnt_NetManager.WeaselNetwork;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


/**
 * Weasel device plugin ("flavor" of the tile entity) - base class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class PCnt_WeaselPlugin implements PC_INBT, NetworkMember {

	/** who asked for functions. */
	protected NetworkMember asker;	

	@Override
	public void setAsker(NetworkMember nm) {
		this.asker = nm;
	}
	
	@Override
	public NetworkMember getAsker() {
		return asker;
	}
	
	/**
	 * Weasel plugin
	 * 
	 * @param tew tile entity weasel
	 */
	public PCnt_WeaselPlugin(PCnt_TileEntityWeasel tew) {
		tileEntity = tew;
	}

	/** The tile entity containing this plugin */
	private PCnt_TileEntityWeasel tileEntity;

	/**
	 * @return coord of the tile entiy
	 */
	protected final PC_CoordI coord() {
		return tileEntity.getCoord();
	}

	/**
	 * @return world the tile entity is in
	 */
	protected final World world() {
		return tileEntity.worldObj;
	}

	/**
	 * @return numeric index of the type, from {@link PCnt_WeaselType}
	 */
	public abstract int getType();


	private boolean registeredToNetwork = false;

	/**
	 * Handler for tile entity's update tick.
	 * 
	 * @return true if the block was changed and needs chunk save
	 */
	public final boolean update() {
		if (!isMaster()) {
			// register to network if not registered yet (eg. after load)
			if (!registeredToNetwork && networkName != null && !networkName.equals("")) {
				registerToNetwork();
				registeredToNetwork = true;
			}

			// set "unregistered" status if network is missing
			if (registeredToNetwork && getNetwork() == null) {
				registeredToNetwork = false;
				networkName = "";
			}
		}
		try {
			return updateTick();
		} catch (Throwable t) {
			t.printStackTrace();
			PC_Logger.throwing("WeaselPlugin", "update", t);
			return false;
		}
	}

	/**
	 * update tick for this plugin
	 * 
	 * @return true if save is requires
	 */
	protected abstract boolean updateTick();

	/** RNG */
	protected static Random rand = new Random();

	/**
	 * Update state when a neighbor block's redstone state was changed
	 */
	public abstract void onRedstoneSignalChanged();

	/**
	 * Called when Core is restarted. All devices must reset their state to
	 * defaults, but stay connected to network and keep the same names.<br>
	 * Core does not implement this, because it actually sends this command.
	 */
	public abstract void restartDevice();

	/**
	 * Handler for block's event
	 */
	public final void onNeighborBlockChanged() {

		boolean[] inport = PCnt_BlockWeasel.getWeaselInputStates(world(), coord());

		boolean changed = false;
		for (int i = 0; i < weaselInport.length; i++) {
			if (weaselInport[i] != inport[i] && !(inport[i]&&weaselOutport[i])) {
				changed = !nextUpdateIgnore[i];
			}
			nextUpdateIgnore[i] = false;
		}
		weaselInport = inport;

		if (changed) {
			onRedstoneSignalChanged();
		}
	}

	/**
	 * @return message describing last error
	 */
	public abstract String getError();

	/**
	 * @return true if has error
	 */
	public abstract boolean hasError();


	/**
	 * Send block update notification to world.
	 */
	protected final void notifyBlockChange() {
		PCnt_BlockWeasel.hugeUpdate(world(), coord());
	}

	/** Name of this member in the network. */
	private String memberName = Calc.generateUniqueName();

	/** Name of the local network this device is connected to. */
	private String networkName = "";

	/**
	 * Flag which is set when the device is being destroyed due to pick-up.
	 * that means not to drop disks.
	 * */
	protected boolean isPickedUp = false;

	@Override
	public final void onNetworkRenamed(String newName) {
		networkName = newName;
	}

	/**
	 * @return name of the network this device is connected to.
	 */
	@Override
	public final String getNetworkName() {
		return networkName;
	}

	/**
	 * @return The network this device is connected to.
	 */
	public final WeaselNetwork getNetwork() {
		return getNetManager().getNetwork(networkName);
	}

	/**
	 * @return The network manager providing global variable sharing pool.
	 */
	protected final PCnt_NetManager getNetManager() {
		return mod_PClogic.NETWORK;
	}

	/**
	 * @return The radio manager.
	 */
	protected final PClo_RadioBus getRadioManager() {
		return mod_PClogic.RADIO;
	}

	/**
	 * @return weasel engine, or null if this device has none
	 */
	public abstract WeaselEngine getWeaselEngine();

	/**
	 * @return color of the network this device is connected to.
	 */
	public final PC_Color getNetworkColor() {

		if (networkName != null && !networkName.equals("")) {
			WeaselNetwork net = getNetManager().getNetwork(networkName);
			if (net != null) return net.getColor();
		}

		return new PC_Color(0.3, 0.3, 0.3);

	}

	/**
	 * Register this device to network, null check
	 */
	protected final void registerToNetwork() {
		WeaselNetwork net = getNetwork();
		if (net != null) net.registerMember(memberName, this);
	}

	/**
	 * Unregister this device from network, null check
	 */
	protected final void unregisterFromNetwork() {
		WeaselNetwork net = getNetwork();
		if (net != null) net.unregisterMember(memberName);
	}

	/**
	 * Set network name, try to register to it.
	 * 
	 * @param network new network name
	 */
	public final void setNetworkNameAndConnect(String network) {
		unregisterFromNetwork();
		networkName = network;
		registerToNetwork();
		onNetworkChanged();
	}

	/**
	 * Set name of this member, reconnect to network.
	 * 
	 * @param name the new name
	 */
	public final void setMemberName(String name) {
		unregisterFromNetwork();
		memberName = name;
		registerToNetwork();
		onNameChanged();
	}
	
	/**
	 * called after name was changed.
	 */
	protected void onNameChanged() {}

	/**
	 * Get name of this device in the network.
	 * 
	 * @return name of this member
	 */
	public final String getName() {
		return memberName;
	}

	/**
	 * Check if this device is master.<br>
	 * Users can not assign network to master, since master provides it's own
	 * one.
	 * 
	 * @return is master
	 */
	public abstract boolean isMaster();

	/**
	 * Called when a network was changed by the user.<br>
	 * The unregistration and registration is already done.
	 */
	protected abstract void onNetworkChanged();



	/**
	 * Create new instance of a plugin described by given plugin ID.
	 * 
	 * @param tew tile entity weasel
	 * @param type the plugin ID
	 * @return the new plugin, or null if id was invalid
	 */
	public static PCnt_WeaselPlugin getPluginForType(PCnt_TileEntityWeasel tew, int type) {

		switch (type) {
			case PCnt_WeaselType.CORE:
				return new PCnt_WeaselPluginCore(tew);
			case PCnt_WeaselType.SLAVE:
				return new PCnt_WeaselPluginSlave(tew);
			case PCnt_WeaselType.PORT:
				return new PCnt_WeaselPluginPort(tew);
			case PCnt_WeaselType.DISPLAY:
				return new PCnt_WeaselPluginDisplay(tew);
			case PCnt_WeaselType.SPEAKER:
				return new PCnt_WeaselPluginSpeaker(tew);
			case PCnt_WeaselType.TOUCHSCREEN:
				return new PCnt_WeaselPluginTouchscreen(tew);
			case PCnt_WeaselType.DISK_MANAGER:
				return new PCnt_WeaselPluginDiskManager(tew);
			case PCnt_WeaselType.DISK_DRIVE:
				return new PCnt_WeaselPluginDiskDrive(tew);
			case PCnt_WeaselType.TERMINAL:
				return new PCnt_WeaselPluginTerminal(tew);
		}

		return null;
	}

	/**
	 * Handler for block removal. Disconnect from bus, network, drop inventory
	 * items etc.
	 */
	public final void onBlockRemoval() {
		unregisterFromNetwork();
		onDeviceDestroyed();
	}
	
	
	/**
	 * Handler for block removal. Disconnect from bus, network, drop inventory
	 * items etc.
	 */
	public final void onBlockPickup() {
		this.isPickedUp = true;
		onBlockRemoval();
	}

	/**
	 * Hook called when this device was destroyed
	 */
	protected abstract void onDeviceDestroyed();

	/**
	 * If this device has a weasel engine, execute given user function and
	 * return retval.<br>
	 * This function can be called by other members of a network in order to
	 * invoke program functions.
	 * 
	 * @param function name of the function in weasel code
	 * @param args arguments for the function
	 */
	public abstract void callFunctionOnEngine(String function, Object... args);



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

	/**
	 * Update buffered input port
	 */
	protected final void refreshInport() {
		weaselInport = PCnt_BlockWeasel.getWeaselInputStates(world(), coord());
	}

	private boolean[] weaselOutport = { false, false, false, false, false, false };
	private boolean[] weaselInport = { false, false, false, false, false, false };
	private boolean[] nextUpdateIgnore =  { false, false, false, false, false, false };


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


	@Override
	public final PCnt_WeaselPlugin readFromNBT(NBTTagCompound tag) {
		networkName = tag.getString("NetworkName");
		memberName = tag.getString("MemberName");

		for (int i = 0; i < weaselOutport.length; i++) {
			weaselOutport[i] = tag.getBoolean("wo" + i);
		}
		for (int i = 0; i < weaselInport.length; i++) {
			weaselInport[i] = tag.getBoolean("wi" + i);
		}
		for (int i = 0; i < nextUpdateIgnore.length; i++) {
			nextUpdateIgnore[i]=tag.getBoolean("nui" + i);
		}
		try {
			return readPluginFromNBT(tag);
		} catch (Exception e) {
			e.printStackTrace();
			return this;
		}

	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("NetworkName", networkName);
		tag.setString("MemberName", memberName);

		for (int i = 0; i < weaselOutport.length; i++) {
			tag.setBoolean("wo" + i, weaselOutport[i]);
		}
		for (int i = 0; i < weaselInport.length; i++) {
			tag.setBoolean("wi" + i, weaselInport[i]);
		}
		for (int i = 0; i < nextUpdateIgnore.length; i++) {
			tag.setBoolean("nui" + i, nextUpdateIgnore[i]);
		}

		try {
			return writePluginToNBT(tag);
		} catch (Exception e) {
			e.printStackTrace();
			return tag;
		}
	}

	/**
	 * Read plugin specific info from tag
	 * 
	 * @param tag
	 * @return this
	 */
	protected abstract PCnt_WeaselPlugin readPluginFromNBT(NBTTagCompound tag);

	/**
	 * Save plugin data to nbt
	 * 
	 * @param tag
	 * @return the tag
	 */
	protected abstract NBTTagCompound writePluginToNBT(NBTTagCompound tag);

	/**
	 * Block bounds + selection & collision box
	 * 
	 * @return array of floats: minX, minY, minZ, maxX, maxY, maxZ - relative to
	 *         block, 0-1.
	 */
	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 0.5F, 1 };
	}

	/**
	 * hook called when the device was placed
	 * 
	 * @param entityliving
	 */
	public abstract void onBlockPlaced(EntityLiving entityliving);

	/**
	 * hook from block - random display tick. You can make some perticles here.
	 * 
	 * @param random
	 */
	public abstract void onRandomDisplayTick(Random random);

	/**
	 * hook from block - called when block is right-clicked.
	 * 
	 * @param player the player who clicked
	 * @return true if event was consumed - not allow block placing.
	 */
	public abstract boolean onClick(EntityPlayer player);

}
