package powercraft.api.energy;

import powercraft.api.PC_Direction;


/**
 * 
 * For TileEntities that can consume energy
 * 
 * @author XOR
 *
 */
public interface PC_IEnergyConsumer {

	/**
	 * 
	 * checks if a tube can connect to a special side of the block
	 * 
	 * @param side to check
	 * @return result of check
	 */
	public boolean canConsumerTubeConnectTo(PC_Direction side);

	/**
	 * 
	 * value of needed energy for the next tick
	 * 
	 * @param side later side of input, in the moment unused, always null
	 * @return value of energy
	 */
	public float getEnergyRequest(PC_Direction side);

	/**
	 * 
	 * called to consume a special energy value
	 * 
	 * @param side later side of input, in the moment unused, always null
	 * @param energy to consume
	 * @return not needed energy, will maybe disappear
	 */
	public float consumeEnergy(PC_Direction side, float energy);

}
