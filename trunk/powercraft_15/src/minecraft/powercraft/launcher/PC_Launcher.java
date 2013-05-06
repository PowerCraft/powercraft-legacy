package powercraft.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import powercraft.launcher.loader.PC_ModuleDiscovery;
import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.launcher.update.PC_UpdateManager;

public class PC_Launcher {
	
	private static PC_Property config;
	private static boolean autoUpdate;
	private static boolean openAlwaysUpdateScreen;
	private static boolean developerVersion;
	private static PC_ModuleDiscovery modules;
	
	public static void loadConfig(){
		File f = new File(PC_LauncherUtils.getMCDirectory(), "config/PowerCraft.cfg");
		if(f.exists()){
			try {
				InputStream is = new FileInputStream(f);
				config = PC_Property.loadFromFile(is);
			} catch (FileNotFoundException e) {
				PC_Logger.severe("Can't find File "+f);
			}
		}
		if(config==null){
			config = new PC_Property(null);
		}
		
		autoUpdate = config.getBoolean("updater.autoUpdate", true, "Should PowerCraft look for updates");
		openAlwaysUpdateScreen = config.getBoolean("updater.openAlwaysUpdateScreen", false, "Should PowerCraft show always the update screen");
		developerVersion = config.getBoolean("updater.showDeveloperVersions", false, "Show developer Versions");
		
		saveConfig();
		
	}
	
	public static void saveConfig(){
		File f = PC_LauncherUtils.createFile(PC_LauncherUtils.getMCDirectory(), "config");
		f = new File(f, "PowerCraft.cfg");
		try {
			OutputStream os = new FileOutputStream(f);
			config.save(os);
		} catch (FileNotFoundException e) {
			PC_Logger.severe("Can't find File "+f);
		}
	}
	
	private static PC_ModuleDiscovery searchModules(boolean addAny){
		File modules = PC_LauncherUtils.getPowerCraftModuleFile();
		File mods = new File(PC_LauncherUtils.getMCDirectory(), "mods");
		File res = null;
		try {
			URL url = mod_PowerCraft.class.getResource("../../");
			if(url!=null){
				res = new File(url.toURI());
			}
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
	
	private static void loadModules(){
		(modules = searchModules(true)).loadModules();
	}
	
	public static void preInit(){
		try{
			PC_Logger.init(PC_LauncherUtils.getPowerCraftFile());
			PC_Logger.enterSection("Loading");
			
			loadConfig();
			
			if(autoUpdate){
				PC_UpdateManager.startUpdateInfoDownload();
				File moduleFiles = PC_LauncherUtils.getPowerCraftModuleFile();
				HashMap<String, PC_ModuleObject> modules = searchModules(false).getModules();
				PC_UpdateManager.moduleInfos(modules);
			}
			
			loadModules();
			
			PC_Logger.exitSection();
			
			getAPI().preInit();
		}catch(Throwable e){
			PC_Logger.throwing("PC_Launcher", "preInit", e);
			e.printStackTrace();
		}
	}
	
	public static void init(){
		try{
			getAPI().initProperties(config);
			getAPI().init();
		}catch(Throwable e){
			PC_Logger.throwing("PC_Launcher", "init", e);
			e.printStackTrace();
		}

	}
	
	public static void postInit(){
		try{
			getAPI().postInit();
		}catch(Throwable e){
			PC_Logger.throwing("PC_Launcher", "postInit", e);
			e.printStackTrace();
		}
	}

	public static Object callapiMethod(String name, Class<?>[] classes, Object[] objects) {
		return getAPI().callMethod(name, classes, objects);
	}

	public static Object callapiMethod(Class<? extends Annotation> annontation, Object[] objects) {
		return getAPI().callMethod(annontation, objects);
	}
	
	public static HashMap<String, PC_ModuleObject> getModules(){
		HashMap<String, PC_ModuleObject> hm = modules.getModules();
		hm.remove("Api");
		return hm;
	}
	
	public static PC_ModuleObject getAPI(){
		return modules.getAPI();
	}
	
	public static PC_Property getConfig(){
		return config;
	}
	
	public static boolean openAlwaysUpdateScreen(){
		return openAlwaysUpdateScreen;
	}

	public static boolean isDeveloperVersion() {
		return developerVersion;
	}
	
}
