package powercraft.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.recipes.PC_ShapedRecipes;

public class PCtr_App implements PC_IModule
{
    public static PC_Block conveyorBelt;
    public static PC_Block speedyBelt;
    public static PC_Block detectionBelt;
    public static PC_Block breakBelt;
    public static PC_Block redirectionBelt;
    public static PC_Block separationBelt;
    public static PC_Block ejectionBelt;
    public static PC_Block elevator;
    public static PC_ItemArmor slimeboots;

   @Override
	public String getName() {
		return "Transport";
	}

	@Override
	public String getVersion() {
		return "1.0.3";
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
	public void initBlocks()
    {
	        conveyorBelt = (PC_Block)ModuleLoader.register(this, PCtr_BlockBeltNormal.class, PCtr_ItemBlockConveyor.class);
	        speedyBelt = (PC_Block)ModuleLoader.register(this, PCtr_BlockBeltSpeedy.class, PCtr_ItemBlockConveyor.class);
	        detectionBelt = (PC_Block)ModuleLoader.register(this, PCtr_BlockBeltDetector.class, PCtr_ItemBlockConveyor.class);
	        breakBelt = (PC_Block)ModuleLoader.register(this, PCtr_BlockBeltBreak.class, PCtr_ItemBlockConveyor.class);
	        redirectionBelt = (PC_Block)ModuleLoader.register(this, PCtr_BlockBeltRedirector.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntityRedirectionBelt.class);
	        separationBelt = (PC_Block)ModuleLoader.register(this, PCtr_BlockBeltSeparator.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntitySeparationBelt.class);
	        ejectionBelt = (PC_Block)ModuleLoader.register(this, PCtr_BlockBeltEjector.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntityEjectionBelt.class);
	        elevator = (PC_Block)ModuleLoader.register(this, PCtr_BlockElevator.class, PCtr_ItemBlockElevator.class);
	        PCtr_BlockHackedWater.hackWater();
    }

	@Override
	public void initItems() {
		slimeboots = (PC_ItemArmor)ModuleLoader.register(this, PCtr_ItemArmorStickyBoots.class);
		
	}

	@Override
	public void initEntities() {}

	@Override
	public List<Object> initRecipes(List<Object> recipes) {
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
		return guis;
	}
}
