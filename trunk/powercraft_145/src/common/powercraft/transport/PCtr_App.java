package powercraft.transport;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraftforge.common.Configuration;
import powercraft.core.PC_Block;
import powercraft.core.PC_ItemArmor;
import powercraft.core.PC_ItemStack;
import powercraft.core.PC_Module;
import powercraft.core.PC_Utils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

//@Mod(modid = "PowerCraft-Transport", name = "PowerCraft-Transport", version = "3.5.0AlphaC", dependencies = "required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class PCtr_App extends PC_Module
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

    public static PCtr_App getInstance()
    {
        return (PCtr_App)PC_Module.getModule("PowerCraft-Transport");
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        preInit(event, proxy);
    }

    @Init
    public void init(FMLInitializationEvent event)
    {
        init();
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        postInit();
    }

    @Override
    protected void initProperties(Configuration config) {}

    @Override
    protected List<String> loadTextureFiles(List<String> textures)
    {
        textures.add(getTerrainFile());
        return textures;
    }

    @Override
    protected void initLanguage()
    {
        PC_Utils.registerLanguage(this,
                "pc.gui.separationBelt.group", "Ignore subtypes of",
                "pc.gui.separationBelt.groupLogs", "Logs",
                "pc.gui.separationBelt.groupPlanks", "Planks",
                "pc.gui.separationBelt.groupAll", "All",
                "pc.gui.ejector.modeEjectTitle", "Ejection mode:",
                "pc.gui.ejector.modeStacks", "Whole stacks",
                "pc.gui.ejector.modeItems", "Single items",
                "pc.gui.ejector.modeAll", "All contents at once",
                "pc.gui.ejector.modeSelectTitle", "Method of selection:",
                "pc.gui.ejector.modeSelectFirst", "First slot",
                "pc.gui.ejector.modeSelectLast", "Last slot",
                "pc.gui.ejector.modeSelectRandom", "Random slot"
                                 );
    }

    @Override
    protected void initBlocks()
    {
        conveyorBelt = (PC_Block)PC_Utils.register(this, 473, PCtr_BlockBeltNormal.class, PCtr_ItemBlockConveyor.class);
        speedyBelt = (PC_Block)PC_Utils.register(this, 474, PCtr_BlockBeltSpeedy.class, PCtr_ItemBlockConveyor.class);
        detectionBelt = (PC_Block)PC_Utils.register(this, 475, PCtr_BlockBeltDetector.class, PCtr_ItemBlockConveyor.class);
        breakBelt = (PC_Block)PC_Utils.register(this, 476, PCtr_BlockBeltBreak.class, PCtr_ItemBlockConveyor.class);
        redirectionBelt = (PC_Block)PC_Utils.register(this, 477, PCtr_BlockBeltRedirector.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntityRedirectionBelt.class);
        separationBelt = (PC_Block)PC_Utils.register(this, 478, PCtr_BlockBeltSeparator.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntitySeparationBelt.class);
        ejectionBelt = (PC_Block)PC_Utils.register(this, 479, PCtr_BlockBeltEjector.class, PCtr_ItemBlockConveyor.class, PCtr_TileEntityEjectionBelt.class);
        elevator = (PC_Block)PC_Utils.register(this, 480, PCtr_BlockElevator.class, PCtr_ItemBlockElevator.class);
        PCtr_BlockHackedWater.hackWater();
    }

    @Override
    protected void initItems()
    {
        slimeboots = (PC_ItemArmor)PC_Utils.register(this, 481, PCtr_ItemArmorStickyBoots.class);
    }

    @Override
    protected void initRecipes()
    {
        PC_Utils.addRecipe(
                new PC_ItemStack(conveyorBelt, 16),
                new Object[] { "XXX", "YRY",
                        'X', Item.leather, 'Y', Item.ingotIron, 'R', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(conveyorBelt, 4),
                new Object[] { "XXX", "YRY",
                        'X', Item.paper, 'Y', Item.ingotIron, 'R', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(speedyBelt, 16),
                new Object[] { "XXX", "YRY",
                        'X', Item.leather, 'Y', Item.ingotGold, 'R', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(speedyBelt, 4),
                new Object[] { "XXX", "YRY",
                        'X', Item.paper, 'Y', Item.ingotGold, 'R', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(ejectionBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Item.bow, 'Y', conveyorBelt, 'Z', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(detectionBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Block.pressurePlatePlanks, 'Y', conveyorBelt, 'Z', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(detectionBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Block.pressurePlateStone, 'Y', conveyorBelt, 'Z', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(separationBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Item.diamond, 'Y', conveyorBelt, 'Z', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(breakBelt, 1),
                new Object[] { "X", "Y", "Z",
                        'X', Item.ingotIron, 'Y', conveyorBelt, 'Z', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(redirectionBelt, 1),
                new Object[] { "X", "Y",
                        'X', conveyorBelt, 'Y', Item.redstone
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(elevator, 6, 0),
                new Object[] { "XGX", "X X", "XGX",
                        'X', conveyorBelt, 'G', Item.ingotGold
                             });
        PC_Utils.addRecipe(
                new PC_ItemStack(elevator, 6, 1),
                new Object[] { "XGX", "XRX", "XGX",
                        'X', conveyorBelt, 'G', Item.ingotGold, 'R', Item.redstone
                             });
        PC_Utils.addRecipe(new PC_ItemStack(slimeboots), new Object[] {"B", "S", 'B', Item.bootsSteel, 'S', Item.slimeBall});
    }

    @Override
    protected List<String> addSplashes(List<String> list)
    {
        return null;
    }
}
