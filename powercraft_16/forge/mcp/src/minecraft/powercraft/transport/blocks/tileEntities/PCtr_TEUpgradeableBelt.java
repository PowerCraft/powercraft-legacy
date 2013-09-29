package powercraft.transport.blocks.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.blocks.PC_TileEntity;
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

	/* (non-Javadoc)
	 * @see powercraft.api.blocks.PC_TileEntity#loadFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see powercraft.api.blocks.PC_TileEntity#saveToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void saveToNBT(NBTTagCompound nbtTagCompound) {
		// TODO Auto-generated method stub
		
	}
}
