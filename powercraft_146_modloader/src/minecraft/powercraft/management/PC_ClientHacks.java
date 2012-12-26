package powercraft.management;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiIngame;
import powercraft.management.PC_Utils.ValueWriting;

public class PC_ClientHacks {

	public static void hackServer(){
		
		hackMinecraftSaver();
		
		ValueWriting.setPrivateValue(GuiIngame.class, PC_ClientUtils.mc().ingameGUI, 0, new PC_RenderItemHack());
		
	}
	
	private static void hackMinecraftSaver(){
		Minecraft mc = PC_ClientUtils.mc();
		ValueWriting.setPrivateValue(Minecraft.class, mc, 42, new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")));
	}
	
}
