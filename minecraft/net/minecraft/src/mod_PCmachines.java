package net.minecraft.src;

import java.util.List;
import java.util.Map;

/**
 * Machines PowerCraft module
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class mod_PCmachines extends PC_Module implements PC_IActivatorListener {

	/** Module instance */
	public static mod_PCmachines instance;

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
		return "/PowerCraft/machines/";
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
		return "MACHINES";
	}



	// *** PROPERTIES ***

	/** block ids ignored by roaster */
	public static List<Integer> roasterIgnoreBlockIDs;

	/** flag for improved dispensers */
	private static boolean dispenseBuckets = true, dispenseWheat = true, dispenseFood = false, dispenseBowls = true;

	private static final String pk_idHarvester = "id.block.harvester";
	private static final String pk_idBuilder = "id.block.builder";
	private static final String pk_idRoaster = "id.block.roaster";
	private static final String pk_idLaser = "id.block.laser";
	private static final String pk_idXpBank = "id.block.XP_bank";
	private static final String pk_idOptical = "id.block.optical";
	private static final String pk_idACT = "id.block.automatic_workbench";
	private static final String pk_idReplacer = "id.block.replacer";
	private static final String pk_optNoSmeltList = "opt.roaster.ignored_blocks_list";
	private static final String pk_optDispenseBuckets = "opt.dispenser.throw_buckets";
	private static final String pk_optDispenseWheat = "opt.dispenser.throw_wheat";
	private static final String pk_optDispenseFood = "opt.dispenser.throw_all_food";
	private static final String pk_optDispenseBowls = "opt.dispenser.throw_bowls";
	private static final String pk_optAltPrismRecipe = "opt.prism.alternate_recipe";



	// *** BLOCKS & ITEMS ***
	//@formatter:off
	/** block harvester machine */
	public static Block harvester;

	/** block placing machine */
	public static Block builder;

	/** instant furnace */
	public static Block roaster;

	/** laser device, tile entity sub-types */
	public static PCma_BlockLaser laser;

	/** mirror or prism, tile entity sub-types */
	public static Block optical;

	/** XP storage block */
	public static PCma_BlockXPBank xpbank;
	

	/** Automatic workbench block */
	public static Block automaticWorkbench;
	
	/**block replacer machine*/
	public static Block replacer;
	
	//@formatter:on



	// *** MODULE INIT ***

	@Override
	public void preInit() {
		instance = this;
	}

	@Override
	public void initProperties(PC_PropertyManager conf) {

		conf.putBlock(pk_idHarvester, 221);
		conf.putBlock(pk_idBuilder, 222);
		conf.putBlock(pk_idRoaster, 228);
		conf.putBlock(pk_idLaser, 230);
		conf.putBlock(pk_idXpBank, 231);
		conf.putBlock(pk_idOptical, 234);
		conf.putBlock(pk_idACT, 217);
		conf.putBlock(pk_idReplacer, 238);
		conf.putString(pk_optNoSmeltList, "1", "IDs of items ignored by roaster (prevents smelting)");

		conf.putBoolean(pk_optDispenseBuckets, true, "Thrown buckets can milk cows.");
		conf.putBoolean(pk_optDispenseBowls, true, "Thrown bowls can get soup from mushroom cow.");
		conf.putBoolean(pk_optDispenseWheat, true, "Thrown wheat can put animals into 'love' mode.");
		conf.putBoolean(pk_optDispenseFood, false, "Thrown meat can heal wolves etc.");
		conf.putBoolean(pk_optAltPrismRecipe, false, "Use \"+\" shaped glass recipe instead of the 4-block one.\n"
				+ "Needed for compatibility with MoCreatures.");

		conf.apply();

		roasterIgnoreBlockIDs = PC_Utils.parseIntList(conf.getString(pk_optNoSmeltList));

		dispenseBuckets = conf.getBoolean(pk_optDispenseBuckets);
		dispenseWheat = conf.getBoolean(pk_optDispenseWheat);
		dispenseFood = conf.getBoolean(pk_optDispenseFood);
		dispenseBowls = conf.getBoolean(pk_optDispenseBowls);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {
		list.add(new PC_Struct3(PCma_EntityThrownBucket.class, "PCma_ThrownBucket", ModLoader.getUniqueEntityId()));
		list.add(new PC_Struct3(PCma_EntityThrownFood.class, "PCma_ThrownFood", ModLoader.getUniqueEntityId()));
		list.add(new PC_Struct3(PCma_EntityFishingMachine.class, "PCmo_FishingMachine", ModLoader.getUniqueEntityId()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {
		list.add(new PC_Struct3(PCma_TileEntityReplacer.class, "PCReplacer", null));
		list.add(new PC_Struct3(PCma_TileEntityBlockBuilder.class, "FCBlockPlacer", null));
		list.add(new PC_Struct3(PCma_TileEntityRoaster.class, "FCRoaster", null));
		list.add(new PC_Struct3(PCma_TileEntityLaser.class, "PCLaser", new PCma_TileEntityLaserRenderer()));
		list.add(new PC_Struct3(PCma_TileEntityXPBank.class, "PCXPBank", null));
		list.add(new PC_Struct3(PCma_TileEntityOptical.class, "PCLaserMirror", new PCma_TileEntityOpticalRenderer()));
		list.add(new PC_Struct3(PCma_TileEntityAutomaticWorkbench.class, "FCConveyorACT", null));

	}

	@Override
	public void registerBlockRenderers() {
		PCma_Renderer.laserRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PCma_Renderer.xpbankRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PCma_Renderer.opticalRenderer = ModLoader.getUniqueBlockModelID(this, true);
	}

	@Override
	public void registerBlocks(List<Block> list) {
		//@formatter:off
		
		harvester = new PCma_BlockHarvester(cfg().getInteger(pk_idHarvester))
				.setHardness(0.7F)
				.setResistance(10.0F)
				.setBlockName("PCmaHarvester")
				.setStepSound(Block.soundStoneFootstep);

		builder = new PCma_BlockBlockBuilder(cfg().getInteger(pk_idBuilder))
				.setHardness(0.7F)
				.setResistance(10.0F)
				.setBlockName("PCmaBlockBuilder")
				.setStepSound(Block.soundStoneFootstep);

		roaster = new PCma_BlockRoaster(cfg().getInteger(pk_idRoaster))
				.setHardness(0.7F)
				.setResistance(10.0F)
				.setBlockName("PCmaRoaster")
				.setStepSound(Block.soundStoneFootstep);

		laser = (PCma_BlockLaser) (new PCma_BlockLaser(cfg().getInteger(pk_idLaser)))
				.setHardness(0.7F)
				.setResistance(10.0F)
				.setBlockName("PCmaLaserBlock")
				.setStepSound(Block.soundStoneFootstep);

		optical = new PCma_BlockOptical(cfg().getInteger(pk_idOptical))
				.setHardness(1.0F)
				.setResistance(4.0F)
				.setBlockName("PCmaOptical")
				.setStepSound(Block.soundStoneFootstep);

		xpbank = (PCma_BlockXPBank) (new PCma_BlockXPBank(cfg().getInteger(pk_idXpBank)))
				.setHardness(6.0F)
				.setResistance(100.0F)
				.setBlockName("PCmaXPBank")
				.setLightValue(0.5F)
				.setStepSound(Block.soundStoneFootstep);		

		/** Automatic workbench block */
		automaticWorkbench = new PCma_BlockAutomaticWorkbench(cfg().getInteger(pk_idACT))
				.setHardness(0.7F)
				.setResistance(10.0F)
				.setBlockName("PCmaAutoWorkbench")
				.setStepSound(Block.soundMetalFootstep);
		
		replacer = new PCma_BlockReplacer(cfg().getInteger(pk_idReplacer))
				.setHardness(0.7F)
				.setResistance(10.0F)
				.setBlockName("PCmaReplacer")
				.setStepSound(Block.soundStoneFootstep);
		
		list.add(harvester);
		list.add(builder);
		list.add(roaster);
		list.add(laser);
		list.add(xpbank);
		list.add(optical);
		list.add(automaticWorkbench);
		list.add(replacer);
		
		//@formatter:on
	}

	@Override
	public void registerItems() {

		removeBlockItem(optical.blockID);
		setBlockItem(optical.blockID, new PCma_ItemBlockOptical(optical.blockID - 256));
	}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getTerrainFile());
		list.add(getImgDir() + "mirror.png");
		list.add(getImgDir() + "laser.png");
		list.add(getImgDir() + "fisher.png");
	}

	@Override
	public void setTextures() {
		// set default textures, sometimes used for particles
		harvester.blockIndexInTexture = 109;
		builder.blockIndexInTexture = 109;
		laser.blockIndexInTexture = 16;
		roaster.blockIndexInTexture = 62;
		optical.blockIndexInTexture = 67;
		xpbank.blockIndexInTexture = 37;
		automaticWorkbench.blockIndexInTexture = 109;
		replacer.blockIndexInTexture = 109;
	}

	@Override
	public void setNames(Map<Object, String> map) {
		map.put(replacer, "Block Replacer");
		map.put(harvester, "Block Harvester");
		map.put(builder, "Block Dispenser");
		map.put(roaster, "Roaster");
		map.put(laser, "Laser");
		map.put(xpbank, "Experience Storage");
		map.put(automaticWorkbench, "Automatic Workbench");

		map.put("tile.PCmaOptical.mirror.name", "Laser Mirror");
		map.put("tile.PCmaOptical.prism.name", "Laser Prism");

		map.put("pc.fisher.errWater", "Not enough water!");
		map.put("pc.fisher.errStructure", "Not a valid Fish Machine structure!");
		map.put("pc.fisher.errClickedPlanks", "Fence, not planks!");
		map.put("pc.gui.laserTypeDecide.title", "Select Laser Type");
		map.put("pc.gui.laserTypeDecide.sensor", "Tripwire");
		map.put("pc.gui.laserTypeDecide.redstoneSender", "Transmitter");
		map.put("pc.gui.laserTypeDecide.redstoneReceiver", "Receiver");
		map.put("pc.optical.mirror.desc", "Reflects laser beam");
		map.put("pc.optical.prism.desc", "Splits laser beam");
		map.put("pc.roaster.insertFuel", "fuel");

		map.put("pc.gui.blockReplacer.title", "Block Replacer");
		map.put("pc.gui.blockReplacer.errWrongValue", "Expects a value between -16 and 16.");
		map.put("pc.gui.blockReplacer.err3zeros", "Expects at least 1 value unequal 0.");
		
		map.put("pc.gui.xpbank.storagePoints", "Stored XP:");
		map.put("pc.gui.xpbank.xpUnit", "points");
		map.put("pc.gui.xpbank.xpLevels", "levels");
		map.put("pc.gui.xpbank.pointsToWithdraw", "Points to withdraw:");
		map.put("pc.gui.xpbank.currentPlayerPoints", "Your XP:");
		map.put("pc.gui.xpbank.currentPlayerLevel", "Your level:");
		map.put("pc.gui.xpbank.withdrawButton", "Withdraw");
		map.put("pc.gui.xpbank.pointsToDeposit", "Points to deposit:");
		map.put("pc.gui.xpbank.depositButton", "Deposit");
	}

	@Override
	public void addRecipes() {
		//@formatter:off
		
		ModLoader.addRecipe(
				new ItemStack(builder, 1),
				new Object[] { "G", "D",
					'G', Item.ingotGold, 'D', Block.dispenser });

		ModLoader.addRecipe(
				new ItemStack(harvester, 1),
				new Object[] { "P", "D",
					'P', Item.ingotIron, 'D', Block.dispenser });

		ModLoader.addRecipe(
				new ItemStack(roaster, 1),
				new Object[] { "III", "IFI", "III",
					'I', Item.ingotIron, 'F', Item.flintAndSteel });

		ModLoader.addRecipe(
				new ItemStack(laser, 1),
				new Object[] { " WD", " S ", "SSS",
					'S', Block.stone, 'W', new ItemStack(Block.planks, 1, -1), 'D', Item.diamond });

		ModLoader.addRecipe(
				new ItemStack(laser, 1),
				new Object[] { " WD", " S ", "SSS",
					'S', Block.cobblestone, 'W', new ItemStack(Block.planks, 1, -1), 'D', Item.diamond });

		ModLoader.addRecipe(new ItemStack(xpbank, 1),
				new Object[] { "ODO", "OGO", "O O",
					'O', Block.obsidian, 'D', Block.blockDiamond, 'G', Item.ghastTear });

		ModLoader.addRecipe(
				new ItemStack(optical, 2, 0),
				new Object[] { "GI", " I",	'G', Block.thinGlass, 'I', Item.ingotIron });


		if(cfg().getBoolean(pk_optAltPrismRecipe)){
			ModLoader.addRecipe(
					new ItemStack(optical, 1, 1),
					new Object[] { " G ", "GGG", " G ", 'G', Block.glass });
		}else{
			ModLoader.addRecipe(
					new ItemStack(optical, 1, 1),
					new Object[] { "GG", "GG", 'G', Block.glass });
		}
		

		ModLoader.addRecipe(
				new ItemStack(automaticWorkbench, 1),
				new Object[] { "X", "Y", "Z",
					'X', Item.diamond, 'Y', Block.workbench, 'Z', Item.redstone });
		
		ModLoader.addRecipe(
				new ItemStack(replacer, 1),
				new Object[] { "B", "R", "H",
					'B', builder, 'R', Item.redstone, 'H', harvester});
		
		//@formatter:on
	}

	@Override
	public void postInit() {
		PC_Module.registerActivatorListener(this);

		PC_InveditManager.setItemCategory(harvester.blockID, "Machines");
		PC_InveditManager.setItemCategory(builder.blockID, "Machines");
		PC_InveditManager.setItemCategory(roaster.blockID, "Machines");
		PC_InveditManager.setItemCategory(xpbank.blockID, "Machines");
		PC_InveditManager.setItemCategory(automaticWorkbench.blockID, "Machines");
		PC_InveditManager.setItemCategory(replacer.blockID, "Machines");

		PC_InveditManager.setItemCategory(laser.blockID, "Optical");

		PC_InveditManager.setDamageRange(optical.blockID, 0, 1);
		PC_InveditManager.setItemCategory(optical.blockID, "Optical");

		addStacksToCraftingTool(PC_CraftingToolGroup.MACHINES, new ItemStack(harvester), new ItemStack(builder),
				new ItemStack(replacer), new ItemStack(roaster),
				new ItemStack(xpbank),	new ItemStack(automaticWorkbench));
		addStacksToCraftingTool(PC_CraftingToolGroup.OPTICAL, new ItemStack(laser), new ItemStack(optical, 1, 0), new ItemStack(optical, 1,
				1), new ItemStack(Block.thinGlass));
	}




	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addRenderer(Map map) {
		map.put(PCma_EntityThrownBucket.class, new PCma_RenderThrownItem());
		map.put(PCma_EntityThrownFood.class, new PCma_RenderThrownItem());
		map.put(PCma_EntityFishingMachine.class, new PCma_RenderFishingMachine());
	}

	@Override
	public boolean onActivatorUsedOnBlock(ItemStack stack, EntityPlayer player, World world, PC_CoordI pos) {

		PCma_EntityFishingMachine fisher = new PCma_EntityFishingMachine(world);

		if (!fisher.tryToBuildFishingMachine(stack, player, world, pos)) {
			fisher = null;
			return false;
		}
		return true;

	}

	/**
	 * Dispense some items as thrown objects rather than EntityItem.
	 */
	@Override
	public boolean dispenseEntity(World world, double d, double d1, double d2, int i1, int j1, ItemStack itemstack) {

		if (dispenseBuckets && itemstack.itemID == Item.bucketEmpty.shiftedIndex) {
			PCma_EntityThrownBucket entity = new PCma_EntityThrownBucket(world, d, d1, d2, false);
			entity.setThrowableHeading(i1, 0.1D, j1, 1.1F, 6F);
			world.spawnEntityInWorld(entity);
			world.playAuxSFX(1002, (int) Math.round(d), (int) Math.round(d1), (int) Math.round(d2), 0);
			return true;
		}

		if (dispenseBowls && itemstack.itemID == Item.bowlEmpty.shiftedIndex) {
			PCma_EntityThrownBucket entity = new PCma_EntityThrownBucket(world, d, d1, d2, true);
			entity.setThrowableHeading(i1, 0.1D, j1, 1.1F, 6F);
			world.spawnEntityInWorld(entity);
			world.playAuxSFX(1002, (int) Math.round(d), (int) Math.round(d1), (int) Math.round(d2), 0);
			return true;
		}

		if (dispenseWheat && itemstack.itemID == Item.wheat.shiftedIndex) {
			PCma_EntityThrownFood entity = new PCma_EntityThrownFood(world, d, d1, d2, itemstack.getItem());
			entity.setThrowableHeading(i1, 0.1D, j1, 1.1F, 6F);
			world.spawnEntityInWorld(entity);
			world.playAuxSFX(1002, (int) Math.round(d), (int) Math.round(d1), (int) Math.round(d2), 0);
			return true;
		}

		if (dispenseFood && (itemstack.getItem() instanceof ItemFood || itemstack.getItem() == Item.bone)) {
			PCma_EntityThrownFood entity = new PCma_EntityThrownFood(world, d, d1, d2, itemstack.getItem());
			entity.setThrowableHeading(i1, 0.1D, j1, 1.1F, 6F);
			world.spawnEntityInWorld(entity);
			world.playAuxSFX(1002, (int) Math.round(d), (int) Math.round(d1), (int) Math.round(d2), 0);
			return true;
		}

		return false;
	}


	@Override
	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int renderType) {
		return PCma_Renderer.renderBlockByType(renderblocks, iblockaccess, i, j, k, block, renderType);
	}

	@Override
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int meta, int rtype) {
		PCma_Renderer.renderInvBlockByType(renderblocks, block, meta, rtype);
	}
}
