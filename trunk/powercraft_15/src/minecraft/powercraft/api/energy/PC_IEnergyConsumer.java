package powercraft.api.energy;

import powercraft.api.PC_Direction;

public interface PC_IEnergyConsumer {

	public int getEnergyRequestInTick(PC_Direction side);
	
	public int receiveEnergy(PC_Direction side, int value);
	
}
