package powercraft.management;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.TileEntity;
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

@Mod(modid = "PowerCraft", name = "PowerCraft", version = "3.5.0AlphaE")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, clientPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_ClientPacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = { "PowerCraft" }, packetHandler = PC_PacketHandler.class))
public class mod_PowerCraft {

	@SidedProxy(clientSide = "powercraft.management.PC_ClientProxy", serverSide = "powercraft.management.PC_CommonProxy")
	public static PC_CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.initUtils();
		PC_Logger.init(PC_Utils.getPowerCraftFile());
		PC_Logger.enterSection("PreInit");
		PC_GlobalVariables.loadConfig();
		PC_Logger.enterSection("Register Hacks");
		hackInfo();
		proxy.hack();
		PC_Logger.exitSection();
		PC_Logger.enterSection("Load Modules");
		PC_ModuleLoader.load(PC_Utils.createFile(PC_Utils.getPowerCraftFile(), "Modules"));
		PC_ModuleLoader.load(new File(PC_Utils.getMCDirectory(), "mods"));
		try {
			PC_ModuleLoader.load(new File(mod_PowerCraft.class.getResource("../../").toURI()));
		} catch (URISyntaxException e) {}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module PreInit");
		List<PC_IModule> modules = PC_Utils.getModules();
		for(PC_IModule module:modules){
			module.preInit();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Property Init");
		for(PC_IModule module:modules){
			module.initProperties(PC_Utils.getConfig(module));
		}
		PC_Logger.exitSection();
		if(PC_Utils.isClient()){
		PC_Logger.enterSection("Module Language Init");
			for(PC_IModule module:modules){
				List<PC_LangEntry> l = ((PC_IClientModule) module).initLanguage(new ArrayList<PC_LangEntry>());
				if(l!=null){
					PC_Utils.registerLanguage(module, l.toArray(new PC_LangEntry[0]));
				}
			}
			PC_Logger.exitSection();
			PC_Logger.enterSection("Module Texture Init");
			for(PC_IModule module:modules){
				if(module instanceof PC_IClientModule){
					List<String> l = ((PC_IClientModule) module).loadTextureFiles(new ArrayList<String>());
					if(l!=null){
						PC_Utils.registerTextureFiles(l.toArray(new String[0]));
					}
				}
			}
			PC_Utils.registerTextureFiles(PC_Utils.getPowerCraftLoaderImageDir() + "PowerCraft.png");
			PC_Logger.exitSection();
		}
		PC_Logger.exitSection();
	}

	@Init
	public void init(FMLInitializationEvent event) {
		PC_Logger.enterSection("Init");
		GameRegistry.registerWorldGenerator(new PC_WorldGenerator());
		GameRegistry.registerFuelHandler(new PC_FuelHandler());
		List<PC_IModule> modules = PC_Utils.getModules();
		proxy.init();
		PC_Logger.enterSection("Module Init");
		for(PC_IModule module:modules){
			module.init();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Block Init");
		for(PC_IModule module:modules){
			module.initBlocks();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Item Init");
		for(PC_IModule module:modules){
			module.initItems();
		}
		PC_Logger.exitSection();
		if(PC_Utils.isClient()){
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
		PC_Logger.enterSection("Module PostInit");
		List<PC_IModule> modules = PC_Utils.getModules();
		for(PC_IModule module:modules){
			module.postInit();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Recipes Init");
		for(PC_IModule module:modules){
			List<IRecipe> l = module.initRecipes(new ArrayList<IRecipe>());
			if(l!=null){
				for(IRecipe recipe:l){
					GameRegistry.addRecipe(recipe);
				}
			}
		}
		PC_Logger.exitSection();
		if(PC_Utils.isClient()){
			PC_Logger.enterSection("Module Language Saving");
			for(PC_IModule module:modules){
				PC_Utils.saveLanguage(module);
			}
			PC_Logger.exitSection();
		}
		PC_Logger.enterSection("Module Config Saving");
		for(PC_IModule module:modules){
			PC_Utils.saveConfig(module);
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
         mm.logoFile = PC_Utils.getPowerCraftLoaderImageDir() + "PowerCraft.png";
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
