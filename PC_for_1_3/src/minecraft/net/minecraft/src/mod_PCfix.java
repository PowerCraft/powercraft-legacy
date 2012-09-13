package net.minecraft.src;


import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * RADIO FIX module - updates radios built before 3.4 to the new format.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class mod_PCfix extends PC_Module {

	@Override
	public String getVersion() {
		return mod_PCcore.VERSION;
	}

	@Override
	public String getModuleName() {
		return "FIX";
	}

	@Override
	public String getPriorities() {
		return "before:mod_PCcore";
	}

	// fake radios

	@SuppressWarnings("javadoc")
	public static Block radio0, radio1;

	// *** MODULE INIT ***

	@Override
	public void preInit() {

		PC_Logger.fine("Starting Radio Migration module. After you fix all your radios, remove this module.");

	}

	private PC_PropertyManager cfg;

	@Override
	public void initProperties(PC_PropertyManager conf) {

		cfg = new PC_PropertyManager("/config/PC_WIRELESS.properties", "Obsolete radios config file.");
		cfg.putBlock("block_Receiver", 154);
		cfg.putBlock("block_Transceiver", 153);

		cfg.apply();

	}

	@Override
	public void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {}

	@Override
	public void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {
		list.add(new PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>(PCfix_TileEntityRadioPlaceholder.class,
				"PCfixRadioPlaceholder", null));
	}

	@Override
	public void registerBlockRenderers() {}

	@Override
	public void registerBlocks(List<Block> list) {

		radio0 = new PCfix_BlockRadioPlaceholder(cfg.num("block_Receiver"), 0);
		radio1 = new PCfix_BlockRadioPlaceholder(cfg.num("block_Transceiver"), 1);

		list.add(radio0);
		list.add(radio1);

	}

	@Override
	public void registerItems() {}

	@Override
	public void preloadTextures(List<String> list) {}

	@Override
	public void setTextures() {}

	@Override
	public void setNames(Map<Object, String> map) {}

	@Override
	public void addRecipes() {}

	@Override
	public void postInit() {}

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

}
