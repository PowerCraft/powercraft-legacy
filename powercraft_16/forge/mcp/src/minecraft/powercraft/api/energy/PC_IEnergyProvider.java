package powercraft.api.energy;

import powercraft.api.PC_Direction;

/**
 * 
 * For TileEntities that can provide energy
 * 
 * @author XOR
 *
 */
public interface PC_IEnergyProvider {

	/**
	 * 
	 * checks if a tube can connect to a special side of the block
	 * 
	 * @param side to check
	 * @return result of check
	 */
	public boolean canProviderTubeConnectTo(PC_Direction side);

	/**
	 * 
	 * value of energy that can be given for the next tick
	 * 
	 * @param side later side of output, in the moment unused, always null
	 * @return value of energy
	 */
	public float getEnergyForUsage(PC_Direction side);

	/**
	 * 
	 * called to get a special amount of energy
	 * 
	 * @param side later side of output, in the moment unused, always null
	 * @param energy expect to get
	 * @return energy to use
	 */
	public float getEnergy(PC_Direction side, float energy);

}
