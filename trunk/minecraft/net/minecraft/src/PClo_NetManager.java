package net.minecraft.src;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import weasel.IWeaselHardware;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselVariableMap;


/**
 * Weasel Network Manager
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_NetManager implements PC_INBT {

	private static final String netfile = "/weaselnet.dat";

	/**
	 * Local weasel network, provided by a weasel CORE to it's peripherals.
	 * 
	 * @author MightyPork
	 */
	public static class WeaselNetwork implements PC_INBT {

		/** Map of members, please dont edit. */
		private Map<String, NetworkMember> members = new HashMap<String, NetworkMember>();

		private PC_Color color = PC_Color.randomColor();

		/** Local shared variable pool */
		public WeaselVariableMap localHeap = new WeaselVariableMap();

		/**
		 * @return the net color
		 */
		public PC_Color getColor() {
			return color;
		}

		/**
		 * Set net color
		 * 
		 * @param color color to set
		 */
		public void setColor(PC_Color color) {
			this.color = color;
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag) {
			tag.setCompoundTag("Color", color.writeToNBT(new NBTTagCompound()));
			tag.setCompoundTag("Heap", localHeap.writeToNBT(new NBTTagCompound()));
			return tag;
		}

		@Override
		public WeaselNetwork readFromNBT(NBTTagCompound tag) {
			color.readFromNBT(tag.getCompoundTag("Color"));
			localHeap.readFromNBT(tag.getCompoundTag("Heap"));
			return this;
		}

		/**
		 * Called when a network was renamed, all members must change their
		 * copies of the network name.
		 * 
		 * @param newName
		 */
		public void renamedTo(String newName) {
			for (NetworkMember member : members.values()) {
				member.onNetworkRenamed(newName);
			}
		}

		/**
		 * Unregister member form the network
		 * 
		 * @param memberName
		 */
		public void unregisterMember(String memberName) {
			members.remove(memberName);
		}

		/**
		 * Register member to the network
		 * 
		 * @param memberName member's name
		 * @param member the member
		 */
		public void registerMember(String memberName, NetworkMember member) {
			members.put(memberName, member);
		}

		/**
		 * Get network member by name
		 * 
		 * @param name
		 * @return member or null
		 */
		public NetworkMember getMember(String name) {
			return members.get(name);
		}

		/**
		 * get size of network
		 * 
		 * @return size
		 */
		public int size() {
			return members.size();
		}

		/**
		 * @return member map
		 */
		public Map<String, NetworkMember> getMembers() {
			return members;
		}
	}

	/**
	 * Interface for weasel network members
	 * 
	 * @author MightyPork
	 */
	public static interface NetworkMember extends IWeaselHardware {
		/**
		 * @return name of the network thuis member is connected to
		 */
		public abstract String getNetworkName();

		/**
		 * Called when a network was renamed. Member must change his copy of the
		 * network name.
		 * 
		 * @param newName new network name
		 */
		public void onNetworkRenamed(String newName);
	}

	/**
	 * Minecraft instance
	 */
	public static Minecraft mc = ModLoader.getMinecraftInstance();

	/**
	 * Flag that this bus contains some new information that should be saved to
	 * disk.
	 */
	public boolean needsSave = false;

	/**
	 * Globally shared variable pool
	 */
	public WeaselVariableMap globalHeap = new WeaselVariableMap();

	/**
	 * Map of local networks, each has it's own local heap and a color
	 */
	private Map<String, WeaselNetwork> localNetworks = new HashMap<String, WeaselNetwork>();

	/**
	 * Set a weasel variable into the global weasel bus. You should use some
	 * prefixes in order to prevent cross-system conflicts.
	 * 
	 * @param name variable key - name
	 * @param value variable value
	 * @throws WeaselRuntimeException if you are trying to store incompatible
	 *             variable type
	 */
	public void setGlobalVariable(String name, WeaselObject value) throws WeaselRuntimeException {
		checkWorldChange();
		globalHeap.setVariableForce(name, value);
		needsSave = true;
	}

	/**
	 * Get state of a weasel variable.
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public WeaselObject getGlobalVariable(String name) {
		checkWorldChange();
		return globalHeap.getVariable(name);
	}

	private String worldName = null;
	private String worldSaveDir = null;

	/**
	 * Clear device lists if the world changed
	 */
	private void checkWorldChange() {
		if (mc.theWorld == null) return;
		if (mc.theWorld.worldInfo.getWorldName() != worldName) {
			if (worldSaveDir != null) {
				System.out.println("World changed.");
				saveToFile();
			}

			globalHeap.clear();
			worldName = mc.theWorld.worldInfo.getWorldName();
			worldSaveDir = (((SaveHandler) mc.theWorld.saveHandler).getSaveDirectory()).toString();
			loadFromFile();
			needsSave = false;
		} else {
			worldSaveDir = (((SaveHandler) mc.theWorld.saveHandler).getSaveDirectory()).toString();
		}
	}

	/**
	 * Load this bus from file in world folder
	 */
	public void loadFromFile() {
		File file = new File(worldSaveDir + netfile);
		if (file.exists()) {
			// load it
			NBTTagCompound tag = new NBTTagCompound();
			try {
				tag.load(new DataInputStream(new FileInputStream(file)));
				needsSave = false;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			readFromNBT(tag);
			needsSave = false;
		}
	}

	/**
	 * Save this bus to a file in world folder
	 */
	public void saveToFile() {
		if (worldSaveDir == null) return;
		File file = new File(worldSaveDir + netfile);
		NBTTagCompound tag = writeToNBT(new NBTTagCompound());
		try {
			tag.write(new DataOutputStream(new FileOutputStream(file)));
			needsSave = false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		tag.setCompoundTag("GlobalHeap", globalHeap.writeToNBT(new NBTTagCompound()));

		System.out.println("Saving global heap: " + globalHeap);

		return tag;
	}

	@Override
	public PClo_NetManager readFromNBT(NBTTagCompound tag) {

		globalHeap.clear();
		globalHeap.readFromNBT(tag.getCompoundTag("GlobalHeap"));
		System.out.println("Global heap loaded, contains: " + globalHeap);

		return this;
	}

	/**
	 * Get local network
	 * 
	 * @param networkName name of the network
	 * @return the network
	 */
	public WeaselNetwork getNetwork(String networkName) {
		checkWorldChange();
		return localNetworks.get(networkName);
	}

	/**
	 * Create and register a network. Note that you must save this network to
	 * your NBT yourself.
	 * 
	 * @param name new network name
	 * @return the newly created network.
	 */
	public WeaselNetwork createNetwork(String name) {
		checkWorldChange();
		WeaselNetwork net = new WeaselNetwork();
		localNetworks.put(name, net);
		return net;
	}

	/**
	 * Remove a local network from the pool (a block providing it was removed)
	 * 
	 * @param name network name
	 * @return the removed network
	 */
	public WeaselNetwork destroyNetwork(String name) {
		checkWorldChange();
		return localNetworks.remove(name);
	}

	/**
	 * Connect a local network to the manager. This network might be loaded from
	 * a nbt tag.
	 * 
	 * @param name
	 * @param network
	 */
	public void registerNetwork(String name, WeaselNetwork network) {
		checkWorldChange();
		localNetworks.put(name, network);
	}

	/**
	 * Rename network, and tell all its members to rename their network names.
	 * Don't forget to change your network name, too. Network itself does not
	 * contain it.
	 * 
	 * @param name
	 * @param newName
	 */
	public void renameNetwork(String name, String newName) {
		checkWorldChange();
		WeaselNetwork net = getNetwork(name);
		localNetworks.remove(name);
		localNetworks.put(newName, net);
		net.renamedTo(newName);
	}
}
