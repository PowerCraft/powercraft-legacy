package powercraft.transport.blocks.tileEntities;

import powercraft.api.blocks.PC_TileEntity;
import powercraft.transport.blocks.blocks.PCtr_BeltTypeEnum;

public class PCtr_TEUpgradeableBelt extends PC_TileEntity
{
	private PCtr_BeltTypeEnum belttype;
	
	public PCtr_TEUpgradeableBelt()
	{
		belttype = PCtr_BeltTypeEnum.Standard;
	}
		
	@Override
	public int getLightOpacity()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
