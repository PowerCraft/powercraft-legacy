package powercraft.api.upgrade;

public enum PC_UpgradeFamily
{
	Unknown (0x00),
	Speed (0x01),
	Range (0x02),
	Damage (0x04),
	Security (0x08);
	
	public int upgradeFlag;
	
	PC_UpgradeFamily(int flag)
	{
		this.upgradeFlag = flag;
	}
	public int getFamilyID()
	{
		return upgradeFlag;
	}
}
