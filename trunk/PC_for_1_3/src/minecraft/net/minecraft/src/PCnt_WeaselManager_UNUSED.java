package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;

import weasel.IWeaselHardware;
import weasel.WeaselEngine;
import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselVariableMap;

public class PCnt_WeaselManager_UNUSED extends PC_PacketHandler implements PC_INBTWD {
	
	public static class WeaselNetwork implements PC_INBTWD{

		/** Map of members, please dont edit. */
		private Map<String, NetworkMember> members = new HashMap<String, NetworkMember>();

		private PC_Color color = PC_Color.randomColor();

		/** Local shared variable pool */
		public WeaselVariableMap localHeap = new WeaselVariableMap();

		private boolean needsSave = false;
		
		/**
		 * @return the net color
		 */
		public PC_Color getColor() {
			return color.copy();
		}

		/**
		 * Set net color
		 * 
		 * @param color color to set
		 */
		public void setColor(PC_Color color) {
			this.color.setTo(color);
			for(NetworkMember nm:members.values()){
				if(nm instanceof PCnt_WeaselPlugin_UNUSED)
					PC_Utils.setTileEntity(null, ((PCnt_WeaselPlugin_UNUSED)nm).getTileEntity(), "color", color.getHex());
			}
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
			NetworkMember nm = members.get(memberName);
			if(nm instanceof PCnt_WeaselPlugin_UNUSED)
				PC_Utils.setTileEntity(null, ((PCnt_WeaselPlugin_UNUSED)nm).getTileEntity(), "nocolor");
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
			if(member instanceof PCnt_WeaselPlugin_UNUSED)
				PC_Utils.setTileEntity(null, ((PCnt_WeaselPlugin_UNUSED)member).getTileEntity(), "color", color.getHex());
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

		@Override
		public boolean needsSave() {
			boolean ns = needsSave;
			needsSave=false;
			return ns;
		}
	}
	
	/**
	 * Interface for weasel network members
	 * 
	 * @author MightyPork
	 */
	public static abstract class NetworkMember implements IWeaselHardware, PC_INBTWD {
		
		public NetworkMember(){
			PCnt_WeaselManager_UNUSED.registerPlugin(this);
		}
		
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
		public abstract void onNetworkRenamed(String newName);
		
		/**
		 * This is used when slave tries to get core's functions. It prevents creating infinite loop, since core skips the asker.
		 * @param nm
		 */
		public abstract void setAsker(NetworkMember nm);
		
		public abstract NetworkMember getAsker();
		
		private int id=0;
		
		public int getID(){
			return id;
		}
		
	}
	
	/**
	 * Minecraft instance
	 */
	public static Minecraft mc = ModLoader.getMinecraftInstance();

	/**
	 * Flag that this bus contains some new information that should be saved to
	 * disk.
	 */
	public static boolean needsSave = false;
	/**
	 * Globally shared variable pool
	 */
	public static WeaselVariableMap globalHeap = new WeaselVariableMap();
	/**
	 * Map of local networks, each has it's own local heap and a color
	 */
	private static Map<String, WeaselNetwork> localNetworks = new HashMap<String, WeaselNetwork>();
	/**
	 * Map of local networks, each has it's own local heap and a color
	 */
	private static List<NetworkMember> weaselPlugin = new ArrayList<NetworkMember>();
	
	/**
	 * Set a weasel variable into the global weasel bus. You should use some
	 * prefixes in order to prevent cross-system conflicts.
	 * 
	 * @param name variable key - name
	 * @param value variable value
	 * @throws WeaselRuntimeException if you are trying to store incompatible
	 *             variable type
	 */
	public static void setGlobalVariable(String name, WeaselObject value) throws WeaselRuntimeException {
		globalHeap.setVariableForce(name, value);
		needsSave = true;
	}

	/**
	 * Get state of a weasel variable.
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public static WeaselObject getGlobalVariable(String name) {
		if(globalHeap.getVariable(name) == null) throw new WeaselRuntimeException("Global network does't contain variable "+name);
		return globalHeap.getVariable(name);
	}

	/**
	 * Get if globvar exists
	 * 
	 * @param name variable name
	 * @return variable value.
	 */
	public static boolean hasGlobalVariable(String name) {
		return globalHeap.getVariable(name) != null;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		tag.setCompoundTag("GlobalHeap", globalHeap.writeToNBT(new NBTTagCompound()));
		
		Set<Entry<String, WeaselNetwork>> ln = localNetworks.entrySet();
		
		tag.setInteger("localNetwork.size", localNetworks.size());
		
		int i=0;
		
		for(Entry<String, WeaselNetwork> e:ln){
			NBTTagCompound t = new NBTTagCompound();
			e.getValue().writeToNBT(t);
			tag.setString("localNetwork["+i+"].key", e.getKey());
			tag.setCompoundTag("localNetwork["+i+"].value", t);
			i++;
		}
		 
		System.out.println("Saving global heap: " + globalHeap);

		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {

		globalHeap.clear();
		localNetworks.clear();
		globalHeap.readFromNBT(tag.getCompoundTag("GlobalHeap"));
		
		int size = tag.getInteger("localNetwork.ize");
		
		for(int i=0; i<size; i++){
			String key = tag.getString("localNetwork["+i+"].key");
			WeaselNetwork value = createNetwork(key);
			value.readFromNBT(tag.getCompoundTag("localNetwork["+i+"].value"));
		}
		
		System.out.println("Global heap loaded, contains: " + globalHeap);

		return this;
	}

	/**
	 * Get local network
	 * 
	 * @param networkName name of the network
	 * @return the network
	 */
	public static WeaselNetwork getNetwork(String networkName) {
		return localNetworks.get(networkName);
	}

	/**
	 * Create and register a network. Note that you must save this network to
	 * your NBT yourself.
	 * 
	 * @param name new network name
	 * @return the newly created network.
	 */
	public static WeaselNetwork createNetwork(String name) {
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
	public static WeaselNetwork destroyNetwork(String name) {
		return localNetworks.remove(name);
	}

	/**
	 * Connect a local network to the manager. This network might be loaded from
	 * a nbt tag.
	 * 
	 * @param name
	 * @param network
	 */
	public static void registerNetwork(String name, WeaselNetwork network) {
		localNetworks.put(name, network);
	}

	public static NetworkMember getPlugin(int id){
		for(NetworkMember nm:weaselPlugin){
			if(nm.id==id){
				return nm;
			}
		}
		return null;
	}
	
	public static void registerPlugin(NetworkMember plugin){
		if(weaselPlugin.contains(plugin))
			return;
		int bID = 1;
		boolean r=true;
		while(r){
			r=false;
			for(NetworkMember nm:weaselPlugin){
				if(bID==nm.id){
					bID++;
					r=true;
				}
			}
		}
		plugin.id = bID;
		weaselPlugin.add(plugin);
	}
	
	public static void removePlugin(NetworkMember plugin){
		if(weaselPlugin.contains(plugin))
			weaselPlugin.remove(plugin);
	}
	
	public static void removePlugin(int pluginID){
		NetworkMember plugin = getPlugin(pluginID);
		if(weaselPlugin.contains(plugin))
			weaselPlugin.remove(plugin);
	}
	
	/**
	 * Rename network, and tell all its members to rename their network names.
	 * Don't forget to change your network name, too. Network itself does not
	 * contain it.
	 * 
	 * @param name
	 * @param newName
	 */
	public static void renameNetwork(String name, String newName) {
		WeaselNetwork net = getNetwork(name);
		localNetworks.remove(name);
		localNetworks.put(newName, net);
		net.renamedTo(newName);
	}

	@Override
	public boolean needsSave() {
		boolean ns = needsSave;
		needsSave=false;
		if(!ns) {
			for(WeaselNetwork wn: localNetworks.values()){
				if(wn.needsSave()){
					ns=true;
					break;
				}
			}
		}
		return ns;
	}
	
	@Override
	public void handleIncomingPacket(World world, Object[] o) {
	}

}
