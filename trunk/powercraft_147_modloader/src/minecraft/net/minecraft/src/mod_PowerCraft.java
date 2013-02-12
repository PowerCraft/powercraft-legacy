package net.minecraft.src;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import powercraft.management.PC_ChunkUpdateForcer;
import powercraft.management.PC_ClientPacketHandler;
import powercraft.management.PC_ClientRenderer;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_FuelHandler;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_GuiUpdateNotification;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Logger;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_ThreadManager;
import powercraft.management.PC_TickHandler;
import powercraft.management.PC_UpdateManager;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.entity.PC_EntityFanFX;
import powercraft.management.entity.PC_EntityLaserFX;
import powercraft.management.entity.PC_EntityLaserParticleFX;
import powercraft.management.hacks.PC_ClientHacks;
import powercraft.management.hacks.PC_MainMenuHacks;
import powercraft.management.hacks.PC_RenderItemHack;
import powercraft.management.hacks.PC_RenderPlayerHack;
import powercraft.management.hacks.PC_RenderSkeletonHack;
import powercraft.management.hacks.PC_RenderZombieHack;
import powercraft.management.moduleloader.PC_ModuleLoader;
import powercraft.management.recipes.PC_3DRecipe;
import powercraft.management.recipes.PC_3DRecipeManager;
import powercraft.management.PC_WorldGenerator;

public class mod_PowerCraft extends BaseMod {

	private static final String updateInfoPath = "https://dl.dropbox.com/s/nrkmh98nchr7nrj/VersionInfo.xml?dl=1";
	
	private PC_WorldGenerator worldGenerator;
	
	private PC_FuelHandler fuelHandler;
	
	private PC_ClientRenderer cr1, cr2;
	
	private PC_MainMenuHacks mainMenuHacks = new PC_MainMenuHacks();
	
	private PC_ClientPacketHandler packetHandler;
	
	private PC_TickHandler tickHandler = new PC_TickHandler();
	
	private static mod_PowerCraft instance;
	
	private static List<KeyBinding> watchKeysForUp = new ArrayList<KeyBinding>();
	
	public static mod_PowerCraft getInstance() {
		return instance;
	}
	
	public mod_PowerCraft(){
		instance = this;
	}
	
	@Override
	public String getVersion() {
		return "3.5.0AlphaI";
	}

	@Override
	public String getName(){
		return "PowerCraft";
	}
	
	@Override
	public String getPriorities(){
		return "after:*";
	}
	
	@Override
	public void load() {
		preInit();
		init();
		postInit();
	}
	
	@Override
	public void generateNether(World world, Random random, int chunkX, int chunkZ) {
		worldGenerator.generate(random, chunkX, chunkZ, world, null, null);
	}

	@Override
    public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
    	worldGenerator.generate(random, chunkX, chunkZ, world, null, null);
    }
	
	@Override
	public int addFuel(int var1, int var2)
    {
		return fuelHandler.getBurnTime(new ItemStack(var1, 1, var2));
    }
	
	@Override
	public boolean renderWorldBlock(RenderBlocks renderer, IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId) {
		if(modelId == PC_ClientRenderer.getRendererID(true)){
			return cr1.renderWorldBlock(blockAccess, x, y, z, block, modelId, renderer);
		}else if(modelId == PC_ClientRenderer.getRendererID(false)){
			return cr2.renderWorldBlock(blockAccess, x, y, z, block, modelId, renderer);
		}
		return false;
	}

	@Override
	public void renderInvBlock(RenderBlocks renderer, Block block, int metadata, int modelId) {
		if(modelId == PC_ClientRenderer.getRendererID(true)){
			cr1.renderInventoryBlock(block, metadata, modelId, renderer);
		}else if(modelId == PC_ClientRenderer.getRendererID(false)){
			cr2.renderInventoryBlock(block, metadata, modelId, renderer);
		}
	}
	
	@Override
	public void keyboardEvent(KeyBinding kb) {
		if(kb.pressed && !watchKeysForUp.contains(kb)){
			PC_ClientUtils.keyDown(kb.keyDescription);
			watchKeysForUp.add(kb);
		}
	}
	
	@Override
	public boolean onTickInGUI(float var1, Minecraft var2, GuiScreen var3) {
		mainMenuHacks.tickStart();
		return true;
	}
	
	@Override
	public boolean onTickInGame(float var1, Minecraft var2) {
		tickHandler.tick();
		Iterator<KeyBinding> i = watchKeysForUp.iterator();
		while(i.hasNext()){
			KeyBinding kb = i.next();
			if(!kb.pressed){
				i.remove();
				PC_ClientUtils.keyUp(kb.keyDescription);
			}
		}
		return true;
	}

	@Override
	public void serverCustomPayload(NetServerHandler netServerHandler, Packet250CustomPayload packet){
		packetHandler.onPacketData(netServerHandler.netManager, packet, netServerHandler.playerEntity);
	}
	
	@Override
	public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload packet){
		packetHandler.onPacketData(var1.getNetManager(), packet, PC_ClientUtils.mc().thePlayer);
	}
	
	@Override
	public void clientConnect(NetClientHandler var1) {
		PC_PacketHandler.requestIDList();
	}
	
	@Override
	public void addRenderer(Map map) {
		if(PC_Utils.GameInfo.isClient()){
			PC_Logger.enterSection("Register EntityRender");
			List<PC_IModule> modules = ModuleInfo.getModules();
			for(PC_IModule module:modules){
				if(module instanceof PC_IClientModule){
					List<PC_Struct2<Class<? extends Entity>, Render>> list = ((PC_IClientModule) module).registerEntityRender(new ArrayList<PC_Struct2<Class<? extends Entity>, Render>>());
					if(list!=null){
						for(PC_Struct2<Class<? extends Entity>, Render> s:list){
							map.put(s.a, s.b);
						}
					}
				}
			}
			map.put(EntityPlayer.class, new PC_RenderPlayerHack());
			map.put(EntitySkeleton.class, new PC_RenderSkeletonHack());
			map.put(EntityZombie.class, new PC_RenderZombieHack());
			map.put(EntityItem.class, new PC_RenderItemHack());
			PC_Logger.exitSection();
		}
	}

	public void preInit() {
		PC_ClientUtils.create();
		PC_Logger.init(GameInfo.getPowerCraftFile());
		PC_Logger.enterSection("PreInit");
		PC_GlobalVariables.loadConfig();
		PC_Logger.enterSection("Register Hacks");
		PC_ClientHacks.hackClient();
		PC_Logger.exitSection();
		PC_Logger.enterSection("Load Modules");
		PC_ModuleLoader.load(ModuleLoader.createFile(GameInfo.getPowerCraftFile(), "Modules"));
		PC_ModuleLoader.load(new File(GameInfo.getMCDirectory(), "mods"));
		try {
			PC_ModuleLoader.load(new File(mod_PowerCraft.class.getResource("../../../").toURI()));
		} catch (Throwable e) {}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Download Update Info");
		PC_UpdateManager.downloadUpdateInfo(updateInfoPath);
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module PreInit");
		List<PC_IModule> modules = ModuleInfo.getModules();
		for(PC_IModule module:modules){
			module.preInit();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Property Init");
		for(PC_IModule module:modules){
			module.initProperties(SaveHandler.getConfig(module));
		}
		ValueWriting.setReverseKey(PC_GlobalVariables.config);
		PC_Logger.exitSection();
		if(PC_Utils.GameInfo.isClient()){
			PC_Logger.enterSection("Module Language Init");
			for(PC_IModule module:modules){
				List<PC_LangEntry> l = ((PC_IClientModule) module).initLanguage(new ArrayList<PC_LangEntry>());
				if(l!=null){
					Lang.registerLanguage(module, l.toArray(new PC_LangEntry[0]));
				}
			}
			PC_Logger.exitSection();
			PC_Logger.enterSection("Module Texture Init");
			for(PC_IModule module:modules){
				if(module instanceof PC_IClientModule){
					List<String> l = ((PC_IClientModule) module).loadTextureFiles(new ArrayList<String>());
					if(l!=null){
						ModuleLoader.registerTextureFiles(l.toArray(new String[0]));
					}
				}
			}
			ModuleLoader.registerTextureFiles(ModuleInfo.getPowerCraftLoaderImageDir() + "PowerCraft.png");
			ModuleLoader.registerTextureFiles(ModuleInfo.getGresImgDir() + "button.png");
			ModuleLoader.registerTextureFiles(ModuleInfo.getGresImgDir() + "dialog.png");
			ModuleLoader.registerTextureFiles(ModuleInfo.getGresImgDir() + "frame.png");
			ModuleLoader.registerTextureFiles(ModuleInfo.getGresImgDir() + "scrollbar_handle.png");
			ModuleLoader.registerTextureFiles(ModuleInfo.getGresImgDir() + "widgets.png");
			PC_Logger.exitSection();
		}
		PC_Logger.exitSection();
	}

	public void init() {
		PC_Logger.enterSection("Init");
		worldGenerator = new PC_WorldGenerator();
		fuelHandler = new PC_FuelHandler();
		mainMenuHacks = new PC_MainMenuHacks();
		packetHandler = new PC_ClientPacketHandler();
		ModLoader.registerPacketChannel(this, "PowerCraft");
		ModLoader.setInGUIHook(this, true, false);
		ModLoader.setInGameHook(this, true, false);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserParticleFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityFanFX.class);
		PC_ClientUtils.registerEnitiyFX("EntitySmokeFX", EntitySmokeFX.class);
		PC_ThreadManager.init();
		List<PC_IModule> modules = ModuleInfo.getModules();
		cr1 = new PC_ClientRenderer(true);
		cr2 = new PC_ClientRenderer(false);
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
		PC_Logger.enterSection("Module Entity Init");
		for(PC_IModule module:modules){
			module.initEntities();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Gui Init");
		for(PC_IModule module:modules){
			List<PC_Struct2<String,Class>> l = module.registerGuis(new ArrayList<PC_Struct2<String,Class>>());
			if(l!=null){
				for(PC_Struct2<String,Class> g:l){
					Gres.registerGres(g.a, g.b);
				}
			}
			Gres.registerGres("UpdateNotification", PC_GuiUpdateNotification.class);
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
	        	PC_GlobalVariables.splashes.add("PowerCraft " + getVersion());
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

	public void postInit() {
		PC_Logger.enterSection("PostInit");
		PC_Logger.enterSection("Module Recipes Init");
		List<PC_IModule> modules = ModuleInfo.getModules();
		for(PC_IModule module:modules){
			List<Object> l = module.initRecipes(new ArrayList<Object>());
			if(l!=null){
				for(Object recipe:l){
					if(recipe instanceof IRecipe){
						CraftingManager.getInstance().getRecipeList().add(recipe);
					}else if(recipe instanceof PC_3DRecipe){
						PC_3DRecipeManager.add3DRecipe((PC_3DRecipe)recipe);
					}else{
						PC_Logger.severe("Unknown recipe of type "+recipe.getClass().getSimpleName());
					}
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Data Handlers Init");
		ModuleLoader.regsterDataHandler("chunckUpdateForcer", PC_ChunkUpdateForcer.getInstance());
		for(PC_IModule module:modules){
			List<PC_Struct2<String, PC_IDataHandler>> l = module.initDataHandlers(new ArrayList<PC_Struct2<String, PC_IDataHandler>>());
			if(l!=null){
				for(PC_Struct2<String, PC_IDataHandler> dataHandler:l){
					ModuleLoader.regsterDataHandler(dataHandler.a, dataHandler.b);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module MSG Objects Init");
		ModuleInfo.registerMSGObject(PC_ChunkUpdateForcer.getInstance());
		for(PC_IModule module:modules){
			List<PC_IMSG> l = module.initMSGObjects(new ArrayList<PC_IMSG>());
			if(l!=null){
				for(PC_IMSG msgObject:l){
					ModuleInfo.registerMSGObject(msgObject);
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
				Lang.saveLanguage(module);
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
    
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlock){
		if(itemBlock==null)
			ModLoader.registerBlock(block);
		else
			ModLoader.registerBlock(block, itemBlock);
	}

	public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass) {
		ModLoader.registerTileEntity(tileEntityClass, tileEntityClass.getName());
	}
	
	public static void addStringLocalization(String key, String lang, String value) {
		ModLoader.addLocalization(key, lang, value);
	}

	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		return new Packet23VehicleSpawn(var1, var2);
	}

	@Override
	public Entity spawnEntity(int id, World world, double x, double y, double z) {
		Entity e = EntityList.createEntityByID(id, world);
		e.setPosition(x, y, z);
		return e;
	}
	
}
