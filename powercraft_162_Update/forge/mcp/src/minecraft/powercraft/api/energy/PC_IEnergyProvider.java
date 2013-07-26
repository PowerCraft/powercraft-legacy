package powercraft.api.energy;


public interface PC_IEnergyProvider {

	public boolean canProviderTubeConnectTo(int side);


	public float getEnergyForUsage();


	public float getEnergy(float energy);

}
