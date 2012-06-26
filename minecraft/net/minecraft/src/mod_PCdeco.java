package net.minecraft.src;

import java.util.List;
import java.util.Map;

/**
 * Decoration module for PowerCraft<br>
 * Adds iron frame block and it's subtypes (redstone storage, metal plates etc.)
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
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
	public static Block deco;
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
		list.add(new PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>(PCde_TileEntityWalkable.class,
				"PCdeWalkableBlock", new PCde_TileEntityWalkableRenderer()));
	}

	@Override
	public void registerBlockRenderers() {
		PCde_Renderer.decorativeBlockRenderer = ModLoader.getUniqueBlockModelID(this, true);
		PCde_Renderer.walkableBlockRenderer = ModLoader.getUniqueBlockModelID(this, true);
	}

	@Override
	public void registerBlocks(List<Block> list) {
		// @formatter:off
		deco = new PCde_BlockDeco(cfg().getInteger(pk_idDecoBlockSolid), 22, Material.rock)
				.setHardness(1.5F)
				.setResistance(5.0F)
				.setBlockName("PCdeDecoBlock")
				.setStepSound(Block.soundMetalFootstep);
		
		walkable = new PCde_BlockWalkable(cfg().getInteger(pk_idDecoBlockNonsolid), 22, Material.rock)
		.setHardness(1.5F)
		.setResistance(5.0F)
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
		map.put("tile.PCdeWalkableBlock.0.name", "Iron Ledge");
		map.put("tile.PCdeWalkableBlock.1.name", "Iron Stairs");
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
		
		ModLoader.addShapelessRecipe(
				new ItemStack(Item.redstone, 9, 0),
				new Object[] { new ItemStack(deco, 32, 1) });
		
		
		ModLoader.addRecipe(
				new ItemStack(walkable, 16, 0),
				new Object[] { "X  ", "X  ", "XXX",
					'X', Item.ingotIron });
		
		ModLoader.addRecipe(
				new ItemStack(walkable, 8, 1),
				new Object[] { "X ", " X",
					'X', new ItemStack(walkable, 1, 0) });		
		
		//@formatter:on
	}

	@Override
	public void postInit() {
		PC_InveditManager.setDamageRange(deco.blockID, 0, 2);
		PC_InveditManager.setDamageRange(walkable.blockID, 0, 1);
		PC_InveditManager.setItemCategory(deco.blockID, "Decorative");
		PC_InveditManager.setItemCategory(walkable.blockID, "Decorative");

		//@formatter:off
		
		addStacksToCraftingTool(
				PC_CraftingToolGroup.DECORATIVE,
				new ItemStack(deco,1,0),
				new ItemStack(deco,1,1),
				new ItemStack(walkable,1,0),
				new ItemStack(walkable,1,1)
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

}
