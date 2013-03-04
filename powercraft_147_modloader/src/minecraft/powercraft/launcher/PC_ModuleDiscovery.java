package powercraft.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;

public class PC_ModuleDiscovery {
	
	private List<PC_ModuleObject> clinetModules = new ArrayList<PC_ModuleObject>();
	private List<PC_ModuleObject> commonModules = new ArrayList<PC_ModuleObject>();
	private HashMap<String, PC_ModuleObject> modules = new HashMap<String, PC_ModuleObject>();
	private PC_ModuleObject management;
	private List<PC_ModuleObject> startList[] = new List[]{new ArrayList<PC_ModuleObject>(), new ArrayList<PC_ModuleObject>(), new ArrayList<PC_ModuleObject>()};
	private ClassLoader moduleLoader = PC_ModuleDiscovery.class.getClassLoader();
	private boolean addFile;
	private PC_ModuleClassReader classReader = new PC_ModuleClassReader(this);
	private File loadFile;
	private File startFile;
	
	public void addClient(int access, String name, String signature, String superName, String[] interfaces, PC_AnnotationVisitor annotationVisitor) {
		if(PC_LauncherUtils.isClient()){
			if(getClientClassWithName(name)!=null){
				return;
			}
			PC_ModuleObject module = getCommonClassWithName(name);
			if(module==null){
				clinetModules.add(new PC_ModuleObject(access, name, signature, superName, interfaces, annotationVisitor, loadFile, startFile));
			}else{
				clinetModules.add(new PC_ModuleObject(access, name, signature, superName, interfaces, module.getAnnotationVisitor(), loadFile, startFile));
				commonModules.remove(module);
			}
		}
	}
	
	public void addCommon(int access, String name, String signature, String superName, String[] interfaces, PC_AnnotationVisitor annotationVisitor) {
		PC_ModuleObject module = getClientClassWithSuperClass(name);
		if(module==null){
			if(getCommonClassWithName(name)!=null){
				return;
			}
			commonModules.add(new PC_ModuleObject(access, name, signature, superName, interfaces, annotationVisitor, loadFile, startFile));
		}else{
			if(module.getAnnotationVisitor()==null){
				module.setAnnotationVisitor(annotationVisitor);
			}
		}
	}
	
	private PC_ModuleObject getClientClassWithSuperClass(String superClass){
		for(PC_ModuleObject module:clinetModules){
			if(superClass.equals(module.getSuperClassName())){
				return module;
			}
		}
		return null;
	}
	
	private PC_ModuleObject getClientClassWithName(String name){
		for(PC_ModuleObject module:clinetModules){
			if(name.equals(module.getClassName())){
				return module;
			}
		}
		return null;
	}
	
	private PC_ModuleObject getCommonClassWithName(String name){
		for(PC_ModuleObject module:commonModules){
			if(name.equals(module.getClassName())){
				return module;
			}
		}
		return null;
	}
	
	public HashMap<String, PC_ModuleObject> getModules(){
		return new HashMap<String, PC_ModuleObject>(modules);
	}
	
	private void sortModules(){
		for(Entry<String, PC_ModuleObject>e:modules.entrySet()){
			PC_ModuleObject module = e.getValue();
			if(module.getName().equals("management")){
				management = module;
				startList[0].add(management);
			}else{
				String dependencies = module.getDependencies();
				String[] dependenciesList = dependencies.split(":", 2);
				dependenciesList[0] = dependenciesList[0].trim();
				if(dependenciesList[1]!=null){
					dependenciesList[1] = dependenciesList[1].trim();
				}
				int addList=1;
				if(dependenciesList[0].equals("required-before") || dependenciesList[0].equals("before")){
					if(dependenciesList[1].equals("*")){
						addList = 0;
					}else{
						String[] modulesList = dependenciesList[1].split(",");
						for(String modulesName:modulesList){
							PC_ModuleObject depModule = modules.get(modulesName.trim());
							if(depModule!=null){
								depModule.addModuleLoadBevore(module);
							}
						}
					}
				}else if(dependenciesList[0].equals("required-after") || dependenciesList[0].equals("after")){
					if(dependenciesList[1].equals("*")){
						addList = 2;
					}else{
						String[] modulesList = dependenciesList[1].split(",");
						for(String modulesName:modulesList){
							PC_ModuleObject depModule = modules.get(modulesName.trim());
							if(depModule!=null){
								module.addModuleLoadBevore(depModule);
							}
						}
					}
				}else if(dependenciesList[0].equals("")){
					
				}
				startList[addList].add(module);
			}
		}
	}
	
	public void search(Object...o){
		for(int i=0; i<o.length; i++){
			if(o[i] instanceof File){
				File file = (File)o[i];
				boolean addFile = false;
				if(i+1<o.length && o[i+1] instanceof Boolean){
					addFile = (Boolean)o[i+1];
					i++;
				}
				if(file.exists()){
					this.addFile = addFile;
					startFile = file;
					searchDir(file);
				}
			}
		}
		for(PC_ModuleObject module:clinetModules){
			modules.put(module.getName(), module);
		}
		for(PC_ModuleObject module:commonModules){
			modules.put(module.getName(), module);
		}
	}

	private void addFileToClassLoader(File file){
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
	
	private void searchDir(File file) {
		if(file.isDirectory()){
			addFileToClassLoader(file);
			for(File f:file.listFiles()){
				searchDir(f);
			}
		}else if(file.isFile()){
			if(file.getName().endsWith(".class")){
				loadFile = file;
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

	private void searchZip(File file) {
		try {
			addFileToClassLoader(file);
			ZipFile zip = new ZipFile(file);
			loadFile = file;
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
	
	private void loadClass(InputStream is) {
		byte[] b;
		try {
			b = new byte[is.available()];
			is.read(b);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		ClassReader cr = new ClassReader(b);
		cr.accept(classReader, 0);
	}
	
	public void loadModules() {
		sortModules();
		management.load();
		for(int i=0; i<3; i++){
			for(PC_ModuleObject module:startList[0]){
				module.load();
			}
		}
	}
	
	public PC_ModuleObject getManagement() {
		return management;
	}
	
}
