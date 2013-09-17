package powercraft.api.upgrade;

/**
 * @author Aaron, overhauled by Buggi
 * Interface defines TileEntities that can be upgraded.
 */
public interface PC_IUpgradeable 
{
	public boolean onUpgradesChanged(PC_ItemUpgrade[] upgradearray);
}