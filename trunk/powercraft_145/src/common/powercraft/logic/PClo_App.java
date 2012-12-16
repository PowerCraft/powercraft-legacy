package powercraft.logic;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Property;
import powercraft.management.PC_ShapedRecipes;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleLoader;

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
	public void initProperties(PC_Property config) {}

	@Override
	public void initBlocks(){
        pulsar = (PC_Block)ModuleLoader.register(this, PClo_BlockPulsar.class, PClo_TileEntityPulsar.class);
        gate = (PC_Block)ModuleLoader.register(this, PClo_BlockGate.class, PClo_ItemBlockGate.class, PClo_TileEntityGate.class);
        flipFlop = (PC_Block)ModuleLoader.register(this, PClo_BlockFlipFlop.class, PClo_ItemBlockFlipFlop.class, PClo_TileEntityFlipFlop.class);
        delayer = (PC_Block)ModuleLoader.register(this, PClo_BlockDelayer.class, PClo_ItemBlockDelayer.class, PClo_TileEntityDelayer.class);
        special = (PC_Block)ModuleLoader.register(this, PClo_BlockSpecial.class, PClo_ItemBlockSpecial.class, PClo_TileEntitySpecial.class);
        repeater = (PC_Block)ModuleLoader.register(this, PClo_BlockRepeater.class, PClo_ItemBlockRepeater.class, PClo_TileEntityRepeater.class);
	}

    @Override
    public void initItems(){}
	
	@Override
	public List<IRecipe> initRecipes(List<IRecipe> recipes) {
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(pulsar, 1, 0),
                    " r ",
                    "ror",
                    " r ",
                    	'r', Item.redstone, 'o', Block.obsidian));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.NOT),
					"rst",
                    	'r', Item.redstone, 's', Block.stone, 't', Block.torchRedstoneActive));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.AND),
                    " r ",
                    "sss",
                    "rrr",
                    	'r', Item.redstone, 's', Block.stone));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.OR),
                    " r ",
                    "rsr",
                    " r ",
                    	'r', Item.redstone, 's', Block.stone));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.XOR),
                    "r",
                    "x",
                    	'r', Item.redstone, 'x', new PC_ItemStack(gate, 1, PClo_GateType.OR)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.NAND),
                    "n",
                    "a",
                    	'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'a', new PC_ItemStack(gate, 1, PClo_GateType.AND)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.NOR),
                    "n",
                    "o",
                    	'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'o', new PC_ItemStack(gate, 1, PClo_GateType.OR)));
		recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.XNOR),
                    "n",
                    "x",
                    	'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'x', new PC_ItemStack(gate, 1, PClo_GateType.XOR)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(gate, 1, PClo_GateType.XNOR),
                    "n",
                    "x",
                    	'n', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'x', new PC_ItemStack(gate, 1, PClo_GateType.XOR)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.D),
                    " S ",
                    "RSR",
                    " S ",
                    	'S', Block.stone, 'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.RS),
                    " R ",
                    "SLS",
                    "R R",
                    	'R', Item.redstone, 'S', Block.stone, 'L', Block.lever));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.T),
                	"RSR",
                		'R', Item.redstone, 'S', Block.stone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.RANDOM),
    				"R",
    				"T",
                    	'R', Item.redstone, 'T', new PC_ItemStack(flipFlop, 1, PClo_FlipFlopType.T)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(delayer, 1, PClo_DelayerType.FIFO),
                    "DDD",
                    "SSS",
                    	'D', Item.redstoneRepeater, 'S', Block.stone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(delayer, 1, PClo_DelayerType.HOLD),
                    "DD",
                    "SS",
                    	'D', Item.redstoneRepeater, 'S', Block.stone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.DAY),
                	"G",
                    "P",
                    	'G', Item.lightStoneDust, 'P', Block.pressurePlatePlanks));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.DAY),
                	"G",
                    "P",
                    	'G', Item.lightStoneDust, 'P', Block.pressurePlateStone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.NIGHT),
                    "N",
                    "G",
                    	'N', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'G', new PC_ItemStack(special, 1, PClo_SpecialType.DAY)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.RAIN),
                	"L",
                    "P",
                    	'L', new PC_ItemStack(Item.dyePowder, 1, 4), 'P', Block.pressurePlatePlanks));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.RAIN),
        			"L",
                    "P",
                    	'L', new PC_ItemStack(Item.dyePowder, 1, 4), 'P', Block.pressurePlateStone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_EMPTY),
                	"C",
                    "P",
                    	'C', Block.chest, 'P', Block.pressurePlatePlanks));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_EMPTY),
                	"C",
                    "P",
                    	'C', Block.chest, 'P', Block.pressurePlateStone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_FULL),
                	"I",
                    "G",
                    	'I', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'G', new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_EMPTY)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.SPECIAL),
                	" I",
                    "RS",
                    	'R', Item.redstone, 'S', Block.stone, 'I', Item.ingotIron));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_STRAIGHT),
                	"R",
                    "R",
                    "R",
                    	'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_CORNER),
                	"RR",
                    " R",
                    	'R', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_STRAIGHT_I),
                	"R",
                    "S",
                    	'R', Item.redstone, 'S', new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_STRAIGHT)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_CORNER_I),
        			"R",
                    "S",
                    	'R', Item.redstone, 'S', new PC_ItemStack(repeater, 1, PClo_RepeaterType.REPEATER_CORNER)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(repeater, 1, PClo_RepeaterType.CROSSING),
            		" r ",
                    "rrr",
                    " r ",
                    	'r', Item.redstone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(repeater, 1, PClo_RepeaterType.SPLITTER_I),
        			"SrS",
                    "rrr",
                    "SrS",
                    	'r', Item.redstone, 'S', Block.stone));
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
			guis.add(new PC_Struct2<String, Class>("Special", PClo_ContainerSpecial.class));
		return guis;
	}
}
