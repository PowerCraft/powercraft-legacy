package powercraft.weasel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.interfaces.PC_IDataHandler;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.network.PC_IPacketHandler;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;
import powercraft.api.recipes.PC_SmeltRecipe;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.utils.PC_Struct2;
import powercraft.launcher.PC_Property;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitDataHandlers;
import powercraft.launcher.loader.PC_Module.PC_InitPacketHandlers;
import powercraft.launcher.loader.PC_Module.PC_InitProperties;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_Instance;
import powercraft.launcher.loader.PC_Module.PC_PreInit;
import powercraft.launcher.loader.PC_Module.PC_RegisterContainers;
import powercraft.launcher.loader.PC_ModuleObject;

@PC_Module(name="Weasel", version="1.1.0")
public class PCws_App {

	@PC_FieldObject(clazz=PCws_WeaselManager.class)
	public static PCws_WeaselManager weaselManager;
	@PC_FieldObject(clazz=PCws_BlockWeasel.class)
	public static PC_Block weasel;
	@PC_FieldObject(clazz=PCws_BlockWeaselDiskManager.class)
	public static PC_Block weaselDiskManager;
	@PC_FieldObject(clazz=PCws_BlockSilicon.class)
	public static PC_Block silicon;
	@PC_FieldObject(clazz=PCws_ItemWeaselDisk.class)
	public static PC_Item weaselDisk;
	@PC_FieldObject(clazz=PCws_ItemTransistor.class)
	public static PC_Item weaselTransistor;
	@PC_FieldObject(clazz=PCws_ItemSilicon.class)
	public static PC_Item ingotSilicon;
	@PC_Instance
	public static PC_ModuleObject instance;
	
	@PC_PreInit
	public void preInit() {
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoCore(), 0);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoPort(), 1);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoSpeaker(), 2);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoDisplay(), 3);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoTerminal(), 4);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoTouchscreen(), 5);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoDiskDrive(), 6);
	}

	@PC_InitProperties
	public void initProperties(PC_Property config) {
		PCws_WeaselHighlightHelper.checkConfigFile(config);
	}

	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weaselTransistor, 2),
				"UUU",
				"III",
				'I', new PC_ItemStack(Item.ingotIron), 'U', new PC_ItemStack(ingotSilicon)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 0),
				"TPT", 
				"RCR", 
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'C', new PC_ItemStack(PC_BlockRegistry.getPCBlockByName("PCco_BlockPowerCrystal"),1,-1),
					'T', new PC_ItemStack(weaselTransistor,1,0), 'P', new PC_ItemStack(weasel,1,1)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 1),
				"RTR", 
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'T', new PC_ItemStack(weaselTransistor,1,0)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 2),
				" N ", 
				" P ",
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'P', new PC_ItemStack(weasel,1,1), 'N', Block.music));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 3),
				" G ", 
				" P ",
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'P', new PC_ItemStack(weasel,1,1), 'G', Block.thinGlass));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 4),
				"  G", 
				"BBP", 
				"SSS",
					'B', Block.stoneButton, 'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'P', new PC_ItemStack(weasel,1,1), 'G', Block.thinGlass));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 5),
				"GGG", 
				"TPT",
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'T', new PC_ItemStack(weaselTransistor,1,0), 'P', new PC_ItemStack(weasel,1,1), 'G', Block.thinGlass));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 6),
				"SSS", 
				"GPG",
				"SSS",
					'B', Block.stoneButton, 'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'P', new PC_ItemStack(weasel,1,1), 'G', Item.goldNugget));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weaselDisk, 4),
				" C ", 
				"CTC",
				" C ",
					'C', Item.coal, 'T', new PC_ItemStack(weaselTransistor,1,0)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weaselDiskManager),
				"BBB", 
				"STS", 
				"SSS",
					'B', Block.stoneButton, 'S', new PC_ItemStack(Block.stoneSingleSlab, 1, 0), 'T', new PC_ItemStack(weaselTransistor,1,0)));
		
		recipes.add(new PC_SmeltRecipe(new PC_ItemStack(ingotSilicon), new PC_ItemStack(silicon), 0));
		
		return recipes;
	}

	@PC_InitDataHandlers
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		dataHandlers.add(new PC_Struct2<String, PC_IDataHandler>("Weasel", weaselManager));
		return dataHandlers;
	}

	@PC_InitPacketHandlers
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("WeaselDiskDrive", (PC_IPacketHandler)weaselDiskManager));
		return packetHandlers;
	}
	
	@PC_RegisterContainers
	public List<PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>> registerContainers(List<PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>("WeaselDiskManager", PCws_ContainerWeaselDiskManager.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>("WeaselDiskDrive", PCws_ContainerWeaselDiskDrive.class));
		return guis;
	}

}
