package powercraft.launcher.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.PC_Logger;
import powercraft.launcher.PC_Property;
import powercraft.launcher.PC_Version;

public class PC_ModuleObject {

	private String moduleName;
	
	private List<PC_ModuleVersion> moduleVersions = new ArrayList<PC_ModuleVersion>();
	
	private PC_Version usingVersion;
	private Class<?> moduleClass;
	private Object module;
	private boolean isLoaded=false;
	private List<PC_ModuleObject> after = new ArrayList<PC_ModuleObject>();
	private PC_Property config;
	
	public PC_ModuleObject(String moduleName){
		String version = getConfig().getString("loader.usingVersion");
		if(!version.equals("")){
			usingVersion = new PC_Version(version);
		}
		this.moduleName = moduleName;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void addModule(PC_ModuleVersion module){
		module.setModule(this);
		moduleVersions.add(module);
	}
	
	public void addModuleLoadBevore(PC_ModuleObject bevore){
		after.add(bevore);
	}
	
	public List<PC_ModuleVersion> getVersions() {
		return new ArrayList<PC_ModuleVersion>(moduleVersions);
	}
	
	public PC_ModuleVersion getVersion(PC_Version version){
		for(PC_ModuleVersion moduleVersion:moduleVersions){
			if(moduleVersion.getVersion().compareTo(version)==0){
				return moduleVersion;
			}
		}
		return null;
	}
	
	public PC_ModuleVersion getNewest(){
		PC_ModuleVersion newest = moduleVersions.get(0);
		for(PC_ModuleVersion module:moduleVersions){
			if(module.getVersion().compareTo(newest.getVersion())>0){
				newest = module;
			}
		}
		return newest;
	}
	
	public PC_ModuleVersion getStandartVersion(){
		PC_ModuleVersion version;
		if(usingVersion!=null){
			version = getVersion(usingVersion);
			if(version!=null){
				return version;
			}
		}
		version = getNewest();
		usingVersion = version.getModuleVersion();
		getConfig().setString("loader.usingVersion", usingVersion.toString());
		saveConfig();
		return version;
	}
	
	public void load(){
		if(!isLoaded){
			isLoaded = true;
			for(PC_ModuleObject module:after){
				module.load();
			}
			try {
				PC_ModuleVersion moduleVersion = getStandartVersion();
				PC_ModuleClassInfo classInfo = moduleVersion.getCommon();
				if(PC_LauncherUtils.isClient()){
					if(moduleVersion.getClient()!=null)
						classInfo = moduleVersion.getClient();
				}
				moduleClass = PC_ModuleLoader.load(classInfo.className.replace('/', '.'), classInfo.file);
				module = moduleClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Class<?> getModuleClass() {
		return moduleClass;
	}
	
	public Object getModule(){
		return module;
	}
	
	public Object callMethod(String name, Class<?>[] classes, Object[] objects) {
		Class<?> c = moduleClass;
		
		while(c!=null){
			
			Method m = null;
			try {
				m = c.getMethod(name, classes);
				m.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if(m!=null){
				try {
					return m.invoke(module, objects);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return null;
			}
			
			c=c.getSuperclass();
		}
		
		return null;
		
	}

	public Object callMethod(Class<? extends Annotation> annontation, Object[] objects) {
		Class<?> c = moduleClass;
		
		while(c!=null){
			
			Method ma[] = c.getDeclaredMethods();
			for(Method m:ma){
				if(m.isAnnotationPresent(annontation)){
					try {
						m.setAccessible(true);
						return m.invoke(module, objects);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					return null;
				}
			}
			
			c=c.getSuperclass();
		}
		return null;
	}
	
	public void loadConfig(){
		File f = new File(PC_LauncherUtils.getMCDirectory(), "config/PowerCraft-"+moduleName+".cfg");
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
	}
	
	public PC_Property getConfig(){
		if(config==null)
			loadConfig();
		return config;
	}
	
	public void setConfig(PC_Property config){
		this.config = config;
	}

	public void saveConfig() {
		File f = new File(PC_LauncherUtils.getMCDirectory(), "config/PowerCraft-"+moduleName+".cfg");
		f.mkdirs();
		try {
			OutputStream os = new FileOutputStream(f);
			config.save(os);
		} catch (FileNotFoundException e) {
			PC_Logger.severe("Can't find File "+f);
		}
	}
	
	public void preInit() {
		callMethod(PC_Module.PC_PreInit.class, new Object[]{});
	}
	
	public void init(){
		callMethod(PC_Module.PC_Init.class, new Object[]{});
	}
	
	public void postInit(){
		callMethod(PC_Module.PC_PostInit.class, new Object[]{});
	}
	
	public void initProperties(PC_Property config){
		callMethod(PC_Module.PC_InitProperties.class, new Object[]{config});
	}
	
	public List initEntities(List entities){
		return (List)callMethod(PC_Module.PC_InitEntities.class, new Object[]{entities});
	}
	
	public List initRecipes(List recipes){
		return (List)callMethod(PC_Module.PC_InitRecipes.class, new Object[]{recipes});
	}
	
	public List initDataHandlers(List dataHandlers){
		return (List)callMethod(PC_Module.PC_InitDataHandlers.class, new Object[]{dataHandlers});
	}
	
	public List initPacketHandlers(List packetHandlers){
		return (List)callMethod(PC_Module.PC_InitPacketHandlers.class, new Object[]{packetHandlers});
	}
	
	public List registerGuis(List guis){
		return (List)callMethod(PC_Module.PC_RegisterGuis.class, new Object[]{guis});
	}

	public List initLanguage(List arrayList) {
		return (List)callMethod(PC_ClientModule.PC_InitLanguage.class, new Object[]{arrayList});
	}

	public List loadTextureFiles(List arrayList) {
		return (List)callMethod(PC_ClientModule.PC_LoadTextureFiles.class, new Object[]{arrayList});
	}

	public List addSplashes(List arrayList) {
		return (List)callMethod(PC_ClientModule.PC_AddSplashes.class, new Object[]{arrayList});
	}
	
	public List registerEntityRender(List arrayList) {
		return (List)callMethod(PC_ClientModule.PC_RegisterEntityRender.class, new Object[]{arrayList});
	}

	public void resolveInstances(HashMap<String, PC_ModuleObject> modules) {
		Class<?> c = moduleClass;
		while(c!=null){
			Field fa[] = c.getDeclaredFields();
			for (Field f : fa) {
				if (f.isAnnotationPresent(PC_Module.PC_Instance.class)) {
					String modulename = f.getAnnotation(PC_Module.PC_Instance.class).module();
					PC_ModuleObject module;
					if(modulename.equals("")){
						module = this;
					}else{
						module= modules.get(modulename);
					}
					if(module!=null){
						f.setAccessible(true);
						try {
							f.set(this.module, module.module);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
	
			c = c.getSuperclass();
		}
	}
	
}
