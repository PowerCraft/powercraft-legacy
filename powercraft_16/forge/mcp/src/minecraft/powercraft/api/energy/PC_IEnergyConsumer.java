package powercraft.api.energy;

import powercraft.core.blocks.PC_TileEntityRoaster;


public interface PC_IEnergyConsumer {

	public boolean canConsumerTubeConnectTo(int side);


	public float getEnergyRequest();


	public float consumeEnergy(float energy);

}
