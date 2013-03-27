package powercraft.api.hacks;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFire;
import net.minecraft.src.ISaveFormat;
import powercraft.api.PC_ClientUtils;
import powercraft.api.reflect.PC_ReflectHelper;

public class PC_ClientHacks {

	public static void hackClient(){
		
		hackMinecraftSaver();
		
		hackFire();
		
	}
	
	private static void hackMinecraftSaver(){
		Minecraft mc = PC_ClientUtils.mc();
		PC_ReflectHelper.setValue(Minecraft.class, mc, 44, new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")), ISaveFormat.class);
	}
	
	private static void hackFire(){
		int fireID = Block.fire.blockID;
		Block.blocksList[fireID] = null;
		Block newFire = new PC_BlockFireHack(Block.fire);
		PC_ReflectHelper.setValue(Block.class, Block.class, 69, newFire, BlockFire.class);
	}
	
}
