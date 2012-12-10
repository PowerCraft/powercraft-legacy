package powercraft.transport;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraftforge.common.Configuration;
import powercraft.management.PC_Block;
import powercraft.management.PC_Configuration;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

//@Mod(modid = "PowerCraft-Transport", name = "PowerCraft-Transport", version = "3.5.0AlphaC", dependencies = "required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
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
		return "1.0AlphaA";
	}

	@Override
	public void preInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initProperties(PC_Configuration config) {
		// TODO Auto-generated method stub
		
	}

	 @Override
	public void initBlocks()
    {
	        conveyorBelt = (PC_Block)PC_Utils.register(this, PCtr_BlockBeltNormal.class, PCtr_ItemBlockConveyor.class);
	        speedyBelt = (PC_Block)PC_Utils.register(this, PCtr_BlockBeltSpeedy.class, PCtr_ItemBlockConveyor.class);
	        detectionBelt = (PC_Block)PC_Utils.register(this, PCtr_BlockBeltDetector.class, PCtr_ItemBlockConveyor.class);
	        breakBelt = (PC_Block)PC_Utils.register(this, PCtr_BlockBeltBreak.class, PCtr_ItemBlockConveyor.class);
	        redirectionBelt = (PC_Block)PC_Utils.register(this, PCtr_BlockBeltRedirector.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntityRedirectionBelt.class);
	        separationBelt = (PC_Block)PC_Utils.register(this, PCtr_BlockBeltSeparator.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntitySeparationBelt.class);
	        ejectionBelt = (PC_Block)PC_Utils.register(this, PCtr_BlockBeltEjector.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntityEjectionBelt.class);
	        elevator = (PC_Block)PC_Utils.register(this, PCtr_BlockElevator.class, PCtr_ItemBlockElevator.class);
	        PCtr_BlockHackedWater.hackWater();
    }

	@Override
	public void initItems() {
		slimeboots = (PC_ItemArmor)PC_Utils.register(this, PCtr_ItemArmorStickyBoots.class);
		
	}

	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(conveyorBelt, 16),
                new Object[] { "XXX", "YRY",
                        'X', Item.leather, 'Y', Item.ingotIron, 'R', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(conveyorBelt, 4),
                new Object[] { "XXX", "YRY",
                        'X', Item.paper, 'Y', Item.ingotIron, 'R', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(speedyBelt, 16),
                new Object[] { "XXX", "YRY",
                        'X', Item.leather, 'Y', Item.ingotGold, 'R', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(speedyBelt, 4),
                new Object[] { "XXX", "YRY",
                        'X', Item.paper, 'Y', Item.ingotGold, 'R', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(ejectionBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Item.bow, 'Y', conveyorBelt, 'Z', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(detectionBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Block.pressurePlatePlanks, 'Y', conveyorBelt, 'Z', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(detectionBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Block.pressurePlateStone, 'Y', conveyorBelt, 'Z', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(separationBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Item.diamond, 'Y', conveyorBelt, 'Z', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(breakBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Item.ingotIron, 'Y', conveyorBelt, 'Z', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(redirectionBelt, 1),
                new Object[] { "X", "Y",
                        'X', conveyorBelt, 'Y', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(elevator, 6, 0),
                new Object[] { "XGX", "X X", "XGX",
                        'X', conveyorBelt, 'G', Item.ingotGold
                             }));
        recipes.add(new PC_ShapedRecipes(
                new PC_ItemStack(elevator, 6, 1),
                new Object[] { "XGX", "XRX", "XGX",
                        'X', conveyorBelt, 'G', Item.ingotGold, 'R', Item.redstone
                             }));
        recipes.add(new PC_ShapedRecipes(
        		new PC_ItemStack(slimeboots),
        		new Object[] {"B", "S", 'B',
        			Item.bootsSteel, 'S', Item.slimeBall}));
        
     
   
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		// TODO Auto-generated method stub
		return null;
	}
}
