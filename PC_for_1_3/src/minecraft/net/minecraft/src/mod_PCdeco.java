package net.minecraft.src;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * Decoration module for PowerCraft<br>
 * Adds iron frame block and it's subtypes (redstone storage, metal plates etc.)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class mod_PCdeco extends PC_Module {

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
		return "/PowerCraft/deco/";
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
		return "DECO";
	}



	// *** PROPERTIES ***

	// property keys
	private static final String pk_idDecoBlockSolid = "id.block.decorative";
	private static final String pk_idDecoBlockNonsolid = "id.block.walkable";



	// *** BLOCKS & ITEMS ***

	/** Decorative block with subtypes - SOLID BLOCKS. */
	public static PCde_BlockDeco deco;
	/** Decorative block with subtypes - NON-SOLID BLOCKS */
	public static Block walkable;



	// *** MODULE INIT ***

	@Override
	public void preInit() {}

	@Override
	public void initProperties(PC_PropertyManager conf) {
		conf.putBlock(pk_idDecoBlockSolid, 233);
		conf.putBlock(pk_idDecoBlockNonsolid, 237);
		conf.apply();
	}

	@Override
	public void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {}

	@Override
	public void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {
		list.add(new PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>(PCde_TileEntityDeco.class, "PCdeDecoBlock",
				new PCde_TileEntityDecoRenderer()));
		list.add(new PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>(PCde_TileEntityWalkable.class, "PCdeWalkableBlock",
				new PCde_TileEntityWalkableRenderer()));
	}

	@Override
	public void registerBlockRenderers() {
		PCde_Renderer.decorativeBlockRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PCde_Renderer.walkableBlockRenderer = ModLoader.getUniqueBlockModelID(this, true);
	}

	@Override
	public void registerBlocks(List<Block> list) {
		// @formatter:off
		deco = (PCde_BlockDeco) new PCde_BlockDeco(cfg().getInteger(pk_idDecoBlockSolid), 22, Material.rock)
				.setHardness(1.5F)
				.setResistance(50.0F)
				.setBlockName("PCdeDecoBlock")
				.setStepSound(Block.soundMetalFootstep);
		
		walkable = new PCde_BlockWalkable(cfg().getInteger(pk_idDecoBlockNonsolid), 22, Material.rock)
		.setHardness(1.5F)
		.setResistance(30.0F)
		.setBlockName("PCdeWalkableBlock")
		.setStepSound(Block.soundMetalFootstep);
		
		list.add(deco);
		list.add(walkable);
		
		// @formatter:on
	}

	@Override
	public void registerItems() {
		removeBlockItem(deco.blockID);
		removeBlockItem(walkable.blockID);
		setBlockItem(deco.blockID, new PCde_ItemBlockDeco(deco.blockID - 256));
		setBlockItem(walkable.blockID, new PCde_ItemBlockWalkable(walkable.blockID - 256));
	}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getTerrainFile());
		list.add(getImgDir() + "block_deco.png");
	}

	@Override
	public void setTextures() {}

	@Override
	public void setNames(Map<Object, String> map) {
		map.put("tile.PCdeDecoBlock.0.name", "Iron Frame");
		map.put("tile.PCdeDecoBlock.1.name", "Redstone Block");
		map.put("tile.PCdeDecoBlock.2.name", "Lightning Conductor");
		map.put("tile.PCdeDecoBlock.3.name", "Transmutation Chamber");
		map.put("tile.PCdeDecoBlock.4.name", "Cobblestone Chimney");
		map.put("tile.PCdeDecoBlock.5.name", "Brick Chimney");
		map.put("tile.PCdeDecoBlock.6.name", "Stone Brick Chimney");
		map.put("tile.PCdeWalkableBlock.0.name", "Iron Ledge");
		map.put("tile.PCdeWalkableBlock.1.name", "Iron Stairs");
		map.put("pc.gui.transmutationChamber.charge", "Charge level:");
	}

	@Override
	public void addRecipes() {
		//@formatter:off
		
		//iron frame
		ModLoader.addRecipe(
				new ItemStack(deco, 32, 0),
				new Object[] { "XXX", "X X", "XXX",
					'X', Item.ingotIron });	
		
		ModLoader.addRecipe(
				new ItemStack(deco, 1, 1),
				new Object[] { "XXX", "XXX", "XXX",
					'X', Item.redstone });
		
		ModLoader.addRecipe(
				new ItemStack(deco, 1, 2),
				new Object[] { " X ", " X ", "XXX",
					'X', Block.blockSteel });
		
		ModLoader.addRecipe(
				new ItemStack(deco, 1, 3),
				new Object[] { "FOF", "OPO", "FOF",
					'F', new ItemStack(deco, 1, 0), 'O', Block.obsidian, 'P', Block.stoneOvenIdle });
		
		ModLoader.addShapelessRecipe(
				new ItemStack(Item.redstone, 9, 0),
				new Object[] { new ItemStack(deco, 32, 1) });
		
		
		ModLoader.addRecipe(
				new ItemStack(walkable, 15, 0),
				new Object[] { "X  ", "X  ", "XXX",
					'X', Item.ingotIron });	
		
		ModLoader.addRecipe(
				new ItemStack(walkable, 15, 1),
				new Object[] { "X  ", "XX ", " XX",
					'X', Item.ingotIron });			

		ModLoader.addShapelessRecipe(
				new ItemStack(Item.ingotIron),
				new Object[] {new ItemStack(walkable, 1, 0),new ItemStack(walkable, 1, 0),new ItemStack(walkable, 1, 0)});
		
		ModLoader.addShapelessRecipe(
				new ItemStack(Item.ingotIron),
				new Object[] {new ItemStack(walkable, 1, 1),new ItemStack(walkable, 1, 1),new ItemStack(walkable, 1, 1)});
		
		// chimneys
		ModLoader.addRecipe(new ItemStack(deco,6,4),new Object[] {"X X", "X X", "X X", Character.valueOf('X'), Block.cobblestone});
		ModLoader.addRecipe(new ItemStack(deco,6,5),new Object[] {"X X", "X X", "X X", Character.valueOf('X'), Block.brick});
		ModLoader.addRecipe(new ItemStack(deco,6,6),new Object[] {"X X", "X X", "X X", Character.valueOf('X'), Block.stoneBrick});
		
		//@formatter:on
	}

	@Override
	public void postInit() {
		PC_InveditManager.setDamageRange(deco.blockID, 0, 3);
		PC_InveditManager.setDamageRange(walkable.blockID, 0, 1);
		PC_InveditManager.setItemCategory(deco.blockID, "Decorative");
		PC_InveditManager.setItemCategory(walkable.blockID, "Decorative");

		//@formatter:off
		
		addStacksToCraftingTool(
				PC_ItemGroup.NON_FUNCTIONAL,
				new ItemStack(deco,1,0),
				new ItemStack(walkable,1,0),
				new ItemStack(walkable,1,1),
				new ItemStack(deco,1,4),
				new ItemStack(deco,1,5),
				new ItemStack(deco,1,6)
			);
		
		// redstone block
		addStacksToCraftingTool(
				PC_ItemGroup.ORES,
				Item.redstone,
				new ItemStack(deco,1,1)
			);
		
		addStacksToCraftingTool(
				PC_ItemGroup.MACHINES,
				new ItemStack(deco,1,2),
				new ItemStack(deco,1,3)
			);
		
		//@formatter:on
	}

	// *** HANDLING RENDERERS ***

	@Override
	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess blockAccess, int i, int j, int k, Block block, int rtype) {
		return PCde_Renderer.renderBlockByType(renderblocks, blockAccess, i, j, k, block, rtype);
	}

	@Override
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int rtype) {
		PCde_Renderer.renderInvBlockByType(renderblocks, block, i, rtype);
	}

	@Override
	public Hashtable<String, PC_IGresGuiCaller> addGui() {
		Hashtable<String, PC_IGresGuiCaller> guis = new Hashtable<String, PC_IGresGuiCaller>();
		guis.put("Transmutator", new PCde_GuiCallerTransmutator());
		return null;
	}


}
