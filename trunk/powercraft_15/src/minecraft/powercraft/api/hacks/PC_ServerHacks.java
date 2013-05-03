package powercraft.api.hacks;

import java.io.File;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveFormat;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_Utils;

public class PC_ServerHacks {

	public static void hackServer(){
		
		hackMinecraftSaver();
		
	}
	
	private static void hackMinecraftSaver(){
		MinecraftServer ms = PC_Utils.mcs();
		File file = PC_ReflectHelper.getValue(MinecraftServer.class, ms, 3, File.class);
		PC_ReflectHelper.setValue(MinecraftServer.class, ms, 1, new PC_HackedSaveConverter(file), ISaveFormat.class);
	}
	
}
