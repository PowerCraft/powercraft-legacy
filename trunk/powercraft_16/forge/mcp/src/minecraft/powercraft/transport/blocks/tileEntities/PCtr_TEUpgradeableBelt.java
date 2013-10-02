package powercraft.transport.blocks.tileEntities;

import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.inventory.PC_Inventory;
import powercraft.transport.blocks.blocks.PCtr_BeltTypeEnum;

public class PCtr_TEUpgradeableBelt extends PC_TileEntityUpgradable
{
	private PCtr_BeltTypeEnum belttype;
	
	public PCtr_TEUpgradeableBelt()
	{
		super(0, 3, "Belt", new PC_Inventory[0]);
		belttype = PCtr_BeltTypeEnum.Standard;
	}
		
	@Override
	public int getLightOpacity()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
