package net.minecraft.src;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.minecraft.src.PCnt.*;

public class mod_PCnt extends PC_Module {

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


	private static final String pk_teleporter = "id.block.teleporter";
	private static final String pk_teleporter_brightness = "brightness.teleporter";
	


	/** entity teleporter block */
	public static Block teleporter;
	
	@Override
	public void preInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initProperties(PC_PropertyManager conf) {
		conf.putBlock(pk_teleporter, 235);
		conf.putInteger(pk_teleporter_brightness, 5, "Teleporter block brightness, scale 0-15.");
		conf.apply();
	}

	@Override
	public void registerEntities(
			List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerTileEntities(
			List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {

		list.add(new PC_Struct3(PCnt_TileEntityTeleporter.class, "PCteleporter", new PCnt_TileEntityTeleporterRenderer()));

	}

	@Override
	public void registerBlockRenderers() {
		PCnt_Renderer.teleporterRenderer = ModLoader.getUniqueBlockModelID(this, true);

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
		
		list.add(teleporter);
	}

	@Override
	public void registerItems() {
		// TODO Auto-generated method stub

	}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getTerrainFile());

	}

	@Override
	public void setTextures() {
		teleporter.blockIndexInTexture = 14;

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
	}

	@Override
	protected List<Class> addGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Hashtable<String, PC_PacketHandler> addPacketHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Hashtable<String, PC_INBTWD> addNetManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postInit() {
		String ctg = "Networking";

		PC_InveditManager.setItemCategory(teleporter.blockID, ctg);
		
	
		addStacksToCraftingTool(PC_ItemGroup.NETWORK, new ItemStack(teleporter));

	}

}
