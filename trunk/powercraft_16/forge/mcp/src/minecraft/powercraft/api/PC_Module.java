package powercraft.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_IBlock;
import powercraft.api.items.PC_Item;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public abstract class PC_Module {

	private Configuration config;
	private List<PC_IBlock> blocks = new ArrayList<PC_IBlock>();
	private List<PC_Item> items = new ArrayList<PC_Item>();
	
	
	public abstract void preInit(FMLPreInitializationEvent event);
	
	public abstract void init(FMLInitializationEvent event);
	
	public abstract void postInit(FMLPostInitializationEvent event);
	
	protected void defaultPreInit(FMLPreInitializationEvent event){
		PC_Logger.info("Create module %s", getMetadata().name);
		config = new Configuration(event.getSuggestedConfigurationFile());
		generateFields();
		saveConfig();
	}
	
	protected void defaultInit(FMLInitializationEvent event) {
		for(PC_IBlock block:blocks){
			PC_Registry.registerRecipes(this, block);
		}
		for(PC_Item item:items){
			PC_Registry.registerRecipes(this, item);
		}
	}
	
	private void generateFields(){
		Field[] fields = getClass().getDeclaredFields();
		for(int i=0; i<fields.length; i++){
			if(fields[i].isAnnotationPresent(PC_FiledGenerator.class)){
				generateField(fields[i]);
			}
		}
	}
	
	private void generateField(Field field){
		PC_FiledGenerator generator = field.getAnnotation(PC_FiledGenerator.class);
		Class<?> clazz = generator.value();
		if(clazz==void.class){
			clazz = field.getType();
		}
		try {
			Object object = createClass(clazz);
			field.setAccessible(true);
			field.set(this, object);
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Failed to initialize field %s in module %s", field.getName(), getMetadata().name);
		} 
	}
	
	private Object createClass(Class<?> clazz) throws InstantiationException, IllegalAccessException{
		if(clazz.isAnnotationPresent(PC_BlockInfo.class)){
			Block block = PC_Registry.registerBlock(this, clazz);
			if(block instanceof PC_IBlock){
				blocks.add((PC_IBlock) block);
			}
			return block;
		}else if(PC_Item.class.isAssignableFrom(clazz)){
			Item item = PC_Registry.registerItem(this, (Class<? extends PC_Item>)clazz);
			if(item instanceof PC_Item){
				items.add((PC_Item) item);
			}
			return item;
		}else{
			return clazz.newInstance();
		}
	}
	
	public ModContainer getContainer(){
		List<ModContainer> modContainers = Loader.instance().getModList();
		for(ModContainer modContainer:modContainers){
			if(modContainer.matches(this)){
				return modContainer;
			}
		}
		return null;
	}
	
	public ModMetadata getMetadata(){
		ModContainer modContainer = getContainer();
		return modContainer.getMetadata();
	}

	public Configuration getConfig() {
		return config;
	}
	
	public void saveConfig(){
		config.save();
	}
	
}
