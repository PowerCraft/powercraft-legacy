package powercraft.tutorial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.blocks.PC_TileEntityUpgradable;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.PC_IGresGuiOpenHandler;
import powercraft.api.inventory.PC_Inventory;
import powercraft.api.upgrade.PC_UpgradeFamily;

// could rename to TileEntityUpgradeableBelt
public class PC_TileEntityTutorial extends PC_TileEntityUpgradable implements PC_IGresGuiOpenHandler
{
	private double basespeed=1;
	public double speed;
	
	
	public PC_TileEntityTutorial() 
	{
		super(PC_UpgradeFamily.Speed.getFamilyID() | PC_UpgradeFamily.Security.getFamilyID(), 3,
				"Tutorial", new PC_Inventory[]{
			new PC_Inventory("main", 9*3, 64, PC_Inventory.USEABLEBYPLAYER|PC_Inventory.DROPSTACKS),
			new PC_Inventory("upgrade", 3, 1, PC_Inventory.USEABLEBYPLAYER|PC_Inventory.DROPSTACKS)});		
		speed = basespeed;
	}


	/* (non-Javadoc)
	 * @see powercraft.api.gres.PC_IGresGuiOpenHandler#openClientGui(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public PC_IGresClient openClientGui(EntityPlayer player) {
		return new PC_GuiTutorial();
	}

	/* (non-Javadoc)
	 * @see powercraft.api.gres.PC_IGresGuiOpenHandler#openServerGui(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public PC_GresBaseWithInventory openServerGui(EntityPlayer player) {
		// TODO Auto-generated method stub
		return null;
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
				if ((upgrades[x].getUpgradeFamily().getFamilyID() & validFamilies) > 0)
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
