package powercraft.tutorial;

import net.minecraft.item.ItemStack;
import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.inventory.PC_Inventory;
import powercraft.api.upgrade.PC_ItemUpgrade;
import powercraft.api.upgrade.PC_Upgrade;
import powercraft.api.upgrade.PC_UpgradeFunction;

public class PC_TileEntityTutorial extends PC_TileEntityUpgradable {

	
	public PC_TileEntityTutorial() {
		super("Tutorial", new PC_Inventory[]{}, 3);
	}

	/* (non-Javadoc)
	 * @see powercraft.api.upgrade.PC_IUpgradeable#onUpgradePlaced(int)
	 */
	@Override
	public void onUpgradePlaced(int slot) {
		currentUpgradeFunctions[slot].onPlaced();
	}

	/* (non-Javadoc)
	 * @see powercraft.api.upgrade.PC_IUpgradeable#onUpgradeRemoved(int)
	 */
	@Override
	public void onUpgradeRemoved(int slot) {
		currentUpgradeFunctions[slot].onRemoved();
		currentUpgradeFunctions[slot]=null;
	}

	
	
}
