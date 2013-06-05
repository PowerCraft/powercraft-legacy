package powercraft.api.structure;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.interfaces.PC_INBT;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_CableNetworkChunk implements PC_INBT<PC_CableNetworkChunk> {

	private HashMap<Integer, PC_CableNetwork> cableNetworks = new HashMap<Integer, PC_CableNetwork>();
	private PC_VecI chunkPos = new PC_VecI();
	public static PC_CableNetworkChunk currentReader;
	
	public PC_CableNetworkChunk(){
		
	}
	
	public PC_CableNetworkChunk(PC_VecI pos) {
		chunkPos = pos.copy();
	}

	public PC_CableNetwork getCableNetwork(int id){
		return cableNetworks.get(id);
	}
	
	public PC_VecI getChunkPos(){
		return chunkPos;
	}

	@Override
	public PC_CableNetworkChunk readFromNBT(NBTTagCompound nbttag) {
		currentReader=this;
		cableNetworks = (HashMap<Integer, PC_CableNetwork>) PC_Utils.loadFromNBT(nbttag, "cableNetworks");
		currentReader=null;
		PC_Utils.loadFromNBT(nbttag, "chunkPos", chunkPos);
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		PC_Utils.saveToNBT(nbttag, "cableNetworks", cableNetworks);
		PC_Utils.saveToNBT(nbttag, "chunkPos", chunkPos);
		return nbttag;
	}

	public int getID(PC_CableNetwork cableNetwork) {
		for(Entry<Integer, PC_CableNetwork> e:cableNetworks.entrySet()){
			if(e.getValue()==cableNetwork){
				return e.getKey(); 
			}
		}
		return 0;
	}

	public int createNewCableNetwork(PC_CableNetworkGlobal global) {
		int id = 1;
		boolean again;
		do{
			again=false;
			for(Integer i:cableNetworks.keySet()){
				if(i==id){
					id++;
					again=true;
				}
			}
		}while(again);
		cableNetworks.put(id, new PC_CableNetwork(this, global));
		return id;
	}
	
}
