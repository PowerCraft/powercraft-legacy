package powercraft.api.upgrade;


public enum PC_UpgradeFamily
{
	Unknown,
	Speed,
	Range,
	Damage,
	Security;
	
	public int getFamilyID()
	{
		return 1<<ordinal();
	}
	
}
