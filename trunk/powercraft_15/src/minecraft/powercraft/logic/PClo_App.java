package powercraft.logic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_InitRecipes;
import powercraft.launcher.loader.PC_Module.PC_RegisterGuis;
import powercraft.api.PC_Struct2;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.recipes.PC_IRecipe;
import powercraft.api.recipes.PC_ShapedRecipes;

@PC_Module(name="Logic", version="1.1.0")
public class PClo_App{

	@PC_FieldObject(clazz=PClo_BlockPulsar.class)
    public static PC_Block pulsar;
	@PC_FieldObject(clazz=PClo_BlockGate.class)
    public static PC_Block gate;
	@PC_FieldObject(clazz=PClo_BlockFlipFlop.class)
    public static PC_Block flipFlop;
	@PC_FieldObject(clazz=PClo_BlockDelayer.class)
    public static PC_Block delayer;
	@PC_FieldObject(clazz=PClo_BlockSpecial.class)
    public static PC_Block special;
	@PC_FieldObject(clazz=PClo_BlockRepeater.class)
    public static PC_Block repeater;
    
	@PC_InitRecipes
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
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
                    	'G', Item.lightStoneDust, 'P', Block.pressurePlatePlanks, Block.pressurePlateStone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.NIGHT),
                    "N",
                    "G",
                    	'N', new PC_ItemStack(gate, 1, PClo_GateType.NOT), 'G', new PC_ItemStack(special, 1, PClo_SpecialType.DAY)));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.RAIN),
                	"L",
                    "P",
                    	'L', new PC_ItemStack(Item.dyePowder, 1, 4), 'P', Block.pressurePlatePlanks, Block.pressurePlateStone));
        recipes.add(new PC_ShapedRecipes(new PC_ItemStack(special, 1, PClo_SpecialType.CHEST_EMPTY),
                	"C",
                    "P",
                    	'C', Block.chest, 'P', Block.pressurePlatePlanks, Block.pressurePlateStone));
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

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
			guis.add(new PC_Struct2<String, Class>("Special", PClo_ContainerSpecial.class));
		return guis;
	}
}
