package powercraft.management.moduleloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import powercraft.management.PC_ClassInfo;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Logger;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleLoader;

public class PC_ModuleLoader{
	
	public static void load(File file) {
		if(file.exists()){
			searchDir(file);
		}
	}

	private static void searchDir(File file) {
		if(file.isDirectory()){
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
				Class<?> c = new PC_ModuleClassLoader(ci.getClassName(), b).getCreateClass();
				try {
					PC_IModule module;
					ModuleLoader.registerModule(module = (PC_IModule)c.newInstance());
					PC_Logger.info("Module \""+module.getName()+"\" have been loaded");
				} catch (Throwable e) {
					PC_Logger.severe("Error on Loading Module \""+ci.getClassName()+"\"");
					e.printStackTrace();
				}
			}
		}
	}
	
}