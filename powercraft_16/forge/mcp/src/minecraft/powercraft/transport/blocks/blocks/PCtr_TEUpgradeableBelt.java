/**
 * 
 */
package powercraft.transport.blocks.blocks;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.inventory.PC_Inventory;

/**
 * @author Aaron
 *
 */
public class PCtr_TEUpgradeableBelt extends PC_TileEntityUpgradable {

	public PCtr_TEUpgradeableBelt() {
		super(0, 3, "Belt", new PC_Inventory[0]);
	}

	/* (non-Javadoc)
	 * @see powercraft.api.blocks.PC_TileEntity#loadFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {
		
	}

	/* (non-Javadoc)
	 * @see powercraft.api.blocks.PC_TileEntity#saveToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void saveToNBT(NBTTagCompound nbtTagCompound) {
		
	}

	/**
	 * @param itemDamage
	 */
	public void setType(int itemDamage) {
		// TODO Auto-generated method stub
		
	}

}
