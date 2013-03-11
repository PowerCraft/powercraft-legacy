/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/

package powercraft.weasel.jep;


import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_ItemRegistry;
import powercraft.weasel.engine.Calc;
import powercraft.weasel.jep.function.Abs;
import powercraft.weasel.jep.function.ArcCosine;
import powercraft.weasel.jep.function.ArcCosineH;
import powercraft.weasel.jep.function.ArcSine;
import powercraft.weasel.jep.function.ArcSineH;
import powercraft.weasel.jep.function.ArcTanH;
import powercraft.weasel.jep.function.ArcTangent;
import powercraft.weasel.jep.function.ArcTangent2;
import powercraft.weasel.jep.function.Arg;
import powercraft.weasel.jep.function.Binomial;
import powercraft.weasel.jep.function.BitwiseOperation;
import powercraft.weasel.jep.function.Bool;
import powercraft.weasel.jep.function.Ceil;
import powercraft.weasel.jep.function.ComplexPFMC;
import powercraft.weasel.jep.function.Conjugate;
import powercraft.weasel.jep.function.Cosine;
import powercraft.weasel.jep.function.CosineH;
import powercraft.weasel.jep.function.Exp;
import powercraft.weasel.jep.function.Floor;
import powercraft.weasel.jep.function.HasNum;
import powercraft.weasel.jep.function.If;
import powercraft.weasel.jep.function.Imaginary;
import powercraft.weasel.jep.function.Logarithm;
import powercraft.weasel.jep.function.LogicalFn;
import powercraft.weasel.jep.function.MakeBit;
import powercraft.weasel.jep.function.MakeByte;
import powercraft.weasel.jep.function.MakeColor;
import powercraft.weasel.jep.function.Max;
import powercraft.weasel.jep.function.Mean;
import powercraft.weasel.jep.function.Min;
import powercraft.weasel.jep.function.Modulus;
import powercraft.weasel.jep.function.NaturalLogarithm;
import powercraft.weasel.jep.function.Num;
import powercraft.weasel.jep.function.NumForm;
import powercraft.weasel.jep.function.Polar;
import powercraft.weasel.jep.function.PostfixMathCommandI;
import powercraft.weasel.jep.function.Power;
import powercraft.weasel.jep.function.Range;
import powercraft.weasel.jep.function.Real;
import powercraft.weasel.jep.function.Round;
import powercraft.weasel.jep.function.Sine;
import powercraft.weasel.jep.function.SineH;
import powercraft.weasel.jep.function.SquareRoot;
import powercraft.weasel.jep.function.Str;
import powercraft.weasel.jep.function.StrFormat;
import powercraft.weasel.jep.function.StrLen;
import powercraft.weasel.jep.function.StrMorf;
import powercraft.weasel.jep.function.StringChar;
import powercraft.weasel.jep.function.StringCheck;
import powercraft.weasel.jep.function.Sum;
import powercraft.weasel.jep.function.TanH;
import powercraft.weasel.jep.function.Tangent;
import powercraft.weasel.jep.function.TimeFunc;
import powercraft.weasel.jep.function.BitwiseOperation.OpType;
import powercraft.weasel.jep.function.LogicalFn.LogicalFnType;
import powercraft.weasel.jep.function.StrFormat.EnumType;
import powercraft.weasel.jep.function.TimeFunc.TimeFuncType;
import powercraft.weasel.jep.type.Complex;
import powercraft.weasel.jep.type.DoubleNumberFactory;
import powercraft.weasel.jep.type.NumberFactory;


/**
 * The JEP class is the main interface with which the user should interact. It
 * contains all necessary methods to parse and evaluate expressions.
 * <p>
 * The most important methods are parseExpression(String), for parsing the
 * mathematical expression, and getValue() for obtaining the value of the
 * expression.
 * <p>
 * Visit <a
 * href="http://www.singularsys.com/jep">http://www.singularsys.com/jep</a> for
 * the newest version of JEP, and complete documentation.
 * 
 * @author Nathan Funk
 * @since 04/02/13 added Binom function
 */
public class JEP {

	/** Debug flag for extra command line output */
	private static final boolean debug = false;

	/** Traverse option */
	private boolean traverse;

	/** Allow undeclared variables option */
	protected boolean allowUndeclared;

	/** Allow undeclared variables option */
	protected boolean allowAssignment;

	/** Implicit multiplication option */
	protected boolean implicitMul;

	/** Symbol Table */
	protected SymbolTable symTab;

	/** Function Table */
	protected FunctionTable funTab;

	/** Error List */
	protected Vector<String> errorList;

	/** The parser object */
	protected Parser parser;

	/** Node at the top of the parse tree */
	private Node topNode;

	/** Evaluator */
	protected EvaluatorVisitor ev;

	/** Number factory */
	protected NumberFactory numberFactory;

	/** OperatorSet */
	protected OperatorSet opSet;

	/**
	 * Creates a new JEP instance with the default settings.
	 * <p>
	 * Traverse = false<br>
	 * Allow undeclared variables = false<br>
	 * Implicit multiplication = false<br>
	 * Number Factory = DoubleNumberFactory
	 */
	public JEP() {
		topNode = null;
		traverse = false;
		allowUndeclared = false;
		allowAssignment = false;
		implicitMul = false;
		numberFactory = new DoubleNumberFactory();
		opSet = new OperatorSet();
		initSymTab();
		initFunTab();
		errorList = new Vector<String>();
		ev = new EvaluatorVisitor();
		parser = new Parser(new StringReader(""));

		//Ensure errors are reported for the initial expression
		//e.g. No expression entered
		//parseExpression("");
	}

	/**
	 * Creates a new JEP instance with custom settings. If the numberFactory_in
	 * is null, the default number factory is used.
	 * 
	 * @param traverse_in The traverse option.
	 * @param allowUndeclared_in The "allow undeclared variables" option.
	 * @param implicitMul_in The implicit multiplication option.
	 * @param numberFactory_in The number factory to be used.
	 */
	public JEP(boolean traverse_in, boolean allowUndeclared_in, boolean implicitMul_in, NumberFactory numberFactory_in) {
		topNode = null;
		traverse = traverse_in;
		allowUndeclared = allowUndeclared_in;
		implicitMul = implicitMul_in;
		if (numberFactory_in == null) {
			numberFactory = new DoubleNumberFactory();
		} else {
			numberFactory = numberFactory_in;
		}
		opSet = new OperatorSet();
		initSymTab();
		initFunTab();
		errorList = new Vector<String>();
		ev = new EvaluatorVisitor();
		parser = new Parser(new StringReader(""));

		//Ensure errors are reported for the initial expression
		//e.g. No expression entered
		parseExpression("");
	}

	/**
	 * This constructor copies the SymbolTable and other components of the
	 * arguments to the new instance. Subclasses can call this protected
	 * constructor and set the individual components themselves.
	 * 
	 * @param j Source jep
	 * @since 2.3.0 alpha
	 */
	protected JEP(JEP j) {
		this.topNode = null;
		this.traverse = j.traverse;
		this.allowUndeclared = j.allowUndeclared;
		this.allowAssignment = j.allowAssignment;
		this.implicitMul = j.implicitMul;
		this.ev = j.ev;
		this.funTab = j.funTab;
		this.opSet = j.opSet;
		this.numberFactory = j.numberFactory;
		this.parser = j.parser;
		this.symTab = j.symTab;
		this.errorList = j.errorList;
	}

	/**
	 * Creates a new SymbolTable object as symTab.
	 */
	public void initSymTab() {
		//Init SymbolTable
		symTab = new SymbolTable(new VariableFactory());
	}

	/**
	 * Creates a new FunctionTable object as funTab.
	 */
	public void initFunTab() {
		//Init FunctionTable
		funTab = new FunctionTable();
	}


	/**
	 * Create a parser suitable for weasel and other numeric expression
	 * evaluating systems in PowerCraft.
	 * 
	 * @param assignmentAllowed
	 * @return the new JEP
	 */
	public static JEP createWeaselParser(boolean assignmentAllowed) {
		JEP newjep = new JEP();
		newjep.allowAssignment = assignmentAllowed;
		newjep.addStandardConstants();
		newjep.addBlockItemConstants();
		newjep.addStandardFunctions();
		newjep.addWeaselFunctions();
		return newjep;
	}

	/**
	 * Add MightyPork's functions
	 */
	public void addWeaselFunctions() {

		// aliases
		funTab.put("rnd", new powercraft.weasel.jep.function.Random());
		funTab.put("random", new powercraft.weasel.jep.function.Random());

		// binary functions

//		funTab.put("not", new Not());
//		funTab.put("or", new LogicalFn(LogicalFnType.OR));
//		funTab.put("and", new LogicalFn(LogicalFnType.AND));
//		funTab.put("xor", new LogicalFn(LogicalFnType.XOR));
//		funTab.put("nor", new LogicalFn(LogicalFnType.NOR));
//		funTab.put("nand", new LogicalFn(LogicalFnType.NAND));
//		funTab.put("xnor", new LogicalFn(LogicalFnType.NXOR));
//		funTab.put("nxor", new LogicalFn(LogicalFnType.NXOR));


		// aditional constructors
		funTab.put("list", new powercraft.weasel.jep.function.List());
		funTab.put("range", new Range());

		// string
		funTab.put("tolower", new StrMorf(0));
		funTab.put("toupper", new StrMorf(1));
		funTab.put("reverse", new StrMorf(2));
		
		funTab.put("length", new StrLen());
		funTab.put("strlen", new StrLen());
		funTab.put("len", new StrLen());
		
		funTab.put("charat", new StringChar());
		funTab.put("strchar", new StringChar());
		
		funTab.put("starts", new StringCheck(0));
		funTab.put("startsWith", new StringCheck(0));
		funTab.put("startswith", new StringCheck(0));
		
		funTab.put("ends", new StringCheck(1));
		funTab.put("endsWith", new StringCheck(1));
		funTab.put("endswith", new StringCheck(1));
		
		funTab.put("contains", new StringCheck(2));
		
		funTab.put("matches", new StringCheck(3));
		
		funTab.put("rgb", new MakeColor(0));
		
		funTab.put("hsv", new MakeColor(1));
		funTab.put("hsvtorgb", new MakeColor(1));
		funTab.put("hsv2rgb", new MakeColor(1));
		funTab.put("hsvrgb", new MakeColor(1));
		
		funTab.put("toBool", new Bool());
		funTab.put("toBoolean", new Bool());
		funTab.put("toboolean", new Bool());
		funTab.put("tobool", new Bool());
		funTab.put("toBool", new Bool());
		funTab.put("atob", new Bool());
		funTab.put("tob", new Bool());
		funTab.put("boolean", new Bool());
		
		funTab.put("toStr", new Str());
		funTab.put("tostr", new Str());
		funTab.put("atos", new Str());
		funTab.put("toString", new Str());
		funTab.put("tostring", new Str());
		funTab.put("tos", new Str());
		
		funTab.put("toNum", new Num());
		funTab.put("tonum", new Num());
		funTab.put("ton", new Num());
		funTab.put("tonumber", new Num());
		funTab.put("toNumber", new Num());
		funTab.put("atoi", new Num());
		funTab.put("toi", new Num());
		funTab.put("toInt", new Num());
		funTab.put("toint", new Num());
		
		funTab.put("num2hex", new NumForm(16));
		funTab.put("numhex", new NumForm(16));
		funTab.put("dec2hex", new NumForm(16));
		funTab.put("dechex", new NumForm(16));		
		funTab.put("hex", new NumForm(16));
		
		funTab.put("num2bun", new NumForm(2));
		funTab.put("numbin", new NumForm(2));
		funTab.put("dec2bun", new NumForm(2));
		funTab.put("decbin", new NumForm(2));
		funTab.put("bin", new NumForm(2));
		
		funTab.put("isnum", new HasNum());
		
		funTab.put("zerofill", new StrFormat(EnumType.ZEROFILL));
		funTab.put("zf", new StrFormat(EnumType.ZEROFILL));
		
		funTab.put("cutfirst", new StrFormat(EnumType.CUTFIRST));
		funTab.put("cf", new StrFormat(EnumType.CUTFIRST));
		
		funTab.put("cutlast", new StrFormat(EnumType.CUTLAST));
		funTab.put("cl", new StrFormat(EnumType.CUTLAST));
		
		
		funTab.put("getfirst", new StrFormat(EnumType.GETFIRST));
		funTab.put("gf", new StrFormat(EnumType.GETFIRST));
		funTab.put("first", new StrFormat(EnumType.GETFIRST));
		
		funTab.put("getlast", new StrFormat(EnumType.GETLAST));
		funTab.put("gl", new StrFormat(EnumType.GETLAST));		
		funTab.put("last", new StrFormat(EnumType.GETLAST));

		// time
		funTab.put("hours", new TimeFunc(TimeFuncType.H));
		funTab.put("mins", new TimeFunc(TimeFuncType.M));
		funTab.put("secs", new TimeFunc(TimeFuncType.S));
		funTab.put("minutes", new TimeFunc(TimeFuncType.M));
		funTab.put("seconds", new TimeFunc(TimeFuncType.S));
		funTab.put("hours_r", new TimeFunc(TimeFuncType.RH));
		funTab.put("mins_r", new TimeFunc(TimeFuncType.RM));
		funTab.put("secs_r", new TimeFunc(TimeFuncType.RS));
		funTab.put("minutes_r", new TimeFunc(TimeFuncType.RM));
		funTab.put("seconds_r", new TimeFunc(TimeFuncType.RS));
		funTab.put("time", new TimeFunc(TimeFuncType.SECS_ALL));
		funTab.put("time_r", new TimeFunc(TimeFuncType.SECS_ALL_R));
		funTab.put("ftime", new TimeFunc(TimeFuncType.FTIME));
		funTab.put("ftime_r", new TimeFunc(TimeFuncType.FTIME_R));
		funTab.put("moon", new TimeFunc(TimeFuncType.MOON));

		//other
		funTab.put("avg", new Mean());
		funTab.put("mean", new Mean());
		funTab.put("min", new Min());
		funTab.put("max", new Max());
		funTab.put("byte", new MakeByte());
		funTab.put("bit", new MakeBit());

		funTab.put("not", new BitwiseOperation(OpType.NOT));
		funTab.put("or", new BitwiseOperation(OpType.OR));
		funTab.put("and", new BitwiseOperation(OpType.AND));
		funTab.put("xor", new BitwiseOperation(OpType.XOR));
		funTab.put("nor", new BitwiseOperation(OpType.NOR));
		funTab.put("nand", new BitwiseOperation(OpType.NAND));
		funTab.put("xnor", new BitwiseOperation(OpType.NXOR));
		funTab.put("nxor", new BitwiseOperation(OpType.NXOR));
		funTab.put("lsl", new BitwiseOperation(OpType.LSL));
		funTab.put("lsr", new BitwiseOperation(OpType.LSR));
		funTab.put("lshift", new BitwiseOperation(OpType.LSL));
		funTab.put("rshift", new BitwiseOperation(OpType.LSR));

		funTab.put("even", new LogicalFn(LogicalFnType.EVEN));
		funTab.put("odd", new LogicalFn(LogicalFnType.ODD));

	}

	/**
	 * Get list of function names
	 * 
	 * @return keywords string array
	 */
	public String[] getKeywords() {
		ArrayList<String> list = new ArrayList<String>();

		list.addAll(funTab.keySet());

		return list.toArray(new String[list.size()]);
	}

	/**
	 * Get list of function names
	 * 
	 * @return keywords string array
	 */
	public String[] getConstants() {
		ArrayList<String> list = new ArrayList<String>();

		for (Entry<String, Variable> var : symTab.entrySet()) {
			if (var.getValue().isConstant()) list.add(var.getKey());
		}

		return list.toArray(new String[list.size()]);
	}

	/**
	 * Adds the standard functions to the parser. If this function is not called
	 * before parsing an expression, functions such as sin() or cos() would
	 * produce an "Unrecognized function..." error. In most cases, this method
	 * should be called immediately after the JEP object is created.
	 * 
	 * @since 2.3.0 alpha added if and exp functions
	 * @since 2.3.0 beta 1 added str function
	 */
	public void addStandardFunctions() {
		//add functions to Function Table
		funTab.put("sin", new Sine());
		funTab.put("cos", new Cosine());
		funTab.put("tan", new Tangent());
		funTab.put("asin", new ArcSine());
		funTab.put("acos", new ArcCosine());
		funTab.put("atan", new ArcTangent());
		funTab.put("atan2", new ArcTangent2());

		funTab.put("sinh", new SineH());
		funTab.put("cosh", new CosineH());
		funTab.put("tanh", new TanH());
		funTab.put("asinh", new ArcSineH());
		funTab.put("acosh", new ArcCosineH());
		funTab.put("atanh", new ArcTanH());

		funTab.put("log", new Logarithm());
		funTab.put("ln", new NaturalLogarithm());
		funTab.put("exp", new Exp());
		funTab.put("pow", new Power());

		funTab.put("sqrt", new SquareRoot());
		funTab.put("abs", new Abs());
		funTab.put("mod", new Modulus());
		funTab.put("sum", new Sum());

		funTab.put("rand", new powercraft.weasel.jep.function.Random());

		// rjm additions
		funTab.put("is", new If());
		funTab.put("str", new Str());

		// rjm 13/2/05
		funTab.put("binom", new Binomial());

		// rjm 26/1/07
		funTab.put("round", new Round());
		funTab.put("floor", new Floor());
		funTab.put("ceil", new Ceil());
	}

	/**
	 * Adds the constants pi and e to the parser. As addStandardFunctions(),
	 * this method should be called immediately after the JEP object is created.
	 */
	public void addStandardConstants() {
		//add constants to Symbol Table
		symTab.addConstant("pi", new Double(Math.PI));
		symTab.addConstant("e", new Double(Math.E));
		symTab.addConstant("true", new Boolean(true));
		symTab.addConstant("false", new Boolean(false));
		symTab.addConstant("True", new Boolean(true));
		symTab.addConstant("False", new Boolean(false));
	}

	/**
	 * add constants for blocks and items - some are names, but mostly ids.
	 */
	public void addBlockItemConstants() {
		symTab.addConstant("DIRT", Block.dirt.blockID);
		symTab.addConstant("COBBLE", Block.cobblestone.blockID);
		symTab.addConstant("MOSSYCOBBLE", Block.cobblestoneMossy.blockID);
		symTab.addConstant("MOSSY_COBBLE", Block.cobblestoneMossy.blockID);
		symTab.addConstant("PLANKS", Block.planks.blockID);
		symTab.addConstant("LOG", Block.wood.blockID);
		symTab.addConstant("GRAVEL", Block.gravel.blockID);
		symTab.addConstant("SAND", Block.sand.blockID);
		symTab.addConstant("SANDSTONE", Block.sandStone.blockID);
		symTab.addConstant("NETHERRACK", Block.netherrack.blockID);
		symTab.addConstant("BRICK", Block.brick.blockID);
		symTab.addConstant("STONEBRICK", Block.stoneBrick.blockID);
		symTab.addConstant("STONE_BRICK", Block.stoneBrick.blockID);
		symTab.addConstant("NETHERBRICK", Block.netherBrick.blockID);
		symTab.addConstant("NETHER_BRICK", Block.netherBrick.blockID);
		symTab.addConstant("AIR", 0);
		symTab.addConstant("BEDROCK", Block.bedrock.blockID);
		symTab.addConstant("OBSIDIAN", Block.obsidian.blockID);
		symTab.addConstant("ENDSTONE", Block.whiteStone.blockID);
		symTab.addConstant("SLAB", Block.stoneSingleSlab.blockID);		
		symTab.addConstant("LEAVES", Block.leaves.blockID);
		symTab.addConstant("STONE", Block.stone.blockID);
		symTab.addConstant("FENCE", Block.fence.blockID);
		symTab.addConstant("NETHERFENCE", Block.netherFence.blockID);
		symTab.addConstant("NETHER_FENCE", Block.netherFence.blockID);
		symTab.addConstant("GOLDCUBE", Block.blockGold.blockID);
		symTab.addConstant("IRONCUBE", Block.blockSteel.blockID);
		symTab.addConstant("LAPISCUBE", Block.blockLapis.blockID);
		symTab.addConstant("GOLD_CUBE", Block.blockGold.blockID);
		symTab.addConstant("IRON_CUBE", Block.blockSteel.blockID);
		symTab.addConstant("LAPIS_CUBE", Block.blockLapis.blockID);
		symTab.addConstant("GOLDBLOCK", Block.blockGold.blockID);
		symTab.addConstant("IRONBLOCK", Block.blockSteel.blockID);
		symTab.addConstant("LAPISBLOCK", Block.blockLapis.blockID);
		symTab.addConstant("GOLD_BLOCK", Block.blockGold.blockID);
		symTab.addConstant("IRON_BLOCK", Block.blockSteel.blockID);
		symTab.addConstant("LAPIS_BLOCK", Block.blockLapis.blockID);
		symTab.addConstant("COALORE", Block.oreCoal.blockID);
		symTab.addConstant("IRONORE", Block.oreIron.blockID);
		symTab.addConstant("GOLDORE", Block.oreGold.blockID);
		symTab.addConstant("DIAMONDORE", Block.oreDiamond.blockID);
		symTab.addConstant("LAPISORE", Block.oreLapis.blockID);
		symTab.addConstant("REDSTONEORE", Block.oreRedstone.blockID);
		symTab.addConstant("COAL_ORE", Block.oreCoal.blockID);
		symTab.addConstant("IRON_ORE", Block.oreIron.blockID);
		symTab.addConstant("GOLD_ORE", Block.oreGold.blockID);
		symTab.addConstant("REDSTONE_ORE", Block.oreRedstone.blockID);
		symTab.addConstant("DIAMOND_ORE", Block.oreDiamond.blockID);
		symTab.addConstant("LAPIS_ORE", Block.oreLapis.blockID);
		symTab.addConstant("GLOWSTONE", Block.glowStone.blockID);
		symTab.addConstant("SOULSAND", Block.slowSand.blockID);
		symTab.addConstant("WOOL", Block.cloth.blockID);
		symTab.addConstant("MELON", Block.melon.blockID);
		symTab.addConstant("PUMPKIN", Block.pumpkin.blockID);
		symTab.addConstant("TNT", Block.tnt.blockID);
		symTab.addConstant("TORCH", Block.torchWood.blockID);
		symTab.addConstant("GRASS", Block.grass.blockID);
		
		symTab.addConstant("FLINT", Item.flint.itemID);
		symTab.addConstant("GOND", Item.ingotGold.itemID);
		symTab.addConstant("IRON", Item.ingotIron.itemID);
		symTab.addConstant("DIAMOND", Item.diamond.itemID);
		symTab.addConstant("DYE", Item.dyePowder.itemID);
		symTab.addConstant("GOLD_NUGGET", Item.goldNugget.itemID);
		symTab.addConstant("SAPLING", Block.sapling.blockID);
		symTab.addConstant("REED", Item.reed.itemID);
		symTab.addConstant("SUGARCANE", Item.reed.itemID);
		symTab.addConstant("SUGAR_CANE", Item.reed.itemID);
		symTab.addConstant("SEEDS", Item.seeds.itemID);
		symTab.addConstant("MELON_SEEDS", Item.melonSeeds.itemID);
		symTab.addConstant("PUMPKIN_SEEDS", Item.pumpkinSeeds.itemID);
		symTab.addConstant("WHEAT", Item.wheat.itemID);
		symTab.addConstant("APPLE", Item.appleRed.itemID);
		symTab.addConstant("REDSTONE", Item.redstone.itemID);
		symTab.addConstant("COAL", Item.coal.itemID);
		int id = PC_BlockRegistry.getPCBlockIDByName("PCco_BlockPowerCrystal");
		if(id!=0)
			symTab.addConstant("CRYSTAL", id);
		id = PC_ItemRegistry.getPCItemIDByName("PCco_ItemPowerDust");
		if(id!=0)
			symTab.addConstant("POWERDUST", id);
		symTab.addConstant("WATER", Block.waterMoving.blockID);
		symTab.addConstant("LAVA", Block.lavaMoving.blockID);
		symTab.addConstant("BUCKETL", Item.bucketLava.itemID);
		symTab.addConstant("BUCKETLAVA", Item.bucketLava.itemID);
		symTab.addConstant("BUCKET_LAVA", Item.bucketLava.itemID);
		symTab.addConstant("LAVABUCKET", Item.bucketLava.itemID);
		symTab.addConstant("LAVA_BUCKET", Item.bucketLava.itemID);
		symTab.addConstant("BUCKETW", Item.bucketWater.itemID);
		symTab.addConstant("BUCKETWATER", Item.bucketWater.itemID);
		symTab.addConstant("BUCKET_WATER", Item.bucketWater.itemID);
		symTab.addConstant("WATERBUCKET", Item.bucketWater.itemID);
		symTab.addConstant("WATER_BUCKET", Item.bucketWater.itemID);
		
		symTab.addConstant("BUILDING_BLOCKS", "BUILDING_BLOCK");
		symTab.addConstant("BUILDING_BLOCK", "BUILDING_BLOCK");
		symTab.addConstant("BUILDING", "BUILDING_BLOCK");
		symTab.addConstant("BLOCKS", "BLOCK");
		symTab.addConstant("ITEMS", "ITEM");
		symTab.addConstant("ALL", "ALL");
		symTab.addConstant("ORE", "ORE");
		symTab.addConstant("ORES", "ORE");
		symTab.addConstant("FUEL", "FUEL");
		symTab.addConstant("FUELS", "FUEL");		
		symTab.addConstant("JUNK", "JUNK");
		symTab.addConstant("VALUABLE", "VALUABLE");
		symTab.addConstant("PRECIOUS", "VALUABLE");
		symTab.addConstant("RARE", "VALUABLE");
		
		symTab.addConstant("LAPIS", "LAPIS");
		symTab.addConstant("BONEMEAL", "BONEMEAL");
		symTab.addConstant("BUCKETS", "BUCKET");
		symTab.addConstant("BUCKET", "BUCKET");

		symTab.addConstant("KEEP_FUEL", "KEEP_FUEL");
		symTab.addConstant("TORCHES", "TORCHES");
		symTab.addConstant("TORCH_FLOOR", "TORCH_FLOOR");
		symTab.addConstant("COMPRESS", "COMPRESS");
		symTab.addConstant("MINING", "MINING");
		symTab.addConstant("BRIDGE", "BRIDGE");
		symTab.addConstant("LAVA_FILL", "LAVA");
		symTab.addConstant("WATER_FILL", "WATER");
		symTab.addConstant("AIR_FILL", "TUNNEL");
		symTab.addConstant("LAVAFILL", "LAVA");
		symTab.addConstant("WATERFILL", "WATER");
		symTab.addConstant("AIRFILL", "TUNNEL");
		symTab.addConstant("TUNNEL", "TUNNEL");
		symTab.addConstant("COBBLEMAKER", "COBBLE");
		symTab.addConstant("MAKE_COBBLE", "COBBLE");
		
		
	}
	/**
	 * Call this function if you want to parse expressions which involve complex
	 * numbers. This method specifies "i" as the imaginary unit (0,1). Two
	 * functions re() and im() are also added for extracting the real or
	 * imaginary components of a complex number respectively.
	 * <p>
	 * 
	 * @since 2.3.0 alpha The functions cmod and arg are added to get the
	 *        modulus and argument.
	 * @since 2.3.0 beta 1 The functions complex and polar to convert x,y and
	 *        r,theta to Complex.
	 * @since Feb 05 added complex conjugate conj.
	 */
	public void addComplex() {
		//add constants to Symbol Table
		symTab.addConstant("i", new Complex(0, 1));
		funTab.put("re", new Real());
		funTab.put("im", new Imaginary());
		funTab.put("arg", new Arg());
		funTab.put("cmod", new Abs());
		funTab.put("complex", new ComplexPFMC());
		funTab.put("polar", new Polar());
		funTab.put("conj", new Conjugate());
	}

	/**
	 * Adds a new function to the parser. This must be done before parsing an
	 * expression so the parser is aware that the new function may be contained
	 * in the expression.
	 * 
	 * @param functionName The name of the function
	 * @param function The function object that is used for evaluating the
	 *            function
	 */
	public void addFunction(String functionName, PostfixMathCommandI function) {
		funTab.put(functionName, function);
	}

	/**
	 * Adds a constant. This is a variable whose value cannot be changed.
	 * 
	 * @param name var name
	 * @param value var value
	 * @since 2.3.0 beta 1
	 */
	public void addConstant(String name, Object value) {
		symTab.addConstant(name, value);
	}

	/**
	 * Adds a new variable to the parser, or updates the value of an existing
	 * variable. This must be done before parsing an expression so the parser is
	 * aware that the new variable may be contained in the expression.
	 * 
	 * @param name Name of the variable to be added
	 * @param value Initial value or new value for the variable
	 * @return Double object of the variable
	 */
	public Double addVariable(String name, double value) {
		Double object = new Double(value);
		symTab.makeVarIfNeeded(name, object);
		return object;
	}


	/**
	 * Adds a new complex variable to the parser, or updates the value of an
	 * existing variable. This must be done before parsing an expression so the
	 * parser is aware that the new variable may be contained in the expression.
	 * 
	 * @param name Name of the variable to be added
	 * @param re Initial real value or new real value for the variable
	 * @param im Initial imaginary value or new imaginary value for the variable
	 * @return Complex object of the variable
	 */
	public Complex addVariable(String name, double re, double im) {
		Complex object = new Complex(re, im);
		symTab.makeVarIfNeeded(name, object);
		return object;
	}

	/**
	 * Adds a new variable to the parser as an object, or updates the value of
	 * an existing variable. This must be done before parsing an expression so
	 * the parser is aware that the new variable may be contained in the
	 * expression.
	 * 
	 * @param name Name of the variable to be added
	 * @param object Initial value or new value for the variable
	 */
	public void addVariable(String name, Object object) {
		symTab.makeVarIfNeeded(name, object);
	}

	/**
	 * Removes a variable from the parser. For example after calling
	 * addStandardConstants(), removeVariable("e") might be called to remove the
	 * euler constant from the set of variables.
	 * 
	 * @param name variable to remove
	 * @return The value of the variable if it was added earlier. If the
	 *         variable is not in the table of variables, <code>null</code> is
	 *         returned.
	 */
	public Object removeVariable(String name) {
		return symTab.remove(name);
	}

	/**
	 * Returns the value of the variable with given name.
	 * 
	 * @param name name of the variable.
	 * @return the current value of the variable.
	 * @since 2.3.0 alpha
	 */
	public Object getVarValue(String name) {
		return symTab.getVar(name).getValue();
	}

	/**
	 * Sets the value of a variable. The variable must exist before hand.
	 * 
	 * @param name name of the variable.
	 * @param val the initial value of the variable.
	 * @throws NullPointerException if the variable has not been previously
	 *             created with {@link #addVariable(String,Object)} first.
	 * @since 2.3.0 alpha
	 * @since April 05 - throws an exception if variable unset.
	 */
	public void setVarValue(String name, Object val) {
		symTab.setVarValue(name, val);
	}

	/**
	 * Gets the object representing the variable with a given name.
	 * 
	 * @param name the name of the variable to find.
	 * @return the Variable object or null if name not found.
	 * @since 2.3.0 alpha
	 */
	public Variable getVar(String name) {
		return symTab.getVar(name);
	}

	/**
	 * Removes a function from the parser.
	 * 
	 * @param name function name
	 * @return If the function was added earlier, the function class instance is
	 *         returned. If the function was not present, <code>null</code> is
	 *         returned.
	 */
	public Object removeFunction(String name) {
		return funTab.remove(name);
	}

	/**
	 * Sets the value of the traverse option. setTraverse is useful for
	 * debugging purposes. When traverse is set to true, the parse-tree will be
	 * dumped to the standard output device.
	 * <p>
	 * The default value is false.
	 * 
	 * @param value The boolean traversal option.
	 */
	public void setTraverse(boolean value) {
		traverse = value;
	}

	/**
	 * Returns the value of the traverse option.
	 * 
	 * @return True if the traverse option is enabled. False otherwise.
	 */
	public boolean getTraverse() {
		return traverse;
	}

	/**
	 * Sets the value of the implicit multiplication option. If this option is
	 * set to true before parsing, implicit multiplication will be allowed. That
	 * means that an expression such as
	 * 
	 * <pre>
	 * &quot;1 2&quot;
	 * </pre>
	 * 
	 * is valid and is interpreted as
	 * 
	 * <pre>
	 * &quot;1*2&quot;
	 * </pre>
	 * 
	 * .
	 * <p>
	 * The default value is false.
	 * 
	 * @param value The boolean implicit multiplication option.
	 */
	public void setImplicitMul(boolean value) {
		implicitMul = value;
	}

	/**
	 * Returns the value of the implicit multiplication option.
	 * 
	 * @return True if the implicit multiplication option is enabled. False
	 *         otherwise.
	 */
	public boolean getImplicitMul() {
		return implicitMul;
	}

	/**
	 * Sets the value for the undeclared variables option. If this option is set
	 * to true, expressions containing variables that were not previously added
	 * to JEP will not produce an "Unrecognized Symbol" error. The new variables
	 * will automatically be added while parsing, and initialized to 0.
	 * <p>
	 * If this option is set to false, variables that were not previously added
	 * to JEP will produce an error while parsing.
	 * <p>
	 * The default value is false.
	 * 
	 * @param value The boolean option for allowing undeclared variables.
	 */
	public void setAllowUndeclared(boolean value) {
		allowUndeclared = value;
	}

	/**
	 * Returns the value of the allowUndeclared option.
	 * 
	 * @return True if the allowUndeclared option is enabled. False otherwise.
	 */
	public boolean getAllowUndeclared() {
		return allowUndeclared;
	}

	/**
	 * Sets whether assignment equations like <tt>y=x+1</tt> are allowed.
	 * 
	 * @param value allow
	 * @since 2.3.0 alpha
	 */
	public void setAllowAssignment(boolean value) {
		allowAssignment = value;
	}

	/**
	 * Whether assignment equation <tt>y=x+1</tt> equations are allowed.
	 * 
	 * @return is assignment allowed
	 * @since 2.3.0 alpha
	 */
	public boolean getAllowAssignment() {
		return allowAssignment;
	}



	/**
	 * Parses the expression. If there are errors in the expression, they are
	 * added to the <code>errorList</code> member. Errors can be obtained
	 * through <code>getErrorInfo()</code>.
	 * 
	 * @param expression_in The input expression string
	 * @return the top node of the expression tree if the parse was successful,
	 *         <code>null</code> otherwise
	 */
	public Node parseExpression(String expression_in) {

		try {
			expression_in = Calc.convertNumbersToDecimal(expression_in);
		} catch (ParseException e1) {
			errorList.addElement(e1.getErrorInfo());
		}

		if (hasError()) return null;

		Reader reader = new StringReader(expression_in);

		try {
			// try parsing
			errorList.removeAllElements();
			topNode = parser.parseStream(reader, this);

			// if there is an error in the list, the parse failed
			// so set topNode to null
			if (hasError()) topNode = null;
		} catch (Throwable e) {
			// an exception was thrown, so there is no parse tree
			topNode = null;

			// check the type of error
			if (e instanceof ParseException) {
				// the ParseException object contains additional error
				// information
				errorList.addElement(((ParseException) e).getMessage());
				//getErrorInfo());
			} else {
				// if the exception was not a ParseException, it was most
				// likely a syntax error
				if (debug) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				errorList.addElement("Syntax error");
			}
		}


		// If traversing is enabled, print a dump of the tree to
		// standard output
		if (traverse && !hasError()) {
			ParserVisitor v = new ParserDumpVisitor();
			try {
				topNode.jjtAccept(v, null);
			} catch (ParseException e) {
				errorList.addElement(e.getMessage());
			}
		}

		return topNode;
	}

	/**
	 * Parses an expression. Returns a object of type Node, does not catch
	 * errors. Does not set the topNode variable of the JEP instance. This
	 * method should generally be used with the {@link #evaluate evaluate}
	 * method rather than getValueAsObject.
	 * 
	 * @param expression represented as a string.
	 * @return The top node of a tree representing the parsed expression.
	 * @throws ParseException
	 * @since 2.3.0 alpha
	 * @since 2.3.0 beta - will raise exception if errorList non empty
	 */
	public Node parse(String expression) throws ParseException {

		expression = Calc.convertNumbersToDecimal(expression);

		if (hasError()) throw new ParseException(getErrorInfo());

		java.io.StringReader sr = new java.io.StringReader(expression);
		errorList.removeAllElements();
		Node node = parser.parseStream(sr, this);
		if (this.hasError()) throw new ParseException(getErrorInfo());
		return node;
	}

	/**
	 * Evaluate an expression. This method evaluates the argument rather than
	 * the topNode of the JEP instance. It should be used in conjunction with
	 * {@link #parse parse} rather than {@link #parseExpression parseExpression}
	 * .
	 * 
	 * @param node the top node of the tree representing the expression.
	 * @return The value of the expression
	 * @throws ParseException if for some reason the expression could not be
	 *             evaluated
	 * @throws RuntimeException could potentially be thrown.
	 * @since 2.3.0 alpha
	 */
	public Object evaluate(Node node) throws ParseException {
		return ev.getValue(node, this.symTab);
	}

	/**
	 * Evaluates and returns the value of the expression as a double number.
	 * 
	 * @return The calculated value of the expression as a double number. If the
	 *         type of the value does not implement the Number interface (e.g.
	 *         Complex), NaN is returned. If an error occurs during evaluation,
	 *         NaN is returned and hasError() will return true.
	 * @see #getComplexValue()
	 */
	public double getValue() {
		Object value = getValueAsObject();

		if (value == null) return Double.NaN;

		if (value instanceof Complex) {
			Complex c = (Complex) value;
			if (c.im() != 0.0) return Double.NaN;
			return c.re();
		}

		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}

		return Double.NaN;
	}


	/**
	 * Evaluates and returns the value of the expression as a complex number.
	 * 
	 * @return The calculated value of the expression as a complex number if no
	 *         errors occur. Returns null otherwise.
	 */
	public Complex getComplexValue() {
		Object value = getValueAsObject();

		if (value == null) {
			return null;
		} else if (value instanceof Complex) {
			return (Complex) value;
		} else if (value instanceof Number) {
			return new Complex(((Number) value).doubleValue(), 0);
		} else {
			return null;
		}
	}



	/**
	 * Evaluates and returns the value of the expression as an object. The
	 * EvaluatorVisitor member ev is used to do the evaluation procedure. This
	 * method is useful when the type of the value is unknown, or not important.
	 * 
	 * @return The calculated value of the expression if no errors occur.
	 *         Returns null otherwise.
	 */
	public Object getValueAsObject() {
		Object result;

		// fail by returning null if no topNode is available
		if (topNode == null) return null;

		// evaluate the expression
		try {
			result = ev.getValue(topNode, symTab);
		} catch (ParseException e) {
			if (debug) System.out.println(e);
			errorList.addElement(e.getMessage());
			return null;
		} catch (RuntimeException e) {
			if (debug) System.out.println(e);
			errorList.addElement(e.getMessage());
			return null;
		}
		return result;
	}

	/**
	 * Returns true if an error occurred during the most recent action (parsing
	 * or evaluation).
	 * 
	 * @return Returns <code>true</code> if an error occurred during the most
	 *         recent action (parsing or evaluation).
	 */
	public boolean hasError() {
		return !errorList.isEmpty();
	}

	/**
	 * Reports information on the errors that occurred during the most recent
	 * action.
	 * 
	 * @return A string containing information on the errors, each separated by
	 *         a newline character; null if no error has occurred
	 */
	public String getErrorInfo() {
		if (hasError()) {
			String str = "";

			// iterate through all errors and add them to the return string
			for (int i = 0; i < errorList.size(); i++) {
				str += errorList.elementAt(i) + "\n";
			}
			return str;
		}
		return null;
	}

	/**
	 * Clear error list.
	 */
	public void clearErrors() {
		if (hasError()) {
			errorList.clear();
		}
	}

	/**
	 * Returns the top node of the expression tree. Because all nodes are
	 * pointed to either directly or indirectly, the entire expression tree can
	 * be accessed through this node. It may be used to manipulate the
	 * expression, and subsequently evaluate it manually.
	 * 
	 * @return The top node of the expression tree
	 */
	public Node getTopNode() {
		return topNode;
	}

	/**
	 * Returns the symbol table (the list of all variables that the parser
	 * recognizes).
	 * 
	 * @return The symbol table
	 */
	public SymbolTable getSymbolTable() {
		return symTab;
	}

	/**
	 * Returns the function table (the list of all functions that the parser
	 * recognizes).
	 * 
	 * @return The function table
	 */
	public FunctionTable getFunctionTable() {
		return funTab;
	}

	/**
	 * Returns the EvaluatorVisitor
	 * 
	 * @return the EvaluatorVisitor.
	 */
	public EvaluatorVisitor getEvaluatorVisitor() {
		return ev;
	}

	/**
	 * Returns the number factory.
	 * 
	 * @return the NumberFactory used by this JEP instance.
	 */
	public NumberFactory getNumberFactory() {
		return numberFactory;
	}

	/**
	 * Returns the operator set.
	 * 
	 * @return the OperatorSet used by this JEP instance.
	 * @since 2.3.0 alpha
	 */
	public OperatorSet getOperatorSet() {
		return opSet;
	}

	/**
	 * Returns the parse object.
	 * 
	 * @return the Parse used by this JEP.
	 * @since 2.3.0 beta 1
	 */
	public Parser getParser() {
		return parser;
	}
}
