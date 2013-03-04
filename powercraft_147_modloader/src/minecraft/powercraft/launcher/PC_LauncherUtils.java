package powercraft.launcher;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.mod_PowerCraft;

public class PC_LauncherUtils {

	public static boolean isClient(){
		return true;
	}
	
	public static File createFile(File pfile, String name) {
		File file = new File(pfile, name);
		if(!file.exists())
			file.mkdirs();
	    return file;
	}

	public static File getMCDirectory() {
		return Minecraft.getMinecraftDir();
	}
	
	public static File getPowerCraftFile() {
		return createFile(getMCDirectory(), "PowerCraft");
	}
	
	public static File getPowerCraftModuleFile() {
		return createFile(getPowerCraftFile(), "Modules");
	}
	
	public static String getMinecraftVersion(){
		return mod_PowerCraft.getMinecraftVersion();
	}

	public static mod_PowerCraft getMod() {
		return mod_PowerCraft.getInstance();
	}

	public static String getVersion() {
		return mod_PowerCraft.getInstance().getVersion();
	}

	public static boolean isForge() {
		return false;
	}
	
}
