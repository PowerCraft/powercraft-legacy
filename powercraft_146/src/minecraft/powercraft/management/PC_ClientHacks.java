package powercraft.management;

import java.io.File;

import net.minecraft.client.Minecraft;
import powercraft.management.PC_Utils.ValueWriting;

public class PC_ClientHacks {

public static void hackServer(){
		
		hackMinecraftSaver();
		
	}
	
	private static void hackMinecraftSaver(){
		Minecraft mc = PC_ClientUtils.mc();
		
		ValueWriting.setPrivateValue(Minecraft.class, mc, 42, new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")));
	}
	
}
