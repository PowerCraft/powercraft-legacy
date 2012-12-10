package powercraft.machines;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraftforge.common.Configuration;
import powercraft.management.PC_Block;
import powercraft.management.PC_Configuration;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

//@Mod(modid = "PowerCraft-Machines", name = "PowerCraft-Machines", version = "3.5.0AlphaC", dependencies = "required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class PCma_App implements PC_IModule
{

    public static PC_Block automaticWorkbench;
    public static PC_Block roaster;
    public static PC_Block replacer;
    public static PC_Block transmutabox;
    public static PC_Block xpBank;
    public static PC_Block blockBuilder;
    public static PC_Block harvester;
    
    public static List<Integer >roasterIgnoreBlockIDs;

    public static PCma_App getInstance()
    {
        return (PCma_App)PC_Module.getModule("PowerCraft-Machines");
    }

   

    @Override
    protected void initBlocks()
    {
        automaticWorkbench = PC_Utils.register(this, 482, PCma_BlockAutomaticWorkbench.class, PCma_TileEntityAutomaticWorkbench.class);
        roaster = PC_Utils.register(this, 483, PCma_BlockRoaster.class, PCma_TileEntityRoaster.class);
        replacer = PC_Utils.register(this, 484, PCma_BlockReplacer.class, PCma_TileEntityReplacer.class);
        transmutabox = PC_Utils.register(this, 485, PCma_BlockTransmutabox.class, PCma_TileEntityTransmutabox.class);
        xpBank = PC_Utils.register(this, 486, PCma_BlockXPBank.class, PCma_TileEntityXPBank.class);
        blockBuilder = PC_Utils.register(this, 487, PCma_BlockBlockBuilder.class, PCma_TileEntityBlockBuilder.class);
        harvester = PC_Utils.register(this, 488, PCma_BlockHarvester.class);
    }

    @Override
    protected void initItems()
    {
    }

    @Override
    protected void initRecipes()
    {
        PC_Utils.addRecipe(new PC_ItemStack(automaticWorkbench),
                new Object[] { "X", "Y", "Z",
                        'X', Item.diamond, 'Y', Block.workbench, 'Z', Item.redstone
                             });
        PC_Utils.addRecipe(new PC_ItemStack(roaster),
                new Object[] { "III", "IFI", "III",
                        'I', Item.ingotIron, 'F', Item.flintAndSteel
                             });
        PC_Utils.addRecipe(new PC_ItemStack(xpBank),
                new Object[] { "ODO", "OGO", "O O",
                        'O', Block.obsidian, 'D', Block.blockDiamond, 'G', Item.ghastTear
                             });
        
        PC_Utils.addRecipe(
				new PC_ItemStack(transmutabox, 1, 0),
				new Object[] { "SOS", "OPO", "SOS",
					'S', Block.blockSteel, 'O', Block.obsidian, 'P', Block.stoneOvenIdle });
        
        PC_Utils.addRecipe(
				new PC_ItemStack(blockBuilder, 1),
				new Object[] { "G", "D",
					'G', Item.ingotGold, 'D', Block.dispenser });

        PC_Utils.addRecipe(
				new PC_ItemStack(harvester, 1),
				new Object[] { "P", "D",
					'P', Item.ingotIron, 'D', Block.dispenser });
		
        PC_Utils.addRecipe(
				new PC_ItemStack(replacer, 1),
				new Object[] { "B", "R", "H",
					'B', blockBuilder, 'R', Item.redstone, 'H', harvester});
        
    }

    @Override
    protected List<String> addSplashes(List<String> list)
    {
        return null;
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void preInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
        PCma_CropHarvestingManager.loadCrops();
        PCma_TreeHarvestingManager.loadTrees();
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initProperties(PC_Configuration config) {
		roasterIgnoreBlockIDs = PC_Utils.parseIntList(PC_Utils.getConfigString(config,
				"roaster_ignored_blocks_list", Configuration.CATEGORY_GENERAL, "1"));
	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		// TODO Auto-generated method stub
		return null;
	}
}
