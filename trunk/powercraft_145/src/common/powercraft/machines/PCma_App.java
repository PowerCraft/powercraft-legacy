package powercraft.machines;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCma_App implements PC_IModule
{

    public static PC_Block automaticWorkbench;
    public static PC_Block roaster;
    public static PC_Block replacer;
    public static PC_Block transmutabox;
    public static PC_Block xpBank;
    public static PC_Block blockBuilder;
    public static PC_Block harvester;
    
    public static List<Integer> roasterIgnoreBlockIDs;

	@Override
	public String getName() {
		return "Machines";
	}

	@Override
	public String getVersion() {
		return "1.0AlphaA";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {
        PCma_CropHarvestingManager.loadCrops();
        PCma_TreeHarvestingManager.loadTrees();
	}

	@Override
	public void postInit() {}

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
	}
	
	@Override
    public void initItems()
    {
    }
	
	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
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
