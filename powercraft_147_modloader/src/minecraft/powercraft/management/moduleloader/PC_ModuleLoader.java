package powercraft.management.moduleloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Logger;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.registry.PC_ModuleRegistry;

public class PC_ModuleLoader{
	
	private static ClassLoader moduleLoader = PC_ModuleLoader.class.getClassLoader();
	private static List<PC_Struct2<File, Boolean>> files = new ArrayList<PC_Struct2<File, Boolean>>();
	private static boolean addFile;
	
	public static void addModuleFile(File file, boolean addFile) {
		if(file.exists()){
			files.add(new PC_Struct2<File, Boolean>(file, addFile));
		}
	}
	
	public static void loadModules() {
		for(PC_Struct2<File, Boolean> file:files){
			load(file.a, file.b);
		}
	}
	
	public static void load(File file, boolean addFile) {
		if(file.exists()){
			PC_ModuleLoader.addFile = addFile;
			searchDir(file);
		}
	}

	private static void addFileToClassLoader(File file){
		if(addFile){
			if(moduleLoader instanceof URLClassLoader){
				try {
					Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
					addURL.setAccessible(true);
					addURL.invoke(moduleLoader, file.toURI().toURL());
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	private static void searchDir(File file) {
		if(file.isDirectory()){
			addFileToClassLoader(file);
			for(File f:file.listFiles()){
				searchDir(f);
			}
		}else if(file.isFile()){
			if(file.getName().endsWith(".class")){
				try {
					loadClass(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}else if(file.getName().endsWith(".jar") || file.getName().endsWith(".zip")){
				searchZip(file);
			}
		}
	}

	private static void searchZip(File file) {
		try {
			addFileToClassLoader(file);
			ZipFile zip = new ZipFile(file);
			for(ZipEntry ze:Collections.list(zip.entries())){
				if(!ze.isDirectory()){
					if(ze.getName().endsWith(".class")){
						loadClass(zip.getInputStream(ze));
					}
				}
			}
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadClass(InputStream is) {
		byte[] b;
		try {
			b = new byte[is.available()];
			is.read(b);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		PC_ClassInfo ci = new PC_ClassInfo(b);
		String[] interfaceNames = ci.getInterfaceNames();
		if(interfaceNames!=null && !(Modifier.isInterface(ci.getClassAccess()) || Modifier.isAbstract(ci.getClassAccess()))){
			boolean create = false;
			for(String interfaceName:ci.getInterfaceNames()){
				if(GameInfo.isClient()){
					if(interfaceName.equals(PC_IClientModule.class.getName())){
						create = true;
						break;
					}
				}else{
					if(interfaceName.equals(PC_IModule.class.getName())){
						create = true;
						break;
					}
				}
			}
			if(create){
				try {
					Class<?> c = moduleLoader.loadClass(ci.getClassName());
					PC_IModule module;
					PC_ModuleRegistry.registerModule(module = (PC_IModule)c.newInstance());
					PC_Logger.info("Module \""+module.getName()+"\" have been loaded");
				} catch (Throwable e) {
					PC_Logger.severe("Error on Loading Module \""+ci.getClassName()+"\"");
					e.printStackTrace();
				}
			}
		}
	}
	
}