package powercraft.machines;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.recipes.PC_3DRecipe;
import powercraft.management.recipes.PC_3DRecipeManager;
import powercraft.management.recipes.PC_I3DRecipeHandler;
import powercraft.management.recipes.PC_ShapedRecipes;

public class PCma_App implements PC_IModule
{

    public static PC_Block automaticWorkbench;
    public static PC_Block roaster;
    public static PC_Block replacer;
    public static PC_Block transmutabox;
    public static PC_Block xpBank;
    public static PC_Block blockBuilder;
    public static PC_Block harvester;
    public static PC_Block fishingMachine;
    public static PC_Block chunkLoader;
    
    public static List<Integer> roasterIgnoreBlockIDs;

	@Override
	public String getName() {
		return "Machines";
	}

	@Override
	public String getVersion() {
		return "1.0.4";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {
        PCma_CropHarvestingManager.loadCrops();
        PCma_TreeHarvestingManager.loadTrees();
	}

	@Override
	public void postInit() {
		PCma_ItemRanking.init();
	}

	@Override
	public void initProperties(PC_Property config) {
		roasterIgnoreBlockIDs = GameInfo.parseIntList(config.getString("PCma_BlockRoaster.roasterIgnoreBlockIDs", "1"));
	}

	@Override
	public void initBlocks() {
        automaticWorkbench = ModuleLoader.register(this, PCma_BlockAutomaticWorkbench.class, PCma_TileEntityAutomaticWorkbench.class);
        roaster = ModuleLoader.register(this, PCma_BlockRoaster.class, PCma_TileEntityRoaster.class);
        replacer = ModuleLoader.register(this, PCma_BlockReplacer.class, PCma_TileEntityReplacer.class);
        transmutabox = ModuleLoader.register(this, PCma_BlockTransmutabox.class, PCma_TileEntityTransmutabox.class);
        xpBank = ModuleLoader.register(this, PCma_BlockXPBank.class, PCma_TileEntityXPBank.class);
        blockBuilder = ModuleLoader.register(this, PCma_BlockBlockBuilder.class, PCma_TileEntityBlockBuilder.class);
        harvester = ModuleLoader.register(this, PCma_BlockHarvester.class);
        fishingMachine = ModuleLoader.register(this, PCma_BlockFishingMachine.class, PCma_TileEntityFishingMachine.class);
        chunkLoader = ModuleLoader.register(this, PCma_BlockChunkLoader.class, PCma_TileEntityChunkLoader.class);
	}
	
	@Override
    public void initItems() {}
	
	@Override
	public void initEntities() {}
	
	@Override
	public List<Object> initRecipes(List<Object> recipes) {
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

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		return null;
	}

	@Override
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		return null;
	}
	
	@Override
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
