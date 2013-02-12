package powercraft.management.hacks;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.RenderManager;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ValueWriting;

public class PC_ClientHacks {

	public static void hackClient(){
		
		hackMinecraftSaver();
		
		hackFire();
		
	}
	
	private static void hackMinecraftSaver(){
		Minecraft mc = PC_ClientUtils.mc();
		ValueWriting.setPrivateValue(Minecraft.class, mc, 42, new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")));
	}
	
	private static void hackFire(){
		int fireID = Block.fire.blockID;
		Block.blocksList[fireID] = null;
		Block newFire = new PC_BlockFireHack(Block.fire);
		ValueWriting.setPrivateValue(Block.class, Block.class, 70, newFire);
	}
	
}
