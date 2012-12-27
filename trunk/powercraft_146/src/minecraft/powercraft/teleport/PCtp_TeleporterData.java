package powercraft.teleport;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_INBT;
import powercraft.management.PC_VecI;

public class PCtp_TeleporterData implements PC_INBT {

	public static final int N=0, E=1, S=2, W=3;
	
	public String name;
	public boolean animals;
	public boolean monsters;
	public boolean items;
	public boolean players;
	public boolean sneakTrigger;
	public PC_VecI defaultTarget;
	public int direction;
	
	public PCtp_TeleporterData(){
		name="";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttag) {
		// TODO Auto-generated method stub
		
	}

}
