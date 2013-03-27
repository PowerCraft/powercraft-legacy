package powercraft.api.energy;

import powercraft.api.PC_Direction;

public interface PC_IEnergyProvider {

	public int getMaxEnergyForUseInTick(PC_Direction side);
	
	public int decreaseEnergy(PC_Direction side, int value);
	
}
