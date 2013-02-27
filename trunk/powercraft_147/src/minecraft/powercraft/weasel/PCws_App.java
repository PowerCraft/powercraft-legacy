package powercraft.weasel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_Item;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;
import powercraft.management.registry.PC_BlockRegistry;

public class PCws_App implements PC_IModule {

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
	
	@Override
	public String getName() {
		return "Weasel";
	}

	@Override
	public String getVersion() {
		return "1.0.2";
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
	public List<PC_Struct2<Class<? extends Entity>, Integer>> initEntities(List<PC_Struct2<Class<? extends Entity>, Integer>> entities){
		return null;
	}

	@Override
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weaselTransistor, 2),
				"UUU",
				"III",
				'I', new PC_ItemStack(Item.ingotIron), 'U', new PC_ItemStack(ingotSilicon)));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 0),
				"TPT ", 
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
		
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		dataHandlers.add(new PC_Struct2<String, PC_IDataHandler>("Weasel", weaselManager));
		return dataHandlers;
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
