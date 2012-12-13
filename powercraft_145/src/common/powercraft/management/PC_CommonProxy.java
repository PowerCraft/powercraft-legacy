package powercraft.management;

import cpw.mods.fml.common.network.NetworkRegistry;

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
        NetworkRegistry.instance().registerConnectionHandler(new PC_ConnectionHandler());
	}
	
}
