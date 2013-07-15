package powercraft.api.energy;


public interface PC_IEnergyConsumer {

	public float getEnergyRequest();
	public float consumeEnergy(float energy);
	
}
