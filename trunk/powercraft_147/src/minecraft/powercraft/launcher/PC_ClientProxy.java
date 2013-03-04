package powercraft.launcher;


public class PC_ClientProxy extends PC_CommonProxy {

	@Override
	public void initUtils() {
		PC_LauncherClientUtils.create();
	}
	
}
