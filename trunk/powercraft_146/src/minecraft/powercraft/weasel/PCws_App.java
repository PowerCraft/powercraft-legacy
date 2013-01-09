package powercraft.weasel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.src.ModLoader;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCws_App implements PC_IModule {

	public static PC_Block weasel;
	public static PC_Block weaselDiskManager;
	public static PC_Block unobtaninium;
	
	public static PC_Item weaselDisk;
	public static PC_Item ingotUnobtaninium;
	
	@Override
	public String getName() {
		return "Weasel";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
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
	public void init() {
		PCws_WeaselManager weaselManager = new PCws_WeaselManager();
		ModuleInfo.registerMSGObject(weaselManager);
		ModuleLoader.regsterDataHandler("Weasel", weaselManager);
	}

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
		PC_PacketHandler.registerPackethandler("WeaselDiskDrive", (PC_IPacketHandler)weaselDiskManager);
		unobtaninium = ModuleLoader.register(this, PCws_BlockUnobtainium.class);
	}

	@Override
	public void initItems() {
		weaselDisk = ModuleLoader.register(this, PCws_ItemWeaselDisk.class);
		ingotUnobtaninium = ModuleLoader.register(this, PCws_ItemUnobtaninium.class);
	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 0),
				"SRS", 
				"RCR", 
				"SRS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'C', ModuleInfo.getPCBlockByName("PCco_BlockPowerCrystal")));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 1),
				"GRG", 
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'G', Item.goldNugget));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 3),
				" G ", 
				"NRN",
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'G', Block.thinGlass, 'N', Item.goldNugget));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 2),
				" N ", 
				"GRG",
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'N', Block.music, 'G', Item.goldNugget));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 5),
				"GGG", 
				"NRN",
				"SSS",
					'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'G', Block.thinGlass, 'N', Item.goldNugget));
		

		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weaselDisk, 4, 0xFFF),
				" C ", 
				"CIC",
				" C ",
					'C', Item.coal, 'I', Item.ingotIron));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weaselDiskManager),
				"BBB", 
				"SRS", 
				"SSS",
					'B', Block.stoneButton, 'S', new PC_ItemStack(Block.stoneSingleSlab, 1, 0), 'R', Item.redstone));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 6),
				"SSS", 
				"GRG",
				"SSS",
					'B', Block.stoneButton, 'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'R', Item.redstone, 'G', Item.goldNugget));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(weasel, 1, 4),
				"  D", 
				"BBS", 
				"SSS",
					'B', Block.stoneButton, 'S', new PC_ItemStack(Block.stoneSingleSlab,1,0), 'D', new PC_ItemStack(weasel, 1, 3)));
		
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("WeaselDiskManager", PCws_ContainerWeaselDiskManager.class));
		guis.add(new PC_Struct2<String, Class>("WeaselDiskDrive", PCws_ContainerWeaselDiskDrive.class));
		return guis;
	}

}
