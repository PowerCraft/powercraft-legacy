package powercraft.transport.blocks;

import powercraft.api.PC_Direction;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_TextureRegistry;

@PC_BlockInfo(name = "ElevatorDown", blockid = "elevatordown", defaultid = 2061)
public class PCtr_BlockClassicElevatorDown extends PCtr_BlockClassicElevator
{
	public PCtr_BlockClassicElevatorDown(int id)
	{
		super(id);
		this.elevatorDirection = PC_Direction.DOWN;
	}
	
	@Override
	public void loadIcons()
	{		
		this.blockIcon = PC_TextureRegistry.registerIcon("elevatordown");
	}
	
	@Override
	public void registerRecipes()
	{
		// TODO Auto-generated method stub
	}
}
