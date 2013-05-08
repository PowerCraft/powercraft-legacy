package powercraft.api.hacks;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.storage.ISaveFormat;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_ClientUtils;

public class PC_ClientHacks {

	public static void hackClient(){
		
		hackMinecraftSaver();
		
		hackEnderman();
		
	}
	
	private static void hackMinecraftSaver(){
		Minecraft mc = PC_ClientUtils.mc();
		
		PC_ReflectHelper.setValue(Minecraft.class, mc, 44, new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")), ISaveFormat.class);
	}
	
	private static void hackEnderman(){
		boolean[] carriableBlocks = (boolean[])PC_ReflectHelper.getValue(EntityEnderman.class, EntityEnderman.class, 0, boolean[].class);
		boolean[] newCarriableBlocks = new boolean[Block.blocksList.length];
		for(int i=0; i<carriableBlocks.length; i++){
			newCarriableBlocks[i] = carriableBlocks[i];
		}
		PC_ReflectHelper.setValue(EntityEnderman.class, EntityEnderman.class, 0, newCarriableBlocks, boolean[].class);
	}
	
}
