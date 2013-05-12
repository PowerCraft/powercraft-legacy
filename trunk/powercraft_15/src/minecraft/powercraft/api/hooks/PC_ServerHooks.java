package powercraft.api.hooks;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveFormat;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_Utils;

public class PC_ServerHooks {

	public static void registerServerHooks(){
		
		hackMinecraftSaver();
		
		hackEnderman();
		
	}
	
	private static void hackMinecraftSaver(){
		MinecraftServer ms = PC_Utils.mcs();
		File file = PC_ReflectHelper.getValue(MinecraftServer.class, ms, 3, File.class);
		PC_ReflectHelper.setValue(MinecraftServer.class, ms, 1, new PC_SaveConverterHook(file), ISaveFormat.class);
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
