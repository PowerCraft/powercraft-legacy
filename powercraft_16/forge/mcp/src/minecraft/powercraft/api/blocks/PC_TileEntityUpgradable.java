/**
 * 
 */
package powercraft.api.blocks;

import java.util.Arrays;
import java.util.List;

import powercraft.api.inventory.PC_Inventory;
import powercraft.api.upgrade.PC_IUpgradeable;
import powercraft.api.upgrade.PC_ItemUpgrade;
import powercraft.api.upgrade.PC_UpgradeFamily;


/**
 * @author Aaron
 *
 */
public abstract class PC_TileEntityUpgradable extends PC_TileEntity implements PC_IUpgradeable
{
	protected int validFamilies;
	protected PC_ItemUpgrade[] upgradeList; // list of ItemUpgrade objects.

	
	public PC_TileEntityUpgradable(int vfamilies, int upgradeSlots)
	{				
		upgradeList = new PC_ItemUpgrade[upgradeSlots];
		validFamilies = vfamilies;
	}
	
	private static PC_Inventory[] addUpgradeInventory(PC_Inventory[] inv, int slots)
	{
		PC_Inventory upgrades = new PC_Inventory("upgrades", slots, 1, PC_Inventory.USEABLEBYPLAYER|PC_Inventory.DROPSTACKS);
		List<PC_Inventory> tmp;
		(tmp=Arrays.asList(inv)).add(upgrades);
		return (PC_Inventory[]) tmp.toArray();
	}
	
	public PC_ItemUpgrade[] getCurrentUpgrades()
	{
		return upgradeList;
	}
	/**
	 *  the GUI needs to pass an array of ID's of all upgrades currently set any time something is added or removed from the list 
	 */
	@Override	
	public boolean onUpgradesChanged(PC_ItemUpgrade[] upgrades)
	{ 
		if (upgrades.length <= upgradeList.length)
		{
			return true;
		}
		else
		{			
			return false;
		}
	}
	
	/**
	 * Checks to see if any of the upgrades currently installed are Security Upgrades 
	 * @return true if Security upgrade is installed
	 */
	public boolean hasSecurityUpgrade()
	{
		for (PC_ItemUpgrade upg : upgradeList)
		{
			if (upg.getUpgradeFamily() == PC_UpgradeFamily.Security)
			{
				return true;
			}
		}
		return false;
	}	
}
