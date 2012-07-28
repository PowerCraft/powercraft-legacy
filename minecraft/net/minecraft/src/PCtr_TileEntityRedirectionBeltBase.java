package net.minecraft.src;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;


/**
 * Redirectable belt tile entity
 * 
 * @author MightyPork
 */
public abstract class PCtr_TileEntityRedirectionBeltBase extends PC_TileEntity {

	/** Random number generator */
	protected Random rand = new Random();

	/**
	 * 
	 */
	public PCtr_TileEntityRedirectionBeltBase() {}

	@Override
	public final boolean canUpdate() {
		return true;
	}

	private Hashtable<Entity, Integer> redirList = new Hashtable<Entity, Integer>();

	@Override
	public final void updateEntity() {
		// remove items from the list that are too far

		Enumeration<Entity> enumer = redirList.keys();
		while (enumer.hasMoreElements()) {

			Entity thisItem = enumer.nextElement();

			if (thisItem.posX < xCoord - 0.2F || thisItem.posY < yCoord - 0.2F || thisItem.posZ < zCoord - 0.2F || thisItem.posX > xCoord + 1.2F
					|| thisItem.posY > yCoord + 2.2F || thisItem.posZ > zCoord + 1.2F) {
				redirList.remove(thisItem);
			}
		}
	}

	/**
	 * Get relative direction for entity (-1,0,1)
	 * 
	 * @param entity the entity
	 * @return direction (-1,0,1)
	 */
	public final int getDirection(Entity entity) {

		if (redirList.containsKey(entity)) {
			return redirList.get(entity);
		} else {
			return calculateItemDirection(entity);
		}

	}

	/**
	 * Calculate entity direction. When done, you must store it into the map
	 * using setItemDirection().
	 * 
	 * @param entity the entity item (or other)
	 * @return direction
	 */
	protected abstract int calculateItemDirection(Entity entity);

	/**
	 * Set calculated direction into the map.
	 * 
	 * @param entity
	 * @param direction
	 */
	protected final void setItemDirection(Entity entity, int direction) {
		redirList.put(entity, direction);
	}

}
