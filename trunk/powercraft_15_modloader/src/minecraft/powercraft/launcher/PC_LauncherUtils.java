package powercraft.launcher;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.mod_PowerCraft;
import powercraft.launcher.loader.PC_ModLoader;
import powercraft.launcher.loader.PC_ModuleDiscovery;

public class PC_LauncherUtils {
	
	protected static PC_LauncherUtils instance;
	
	public PC_LauncherUtils() {
		instance = this;
	}
	
	public static MinecraftServer mcs() {
		return MinecraftServer.getServer();
	}
	
	public static boolean isClient() {
		return instance.pIsClient();
	}
	
	public static File createFile(File pfile, String name) {
		File file = new File(pfile, name);
		if (!file.exists())
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
	
	public static String getMinecraftVersion() {
		return mod_PowerCraft.minecraftVersion();
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
	
	public static void addAuthor(String name) {
		mod_PowerCraft.addAuthor(name);
	}
	
	public static void addCredit(String name) {
		mod_PowerCraft.addCredit(name);
	}
	
	public static boolean usingModLoader(PC_ModLoader modLoader) {
		if (modLoader == PC_ModLoader.ALL)
			return true;
		return modLoader == getModLoader();
	}
	
	public static PC_ModLoader getModLoader() {
		return PC_ModLoader.RISUGAMIS_MODLOADER;
	}
	
	public static boolean isDeveloperVersion(){
		return PC_Launcher.isDeveloperVersion();
	}
	
	public static void openUpdateGui(boolean requestDownloadTarget){
		instance.pOpenUpdateGui(requestDownloadTarget);
	}
	
	public static PC_ModuleDiscovery searchModules(boolean addAny){
		File modules = PC_LauncherUtils.getPowerCraftModuleFile();
		File res = null;
		try {
			URL url = mod_PowerCraft.class.getResource("../../../");
			if(url!=null){
				res = new File(url.toURI());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		PC_ModuleDiscovery moduleDiscovery = new PC_ModuleDiscovery();
		moduleDiscovery.search(modules, addAny, res, false);
		return moduleDiscovery;
	}
	
	public static void lookForUpdates() {
		instance.pLookForUpdates();
	}
	
	public void pLookForUpdates(){
		
	}
	
	public void pOpenUpdateGui(boolean requestDownloadTarget){
		
	}
	
	protected boolean pIsClient() {
		return false;
	}
	
	protected File pGetMCDirectory() {
		return mcs().getFile("");
	}
	
}
