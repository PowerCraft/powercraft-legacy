package powercraft.weasel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.src.ModLoader;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.recipes.PC_ShapedRecipes;

public class PCws_App implements PC_IModule {

	public static PCws_WeaselManager weaselManager = new PCws_WeaselManager();
	
	public static PC_Block weasel;
	public static PC_Block weaselDiskManager;
	public static PC_Block unobtaninium;
	
	public static PC_Item weaselDisk;
	public static PC_Item weaselTransistor;
	public static PC_Item ingotUnobtaninium;
	
	@Override
	public String getName() {
		return "Weasel";
	}

	@Override
	public String getVersion() {
		return "1.0.1";
	}

	@Override
	public void preInit() {
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoCore(), 0);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoPort(), 1);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoSpeaker(), 2);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoDisplay(), 3);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoTerminal(), 4);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoTouchscreen(), 5);
		PCws_WeaselManager.registerPluginInfo(new PCws_WeaselPluginInfoDiskDrive(), 6);
	}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
		PCws_WeaselHighlightHelper.checkConfigFile(config);
	}

	@Override
	public void initBlocks() {
		weasel = ModuleLoader.register(this, PCws_BlockWeasel.class, PCws_ItemBlockWeasel.class, PCws_TileEntityWeasel.class);
		weaselDiskManager = ModuleLoader.register(this, PCws_BlockWeaselDiskManager.class, PCws_TileEntityWeaselDiskManager.class);
		unobtaninium = ModuleLoader.register(this, PCws_BlockUnobtainium.class, PCws_TileEntityUnobtainium.class);
	}

	@Override
	public void initItems() {
		weaselDisk = ModuleLoader.register(this, PCws_ItemWeaselDisk.class);
		weaselTransistor = ModuleLoader.register(this, PCws_ItemTransistor.class);
		ingotUnobtaninium = ModuleLoader.register(this, PCws_ItemUnobtaninium.class);
	}

	@Override
	public void initEntities() {}

	@Override
	public List<Object> initRecipes(List<Object> recipes) {
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weaselTransistor, 2),
				"UUU",
				"III",
				'I', new PC_ItemStack(Item.ingotIron), 'U', new PC_ItemStack(ingotUnobtaninium)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 0),
				"TPT ", 
				"RCR", 
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'C', new PC_ItemStack(ModuleInfo.getPCBlockByName("PCco_BlockPowerCrystal"),1,-1),
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
		
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		dataHandlers.add(new PC_Struct2<String, PC_IDataHandler>("Weasel", weaselManager));
		return dataHandlers;
	}

	@Override
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects) {
		msgObjects.add(weaselManager);
		return msgObjects;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("WeaselDiskDrive", (PC_IPacketHandler)weaselDiskManager));
		return packetHandlers;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("WeaselDiskManager", PCws_ContainerWeaselDiskManager.class));
		guis.add(new PC_Struct2<String, Class>("WeaselDiskDrive", PCws_ContainerWeaselDiskDrive.class));
		return guis;
	}

}
