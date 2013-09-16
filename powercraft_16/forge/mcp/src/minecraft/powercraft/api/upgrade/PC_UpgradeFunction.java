package powercraft.api.upgrade;

public interface PC_UpgradeFunction{
	
	public void onPlaced();
	
	public void onTick();
	
	public void onRemoved();
	
	public Object function(int index, Object...obj);
	
	public static class Impl implements PC_UpgradeFunction{

		@Override
		public void onPlaced() {}

		@Override
		public void onTick() {}

		@Override
		public void onRemoved() {}

		@Override
		public Object function(int index, Object... obj) {
			return null;
		}
		
	}
	
}