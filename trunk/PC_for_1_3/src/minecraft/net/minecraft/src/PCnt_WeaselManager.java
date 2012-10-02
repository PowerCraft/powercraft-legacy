package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import weasel.obj.WeaselObject;
import weasel.obj.WeaselObject.WeaselObjectType;

public class PCnt_WeaselManager extends PC_PacketHandler implements PC_INBTWD {

	private static List<PCnt_WeaselPlugin> weaselPlugins = new ArrayList<PCnt_WeaselPlugin>();
	private static List<PCnt_WeaselNetwork> weaselNetworks = new ArrayList<PCnt_WeaselNetwork>();
	
	private static boolean needsSave=false;
	
	public static PCnt_WeaselPlugin getPlugin(int id){
		for(PCnt_WeaselPlugin plugin:weaselPlugins){
			if(plugin.getID()==id)
				return plugin;
		}
		return null;
	}
	
	public static PCnt_TileEntityWeasel getTileEntity(PCnt_WeaselPlugin plugin){
		return plugin.getTileEntity();
	}
	
	public static int getFreeID(){
		int id=0;
		boolean loop = true;
		while(loop){
			loop = false;
			for(PCnt_WeaselPlugin plugin:weaselPlugins){
				if(plugin.getID()==id){
					loop = true;
					id++;
				}
			}
		}
		return id;
	}
	
	public static void registerPlugin(PCnt_WeaselPlugin plugin){
		if(!weaselPlugins.contains(plugin))
			weaselPlugins.add(plugin);
	}
	
	public static void removePlugin(PCnt_WeaselPlugin plugin){
		if(weaselPlugins.contains(plugin)){
			plugin.remove();
			weaselPlugins.remove(plugin);
		}
	}
	
	public static PCnt_WeaselNetwork getNetwork(String name){
		if(name==null||name.equals(""))
			return null;
		for(PCnt_WeaselNetwork network:weaselNetworks){
			if(name.equals(network.getName())){
				return network;
			}
		}
		return null;
	}
	
	public static void registerNetwork(PCnt_WeaselNetwork network){
		if(!weaselNetworks.contains(network))
			weaselNetworks.add(network);
	}
	
	public static void removeNetwork(PCnt_WeaselNetwork network){
		if(weaselNetworks.contains(network)){
			network.remove();
			weaselNetworks.remove(network);
		}
	}
	
	public void update(){
		for(PCnt_WeaselPlugin plugin:weaselPlugins){
			plugin.update();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		int size = weaselPlugins.size();
		tag.setInteger("plugins", size);
		for(int i=0; i<size; i++){
			PCnt_WeaselPlugin plugin = weaselPlugins.get(i);
			tag.setInteger("plugin["+i+"].type", plugin.getType());
			tag.setInteger("plugin["+i+"].id", plugin.getID());
			NBTTagCompound t = new NBTTagCompound();
			plugin.writeToNBT(t);
			tag.setCompoundTag("plugin["+i+"].value", t);
		}
		
		size = weaselNetworks.size();
		tag.setInteger("networks", size);
		for(int i=0; i<size; i++){
			PCnt_WeaselNetwork network = weaselNetworks.get(i);
			NBTTagCompound t = new NBTTagCompound();
			network.writeToNBT(t);
			tag.setCompoundTag("network["+i+"].value", t);
		}
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		for(PCnt_WeaselNetwork network:weaselNetworks){
			network.remove();
		}
		for(PCnt_WeaselPlugin plugin:weaselPlugins){
			PCnt_TileEntityWeasel tew = plugin.getTileEntity();
			plugin.remove();
			if(tew!=null){
				tew.releasePlugin();
			}
		}
		weaselPlugins.clear();
		weaselNetworks.clear();
		int size = tag.getInteger("plugins");
		for(int i=0; i<size; i++){
			int type = tag.getInteger("plugin["+i+"].type");
			int id = tag.getInteger("plugin["+i+"].id");
			PCnt_WeaselPlugin plugin = PCnt_WeaselPlugin.getPluginForType(type, id);
			plugin.readFromNBT(tag.getCompoundTag("plugin["+i+"].value"));
		}
		
		size = tag.getInteger("networks");
		for(int i=0; i<size; i++){
			PCnt_WeaselNetwork network = new PCnt_WeaselNetwork();
			network.readFromNBT(tag.getCompoundTag("plugin["+i+"].value"));
		}
		
		return this;
	}

	@Override
	public boolean needsSave() {
		if(needsSave){
			needsSave = false;
			return true;
		}
		return false;
	}

	@Override
	public void handleIncomingPacket(World world, Object[] o) {
		int id;
		switch((Integer)o[0]){
		case 0:
			id = (Integer)o[1];
			getPlugin(id).set((Object[])o[2]);
			break;
		}
	}
	
	public static void sendChange(PCnt_WeaselPlugin plugin, Object... o){
		sendChangeArray(plugin, o);
	}
	
	public static void sendChangeArray(PCnt_WeaselPlugin plugin, Object[] o){
		PC_Utils.sendToPacketHandler(null, "Weasel", 0, plugin.getID(), o);
	}

	public static WeaselObject getGlobalVariable(String string) {
		return null;
	}

	public static void setGlobalVariable(String string,
			WeaselObject weaselObject) {
		// TODO Auto-generated method stub
		
	}

	public static WeaselObjectType hasGlobalVariable(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
