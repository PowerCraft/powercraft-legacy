package powercraft.launcher;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.entity.Render;

import org.w3c.dom.Entity;

import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

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
	
	public PC_Property getConfig(){
		return config;
	}
	
	public void setConfig(PC_Property config){
		this.config = config;
	}

	public void saveConfig() {
		// TODO Auto-generated method stub
		
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
	public List<PC_Struct2<Class<? extends Entity>, Integer>> initEntities(List<PC_Struct2<Class<? extends Entity>, Integer>> entities){
		return (List)callMethod(PC_Module.PC_InitEntities.class, new Object[]{entities});
	}
	
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes){
		return (List)callMethod(PC_Module.PC_InitRecipes.class, new Object[]{recipes});
	}
	
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(List<PC_Struct2<String, PC_IDataHandler>> dataHandlers){
		return (List)callMethod(PC_Module.PC_InitDataHandlers.class, new Object[]{dataHandlers});
	}
	
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers){
		return (List)callMethod(PC_Module.PC_InitPacketHandlers.class, new Object[]{packetHandlers});
	}
	
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis){
		return (List)callMethod(PC_Module.PC_RegisterGuis.class, new Object[]{guis});
	}

	public List<LangEntry> initLanguage(ArrayList<LangEntry> arrayList) {
		return (List)callMethod(PC_ClientModule.PC_InitLanguage.class, new Object[]{arrayList});
	}

	public List<String> loadTextureFiles(ArrayList<String> arrayList) {
		return (List)callMethod(PC_ClientModule.PC_LoadTextureFiles.class, new Object[]{arrayList});
	}

	public List<String> addSplashes(ArrayList<String> arrayList) {
		return (List)callMethod(PC_ClientModule.PC_AddSplashes.class, new Object[]{arrayList});
	}
	
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(ArrayList<PC_Struct2<Class<? extends Entity>, Render>> arrayList) {
		return (List)callMethod(PC_ClientModule.PC_RegisterEntityRender.class, new Object[]{arrayList});
	}
	
}
