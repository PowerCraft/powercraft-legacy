package net.minecraft.src;


import java.util.Hashtable;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;


/**
 * Radio signal manager and receivers updater
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_RadioManager {

	private static Minecraft mc = ModLoader.getMinecraftInstance();

	/** RECEIVERS LIST coordinate -> channel:dimension */
	public static Hashtable<PC_CoordI, PC_Struct2<String, Integer>> receivers = new Hashtable<PC_CoordI, PC_Struct2<String, Integer>>();

	/**
	 * SIGNAL LIST hashtable of: (string) channel -> hashtable{
	 * struc(coordinate,dimension)->is_active }
	 */
	public static Hashtable<String, Hashtable<PC_Struct2<PC_CoordI, Integer>, Boolean>> signals = new Hashtable<String, Hashtable<PC_Struct2<PC_CoordI, Integer>, Boolean>>();

	private static String worldName = null;

	/**
	 * Clear device lists if the world changed
	 */
	private static void checkWorldChange() {
		if (mc.theWorld.worldInfo.getWorldName() != worldName) {
			receivers.clear();
			signals.clear();
			worldName = mc.theWorld.worldInfo.getWorldName();
		}
	}

	/**
	 * Register receiver
	 * 
	 * @param dimension the dimension of the device (-1,0,1)
	 * @param pos device position
	 * @param channel device channel
	 */
	public static void registerReceiver(int dimension, PC_CoordI pos, String channel) {
		checkWorldChange();
		receivers.put(pos, new PC_Struct2<String, Integer>(channel, dimension));
	}

	/**
	 * Unregister receiver
	 * 
	 * @param pos device position
	 */
	public static void unregisterReceiver(PC_CoordI pos) {
		checkWorldChange();
		receivers.remove(pos);
	}

	/**
	 * Set on/off state of a transmitter.
	 * 
	 * @param dimension the dimension of the device (-1,0,1)
	 * @param pos device position
	 * @param channel device channel
	 * @param state is active (sending signal)
	 */
	public static void setTransmitterState(int dimension, PC_CoordI pos, String channel, boolean state) {
		checkWorldChange();

		if (!signals.containsKey(channel)) {
			signals.put(channel, new Hashtable<PC_Struct2<PC_CoordI, Integer>, Boolean>());
		}

		boolean oldState = getSignalStrength(channel) > 0;

		signals.get(channel).put(new PC_Struct2<PC_CoordI, Integer>(pos, dimension), state);

		boolean newState = getSignalStrength(channel) > 0;

		if (oldState != newState) {

			for (PC_CoordI recpos : receivers.keySet()) {
				if (receivers.get(recpos).get1().equals(channel)) {

					if (receivers.get(recpos).get2() != mc.theWorld.worldInfo.getDimension()) {
						// from other dimensions.
						continue;
					}

					TileEntity te = mc.theWorld.getBlockTileEntity(recpos.x, recpos.y, recpos.z);
					if (te != null) {

						PClo_TileEntityRadio ter = (PClo_TileEntityRadio) te;

						if (ter.isReceiver() && recpos.getId(mc.theWorld) == mod_PClogic.radio.blockID) {
							mc.theWorld.setBlockMetadata(recpos.x, recpos.y, recpos.z, newState ? 1 : 0);
							ter.setStateWithNotify(newState);
							ter.updateBlock();
						}

					}
				}
			}

		}
	}

	/**
	 * Set or change transmitter's channel.<br>
	 * Updates signal states and sends block updates if needed.
	 * 
	 * @param dimension the dimension of the device (-1,0,1)
	 * @param pos device position
	 * @param oldChannel old channel (before change)
	 * @param newChannel new channel (changed to)
	 * @param state current device state (on/off)
	 */
	public static void setTransmitterChannel(int dimension, PC_CoordI pos, String oldChannel, String newChannel, boolean state) {
		checkWorldChange();

		setTransmitterState(dimension, pos, oldChannel, false);
		signals.get(oldChannel).remove(new PC_Struct2<PC_CoordI, Integer>(pos, dimension));
		setTransmitterState(dimension, pos, newChannel, state);
	}

	/**
	 * Set receiver's channel
	 * 
	 * @param dimension the device is in
	 * @param pos device position
	 * @param newChannel new channel (changed to)
	 */
	public static void setReceiverChannel(int dimension, PC_CoordI pos, String newChannel) {
		checkWorldChange();

		unregisterReceiver(pos);
		registerReceiver(dimension, pos, newChannel);

	}

	/**
	 * Get signal strength
	 * 
	 * @param channel signal channel
	 * @return number of active transmitters with this channel
	 */
	public static int getSignalStrength(String channel) {
		checkWorldChange();

		if (!signals.containsKey(channel)) {
			return 0;
		}

		int cnt = 0;
		for (Entry<PC_Struct2<PC_CoordI, Integer>, Boolean> a : signals.get(channel).entrySet()) {
			if (a.getValue() == true) {
				// for server, ignore this IF - server has multiple dimensions accessible at once.
				if (a.getKey().get2() == mc.theWorld.worldInfo.getDimension()) {
					cnt++;
				}
			}
		}

		return cnt;
	}

	/**
	 * Send pulse from the portable device
	 * 
	 * @param channel channel name for pulse
	 */
	public static void sendRemotePulse(String channel) {
		checkWorldChange();

		for (PC_CoordI recpos : receivers.keySet()) {

			PC_Struct2<String, Integer> receiver = receivers.get(recpos);

			if (receiver.get1().equals(channel)) {

				// on server, use receiver.get2() to get dimension number.
				// I believe that server can have cross-dimension radios
				// -- MP
				World world = mc.theWorld;

				recpos.setMeta(world, 1);
				world.notifyBlocksOfNeighborChange(recpos.x, recpos.y, recpos.z, mod_PClogic.radio.blockID);
				world.notifyBlocksOfNeighborChange(recpos.x, recpos.y - 1, recpos.z, mod_PClogic.radio.blockID);
				world.markBlocksDirty(recpos.x, recpos.y, recpos.z, recpos.x, recpos.y, recpos.z);
				world.scheduleBlockUpdate(recpos.x, recpos.y, recpos.z, mod_PClogic.radio.blockID, 10);

			}
		}

	}

	/**
	 * unregister transmitter
	 * 
	 * @param dimension the dimension of the device (-1,0,1)
	 * @param pos device position
	 * @param channel device channel
	 */
	public static void unregisterTx(int dimension, PC_CoordI pos, String channel) {
		checkWorldChange();
		setTransmitterState(dimension, pos, channel, false);
		signals.get(channel).remove(new PC_Struct2<PC_CoordI, Integer>(pos, dimension));
	}
}
