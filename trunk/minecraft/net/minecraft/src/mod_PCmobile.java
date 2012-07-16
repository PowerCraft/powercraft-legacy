package net.minecraft.src;


import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;


/**
 * PowerCraft's Mobile module
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class mod_PCmobile extends PC_Module implements PC_IActivatorListener {

	/** Module instance */
	public static mod_PCmobile instance;

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
		return "/PowerCraft/mobile/";
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
	public String getPriorities() {
		return "after:mod_PCcore";
	}

	@Override
	public String getModuleName() {
		return "MOBILE";
	}


	// *** PROPERTIES ***
	public static final String pk_mForward = "key.miner.move_forward";
	public static final String pk_mBackward = "key.miner.move_backward";
	public static final String pk_mLeft = "key.miner.turn_left";
	public static final String pk_mRight = "key.miner.turn_right";
	public static final String pk_mAround = "key.miner.turn_around";
	public static final String pk_mUp = "key.miner.mine_up";
	public static final String pk_mDown = "key.miner.mine_down";
	public static final String pk_mBridgeOn = "key.miner.set_bridge_on";
	public static final String pk_mBridgeOff = "key.miner.set_bridge_off";
	public static final String pk_mRun = "key.miner.run_program";
	public static final String pk_mDeposit = "key.miner.store_to_chest";
	public static final String pk_mToBlocks = "key.miner.deactivate";
	public static final String pk_mMiningOn = "key.miner.set_mining_on";
	public static final String pk_mMiningOff = "key.miner.set_mining_off";
	public static final String pk_mCancel = "key.miner.reset";



	// *** MODULE INIT ***

	@Override
	public void preInit() {
		instance = this;
	}

	@Override
	public void initProperties(PC_PropertyManager conf) {

		conf.putKey(pk_mForward, Keyboard.KEY_NUMPAD8);
		conf.putKey(pk_mBackward, Keyboard.KEY_NUMPAD2);
		conf.putKey(pk_mLeft, Keyboard.KEY_NUMPAD4);
		conf.putKey(pk_mRight, Keyboard.KEY_NUMPAD6);
		conf.putKey(pk_mAround, Keyboard.KEY_NUMPAD5);
		conf.putKey(pk_mDown, Keyboard.KEY_SUBTRACT);
		conf.putKey(pk_mUp, Keyboard.KEY_ADD);

		conf.putKey(pk_mBridgeOn, Keyboard.KEY_O);
		conf.putKey(pk_mBridgeOff, Keyboard.KEY_P);
		conf.putKey(pk_mRun, Keyboard.KEY_NUMPADENTER);

		conf.putKey(pk_mDeposit, Keyboard.KEY_DECIMAL);
		conf.putKey(pk_mToBlocks, Keyboard.KEY_NUMPAD1);
		conf.putKey(pk_mMiningOn, Keyboard.KEY_NUMPAD7);
		conf.putKey(pk_mMiningOff, Keyboard.KEY_NUMPAD9);

		conf.putKey(pk_mCancel, Keyboard.KEY_DELETE);

		conf.apply();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerEntities(List<PC_Struct3<Class<? extends Entity>, String, Integer>> list) {
		list.add(new PC_Struct3(net.minecraft.src.PCmo_EntityMiner.class, "PCmo_Miner", ModLoader.getUniqueEntityId()));
	}

	@Override
	public void registerTileEntities(List<PC_Struct3<Class<? extends TileEntity>, String, TileEntitySpecialRenderer>> list) {}

	@Override
	public void registerBlockRenderers() {}

	@Override
	public void registerBlocks(List<Block> list) {}

	@Override
	public void registerItems() {}

	@Override
	public void preloadTextures(List<String> list) {
		list.add(getImgDir() + "miner_base.png");
		list.add(getImgDir() + "miner_overlay_1.png");
		list.add(getImgDir() + "miner_overlay_2.png");
		list.add(getImgDir() + "miner_overlay_3.png");
		list.add(getImgDir() + "miner_overlay_4.png");
		list.add(getImgDir() + "miner_overlay_5.png");
		list.add(getImgDir() + "miner_overlay_6.png");
		list.add(getImgDir() + "miner_overlay_7.png");
		list.add(getImgDir() + "miner_overlay_8.png");
		list.add(getImgDir() + "miner_overlay_keyboard.png");
	}

	@Override
	public void setTextures() {}

	@Override
	public void setNames(Map<Object, String> map) {
		map.put("pc.miner.disconnected", "Miner %s disconnected from keyboard.");
		map.put("pc.miner.connected", "Miner %s connected to keyboard.");
		map.put("pc.miner.bridgeOn", "Bridge building enabled.");
		map.put("pc.miner.bridgeOff", "Bridge building disabled.");
		map.put("pc.miner.launchedAll", "Miners launched and disconnected.");
		map.put("pc.miner.miningOn", "Mining enabled.");
		map.put("pc.miner.miningOff", "Mining disabled.");
		map.put("pc.miner.operationsCancelled", "All operations cancelled.");
		map.put("pc.miner.build.errInvalidStructure", "Not a valid Miner's structure!");
		map.put("pc.miner.build.errMissingCrystals", "Put some Power Crystals into the chest!");
		map.put("pc.gui.miner.quit", "Quit");
		map.put("pc.gui.miner.clear", "Clear");
		map.put("pc.gui.miner.run", "RUN!");
		map.put("pc.gui.miner.go", "GO!");
		map.put("pc.gui.miner.reset", "RESET");
		map.put("pc.gui.miner.copy", "copy");
		map.put("pc.gui.miner.paste", "paste");
		map.put("pc.gui.miner.programCode", "Program code:");
		map.put("pc.gui.miner.opt.mining", "Mining");
		map.put("pc.gui.miner.opt.bridge", "Bridge");
		map.put("pc.gui.miner.opt.lavaFill", "Lava filling");
		map.put("pc.gui.miner.opt.waterFill", "Water filling");
		map.put("pc.gui.miner.opt.keepFuel", "Keep all fuel");
		map.put("pc.gui.miner.opt.destroyCobble", "Destroy cobble");
		map.put("pc.gui.miner.opt.destroyGravel", "Destroy gravel");
		map.put("pc.gui.miner.opt.destroyDirt", "Destroy dirt");
		map.put("pc.gui.miner.opt.torchesOnFloor", "Torch > floor");
		map.put("pc.gui.miner.opt.compress", "Compress");
		map.put("pc.gui.miner.title", "Miner Control Interface");
		map.put("pc.miner.chestName", "Miner Cargo Storage");
	}

	@Override
	public void addRecipes() {}

	@Override
	public void postInit() {
		ModLoader.setInGameHook(this, true, true);
		registerActivatorListener(this);
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addRenderer(Map map) {
		map.put(PCmo_EntityMiner.class, new PCmo_RenderMiner());
	}


	@Override
	public boolean onTickInGame(float f, Minecraft minecraft) {
		PCmo_MinerControlHandler.onGameTick();
		return true;
	}

	@Override
	public boolean onActivatorUsedOnBlock(ItemStack stack, EntityPlayer player, World world, PC_CoordI pos) {
		PCmo_EntityMiner miner = new PCmo_EntityMiner(world);

		if (!miner.tryToSpawnMinerAt(stack, player, world, pos)) {
			miner = null;
			return false;
		}
		return false;
	}


}
