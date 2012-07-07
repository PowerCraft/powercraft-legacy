package net.minecraft.src;


import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import weasel.Test;


/**
 * PowerCraft's core module<br>
 * This module is an integral part of PowerCraft.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class mod_PCcore extends PC_Module implements PC_IActivatorListener {

	/** the mod instance */
	public static mod_PCcore instance;

	/**
	 * Mod version, shared by other modules.<br>
	 * Since this is static final, it is replaced by it's value during
	 * compilation, thus all modules keep their compile-time version and aren't
	 * affected by current core version.
	 */
	public static final String VERSION = "3.4.1";

	/** Location of the file with updates */
	public static final String updateInfoPath = "http://dl.dropbox.com/u/64454818/POWERCRAFT_DATA/info.xml"; // "http://bit.ly/Ld7sOI";
	/** Location of latest language pack file */
	public static final String updateLangPath = "http://dl.dropbox.com/u/64454818/POWERCRAFT_DATA/lang.zip"; // "http://bit.ly/Ld7y8S";

	/**
	 * Directory with settings. /something/something<br>
	 * Note that All image files are inside jar (virtually) in
	 * /PowerCraft/module_name_lowercase/*.png
	 */
	public static final String cfgdir = "/PowerCraft";

	@Override
	public String getVersion() {
		return mod_PCcore.VERSION;
	}

	/**
	 * Get images directory, ending with slash
	 * 
	 * @return the directory
	 */
	public static String getImgDir() {
		return "/PowerCraft/core/";
	}

	/**
	 * Get terrain file path (png)
	 * 
	 * @return the file path
	 */
	public static String getTerrainFile() {
		return getImgDir() + "tiles.png";
	}

	// CORE must override this, others don't.
	@Override
	public String getPriorities() {
		return "before:*";
	}

	@Override
	public String getModuleName() {
		return "CORE";
	}

	// Add fuels.
	@Override
	public int addFuel(int i, int j) {
		return (i == powerDust.shiftedIndex) ? 2200 : 0;
	}

	// *** BLOCKS & ITEMS ***

	/** gate crafting tool */
	public static Item craftingTool;

	/** Activation Crystal item */
	public static Item activator;


	/** Power Dust item (fuel) */
	public static Item powerDust;

	/** Ore Sniffer item */
	public static PCco_ItemOreSniffer oreSniffer;

	/** Power Crystal block */
	public static Block powerCrystal;




	/* Property keys */

	/* settings for the crafting tool */

	/** Enable update checking. */
	private boolean optUpdateNotify;
	/** Enable language pack updating */
	private boolean optUpdateLangpack;
	/** Enable sounds and loud things. */
	public static boolean soundsEnabled;
	/** Last update version marked as "ignore this". */
	public static String update_last_ignored_version;
	/** Version of current language pack */
	public static String current_lang_version;

	// property keys
	/** Key used to build in reversed direction */
	public static final String pk_keyReverse = "global.key.reverse_placing";

	private static final String pk_logEnabled = "global.logger.enabled";
	private static final String pk_logToStdout = "global.logger.toStdout";

	// recipe options
	private static final String pk_optRecRecyclation = "opt.new_recipes.recyclation";
	private static final String pk_optRecSpawner = "opt.new_recipes.spawner";
	private static final String pk_optRecEasyCrystals = "opt.cheat.easy_crystals";

	private static final String pk_idCraftingTool = "id.item.crafting_tool";
	private static final String pk_idActivator = "id.item.activation_crystal";
	private static final String pk_idPowerDust = "id.item.power_dust";
	private static final String pk_idCrystal = "id.block.power_crystal";
	private static final String pk_idSniffer = "id.item.ore_sniffer";

	private static final String pk_brightCrystal = "brightness.power_crystal";

	private static final String pk_optCraftCheating = "opt.crafting_tool.cheat_in_survival";


	private static final String pk_genCrystalsInChunk = "opt.worldgen.crystals.in_chunk";
	private static final String pk_genCrystalsDepositMaxCount = "opt.worldgen.crystals.deposit_max_size";
	private static final String pk_genCrystalsMinY = "opt.worldgen.crystals.min_y";
	private static final String pk_genCrystalsMaxY = "opt.worldgen.crystals.max_y";
	private static final String pk_genCrystal_gen = "opt.worldgen.crystals.enabled";

	private static final String pk_optUpdateNotify = "global.update.notifications";
	private static final String pk_optUpdateTranslations = "global.update.translations.autoDownload";
	private static final String pk_optMuteSound = "global.disableSounds";
	private static final String pk_optSoundCrystal = "opt.power_crystal.soundEnabled";

	/** last version marked as "do not show again" */
	public static final String pk_cfgUpdateIgnored = "cfg.updateVersionMarkedAsIgnored";

	/** current langpack version */
	public static final String pk_cfgCurrentLangVersion = "cfg.currentLangVersion";


	private static class PC_ErrorHandler implements Thread.UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread t, Throwable e) {

			PC_Logger.throwing(e.getClass().getName(), "Uncaught Exception", e);

			PC_Utils.mc().onMinecraftCrash(new UnexpectedThrowable("Uncaught Exception\n" + e.getMessage(), e));

		}

	}


	@Override
	public void preInit() {

		// Weasel testing
		Test test = new Test();
		test.run();


		Thread.setDefaultUncaughtExceptionHandler(new PC_ErrorHandler());

		instance = this;

	}

	@Override
	public void initProperties(PC_PropertyManager conf) {

		conf.renameKey("global.checkUpdates", pk_optUpdateNotify);

		conf.putKey(pk_keyReverse, Keyboard.KEY_LCONTROL, "Keyboard key used to place blocks in reversed orientation");
		conf.putBoolean(pk_optRecRecyclation, true, "Add new recypes allowing easy material recyclation");
		conf.putBoolean(pk_optRecSpawner, true, "Make spawners craftable of iron and mossy cobble");
		conf.putItem(pk_idCraftingTool, 19003);

		conf.putItem(pk_idActivator, 19001);
		conf.putItem(pk_idPowerDust, 19002);
		conf.putItem(pk_idSniffer, 19004);

		conf.putBlock(pk_idCrystal, 232);

		conf.putBoolean(pk_genCrystal_gen, true, "Generate Power Crystals in the world?");
		conf.putInteger(pk_brightCrystal, 15, "Power Crystal block brightness, scale 0-15.");

		conf.putBoolean(pk_optRecEasyCrystals, false, "Get power crystals by smelting diamonds,\nchange crystals color by crafting.");
		conf.putInteger(pk_genCrystalsInChunk, 3, "Number of deposits in each 16x16 chunk.");
		conf.putInteger(pk_genCrystalsDepositMaxCount, 4, "Highest crystal count in one deposit");
		conf.putInteger(pk_genCrystalsMinY, 5, "Min Y coordinate of crystal deposits.");
		conf.putInteger(pk_genCrystalsMaxY, 15, "Max Y coordinate of crystal deposits.");
		conf.putBoolean(pk_optUpdateNotify, true, "Enable notifications about PowerCraft updates.");
		conf.putBoolean(pk_optUpdateTranslations, true, "Enable automatic language files updating.");
		conf.putBoolean(pk_optMuteSound, false, "Disable all sounds and breaking animations with sounds.");
		conf.putBoolean(pk_optSoundCrystal, true, "Enable \"jingle\" sounds made by PowerCrystals.");

		conf.putBoolean(pk_optCraftCheating, false, "Makes the Crafting Tool work like TMI in Survival,\ngiving you everything with no resources consumed.");

		conf.putBoolean(pk_logEnabled, true, "Enable logging. Logs errors, events and debug info.");
		conf.putBoolean(pk_logToStdout, false, "Send log also to stdout (terminal screen).");

		conf.putString(pk_cfgUpdateIgnored, getVersion());
		conf.putString(pk_cfgCurrentLangVersion, "0");

		conf.apply();

		PCco_SlotDirectCrafting.survivalCheating = conf.flag(pk_optCraftCheating);
		optUpdateNotify = conf.flag(pk_optUpdateNotify);
		optUpdateLangpack = conf.flag(pk_optUpdateTranslations);
		soundsEnabled = !conf.flag(pk_optMuteSound);
		PCco_BlockPowerCrystal.makeSound = conf.flag(pk_optSoundCrystal);
		update_last_ignored_version = conf.string(pk_cfgUpdateIgnored);
		current_lang_version = conf.string(pk_cfgCurrentLangVersion);

		PC_Logger.setPrintToStdout(conf.flag(pk_logToStdout));
		PC_Logger.enableLogging(conf.flag(pk_logEnabled));
	}

	@Override
	public void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {}

	@Override
	public void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {}

	@Override
	public void registerBlockRenderers() {
		PC_Renderer.swapTerrainRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PC_Renderer.rotatedBoxRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PCco_Renderer.crystalRenderer = ModLoader.getUniqueBlockModelID(this, true);
	}

	@Override
	public void registerBlocks(List<Block> list) {

		// @formatter:off
		
		powerCrystal = new PCco_BlockPowerCrystal(cfg().num(pk_idCrystal), 49)
				.setHardness(0.5F)
				.setResistance(0.5F)
				.setBlockName("PCcoPowerCrystal")
				.setStepSound(Block.soundGlassFootstep)
				.setLightValue(cfg().num(pk_brightCrystal) * 0.0625F);
		
		// @formatter:on


		list.add(powerCrystal);
	}

	@Override
	public void registerItems() {

		// @formatter:off
		
		craftingTool = new PCco_ItemCraftingTool(cfg().num(pk_idCraftingTool))
				.setItemName("PCcoGateCrafter");
		
		activator = new PCco_ItemActivator(cfg().num(pk_idActivator))
				.setIconIndex(37)
				.setItemName("PCcoActivatorItem");

		powerDust = new PCco_ItemPowerDust(cfg().num(pk_idPowerDust))
				.setIconCoord(13, 9)
				.setItemName("PCcoPowerDust");

		oreSniffer = (PCco_ItemOreSniffer) new PCco_ItemOreSniffer(cfg().num(pk_idSniffer))
				.setIconIndex(37)
				.setItemName("PCcoOreSnifferItem");
		
		removeBlockItem(powerCrystal.blockID);
		setBlockItem(powerCrystal.blockID, new PCco_ItemBlockPowerCrystal(powerCrystal.blockID - 256));
		
		removeBlockItem(Block.lockedChest.blockID);
		Block.blocksList[Block.lockedChest.blockID] = null;
		Block.blocksList[Block.lockedChest.blockID] = new PCco_BlockLockedChestReplacement(Block.lockedChest.blockID);
		removeBlockItem(Block.lockedChest.blockID);
		setBlockItem(Block.lockedChest.blockID, new PCco_ItemBlockLockedChestReplacement(Block.lockedChest.blockID - 256));
		
		// @formatter:on
	}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getTerrainFile());
		list.add(getImgDir() + "gres/dialog.png");
		list.add(getImgDir() + "gres/widgets.png");
		list.add(getImgDir() + "gres/frame.png");
		list.add(getImgDir() + "gres/button.png");
		list.add(getImgDir() + "graphics.png");
	}

	@Override
	public void setTextures() {
		craftingTool.setIconIndex(ModLoader.addOverride("/gui/items.png", getImgDir() + "item_craftingtool.png"));
		activator.setIconIndex(ModLoader.addOverride("/gui/items.png", getImgDir() + "item_activator.png"));
		oreSniffer.setIconIndex(ModLoader.addOverride("/gui/items.png", getImgDir() + "item_sniffer.png"));
	}

	@Override
	public void setNames(Map<Object, String> map) {

		map.put(craftingTool, "PowerCraft's Crafting Tool");

		map.put(activator, "Activation Crystal");
		map.put(powerCrystal, "Power Crystal");
		map.put(powerDust, "Power Dust");
		map.put(oreSniffer, "Ore Sniffer");

		map.put("tile.PCcoPowerCrystal.color0.name", "\u03b1 Power Crystal");
		map.put("tile.PCcoPowerCrystal.color1.name", "\u03b2 Power Crystal");
		map.put("tile.PCcoPowerCrystal.color2.name", "\u03b3 Power Crystal");
		map.put("tile.PCcoPowerCrystal.color3.name", "\u03b4 Power Crystal");
		map.put("tile.PCcoPowerCrystal.color4.name", "\u03d1 Power Crystal");
		map.put("tile.PCcoPowerCrystal.color5.name", "\u03b6 Power Crystal");
		map.put("tile.PCcoPowerCrystal.color6.name", "\u03be Power Crystal");
		map.put("tile.PCcoPowerCrystal.color7.name", "\u03d7 Power Crystal");
		map.put("tile.PCcoContainerChest.name", "Chest with Contents");
		map.put("pc.gui.craftingTool.title", "PowerCraft's Crafting Tool");
		map.put("pc.gui.spawnerEditor.enableDangerous", "Enable dangerous");
		map.put("pc.gui.ok", "OK");
		map.put("pc.gui.cancel", "Cancel");

		map.put("pc.sniffer.sniffing", "Sniffing ores...");
		map.put("pc.sniffer.away", "(far away)");
		map.put("pc.sniffer.desc", "Portable radar device");
		map.put("pc.sniffer.distance", "Sniffing depth (blocks):");


		map.put("pc.gui.update.title", "Mod Update Notification");
		map.put("pc.gui.update.newVersionAvailable", "Update available!");
		map.put("pc.gui.update.readMore", "Read more...");
		map.put("pc.gui.update.version", "Using %1$s (%2$s), Available %3$s (%4$s)");
		map.put("pc.gui.update.doNotShowAgain", "Don't show again");


		map.put("pc.block.pickedUp", "Picked-up %s");
		map.put("pc.block.pickedUp.special", "Special Block");

	}

	@Override
	public void addRecipes() {
		//@formatter:off
		
		ModLoader.addRecipe(
				new ItemStack(craftingTool),
				new Object[] { " r ", "rIr", " r ",
					'r', Item.redstone, 'I', Block.blockSteel });
		//ModLoader.addShapelessRecipe(new ItemStack(craftingTool), new Object[] { Block.dirt });
		//ModLoader.addShapelessRecipe(new ItemStack(Block.wood,32), new Object[] { Block.dirt,Block.dirt });
		
		// @formatter:on

		// new recipes
		if (cfg().getBoolean(pk_optRecRecyclation)) {
			ModLoader.addShapelessRecipe(new ItemStack(Block.sand, 4), new Object[] { new ItemStack(Block.sandStone, 1, -1) });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 6), new Object[] { Item.doorWood });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 8), new Object[] { Block.chest });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 4), new Object[] { Block.workbench });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 2), new Object[] { Block.pressurePlatePlanks });
			ModLoader.addShapelessRecipe(new ItemStack(Block.stone, 2), new Object[] { Block.pressurePlateStone });
			ModLoader.addShapelessRecipe(new ItemStack(Block.stone, 2), new Object[] { Block.button });
			ModLoader.addShapelessRecipe(new ItemStack(Item.stick, 3), new Object[] { Block.fence });
			ModLoader.addShapelessRecipe(new ItemStack(Item.stick, 2), new Object[] { Block.ladder });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 6), new Object[] { Item.sign });
			ModLoader.addShapelessRecipe(new ItemStack(Item.ingotIron, 6), new Object[] { Item.doorSteel });
			ModLoader.addShapelessRecipe(new ItemStack(Block.cobblestone, 8), new Object[] { Block.stoneOvenIdle });
			ModLoader.addShapelessRecipe(new ItemStack(Item.ingotIron, 5), new Object[] { Item.minecartEmpty });
			ModLoader.addShapelessRecipe(new ItemStack(Item.ingotIron, 3), new Object[] { Item.bucketEmpty });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 5), new Object[] { Item.boat });
			ModLoader.addShapelessRecipe(new ItemStack(Item.stick, 3), new Object[] { Block.fence });
			ModLoader.addShapelessRecipe(new ItemStack(Item.stick, 8), new Object[] { Block.fenceGate });
			ModLoader.addShapelessRecipe(new ItemStack(Item.stick, 7), new Object[] { Block.ladder, Block.ladder });
			ModLoader.addShapelessRecipe(new ItemStack(Block.stone), new Object[] { new ItemStack(Block.stoneBrick, 1, -1) });
			ModLoader.addShapelessRecipe(new ItemStack(Item.ingotIron, 7), new Object[] { new ItemStack(Item.cauldron, 1, -1) });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 3), new Object[] { Block.trapdoor });
			ModLoader.addShapelessRecipe(new ItemStack(Block.planks, 1), new Object[] { Item.stick, Item.stick });
		}

		// @formatter:off
		
		// crafting recipe for spawner
		if (cfg().getBoolean(pk_optRecSpawner)) {
			ModLoader.addRecipe(
					new ItemStack(Block.mobSpawner, 1),
					new Object[] { "SIS", "I I", "SIS", 'I', Item.ingotIron, 'S', Block.cobblestoneMossy });
			
		}
		
		
		// crystal changing color, crystal from smelted diamonds
		if (cfg().getBoolean(pk_optRecEasyCrystals)) {
			ModLoader.addSmelting(Item.diamond.shiftedIndex, new ItemStack(powerCrystal, 1));
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal, 1, 1),
					new Object[] { new ItemStack(powerCrystal, 1, 0) });
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal, 1, 2),
					new Object[] { new ItemStack(powerCrystal, 1, 1) });
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal, 1, 3),
					new Object[] { new ItemStack(powerCrystal, 1, 2) });
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal, 1, 4),
					new Object[] { new ItemStack(powerCrystal, 1, 3) });
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal, 1, 5),
					new Object[] { new ItemStack(powerCrystal, 1, 4) });
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal, 1, 6),
					new Object[] { new ItemStack(powerCrystal, 1, 5) });
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal, 1, 7),
					new Object[] { new ItemStack(powerCrystal, 1, 6) });
			ModLoader.addShapelessRecipe(
					new ItemStack(powerCrystal),
					new Object[] { new ItemStack(powerCrystal, 1, 7) });
			
			// two crystals of any color -> two portions of PowerDust
			ModLoader.addShapelessRecipe(
					new ItemStack(powerDust, 25, 0),
					new Object[] { new ItemStack(powerCrystal, 1, -1),	new ItemStack( Item.coal,1,-1) });
			
		} else {
			
			// Normal recipe for power dust
			ModLoader.addShapelessRecipe(new ItemStack(powerDust, 24, 0), new Object[] { new ItemStack(powerCrystal, 1, -1) });
		}
		
		// sniffer		
		ModLoader.addRecipe(
				new ItemStack(oreSniffer),
				new Object[] { " G ", "GCG", " G ",
					'C', new ItemStack(powerCrystal, 1, -1), 'G', Item.ingotGold });

		// activator
		ModLoader.addRecipe(
				new ItemStack(activator, 1),
				new Object[] { "C", "I",
					'C', new ItemStack(powerCrystal, 1, -1), 'I', Item.ingotIron });
		
		//@formatter:on
	}

	@Override
	public void postInit() {

		// @formatter:off
		
		// load crops and generate default file
		PC_CropHarvestingManager.loadCrops();	
		//load trees from XMl files.
		PC_TreeHarvestingManager.loadTrees();	
		
		// register activator listener (for spawner GUI)
		PC_Module.registerActivatorListener(this);

		// set invedit groups and exceptions
		PC_InveditManager.setDamageRange(powerCrystal.blockID, 0, 7);

		PC_InveditManager.setItemCategory(powerCrystal.blockID, "Power crystals");
		PC_InveditManager.setItemCategory(powerDust.shiftedIndex, "Power crystals");

		PC_InveditManager.setItemCategory(craftingTool.shiftedIndex, "Handheld devices");
		PC_InveditManager.setItemCategory(activator.shiftedIndex, "Handheld devices");
		
		PC_InveditManager.setItemCategory(oreSniffer.shiftedIndex, "Handheld devices");



		// adding stuff to crafting tool
		ItemStack[] crystals = new ItemStack[9];
		for (int i = 0; i < 8; i++) {
			crystals[i] = new ItemStack(powerCrystal, 1, i);
		}
		crystals[8] = new ItemStack(powerDust);
		addStacksToCraftingTool(PC_CraftingToolGroup.DECORATIVE, crystals);

		
		addStacksToCraftingTool(
				PC_CraftingToolGroup.HANDHELD,
				new ItemStack(activator),
				new ItemStack(oreSniffer),
				new ItemStack(craftingTool)
			);
		
		addStacksToCraftingTool(
				PC_CraftingToolGroup.VANILLA_RELATED,
				new ItemStack(Item.redstone),
				new ItemStack(Item.redstoneRepeater),
				new ItemStack(Block.torchRedstoneActive),
				new ItemStack(Block.lever),
				new ItemStack(Block.button),
				new ItemStack(Block.pressurePlateStone),
				new ItemStack(Block.pressurePlatePlanks),
				new ItemStack(Block.redstoneLampIdle),
				new ItemStack(Block.pistonBase),
				new ItemStack(Block.pistonStickyBase),
				new ItemStack(Block.mobSpawner),
				new ItemStack(Block.music),
				new ItemStack(Block.jukebox),
				new ItemStack(Block.dispenser),
				new ItemStack(Block.stoneOvenIdle),
				new ItemStack(Block.chest),
				new ItemStack(Block.workbench),
				new ItemStack(Block.glowStone),
				new ItemStack(Item.lightStoneDust),
				new ItemStack(Block.tnt),
				new ItemStack(Item.fireballCharge),
				new ItemStack(Block.rail),
				new ItemStack(Block.railDetector),
				new ItemStack(Block.railPowered),
				new ItemStack(Item.minecartEmpty),
				new ItemStack(Item.minecartCrate),
				new ItemStack(Item.minecartPowered),
				new ItemStack(Block.ladder),
				new ItemStack(Block.fenceIron),
				new ItemStack(Block.trapdoor),
				new ItemStack(Item.doorSteel),
				new ItemStack(Item.doorWood),
				new ItemStack(Item.cauldron),
				new ItemStack(Item.brewingStand),
				new ItemStack(Item.bucketEmpty),
				new ItemStack(Block.enchantmentTable),
				new ItemStack(Item.glassBottle),
				new ItemStack(Item.blazePowder),
				new ItemStack(Item.magmaCream),
				new ItemStack(Item.netherStalkSeeds),
				new ItemStack(Item.sugar),
				new ItemStack(Item.gunpowder),
				new ItemStack(Item.spiderEye),
				new ItemStack(Item.fermentedSpiderEye),
				new ItemStack(Item.eyeOfEnder),
				new ItemStack(Item.ghastTear),
				new ItemStack(Item.speckledMelon),
				new ItemStack(Item.goldNugget),
				new ItemStack(Item.dyePowder,1,15),
				new ItemStack(Block.mushroomBrown),
				new ItemStack(Block.mushroomRed),
				new ItemStack(Block.sapling,1,0),
				new ItemStack(Block.sapling,1,1),
				new ItemStack(Block.sapling,1,2),
				new ItemStack(Block.sapling,1,3),
				new ItemStack(Item.compass),
				new ItemStack(Item.pocketSundial),
				new ItemStack(Item.fishingRod),
				new ItemStack(Item.shears),
				new ItemStack(Block.torchWood),
				new ItemStack(Item.sign),
				new ItemStack(Block.fence),
				new ItemStack(Block.fenceGate),
				new ItemStack(Item.stick),
				new ItemStack(Block.blockDiamond),
				new ItemStack(Block.blockGold),
				new ItemStack(Block.blockSteel),
				new ItemStack(Block.blockLapis),
				new ItemStack(Item.diamond),
				new ItemStack(Item.ingotGold),
				new ItemStack(Item.ingotIron),
				new ItemStack(Item.dyePowder,PC_Color.dye.BLUE.meta),
				new ItemStack(Block.planks),
				new ItemStack(Block.obsidian),
				new ItemStack(Block.stone,1,0),
				new ItemStack(Block.cobblestone),
				new ItemStack(Block.cobblestoneMossy),
				new ItemStack(Block.stoneBrick),
				new ItemStack(Block.blockClay),
				new ItemStack(Block.brick),
				new ItemStack(Block.sand),
				new ItemStack(Block.sandStone)
			);
		
		// @formatter:on

		if (optUpdateNotify) {
			ModLoader.setInGameHook(this, true, false);
		}

		if (optUpdateLangpack) {
			(new PCco_ThreadCheckUpdates()).start();
		}

	}


	// *** HANDLING RENDERERS ***

	@Override
	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess blockAccess, int i, int j, int k, Block block, int rtype) {
		boolean flag = false;
		flag |= PC_Renderer.renderBlockByType(renderblocks, blockAccess, i, j, k, block, rtype);
		flag |= PCco_Renderer.renderBlockByType(renderblocks, blockAccess, i, j, k, block, rtype);
		return flag;
	}

	@Override
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int rtype) {
		PC_Renderer.renderInvBlockByType(renderblocks, block, i, rtype);
		PCco_Renderer.renderInvBlockByType(renderblocks, block, i, rtype);
	}


	// Generate the power crystals if enabled.
	@Override
	public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		if (!cfg().flag(pk_genCrystal_gen)) {
			return;
		}

		int posX;
		int posY;
		int posZ;

		for (int q = 0; q < cfg().num(pk_genCrystalsInChunk); q++) {
			int maxBlocks = random.nextInt(MathHelper.clamp_int(cfg().num(pk_genCrystalsDepositMaxCount) - 1, 1, 10)) + 2;

			posX = chunkX + random.nextInt(16);
			posY = MathHelper.clamp_int(random.nextInt(MathHelper.clamp_int(cfg().num(pk_genCrystalsMaxY) - cfg().num(pk_genCrystalsMinY), 1, 255)) + cfg().num(pk_genCrystalsMinY), 1, 255);

			posZ = chunkZ + random.nextInt(16);

			PC_Logger.finest("Generating PowerCrystals deposit of size " + maxBlocks + " at coords " + new PC_CoordI(posX, posY, posZ));

			new PC_WorldGenMinableMetadata(powerCrystal.blockID, random.nextInt(8), maxBlocks).generate(world, random, posX, posY, posZ);
		}
	}

	@Override
	public boolean onActivatorUsedOnBlock(ItemStack stack, EntityPlayer player, World world, PC_CoordI pos) {

		if (pos.getId(world) == Block.mobSpawner.blockID) {

			PC_Utils.openGres(player, new PCco_GuiSpawnerEditor((TileEntityMobSpawner) pos.getTileEntity(world)));

			stack.damageItem(1, player);

			return true;
		}

		if (pos.getId(world) == Block.signPost.blockID || pos.getId(world) == Block.signWall.blockID) {

			TileEntitySign tileentitysign = (TileEntitySign) world.getBlockTileEntity(pos.x, pos.y, pos.z);

			if (tileentitysign != null) {
				player.displayGUIEditSign(tileentitysign);
				stack.damageItem(1, player);
			}

			return true;
		}


		int dir = ((MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

//		if (PC_Utils.isPlacingReversed()) {
//			dir = PC_Utils.reverseSide(dir);
//		}



		for (int i = 0; i < 3; i++) {

			PC_CoordI chest = pos.offset(-Direction.offsetX[dir], i, -Direction.offsetZ[dir]);
			if (i == 2) {
				//try direct up.
				chest = pos.offset(0, 1, 0);
			}

			if (chest.getId(world) == Block.chest.blockID && chest.offset(0, -1, 0).getId(world) == Block.blockSteel.blockID) {
				break;
			}

			ItemStack stackchest = PCco_ItemBlockLockedChestReplacement.extractAndRemoveChest(world, chest);
			if (stackchest != null) {
				float f = 0.7F;
				double d = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
				double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
				double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
				EntityItem entityitem = new EntityItem(world, chest.x + d, chest.y + d1, chest.z + d2, stackchest);
				entityitem.delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityitem);
				stack.damageItem(1, player);
				return true;
			}
		}

		return false;
	}




	/** Flag that mod update is available */
	public static boolean updateAvailable = false;
	/** PowerCraft version in latest update */
	public static String updateModVersion = "";
	/** Minecraft version required by the latest update */
	public static String updateMcVersion = "";
	/** Version of latest available translation pack */
	public static String updateLangVersion = "";
	/** Text with information about the latest update */
	public static String updateText = "";

	/**
	 * Called when PCco_ThreadCheckUpdates terminates.
	 * 
	 * @param file_contents string with the downloaded file contents
	 */
	public static void onUpdateInfoDownloaded(String file_contents) {

		PC_Logger.fine("\n\nUpdate information received from server.");

		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new ByteArrayInputStream(file_contents.getBytes("UTF-8")));

			doc.getDocumentElement().normalize();

			// latest.
			do {
				NodeList latest_l = doc.getElementsByTagName("update");
				if (latest_l.getLength() != 1) {
					break;
				}

				Element latest = (Element) latest_l.item(0);

				updateModVersion = latest.getAttribute("modVersion");

				if (updateModVersion == null || updateModVersion.equals("")) {
					break;
				}

				updateMcVersion = latest.getAttribute("mcVersion");

				if (updateMcVersion == null || updateMcVersion.equals("")) {
					break;
				}

				updateLangVersion = latest.getAttribute("langVersion");
				String updateLangModVersion = latest.getAttribute("langModVersion");

				if (updateLangVersion != null && !updateLangVersion.equals("")) {
					if (!updateLangVersion.equals(current_lang_version) && updateLangModVersion.equals(instance.getVersion())) {
						(new PCco_ThreadDownloadTranslations()).start();
					}
				}

				updateText = latest.getTextContent();

				if (updateText == null || updateText.equals("")) {
					break;
				}

				updateText = updateText.trim();

				updateAvailable = !updateModVersion.equals(instance.getVersion()) && !updateModVersion.equals(update_last_ignored_version);

				PC_Logger.finer("* Update mod version = " + updateModVersion);
				PC_Logger.finer("* Update MC version = " + updateMcVersion);
				PC_Logger.finer("* Latest language pack version = " + updateLangVersion);
				PC_Logger.finer("* Update info = " + updateText);

			} while (false);



		} catch (SAXParseException err) {
			PC_Logger.severe("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			PC_Logger.severe(" " + err.getMessage());
		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	/**
	 * Method called translation files were downloaded and updated. Reloads the
	 * translations and forces modloader to update loaded names.
	 */
	public static void onTranslationsUpdated() {

		PC_Logger.fine("Loading translations from updated files.\n");

		for (PC_Module module : PC_Module.modules.values()) {
			PC_Logger.finer("Loading translations for module " + module.getModuleName());
			if (module.lang != null) {
				module.lang.loadTranstalions();
			}
			PC_Logger.finer("\n");
		}

		PC_Logger.fine("All translations loaded.\n");

		PC_Logger.fine("Saving Language Pack version number to property file CORE.cfg");

		PC_PropertyManager cfg = mod_PCcore.instance.cfg();

		cfg.enableValidation(false);
		cfg.cfgSilent(true);

		cfg.setValue(mod_PCcore.pk_cfgCurrentLangVersion, mod_PCcore.updateLangVersion);
		cfg.apply();

		cfg.enableValidation(true);
		cfg.cfgSilent(false);


		PC_Logger.fine("Forcing ModLoader to update Minecraft's list of translations.");
		try {
			ModLoader.setPrivateValue(ModLoader.class, null, "langPack", null);
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.throwing("mod_PCcore", "onTranslationsUpdated()", e);
		}

	}

	private static boolean updateAlreadyShown = false;

	private int inGameTickCounter = 0;

	@Override
	public boolean onTickInGame(float f, Minecraft minecraft) {
		if (!updateAlreadyShown && updateAvailable && optUpdateNotify) {
			if (++inGameTickCounter > 20) {
				updateAlreadyShown = true;
				PC_Utils.openGres(mc.thePlayer, new PCco_GuiUpdateNotification());
				PC_Logger.fine("Openning UPDATE NOTIFICATION screen.");
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

}
