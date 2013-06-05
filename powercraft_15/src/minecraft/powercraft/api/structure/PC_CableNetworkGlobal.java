package powercraft.api.structure;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.api.interfaces.PC_INBT;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_CableNetworkGlobal implements PC_INBT<PC_CableNetworkGlobal> {

	private int dimension;
	private List<PC_CableNetwork> cableNetworks = new ArrayList<PC_CableNetwork>();
	private int powerValue;
	private List<PC_VecI> chunkPoses;
	
	public PC_CableNetworkGlobal(){
		
	}
	
	public World getWorld() {
		return PC_Utils.mcs().worldServerForDimension(dimension);
	}

	public int getPowerValue() {
		return powerValue>0?powerValue-1:0;
	}
	
	public void setPowerValue(int value) {
		if(powerValue<value){
			powerValue = updateInputs();
			System.out.println("powerValue:"+powerValue);
			updateOutputs();
		}else if(powerValue>value){
			powerValue = value;
			updateOutputs();
		}
	}
	
	public void remove(PC_CableNetwork cableNetwork){
		cableNetworks.remove(cableNetwork);
	}
	
	public void add(PC_CableNetwork cableNetwork){
		cableNetworks.add(cableNetwork);
	}
	
	private void updateOutputs(){
		for(PC_CableNetwork cableNetwork:cableNetworks){
			cableNetwork.updateOutputs();
		}
	}
	
	private int updateInputs(){
		int maxPowerValue = 0;
		for(PC_CableNetwork cableNetwork:cableNetworks){
			cableNetwork.updateInputs();
			int mpv = cableNetwork.getMaxPowerValue();
			if(mpv>maxPowerValue){
				maxPowerValue = mpv;
			}
		}
		return maxPowerValue;
	}

	public void resolve(){
		if(chunkPoses!=null){
			System.out.println("resolve");
			for(PC_VecI chunkPos:chunkPoses){
				cableNetworks.add(PC_CableNetworkManager.getCableNetwork(getWorld(), new PC_VecI(chunkPos.x<<4, 0, chunkPos.y<<4), chunkPos.z));
			}
			for(PC_CableNetwork cableNetwork:cableNetworks){
				cableNetwork.setGlobalNetworkWithoutUpdate(this);
			}
			chunkPoses = null;
		}
	}
	
	@Override
	public PC_CableNetworkGlobal readFromNBT(NBTTagCompound nbttag) {
		dimension = nbttag.getInteger("dimension");
		chunkPoses = (List<PC_VecI>) PC_Utils.loadFromNBT(nbttag, "chunkPoses");
		powerValue = nbttag.getInteger("powerValue");
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		List<PC_VecI> chunkPoses = new ArrayList<PC_VecI>();
		for(PC_CableNetwork cableNetwork:cableNetworks){
			PC_VecI chunkPos = cableNetwork.getChunkPos();
			chunkPoses.add(new PC_VecI(chunkPos.x, chunkPos.y, cableNetwork.getID()));
		}
		nbttag.setInteger("dimension", dimension);
		PC_Utils.saveToNBT(nbttag, "chunkPoses", chunkPoses);
		nbttag.setInteger("powerValue", powerValue);
		return nbttag;
	}
	
}
