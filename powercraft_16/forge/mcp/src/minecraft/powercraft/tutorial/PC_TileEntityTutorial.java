package powercraft.tutorial;

import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.inventory.PC_Inventory;

public class PC_TileEntityTutorial extends PC_TileEntityUpgradable {
	double speed=1;
	
	public PC_TileEntityTutorial() {
		super("Tutorial", new PC_Inventory[]{}, 3);
	}
}
