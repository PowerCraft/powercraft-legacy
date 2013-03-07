package powercraft.machines;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_Init;
import powercraft.launcher.loader.PC_Module.PC_InitProperties;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_PostInit;
import powercraft.launcher.loader.PC_Module.PC_RegisterGuis;
import powercraft.launcher.PC_Property;
import powercraft.api.PC_Struct2;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_3DRecipe;
import powercraft.api.recipes.PC_I3DRecipeHandler;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;

@PC_Module(name="Machines", version="1.1.0")
public class PCma_App{

	@PC_FieldObject(clazz=PCma_BlockAutomaticWorkbench.class)
    public static PC_Block automaticWorkbench;
	@PC_FieldObject(clazz=PCma_BlockRoaster.class)
    public static PC_Block roaster;
	@PC_FieldObject(clazz=PCma_BlockReplacer.class)
    public static PC_Block replacer;
	@PC_FieldObject(clazz=PCma_BlockTransmutabox.class)
    public static PC_Block transmutabox;
	@PC_FieldObject(clazz=PCma_BlockXPBank.class)
    public static PC_Block xpBank;
	@PC_FieldObject(clazz=PCma_BlockBlockBuilder.class)
    public static PC_Block blockBuilder;
	@PC_FieldObject(clazz=PCma_BlockHarvester.class)
    public static PC_Block harvester;
	@PC_FieldObject(clazz=PCma_BlockFishingMachine.class)
    public static PC_Block fishingMachine;
	@PC_FieldObject(clazz=PCma_BlockChunkLoader.class)
    public static PC_Block chunkLoader;
    
    public static List<Integer> roasterIgnoreBlockIDs;

	@PC_Init
	public void init() {
        PCma_CropHarvestingManager.loadCrops();
        PCma_TreeHarvestingManager.loadTrees();
	}

	@PC_PostInit
	public void postInit() {
		PCma_ItemRanking.init();
	}

	@PC_InitProperties
	public void initProperties(PC_Property config) {
		roasterIgnoreBlockIDs = GameInfo.parseIntList(config.getString("PCma_BlockRoaster.roasterIgnoreBlockIDs", "1"));
	}
	
	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(automaticWorkbench),
                	"X", 
                	"Y", 
                	"Z",
                        'X', Item.diamond, 'Y', Block.workbench, 'Z', Item.redstone));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(roaster),
                	"III", 
                	"IFI", 
                	"III",
                        'I', Item.ingotIron, 'F', Item.flintAndSteel));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(xpBank),
                	"ODO", 
                	"OGO", 
                	"O O",
                        'O', Block.obsidian, 'D', Block.blockDiamond, 'G', Item.ghastTear));
        
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(transmutabox, 1, 0),
					"SOS", 
					"OPO", 
					"SOS",
						'S', Block.blockSteel, 'O', Block.obsidian, 'P', Block.stoneOvenIdle));
        
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(blockBuilder, 1),
					"G", 
					"D",
						'G', Item.ingotGold, 'D', Block.dispenser));

		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(harvester, 1),
					"P", 
					"D",
						'P', Item.ingotIron, 'D', Block.dispenser));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(replacer, 1),
					"B", 
					"R", 
					"H",
						'B', blockBuilder, 'R', Item.redstone, 'H', harvester));
		
		recipes.add(new PC_3DRecipe((PC_I3DRecipeHandler)fishingMachine, 
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"www",
				"www",
				"www"}, 
				new String[]{
				"www",
				"www",
				"www"},
				new String[]{
				"fpf",
				"pip",
				"fpf"},
				new String[]{
				" !c ",
				"!cc!c",
				" !c "},
				'w', Block.waterMoving, Block.waterStill, 'f', Block.fence, 'p', Block.planks, 'c', Block.chest, 'i', Block.blockSteel));
		
		recipes.add(new PC_3DRecipe((PC_I3DRecipeHandler)chunkLoader, 
				new String[]{
				"gog",
				"ogo",
				"gog"},
				new String[]{
				" f ",
				"f f",
				" f "},
				'g', Block.glass, 'o', Block.obsidian, 'f', Block.fire));
		
		return recipes;
	}

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("AutomaticWorkbench", PCma_ContainerAutomaticWorkbench.class));
		guis.add(new PC_Struct2<String, Class>("BlockBuilder", PCma_ContainerBlockBuilder.class));
		guis.add(new PC_Struct2<String, Class>("Replacer", PCma_ContainerReplacer.class));
		guis.add(new PC_Struct2<String, Class>("Roaster", PCma_ContainerRoaster.class));
		guis.add(new PC_Struct2<String, Class>("Transmutabox", PCma_ContainerTransmutabox.class));
		return guis;
	}
}
