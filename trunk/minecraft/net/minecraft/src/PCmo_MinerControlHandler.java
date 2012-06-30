package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;


/**
 * Miner keyboard control handler
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCmo_MinerControlHandler {


	/** list of keyboard controlled miners */
	public static List<PCmo_EntityMiner> controlledMiners = new ArrayList<PCmo_EntityMiner>();

	/**
	 * World in which last tick was received. If changes, reset timers and
	 * everything.
	 */
	static World lastTickWorld = null;

	/** cool-down timer for repeated key presses */
	private static int[] keyPressTimer = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	/** number of ticks before the key is accepted again */
	private static final int CooldownTime = 8;

	/**
	 * Remove this Miner from list of keyboard-controlled
	 * 
	 * @param miner the miner entity
	 * @param silent don't show chat message
	 */
	public static void disconnectMinerFromKeyboardControl(PCmo_EntityMiner miner, boolean silent) {
		lastTickWorld = ModLoader.getMinecraftInstance().theWorld;
		if (controlledMiners.contains(miner)) {
			controlledMiners.remove(miner);
		}

		if (!silent) {
			PC_Utils.chatMsg(PC_Lang.tr("pc.miner.disconnected", new String[] { miner.level + "" }), true);
		}
	}

	/**
	 * Send given command to all connected miners<br>
	 * Some miners may reject it.
	 * 
	 * @param cmd command index
	 * @return true if at least one miner accepted it
	 */
	public static boolean sendCommandToMiners(int cmd) {
		boolean flag = false;
		for (PCmo_EntityMiner miner : controlledMiners) {
			if (miner.canReceiveKeyboardCommand()) {
				flag = true;
				miner.receiveKeyboardCommand(cmd);
			}
		}
		return flag;
	}

	/**
	 * Send command sequence to all connected miners
	 * 
	 * @param seq String with command characters
	 * @return true if at least one miner accepted it
	 */
	private static boolean sendSequenceToMiners(String seq) {
		boolean flag = false;
		for (PCmo_EntityMiner miner : controlledMiners) {
			if (miner.canReceiveKeyboardCommand()) {
				flag = true;
				try {
					miner.appendCode(seq);
				} catch (PCmo_CommandException ce) {

					PC_Logger.severe("Error in keyboard-sent command! This is a bug!");
					return false;
				}
			}
		}
		return flag;
	}


	/**
	 * Send urgent command to all connected miners.<br>
	 * Miners will do their best top accept it
	 * 
	 * @param cmd command index
	 * @return true if at least one miner accepted it
	 */
	public static boolean sendUrgentCommandToMiners(int cmd) {
		boolean flag = false;
		for (PCmo_EntityMiner miner : controlledMiners) {
			if (miner.canReceiveKeyboardCommand()) {
				flag = true;
				miner.receiveKeyboardCommand(cmd);
			}
		}
		return flag;
	}


	/**
	 * Send urgent command to all connected miners.<br>
	 * This method works for commands that involve disconnecting from keyboard
	 * control.
	 * 
	 * @param cmd command index
	 * @return true if at least one miner accepted it
	 */
	private static boolean sendSafelyUrgentCommandToMiners(int cmd) {
		boolean flag = false;

		List<PCmo_EntityMiner> controlled2 = new ArrayList<PCmo_EntityMiner>();

		for (PCmo_EntityMiner miner : controlledMiners) {
			controlled2.add(miner);
		}

		for (PCmo_EntityMiner miner : controlled2) {
			if (miner.canReceiveKeyboardCommand()) {
				flag = true;
				miner.receiveKeyboardCommand(cmd);
			}
		}
		return flag;
	}



	/**
	 * Add this Miner to list of keyboard-controlled
	 * 
	 * @param miner the miner entity
	 * @param silent don't show chat message
	 */
	public static void setMinerForKeyboardControl(PCmo_EntityMiner miner, boolean silent) {
		lastTickWorld = ModLoader.getMinecraftInstance().theWorld;
		if (!controlledMiners.contains(miner)) {
			controlledMiners.add(miner);
		}

		if (!silent) {
			PC_Utils.chatMsg(PC_Lang.tr("pc.miner.connected", new String[] { miner.level + "" }), true);
		}
	}


	/*
	 * When this is ported to SMP, this method should stay unchanged,
	 * and the "sendCommand" methods should send packets to server.
	 */

	/**
	 * Update keyboard controls state.
	 */
	public static void onGameTick() {
		if (lastTickWorld != ModLoader.getMinecraftInstance().theWorld) {
			controlledMiners.clear();
			lastTickWorld = ModLoader.getMinecraftInstance().theWorld;
		}

		if (lastTickWorld == null) {
			return;
		}

		if (ModLoader.getMinecraftInstance().currentScreen != null) {
			return;
		}

		for (int i = 0; i <= 8; i++) {
			if (keyPressTimer[i] > 0) {
				keyPressTimer[i]--;
			}
		}

		PC_PropertyManager conf = mod_PCmobile.instance.cfg();

		if (ModLoader.getMinecraftInstance().theWorld != null && lastTickWorld == ModLoader.getMinecraftInstance().theWorld) {
			if (conf.isKeyDown(mod_PCmobile.pk_mForward)) {
				if (keyPressTimer[0] == 0) {
					keyPressTimer[0] = CooldownTime;
					sendCommandToMiners(PCmo_Command.FORWARD);
				}
				return;
			}
			if (conf.isKeyDown(mod_PCmobile.pk_mLeft)) {
				if (keyPressTimer[1] == 0) {
					keyPressTimer[1] = CooldownTime;
					sendCommandToMiners(PCmo_Command.LEFT);
				}
				return;
			}
			if (conf.isKeyDown(mod_PCmobile.pk_mRight)) {
				if (keyPressTimer[2] == 0) {
					keyPressTimer[2] = CooldownTime;
					sendCommandToMiners(PCmo_Command.RIGHT);
				}
				return;
			}
			if (conf.isKeyDown(mod_PCmobile.pk_mAround)) {
				if (keyPressTimer[3] == 0) {
					keyPressTimer[3] = CooldownTime;
					sendSequenceToMiners("RR");
				}
				return;
			}
			if (conf.isKeyDown(mod_PCmobile.pk_mBackward)) {
				if (keyPressTimer[4] == 0) {
					keyPressTimer[4] = CooldownTime;
					sendCommandToMiners(PCmo_Command.BACKWARD);
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mDown)) {
				if (keyPressTimer[5] == 0) {
					keyPressTimer[5] = CooldownTime;
					sendCommandToMiners(PCmo_Command.DOWN);
				}
				return;
			}
			if (conf.isKeyDown(mod_PCmobile.pk_mUp)) {
				if (keyPressTimer[6] == 0) {
					keyPressTimer[6] = CooldownTime;
					sendCommandToMiners(PCmo_Command.UP);
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mBridgeOn)) {
				if (sendCommandToMiners(PCmo_Command.BRIDGE_ENABLE)) {
					PC_Utils.chatMsg(PC_Lang.tr("pc.miner.bridgeOn"), true);
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mBridgeOff)) {
				if (sendCommandToMiners(PCmo_Command.BRIDGE_DISABLE)) {
					PC_Utils.chatMsg(PC_Lang.tr("pc.miner.bridgeOff"), true);
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mDeposit)) {
				if (keyPressTimer[7] == 0) {
					keyPressTimer[7] = CooldownTime;
					sendCommandToMiners(PCmo_Command.DEPOSIT);
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mRun)) {
				if (keyPressTimer[8] == 0) {
					keyPressTimer[8] = CooldownTime;
					if (sendSafelyUrgentCommandToMiners(PCmo_Command.RUN_PROGRAM)) {
						PC_Utils.chatMsg(PC_Lang.tr("pc.miner.launchedAll"), true);
					}
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mToBlocks)) {
				sendUrgentCommandToMiners(PCmo_Command.DISASSEMBLY);
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mMiningOn)) {
				if (sendCommandToMiners(PCmo_Command.MINING_ENABLE)) {
					PC_Utils.chatMsg(PC_Lang.tr("pc.miner.miningOn"), true);
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mMiningOff)) {
				if (sendCommandToMiners(PCmo_Command.MINING_DISABLE)) {
					PC_Utils.chatMsg(PC_Lang.tr("pc.miner.miningOff"), true);
				}
				return;
			}

			if (conf.isKeyDown(mod_PCmobile.pk_mCancel)) {
				if (sendUrgentCommandToMiners(PCmo_Command.RESET)) {
					PC_Utils.chatMsg(PC_Lang.tr("pc.miner.operationsCancelled"), true);
				}
				return;
			}
		}
	}

}
