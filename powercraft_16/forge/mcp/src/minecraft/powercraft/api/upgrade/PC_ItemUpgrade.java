package powercraft.api.upgrade;

import powercraft.api.items.PC_Item;

public abstract class PC_ItemUpgrade extends PC_Item 
{
	protected PC_UpgradeFamily upgradeFamily;
	protected int upgradeID;
	protected String upgradeName;	
	protected float upgradeAmplitude;
	protected int upgradeMeta; // use for non-float operations
	
	public PC_ItemUpgrade(int id) {
		super(id);		
	}

	public PC_UpgradeFamily getUpgradeFamily()
	{		
		return upgradeFamily;
	}
	
	public int getUpgradeID()
	{
		return upgradeID;
	}
	
	public String getUpgradeName()
	{
		return upgradeName;
	}
	
	public float getUpgradeEffect()
	{
		return upgradeAmplitude;
	}
}
