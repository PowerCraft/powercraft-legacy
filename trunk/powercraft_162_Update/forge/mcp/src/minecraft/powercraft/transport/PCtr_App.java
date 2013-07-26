package powercraft.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;
import powercraft.api.utils.PC_Struct2;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_RegisterContainers;

@PC_Module(name="Transport", version="1.1.1")
public class PCtr_App{
	
	@PC_FieldObject(clazz = PCtr_BlockBeltNormal.class)
    public static PC_Block conveyorBelt;
	@PC_FieldObject(clazz = PCtr_BlockBeltSpeedy.class)
    public static PC_Block speedyBelt;
	@PC_FieldObject(clazz = PCtr_BlockBeltDetector.class)
    public static PC_Block detectionBelt;
	@PC_FieldObject(clazz = PCtr_BlockBeltBreak.class)
    public static PC_Block breakBelt;
	@PC_FieldObject(clazz = PCtr_BlockBeltRedirector.class)
    public static PC_Block redirectionBelt;
	@PC_FieldObject(clazz = PCtr_BlockBeltSeparator.class)
    public static PC_Block separationBelt;
	@PC_FieldObject(clazz = PCtr_BlockBeltEjector.class)
    public static PC_Block ejectionBelt;
	@PC_FieldObject(clazz = PCtr_BlockElevator.class)
    public static PC_Block elevator;
	@PC_FieldObject(clazz = PCtr_BlockSplitter.class)
    public static PC_Block splitter;
	@PC_FieldObject(clazz = PCtr_ItemArmorStickyBoots.class)
    public static PC_ItemArmor slimeboots;

	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(conveyorBelt, 16),
                	"XXX", 
                	"YRY",
                        'X', Item.leather, 'Y', Item.ingotIron, 'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(conveyorBelt, 4),
                	"XXX",
                	"YRY",
                        'X', Item.paper, 'Y', Item.ingotIron, 'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(speedyBelt, 16),
                	"XXX", 
                	"YRY",
                        'X', Item.leather, 'Y', Item.ingotGold, 'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(speedyBelt, 4),
                	"XXX", 
                	"YRY",
                        'X', Item.paper, 'Y', Item.ingotGold, 'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(ejectionBelt, 1),
                	"X", 
                	"Y", 
                	"Z",
                        'X', Item.bow, 'Y', conveyorBelt, 'Z', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(detectionBelt, 1),
                	"X", 
                	"Y", 
                	"Z",
                        'X', Block.pressurePlatePlanks, Block.pressurePlateStone, 'Y', conveyorBelt, 'Z', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(separationBelt, 1),
                	"X", 
                	"Y", 
                	"Z",
                        'X', Item.diamond, 'Y', conveyorBelt, 'Z', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(breakBelt, 1),
                	"X", 
                	"Y", 
                	"Z",
                        'X', Item.ingotIron, 'Y', conveyorBelt, 'Z', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(redirectionBelt, 1),
                	"X",
                	"Y",
                        'X', conveyorBelt, 'Y', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(elevator, 6, 0),
                	"XGX", 
                	"X X", 
                	"XGX",
                        'X', conveyorBelt, 'G', Item.ingotGold));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(elevator, 6, 1),
                	"XGX", 
                	"XRX", 
                	"XGX",
                        'X', conveyorBelt, 'G', Item.ingotGold, 'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(splitter), 
        			" U ",
        			"XSX",
        			" D ",
        			'U', new PC_ItemStack(elevator, 1, 0), 'X', conveyorBelt, 'S', separationBelt, 'D', new PC_ItemStack(elevator, 1, 1)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(slimeboots),
        			"B", 
        			"S", 
        			'B', Item.bootsIron, 'S', Item.slimeBall));
        
		return recipes;
	}

	@PC_RegisterContainers
	public List<PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>> registerContainers(List<PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>("SeperationBelt", PCtr_ContainerSeparationBelt.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_GresBaseWithInventory>>("Splitter", PCtr_ContainerSplitter.class));
		return guis;
	}
}
