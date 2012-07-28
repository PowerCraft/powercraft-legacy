package net.minecraft.src;


import java.util.List;
import java.util.Map;


/**
 * Transport PowerCraft module
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class mod_PCtransport extends PC_Module {

	/** Module instance */
	public static mod_PCtransport instance;

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
		return "/PowerCraft/transport/";
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
		return "TRANSPORT";
	}



	// *** PROPERTIES ***

	private static final String pk_belt = "id.block.conveyor_belt";
	private static final String pk_ejector = "id.block.ejection_belt";
	private static final String pk_detector = "id.block.detection_belt";
	private static final String pk_separator = "id.block.separation_belt";
	private static final String pk_brake = "id.block.brake_belt";
	private static final String pk_redirector = "id.block.redirection_belt";
	private static final String pk_elevator = "id.block.elevator";
	private static final String pk_speedybelt = "id.block.speedy_belt";
	private static final String pk_teleporter = "id.block.teleporter";

	private static final String pk_teleporter_brightness = "brightness.teleporter";

	private static final String pk_idSlimeBoots = "id.item.stickyBoots";
	private static final String pk_optHackedWater = "opt.hack.improveWater";


	/** Allow hacking water */
	private boolean optHackWater;



	// *** BLOCKS & ITEMS ***
	/** standard gray conveyor belt */
	public static Block conveyorBelt;

	/** Item ejection belt */
	public static Block ejectionBelt;

	/** Item / entity detection belt */
	public static Block detectionBelt;

	/** item separation belt */
	public static Block separationBelt;

	/** Brake belt */
	public static Block brakeBelt;

	/** Item redirection belt */
	public static Block redirectionBelt;

	/** Item elevator (meta=1 for descender) */
	public static Block itemElevator;

	/** Speedy belt */
	public static Block speedyBelt;

	/** entity teleporter block */
	public static Block teleporter;

	/** Slime Boots */
	public static Item slimeboots;


	// *** MODULE INIT ***

	@Override
	public void preInit() {}

	@Override
	public void initProperties(PC_PropertyManager conf) {

		conf.putBlock(pk_belt, 212);
		conf.putBlock(pk_ejector, 213);
		conf.putBlock(pk_detector, 214);
		conf.putBlock(pk_separator, 215);
		conf.putBlock(pk_brake, 216);
		conf.putBlock(pk_redirector, 218);
		conf.putBlock(pk_elevator, 219);
		conf.putBlock(pk_speedybelt, 220);
		conf.putBlock(pk_teleporter, 235);
		conf.putInteger(pk_teleporter_brightness, 5, "Teleporter block brightness, scale 0-15.");
		conf.putBoolean(pk_optHackedWater, true, "Modify water to store items to chests and work seamlessly with conveyors.");

		conf.putBlock(pk_idSlimeBoots, 19005);

		conf.apply();
		optHackWater = conf.flag(pk_optHackedWater);
	}

	@Override
	public void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {
		list.add(new PC_Struct3(PCtr_TileEntitySeparationBelt.class, "FCConveyorFilter", null));
		list.add(new PC_Struct3(PCtr_TileEntityRedirectionBelt.class, "PCConveyorRedirector", null));
		list.add(new PC_Struct3(PCtr_TileEntityEjectionBelt.class, "PCConveyorEjector", null));
		list.add(new PC_Struct3(PCtr_TileEntityTeleporter.class, "PCteleporter", new PCtr_TileEntityTeleporterRenderer()));
	}

	@Override
	public void registerBlockRenderers() {
		PCtr_Renderer.teleporterRenderer = ModLoader.getUniqueBlockModelID(this, true);
	}

	@Override
	public void registerBlocks(List<Block> list) {

		//@formatter:off
		
		conveyorBelt = new PCtr_BlockBeltNormal(cfg().getInteger(pk_belt), false)
				.setHardness(0.22F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorBelt")
				.setStepSound(Block.soundMetalFootstep);

		ejectionBelt = new PCtr_BlockBeltEjector(cfg().getInteger(pk_ejector))
				.setHardness(0.22F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorFurnace")
				.setStepSound(Block.soundMetalFootstep);

		detectionBelt = new PCtr_BlockBeltDetector(cfg().getInteger(pk_detector))
				.setHardness(0.22F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorDetector")
				.setStepSound(Block.soundMetalFootstep);

		separationBelt = new PCtr_BlockBeltSeparator(cfg().getInteger(pk_separator))
				.setHardness(0.22F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorFilter")
				.setStepSound(Block.soundMetalFootstep);

		brakeBelt = new PCtr_BlockBeltBrake(cfg().getInteger(pk_brake))
				.setHardness(0.22F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorBrake")
				.setStepSound(Block.soundMetalFootstep);

		redirectionBelt = new PCtr_BlockBeltRedirector(cfg().getInteger(pk_redirector))
				.setHardness(0.22F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorRedirector")
				.setStepSound(Block.soundMetalFootstep);

		itemElevator = new PCtr_BlockItemElevator(cfg().getInteger(pk_elevator), 23)
				.setHardness(0.5F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorItemElevator")
				.setStepSound(Block.soundMetalFootstep);

		speedyBelt = new PCtr_BlockBeltNormal(cfg().getInteger(pk_speedybelt), true)
				.setHardness(0.22F)
				.setResistance(8.0F)
				.setBlockName("PCconveyorSpeedBelt")
				.setStepSound(Block.soundMetalFootstep);

		teleporter = new PCtr_BlockTeleporter(cfg().getInteger(pk_teleporter), 14, Material.portal)
				.setHardness(1.0F)
				.setResistance(8.0F)
				.setLightValue(cfg().getInteger(pk_teleporter_brightness) * 0.0625F)
				.setBlockName("PCteleporter")
				.setStepSound(Block.soundMetalFootstep);
		
		//@formatter:on

		if (optHackWater) {
			// The super dirty way how to get the new, amazing hacked water
			Block.blocksList[Block.waterStill.blockID] = null;
			Block.blocksList[Block.waterStill.blockID] = new PCtr_BlockHackedWater(9, Material.water).setHardness(100F).setLightOpacity(3)
					.setBlockName("water").disableStats().setRequiresSelfNotify();
		}

		list.add(conveyorBelt);
		list.add(ejectionBelt);
		list.add(detectionBelt);
		list.add(separationBelt);
		list.add(brakeBelt);
		list.add(redirectionBelt);
		list.add(itemElevator);
		list.add(speedyBelt);
		list.add(teleporter);
	}

	@Override
	public void registerItems() {
		removeBlockItem(itemElevator.blockID);
		setBlockItem(itemElevator.blockID, new PCtr_ItemBlockElevator(itemElevator.blockID - 256));

		int armour_index = ModLoader.addArmor("pcslime");
		slimeboots = new PCtr_ItemStickyBoots(cfg().num(pk_idSlimeBoots), armour_index).setIconIndex(Item.bootsSteel.iconIndex).setItemName(
				"PCtrStickyIronBoots");
	}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getTerrainFile());
	}

	@Override
	public void setTextures() {
		// these textures are used to generate particles, the real textures are defined in the blocks themselves.
		conveyorBelt.blockIndexInTexture = 1;
		ejectionBelt.blockIndexInTexture = 1;
		detectionBelt.blockIndexInTexture = 1;
		separationBelt.blockIndexInTexture = 1;
		brakeBelt.blockIndexInTexture = 1;
		redirectionBelt.blockIndexInTexture = 1;
		itemElevator.blockIndexInTexture = 23;
		speedyBelt.blockIndexInTexture = 1;
		teleporter.blockIndexInTexture = 14;
	}

	@Override
	public void setNames(Map<Object, String> map) {
		map.put(conveyorBelt, "Conveyor Belt");
		map.put(speedyBelt, "Speedy Conveyor Belt");
		map.put(ejectionBelt, "Item Ejection Belt");
		map.put(detectionBelt, "Item Detector Belt");
		map.put(separationBelt, "Conveyor Item Separator");
		map.put(brakeBelt, "Brake Conveyor");
		map.put(redirectionBelt, "Item Redirection Belt");
		map.put(teleporter, "Teleporter");
		map.put(slimeboots, "Sticky Iron Boots");

		map.put("tile.PCconveyorItemElevator.up.name", "Item Elevator (up)");
		map.put("tile.PCconveyorItemElevator.down.name", "Item Descender (down)");
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

		map.put("pc.gui.ejector.modeEjectTitle", "Ejection mode:");
		map.put("pc.gui.ejector.modeStacks", "Whole stacks");
		map.put("pc.gui.ejector.modeItems", "Single items");
		map.put("pc.gui.ejector.modeAll", "All contents at once");

		map.put("pc.gui.ejector.modeSelectTitle", "Method of selection:");
		map.put("pc.gui.ejector.modeSelectFirst", "First slot");
		map.put("pc.gui.ejector.modeSelectLast", "Last slot");
		map.put("pc.gui.ejector.modeSelectRandom", "Random slot");


		map.put("pc.gui.separationBelt.group", "Ignore subtypes of");
		map.put("pc.gui.separationBelt.groupLogs", "Logs");
		map.put("pc.gui.separationBelt.groupPlanks", "Planks");
		map.put("pc.gui.separationBelt.groupAll", "All");


	}

	@Override
	public void addRecipes() {
		//@formatter:off
		
		ModLoader.addRecipe(
				new ItemStack(conveyorBelt, 16),
				new Object[] { "XXX", "YRY",
					'X', Item.leather, 'Y', Item.ingotIron, 'R', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(conveyorBelt, 4),
				new Object[] { "XXX", "YRY",
					'X', Item.paper, 'Y', Item.ingotIron, 'R', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(speedyBelt, 16),
				new Object[] { "XXX", "YRY",
					'X', Item.leather, 'Y', Item.ingotGold, 'R', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(speedyBelt, 4),
				new Object[] { "XXX", "YRY",
					'X', Item.paper, 'Y', Item.ingotGold, 'R', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(ejectionBelt, 1),
				new Object[] { "X", "Y", "Z",
					'X', Item.bow, 'Y', conveyorBelt, 'Z', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(detectionBelt, 1),
				new Object[] { "X", "Y", "Z",
					'X', Block.pressurePlatePlanks, 'Y', conveyorBelt, 'Z', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(detectionBelt, 1),
				new Object[] { "X", "Y", "Z",
					'X', Block.pressurePlateStone, 'Y', conveyorBelt, 'Z', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(separationBelt, 1),
				new Object[] { "X", "Y", "Z",
					'X', Item.diamond, 'Y', conveyorBelt, 'Z', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(brakeBelt, 1),
				new Object[] { "X", "Y", "Z",
					'X', Item.ingotIron, 'Y', conveyorBelt, 'Z', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(redirectionBelt, 1),
				new Object[] { "X", "Y",
					'X', conveyorBelt, 'Y', Item.redstone });

		ModLoader.addRecipe(
				new ItemStack(itemElevator, 6, 0),
				new Object[] { "XGX", "X X", "XGX",
					'X', conveyorBelt, 'G', Item.ingotGold });

		ModLoader.addRecipe(
				new ItemStack(itemElevator, 6, 1),
				new Object[] { "XGX", "XRX", "XGX",
					'X', conveyorBelt, 'G', Item.ingotGold, 'R', Item.redstone });
		
		ModLoader.addRecipe(new ItemStack(slimeboots), new Object[] {"B","S",'B', Item.bootsSteel,'S',Item.slimeBall});
		
		// PRISM RECIPE is added in ModulesLoaded method.
		
		//@formatter:on
	}

	@Override
	public void postInit() {
		PC_InveditManager.setDamageRange(itemElevator.blockID, 0, 1);

		String ctg = "Transportation";

		PC_InveditManager.setItemCategory(conveyorBelt.blockID, ctg);
		PC_InveditManager.setItemCategory(ejectionBelt.blockID, ctg);
		PC_InveditManager.setItemCategory(detectionBelt.blockID, ctg);
		PC_InveditManager.setItemCategory(redirectionBelt.blockID, ctg);
		PC_InveditManager.setItemCategory(brakeBelt.blockID, ctg);
		PC_InveditManager.setItemCategory(separationBelt.blockID, ctg);
		PC_InveditManager.setItemCategory(speedyBelt.blockID, ctg);
		PC_InveditManager.setItemCategory(itemElevator.blockID, ctg);
		PC_InveditManager.setItemCategory(teleporter.blockID, ctg);
		PC_InveditManager.setItemCategory(slimeboots.shiftedIndex, ctg);

		// @formatter:off
		addStacksToCraftingTool(
				PC_ItemGroup.TRANSPORT,
				new ItemStack(conveyorBelt),
				new ItemStack(speedyBelt),
				new ItemStack(ejectionBelt),
				new ItemStack(detectionBelt),
				new ItemStack(redirectionBelt),
				new ItemStack(brakeBelt),
				new ItemStack(separationBelt),
				new ItemStack(itemElevator,1,0),
				new ItemStack(itemElevator,1,1),
				new ItemStack(teleporter)
			);
		
		addStacksToCraftingTool(PC_ItemGroup.ARMOR, slimeboots);
		// @formatter:on
	}

	@Override
	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int renderType) {
		return PCtr_Renderer.renderBlockByType(renderblocks, iblockaccess, i, j, k, block, renderType);
	}

	@Override
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int rtype) {
		PCtr_Renderer.renderInvBlockByType(renderblocks, block, i, rtype);
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

}
