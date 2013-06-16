package powercraft.mobile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.NBTTagCompound;
import powercraft.api.gres.PC_GresTextEditMultiline.Keyword;
import powercraft.mobile.PCmo_Command.ParseException;

public class PCmo_MinerBrain implements PCmo_IMinerBrain {

	private PCmo_EntityMiner miner;
	private String program="";
	
	public PCmo_MinerBrain(PCmo_EntityMiner miner){
		this.miner = miner;
	}
	
	@Override
	public String getScriptName(){
		return "Easy Script";
	}
	
	@Override
	public void setProgram(String prog){
		program = prog;
	}
	
	@Override
	public String getProgram(){
		return program;
	}
	
	@Override
	public void restart() {
	}
	
	@Override
	public void launch() throws ParseException {
		restart();
		miner.appendCode(program);
	}
	
	private void setError(String error){
	}
	
	@Override
	public boolean hasError() {
		return getError()!=null;
	}
	
	@Override
	public String getError() {
		return null;
	}
	
	@Override
	public void run() {
	}
	
	@Override
	public List<Keyword> getKeywords() {
		return new ArrayList<Keyword>();
	}
	
	@Override
	public PCmo_MinerBrain readFromNBT(NBTTagCompound nbttag) {
		program = nbttag.getString("program");
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setString("program", program);
		return nbttag;
	}

	@Override
	public void compileProgram(String text) throws Exception {
	}

	@Override
	public void msg(Object[] obj) {}

	@Override
	public void onOpenGui() {
		// TODO Auto-generated method stub
		
	}
	
}
