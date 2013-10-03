package powercraft.tutorial.items;

import powercraft.api.items.PC_ItemInfo;
import powercraft.api.upgrade.PC_ItemUpgrade;
import powercraft.api.upgrade.PC_UpgradeFamily;

// should be PC_ItemUpgradeSpeedBoost or something
@PC_ItemInfo(defaultid = 3412, itemid = "tutorial", name = "Tutorial")
public class PC_ItemTutorial extends PC_ItemUpgrade
{
	private static int itemid = 3412;
	
	public PC_ItemTutorial()
	{
		super(itemid);
		upgradeID = 1492; // this can be set in a cfg file
		upgradeFamily = PC_UpgradeFamily.Speed;
		upgradeAmplitude = 2;
		upgradeName = "SpeedBooster";
	}
	@Override
	public void registerRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void loadIcons()
	{
		// TODO Auto-generated method stub

	}

}
