package powercraft.tutorial;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.inventory.PC_Inventory;
import powercraft.api.upgrade.PC_UpgradeFamily;

// could rename to TileEntityUpgradeableBelt
public class PC_TileEntityTutorial extends PC_TileEntityUpgradable
{
	private double basespeed=1;
	public double speed;
	
	
	public PC_TileEntityTutorial() 
	{
		super(PC_UpgradeFamily.Speed.upgradeFlag | PC_UpgradeFamily.Security.upgradeFlag, 3, "Inv", new PC_Inventory[0]);
		
		// 0x01 | 0x08 = 0x09; Limiting us to 32 unique families of upgrades
		//validFamilies = PC_UpgradeFamily.Speed.upgradeFlag | PC_UpgradeFamily.Security.upgradeFlag;
				
		speed = basespeed;
	}

	
	/**
	 * Catches event thrown by GUI any time there is a change to the list of upgrades
	 * Rebuilds the upgrade list, checking to see if its a valid family.
	 */
	/*@Override
	public boolean onUpgradesChanged(PC_ItemUpgrade[] upgrades)
	{
		// NOTE:
		// BEFORE this event is thrown, the GUI should have already done the hasSecurityUpgrade() on this
		// entity and prompted a password dialog. When this method is called it is assumed the player had access 
		// as this process will over-write the existing list of upgrades attached to this tile entity.		
		if (upgrades.length <= this.upgradeList.length)
		{			
			// reset the speed
			/*speed = basespeed;
						
			for (int x = 0, y=0; x < upgrades.length; x++)
			{					
				if ((upgrades[x].getUpgradeFamily().upgradeFlag & validFamilies) > 0)
				{					
					// set the upgrade in the slot x to this upgrade 
					// using y will ensure we do not go out of bounds in case there are more upgrades attached than we have slots for
					// there should be other checks in the GUI that contain filters for the upgrades it allows based on the validFamilies flag.
					this.upgradeList[y] = upgrades[x]; y++;
					
					switch (upgrades[x].getUpgradeFamily())
					{
						case Speed:							
							// modify speed of this TE, if for example 3 speed upgrades are attached
							// this will *= the speed three times.
							speed *= upgrades[x].getUpgradeEffect();
							break;
						case Security:
							// prompt for password
							break;
						default: 
							break;
					}
				}
				else 
				{
					// family is not supported, so do nothing					
					return false;
				}
			}
			return true;
		}		
		return false;
	}	*/
	@Override
	public int getLightOpacity()
	{
		// TODO Auto-generated method stub
		//System.out.println("getLightOpacity inside my tile entity");
		return super.getLightOpacity();
	}


	@Override
	public void loadFromNBT(NBTTagCompound nbtTagCompound) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void saveToNBT(NBTTagCompound nbtTagCompound) {
		// TODO Auto-generated method stub
		
	}
}
