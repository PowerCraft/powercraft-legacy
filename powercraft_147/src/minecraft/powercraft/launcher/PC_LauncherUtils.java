package powercraft.launcher;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CallableMinecraftVersion;

public class PC_LauncherUtils {

	protected static PC_LauncherUtils instance;
	
	public PC_LauncherUtils(){
		instance = this;
	}
	
	public static boolean isClient(){
		return instance.pIsClient();
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
		return new CallableMinecraftVersion(null).minecraftVersion();
	}

	public static mod_PowerCraft getMod() {
		return mod_PowerCraft.getInstance();
	}

	public static String getPowerCraftVersion() {
		return mod_PowerCraft.getInstance().getVersion();
	}

	public static String getPowerCraftName() {
		return mod_PowerCraft.getInstance().getName();
	}
	
	public static void addAuthor(String name){
		mod_PowerCraft.getInstance().getModMetadata().authorList.add(name);
	}
	
	public static void addCredit(String name){
		mod_PowerCraft.getInstance().getModMetadata().credits += ", "+name;
	}
	
	public static boolean isForge() {
		return true;
	}
	
	protected boolean pIsClient(){
		return false;
	}
	
}
