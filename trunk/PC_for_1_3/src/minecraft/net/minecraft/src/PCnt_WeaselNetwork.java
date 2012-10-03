package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class PCnt_WeaselNetwork implements PC_INBTWD {

	private String name;
	private PC_Color color;
	private List<Integer> members = new ArrayList<Integer>();
	private boolean needsSave = false;
	
	public PCnt_WeaselNetwork(){
		PCnt_WeaselManager.registerNetwork(this);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setColor(PC_Color color) {
		this.color = color;
	}
	
	public PC_Color getColor() {
		return color;
	}
	
	public void remove() {
		for(Integer i:members){
			PCnt_WeaselPlugin member = PCnt_WeaselManager.getPlugin(i);
			if(member!=null){
				member.removeFormNetwork();
			}
		}
	}
	
	public PCnt_WeaselPlugin getMember(String name){
		for(Integer i:members){
			PCnt_WeaselPlugin member = PCnt_WeaselManager.getPlugin(i);
			if(member!=null){
				if(name.equals(member.getName())){
					return member;
				}
			}
		}
		return null;
	}
	
	public void registerMember(PCnt_WeaselPlugin member){
		if(!members.contains(member.getID()))
			members.add(member.getID());
	}
	
	public void removeMember(PCnt_WeaselPlugin member){
		int id = member.getID();
		if(members.contains(id)){
			member.removeFormNetwork();
			members.remove((Integer)id);
		}
	}
	
	public PCnt_WeaselPlugin[] iterator(){
		PCnt_WeaselPlugin[] plugin = new PCnt_WeaselPlugin[members.size()];
		int i=0;
		for(Integer member:members){
			plugin[i] = PCnt_WeaselManager.getPlugin(member);
		}
		return plugin;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("name", name);
		int[] array = new int[members.size()];
		for(int i=0; i<array.length; i++){
			array[i] = members.get(i);
		}
		tag.setIntArray("members", array);
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		for(Integer i:members){
			PCnt_WeaselPlugin member = PCnt_WeaselManager.getPlugin(i);
			if(member!=null){
				member.removeFormNetwork();
			}
		}
		members.clear();
		name = tag.getString("name");
		int[] array = tag.getIntArray("members");
		for(int i=0; i<array.length; i++){
			PCnt_WeaselManager.getPlugin(array[i]).setNetworkNameAndConnect(name);
		}
		return this;
	}

	@Override
	public boolean needsSave() {
		boolean ns = needsSave;
		needsSave = false;
		return ns;
	}

	
	
}
