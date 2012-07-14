package net.minecraft.src;



import java.util.List;
import java.util.Random;

import weasel.Calc;

import net.minecraft.src.PClo_NetManager.WeaselNetwork;
import net.minecraft.src.PClo_NetManager.NetworkMember;


/**
 * Weasel device plugin ("flavor" of the tile entity) - base class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class PClo_WeaselPlugin implements PC_INBT, NetworkMember {
	
	/**
	 * Weasel plugin
	 * @param tew tile entity weasel
	 */
	public PClo_WeaselPlugin(PClo_TileEntityWeasel tew) {
		tileEntity = tew;
	}
	
	/** The tile entity containing this plugin */
	protected PClo_TileEntityWeasel tileEntity;

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
	 * @return numeric index of the type, from {@link PClo_WeaselType}
	 */
	public abstract int getType();

	
	private boolean registeredToNetwork = false;
	
	/**
	 * Handler for tile entity's update tick.
	 */
	public final void update() {
		if(!isMaster()) {
			// register to network if not registered yet (eg. after load)
			if(!registeredToNetwork) {
				registerToNetwork();
				registeredToNetwork = true;
			}
			
			// set "unregistered" status if network is missing
			if(registeredToNetwork && getNetwork() == null) {
				registeredToNetwork = false;
			}
		}
		updateTick();
	}
	
	/**
	 * update tick for this plugin
	 */
	protected abstract void updateTick();

	/** RNG */
	protected static Random rand = new Random();

	/**
	 * Handler for block's event
	 */
	public abstract void onNeighborBlockChanged();	

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
		PClo_BlockWeasel.hugeUpdate(world(), coord());
	}
	
	/** Name of this member in the network. */
	public String memberName = Calc.generateUniqueName();
	
	/** Name of the local network this device is connected to. */
	public String networkName = "";
	
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
		return mod_PClogic.NETWORK.getNetwork(networkName);
	}
	
	/**
	 * @return color of the network this device is connected to.
	 */
	public final PC_Color getNetworkColor() {
		
		if(networkName != null && !networkName.equals("")) {
			WeaselNetwork net = mod_PClogic.NETWORK.getNetwork(networkName);
			if(net != null) return net.getColor();
		}
		
		return new PC_Color(0.3,0.3,0.3);
		
	}
	
	/**
	 * Register this device to network, null check
	 */
	protected final void registerToNetwork() {
		WeaselNetwork net = getNetwork();
		if(net != null) net.registerMember(memberName, this);
	}
	/**
	 * Unregister this device from network, null check
	 */
	protected final void unregisterFromNetwork() {
		WeaselNetwork net = getNetwork();
		if(net != null) net.unregisterMember(memberName);
	}
	
	/**
	 * Set network name, try to register to it.
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
	 * @param name the new name
	 */
	public final void setMemberName(String name) {		
		memberName = name;
		unregisterFromNetwork();
		registerToNetwork();
	}
	
	/**
	 * Get name of this member.
	 * @return name of this member
	 */
	public final String getMemberName() {
		return memberName;
	}
	
	/**
	 * Check if this device is master.<br>
	 * Users can not assign network to master, since master provides it's own one.
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
	 * @param tew tile entity weasel
	 * @param type the plugin ID
	 * @return the new plugin, or null if id was invalid
	 */
	public static PClo_WeaselPlugin getPluginForType(PClo_TileEntityWeasel tew, int type) {
		
		switch(type) {
			case PClo_WeaselType.CORE: return new PClo_WeaselPluginCore(tew);
			case PClo_WeaselType.PORT: return new PClo_WeaselPluginPort(tew);
		}
		
		return null;
	}

	/**
	 * Handler for block removal. Disconnect from bus, network, drop inventory items etc.
	 */
	public final void onBlockRemoval() {
		unregisterFromNetwork();
		onDeviceDestroyed();
	}
	
	public abstract void onDeviceDestroyed();
	
	
	
	
	// Inventory status detection
	

	/**
	 * Check if chest on given side is full
	 * 
	 * @param side meta of the gate, rotation
	 * @param allSlotsFull require that all slots are full. otherwise also
	 *            partially used slots are treated as used.
	 * @return true if chest is full.
	 */
	protected final boolean isChestFull(int side, boolean allSlotsFull) {

		if (side == 0) {
			return isFullChestAt(tileEntity.getCoord().offset(0, 0, 1), allSlotsFull);
		}
		if (side == 1) {
			return isFullChestAt(tileEntity.getCoord().offset(-1, 0, 0), allSlotsFull);
		}
		if (side == 2) {
			return isFullChestAt(tileEntity.getCoord().offset(0, 0, -1), allSlotsFull);
		}
		if (side == 3) {
			return isFullChestAt(tileEntity.getCoord().offset(1, 0, 0), allSlotsFull);
		}
		return false;
	}

	/**
	 * Check if chest on given side is empty
	 * 
	 * @param side metadata of the gate
	 * @return true if chest is empty.
	 */
	protected final boolean isChestEmpty(int side) {
		if (side == 0) {
			return isEmptyChestAt(tileEntity.getCoord().offset(0, 0, 1));
		}
		if (side == 1) {
			return isEmptyChestAt(tileEntity.getCoord().offset(-1, 0, 0));
		}
		if (side == 2) {
			return isEmptyChestAt(tileEntity.getCoord().offset(0, 0, -1));
		}
		if (side == 3) {
			return isEmptyChestAt(tileEntity.getCoord().offset(1, 0, 0));
		}
		return true;
	}

	/**
	 * Check if the chest at given coords is empty
	 * 
	 * @param pos chest pos
	 * @return is full
	 */
	private boolean isEmptyChestAt(PC_CoordI pos) {

		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(world(), pos);
		if (invAt != null) return PC_InvUtils.isInventoryEmpty(invAt);

		List<IInventory> list = world().getEntitiesWithinAABB(IInventory.class, AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			return PC_InvUtils.isInventoryEmpty(list.get(0));
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

		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(tileEntity.worldObj, pos);
		if (invAt != null) {
			if (allSlotsFull) {
				return PC_InvUtils.isInventoryFull(invAt);
			} else {
				return PC_InvUtils.hasInventoryNoFreeSlots(invAt);
			}
		}

		List<IInventory> list = tileEntity.worldObj.getEntitiesWithinAABB(IInventory.class, AxisAlignedBB.getBoundingBox(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			if (allSlotsFull) {
				return PC_InvUtils.isInventoryFull(list.get(0));
			} else {
				return PC_InvUtils.hasInventoryNoFreeSlots(list.get(0));
			}
		}

		return false;
	}
	

	@Override
	public final PClo_WeaselPlugin readFromNBT(NBTTagCompound tag) {		
		networkName = tag.getString("NetworkName");	
		memberName = tag.getString("MemberName");				
		return readPluginFromNBT(tag);
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("NetworkName", networkName);
		tag.setString("MemberName", memberName);
		return writePluginToNBT(tag);
	}
	
	/**
	 * Read plugin specific info from tag
	 * @param tag
	 * @return this
	 */
	protected abstract PClo_WeaselPlugin readPluginFromNBT(NBTTagCompound tag);	
	
	/**
	 * Save plugin data to nbt
	 * @param tag
	 * @return the tag
	 */
	protected abstract NBTTagCompound writePluginToNBT(NBTTagCompound tag);

}
