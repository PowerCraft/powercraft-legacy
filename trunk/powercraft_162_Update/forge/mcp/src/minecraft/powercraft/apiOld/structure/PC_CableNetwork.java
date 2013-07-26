package powercraft.api.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.api.interfaces.PC_INBT;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_CableNetwork implements PC_INBT<PC_CableNetwork> {

	private HashMap<Integer, Integer> connections = new HashMap<Integer, Integer>();
	private List<Integer> ios = new ArrayList<Integer>();
	private List<Integer> outs = new ArrayList<Integer>();
	private int maxPowerValue;
	private PC_CableNetworkChunk chunk;
	private PC_CableNetworkGlobal global;
	private int count;
	
	public PC_CableNetwork(){
		
	}
	
	public PC_CableNetwork(PC_CableNetworkChunk chunk, PC_CableNetworkGlobal global){
		this.chunk = chunk;
		this.global = global;
		global.add(this);
	}
	
	private int vec2Int(PC_VecI pos){
		return (pos.x&15) | ((pos.z&15)<<4) | (pos.y<<8);
	}
	
	private PC_VecI int2Vec(int i){
		return new PC_VecI(i&15, i>>8, (i>>4)&15);
	}
	
	public void updateInputs(){
		maxPowerValue = 0;
		PC_VecI offset = getChunkPos().copy();
		offset.z = offset.y<<4;
		offset.y = 0;
		offset.x <<= 4;
		for(Integer input:ios){
			PC_VecI vec = int2Vec(input).offset(offset);
			int inputPowerValue = PC_BlockStructure.structure.getRedstonePowereValueEx(getWorld(), vec.x, vec.y, vec.z);
			System.out.println(inputPowerValue);
			if(inputPowerValue>maxPowerValue){
				maxPowerValue = inputPowerValue;
			}
		}
		System.out.println("maxPowerValue:"+maxPowerValue);
	}
	
	public void updateOutputs(){
		World world = getWorld();
		PC_VecI offset = getChunkPos().copy();
		offset.z = offset.y<<4;
		offset.y = 0;
		offset.x <<= 4;
		for(Integer out:ios){
			PC_VecI vec = int2Vec(out).offset(offset);
			world.notifyBlocksOfNeighborChange(vec.x, vec.y, vec.z, PC_BlockStructure.structure.blockID);
		}
		for(Integer out:outs){
			PC_VecI vec = int2Vec(out).offset(offset);
			world.notifyBlocksOfNeighborChange(vec.x, vec.y, vec.z, PC_BlockStructure.structure.blockID);
		}
	}

	private World getWorld() {
		return global.getWorld();
	}
	
	public int getMaxPowerValue(){
		return maxPowerValue;
	}
	
	public int getPowerValue(){
		return global.getPowerValue();
	}
	
	public void setPowerValue(int value){
		global.setPowerValue(value);
	}
	
	public void setGlobalNetwork(PC_CableNetworkGlobal global){
		if(this.global != global){
			if(this.global!=null)
				this.global.remove(this);
			global.add(this);
			this.global = global;
			for(Entry<Integer, Integer> e:connections.entrySet()){
				int i = e.getKey();
				int s = i&3;
				PC_VecI pos = chunk.getChunkPos();
				pos = pos.offset(s==0?-1:s==1?1:0, s==2?-1:s==3?1:0, 0);
				PC_CableNetwork otherNetwork = PC_CableNetworkManager.getCableNetwork(getWorld(), new PC_VecI(pos.x, 0, pos.y), e.getValue());
				otherNetwork.setGlobalNetwork(global);
			}
		}
	}

	@Override
	public PC_CableNetwork readFromNBT(NBTTagCompound nbttag) {
		connections = (HashMap<Integer, Integer>) PC_Utils.loadFromNBT(nbttag, "connections");
		ios = (List<Integer>) PC_Utils.loadFromNBT(nbttag, "ios");
		outs = (List<Integer>) PC_Utils.loadFromNBT(nbttag, "outs");
		maxPowerValue = nbttag.getInteger("maxPowerValue");
		chunk = PC_CableNetworkChunk.currentReader;
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		PC_Utils.saveToNBT(nbttag, "connections", connections);
		PC_Utils.saveToNBT(nbttag, "ios", ios);
		PC_Utils.saveToNBT(nbttag, "outs", outs);
		nbttag.setInteger("maxPowerValue", maxPowerValue);
		return nbttag;
	}
	
	public void setGlobalNetworkWithoutUpdate(PC_CableNetworkGlobal global){
		System.out.println("setGlobalNetworkWithoutUpdate:"+global);
		this.global = global;
	}

	public PC_VecI getChunkPos() {
		return chunk.getChunkPos();
	}

	public int getID() {
		return chunk.getID(this);
	}

	public void addRef() {
		count++;
	}

	public void release(PC_VecI pos) {
		count--;
		ios.remove((Object)vec2Int(pos));
		outs.remove((Object)vec2Int(pos));
	}

	public void addIO(PC_VecI pos) {
		int i = vec2Int(pos);
		if(!ios.contains(i))
			ios.add(vec2Int(pos));
	}
	
}
