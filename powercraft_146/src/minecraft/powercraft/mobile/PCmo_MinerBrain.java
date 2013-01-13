package powercraft.mobile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.mobile.PCmo_Command.ParseException;

public class PCmo_MinerBrain implements PCmo_IMinerBrain {

	private PCmo_EntityMiner miner;
	
	public PCmo_MinerBrain(PCmo_EntityMiner miner){
		this.miner = miner;
	}
	
	@Override
	public void setProgram(String prog){
	}
	
	@Override
	public String getProgram(){
		return "";
	}
	
	public void restart() {
	}
	
	public void launch() throws ParseException {
	}
	
	private void setError(String error){
	}
	
	public boolean hasError() {
		return getError()!=null;
	}
	
	public String getError() {
		return null;
	}
	
	public void run() {
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getProvidedVariableNames() {
		return new ArrayList<String>();
	}
	
	@Override
	public PCmo_MinerBrain readFromNBT(NBTTagCompound nbttag) {
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		return nbttag;
	}
	
}
