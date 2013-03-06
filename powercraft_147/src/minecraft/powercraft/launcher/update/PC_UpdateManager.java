package powercraft.launcher.update;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import powercraft.launcher.PC_Launcher;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.PC_Version;
import powercraft.launcher.loader.PC_ModuleDiscovery;
import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.launcher.loader.PC_ModuleVersion;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLInfoTag;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLModuleTag;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLVersionTag;
import powercraft.launcher.updategui.PC_GuiUpdate;

public class PC_UpdateManager {

	private static PC_ThreadCheckUpdates updateChecker;
	private static WatchKey key;
	private static WatchService watcher;
	private static Path dir;
	
	public static void startUpdateInfoDownload(){
		updateChecker = new PC_ThreadCheckUpdates();
	}
	
	public static void moduleInfos(HashMap<String, PC_ModuleObject> modules){
		XMLInfoTag updateInfo = updateChecker.getUpdateInfo();
		boolean showUpdate = PC_Launcher.openAlwaysUpdateScreen();
		List<ModuleUpdateInfo> moduleList = new ArrayList<ModuleUpdateInfo>();
		for(XMLModuleTag xmlModule:updateInfo.getModules()){
			ModuleUpdateInfo mui = new ModuleUpdateInfo();
			mui.xmlModule = xmlModule;
			mui.newVersion = mui.xmlModule.getNewestVersion();
			mui.module = modules.get(xmlModule.getName());
			if(mui.module==null){
				showUpdate |= true;
			}else{
				mui.versions = new ArrayList<PC_Version>();
				for(PC_ModuleVersion v:mui.module.getVersions()){
					mui.versions.add(v.getVersion());
				}
				mui.oldVersion = mui.module.getStandartVersion().getVersion();
				if(mui.newVersion.getVersion().compareTo(mui.oldVersion)>0){
					showUpdate |= true;
				}
			}
			moduleList.add(mui);
		}
		if(showUpdate){
			PC_GuiUpdate.show(moduleList, updateInfo);
		}
	}
	
	public static class ModuleUpdateInfo{
		public List<PC_Version> versions;
		public PC_Version oldVersion;
		public XMLVersionTag newVersion;
		public PC_ModuleObject module;
		public XMLModuleTag xmlModule;
	}

	public static void download(XMLVersionTag version) {
		try {
			Desktop.getDesktop().browse(new URI(version.getDownloadLink()));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static void watchDirectory(File downloadTarget) {
		if(key!=null)
			key.cancel();
		try {
			if(watcher==null)
				watcher = FileSystems.getDefault().newWatchService();
			dir = downloadTarget.toPath();
			key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void stopWatchDirectory() {
		if(key!=null){
			key.cancel();
			key=null;
		}
	}
	
	private static void tryToUseFile(File file){
		PC_ModuleDiscovery discovery = new PC_ModuleDiscovery();
		discovery.search(file);
		HashMap<String, PC_ModuleObject>modules = discovery.getModules();
		if(modules.size()>0){
			try {
				Files.move(file.toPath(), new File(PC_LauncherUtils.getPowerCraftModuleFile(), file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
				for(PC_ModuleObject module:modules.values()){
					module.getConfig().setString("loader.usingVersion", module.getNewest().toString());
					module.saveConfig();
				}
				System.out.println("Found Module Pack "+file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void lookForDirectoryChange(){
		if(key!=null){
			 for (WatchEvent<?> event: key.pollEvents()) {
		        WatchEvent.Kind<?> kind = event.kind();
		        if (kind == StandardWatchEventKinds.OVERFLOW) {
		            continue;
		        }
		        WatchEvent<Path> ev = (WatchEvent<Path>)event;
		        Path filename = dir.resolve(ev.context());
		        tryToUseFile(filename.toFile());
		    }
		}
	}
	
}
