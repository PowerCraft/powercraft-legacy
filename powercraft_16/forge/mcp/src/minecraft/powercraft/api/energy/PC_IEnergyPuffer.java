package powercraft.api.energy;

import powercraft.api.PC_Direction;

/**
 * 
 * For TileEntities that can buffer energy
 * 
 * @author XOR
 *
 */
public interface PC_IEnergyPuffer extends PC_IEnergyConsumer, PC_IEnergyProvider {

	/**
	 * 
	 * Amount of power the block saves
	 * 
	 * @param side later side of buffer, in the moment unused, always null
	 * @return energy level of the block
	 */
	public float getEnergyLevel(PC_Direction side);

}
