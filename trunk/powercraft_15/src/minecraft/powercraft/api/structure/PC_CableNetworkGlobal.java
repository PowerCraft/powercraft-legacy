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

public class PC_CableNetworkGlobal implements PC_INBT<PC_CableNetworkGlobal>{

	private int dimension;
	private HashMap<PC_VecI, PC_CableNetworkChunk> chunks = new HashMap<PC_VecI, PC_CableNetworkChunk>();
	private int powerValue;
	private int cable;
	private int repeat;
	
	public PC_CableNetworkGlobal(){
		
	}
	
	public PC_CableNetworkGlobal(World world, int cable){
		dimension = world.getWorldInfo().getDimension();
		this.cable = cable;
	}
	
	private PC_VecI getChunkPos(PC_VecI pos){
		return new PC_VecI(pos.x>>4, pos.z>>4);
	}
	
	public void addIO(PC_VecI pos){
		PC_VecI chunkPos = getChunkPos(pos);
		PC_CableNetworkChunk chunk = chunks.get(chunkPos);
		chunk.addIO(pos.sub(chunkPos.x<<4, 0, chunkPos.z<<4));
	}
	
	public void removeIO(PC_VecI pos){
		PC_VecI chunkPos = getChunkPos(pos);
		PC_CableNetworkChunk chunk = chunks.get(chunkPos);
		chunks.remove(chunkPos);
	}
	
	public void setPowerValue(int value){
		if(value==powerValue)
			return;
		/*if(ioToAdd!=null){
			repeat = repeat<value?repeat:value;
			return;
		}*/
		if(value>powerValue){
			powerValue = value;
			updateOutputs();
		}else{
			do{
				repeat = 15;
				updatePowerValue();
			}while(repeat<powerValue);
		}
		repeat = 0;
	}
	
	public int getPowerValue(){
		return powerValue==0?0:powerValue-1;
	}
	
	public void addRef(PC_VecI pos){
		PC_VecI chunkPos = getChunkPos(pos);
		PC_CableNetworkChunk chunk = chunks.get(chunkPos);
		chunk.addRef();
	}
	
	public boolean release(PC_VecI pos){
		PC_VecI chunkPos = getChunkPos(pos);
		PC_CableNetworkChunk chunk = chunks.get(chunkPos);
		if(chunk.release()){
			chunks.remove(chunkPos);
		}
		return chunks.isEmpty();
	}
	
	public void updatePowerValue(){
		powerValue = 0;
		World world = getWorld();
		for(Entry<PC_VecI, PC_CableNetworkChunk> chunk:chunks.entrySet()){
			PC_VecI chunkPos = chunk.getKey();
			if(world.blockExists(chunkPos.x<<4, 0, chunkPos.y<<4)){
				chunk.getValue().updatePowerValue();
			}
		}
		updateOutputs();
	}
	
	public void updateOutputs(){
		World world = getWorld();
		for(Entry<PC_VecI, PC_CableNetworkChunk> chunk:chunks.entrySet()){
			PC_VecI chunkPos = chunk.getKey();
			if(world.blockExists(chunkPos.x<<4, 0, chunkPos.y<<4)){
				chunk.getValue().updateOutputs();
			}
		}
	}
	
	public World getWorld(){
		return PC_Utils.mcs().worldServerForDimension(dimension);
	}

	@Override
	public PC_CableNetworkGlobal readFromNBT(NBTTagCompound nbttag) {
		dimension = nbttag.getInteger("dimension");
		powerValue = nbttag.getInteger("powerValue");
		cable = nbttag.getInteger("cable");
		chunks = (HashMap<PC_VecI, PC_CableNetworkChunk>)PC_Utils.loadFromNBT(nbttag, "chunks");
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setInteger("dimension", dimension);
		nbttag.setInteger("powerValue", powerValue);
		nbttag.setInteger("cable", cable);
		PC_Utils.saveToNBT(nbttag, "chunks", chunks);
		return nbttag;
	}
	
}
