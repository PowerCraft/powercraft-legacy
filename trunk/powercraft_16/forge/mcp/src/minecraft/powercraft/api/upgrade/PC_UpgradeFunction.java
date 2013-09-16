package powercraft.api.upgrade;

import powercraft.api.blocks.PC_TileEntityUpgradable;

public interface PC_UpgradeFunction{
	
	public void onPlaced(PC_TileEntityUpgradable te);
	
	public void onTick(PC_TileEntityUpgradable te);
	
	public void onRemoved(PC_TileEntityUpgradable te);
	
	public Object function(int index, PC_TileEntityUpgradable te, Object...obj);
	
	public static class Impl implements PC_UpgradeFunction{

		@Override
		public void onPlaced(PC_TileEntityUpgradable te) {}

		@Override
		public void onTick(PC_TileEntityUpgradable te) {}

		@Override
		public void onRemoved(PC_TileEntityUpgradable te) {}

		@Override
		public Object function(int index, PC_TileEntityUpgradable te, Object... obj) {
			return null;
		}
		
	}
	
}