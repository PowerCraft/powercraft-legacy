package powercraft.logic;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import powercraft.core.PC_Block;
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

@Mod(modid = "PowerCraft-Logic", name = "PowerCraft-Logic", version = "3.5.0AlphaC", dependencies = "required-after:PowerCraft-Core")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class mod_PowerCraftLogic extends PC_Module
{
    @SidedProxy(clientSide = "powercraft.logic.PClo_ClientProxy", serverSide = "powercraft.logic.PClo_CommonProxy")
    public static PClo_CommonProxy proxy;

    public static PC_Block pulsar;
    public static PC_Block gate;
    public static PC_Block flipFlop;
    public static PC_Block delayer;
    public static PC_Block special;
    public static PC_Block repeater;

    public static mod_PowerCraftLogic getInstance()
    {
        return (mod_PowerCraftLogic)PC_Module.getModule("PowerCraft-Logic");
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
    protected void initProperties(Configuration config)
    {
    }

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
                "pc.pulsar.clickMsg", "Period %s ticks (%s s)",
                "pc.pulsar.clickMsgTime", "Period %s ticks (%s s), remains %s",
                "pc.gate.not.desc", "negates input",
                "pc.gate.and.desc", "all inputs on",
                "pc.gate.nand.desc", "some inputs off",
                "pc.gate.or.desc", "at least one input on",
                "pc.gate.nor.desc", "all inputs off",
                "pc.gate.xor.desc", "inputs different",
                "pc.gate.xnor.desc", "inputs equal",
                "pc.flipflop.D.desc", "latch memory",
                "pc.flipflop.RS.desc", "set/reset memory",
                "pc.flipflop.T.desc", "divides signal by 2",
                "pc.flipflop.random.desc", "changes state randomly on pulse",
                "pc.delayer.buffer.desc", "slows down signal",
                "pc.delayer.slowRepeater.desc", "makes pulses longer",
                "pc.special.day.desc", "on during day",
                "pc.special.night.desc", "on during night",
                "pc.special.rain.desc", "on during rain",
                "pc.special.chestEmpty.desc", "on if nearby container is empty",
                "pc.special.chestFull.desc", "on if nearby container is full",
                "pc.special.special.desc", "spawner & pulsar control",
                "pc.repeater.crossing.desc", "lets two wires intersect",
                "pc.repeater.splitter.desc", "splits signal",
                "pc.repeater.repeaterStraight.desc", "simple 1-tick repeater",
                "pc.repeater.repeaterCorner.desc", "simple 1-tick corner repeater",
                "pc.repeater.repeaterStraightInstant.desc", "instant repeater",
                "pc.repeater.repeaterCornerInstant.desc", "instant corner repeater",
                "pc.gui.pulsar.silent", "Silent",
                "pc.gui.pulsar.paused", "Pause",
                "pc.gui.pulsar.delay", "Delay (sec)",
                "pc.gui.pulsar.hold", "Hold time (sec)",
                "pc.gui.pulsar.ticks", "ticks",
                "pc.gui.pulsar.errDelay", "Bad delay time!",
                "pc.gui.pulsar.errHold", "Bad hold time!",
                "pc.gui.delayer.delay", "Delay (sec)",
                "pc.gui.pulsar.errintputzero", "Bad delay time!",
                "pc.gui.delayer.errnoinput", "No Input!",
                "pc.gui.special.chestEmpty.name", "Empty Chest",
                "pc.gui.special.chestEmpty.inv", "No item of kind",
                "pc.gui.special.chestFull.name", "Full Chest",
                "pc.gui.special.chestFull.inv", "Space for"
                                 );
    }

    @Override
    protected void initBlocks()
    {
        pulsar = (PC_Block)PC_Utils.register(this, 461, PClo_BlockPulsar.class, PClo_TileEntityPulsar.class);
        gate = (PC_Block)PC_Utils.register(this, 463, PClo_BlockGate.class, PClo_ItemBlockGate.class, PClo_TileEntityGate.class);
        flipFlop = (PC_Block)PC_Utils.register(this, 465, PClo_BlockFlipFlop.class, PClo_ItemBlockFlipFlop.class, PClo_TileEntityFlipFlop.class);
        delayer = (PC_Block)PC_Utils.register(this, 467, PClo_BlockDelayer.class, PClo_ItemBlockDelayer.class, PClo_TileEntityDelayer.class);
        special = (PC_Block)PC_Utils.register(this, 469, PClo_BlockSpecial.class, PClo_ItemBlockSpecial.class, PClo_TileEntitySpecial.class);
        repeater = (PC_Block)PC_Utils.register(this, 471, PClo_BlockRepeater.class, PClo_ItemBlockRepeater.class, PClo_TileEntityRepeater.class);
    }

    @Override
    protected void initItems()
    {
    }

    @Override
    protected void initRecipes()
    {
        PC_Utils.addRecipe(new PC_ItemStack(pulsar, 1, 0),
                new Object[]
                {
                    " r ",
                    "ror",
                    " r ",
                    'r', Item.redstone, 'o', Block.obsidian
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.NOT),
                new Object[]
                {
                    "rst",
                    'r', Item.redstone, 's', Block.stone, 't', Block.torchRedstoneActive
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.AND),
                new Object[]
                {
                    " r ",
                    "sss",
                    "rrr",
                    'r', Item.redstone, 's', Block.stone
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.OR),
                new Object[]
                {
                    " r ",
                    "rsr",
                    " r ",
                    'r', Item.redstone, 's', Block.stone
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.XOR),
                new Object[]
                {
                    "r",
                    "x",
                    'r', Item.redstone, 'x', new PC_ItemStack(gate, 1, PClo_GateType.OR)
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.NAND),
                new Object[]
                {
                    "n",
                    "a",
                    'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'a', new PC_ItemStack(gate, 1, PClo_GateType.AND)
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.NOR),
                new Object[]
                {
                    "n",
                    "o",
                    'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'o', new PC_ItemStack(gate, 1, PClo_GateType.OR)
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.XNOR),
                new Object[]
                {
                    "n",
                    "x",
                    'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'x', new PC_ItemStack(gate, 1, PClo_GateType.XOR)
                });
        PC_Utils.addRecipe(new PC_ItemStack(gate, 1, PClo_GateType.XNOR),
                new Object[]
                {
                    "n",
                    "x",
                    'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'x', new PC_ItemStack(gate, 1, PClo_GateType.XOR)
                });
        PC_Utils.addRecipe(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.D),
                new Object[]
                {
                    " S ",
                    "RSR",
                    " S ",
                    'S', Block.stone, 'R', Item.redstone
                });
        PC_Utils.addRecipe(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.RS),
                new Object[]
                {
                    " R ",
                    "SLS",
                    "R R",
                    'R', Item.redstone, 'S', Block.stone, 'L', Block.lever
                });
        PC_Utils.addRecipe(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.T),
                new Object[]
                {
                    "RSR",
                    'R', Item.redstone, 'S', Block.stone
                });
        PC_Utils.addRecipe(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.RANDOM),
                new Object[] { "R", "T",
                        'R', Item.redstone, 'T', new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.T)
                             });
        PC_Utils.addRecipe(new PC_ItemStack(delayer, 1, PClo_DelayerType.FIFO),
                new Object[]
                {
                    "DDD",
                    "SSS",
                    'D', Item.redstoneRepeater, 'S', Block.stone
                });
        PC_Utils.addRecipe(new PC_ItemStack(delayer, 1, PClo_DelayerType.HOLD),
                new Object[]
                {
                    "DD",
                    "SS",
                    'D', Item.redstoneRepeater, 'S', Block.stone
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.DAY),
                new Object[]
                {
                    "G",
                    "P",
                    'G', Item.lightStoneDust, 'P', Block.pressurePlatePlanks
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.DAY),
                new Object[]
                {
                    "G",
                    "P",
                    'G', Item.lightStoneDust, 'P', Block.pressurePlateStone
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.NIGHT),
                new Object[]
                {
                    "N",
                    "G",
                    'N', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'G', new PC_ItemStack(special, 1, PClo_SpecialType.DAY)
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.RAIN),
                new Object[]
                {
                    "L",
                    "P",
                    'L', new PC_ItemStack(Item.dyePowder, 1, 4), 'P', Block.pressurePlatePlanks
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.RAIN),
                new Object[]
                {
                    "L",
                    "P",
                    'L', new PC_ItemStack(Item.dyePowder, 1, 4), 'P', Block.pressurePlateStone
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_EMPTY),
                new Object[]
                {
                    "C",
                    "P",
                    'C', Block.chest, 'P', Block.pressurePlatePlanks
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_EMPTY),
                new Object[]
                {
                    "C",
                    "P",
                    'C', Block.chest, 'P', Block.pressurePlateStone
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_FULL),
                new Object[]
                {
                    "I",
                    "G",
                    'I', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'G', new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_EMPTY)
                });
        PC_Utils.addRecipe(new PC_ItemStack(special, 1, PClo_SpecialType.SPECIAL),
                new Object[]
                {
                    " I",
                    "RS",
                    'R', Item.redstone, 'S', Block.stone, 'I', Item.ingotIron
                });
        PC_Utils.addRecipe(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_STRAIGHT),
                new Object[]
                {
                    "R",
                    "R",
                    "R",
                    'R', Item.redstone
                });
        PC_Utils.addRecipe(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_CORNER),
                new Object[]
                {
                    "RR",
                    " R",
                    'R', Item.redstone
                });
        PC_Utils.addRecipe(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_STRAIGHT_I),
                new Object[]
                {
                    "R",
                    "S",
                    'R', Item.redstone, 'S', new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_STRAIGHT)
                });
        PC_Utils.addRecipe(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_CORNER_I),
                new Object[]
                {
                    "R",
                    "S",
                    'R', Item.redstone, 'S', new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_CORNER)
                });
        PC_Utils.addRecipe(new PC_ItemStack(repeater, 1, PClo_RepeaterType.CROSSING),
                new Object[]
                {
                    " r ",
                    "rrr",
                    " r ",
                    'r', Item.redstone
                });
        PC_Utils.addRecipe(new PC_ItemStack(repeater, 1, PClo_RepeaterType.SPLITTER_I),
                new Object[]
                {
                    "SrS",
                    "rrr",
                    "SrS",
                    'r', Item.redstone, 'S', Block.stone
                });
    }

    @Override
    protected List<String> addSplashes(List<String> list)
    {
        list.add("Adjustable clock pulse!");
        return list;
    }
}
