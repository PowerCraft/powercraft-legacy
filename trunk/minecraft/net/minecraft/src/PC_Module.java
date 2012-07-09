package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;


/**
 * PowerCraft's module. Extend this instead of BaseMod by your mod_something
 * classes.<br>
 * <br>
 * <b>*** About PowerCraft API ***</b><br>
 * PowerCraft has various useful classes, prefixed with <i>PC_</i>. There are
 * few Utils classes, logger, new Gui elements and commonly shared renderer. One
 * of the most useful things are also the Coordinate and Struct classes, which
 * make your methods much easier to use.<br>
 * <br>
 * mod_PCcore is an integral part of PowerCraft.<br>
 * Classes prefixed with <i>PCco_</i> are parts of core module, but not this
 * API. <br>
 * Some useful methods are here, on PC_Module, so you can use them directly when
 * initializing your modules. <br>
 * <br>
 * <b>Most important and useful API classes:</b>
 * <ul>
 * <li>PC_Module - abstract class to be used instead of BaseMod
 * <li>PC_PropertyManager - property file reader; reads properties, validates
 * entries etc.
 * <li>PC_Renderer - PowerCraft's common renderer (few most important rendering
 * methods)
 * <li>PC_CropManager - the crops API
 * <li>PC_InveditManager - TMI & NEI compatibility manager
 * <li>PC_Logger - static PowerCraft's logger, stores output into a file
 * <li>PC_Utils - common utilities
 * <li>PC_InvUtils - tools for inventory manipulation
 * <li>PC_KeyUtils - tools for key filtering, used mainly in GUIs
 * <li>PC_BlockUtils - lets you get information about block type at coord in
 * world
 * <li>PC_Color - universal color class
 * <li>PC_CoordI - integer coordinate 3D (there is also float and double
 * version)
 * </ul>
 * <br>
 * <b>Most important interfaces:</b>
 * <ul>
 * <li>PC_IActivatorListener - handler interface for activator device. To be
 * implemented by main module class.
 * <li>PC_IBlockType - block type interface, all PC blocks must implement it
 * <li>PC_IFullnessReporter - interface for non-standard IInventory block, used
 * to get full and empty flags
 * <li>PC_IInvTextures - block with differently mapped textures when in
 * inventory than when in the world
 * <li>PC_IRotatedBox - block whose top face rotates based on metadata
 * information (gates, conveyors)
 * <li>PC_ISelectiveInventory - inventory which can store only some blocks to
 * some slots; it lets you check where is what stored by conveyors.
 * <li>PC_ISwapTerrain - block with custom terrain texture
 * </ul>
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class PC_Module extends BaseMod {

	/** Minecraft instance */
	public static Minecraft mc;

	/** per-module config file */
	private PC_PropertyManager conf;

	/** Module translation manager */
	public PC_Lang lang;

	/**
	 * @return instance of configuration manager
	 */
	public PC_PropertyManager cfg() {
		return conf;
	}

	/** List of registered modules. */
	public static final Hashtable<String, PC_Module> modules = new Hashtable<String, PC_Module>();


	/**
	 * PowerCraft module.
	 */
	public PC_Module() {
		modules.put(getModuleName(), this);
	}


	/**
	 * Get unique module name. Example: "TRANSPORT"
	 * 
	 * @return the name
	 */
	public abstract String getModuleName();


	/**
	 * Check if module is loaded.
	 * 
	 * @param name Module name to check. Example: "TRANSPORT"
	 * @return is loaded
	 */
	public static final boolean isModuleLoaded(String name) {
		return modules.containsKey(name);
	}


	/**
	 * Get module instance.
	 * 
	 * @param name Name of module to get
	 * @return the module, or null if not loaded
	 */
	public static final PC_Module getModule(String name) {
		return modules.get(name);
	}


	@Override
	public abstract String getVersion();


	@Override
	public String getPriorities() {
		return "after:mod_PCcore";
	}

	@Override
	public void modsLoaded() {
		// send block entries to TMI when all mods are loaded.
		PC_InveditManager.sendToTMI();
	}

	/**
	 * Converts object (stack, item, block) to a translation identifier.
	 * 
	 * @param obj object
	 * @return name tag
	 */
	private String getNameTagForObject(Object obj) {

		String name = null;

		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof Item) {
			Item item = (Item) obj;

			if (item.getItemName() != null) {
				name = (new StringBuilder(String.valueOf(item.getItemName()))).append(".name").toString();
			}
		} else if (obj instanceof Block) {
			Block block = (Block) obj;

			if (block.getBlockName() != null) {
				name = (new StringBuilder(String.valueOf(block.getBlockName()))).append(".name").toString();
			}
		} else if (obj instanceof ItemStack) {
			ItemStack itemstack = (ItemStack) obj;
			String s3 = Item.itemsList[itemstack.itemID].getItemNameIS(itemstack);

			if (s3 != null) {
				name = (new StringBuilder(String.valueOf(s3))).append(".name").toString();
			}
		} else {
			PC_Logger.warning("Trying to add name to invalid object (not Block, Item or ItemStack)");
		}

		return name;
	}


	@Override
	public final void load() {

		try {
			if (mc == null) {
				mc = ModLoader.getMinecraftInstance();
				PC_Logger.fine("\nInitializing Minecraft instance.");
			}



			PC_Logger.fine("\n\nLoading module " + getModuleName());



			PC_Logger.finer("Calling pre-init hook...");
			preInit();


			PC_Logger.finer("Initializing properties...");
			conf = new PC_PropertyManager(mod_PCcore.cfgdir + "/" + getModuleName() + ".cfg", "PowerCraft " + getModuleName() + " module\nconfiguration file");
			initProperties(conf);



			PC_Logger.finer("Registering entities...");
			ArrayList<PC_Struct3<Class<? extends Entity>, String, Integer>> entities = new ArrayList<PC_Struct3<Class<? extends Entity>, String, Integer>>();

			registerEntities(entities);

			for (PC_Struct3<Class<? extends Entity>, String, Integer> entry : entities) {
				ModLoader.registerEntityID(entry.getA(), entry.getB(), entry.getC());
			}



			PC_Logger.finer("Registering tile entities...");
			ArrayList<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> tileentities = new ArrayList<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>>();

			registerTileEntities(tileentities);

			for (PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer> entry : tileentities) {
				if (entry.c == null) {
					ModLoader.registerTileEntity(entry.a, entry.b);
				} else {
					ModLoader.registerTileEntity(entry.a, entry.b, entry.c);
				}

			}



			PC_Logger.finer("Registering block model renderers...");
			registerBlockRenderers();



			PC_Logger.finer("Registering blocks...");
			ArrayList<Block> blocks = new ArrayList<Block>();

			registerBlocks(blocks);

			for (Block block : blocks) {
				ModLoader.registerBlock(block);
			}



			PC_Logger.finer("Registering items...");
			registerItems();




			PC_Logger.finer("Preloading textures...");
			ArrayList<String> textures = new ArrayList<String>();

			preloadTextures(textures);

			for (String texture : textures) {
				PC_Renderer.preloadTexture(texture);
			}



			PC_Logger.finer("Adding texture overrides...");
			setTextures();



			PC_Logger.finer("Adding default names...");
			
			generateTranslationFiles();



			PC_Logger.finer("Adding recipes...");
			addRecipes();



			PC_Logger.finer("Calling post-init hook...");
			postInit();


			PC_Logger.fine("Module loaded");

		} catch (RuntimeException e) {

			PC_Logger.severe("\nAn error occured, probably due to wrong IDs in your property files.");
			PC_Logger.severe("Read this log file carefully to locate the problem.\n\n");

			PC_Logger.throwing("PowerCraft module " + getModuleName() + " - method name: " + getName(), "load()", e);

			throw (e);
		}
	}



	// UTILS

	
	/**
	 * Read needed names from the addNames() and generate new en_US lang files in the lang folder.
	 */
	protected final void generateTranslationFiles() {
		HashMap<Object, String> names = new HashMap<Object, String>();
		HashMap<String, String> en_US = new HashMap<String, String>();

		setNames(names);

		for (Entry<Object, String> loc : names.entrySet()) {

			if (loc.getKey() == null || loc.getValue() == null) {
				PC_Logger.severe("Trying to use null when adding names.");
				continue;
			}

			String nametag = getNameTagForObject(loc.getKey());

			if (nametag == null) {
				PC_Logger.severe("Setting name to invalid object.");
				continue;
			}

			en_US.put(nametag, loc.getValue());

		}

		lang = new PC_Lang(getModuleName(), en_US);

		PC_Logger.finer("Generating default translation file (en_US) if not exists");
		lang.generateDefaultTranslationFile();
		PC_Logger.finer("Loading translations from " + mod_PCcore.cfgdir + "/lang");
		lang.loadTranstalions();
	}


	/**
	 * Add stacks with damage ranging from A to B into Crafting Tool.
	 * 
	 * @param group item type group
	 * @param id item ID
	 * @param damage1 starting damage
	 * @param damage2 ending damage
	 * @param stackSize items in each stack
	 */
	public static final void addStackRangeToCraftingTool(PC_CraftingToolGroup group, int id, int damage1, int damage2, int stackSize) {

		ArrayList<ItemStack> list = new ArrayList<ItemStack>();

		for (int dmg = damage1; dmg <= damage2; dmg++) {
			list.add(new ItemStack(id, stackSize, dmg));
		}

		PCco_CraftingToolManager.addStacks(group.index, list.toArray(new ItemStack[list.size()]));
	}

	/**
	 * Add given stacks into the crafting tool.
	 * 
	 * @param group item type group
	 * @param stacks array of stacks to add
	 */
	public static final void addStacksToCraftingTool(PC_CraftingToolGroup group, ItemStack... stacks) {
		PCco_CraftingToolManager.addStacks(group.index, stacks);
	}

	/**
	 * Connect custom listener to Activation Crystal from Core module.
	 * 
	 * @param listener implementation of the listener interface.
	 */
	public static final void registerActivatorListener(PC_IActivatorListener listener) {
		PCco_ItemActivator.registerListener(listener);
	}

	/**
	 * Remove existing block item from items list. This is needed to prevent
	 * generating "CONFLICT" error message
	 * 
	 * @param id block id
	 */
	public static final void removeBlockItem(int id) {
		Item.itemsList[id] = null;
	}

	/**
	 * Set alternate block item.<br>
	 * Call after "removeBlockItem(int id)"
	 * 
	 * @param id block id
	 * @param item item instance
	 */
	public static final void setBlockItem(int id, Item item) {
		Item.itemsList[id] = item;
	}


	// MODULE INITIALIZATION HOOKS

	/**
	 * Do something before the initialization begins
	 */
	public abstract void preInit();

	/**
	 * Initialize your property manager, add needed keys and apply.<br>
	 * Property manager is per-module, it's only yours, with your own property
	 * file.<br>
	 * You can also put some things from the property manager into static fields
	 * after calling "apply().<br>
	 * <br>
	 * <b>Look into some of the base modules for help how to use property
	 * manager.</b>
	 * 
	 * @param conf the initialized property manager for your module
	 */
	public abstract void initProperties(PC_PropertyManager conf);

	/**
	 * Register tile entities
	 * 
	 * @param list List of structures { YourEntity.class, name, index }<br>
	 *            Name must be an unique entity name, preferably prefixed with
	 *            "PC".<br>
	 *            Use ModLoader.getUniqueEntityID() to get index in singleplayer
	 *            modules.<br>
	 */
	public abstract void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list);

	/**
	 * Register tile entities
	 * 
	 * @param list List of structures { TileEntity.class, name,
	 *            TileEntitySpecialRenderer }<br>
	 *            Name must be an unique entity name, preferably prefixed with
	 *            "PC".<br>
	 *            Use null in place of renderer if you use block renderer.
	 */
	public abstract void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list);

	/**
	 * Register block model renderers.<br>
	 * Example:
	 * 
	 * <pre>
	 * PCmy_Renderer.fooRenderer = ModLoader.getUniqueBlockModelID(this, true);
	 * </pre>
	 * 
	 * Renderer ids initialized here are then to be handled using BaseMod's
	 * renderWorldBlock and renderInvBlock methods.
	 */
	public abstract void registerBlockRenderers();

	/**
	 * Initialize and register blocks you are adding.<br>
	 * Do not replace ItemBlocks here, do it in registerItems() call.
	 * 
	 * @param list List of blocks to register
	 */
	public abstract void registerBlocks(List<Block> list);

	/**
	 * Initialize items you are adding, replace ItemBlocks if needed.<br>
	 * Use something like:
	 * 
	 * <pre>
	 * replaceBlockItem(myBlock.blockID, new PCmy_MyItemBlock(myBlock.blockID - 256));
	 * </pre>
	 * 
	 * You can alternatively set item name, and do some metadata stuff in your
	 * custom ItemBlock.
	 */
	public abstract void registerItems();

	/**
	 * Preload custom texture files.<br>
	 * Important for entities and blocks.
	 * 
	 * @param list List of texture file names to preload. PNG!
	 */
	public abstract void preloadTextures(List<String> list);

	/**
	 * Register item and block textures, add overrides.<br>
	 * use block.blockIndexInTexture = number, or item.setIconIndex(number).<br>
	 * Use ModLoader.addOverride() to get texture from small file for items.<br>
	 * Blocks should use PC_ISwapTerrain and PC_Renderer.swapTerrainRenderer.
	 */
	public abstract void setTextures();

	/**
	 * Add names and localizations for your blocks and items.
	 * 
	 * @param map map of { Block/Item/String (localization) : String name }
	 */
	public abstract void setNames(Map<Object, String> map);

	/**
	 * Add recipes.<br>
	 * Use standard ModLoader's methods for this - addRecipe and
	 * addShapelessRecipe, and addSmelting.<br>
	 * For fuels, override addFuel method from baseMod.
	 */
	public abstract void addRecipes();

	/**
	 * Do something when all is initialized.
	 */
	public abstract void postInit();
}
