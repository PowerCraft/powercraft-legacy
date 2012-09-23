package net.minecraft.src;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class mod_PCnet extends PC_Module {

	public static mod_PCtransport instance;
	
	@Override
	public String getModuleName() {
		return "NETWORK";
	}

	@Override
	public String getVersion() {
		
		return mod_PCcore.VERSION;
	}
	
	/**
	 * Get terrain file path (png)
	 * 
	 * @return the file path
	 */
	public static String getTerrainFile() {
		return getImgDir() + "tiles.png";
	}
	
	/**
	 * Get images directory (ending with slash)
	 * 
	 * @return the directory
	 */
	public static String getImgDir(){
		return "/PowerCraft/network/";
	}

	/**
	 * Network manager used by weasel devices to transfer information. Holds
	 * also instances of local networks, but the CORE weasel devices have to
	 * save them themselves.
	 */
	public static PCnt_WeaselManager_UNUSED NETWORK = new PCnt_WeaselManager_UNUSED();

	private static final String pk_teleporter = "id.block.teleporter";
	private static final String pk_teleporter_brightness = "brightness.teleporter";
	
	private static final String pk_idWeasel = "id.block.weasel_device";
	private static final String pk_idDisk = "id.item.weasel_data_disk";
	
	// *** BLOCKS & ITEMS ***

	/** entity teleporter block */
	public static Block teleporter;

	/** WeaselDisk */
	public static PCnt_ItemWeaselDisk_UNUSED weaselDisk;

	/** weasel device */
	public static Block weaselDevice;
	
	
	
	// *** MODULE INIT ***
	
	@Override
	public void preInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initProperties(PC_PropertyManager conf) {
		conf.putBlock(pk_teleporter, 235);
		conf.putInteger(pk_teleporter_brightness, 5, "Teleporter block brightness, scale 0-15.");
		conf.putBlock(pk_idWeasel, 239);
		conf.putItem(pk_idDisk, 19006);

		conf.apply();
	}

	@Override
	public void registerEntities(
			List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {

	}

	@Override
	public void registerTileEntities(
			List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {
		
		list.add(new PC_Struct3(PCnt_TileEntityWeasel_UNUSED.class, "PCWeaselDevice", new PCnt_TileEntityWeaselRenderer_UNUSED()));
		list.add(new PC_Struct3(PCnt_TileEntityTeleporter.class, "PCteleporter", new PCnt_TileEntityTeleporterRenderer()));

	}

	@Override
	public void registerBlockRenderers() {
		PCnt_Renderer.teleporterRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PCnt_Renderer.weaselRenderer = ModLoader.getUniqueBlockModelID(this, true);

	}
	
	@Override
	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int renderType) {
		return PCnt_Renderer.renderBlockByType(renderblocks, iblockaccess, i, j, k, block, renderType);
	}

	@Override
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int rtype) {
		PCnt_Renderer.renderInvBlockByType(renderblocks, block, i, rtype);
	}

	@Override
	public void registerBlocks(List<Block> list) {

		teleporter = new PCnt_BlockTeleporter(cfg().getInteger(pk_teleporter), 14, Material.portal)
				.setHardness(1.0F)
				.setResistance(8.0F)
				.setLightValue(cfg().getInteger(pk_teleporter_brightness) * 0.0625F)
				.setBlockName("PCteleporter")
				.setStepSound(Block.soundMetalFootstep);

		
		weaselDevice = new PCnt_BlockWeasel_UNUSED(cfg().getInteger(pk_idWeasel))
				.setBlockName("PCntWeasel")
				.setHardness(0.5F).setLightValue(0)
				.setStepSound(Block.soundWoodFootstep)
				.disableStats().setRequiresSelfNotify()
				.setResistance(60.0F);
		
		list.add(teleporter);
	}

	@Override
	public void registerItems() {
		
		weaselDisk = (PCnt_ItemWeaselDisk_UNUSED) (new PCnt_ItemWeaselDisk_UNUSED(cfg().getInteger(pk_idDisk)))
				.setMaxStackSize(1)
				.setItemName("PCntWeaselDisk");

		removeBlockItem(weaselDevice.blockID);
		setBlockItem(weaselDevice.blockID, new PCnt_ItemBlockWeasel_UNUSED(weaselDevice.blockID - 256));

	}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getTerrainFile());
		list.add(getImgDir() + "block_chip.png");

	}

	@Override
	public void setTextures() {
		teleporter.blockIndexInTexture = 14;

		weaselDisk.textureBg = ModLoader.addOverride("/gui/items.png", getImgDir() + "item_disk_base.png");
		weaselDisk.textureFg = ModLoader.addOverride("/gui/items.png", getImgDir() + "item_disk_label.png");

	}

	@Override
	public void setNames(Map<Object, String> map) {
		map.put(teleporter, "Teleporter");

		map.put("pc.gui.teleporter.set", "SET");
		map.put("pc.gui.teleporter.items", "items");
		map.put("pc.gui.teleporter.animals", "animals");
		map.put("pc.gui.teleporter.monsters", "monsters");
		map.put("pc.gui.teleporter.players", "players");
		map.put("pc.gui.teleporter.dir.north", "north");
		map.put("pc.gui.teleporter.dir.south", "south");
		map.put("pc.gui.teleporter.dir.east", "east");
		map.put("pc.gui.teleporter.dir.west", "west");
		map.put("pc.gui.teleporter.sneak", "Sneak to trigger");
		map.put("pc.gui.teleporter.errIdUsed", "ID already used.");
		map.put("pc.gui.teleporter.errIdRequired", "ID is required!");
		map.put("pc.gui.teleporter.errIdNotFound", "Target not found.");
		map.put("pc.gui.teleporter.errIdDimEnd", "Target is in the End.");
		map.put("pc.gui.teleporter.errIdDimWorld", "Target is in the Overworld.");
		map.put("pc.gui.teleporter.errIdDimNether", "Target is in the Nether.");
		map.put("pc.gui.teleporter.errIdDim", "Target is in other dimension.");
		map.put("pc.gui.teleporter.errTargetRequired", "Target is required.");
		map.put("pc.gui.teleporter.titleSender", "Entity Teleporter - Sender");
		map.put("pc.gui.teleporter.titleTarget", "Entity Teleporter - Target");
		map.put("pc.gui.teleporter.title", "Entity Teleporter");
		map.put("pc.gui.teleporter.linksTo", "Links to (target ID):");
		map.put("pc.gui.teleporter.selectType", "Select device type.");
		map.put("pc.gui.teleporter.selectTypeDescr", "Sender teleports items to target.");
		map.put("pc.gui.teleporter.deviceId", "Device ID:");
		map.put("pc.gui.teleporter.teleportGroup", "Teleport:");
		map.put("pc.gui.teleporter.outputDirection", "Primary output:");
		map.put("pc.gui.teleporter.type.sender", "SENDER");
		map.put("pc.gui.teleporter.type.target", "TARGET");
		map.put("pc.gui.teleporter.showLabel", "Show label");
		
		
		
		map.put("tile.PCntWeasel.name", "Weasel Device");

		map.put("tile.PCntWeasel.core.name", "Weasel Controller");
		map.put("tile.PCntWeasel.slave.name", "Slave Weasel Controller");
		map.put("tile.PCntWeasel.port.name", "Wireless Port");
		map.put("tile.PCntWeasel.display.name", "Wireless Display");
		map.put("tile.PCntWeasel.sound.name", "Wireless Speaker");
		map.put("tile.PCntWeasel.touchscreen.name", "Wireless Touchscreen");
		map.put("tile.PCntWeasel.diskManager.name", "Digital Workbench");
		map.put("tile.PCntWeasel.diskDrive.name", "Wireless Disk Drive");
		map.put("tile.PCntWeasel.terminal.name", "Weasel Terminal");
		map.put("pc.weasel.core.desc", "programmable chip");
		map.put("pc.weasel.port.desc", "expansion redstone port");
		map.put("pc.weasel.display.desc", "display for Weasel");
		map.put("pc.weasel.sound.desc", "electronic noteblock");
		map.put("pc.weasel.touchscreen.desc", "touchscreen display");
		map.put("pc.weasel.diskManager.desc", "Weasel disk editor");
		map.put("pc.weasel.diskDrive.desc", "Weasel disk reader");
		map.put("pc.weasel.terminal.desc", "command interface");
		map.put("pc.weasel.slave.desc", "slave co-processor");

		map.put("pc.weasel.activatorGetNetwork", "Network \"%s\" assigned to activation crystal.");
		map.put("pc.weasel.activatorSetNetwork", "Device connected to network \"%s\".");

		map.put("pc.gui.weasel.connectedToNetwork", "Connected to network:");
		map.put("pc.gui.weasel.rename", "Rename");
		map.put("pc.gui.weasel.errDeviceNameTooShort", "Entered name is too short.");
		map.put("pc.gui.weasel.errDeviceNameAlreadyUsed", "Device %s already exists in this network.");
		map.put("pc.gui.weasel.deviceRenamed", "Device renamed to %s.");
		map.put("pc.gui.weasel.close", "Close");

		map.put("pc.gui.weasel.port.portName", "Port name:");
		map.put("pc.gui.weasel.display.displayName", "Display name:");
		map.put("pc.gui.weasel.sound.speakerName", "Speaker name:");
		map.put("pc.gui.weasel.drive.driveName", "Drive name:");
		map.put("pc.gui.weasel.terminal.terminalName", "Terminal name:");
		map.put("pc.gui.weasel.slave.slaveName", "Controller name:");


		map.put("pc.gui.weasel.core.program", "Program");
		map.put("pc.gui.weasel.core.settings", "Settings");
		map.put("pc.gui.weasel.core.status", "Status");

		map.put("pc.gui.weasel.core.runningStateLabel", "Program state:");
		map.put("pc.gui.weasel.core.stackLabel", "Stack size:");
		map.put("pc.gui.weasel.core.memoryLabel", "Memory size:");
		map.put("pc.gui.weasel.core.statusLabel", "Status:");
		map.put("pc.gui.weasel.core.programLength", "Program length:");
		map.put("pc.gui.weasel.core.peripheralsLabel", "Peripherals:");
		map.put("pc.gui.weasel.core.unitInstructions", "instructions");
		map.put("pc.gui.weasel.core.unitObjects", "values");
		map.put("pc.gui.weasel.core.networkLabel", "Network name:");
		map.put("pc.gui.weasel.core.colorLabel", "Network color:");

		map.put("pc.gui.weasel.core.title", "Weasel Controller");
		map.put("pc.gui.weasel.slave.title", "Slave Weasel Controller");
		map.put("pc.gui.weasel.diskDrive.title", "Weasel Disk Drive");
		map.put("pc.gui.weasel.port.title", "Expansion port for Weasel Controller");
		map.put("pc.gui.weasel.display.title", "Display for Weasel Controller");
		map.put("pc.gui.weasel.sound.title", "Audio output for Weasel");
		map.put("pc.gui.weasel.touchscreen.title", "Touchscreen display for Weasel");
		map.put("pc.gui.weasel.terminal.title", "Weasel Terminal");

		map.put("pc.gui.weasel.core.undoAll", "Undo All");
		map.put("pc.gui.weasel.core.check", "Check");
		map.put("pc.gui.weasel.core.launch", "Launch");
		map.put("pc.gui.weasel.core.running", "Running");
		map.put("pc.gui.weasel.core.paused", "Paused");
		map.put("pc.gui.weasel.core.idle", "Idle");
		map.put("pc.gui.weasel.core.waiting", "Waiting");
		map.put("pc.gui.weasel.core.halted", "Halted");
		map.put("pc.gui.weasel.core.crashed", "Crashed");
		map.put("pc.gui.weasel.core.pause", "Pause");
		map.put("pc.gui.weasel.core.resume", "Resume");
		map.put("pc.gui.weasel.core.restart", "Restart");
		map.put("pc.gui.weasel.core.stop", "Stop");
		map.put("pc.gui.weasel.core.title", "Weasel Controller");
		map.put("pc.gui.weasel.core.colorChange", "Change");

		map.put("pc.gui.weasel.core.errNetworkNameTooShort", "Entered network name is too short.");
		map.put("pc.gui.weasel.core.errNetworkNameAlreadyUsed", "Network with this name already exists.");
		map.put("pc.gui.weasel.core.msgNetworkRenamed", "Network renamed to %s.");
		map.put("pc.gui.weasel.core.msgNetworkColorChanged", "Network color changed.");
		map.put("pc.gui.weasel.core.msgPaused", "Program paused.");
		map.put("pc.gui.weasel.core.msgResumed", "Program resumed.");
		map.put("pc.gui.weasel.core.msgRestarted", "Program restarted.");
		map.put("pc.gui.weasel.core.msgLaunched", "New program compiled and started.");
		map.put("pc.gui.weasel.core.msgNoErrors", "Program has no syntax errors.");
		map.put("pc.gui.weasel.core.msgAllUndone", "All changes reverted.");
		map.put("pc.gui.weasel.core.msgHalted", "Program execution halted, network restarted.");
		map.put("pc.gui.weasel.slave.msgHalted", "Program execution halted.");

		map.put("pc.weasel.disk.new_label", "disk");
		map.put("pc.weasel.disk.empty", "Blank Weasel Disk");
		map.put("pc.weasel.disk.text", "Weasel Text Disk");
		map.put("pc.weasel.disk.image", "Weasel Image Disk");
		map.put("pc.weasel.disk.numberList", "Weasel Numbers Disk");
		map.put("pc.weasel.disk.stringList", "Weasel Strings Disk");
		map.put("pc.weasel.disk.variableMap", "Weasel Data Disk");
		map.put("pc.weasel.disk.programLibrary", "Weasel Library Disk");
		map.put("pc.gui.weasel.diskManager.color", "Color:");
		map.put("pc.gui.weasel.diskManager.label", "Disk label:");
		map.put("pc.gui.weasel.diskManager.separator", "Entry separator:");
		map.put("pc.gui.weasel.diskManager.resize", "Resize");
		map.put("pc.gui.weasel.diskManager.set", "Set");
		map.put("pc.gui.weasel.diskManager.edit", "Edit");
		map.put("pc.gui.weasel.diskManager.img.clear", "Clear");
		map.put("pc.gui.weasel.diskManager.img.fill", "Fill");
		map.put("pc.gui.weasel.diskManager.disk", "Disk");
		map.put("pc.gui.weasel.diskManager.format", "Format:");
		map.put("pc.gui.weasel.diskManager.formatText", "Text");
		map.put("pc.gui.weasel.diskManager.formatImage", "Image");
		map.put("pc.gui.weasel.diskManager.formatIntegerList", "Numbers");
		map.put("pc.gui.weasel.diskManager.formatStringList", "Strings");
		map.put("pc.gui.weasel.diskManager.formatVariableMap", "Data");
		map.put("pc.gui.weasel.diskManager.formatLibrary", "Library");
		map.put("pc.gui.weasel.diskManager.clickCompile", "Click \"Compile\" to make the library executable.");
		map.put("pc.gui.weasel.diskManager.compiled", "Library was successfully compiled.");


		map.put("pc.gui.weasel.diskManager.empty.title", "Digital Workbench - insert disk");

	}

	@Override
	public void modsLoaded() {

		// @formatter:off
		
		ItemStack prism;
		
		if(PC_Module.isModuleLoaded("MACHINES")){
			//safety check
			prism = new ItemStack(mod_PCmachines.optical,1,1);
		}else{
			prism = new ItemStack(Block.glass);
		}
		
		ModLoader.addRecipe(
				new ItemStack(teleporter, 1, 0),
				new Object[] { " P ", "PVP", "SSS",
					'V', new ItemStack(Item.dyePowder, 1, 5), 'P', prism, 'S', Item.ingotIron });
		
		// @formatter:on

		super.modsLoaded();
	}
	
	@Override
	public void addRecipes() {
		
		// weasel.
		
		
		/**
		 * TODO stairsBrick was stairsBrick??
		 */
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.CORE),
				new Object[] { "SRS", "RCR", "SRS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'C', mod_PCcore.powerCrystal });
		

		ModLoader.addShapelessRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.SLAVE),
				new Object[] { new ItemStack(weaselDevice, 1, PCnt_WeaselType.CORE) });

		ModLoader.addShapelessRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.CORE),
				new Object[] { new ItemStack(weaselDevice, 1, PCnt_WeaselType.SLAVE) });
		
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.PORT),
				new Object[] { "GRG", "SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'G', Item.goldNugget });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.DISPLAY),
				new Object[] { " G ", "NRN","SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'G', Block.thinGlass, 'N', Item.goldNugget });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.SPEAKER),
				new Object[] { " N ", "GRG","SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'N', Block.music, 'G', Item.goldNugget  });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.TOUCHSCREEN),
				new Object[] { "GGG", "NRN","SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'G', Block.thinGlass, 'N', Item.goldNugget });
		

		ModLoader.addRecipe(
				new ItemStack(weaselDisk, 4, 0xFFF),
				new Object[] { " C ", "CIC"," C ",
					'C', Item.coal, 'I', Item.ingotIron });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.DISK_MANAGER),
				new Object[] { "BBB", "SRS", "SSS",
					'B', Block.button,
					'S', new ItemStack(Block.stairsBrick,1,0),
					'R', Item.redstone});
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.DISK_DRIVE),
				new Object[] { "SSS", "GRG", "SSS",
					'B', Block.button,
					'S', new ItemStack(Block.stairsBrick,1,0),
					'R', Item.redstone,
					'G', Item.goldNugget
					});
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PCnt_WeaselType.TERMINAL),
				new Object[] { "  D", "BBS", "SSS",
					'B', Block.button,
					'S', new ItemStack(Block.stairsBrick,1,0),
					'D', new ItemStack(weaselDevice, 1, PCnt_WeaselType.DISPLAY),
					});
	}

	@Override
	protected Hashtable<String, PC_PacketHandler> addPacketHandler() {
		Hashtable<String, PC_PacketHandler> ph = new Hashtable<String, PC_PacketHandler>();
		ph.put("Weasel", NETWORK);
		return ph;
	}

	@Override
	protected Hashtable<String, PC_INBTWD> addNetManager() {
		Hashtable<String, PC_INBTWD> nm = new Hashtable<String, PC_INBTWD>();
		nm.put("Weasel", NETWORK);
		return nm;
	}

	@Override
	public void postInit() {
		String ctg = "Networking";

		PC_InveditManager.setItemCategory(teleporter.blockID, ctg);
		
		PC_InveditManager.setDamageRange(weaselDevice.blockID, 0, PCnt_WeaselType.WEASEL_DEVICE_COUNT - 1);

		PC_InveditManager.setItemCategory(weaselDevice.blockID, "Weasel system");
	
		addStacksToCraftingTool(PC_ItemGroup.NETWORK, new ItemStack(teleporter));

		addStackRangeToCraftingTool(PC_ItemGroup.NETWORK, weaselDevice.blockID, 0, PCnt_WeaselType.WEASEL_DEVICE_COUNT - 1, 1);
		addStacksToCraftingTool(PC_ItemGroup.NETWORK, new ItemStack(weaselDisk,1,0xfff));
	}

}
