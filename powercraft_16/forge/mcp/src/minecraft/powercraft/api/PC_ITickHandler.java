package powercraft.api;

/**
 * 
 * the tick handler
 * 
 * @author XOR
 *
 */
public interface PC_ITickHandler {

	/**
	 * called when the tick started
	 * @param tickData extra datas for the tick
	 */
	public void tickStart(Object[] tickData);

	/**
	 * called when the tick ended
	 * @param tickData extra datas for the tick
	 */
	public void tickEnd(Object[] tickData);

}
