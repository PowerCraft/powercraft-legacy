package powercraft.api.upgrade;

import powercraft.api.items.PC_Item;

public abstract class PC_ItemUpgrade extends PC_Item {

	private int upgradeID=0;
	private final String upgradeName;
	
	public PC_ItemUpgrade(int id, String upgradeName) {
		super(id);
		this.upgradeName = upgradeName;
	}

	public int getUpgradeType(){
		if(upgradeID==0){
			upgradeID = PC_Upgrade.getUpgrade(upgradeName);
		}
		return upgradeID;
	}
	
}
