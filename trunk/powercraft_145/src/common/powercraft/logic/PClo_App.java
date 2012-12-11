package powercraft.logic;

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

public class PClo_App implements PC_IModule
{

    public static PC_Block pulsar;
    public static PC_Block gate;
    public static PC_Block flipFlop;
    public static PC_Block delayer;
    public static PC_Block special;
    public static PC_Block repeater;
    
	@Override
	public String getName() {
		return "Logic";
	}

	@Override
	public String getVersion() {
		return "1.0AlphaA";
	}

	@Override
	public void preInit() {}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Configuration config) {}

	@Override
	public void initBlocks(){
        pulsar = (PC_Block)PC_Utils.register(this, PClo_BlockPulsar.class, PClo_TileEntityPulsar.class);
        gate = (PC_Block)PC_Utils.register(this, PClo_BlockGate.class, PClo_ItemBlockGate.class, PClo_TileEntityGate.class);
        flipFlop = (PC_Block)PC_Utils.register(this, PClo_BlockFlipFlop.class, PClo_ItemBlockFlipFlop.class, PClo_TileEntityFlipFlop.class);
        delayer = (PC_Block)PC_Utils.register(this, PClo_BlockDelayer.class, PClo_ItemBlockDelayer.class, PClo_TileEntityDelayer.class);
        special = (PC_Block)PC_Utils.register(this, PClo_BlockSpecial.class, PClo_ItemBlockSpecial.class, PClo_TileEntitySpecial.class);
        repeater = (PC_Block)PC_Utils.register(this, PClo_BlockRepeater.class, PClo_ItemBlockRepeater.class, PClo_TileEntityRepeater.class);
    }

    @Override
    public void initItems(){}
	
	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
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
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		// TODO Auto-generated method stub
		return null;
	}
}
