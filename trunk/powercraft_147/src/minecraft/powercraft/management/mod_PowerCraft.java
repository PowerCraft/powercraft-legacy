package powercraft.management;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.moduleloader.PC_ModuleLoader;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.reflect.PC_FieldWithAnnotation;
import powercraft.management.reflect.PC_IFieldAnnotationIterator;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_BlockRegistry;
import powercraft.management.registry.PC_DataHandlerRegistry;
import powercraft.management.registry.PC_EntityRegistry;
import powercraft.management.registry.PC_GresRegistry;
import powercraft.management.registry.PC_ItemRegistry;
import powercraft.management.registry.PC_KeyRegistry;
import powercraft.management.registry.PC_LangRegistry;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.registry.PC_RecipeRegistry;
import powercraft.management.registry.PC_TextureRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "PowerCraft", name = "PowerCraft", version = "3.5.0AlphaJ", dependencies = "after:*")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, clientPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_ClientPacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_PacketHandler.class))
public class mod_PowerCraft{
	
	@SidedProxy(clientSide = "powercraft.management.PC_ClientProxy", serverSide = "powercraft.management.PC_CommonProxy")
	public static PC_CommonProxy proxy;

	private static final String updateInfoPath = "https://dl.dropbox.com/s/nrkmh98nchr7nrj/VersionInfo.xml?dl=1";
	
	private static mod_PowerCraft instance;

	public static PC_CreativeTab creativeTab; 
	
	public static mod_PowerCraft getInstance(){
		return instance;
	}
	
	public mod_PowerCraft(){
		instance = this;
	}
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.initUtils();
		PC_Logger.init(GameInfo.getPowerCraftFile());
		PC_Logger.enterSection("PreInit");
		PC_GlobalVariables.loadConfig();
		PC_Logger.enterSection("Register Hacks");
		hackInfo();
		proxy.hack();
		PC_Logger.exitSection();
		PC_Logger.enterSection("Load Modules");
		PC_ModuleLoader.load(ModuleLoader.createFile(GameInfo.getPowerCraftFile(), "Modules"));
		PC_ModuleLoader.load(new File(GameInfo.getMCDirectory(), "mods"));
		try {
			PC_ModuleLoader.load(new File(mod_PowerCraft.class.getResource("../../").toURI()));
		} catch (Throwable e) {}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Download Update Info");
		PC_UpdateManager.downloadUpdateInfo(updateInfoPath);
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module PreInit");
		List<PC_IModule> modules = PC_ModuleRegistry.getModules();
		for(PC_IModule module:modules){
			module.preInit();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Property Init");
		for(PC_IModule module:modules){
			module.initProperties(PC_ModuleRegistry.getConfig(module));
		}
		PC_KeyRegistry.setReverseKey(PC_GlobalVariables.config);
		PC_Logger.exitSection();
		if(PC_Utils.GameInfo.isClient()){
			PC_Logger.enterSection("Module Language Init");
			for(PC_IModule module:modules){
				List<LangEntry> l = ((PC_IClientModule) module).initLanguage(new ArrayList<LangEntry>());
				if(l!=null){
					PC_LangRegistry.registerLanguage(module, l.toArray(new LangEntry[0]));
				}
			}
			PC_Logger.exitSection();
			PC_Logger.enterSection("Module Texture Init");
			for(PC_IModule module:modules){
				if(module instanceof PC_IClientModule){
					List<String> l = ((PC_IClientModule) module).loadTextureFiles(new ArrayList<String>());
					if(l!=null){
						PC_TextureRegistry.registerTextureFiles(l.toArray(new String[0]));
					}
				}
			}
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getPowerCraftLoaderImageDir() + "PowerCraft.png");
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getPowerCraftLoaderImageDir() + "laser.png");
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getPowerCraftLoaderImageDir() + "fan.png");
			
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getGresImgDir() + "button.png");
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getGresImgDir() + "dialog.png");
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getGresImgDir() + "frame.png");
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getGresImgDir() + "scrollbar_handle.png");
			PC_TextureRegistry.registerTextureFiles(PC_TextureRegistry.getGresImgDir() + "widgets.png");
			
			PC_Logger.exitSection();
		}
		PC_Logger.exitSection();
	}

	@Init
	public void init(FMLInitializationEvent event) {
		PC_Logger.enterSection("Init");
		GameRegistry.registerWorldGenerator(new PC_WorldGenerator());
		GameRegistry.registerFuelHandler(new PC_FuelHandler());
		PC_ThreadManager.init();
		List<PC_IModule> modules = PC_ModuleRegistry.getModules();
		proxy.init();
		creativeTab = new PC_CreativeTab();
		PC_Logger.enterSection("Module Init");
		for(PC_IModule module:modules){
			module.init();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Field Init");
		for(PC_IModule module:modules){
			final PC_IModule m = module;
			PC_ReflectHelper.getAllFieldsWithAnnotation(module.getClass(), module, PC_FieldObject.class, new PC_IFieldAnnotationIterator<PC_FieldObject>(){

				@Override
				public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_FieldObject> fieldWithAnnotation) {
					Class<?> clazz = fieldWithAnnotation.getAnnotation().clazz();
					if(PC_Block.class.isAssignableFrom(clazz)){
						Object block = PC_BlockRegistry.register(m, (Class<? extends PC_Block>)clazz);
						fieldWithAnnotation.setValue(block);
					}else if(PC_Item.class.isAssignableFrom(clazz) || PC_ItemArmor.class.isAssignableFrom(clazz)){
						Object item = PC_ItemRegistry.register(m, (Class<? extends Item>)clazz);
						fieldWithAnnotation.setValue(item);
					}
					return false;
				}
				
			});
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Entity Init");
		for(PC_IModule module:modules){
			List<Class<? extends Entity>> l = module.initEntities(new ArrayList<Class<? extends Entity>>());
			if(l!=null){
				for(Class<? extends Entity> entity:l){
					PC_EntityRegistry.register(module, entity);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Gui Init");
		for(PC_IModule module:modules){
			List<PC_Struct2<String,Class>> l = module.registerGuis(new ArrayList<PC_Struct2<String,Class>>());
			if(l!=null){
				for(PC_Struct2<String,Class> g:l){
					PC_GresRegistry.registerGres(g.a, g.b);
				}
			}
		}
		PC_Logger.exitSection();
		if(PC_Utils.GameInfo.isClient()){
			PC_Logger.enterSection("Module Splashes Init");
			for(PC_IModule module:modules){
				if(module instanceof PC_IClientModule){
					List<String> l = ((PC_IClientModule) module).addSplashes(new ArrayList<String>());
					if(l!=null){
						PC_GlobalVariables.splashes.addAll(l);
					}
				}
			}
			
			PC_GlobalVariables.splashes.add("GRES");

	        for (int i = 0; i < 10; i++)
	        {
	        	PC_GlobalVariables.splashes.add("Modded by MightyPork!");
	        }

	        for (int i = 0; i < 6; i++)
	        {
	        	PC_GlobalVariables.splashes.add("Modded by XOR!");
	        }

	        for (int i = 0; i < 5; i++)
	        {
	        	PC_GlobalVariables.splashes.add("Modded by Rapus95!");
	        }

	        for (int i = 0; i < 4; i++)
	        {
	        	PC_GlobalVariables.splashes.add("Reviewed by RxD");
	        }

	        PC_GlobalVariables.splashes.add("Modded by masters!");

	        for (int i = 0; i < 3; i++)
	        {
	        	PC_GlobalVariables.splashes.add("PowerCraft " + getModMetadata().version);
	        }

	        PC_GlobalVariables.splashes.add("Null Pointers included!");
	        PC_GlobalVariables.splashes.add("ArrayIndexOutOfBoundsException");
	        PC_GlobalVariables.splashes.add("Null Pointer loves you!");
	        PC_GlobalVariables.splashes.add("Unstable!");
	        PC_GlobalVariables.splashes.add("Buggy code!");
	        PC_GlobalVariables.splashes.add("Break it down!");
	        PC_GlobalVariables.splashes.add("Addictive!");
	        PC_GlobalVariables.splashes.add("Earth is flat!");
	        PC_GlobalVariables.splashes.add("Faster than Atari!");
	        PC_GlobalVariables.splashes.add("DAFUQ??");
	        PC_GlobalVariables.splashes.add("LWJGL");
	        PC_GlobalVariables.splashes.add("Don't press the button!");
	        PC_GlobalVariables.splashes.add("Press the button!");
	        PC_GlobalVariables.splashes.add("Ssssssssssssssss!");
	        PC_GlobalVariables.splashes.add("C'mon!");
	        PC_GlobalVariables.splashes.add("Redstone Wizzard!");
	        PC_GlobalVariables.splashes.add("Keep your mods up-to-date!");
	        PC_GlobalVariables.splashes.add("Read the changelog!");
	        PC_GlobalVariables.splashes.add("Read the log files!");
	        PC_GlobalVariables.splashes.add("Discoworld!");
	        PC_GlobalVariables.splashes.add("Also try ICE AGE mod!");
	        PC_GlobalVariables.splashes.add("Also try Backpack mod!");
	        
	        PC_Logger.exitSection();
		}
		PC_Logger.exitSection();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		PC_Logger.enterSection("PostInit");
		PC_Logger.enterSection("Module Recipes Init");
		List<PC_IModule> modules = PC_ModuleRegistry.getModules();
		for(PC_IModule module:modules){
			List<PC_IRecipe> l = module.initRecipes(new ArrayList<PC_IRecipe>());
			if(l!=null){
				for(Object recipe:l){
					if(recipe instanceof PC_IRecipe){
						PC_RecipeRegistry.register((PC_IRecipe)recipe);
					}
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Data Handlers Init");
		PC_DataHandlerRegistry.regsterDataHandler("chunckUpdateForcer", PC_ChunkUpdateForcer.getInstance());
		for(PC_IModule module:modules){
			List<PC_Struct2<String, PC_IDataHandler>> l = module.initDataHandlers(new ArrayList<PC_Struct2<String, PC_IDataHandler>>());
			if(l!=null){
				for(PC_Struct2<String, PC_IDataHandler> dataHandler:l){
					PC_DataHandlerRegistry.regsterDataHandler(dataHandler.a, dataHandler.b);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module MSG Objects Init");
		PC_MSGRegistry.registerMSGObject(PC_ChunkUpdateForcer.getInstance());
		for(PC_IModule module:modules){
			List<PC_IMSG> l = module.initMSGObjects(new ArrayList<PC_IMSG>());
			if(l!=null){
				for(PC_IMSG msgObject:l){
					PC_MSGRegistry.registerMSGObject(msgObject);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Packet Handlers Init");
		for(PC_IModule module:modules){
			List<PC_Struct2<String, PC_IPacketHandler>> l = module.initPacketHandlers(new ArrayList<PC_Struct2<String, PC_IPacketHandler>>());
			if(l!=null){
				for(PC_Struct2<String, PC_IPacketHandler> packetHandler:l){
					PC_PacketHandler.registerPackethandler(packetHandler.a, packetHandler.b);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module PostInit");
		for(PC_IModule module:modules){
			module.postInit();
		}
		PC_Logger.exitSection();
		if(PC_Utils.GameInfo.isClient()){
			PC_Logger.enterSection("Module Language Saving");
			for(PC_IModule module:modules){
				PC_LangRegistry.saveLanguage(module);
			}
			PC_Logger.exitSection();
		}
		PC_Logger.enterSection("Module Config Saving");
		for(PC_IModule module:modules){
			SaveHandler.saveConfig(module);
		}
		PC_GlobalVariables.saveConfig();
		PC_Logger.exitSection();
		PC_Logger.exitSection();
	}

 	public ModContainer getModContainer()
    {
        List<ModContainer> modContainers = Loader.instance().getModList();

        for (ModContainer modContainer: modContainers)
        {
            if (modContainer.matches(this))
            {
                return modContainer;
            }
        }

        return null;
    }

    public ModMetadata getModMetadata()
    {
        return getModContainer().getMetadata();
    }
	
    public void hackInfo(){
    	 ModMetadata mm = getModMetadata();
         mm.autogenerated = false;
         mm.authorList = new ArrayList<String>();
         mm.authorList.add("XOR");
         mm.authorList.add("Rapus");
         mm.credits = "MightyPork, RxD, LOLerul2";
         mm.description = "";
         mm.logoFile = PC_TextureRegistry.getPowerCraftLoaderImageDir() + "PowerCraft.png";
         mm.url = "http://powercrafting.net/";
    }
    
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlock){
		if(itemBlock==null)
			GameRegistry.registerBlock(block);
		else
			GameRegistry.registerBlock(block, itemBlock);
	}

	public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass) {
		GameRegistry.registerTileEntity(tileEntityClass, tileEntityClass.getName());
	}
	
	public static void addStringLocalization(String key, String lang, String value) {
		LanguageRegistry.instance().addStringLocalization(key, lang, value);
	}
	
}
