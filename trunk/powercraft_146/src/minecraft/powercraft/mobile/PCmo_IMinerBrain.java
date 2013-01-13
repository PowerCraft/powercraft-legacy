package powercraft.mobile;

import java.util.List;

import powercraft.management.PC_INBT;
import powercraft.mobile.PCmo_Command.ParseException;

public interface PCmo_IMinerBrain extends PC_INBT<PCmo_MinerBrain> {

	public void setProgram(String prog);
	public String getProgram();
	public void restart();
	public void launch() throws ParseException;
	public boolean hasError();
	public String getError();
	public void run();
	public List<String> getProvidedFunctionNames();
	public List<String> getProvidedVariableNames();
	
}
