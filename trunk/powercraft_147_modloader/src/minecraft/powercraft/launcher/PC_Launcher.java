package powercraft.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.HashMap;

import net.minecraft.src.mod_PowerCraft;

public class PC_Launcher {
	
	private PC_Property config;
	private boolean autoUpdate;
	private PC_ModuleDiscovery modules;
	private static PC_Launcher instance;
	
	public PC_Launcher(){
		instance = this;
	}
	
	public static PC_Launcher getLauncher(){
		return instance;
	}
	
	public void loadConfig(){
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
		
	}
	
	public void saveConfig(){
		File f = new File(PC_LauncherUtils.getMCDirectory(), "config/PowerCraft.cfg");
		f.mkdirs();
		try {
			OutputStream os = new FileOutputStream(f);
			config.save(os);
		} catch (FileNotFoundException e) {
			PC_Logger.severe("Can't find File "+f);
		}
	}
	
	public PC_ModuleDiscovery searchModules(boolean addAny){
		File modules = PC_LauncherUtils.getPowerCraftModuleFile();
		File mods = new File(PC_LauncherUtils.getMCDirectory(), "mods");
		File res = null;
		try {
			res = new File(mod_PowerCraft.class.getResource("../../../").toURI());
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
	
	private void updatePowerCraft(){
		PC_UpdateManager.startUpdateInfoDownload();
		File moduleFiles = PC_LauncherUtils.getPowerCraftModuleFile();
		HashMap<String, PC_ModuleObject> modules = searchModules(false).getModules();
		PC_UpdateManager.moduleInfos(modules);
	}
	
	private void loadModules(){
		(modules = searchModules(true)).loadModules();
	}
	
	public void preInit(){
		PC_Logger.init(PC_LauncherUtils.getPowerCraftFile());
		PC_Logger.enterSection("Loading");
		
		loadConfig();
		
		if(autoUpdate){
			updatePowerCraft();
		}
		
		loadModules();
		
		PC_Logger.exitSection();
		
		getManagement().preInit();

		
	}
	
	public void init(){
		
		getManagement().initProperties(config);
		getManagement().init();

	}
	
	public void postInit(){
		
		getManagement().postInit();
		
	}

	public Object callManagementMethod(String name, Class<?>[] classes, Object[] objects) {
		return getManagement().callMethod(name, classes, objects);
	}

	public Object callManagementMethod(Class<? extends Annotation> annontation, Object[] objects) {
		return getManagement().callMethod(annontation, objects);
	}
	
	public HashMap<String, PC_ModuleObject> getModules(){
		HashMap<String, PC_ModuleObject> hm = modules.getModules();
		hm.remove("management");
		return hm;
	}
	
	public PC_ModuleObject getManagement(){
		return modules.getManagement();
	}
	
	public PC_Property getConfig(){
		return config;
	}
	
}
