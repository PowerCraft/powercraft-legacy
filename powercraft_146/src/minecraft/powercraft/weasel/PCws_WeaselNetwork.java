package powercraft.weasel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Color;
import powercraft.management.PC_INBT;
import powercraft.management.PC_Utils.SaveHandler;
import weasel.obj.WeaselVariableMap;

public class PCws_WeaselNetwork implements Iterable<PCws_WeaselPlugin>, PC_INBT<PCws_WeaselNetwork> {

	private int id;
	private String name;
	private PC_Color color = new PC_Color();
	private List<Integer> members = new ArrayList<Integer>();
	/** Local shared variable pool */
	public WeaselVariableMap localHeap = new WeaselVariableMap();
	private boolean needsSave = false;
	
	public PCws_WeaselNetwork(){
		id = PCws_WeaselManager.registerNetwork(this);
	}
	
	@Override
	public PCws_WeaselNetwork readFromNBT(NBTTagCompound nbttag) {
		PCws_WeaselManager.removeNetwork(this);
		id = nbttag.getInteger("id");
		PCws_WeaselManager.registerNetwork(this, id);
		name = nbttag.getString("name");
		SaveHandler.loadFromNBT(nbttag, "color", color);
		int num = nbttag.getInteger("count");
		members.clear();
		for(int i=0; i<num; i++){
			members.add(nbttag.getInteger("value["+i+"]"));
		}
		SaveHandler.loadFromNBT(nbttag, "localHeap", localHeap);
		return this;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		needsSave = false;
		nbttag.setInteger("id", id);
		nbttag.setString("name", name);
		SaveHandler.saveToNBT(nbttag, "color", color);
		nbttag.setInteger("count", members.size());
		int i=0;
		for(Integer mem:members){
			nbttag.setInteger("value["+i+"]", mem);
		}
		SaveHandler.saveToNBT(nbttag, "localHeap", localHeap);
		return nbttag;
	}
	
	public boolean needSave(){
		return needsSave;
	}
	
	public int getID(){
		return id;
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
			PCws_WeaselPlugin member = PCws_WeaselManager.getPlugin(i);
			if(member!=null){
				member.removeFormNetwork();
			}
		}
	}
	
	public PCws_WeaselPlugin getMember(String name){
		for(Integer i:members){
			PCws_WeaselPlugin member = PCws_WeaselManager.getPlugin(i);
			if(member!=null){
				if(name.equals(member.getName())){
					return member;
				}
			}
		}
		return null;
	}
	
	public void registerMember(PCws_WeaselPlugin member){
		if(!members.contains(member.getID()))
			members.add(member.getID());
	}
	
	public void removeMember(PCws_WeaselPlugin member){
		int id = member.getID();
		if(members.contains(id)){
			member.removeFormNetwork();
			members.remove((Integer)id);
		}
	}
	
	@Override
	public Iterator<PCws_WeaselPlugin> iterator() {
		return new WeaselNetworkIterator();
	}
	
	private class WeaselNetworkIterator implements Iterator<PCws_WeaselPlugin>{

		private int i=0;
		
		@Override
		public boolean hasNext() {
			return members.size()>i;
		}

		@Override
		public PCws_WeaselPlugin next() {
			PCws_WeaselPlugin wp = PCws_WeaselManager.getPlugin(members.get(i));
			i++;
			return wp;
		}

		@Override
		public void remove() {
			i--;
			members.remove(i);
		}
		
	}
	
}
