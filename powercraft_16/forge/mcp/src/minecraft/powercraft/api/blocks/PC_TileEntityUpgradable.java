/**
 * 
 */
package powercraft.api.blocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import powercraft.api.inventory.PC_Inventory;
import powercraft.api.upgrade.PC_IUpgradeable;
import powercraft.api.upgrade.PC_ItemUpgrade;
import powercraft.api.upgrade.PC_Upgrade;
import powercraft.api.upgrade.PC_UpgradeFunction;


/**
 * @author Aaron
 *
 */
public abstract class PC_TileEntityUpgradable extends PC_TileEntityWithInventory implements PC_IUpgradeable{

	public PC_UpgradeFunction[] currentUpgradeFunctions;
	private static final Map<String, PC_UpgradeFunction> upgradeFunctions = new HashMap<String, PC_UpgradeFunction>();

	/**
	 * @param name
	 * @param inventories
	 * @param slotsForID
	 */
	public PC_TileEntityUpgradable(String name, PC_Inventory[] inventories,
			int upgradeSlots, int[]... slotsForID) {
		super(name, addUpgradeInventory(inventories, upgradeSlots), slotsForID);
		currentUpgradeFunctions = new PC_UpgradeFunction[upgradeSlots];
	}
	
	private static PC_Inventory[] addUpgradeInventory(PC_Inventory[] inv, int slots){
		PC_Inventory upgrades = new PC_Inventory("upgrades", slots, 1, PC_Inventory.USEABLEBYPLAYER|PC_Inventory.DROPSTACKS);
		List<PC_Inventory> tmp;
		(tmp=Arrays.asList(inv)).add(upgrades);
		return (PC_Inventory[]) tmp.toArray();
	}
	
	

	@Override
	public boolean onTryToPlaceUpgrade(int slot) {
		PC_Inventory upgradeInventory = getSubInventoryByName("upgrades");
		ItemStack upgrade = upgradeInventory.getStackInSlot(slot);
		if(upgrade==null || !(upgrade.getItem() instanceof PC_ItemUpgrade)) return false;
		return setUpgradeFunctionToSlot(slot, PC_Upgrade.getUpgradeName(((PC_ItemUpgrade)upgrade.getItem()).getUpgradeType()));
	}

	protected static final void registerUpgradeFunction(String upgrade, PC_UpgradeFunction func){
		upgradeFunctions.put(upgrade, func);
	}
	
	protected final boolean setUpgradeFunctionToSlot(int slot, String name){
		PC_UpgradeFunction tmp = upgradeFunctions.get(name);
		currentUpgradeFunctions[slot] = tmp;
		return tmp!=null;		
	}

}
