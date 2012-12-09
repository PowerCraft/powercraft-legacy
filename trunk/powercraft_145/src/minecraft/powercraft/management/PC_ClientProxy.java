package powercraft.management;

public class PC_ClientProxy extends PC_CommonProxy {

	@Override
	public void initUtils() {
		PC_ClientUtils.create();
	}
	
	@Override
	public void hack() {
		PC_ClientHacks.hackServer();
	}
	
}
