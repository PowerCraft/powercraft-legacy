/**
 * 
 */
package powercraft.api.upgrade;

/**
 * @author Aaron
 *
 */
public interface PC_IUpgradeable {

	public boolean onTryToPlaceUpgrade(int slot);
	
	public void onUpgradePlaced(int slot);
	
	public void onUpgradeRemoved(int slot);
}