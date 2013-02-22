package powercraft.mobile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Property;
import powercraft.management.PC_Struct2;
import powercraft.management.recipes.PC_3DRecipe;
import powercraft.management.recipes.PC_IRecipe;
import powercraft.management.registry.PC_KeyRegistry;
import powercraft.management.registry.PC_ModuleRegistry;

public class PCmo_App implements PC_IModule {

	public static PCmo_MinerManager minerManager = new PCmo_MinerManager();
	
	public static final String pk_mForward = "move_forward";
	public static final String pk_mBackward = "move_backward";
	public static final String pk_mLeft = "turn_left";
	public static final String pk_mRight = "turn_right";
	public static final String pk_mAround = "turn_around";
	public static final String pk_mUp = "mine_up";
	public static final String pk_mDown = "mine_down";
	public static final String pk_mBridgeOn = "set_bridge_on";
	public static final String pk_mBridgeOff = "set_bridge_off";
	public static final String pk_mRun = "run_program";
	public static final String pk_mDeposit = "store_to_chest";
	public static final String pk_mToBlocks = "deactivate";
	public static final String pk_mMiningOn = "set_mining_on";
	public static final String pk_mMiningOff = "set_mining_off";
	public static final String pk_mCancel = "reset";
	
	@Override
	public String getName() {
		return "Mobile";
	}

	@Override
	public String getVersion() {
		return "1.0.2";
	}

	@Override
	public void preInit() {
		if(PC_ModuleRegistry.getModule("Weasel")!=null){
			try {
				PCmo_MinerManager.mierBrainClass = (Class<? extends PCmo_MinerBrain>) Class.forName("powercraft.mobile.PCmo_MinerWeaselBrain");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init() {
		
	}

	@Override
	public void postInit() {}

	@Override
	public void initProperties(PC_Property config) {
		PC_KeyRegistry.watchForKey(config, pk_mForward, 0x48); // KEY_NUMPAD8
		PC_KeyRegistry.watchForKey(config, pk_mBackward, 0x50); // KEY_NUMPAD2
		PC_KeyRegistry.watchForKey(config, pk_mLeft, 0x4b); // KEY_NUMPAD4
		PC_KeyRegistry.watchForKey(config, pk_mRight, 0x4d); // KEY_NUMPAD6
		PC_KeyRegistry.watchForKey(config, pk_mAround, 0x4c); // KEY_NUMPAD5
		PC_KeyRegistry.watchForKey(config, pk_mDown, 0x4a); // KEY_SUBTRACT
		PC_KeyRegistry.watchForKey(config, pk_mUp, 0x4e); // KEY_ADD
		
		PC_KeyRegistry.watchForKey(config, pk_mBridgeOn, 0x18); // KEY_O
		PC_KeyRegistry.watchForKey(config, pk_mBridgeOff, 0x19); // KEY_P
		PC_KeyRegistry.watchForKey(config, pk_mRun, 0x9c); // KEY_NUMPADENTER
		
		PC_KeyRegistry.watchForKey(config, pk_mDeposit, 0x53); // KEY_DECIMAL
		PC_KeyRegistry.watchForKey(config, pk_mToBlocks, 0x4f); // KEY_NUMPAD1
		PC_KeyRegistry.watchForKey(config, pk_mMiningOn, 0x47); // KEY_NUMPAD7
		PC_KeyRegistry.watchForKey(config, pk_mMiningOff, 0x49); // KEY_NUMPAD9

		PC_KeyRegistry.watchForKey(config, pk_mCancel, 0xd3); // KEY_DELETE
	}

	@Override
	public List<Class<? extends Entity>> initEntities(
			List<Class<? extends Entity>> entities) {
		entities.add(PCmo_EntityMiner.class);
		return entities;
	}
	
	@Override
	public List<PC_IRecipe> initRecipes(List<PC_IRecipe> recipes) {
		
		recipes.add(new PC_3DRecipe(minerManager,
				new String[]{
				"ss",
				"ss"},
				new String[]{
				"ss",
				"cc"},
				's', Block.blockSteel, 'c', Block.chest));
		
		recipes.add(new PC_3DRecipe(minerManager,
				new String[]{
				"oooo",
				"oooo",
				"oooo",
				"oooo"},
				new String[]{
				"oooo",
				"o  o",
				"o  o",
				"oooo"},
				new String[]{
				"oooo",
				"o  o",
				"o  o",
				"oooo"},
				new String[]{
				"oooo",
				"oooo",
				"oooo",
				"oooo"},
				'o', Block.obsidian));
		
		return recipes;
	}

	@Override
	public List<PC_Struct2<String, PC_IDataHandler>> initDataHandlers(
			List<PC_Struct2<String, PC_IDataHandler>> dataHandlers) {
		return null;
	}

	@Override
	public List<PC_IMSG> initMSGObjects(List<PC_IMSG> msgObjects) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, PC_IPacketHandler>> initPacketHandlers(
			List<PC_Struct2<String, PC_IPacketHandler>> packetHandlers) {
		packetHandlers.add(new PC_Struct2<String, PC_IPacketHandler>("MinerManager", minerManager));
		return packetHandlers;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Miner", PCmo_ContainerMiner.class));
		return guis;
	}
}
