package powercraft.management;

import java.io.File;

import net.minecraft.server.MinecraftServer;

public class PC_ServerHacks {

	public static void hackServer(){
		
		hackMinecraftSaver();
		
	}
	
	private static void hackMinecraftSaver(){
		MinecraftServer ms = PC_Utils.mcs();
		File file = (File)PC_Utils.getPrivateValue(MinecraftServer.class, ms, 4);
		PC_Utils.setPrivateValue(MinecraftServer.class, ms, 2, new PC_HackedSaveConverter(file));
	}
	
}
