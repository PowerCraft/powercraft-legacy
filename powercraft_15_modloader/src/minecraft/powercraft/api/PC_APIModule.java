package powercraft.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySmokeFX;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet23VehicleSpawn;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.building.PC_CropHarvesting;
import powercraft.api.building.PC_TreeHarvesting;
import powercraft.api.entity.PC_EntityFanFX;
import powercraft.api.entity.PC_EntityLaserFX;
import powercraft.api.entity.PC_EntityLaserParticleFX;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.hooks.PC_ClientHooks;
import powercraft.api.hooks.PC_Hooks;
import powercraft.api.hooks.PC_ModInfo;
import powercraft.api.hooks.PC_RenderPlayerHook;
import powercraft.api.hooks.PC_RenderSkeletonHook;
import powercraft.api.hooks.PC_RenderZombieHook;
import powercraft.api.interfaces.PC_IDataHandler;
import powercraft.api.interfaces.PC_IMSG;
import powercraft.api.interfaces.PC_IWorldGenerator;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.network.PC_ClientPacketHandler;
import powercraft.api.network.PC_IPacketHandler;
import powercraft.api.network.PC_PacketHandler;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.reflect.PC_FieldWithAnnotation;
import powercraft.api.reflect.PC_IFieldAnnotationIterator;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_BuildingRegistry;
import powercraft.api.registry.PC_ChunkForcerRegistry;
import powercraft.api.registry.PC_DataHandlerRegistry;
import powercraft.api.registry.PC_EntityRegistry;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_ItemRegistry;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_ModuleRegistry;
import powercraft.api.registry.PC_RecipeRegistry;
import powercraft.api.registry.PC_RegistryClient;
import powercraft.api.registry.PC_TickRegistry;
import powercraft.api.registry.PC_WorldGeneratorRegistry;
import powercraft.api.thread.PC_ThreadManager;
import powercraft.api.tick.PC_ITickHandler;
import powercraft.api.tick.PC_TickHandler;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_Utils;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.PC_Logger;
import powercraft.launcher.PC_Property;
import powercraft.launcher.loader.PC_ModLoader;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_Init;
import powercraft.launcher.loader.PC_Module.PC_InitProperties;
import powercraft.launcher.loader.PC_Module.PC_Instance;
import powercraft.launcher.loader.PC_Module.PC_PostInit;
import powercraft.launcher.loader.PC_Module.PC_PreInit;
import powercraft.launcher.loader.PC_ModuleObject;

@PC_Module(name = "Api", version = "3.5.2", modLoader = PC_ModLoader.RISUGAMIS_MODLOADER)
public class PC_APIModule {
	
	private PC_FuelHandler fuelHandler;
	
	private PC_ClientHooks clientHooks = new PC_ClientHooks();
	
	protected PC_PacketHandler packetHandler;
	
	private PC_TickHandler tickHandler = new PC_TickHandler();
	
	private static List<KeyBinding> watchKeysForUp = new ArrayList<KeyBinding>();
	
	private PC_WorldGeneratorRegistry worldGenerator = new PC_WorldGeneratorRegistry();
	
	@PC_Instance
	private static PC_ModuleObject instance;
	
	@PC_PreInit
	public void preInit() {
		initVars();
		powercraft.launcher.PC_PacketHandler.handler = packetHandler;
		PC_Logger.enterSection("PreInit");
		PC_GlobalVariables.loadConfig();
		PC_Logger.enterSection("Register Hacks");
		PC_ClientHooks.registerClientHooks();
		PC_Hooks.registerHooks();
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module PreInit");
		List<PC_ModuleObject> modules = PC_ModuleRegistry.getModuleList();
		for (PC_ModuleObject module : modules) {
			module.preInit();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Property Init");
		for (PC_ModuleObject module : modules) {
			module.initProperties(module.getConfig());
		}
		PC_KeyRegistry.setReverseKey(PC_GlobalVariables.config);
		PC_Logger.exitSection();
		clientPreInit(modules);
		PC_Logger.exitSection();
	}
	
	protected void initVars() {
		PC_Utils.create();
		packetHandler = new PC_PacketHandler();
	}
	
	protected void clientPreInit(List<PC_ModuleObject> modules) {
		
	}
	
	@PC_Init
	public void init() {
		PC_Logger.enterSection("Init");
		fuelHandler = new PC_FuelHandler();
		clientHooks = new PC_ClientHooks();
		packetHandler = new PC_ClientPacketHandler();
		ModLoader.registerPacketChannel(PC_LauncherUtils.getMod(), "PowerCraft");
		ModLoader.setInGUIHook(PC_LauncherUtils.getMod(), true, false);
		ModLoader.setInGameHook(PC_LauncherUtils.getMod(), true, false);
		PC_BuildingRegistry.register(new PC_CropHarvesting());
		PC_BuildingRegistry.register(new PC_TreeHarvesting());
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserParticleFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityLaserFX.class);
		PC_ClientUtils.registerEnitiyFX(PC_EntityFanFX.class);
		PC_ClientUtils.registerEnitiyFX("EntitySmokeFX", EntitySmokeFX.class);
		PC_ThreadManager.init();
		List<PC_ModuleObject> modules = PC_ModuleRegistry.getModuleList();
		PC_Logger.enterSection("Module Init");
		for (PC_ModuleObject module : modules) {
			module.init();
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Field Init");
		for (PC_ModuleObject module : modules) {
			PC_ReflectHelper.getAllFieldsWithAnnotation(module.getModuleClass(), module, PC_FieldObject.class, getModuleFieldInit(module));
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Entity Init");
		for (PC_ModuleObject module : modules) {
			List<PC_Struct2<Class<? extends Entity>, Integer>> l = module.initEntities(new ArrayList<PC_Struct2<Class<? extends Entity>, Integer>>());
			if (l != null) {
				for (PC_Struct2<Class<? extends Entity>, Integer> entity : l) {
					PC_EntityRegistry.register(module, entity.a, entity.b);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Container Init");
		for (PC_ModuleObject module : modules) {
			List<PC_Struct2<String, Class<PC_GresBaseWithInventory>>> l = module
					.registerContainers(new ArrayList<PC_Struct2<String, Class<PC_GresBaseWithInventory>>>());
			if (l != null) {
				for (PC_Struct2<String, Class<PC_GresBaseWithInventory>> g : l) {
					PC_GresRegistry.registerGresContainer(g.a, g.b);
				}
			}
		}
		PC_Logger.exitSection();
		clientInit(modules);
		PC_Logger.exitSection();
	}
	
	protected ModuleFieldInit getModuleFieldInit(PC_ModuleObject module) {
		return new ModuleFieldInit(module);
	}
	
	protected void clientInit(List<PC_ModuleObject> modules) {
		
	}
	
	@PC_PostInit
	public void postInit() {
		PC_Logger.enterSection("PostInit");
		PC_TickRegistry.register(PC_ChunkForcerRegistry.getInstance());
		loadMods();
		PC_Logger.enterSection("Module Recipes Init");
		List<PC_ModuleObject> modules = PC_ModuleRegistry.getModuleList();
		for (PC_ModuleObject module : modules) {
			List<PC_IRecipe> l = module.initRecipes(new ArrayList<PC_IRecipe>());
			if (l != null) {
				for (PC_IRecipe recipe : l) {
					PC_RecipeRegistry.register(recipe);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Data Handlers Init");
		PC_DataHandlerRegistry.regsterDataHandler("chunckUpdateForcer", PC_ChunkForcerRegistry.getInstance());
		for (PC_ModuleObject module : modules) {
			List<PC_Struct2<String, PC_IDataHandler>> l = module.initDataHandlers(new ArrayList<PC_Struct2<String, PC_IDataHandler>>());
			if (l != null) {
				for (PC_Struct2<String, PC_IDataHandler> dataHandler : l) {
					PC_DataHandlerRegistry.regsterDataHandler(dataHandler.a, dataHandler.b);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module Packet Handlers Init");
		for (PC_ModuleObject module : modules) {
			List<PC_Struct2<String, PC_IPacketHandler>> l = module.initPacketHandlers(new ArrayList<PC_Struct2<String, PC_IPacketHandler>>());
			if (l != null) {
				for (PC_Struct2<String, PC_IPacketHandler> packetHandler : l) {
					PC_PacketHandler.registerPackethandler(packetHandler.a, packetHandler.b);
				}
			}
		}
		PC_Logger.exitSection();
		PC_Logger.enterSection("Module PostInit");
		for (PC_ModuleObject module : modules) {
			module.postInit();
		}
		PC_Logger.exitSection();
		clientPostInit(modules);
		PC_Logger.enterSection("Module Config Saving");
		for (PC_ModuleObject module : modules) {
			module.saveConfig();
		}
		PC_GlobalVariables.saveConfig();
		PC_Logger.exitSection();
		PC_Logger.exitSection();
	}
	
	protected void clientPostInit(List<PC_ModuleObject> modules) {
		
	}
	
	private void loadMods() {
		PC_ModInfo modLoader = new PC_ModInfo();
		modLoader.name = "ModLoader";
		modLoader.version = ModLoader.VERSION;
		if (modLoader.version.startsWith(modLoader.name)) {
			modLoader.version = modLoader.version.substring(modLoader.name.length()).trim();
		}
		modLoader.description = "This mod is needed for most other mods and will load them into Minecraft";
		List<BaseMod> list = ModLoader.getLoadedMods();
		for (BaseMod bm : list) {
			new PC_ModInfo(bm);
		}
	}
	
	@PC_InitProperties
	public void initProperties(PC_Property config) {
		
	}
	
	public static PC_APIModule getInstance() {
		return (PC_APIModule) instance.getModule();
	}
	
	public void generateNether(World world, Random random, int chunkX, int chunkZ) {
		generateSurface(world, random, chunkX, chunkZ);
	}
	
	public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		IChunkProvider currentChunkProvider = world.provider.createChunkGenerator();
		IChunkProvider chunkProvider = null;
		if (world instanceof WorldServer) {
			chunkProvider = ((WorldServer) world).theChunkProviderServer;
		}
		worldGenerator.generate(random, chunkX, chunkZ, world, currentChunkProvider, chunkProvider);
	}
	
	public int addFuel(int var1, int var2) {
		return fuelHandler.getBurnTime(new ItemStack(var1, 1, var2));
	}
	
	public boolean renderWorldBlock(RenderBlocks renderer, IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId) {
		return false;
	}
	
	public void renderInvBlock(RenderBlocks renderer, Block block, int metadata, int modelId) {
	}
	
	public void keyboardEvent(KeyBinding kb) {
		if (kb.pressed && !watchKeysForUp.contains(kb)) {
			PC_RegistryClient.keyEvent(kb.keyDescription, true);
			watchKeysForUp.add(kb);
		}
	}
	
	public boolean onTickInGUI(float var1, Minecraft var2, GuiScreen var3) {
		clientHooks.tickStart();
		return true;
	}
	
	public boolean onTickInGame(float var1, Minecraft var2) {
		tickHandler.tick();
		Iterator<KeyBinding> i = watchKeysForUp.iterator();
		while (i.hasNext()) {
			KeyBinding kb = i.next();
			if (!kb.pressed) {
				i.remove();
				PC_RegistryClient.keyEvent(kb.keyDescription, false);
			}
		}
		return true;
	}
	
	public void clientConnect(NetClientHandler var1) {
		PC_PacketHandler.requestIDList();
	}
	
	public void addRenderer(Map map) {
		if (PC_Utils.isClient()) {
			PC_Logger.enterSection("Register EntityRender");
			List<PC_ModuleObject> modules = PC_ModuleRegistry.getModuleList();
			for (PC_ModuleObject module : modules) {
				List<PC_Struct2<Class<? extends Entity>, Render>> list = module
						.registerEntityRender(new ArrayList<PC_Struct2<Class<? extends Entity>, Render>>());
				if (list != null) {
					for (PC_Struct2<Class<? extends Entity>, Render> s : list) {
						map.put(s.a, s.b);
					}
				}
			}
			map.put(EntityPlayer.class, new PC_RenderPlayerHook());
			map.put(EntitySkeleton.class, new PC_RenderSkeletonHook());
			map.put(EntityZombie.class, new PC_RenderZombieHook());
			PC_Logger.exitSection();
		}
	}
	
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {
		if (itemBlock == null)
			ModLoader.registerBlock(block);
		else
			ModLoader.registerBlock(block, itemBlock);
	}
	
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		return new Packet23VehicleSpawn(var1, var2);
	}
	
	public Entity spawnEntity(int id, World world, double x, double y, double z) {
		Entity e = EntityList.createEntityByID(id, world);
		e.setPosition(x, y, z);
		return e;
	}
	
	public void onItemPickup(EntityPlayer var1, ItemStack var2) {
	}
	
	public void clientChat(String var1) {
	}
	
	public void serverChat(NetServerHandler var1, String var2) {
	}
	
	public void registerAnimation(Minecraft var1) {
	}
	
	public void clientDisconnect(NetClientHandler var1) {
	}
	
	public void takenFromCrafting(EntityPlayer var1, ItemStack var2, IInventory var3) {
	}
	
	public void takenFromFurnace(EntityPlayer var1, ItemStack var2) {
	}
	
	public GuiContainer getContainerGUI(EntityClientPlayerMP var1, int var2, int var3, int var4, int var5) {
		return null;
	}
	
	protected static class ModuleFieldInit implements PC_IFieldAnnotationIterator<PC_FieldObject> {
		
		protected PC_ModuleObject module;
		
		public ModuleFieldInit(PC_ModuleObject module) {
			this.module = module;
		}
		
		@Override
		public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_FieldObject> fieldWithAnnotation) {
			Class<?> clazz = fieldWithAnnotation.getAnnotation().clazz();
			Object o;
			if (PC_Block.class.isAssignableFrom(clazz)) {
				o = PC_BlockRegistry.register(module, (Class<? extends PC_Block>) clazz);
			} else if (PC_Item.class.isAssignableFrom(clazz) || PC_ItemArmor.class.isAssignableFrom(clazz)) {
				o = PC_ItemRegistry.register(module, (Class<? extends Item>) clazz);
			} else {
				o = PC_ReflectHelper.create(clazz);
			}
			registerObject(o);
			fieldWithAnnotation.setValue(o);
			return false;
		}
		
		protected void registerObject(Object object) {
			if (object instanceof PC_IMSG) {
				PC_MSGRegistry.registerMSGObject((PC_IMSG) object);
			}
			if (object instanceof PC_ITickHandler) {
				PC_TickRegistry.register((PC_ITickHandler) object);
			}
			if (object instanceof PC_IWorldGenerator) {
				PC_WorldGeneratorRegistry.register((PC_IWorldGenerator) object);
			}
		}
		
	}
	
}
