package powercraft.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.annotation.PC_FieldObject;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.recipes.PC_ShapedRecipes;

public class PCtr_App implements PC_IModule
{
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

   @Override
	public String getName() {
		return "Transport";
	}

	@Override
	public String getVersion() {
		return "1.0.4";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
	}

	@Override
	public List<Class<? extends Entity>> initEntities(List<Class<? extends Entity>> entities) {
		return null;
	}

	@Override
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
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(slimeboots),
        			"B", 
        			"S", 
        			'B', Item.bootsSteel, 'S', Item.slimeBall));
        
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
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("SeperationBelt", PCtr_ContainerSeparationBelt.class));
		guis.add(new PC_Struct2<String, Class>("Splitter", PCtr_ContainerSplitter.class));
		return guis;
	}
}
