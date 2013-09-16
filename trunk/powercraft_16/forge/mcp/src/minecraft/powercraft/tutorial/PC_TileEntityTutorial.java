package powercraft.tutorial;

import net.minecraft.item.ItemStack;
import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.inventory.PC_Inventory;
import powercraft.api.upgrade.PC_ItemUpgrade;
import powercraft.api.upgrade.PC_Upgrade;
import powercraft.api.upgrade.PC_UpgradeFunction;

public class PC_TileEntityTutorial extends PC_TileEntityUpgradable {
	int speed=1;
	
	public PC_TileEntityTutorial() {
		super("Tutorial", new PC_Inventory[]{}, 3);
	}
	
	static{
		registerUpgradeFunction("UpgradeA", new PC_UpgradeFunction.Impl(){

			@Override
			public void onPlaced(PC_TileEntityUpgradable te) {
				((PC_TileEntityTutorial)te).speed=2;
			}

			@Override
			public void onRemoved(PC_TileEntityUpgradable te) {
				((PC_TileEntityTutorial)te).speed=1;
			}
			
		});
	}

	/* (non-Javadoc)
	 * @see powercraft.api.upgrade.PC_IUpgradeable#onUpgradePlaced(int)
	 */
	@Override
	public void onUpgradePlaced(int slot) {
		currentUpgradeFunctions[slot].onPlaced(this);
	}

	/* (non-Javadoc)
	 * @see powercraft.api.upgrade.PC_IUpgradeable#onUpgradeRemoved(int)
	 */
	@Override
	public void onUpgradeRemoved(int slot) {
		currentUpgradeFunctions[slot].onRemoved(this);
		currentUpgradeFunctions[slot]=null;
	}

	
	
}
