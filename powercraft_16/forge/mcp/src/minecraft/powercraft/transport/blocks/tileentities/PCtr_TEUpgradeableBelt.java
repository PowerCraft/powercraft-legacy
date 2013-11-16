/**
 * 
 */
package powercraft.transport.blocks.tileentities;

import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.inventory.PC_Inventory;

/**
 * @author Aaron
 *
 */
public class PCtr_TEUpgradeableBelt extends PC_TileEntityUpgradable {

	public PCtr_TEUpgradeableBelt() {
		super(0, 3, "Belt", new PC_Inventory[0], new int[]{});
	}

	/**
	 * @param itemDamage
	 */
	public void setType(int itemDamage) {
		// TODO Auto-generated method stub
		
	}

}
