package powercraft.api.upgrade;

import net.minecraft.item.ItemStack;

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
