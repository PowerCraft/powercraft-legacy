package powercraft.api.multiblock.redstone;

import powercraft.api.utils.PC_Direction;

public interface PC_IRedstoneConnection {

	public int getRedstonePowerValue(PC_Direction side, int side2, int cable);
	
	public int getConnectionMask(PC_Direction side, int side2);
	
}
