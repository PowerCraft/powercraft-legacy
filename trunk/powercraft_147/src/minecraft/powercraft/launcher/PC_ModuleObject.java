package powercraft.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PC_ModuleObject {

	private int access;
	private String name;
	private String signature;
	private String superName;
	private String[] interfaces;
	private PC_AnnotationVisitor annotationVisitor;
	private Class<?> moduleClass;
	private Object module;
	private boolean isLoaded=false;
	private List<PC_ModuleObject> after = new ArrayList<PC_ModuleObject>();
	private File file;
	private File startFile;
	private PC_Property config;
	
	public PC_ModuleObject(int access, String name, String signature, String superName, String[] interfaces, PC_AnnotationVisitor annotationVisitor, File file, File startFile){
		this.access = access;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
		this.annotationVisitor = annotationVisitor;
		this.file = file;
		this.startFile = startFile;
	}
	
	public void addModuleLoadBevore(PC_ModuleObject bevore){
		after.add(bevore);
	}
	
	public void load(){
		if(!isLoaded){
			isLoaded = true;
			for(PC_ModuleObject module:after){
				module.load();
			}
			try {
				moduleClass = Class.forName(name.replace('/', '.'));
				module = moduleClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Class<?> getModuleClass() {
		return moduleClass;
	}
	
	public String getName(){
		return annotationVisitor.getModuleName();
	}
	
	public PC_Version getVersion() {
		return annotationVisitor.getVersion();
	}
	
	public String getDependencies() {
		return annotationVisitor.getDependencies();
	}
	
	public File getFile() {
		return file;
	}

	public File getStartFile() {
		return startFile;
	}
	
	public Object getModule(){
		return module;
	}
	
	public Object callMethod(String name, Class<?>[] classes, Object[] objects) {
		Class<?> c = module.getClass();
		
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
		Class<?> c = module.getClass();
		
		while(c!=null){
			
			Method ma[] = c.getDeclaredMethods();
			for(Method m:ma){
				if(m.isAnnotationPresent(annontation)){
					try {
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

	public String getClassName() {
		return name;
	}
	
	public String getSuperClassName() {
		return superName;
	}

	public PC_AnnotationVisitor getAnnotationVisitor() {
		return annotationVisitor;
	}
	
	public void setAnnotationVisitor(PC_AnnotationVisitor annotationVisitor) {
		this.annotationVisitor = annotationVisitor;
	}
	
	public void loadConfig(){
		File f = new File(PC_LauncherUtils.getMCDirectory(), "config/PowerCraft-"+annotationVisitor.getModuleName()+".cfg");
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
		File f = new File(PC_LauncherUtils.getMCDirectory(), "config/PowerCraft-"+annotationVisitor.getModuleName()+".cfg");
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
	
}
