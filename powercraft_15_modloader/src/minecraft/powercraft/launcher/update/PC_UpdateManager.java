package powercraft.launcher.update;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import powercraft.launcher.PC_Launcher;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.PC_Property;
import powercraft.launcher.PC_Version;
import powercraft.launcher.loader.PC_ModuleClassInfo;
import powercraft.launcher.loader.PC_ModuleDiscovery;
import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.launcher.loader.PC_ModuleVersion;
import powercraft.launcher.update.PC_FileWatcher.Event;
import powercraft.launcher.update.PC_FileWatcher.WatchEvent;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLInfoTag;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLModuleTag;
import powercraft.launcher.update.PC_UpdateXMLFile.XMLVersionTag;
import powercraft.launcher.updategui.PC_GuiUpdate;

public class PC_UpdateManager {

	private static PC_ThreadCheckUpdates updateChecker;
	private static PC_FileWatcher watcher;
	
	public static List<ModuleUpdateInfo> moduleList;
	public static XMLInfoTag updateInfo;
	public static File downloadTarget;
	public static boolean newLauncher;
	
	public static void startUpdateInfoDownload(){
		updateChecker = new PC_ThreadCheckUpdates();
	}
	
	public static void moduleInfos(HashMap<String, PC_ModuleObject> modules){
		updateInfo = updateChecker.getUpdateInfo();
		if(updateInfo==null)
			return;
		boolean showUpdate = PC_Launcher.openAlwaysUpdateScreen();
		if(updateInfo.getPowerCraftVersion().compareTo(new PC_Version(PC_LauncherUtils.getPowerCraftVersion()))>0){
			newLauncher = true;
			showUpdate=true;
		}
		moduleList = new ArrayList<ModuleUpdateInfo>();
		for(XMLModuleTag xmlModule:updateInfo.getModules()){
			ModuleUpdateInfo mui = new ModuleUpdateInfo();
			mui.xmlModule = xmlModule;
			mui.newVersion = mui.xmlModule.getNewestVersion();
			mui.module = modules.get(xmlModule.getName());
			List<PC_Version> versionList = new ArrayList<PC_Version>();
			for(XMLVersionTag v:xmlModule.getVersions()){
				if(!versionList.contains(v.getVersion()))
					versionList.add(v.getVersion());
			}
			if(mui.module==null){
				String ignoreVersion = PC_Launcher.getConfig().getString("updater.ignore."+mui.xmlModule.getName());
				if(ignoreVersion.equals("")){
					showUpdate |= true;
				}else{
					if(new PC_Version(ignoreVersion).compareTo(mui.newVersion.getVersion())>0){
						showUpdate |= true;
					}
				}
			}else{
				for(PC_ModuleVersion v:mui.module.getVersions()){
					if(!versionList.contains(v.getVersion()))
						versionList.add(v.getVersion());
				}
				mui.oldVersion = mui.module.getStandartVersion().getVersion();
				if(mui.newVersion.getVersion().compareTo(mui.oldVersion)>0){
					String ignoreVersion = PC_Launcher.getConfig().getString("updater.ignore."+mui.xmlModule.getName());
					if(ignoreVersion.equals("")){
						showUpdate |= true;
					}else{
						if(new PC_Version(ignoreVersion).compareTo(mui.newVersion.getVersion())>0){
							showUpdate |= true;
						}
					}
				}
			}
			PC_Version[] versionArray = versionList.toArray(new PC_Version[0]);
			Arrays.sort(versionArray);
			mui.versions = new PC_Version[versionArray.length];
			for(int i=0; i<versionArray.length; i++){
				mui.versions[i] = versionArray[versionArray.length-i-1];
			}
			moduleList.add(mui);
		}
		if(showUpdate){
			boolean requestDownloadTarget = false;
			if(PC_Launcher.getConfig().getString("updater.source").equals("")){
				downloadTarget = new File(System.getProperty("user.home"));
				requestDownloadTarget = true;
			}else{
				downloadTarget = new File(PC_Launcher.getConfig().getString("updater.source"));
			}
			PC_Launcher.saveConfig();
			watchDirectory(downloadTarget);
			PC_GuiUpdate.show(requestDownloadTarget);
			stopWatchDirectory();
		}
	}
	
	public static class ModuleUpdateInfo{
		public PC_Version[] versions;
		public PC_Version oldVersion;
		public XMLVersionTag newVersion;
		public PC_ModuleObject module;
		public XMLModuleTag xmlModule;
	}

	public static void openURL(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void download(XMLVersionTag version) {
		openURL(version.getDownloadLink());
	}

	public static void watchDirectory(File downloadTarget) {
		watcher = new PC_FileWatcher(downloadTarget);
	}

	public static void stopWatchDirectory() {
		watcher=null;
	}
	
	private static void tryToUseFile(File file){
		PC_ModuleDiscovery discovery = new PC_ModuleDiscovery();
		discovery.search(file);
		HashMap<String, PC_ModuleObject>modules = discovery.getModules();
		if(modules.size()>0){
			File to = new File(PC_LauncherUtils.getPowerCraftModuleFile(), file.getName());
			PC_FileMover.moveAndDelete(file, to);
			discovery = new PC_ModuleDiscovery();
			discovery.search(to);
			modules = discovery.getModules();
			for(PC_ModuleObject module:modules.values()){
				addModule(module);
			}
			System.out.println("Found Module Pack "+file);
		}
	}

	public static void lookForDirectoryChange(){
		if(watcher!=null){
			for (WatchEvent event: watcher.pollEvents()) {
				if(event.event==Event.CREATE || event.event==Event.MODIFY){
					tryToUseFile(event.file);
				}
		    }
		}
	}

	private static void delete(File file){
		for(ModuleUpdateInfo ui:moduleList){
			if(ui.module!=null){
				for(PC_ModuleVersion mv:ui.module.getVersions()){
					boolean delete=false;
					if(mv.getClient()!=null){
						if(mv.getClient().file.equals(file)){
							delete=true;
						}
					}
					if(mv.getCommon()!=null){
						if(mv.getCommon().file.equals(file)){
							delete=true;
						}
					}
					if(delete){
						if(ui.xmlModule.getVersion(mv.getVersion())==null){
							List<PC_Version> versionList = Arrays.asList(ui.versions);
							versionList.remove(mv.getVersion());
							versionList.add(mv.getVersion());
							PC_Version[] versionArray = versionList.toArray(new PC_Version[0]);
							Arrays.sort(versionArray);
							ui.versions = new PC_Version[versionArray.length];
							for(int i=0; i<versionArray.length; i++){
								ui.versions[i] = versionArray[versionArray.length-i-1];
							}
						}
						ui.module.removeModule(mv);
					}
				}
			}
		}
	}
	
	public static void delete(PC_ModuleVersion moduleVersion) {
		PC_ModuleClassInfo mci = moduleVersion.getClient();
		if(mci!=null){
			delete(mci.file);
			mci.file.delete();
		}
		mci = moduleVersion.getCommon();
		if(mci!=null){
			delete(mci.file);
			mci.file.delete();
		}
		
	}

	public static void setDownloadTarget(File selectedFile) {
		PC_Launcher.getConfig().setString("updater.source", selectedFile.getAbsolutePath());
		PC_Launcher.saveConfig();
		downloadTarget = selectedFile;
		PC_UpdateManager.stopWatchDirectory();
		PC_UpdateManager.watchDirectory(downloadTarget);
	}

	private static void addModule(PC_ModuleObject module) {
		for(ModuleUpdateInfo mui:moduleList){
			if(mui.module==null){
				if(mui.xmlModule.getName().equals(module.getModuleName())){
					mui.module = module;
					for(PC_ModuleVersion mv:module.getVersions()){
						List<PC_Version> versionList = Arrays.asList(mui.versions);
						if(!versionList.contains(mv.getVersion())){
							versionList.add(mv.getVersion());
							PC_Version[] versionArray = versionList.toArray(new PC_Version[0]);
							Arrays.sort(versionArray);
							mui.versions = new PC_Version[versionArray.length];
							for(int i=0; i<versionArray.length; i++){
								mui.versions[i] = versionArray[versionArray.length-i-1];
							}
						}
					}
					activateModule(module.getNewest());
					return;
				}
			}else{
				if(mui.module.getModuleName().equals(module.getModuleName())){
					for(PC_ModuleVersion mv:module.getVersions()){
						if(mui.module.getVersion(mv.getVersion())==null){
							mui.module.addModule(mv);
							List<PC_Version> versionList = Arrays.asList(mui.versions);
							if(!versionList.contains(mv.getVersion())){
								versionList.add(mv.getVersion());
								PC_Version[] versionArray = versionList.toArray(new PC_Version[0]);
								Arrays.sort(versionArray);
								mui.versions = new PC_Version[versionArray.length];
								for(int i=0; i<versionArray.length; i++){
									mui.versions[i] = versionArray[versionArray.length-i-1];
								}
							}
						}
					}
					activateModule(module.getNewest());
					return;
				}
			}
		}
	}
	
	public static void activateModule(PC_ModuleVersion activeModuleVersion) {
		PC_ModuleObject module = activeModuleVersion.getModule();
		module.getConfig().setString("loader.usingVersion", activeModuleVersion.getVersion().toString());
		module.saveConfig();
		for(ModuleUpdateInfo mui:moduleList){
			if(mui.module==module){
				mui.oldVersion = activeModuleVersion.getVersion();
			}
		}
	}

	public static void ignoreUpdates() {
		PC_Property config = PC_Launcher.getConfig();
		for(ModuleUpdateInfo mui:moduleList){
			config.setString("updater.ignore."+mui.xmlModule.getName(), mui.newVersion.getVersion().toString());
		}
		PC_Launcher.saveConfig();
	}
	
}
