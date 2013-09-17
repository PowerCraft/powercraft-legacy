/**
 * 
 */
package powercraft.api.blocks;

import java.util.Arrays;
import java.util.List;

import powercraft.api.inventory.PC_Inventory;
import powercraft.api.upgrade.PC_IUpgradeable;


/**
 * @author Aaron
 *
 */
public abstract class PC_TileEntityUpgradable extends PC_TileEntityWithInventory implements PC_IUpgradeable
{
	protected int[] upgradeList;
	/**
	 * @param name
	 * @param inventories
	 * @param slotsForID
	 */
	public PC_TileEntityUpgradable(String name, PC_Inventory[] inventories, int upgradeSlots, int[]... slotsForID) {
		super(name, addUpgradeInventory(inventories, upgradeSlots), slotsForID);
		
		upgradeList = new int[upgradeSlots];
	}
	
	private static PC_Inventory[] addUpgradeInventory(PC_Inventory[] inv, int slots){
		PC_Inventory upgrades = new PC_Inventory("upgrades", slots, 1, PC_Inventory.USEABLEBYPLAYER|PC_Inventory.DROPSTACKS);
		List<PC_Inventory> tmp;
		(tmp=Arrays.asList(inv)).add(upgrades);
		return (PC_Inventory[]) tmp.toArray();
	}
	
	public int[] getCurrentUpgrades()
	{
		return upgradeList;
	}
	/**
	 *  the GUI needs to pass an array of ID's of all upgrades currently set any time something is added or removed from the list 
	 */
	@Override	
	public boolean onUpgradesChanged(int[] upgradeIDs)
	{ 
		if (upgradeIDs.length > upgradeList.length)
		{
			return false;
		}
		else
		{			
			return true;
		}
	}
}
