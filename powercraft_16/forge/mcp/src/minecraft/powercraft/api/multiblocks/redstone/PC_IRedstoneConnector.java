package powercraft.api.multiblocks.redstone;

import powercraft.api.PC_Direction;

public interface PC_IRedstoneConnector {

	public int canConnectTo(PC_Direction dir, PC_Direction dir2, int cables);
	
	public int getRedstoneValue(PC_Direction dir, PC_Direction dir2, int cable);
	
	public void setRedstoneValue(PC_Direction dir, PC_Direction dir2, int cable, int value);
	
}
