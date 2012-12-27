package powercraft.teleport;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_INBT;
import powercraft.management.PC_VecI;

public class PCtp_TeleporterData implements PC_INBT {

	public String name;
	public boolean animals;
	public boolean monsters;
	public boolean items;
	public boolean players;
	public boolean sneakTrigger;
	public PC_VecI defaultTarget;
	
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
