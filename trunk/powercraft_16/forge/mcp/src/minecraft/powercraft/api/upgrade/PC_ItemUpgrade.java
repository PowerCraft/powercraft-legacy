package powercraft.api.upgrade;

import powercraft.api.items.PC_Item;

public abstract class PC_ItemUpgrade extends PC_Item 
{
	private PC_UpgradeFamily upgradeFamily;
	private int upgradeID;
	private String upgradeName;	
	private float upgradeAmplitude;
	
	public PC_ItemUpgrade(int id, String uName) {
		super(id);
		this.upgradeID = id;
		this.upgradeName = uName;
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
