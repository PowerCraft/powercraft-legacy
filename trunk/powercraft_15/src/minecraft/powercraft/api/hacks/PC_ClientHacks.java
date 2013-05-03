package powercraft.api.hacks;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.ISaveFormat;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_ClientUtils;

public class PC_ClientHacks {

	public static void hackClient(){
		
		hackMinecraftSaver();
		
	}
	
	private static void hackMinecraftSaver(){
		Minecraft mc = PC_ClientUtils.mc();
		
		PC_ReflectHelper.setValue(Minecraft.class, mc, 44, new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")), ISaveFormat.class);
	}
	
}
