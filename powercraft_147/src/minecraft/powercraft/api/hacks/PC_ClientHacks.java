package powercraft.api.hacks;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.ISaveFormat;
import powercraft.api.PC_ClientUtils;
import powercraft.api.reflect.PC_ReflectHelper;

public class PC_ClientHacks {

	public static void hackClient(){
		
		hackMinecraftSaver();
		
	}
	
	private static void hackMinecraftSaver(){
		Minecraft mc = PC_ClientUtils.mc();
		
		PC_ReflectHelper.setValue(Minecraft.class, mc, 42, new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")), ISaveFormat.class);
	}
	
}
