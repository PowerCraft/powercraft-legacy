package net.minecraft.src;


import java.util.List;
import java.util.Map;

import weasel.Calc;

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
	private static final String pk_optRadioDefChannel = "default.radio.channel";
	private static final String pk_optSensorDefRange = "default.sensor.range";



	// *** BLOCKS & ITEMS ***

	/** Radio transmitter block */
	public static Block radio;

	/** Entity sensor block (metadata subtypes) */
	public static PClo_BlockSensor sensor;

	/** Portable transmitter */
	public static Item portableTx;

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
		list.add(new PC_Struct3(PClo_TileEntityLight.class, "FCRedstoneIndicator", null));
		list.add(new PC_Struct3(PClo_TileEntitySensor.class, "FCSensorRanged", new PClo_TileEntitySensorRenderer()));
		list.add(new PC_Struct3(PClo_TileEntityRadio.class, "PCRadioDevice", new PClo_TileEntityRadioRenderer()));
	}

	@Override
	public void registerBlockRenderers() {
		PClo_Renderer.radioRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PClo_Renderer.sensorRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PClo_Renderer.weaselRenderer = ModLoader.getUniqueBlockModelID(this, true);
	}

	@Override
	public void registerBlocks(List<Block> list) {
		//@formatter:off
		
		radio = new PClo_BlockRadio(cfg().getInteger(pk_idRadio))
				.setBlockName("PCloRadio")
				.setHardness(0.35F)
				.setResistance(15.0F);

		sensor = (PClo_BlockSensor) (new PClo_BlockSensor(cfg().getInteger(pk_idSensor))
				.setBlockName("PCloSensorRanged")
				.setHardness(0.35F)
				.setResistance(15.0F));


		gateOff = new PClo_BlockGate(cfg().getInteger(pk_idGateOff), false)
				.setBlockName("PCloLogicGate")
				.setHardness(0.35F).setLightValue(0)
				.setStepSound(Block.soundWoodFootstep)
				.disableStats().setRequiresSelfNotify()
				.setResistance(15.0F);


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
				.setResistance(15.0F);

		pulsar = new PClo_BlockPulsar(cfg().getInteger(pk_idPulsar))
				.setHardness(0.8F)
				.setResistance(20.0F)
				.setBlockName("PCloRedstonePulsar")
				.setRequiresSelfNotify()
				.setStepSound(Block.soundWoodFootstep);

		lightOff = new PClo_BlockLight(cfg().getInteger(pk_idLightOff), false)
				.setHardness(0.3F)
				.setResistance(5.0F)
				.setBlockName("PCloLight")
				.setStepSound(Block.soundStoneFootstep)
				.setRequiresSelfNotify();

		lightOn = new PClo_BlockLight(cfg().getInteger(pk_idLightOn), true)
				.setHardness(0.3F)
				.setResistance(5.0F)
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
	}

	@Override
	public void setTextures() {
		portableTx.setIconIndex(ModLoader.addOverride("/gui/items.png", getImgDir() + "portable.png"));
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



		map.put("tile.PCloWeasel.name", "Weasel Device");
		
		map.put("tile.PCloWeasel.core.name", "Weasel Controller");
		map.put("tile.PCloWeasel.port.name", "Wireless Port");
		map.put("pc.weasel.core.desc", "programmable chip");
		map.put("pc.weasel.port.desc", "expansion redstone port");
		
		map.put("pc.weasel.activatorGetNetwork", "Network \"%s\" assigned to activation crystal.");
		map.put("pc.weasel.activatorSetNetwork", "Device connected to network \"%s\".");
		
		map.put("pc.gui.weasel.core.program", "Program");
		map.put("pc.gui.weasel.core.settings", "Settings");
		map.put("pc.gui.weasel.core.status", "Status");
		
		map.put("pc.gui.weasel.core.runningLabel", "Running:");
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
		map.put("pc.gui.weasel.core.save", "Save");
		map.put("pc.gui.weasel.core.check", "Check");
		map.put("pc.gui.weasel.core.launch", "Launch");
		map.put("pc.gui.weasel.core.title", "Weasel Controller");
		map.put("pc.gui.weasel.core.yes", "Yes");
		map.put("pc.gui.weasel.core.no", "No");
		map.put("pc.gui.weasel.core.rename", "Rename");
		map.put("pc.gui.weasel.core.colorChange", "Change");
		map.put("pc.gui.weasel.core.colorSave", "Save");
		
		map.put("pc.gui.weasel.core.errNetworkNameTooShort", "Entered network name is too short.");
		map.put("pc.gui.weasel.core.errNetworkNameAlreadyUsed", "Network with this name already exists.");
		map.put("pc.gui.weasel.core.networkRenamed", "Network renamed to %s.");
		map.put("pc.gui.weasel.core.networkColorChanged", "Network color changed.");

		map.put("pc.radioRemote.connected", "Portable transmitter connected to channel \"%s\".");
		map.put("pc.radioRemote.desc", "Channel: %s");


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
		map.put("pc.gui.radio.errChannel", "Invalid channel name.");
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
				new ItemStack(weaselDevice, 1, PClo_WeaselType.CORE),
				new Object[] { "SRS", "RCR", "SRS",
					'S', Block.stone, 'R', Item.redstone, 'C', mod_PCcore.powerCrystal });

		
		// *** lights ***

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 0),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 0) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 1),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 1) });
		
		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 2),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 2) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 3),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 3) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 4),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 4) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 5),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 5) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 6),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 6) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 7),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 7) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 8),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 8) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 9),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 9) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 10),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 10) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 11),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 11) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 12),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 12) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 13),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 13) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 14),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 14) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 15),
				new Object[] { Item.redstone, Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 15) });
		
		// STABLE

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 0 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 0) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 1 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 1) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 2 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 2) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 3 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 3) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 4 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 4) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 5 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 5) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 6 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 6) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 7 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 7) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 8 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 8) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 9 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 9) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 10 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 10) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 11 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 11) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 12 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 12) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 13 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 13) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 14 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 14) });

		ModLoader.addShapelessRecipe(
				new ItemStack(lightOn, 1, 15 + 16),
				new Object[] { Item.lightStoneDust, new ItemStack(Item.dyePowder, 1, 15) });
		
		
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
		PC_InveditManager.setDamageRange(lightOn.blockID, 0, 31);
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
		return false;
	}

	@Override
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int rtype) {
		PClo_Renderer.renderInvBlockByType(renderblocks, block, i, rtype);
	}

	private int tickCounter = 0;

	@Override
	public boolean onTickInGame(float f, Minecraft minecraft) {
		if (tickCounter++ % 20 == 0) {
			if (NETWORK.needsSave) {
				NETWORK.saveToFile();
			}
		}
		return true;
	}

	@Override
	public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen guiscreen) {
		if (NETWORK.needsSave) {
			NETWORK.saveToFile();
		}
		return true;
	}
}
