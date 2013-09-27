package powercraft.api.energy;



public interface PC_IEnergyConsumer {

	public boolean canConsumerTubeConnectTo(int side);


	public float getEnergyRequest();


	public float consumeEnergy(float energy);

}
