package net.minecraft.src;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;


/**
 * PowerCraft Logic module
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class mod_PClogic extends PC_Module {
	@Override
	public String getVersion() {
		return mod_PCcore.VERSION;
	}

	/**
	 * Get images directory (ending with slash)
	 * 
	 * @return the directory
	 */
	public static String getImgDir() {
		return "/PowerCraft/logic/";
	}

	/**
	 * Get terrain file path (png)
	 * 
	 * @return the file path
	 */
	public static String getTerrainFile() {
		return getImgDir() + "tiles.png";
	}

	@Override
	public String getModuleName() {
		return "LOGIC";
	}


	/**
	 * World-wide radio manager
	 */
	public static PClo_RadioBus RADIO = new PClo_RadioBus();

	/**
	 * Network manager used by weasel devices to transfer information. Holds
	 * also instances of local networks, but the CORE weasel devices have to
	 * save them themselves.
	 */
	public static PClo_NetManager NETWORK = new PClo_NetManager();


	// *** PROPERTIES ***

	/** Channel used for newly built radio */
	public static String default_radio_channel = "default";
	/** range of newly placed sensor */
	public static int default_sensor_range = 3;

	private static final String pk_idGateOff = "id.block.gate_off";
	private static final String pk_idGateOn = "id.block.gate_on";
	private static final String pk_idPulsar = "id.block.pulsar";
	private static final String pk_idLightOff = "id.block.light_off";
	private static final String pk_idLightOn = "id.block.light_on";
	private static final String pk_brightLight = "brightness.light_on";
	private static final String pk_brightGate = "brightness.gate_on";
	private static final String pk_idRadio = "id.block.radio";
	private static final String pk_idWeasel = "id.block.weasel_device";
	private static final String pk_idSensor = "id.block.motion_sensor";
	private static final String pk_idRemote = "id.item.radio_remote";
	private static final String pk_idDisk = "id.item.weasel_data_disk";
	private static final String pk_optRadioDefChannel = "default.radio.channel";
	private static final String pk_optSensorDefRange = "default.sensor.range";



	// *** BLOCKS & ITEMS ***

	/** Radio transmitter block */
	public static Block radio;

	/** Entity sensor block (metadata subtypes) */
	public static PClo_BlockSensor sensor;

	/** Portable transmitter */
	public static Item portableTx;

	/** WeaselDisk */
	public static PClo_ItemWeaselDisk weaselDisk;

	/** flat device, off state */
	public static Block gateOff;

	/** flat device, on state */
	public static Block gateOn;

	/** pulsar block */
	public static Block pulsar;

	/** light, off state */
	public static Block lightOff;

	/** light, on state */
	public static Block lightOn;

	/** weasel device */
	public static Block weaselDevice;



	// *** MODULE INIT ***

	@Override
	public void preInit() {}

	@Override
	public void initProperties(PC_PropertyManager conf) {

		conf.putBlock(pk_idGateOff, 223);
		conf.putBlock(pk_idGateOn, 224);
		conf.putBlock(pk_idPulsar, 225);
		conf.putBlock(pk_idLightOff, 226);
		conf.putBlock(pk_idLightOn, 227);
		conf.putInteger(pk_brightLight, 12, "Light block brightness, scale 0-15.");
		conf.putInteger(pk_brightGate, 8, "Active gate block brightness, scale 0-15.");
		conf.putBlock(pk_idRadio, 236);
		conf.putBlock(pk_idSensor, 229);
		conf.putBlock(pk_idWeasel, 239);
		conf.putItem(pk_idRemote, 19000);
		conf.putItem(pk_idDisk, 19006);
		conf.putString(pk_optRadioDefChannel, "default", "the default channel for radios");
		conf.putInteger(pk_optSensorDefRange, 3, "the range of newly placed sensor");

		conf.apply();

		default_radio_channel = conf.getString(pk_optRadioDefChannel);
		default_sensor_range = conf.getInteger(pk_optSensorDefRange);
	}

	@Override
	public void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {
		list.add(new PC_Struct3(PClo_TileEntityGate.class, "FCLogicGate", null));
		list.add(new PC_Struct3(PClo_TileEntityWeasel.class, "PCWeaselDevice", new PClo_TileEntityWeaselRenderer()));
		list.add(new PC_Struct3(PClo_TileEntityPulsar.class, "FCRedstonePulsar", null));
		list.add(new PC_Struct3(PClo_TileEntityLight.class, "FCRedstoneIndicator", new PClo_TileEntityLightRenderer()));
		list.add(new PC_Struct3(PClo_TileEntitySensor.class, "FCSensorRanged", new PClo_TileEntitySensorRenderer()));
		list.add(new PC_Struct3(PClo_TileEntityRadio.class, "PCRadioDevice", new PClo_TileEntityRadioRenderer()));
	}

	@Override
	public void registerBlockRenderers() {
		PClo_Renderer.radioRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PClo_Renderer.sensorRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PClo_Renderer.weaselRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PClo_Renderer.lightRenderer = ModLoader.getUniqueBlockModelID(this, true);
	}

	@Override
	public void registerBlocks(List<Block> list) {
		//@formatter:off
		
		radio = new PClo_BlockRadio(cfg().getInteger(pk_idRadio))
				.setBlockName("PCloRadio")
				.setHardness(0.35F)
				.setResistance(30.0F);

		sensor = (PClo_BlockSensor) (new PClo_BlockSensor(cfg().getInteger(pk_idSensor))
				.setBlockName("PCloSensorRanged")
				.setHardness(0.35F)
				.setResistance(30.0F));


		gateOff = new PClo_BlockGate(cfg().getInteger(pk_idGateOff), false)
				.setBlockName("PCloLogicGate")
				.setHardness(0.35F).setLightValue(0)
				.setStepSound(Block.soundWoodFootstep)
				.disableStats().setRequiresSelfNotify()
				.setResistance(30.0F);


		weaselDevice = new PClo_BlockWeasel(cfg().getInteger(pk_idWeasel))
				.setBlockName("PCloWeasel")
				.setHardness(0.5F).setLightValue(0)
				.setStepSound(Block.soundWoodFootstep)
				.disableStats().setRequiresSelfNotify()
				.setResistance(60.0F);

		gateOn = new PClo_BlockGate(cfg().getInteger(pk_idGateOn), true)
				.setBlockName("PCloLogicGate")
				.setHardness(0.35F).setLightValue(cfg().getInteger(pk_brightGate) * 0.0625F)
				.setStepSound(Block.soundWoodFootstep)
				.disableStats().setRequiresSelfNotify()
				.setResistance(30.0F);

		pulsar = new PClo_BlockPulsar(cfg().getInteger(pk_idPulsar))
				.setHardness(0.8F)
				.setResistance(30.0F)
				.setBlockName("PCloRedstonePulsar")
				.setRequiresSelfNotify()
				.setStepSound(Block.soundWoodFootstep);

		lightOff = new PClo_BlockLight(cfg().getInteger(pk_idLightOff), false)
				.setHardness(0.3F)
				.setResistance(20F)
				.setBlockName("PCloLight")
				.setStepSound(Block.soundStoneFootstep)
				.setRequiresSelfNotify();

		lightOn = new PClo_BlockLight(cfg().getInteger(pk_idLightOn), true)
				.setHardness(0.3F)
				.setResistance(20F)
				.setLightValue(cfg().getInteger(pk_brightLight) * 0.0625F)
				.setBlockName("PCloLight")
				.setStepSound(Block.soundStoneFootstep)
				.setRequiresSelfNotify();
		
		list.add(radio);
		list.add(sensor);
		list.add(gateOff);
		list.add(gateOn);
		list.add(pulsar);
		list.add(lightOff);
		list.add(lightOn);
		
		//@formatter:on

	}

	@Override
	public void registerItems() {
		// @formatter:off
		
		portableTx = (new PClo_ItemRadioRemote(cfg().getInteger(pk_idRemote)))
				.setMaxStackSize(1)
				.setItemName("PCloRadioPortableTx");
		
		weaselDisk = (PClo_ItemWeaselDisk) (new PClo_ItemWeaselDisk(cfg().getInteger(pk_idDisk)))
				.setMaxStackSize(1)
				.setItemName("PCloWeaselDisk");
		
		// @formatter:on

		removeBlockItem(gateOn.blockID);
		setBlockItem(gateOn.blockID, new PClo_ItemBlockGate(gateOn.blockID - 256));
		removeBlockItem(weaselDevice.blockID);
		setBlockItem(weaselDevice.blockID, new PClo_ItemBlockWeasel(weaselDevice.blockID - 256));
		removeBlockItem(lightOn.blockID);
		setBlockItem(lightOn.blockID, new PClo_ItemBlockLight(lightOn.blockID - 256));
		removeBlockItem(sensor.blockID);
		setBlockItem(sensor.blockID, new PClo_ItemBlockSensor(sensor.blockID - 256, sensor));
		removeBlockItem(radio.blockID);
		setBlockItem(radio.blockID, new PClo_ItemBlockRadio(radio.blockID - 256));

	}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getTerrainFile());
		list.add(getImgDir() + "block_chip.png");
		list.add(getImgDir() + "block_radio.png");
		list.add(getImgDir() + "block_sensor.png");
		list.add(getImgDir() + "block_light.png");
	}

	@Override
	public void setTextures() {
		portableTx.setIconIndex(ModLoader.addOverride("/gui/items.png", getImgDir() + "portable.png"));
		weaselDisk.textureBg = ModLoader.addOverride("/gui/items.png", getImgDir() + "item_disk_base.png");
		weaselDisk.textureFg = ModLoader.addOverride("/gui/items.png", getImgDir() + "item_disk_label.png");
	}

	@Override
	public void setNames(Map<Object, String> map) {
		map.put(pulsar, "Redstone Pulsar");
		map.put(portableTx, "Radio Remote");

		map.put("tile.PCloRadio.name", "Redstone Radio Device");
		map.put("tile.PCloRadio.tx.name", "Redstone Radio Transmitter");
		map.put("tile.PCloRadio.rx.name", "Redstone Radio Receiver");

		map.put("tile.PCloSensorRanged.name", "Proximity Detector");
		map.put("tile.PCloSensorRanged.item.name", "Item Proximity Detector");
		map.put("tile.PCloSensorRanged.living.name", "Mob Proximity Detector");
		map.put("tile.PCloSensorRanged.player.name", "Player Proximity Detector");

		map.put("tile.PCloLight.0.name", "Black Indicator");
		map.put("tile.PCloLight.1.name", "Red Indicator");
		map.put("tile.PCloLight.2.name", "Green Indicator");
		map.put("tile.PCloLight.3.name", "Brown Indicator");
		map.put("tile.PCloLight.4.name", "Blue Indicator");
		map.put("tile.PCloLight.5.name", "Purple Indicator");
		map.put("tile.PCloLight.6.name", "Cyan Indicator");
		map.put("tile.PCloLight.7.name", "Light-gray Indicator");
		map.put("tile.PCloLight.8.name", "Gray Indicator");
		map.put("tile.PCloLight.9.name", "Pink Indicator");
		map.put("tile.PCloLight.10.name", "Lime Indicator");
		map.put("tile.PCloLight.11.name", "Yellow Indicator");
		map.put("tile.PCloLight.12.name", "Light-blue Indicator");
		map.put("tile.PCloLight.13.name", "Magenta Indicator");
		map.put("tile.PCloLight.14.name", "Orange Indicator");
		map.put("tile.PCloLight.15.name", "White Indicator");
		map.put("tile.PCloLight.name", "Indicator Light");


		map.put("tile.PCloLight.0.stable.name", "Black Lamp");
		map.put("tile.PCloLight.1.stable.name", "Red Lamp");
		map.put("tile.PCloLight.2.stable.name", "Green Lamp");
		map.put("tile.PCloLight.3.stable.name", "Brown Lamp");
		map.put("tile.PCloLight.4.stable.name", "Blue Lamp");
		map.put("tile.PCloLight.5.stable.name", "Purple Lamp");
		map.put("tile.PCloLight.6.stable.name", "Cyan Lamp");
		map.put("tile.PCloLight.7.stable.name", "Light-gray Lamp");
		map.put("tile.PCloLight.8.stable.name", "Gray Lamp");
		map.put("tile.PCloLight.9.stable.name", "Pink Lamp");
		map.put("tile.PCloLight.10.stable.name", "Lime Lamp");
		map.put("tile.PCloLight.11.stable.name", "Yellow Lamp");
		map.put("tile.PCloLight.12.stable.name", "Light-blue Lamp");
		map.put("tile.PCloLight.13.stable.name", "Magenta Lamp");
		map.put("tile.PCloLight.14.stable.name", "Orange Lamp");
		map.put("tile.PCloLight.15.stable.name", "White Lamp");

		map.put("tile.PCloLight.0.huge.name", "Black Display Segment");
		map.put("tile.PCloLight.1.huge.name", "Red Display Segment");
		map.put("tile.PCloLight.2.huge.name", "Green Display Segment");
		map.put("tile.PCloLight.3.huge.name", "Brown Display Segment");
		map.put("tile.PCloLight.4.huge.name", "Blue Display Segment");
		map.put("tile.PCloLight.5.huge.name", "Purple Display Segment");
		map.put("tile.PCloLight.6.huge.name", "Cyan Display Segment");
		map.put("tile.PCloLight.7.huge.name", "Light-gray Display Segment");
		map.put("tile.PCloLight.8.huge.name", "Gray Display Segment");
		map.put("tile.PCloLight.9.huge.name", "Pink Display Segment");
		map.put("tile.PCloLight.10.huge.name", "Lime Display Segment");
		map.put("tile.PCloLight.11.huge.name", "Yellow Display Segment");
		map.put("tile.PCloLight.12.huge.name", "Light-blue Display Segment");
		map.put("tile.PCloLight.13.huge.name", "Magenta Display Segment");
		map.put("tile.PCloLight.14.huge.name", "Orange Display Segment");
		map.put("tile.PCloLight.15.huge.name", "White Display Segment");

		map.put("tile.PCloLogicGate.name", "Redstone Logic Gate");
		map.put("tile.PCloLogicGate.not.name", "Redstone Inverter");
		map.put("tile.PCloLogicGate.and.name", "Redstone AND gate");
		map.put("tile.PCloLogicGate.nand.name", "Redstone NAND gate");
		map.put("tile.PCloLogicGate.or.name", "Redstone OR gate");
		map.put("tile.PCloLogicGate.nor.name", "Redstone NOR gate");
		map.put("tile.PCloLogicGate.xor.name", "Redstone XOR gate");
		map.put("tile.PCloLogicGate.xnor.name", "Redstone XNOR gate");
		map.put("tile.PCloLogicGate.xnor3.name", "Redstone 3-input XNOR gate");
		map.put("tile.PCloLogicGate.and3.name", "Redstone 3-input AND gate");
		map.put("tile.PCloLogicGate.nand3.name", "Redstone 3-input NAND gate");
		map.put("tile.PCloLogicGate.or3.name", "Redstone 3-input OR gate");
		map.put("tile.PCloLogicGate.nor3.name", "Redstone 3-input NOR gate");
		map.put("tile.PCloLogicGate.xor3.name", "Redstone 3-input XOR gate");

		map.put("tile.PCloLogicGate.d.name", "Redstone D flip-flop");
		map.put("tile.PCloLogicGate.rs.name", "Redstone RS flip-flop");
		map.put("tile.PCloLogicGate.t.name", "Redstone T flip-flop");

		map.put("tile.PCloLogicGate.day.name", "Day Sensor");
		map.put("tile.PCloLogicGate.rain.name", "Rain Sensor");
		map.put("tile.PCloLogicGate.chestEmpty.name", "Empty Chest Detector");
		map.put("tile.PCloLogicGate.chestFull.name", "Full Chest Detector");
		map.put("tile.PCloLogicGate.special.name", "Redstone Special Controller");
		map.put("tile.PCloLogicGate.buffer.name", "Buffered Delayer");
		map.put("tile.PCloLogicGate.slowRepeater.name", "Slow Repeater");
		map.put("tile.PCloLogicGate.crossing.name", "Redstone Crossing");
		map.put("tile.PCloLogicGate.random.name", "Redstone Random Gate");
		map.put("tile.PCloLogicGate.repeaterStraight.name", "Quick Repeater");
		map.put("tile.PCloLogicGate.repeaterCorner.name", "Angled Repeater");
		map.put("tile.PCloLogicGate.repeaterStraightInstant.name", "Instant Repeater");
		map.put("tile.PCloLogicGate.repeaterCornerInstant.name", "Instant Angled Repeater");
		map.put("tile.PCloLogicGate.night.name", "Night Sensor");
		map.put("tile.PCloLogicGate.splitter.name", "Redstone Splitter");


		// descriptions.
		map.put("pc.gate.not.desc", "negates input");
		map.put("pc.gate.and.desc", "both inputs on");
		map.put("pc.gate.nand.desc", "some inputs off");
		map.put("pc.gate.or.desc", "at least one input on");
		map.put("pc.gate.nor.desc", "all inputs off");
		map.put("pc.gate.xor.desc", "inputs different");
		map.put("pc.gate.xnor.desc", "inputs equal");
		map.put("pc.gate.xnor3.desc", "all inputs equal");
		map.put("pc.gate.and3.desc", "all inputs on");
		map.put("pc.gate.nand3.desc", "some inputs off");
		map.put("pc.gate.or3.desc", "at least one input on");
		map.put("pc.gate.nor3.desc", "all inputs off");
		map.put("pc.gate.xor3.desc", "inputs different");

		map.put("pc.gate.d.desc", "latch memory");
		map.put("pc.gate.rs.desc", "set/reset memory");
		map.put("pc.gate.t.desc", "divides signal by 2");

		map.put("pc.gate.day.desc", "on during day");
		map.put("pc.gate.rain.desc", "on during rain");
		map.put("pc.gate.chestEmpty.desc", "on if nearby container is empty");
		map.put("pc.gate.chestFull.desc", "on if nearby container is full");
		map.put("pc.gate.special.desc", "spawner & pulsar control");
		map.put("pc.gate.buffer.desc", "slows down signal");
		map.put("pc.gate.slowRepeater.desc", "makes pulses longer");
		map.put("pc.gate.crossing.desc", "lets two wires intersect");
		map.put("pc.gate.random.desc", "changes state randomly on pulse");
		map.put("pc.gate.repeaterStraight.desc", "simple 1-tick repeater");
		map.put("pc.gate.repeaterCorner.desc", "simple 1-tick corner repeater");
		map.put("pc.gate.repeaterStraightInstant.desc", "instant repeater");
		map.put("pc.gate.repeaterCornerInstant.desc", "instant corner repeater");
		map.put("pc.gate.night.desc", "on during night");
		map.put("pc.gate.splitter.desc", "splits signal");



		map.put("tile.PCloWeasel.name", "Weasel Device");

		map.put("tile.PCloWeasel.core.name", "Weasel Controller");
		map.put("tile.PCloWeasel.slave.name", "Slave Weasel Controller");
		map.put("tile.PCloWeasel.port.name", "Wireless Port");
		map.put("tile.PCloWeasel.display.name", "Wireless Display");
		map.put("tile.PCloWeasel.sound.name", "Wireless Speaker");
		map.put("tile.PCloWeasel.touchscreen.name", "Wireless Touchscreen");
		map.put("tile.PCloWeasel.diskManager.name", "Digital Workbench");
		map.put("tile.PCloWeasel.diskDrive.name", "Wireless Disk Drive");
		map.put("tile.PCloWeasel.terminal.name", "Weasel Terminal");
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
		map.put("pc.radio.activatorGetChannel", "Channel \"%s\" assigned to activation crystal.");
		map.put("pc.radio.activatorSetChannel", "Radio connected to channel \"%s\".");

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

		map.put("pc.radioRemote.connected", "Portable transmitter connected to channel \"%s\".");
		map.put("pc.radioRemote.desc", "Channel: %s");

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



		map.put("pc.gui.gate.delay", "Delay (sec)");
		map.put("pc.gui.gate.delayer.errRange", "Delay time out of range.");
		map.put("pc.gui.gate.delayer.errNumFormat", "Invalid number format.");
		map.put("pc.gui.gate.delayer.ticks", "ticks");
		map.put("pc.gui.pulsar.silent", "Silent");
		map.put("pc.gui.pulsar.delay", "Delay (sec)");
		map.put("pc.gui.pulsar.hold", "Hold time (sec)");
		map.put("pc.gui.pulsar.ticks", "ticks");
		map.put("pc.gui.pulsar.errDelay", "Bad delay time!");
		map.put("pc.gui.pulsar.errHold", "Bad hold time!");
		map.put("pc.gui.radio.channel", "Channel:");
		map.put("pc.gui.radio.showLabel", "Show label");
		map.put("pc.gui.radio.errChannel", "Invalid channel name.");
		map.put("pc.gui.radio.renderSmall", "Tiny");
		map.put("pc.pulsar.clickMsg", "Period %s ticks (%s s)");
		map.put("pc.pulsar.clickMsgTime", "Period %s ticks (%s s), remains %s");


		map.put("pc.gui.sensor.range", "Detection distance:");

		map.put("pc.sensor.range.1", "Range: %s block");
		map.put("pc.sensor.range.2-4", "Range: %s blocks");
		map.put("pc.sensor.range.5+", "Range: %s blocks");


		map.put("pc.gui.chestFull.requireAllSlotsFull", "All slots must be fully used");

	}

	@Override
	public void addRecipes() {
		//@formatter:off
		
		// *** pulsar ***
		
		ModLoader.addRecipe(
				new ItemStack(pulsar, 1, 0),
				new Object[] { " r ", "ror", " r ",
					'r', Item.redstone, 'o', Block.obsidian });

		
		// *** basic gates ***
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.NOT),
				new Object[] { "RST",
					'R', Item.redstone, 'S', Block.stone, 'T', Block.torchRedstoneActive });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.AND),
				new Object[] { " R ", "SSS", "R R",
					'R', Item.redstone, 'S', Block.stone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.OR),
				new Object[] { " R ", "RSR",
					'R', Item.redstone, 'S', Block.stone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.XOR),
				new Object[] { "R", "X",
					'X', new ItemStack(gateOn, 1, PClo_GateType.OR), 'R', Item.redstone });

		
		// *** negated ***
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.NAND),
				new Object[] { "I", "G",
					'I', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.AND) });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.NOR),
				new Object[] { "I", "G",
					'I', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.OR) });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.XNOR),
				new Object[] { "I", "G",
					'I', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.XOR) });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.NOR3),
				new Object[] { "I", "G",
					'I', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.OR3) });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.NAND3),
				new Object[] { "I", "G",
					'I', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.AND3) });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.XNOR3),
				new Object[] { "I", "G",
					'I', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.XOR3) });

		
		// *** basic 3-input ***
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.AND3),
				new Object[] { " R ", "SSS", "RRR",
					'R', Item.redstone, 'S', Block.stone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.OR3),
				new Object[] { " R ", "RSR", " R ",
					'R', Item.redstone, 'S', Block.stone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.XOR3),
				new Object[] { "R", "X",
					'X', new ItemStack(gateOn, 1, PClo_GateType.OR3), 'R', Item.redstone });

		// *** flip flops ***
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.D),
				new Object[] { " S ", "RSR", " S ",
					'S', Block.stone, 'R', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.RS),
				new Object[] { " R ", "SLS", "R R",
					'R', Item.redstone, 'S', Block.stone, 'L', Block.lever });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.T),
				new Object[] { "RSR",
					'R', Item.redstone, 'S', Block.stone });
		

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.RANDOM),
				new Object[] { "R","T",
					'R', Item.redstone, 'T', new ItemStack(gateOn, 1, PClo_GateType.T) });

		// *** special sensors ***
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.DAY),
				new Object[] { "G", "P",
					'G', Item.lightStoneDust, 'P', Block.pressurePlatePlanks });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.DAY),
				new Object[] { "G", "P",
					'G', Item.lightStoneDust, 'P', Block.pressurePlateStone });
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.NIGHT),
				new Object[] { "N", "G",
					'N', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.DAY) });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.RAIN),
				new Object[] { "L", "P",
					'L', new ItemStack(Item.dyePowder, 1, 4), 'P', Block.pressurePlatePlanks });
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1,
						PClo_GateType.RAIN),
				new Object[] { "L", "P",
					'L', new ItemStack(Item.dyePowder, 1, 4), 'P', Block.pressurePlateStone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.CHEST_EMPTY),
				new Object[] { "C", "P",
					'C', Block.chest, 'P', Block.pressurePlatePlanks });
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.CHEST_EMPTY),
				new Object[] { "C", "P",
					'C', Block.chest, 'P', Block.pressurePlateStone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.CHEST_FULL),
				new Object[] { "I", "G",
					'I', new ItemStack(gateOn, 1, PClo_GateType.NOT), 'G', new ItemStack(gateOn, 1, PClo_GateType.CHEST_EMPTY) });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.SPECIAL),
				new Object[] { " I", "RS",
					'R', Item.redstone, 'S', Block.stone, 'I', Item.ingotIron });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.FIFO_DELAYER),
				new Object[] { "DDD", "SSS",
					'D', Item.redstoneRepeater, 'S', Block.stone });

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.HOLD_DELAYER),
				new Object[] { "DD", "SS",
					'D', Item.redstoneRepeater, 'S', Block.stone });
		

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.REPEATER_STRAIGHT),
				new Object[] { "R", "R", "R",
					'R', Item.redstone});
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.REPEATER_CORNER),
				new Object[] { "RR", " R",
					'R', Item.redstone});
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.REPEATER_STRAIGHT_I),
				new Object[] { "R", "S",
					'R', Item.redstone, 'S', new ItemStack(gateOn,1,PClo_GateType.REPEATER_STRAIGHT)});
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.REPEATER_CORNER_I),
				new Object[] { "R", "S",
					'R', Item.redstone, 'S', new ItemStack(gateOn,1,PClo_GateType.REPEATER_CORNER)});

		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.CROSSING),
				new Object[] { " + ", "+++", " + ",
					'+', Item.redstone });
		
		ModLoader.addRecipe(
				new ItemStack(gateOn, 1, PClo_GateType.SPLITTER_I),
				new Object[] { "S+S", "+++", "S+S",
					'+', Item.redstone,'S', Block.stone});
		
		
		
		// weasel.
		
		
		/**
		 * TODO stairsBrick was stairsBrick??
		 */
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.CORE),
				new Object[] { "SRS", "RCR", "SRS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'C', mod_PCcore.powerCrystal });
		

		ModLoader.addShapelessRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.SLAVE),
				new Object[] { new ItemStack(weaselDevice, 1, PClo_WeaselType.CORE) });

		ModLoader.addShapelessRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.CORE),
				new Object[] { new ItemStack(weaselDevice, 1, PClo_WeaselType.SLAVE) });
		
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.PORT),
				new Object[] { "GRG", "SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'G', Item.goldNugget });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.DISPLAY),
				new Object[] { " G ", "NRN","SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'G', Block.thinGlass, 'N', Item.goldNugget });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.SPEAKER),
				new Object[] { " N ", "GRG","SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'N', Block.music, 'G', Item.goldNugget  });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.TOUCHSCREEN),
				new Object[] { "GGG", "NRN","SSS",
					'S', new ItemStack(Block.stairsBrick,1,0), 'R', Item.redstone, 'G', Block.thinGlass, 'N', Item.goldNugget });
		

		ModLoader.addRecipe(
				new ItemStack(weaselDisk, 4, 0xFFF),
				new Object[] { " C ", "CIC"," C ",
					'C', Item.coal, 'I', Item.ingotIron });
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.DISK_MANAGER),
				new Object[] { "BBB", "SRS", "SSS",
					'B', Block.button,
					'S', new ItemStack(Block.stairsBrick,1,0),
					'R', Item.redstone});
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.DISK_DRIVE),
				new Object[] { "SSS", "GRG", "SSS",
					'B', Block.button,
					'S', new ItemStack(Block.stairsBrick,1,0),
					'R', Item.redstone,
					'G', Item.goldNugget
					});
		
		ModLoader.addRecipe(
				new ItemStack(weaselDevice, 1, PClo_WeaselType.TERMINAL),
				new Object[] { "  D", "BBS", "SSS",
					'B', Block.button,
					'S', new ItemStack(Block.stairsBrick,1,0),
					'D', new ItemStack(weaselDevice, 1, PClo_WeaselType.DISPLAY),
					});
		
		
		// *** lights ***
		
		// SMALL
		
		for(int i=0; i<16; i++) {
			ModLoader.addShapelessRecipe(
					new ItemStack(lightOn, 1, i),
					new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, i) });
		}

		
		// STABLE

		for(int i=16; i<32; i++) {
			ModLoader.addShapelessRecipe(
					new ItemStack(lightOn, 1, i),
					new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, i%16) });
		}
		
		// SEGMENTS
		
		for(int i=32; i<48; i++) {
			ModLoader.addShapelessRecipe(
					new ItemStack(lightOn, 1, i),
					new Object[] { Item.redstone, Block.glowStone, new ItemStack(Item.dyePowder, 1, i%16) });
		}
		

		
		
		
		// *** RADIOS ***
		
		//transmiter (gold) = 0
		ModLoader.addRecipe(
				new ItemStack(radio,1,0),
				new Object[] { " I ", "RIR", "SSS",
					'I', Item.ingotGold, 'R', Item.redstone, 'S', Block.stone });

		//receiver (iron) = 1
		ModLoader.addRecipe(
				new ItemStack(radio,1,1),
				new Object[] { " I ", "RIR", "SSS",
					'I', Item.ingotIron, 'R', Item.redstone, 'S', Block.stone });

		ModLoader.addRecipe(
				new ItemStack(portableTx),
				new Object[] { "T", "B",
					'B', Block.button, 'T', radio });

		// *** SENSORS ***
		
		ModLoader.addRecipe(
				new ItemStack(sensor, 1, 1),
				new Object[] { "R", "I", "S",
					'I', Item.ingotIron, 'R', Item.redstone, 'S', Block.stone });


		ModLoader.addRecipe(
				new ItemStack(sensor, 1, 0),
				new Object[] { "R", "I", "W",
					'I', Item.ingotIron, 'R', Item.redstone, 'W', Block.planks });

		ModLoader.addRecipe(
				new ItemStack(sensor, 1, 2),
				new Object[] { "R", "I", "O",
					'I', Item.ingotIron, 'R', Item.redstone, 'O', Block.obsidian });
		

		//@formatter:on
	}

	@Override
	public void postInit() {
		PC_InveditManager.setDamageRange(gateOn.blockID, 0, PClo_GateType.TOTAL_GATE_COUNT - 1);
		PC_InveditManager.setDamageRange(weaselDevice.blockID, 0, PClo_WeaselType.WEASEL_DEVICE_COUNT - 1);
		PC_InveditManager.setDamageRange(sensor.blockID, 0, 2);
		PC_InveditManager.setDamageRange(lightOn.blockID, 0, 47);
		PC_InveditManager.hideItem(gateOff.blockID);
		PC_InveditManager.hideItem(lightOff.blockID);

		PC_InveditManager.setItemCategory(lightOn.blockID, "Control lights");

		PC_InveditManager.setItemCategory(pulsar.blockID, "Logic gates");
		PC_InveditManager.setItemCategory(gateOn.blockID, "Logic gates");
		PC_InveditManager.setItemCategory(weaselDevice.blockID, "Weasel system");
		PC_InveditManager.setItemCategory(sensor.blockID, "Wireless");
		PC_InveditManager.setItemCategory(radio.blockID, "Wireless");
		PC_InveditManager.setItemCategory(portableTx.shiftedIndex, "Wireless");


		//@formatter:off
		addStackRangeToCraftingTool(PC_ItemGroup.LOGIC, gateOn.blockID, 0, PClo_GateType.TOTAL_GATE_COUNT - 1, 1);
		addStacksToCraftingTool(PC_ItemGroup.LOGIC, 
				new ItemStack(pulsar));
		addStackRangeToCraftingTool(PC_ItemGroup.LOGIC, weaselDevice.blockID, 0, PClo_WeaselType.WEASEL_DEVICE_COUNT - 1, 1);
		addStacksToCraftingTool(PC_ItemGroup.LOGIC, new ItemStack(weaselDisk,1,0xfff));
		
		addStacksToCraftingTool(PC_ItemGroup.LIGHTS,
				
				new ItemStack(lightOn, 1, PC_Color.dye.RED.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.ORANGE.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.YELLOW.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.LIME.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.GREEN.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.CYAN.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.LIGHTBLUE.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.BLUE.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.PURPLE.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.MAGENTA.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.PINK.meta),
				new ItemStack(lightOn, 1, PC_Color.dye.WHITE.meta),
				
				new ItemStack(lightOn, 1, PC_Color.dye.RED.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.ORANGE.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.YELLOW.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.LIME.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.GREEN.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.CYAN.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.LIGHTBLUE.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.BLUE.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.PURPLE.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.MAGENTA.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.PINK.meta+32),
				new ItemStack(lightOn, 1, PC_Color.dye.WHITE.meta+32),
				
				new ItemStack(lightOn, 1, PC_Color.dye.RED.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.ORANGE.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.YELLOW.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.LIME.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.GREEN.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.CYAN.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.LIGHTBLUE.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.BLUE.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.PURPLE.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.MAGENTA.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.PINK.meta+16),
				new ItemStack(lightOn, 1, PC_Color.dye.WHITE.meta+16)
		
				);
		
		addStacksToCraftingTool(PC_ItemGroup.WIRELESS,
				
				new ItemStack(radio, 1, 0),
				new ItemStack(radio, 1, 1), 
				new ItemStack(portableTx),
				
				new ItemStack(sensor, 1, 0),
				new ItemStack(sensor, 1, 1),
				new ItemStack(sensor, 1, 2));
		
		//@formatter:on

		ModLoader.setInGameHook(this, true, false);
		ModLoader.setInGUIHook(this, true, false);
	}



	@Override
	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int renderType) {
		return PClo_Renderer.renderBlockByType(renderblocks, iblockaccess, i, j, k, block, renderType);
	}

	@Override
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int rtype) {
		PClo_Renderer.renderInvBlockByType(renderblocks, block, i, rtype);
	}

	private int tickCounter = 0;

	@Override
	public boolean onTickInGame(float f, Minecraft minecraft) {
		if (tickCounter++ % 100 == 0) {
			if (NETWORK.needsSave) {
				NETWORK.saveToFile();
			}
		}
		return true;
	}

	@Override
	public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen guiscreen) {

		if (guiscreen == null) return true;

		if (NETWORK.needsSave) {
			NETWORK.saveToFile();
		}
		return true;
	}

	@Override
	public List<Class> addGui() {
		List<Class> guis = new ArrayList<Class>();
		guis.add(PClo_GuiCallerDelayer.class);
		guis.add(PClo_GuiCallerPulsar.class);
		guis.add(PClo_GuiCallerRadio.class);
		guis.add(PClo_GuiCallerSensor.class);
		return guis;
	}

	@Override
	protected Hashtable<String, PC_PacketHandler> addPacketHandler() {
		// TODO Auto-generated method stub
		return null;
	}
}
