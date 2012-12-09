package powercraft.management;

public class PC_CommonProxy {

	public void initUtils() {
		PC_Utils.create();
	}

	public void hack() {
		PC_ServerHacks.hackServer();
	}

	public void init() {
		new PC_Renderer(true);
        new PC_Renderer(false);
	}
	
}
