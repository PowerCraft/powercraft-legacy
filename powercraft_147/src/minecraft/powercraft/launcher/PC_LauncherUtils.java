package powercraft.launcher;

import java.io.File;
import java.net.URISyntaxException;

import net.minecraft.crash.CallableMinecraftVersion;
import net.minecraft.server.MinecraftServer;
import powercraft.launcher.loader.PC_ModLoader;
import powercraft.launcher.loader.PC_ModuleDiscovery;

public class PC_LauncherUtils {

	protected static PC_LauncherUtils instance;
	
	public PC_LauncherUtils(){
		instance = this;
	}
	
	public static MinecraftServer mcs(){
		return MinecraftServer.getServer();
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
		return instance.pGetMCDirectory();
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

	public static boolean usingModLoader(PC_ModLoader modLoader) {
		if(modLoader==PC_ModLoader.ALL)
			return true;
		return modLoader==getModLoader();
	}
	
	public static PC_ModLoader getModLoader() {
		return PC_ModLoader.FORGE_MODLOADER;
	}

	public static PC_ModuleDiscovery searchModules(boolean addAny){
		File modules = getPowerCraftModuleFile();
		File mods = new File(getMCDirectory(), "mods");
		File res = null;
		try {
			res = new File(mod_PowerCraft.class.getResource("../../").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		PC_ModuleDiscovery moduleDiscovery = new PC_ModuleDiscovery();
		if(res==null){
			moduleDiscovery.search(modules, addAny, mods, false);
		}else{
			moduleDiscovery.search(modules, addAny, mods, false, res, false);
		}
		return moduleDiscovery;
	}
	
	public void updatePowerCraft() {
		
	}
	
	protected boolean pIsClient(){
		return false;
	}
	
	protected File pGetMCDirectory() {
		return mcs().getFile("");
	}
	
}
