package powercraft.management;

import java.io.File;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;

import net.minecraft.server.MinecraftServer;

public class PC_ServerHacks {

	public static void hackServer(){
		
		hackMinecraftSaver();
		
	}
	
	private static void hackMinecraftSaver(){
		MinecraftServer ms = GameInfo.mcs();
		File file = (File)ValueWriting.getPrivateValue(MinecraftServer.class, ms, 4);
		ValueWriting.setPrivateValue(MinecraftServer.class, ms, 2, new PC_HackedSaveConverter(file));
	}
	
}
