package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_ShapelessRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ModuleLoader;

public class PCli_App implements PC_IModule
{
    public static PC_Block light;
    public static PC_Block lightningConductor;
    public static PC_Block laser;
    public static PC_Block mirrow;
    public static PC_Block prism;
    public static PC_Block laserSensor;
    public static PC_Item laserComposition;

	@Override
	public String getName() {
		return "Light";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {}

	@Override
	public void initBlocks()
    {
        light = ModuleLoader.register(this, PCli_BlockLight.class, PCli_TileEntityLight.class);
        lightningConductor = ModuleLoader.register(this, PCli_BlockLightningConductor.class, PCli_ItemBlockLightningConductor.class, PCli_TileEntityLightningConductor.class);
        laser = ModuleLoader.register(this, PCli_BlockLaser.class, PCli_TileEntityLaser.class);
        mirrow = ModuleLoader.register(this, PCli_BlockMirrow.class, PCli_TileEntityMirrow.class);
        prism = ModuleLoader.register(this, PCli_BlockPrism.class, PCli_TileEntityPrism.class);
        laserSensor = ModuleLoader.register(this, PCli_BlockLaserSensor.class, PCli_TileEntityLaserSensor.class);
    }

    @Override
    public void initItems()
    {
    	laserComposition = ModuleLoader.register(this, PCli_ItemLaserComposition.class);
    }
	
	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(light),
                Item.redstone, Block.glowStone));
		
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(lightningConductor),
                	" X ", 
                	" X ", 
                	"XXX",
                        'X', Block.blockSteel));
        
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(laser, 1),
					" WD", 
					" S ", 
					"SSS",
						'S', Block.stone, 'W', new PC_ItemStack(Block.planks, 1, -1), 'D', Item.diamond));
        
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(laser, 1),
					" WD", 
					" S ", 
					"SSS",
						'S', Block.cobblestone, 'W', new PC_ItemStack(Block.planks, 1, -1), 'D', Item.diamond));
        
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(laserSensor, 1),
        			"L", 
        			"R", 
        				'L', new PC_ItemStack(laser, 1), 'R', Item.redstone));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(mirrow, 2, 0),
					"GI", 
					" I",	
						'G', Block.thinGlass, 'I', Item.ingotIron));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(prism, 1, 1),
					"GG", 
					"GG", 
						'G', Block.glass));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(laserComposition),
                	"XXX", 
                	"XPX", 
                	"XXX",
                        'X', Block.glass, 'P', new PC_ItemStack(ModuleInfo.getPCBlockByName("PCco_BlockPowerCrystal"), 1, 0)));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(laserComposition),
                	"XXX", 
                	"XPX", 
                	"XXX",
                        'X', Block.glass, 'P', new PC_ItemStack(ModuleInfo.getPCBlockByName("PCco_BlockPowerCrystal"), 1, 1)));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(laserComposition),
                	"XXX", 
                	"XPX", 
                	"XXX",
                        'X', Block.glass, 'P', new PC_ItemStack(ModuleInfo.getPCBlockByName("PCco_BlockPowerCrystal"), 1, 2)));
        
        for(int i=2; i<10; i++){
        	Object[] o = new Object[i];
        	for(int j=0; j<i; j++){
        		o[j] = new PC_ItemStack(laserComposition);
        	}
        	recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(laserComposition), o));
        }
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		return null;
	}
}
