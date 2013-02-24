package powercraft.management.hacks;

import java.io.File;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveFormat;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.reflect.PC_ReflectHelper;

public class PC_ServerHacks {

	public static void hackServer(){
		
		hackMinecraftSaver();
		
	}
	
	private static void hackMinecraftSaver(){
		MinecraftServer ms = GameInfo.mcs();
		File file = PC_ReflectHelper.getValue(MinecraftServer.class, ms, 4, File.class);
		PC_ReflectHelper.setValue(MinecraftServer.class, ms, 2, new PC_HackedSaveConverter(file), ISaveFormat.class);
	}
	
}
