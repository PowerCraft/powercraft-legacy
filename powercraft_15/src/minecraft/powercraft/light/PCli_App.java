package powercraft.light;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;
import powercraft.api.recipes.PC_ShapelessRecipes;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_Instance;
import powercraft.launcher.loader.PC_ModuleObject;

@PC_Module(name="Light", version="1.1.0")
public class PCli_App{
	
	@PC_FieldObject(clazz=PCli_BlockLight.class)
    public static PC_Block light;
	@PC_FieldObject(clazz=PCli_BlockLightningConductor.class)
    public static PC_Block lightningConductor;
	@PC_FieldObject(clazz=PCli_BlockLaser.class)
    public static PC_Block laser;
	@PC_FieldObject(clazz=PCli_BlockMirror.class)
    public static PC_Block mirror;
	@PC_FieldObject(clazz=PCli_BlockPrism.class)
    public static PC_Block prism;
	@PC_FieldObject(clazz=PCli_BlockLaserSensor.class)
    public static PC_Block laserSensor;
	@PC_FieldObject(clazz=PCli_ItemLaserComposition.class)
    public static PC_Item laserComposition;
	@PC_Instance
	public static PC_ModuleObject instance;

	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
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
						'S', Block.stone, Block.cobblestone, 'W', new PC_ItemStack(Block.planks, 1, -1), 'D', Item.diamond));
        
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(laserSensor, 1),
        			"L", 
        			"R", 
        				'L', new PC_ItemStack(laser, 1), 'R', Item.redstone));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(mirror, 2, 0),
					"GI", 
					" I",	
						'G', Block.thinGlass, 'I', Item.ingotIron));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(prism, 1),
					"GG", 
					"GG", 
						'G', Block.glass));
        
        List<ItemStack> l = laserComposition.getItemStacks(new ArrayList<ItemStack>());
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(l.get(0)),
                	"XXX", 
                	"XPX", 
                	"XXX",
                        'X', Block.glass, 'P', new PC_ItemStack(PC_BlockRegistry.getPCBlockByName("PCco_BlockPowerCrystal"), 1, 0)));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(l.get(1)),
                	"XXX", 
                	"XPX", 
                	"XXX",
                        'X', Block.glass, 'P', new PC_ItemStack(PC_BlockRegistry.getPCBlockByName("PCco_BlockPowerCrystal"), 1, 1)));
        
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(l.get(2)),
                	"XXX", 
                	"XPX", 
                	"XXX",
                        'X', Block.glass, 'P', new PC_ItemStack(PC_BlockRegistry.getPCBlockByName("PCco_BlockPowerCrystal"), 1, 2)));
        
        for(int i=2; i<10; i++){
        	Object[] o = new Object[i];
        	for(int j=0; j<i; j++){
        		o[j] = new PC_ItemStack(laserComposition);
        	}
        	recipes.add(new PC_ShapelessRecipes(new PC_ItemStack(laserComposition), o));
        }
		return recipes;
	}

}
