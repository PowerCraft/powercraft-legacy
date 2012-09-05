package net.minecraft.src;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;


/**
 * Wireless Bus
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_RadioBus {

	private Minecraft mc = ModLoader.getMinecraftInstance();

	/**
	 * Radio device, which can have more than one output channels.<br>
	 * There is no system of notifications, so you must check the channel you
	 * are monitoring yourself.
	 * 
	 * @author MightyPork
	 */
	public interface IRadioDevice {
		/**
		 * Is this device atm transmitting TRUE signal on given channel?<br>
		 * This is called when a receiver asks for signal state.
		 * 
		 * @param channel channel name
		 * @return is transmitting.
		 */
		public boolean doesTransmitOnChannel(String channel);
	}

	private Set<IRadioDevice> devices = new HashSet<IRadioDevice>();

	private Map<String, Integer> anonymousTransmitters = new HashMap<String, Integer>();



	/**
	 * Connect transmitter to the redstone bus
	 * 
	 * @param device
	 */
	public void connectToRedstoneBus(IRadioDevice device) {
		checkWorldChange();
		if (!devices.contains(device)) {
			devices.add(device);
		}
	}

	/**
	 * Disconnect transmitter form the redstone bus
	 * 
	 * @param device
	 */
	public void disconnectFromRedstoneBus(IRadioDevice device) {
		checkWorldChange();
		devices.remove(device);
	}

	/**
	 * Get state of a channel
	 * 
	 * @param channel channel name
	 * @return TRUE if channel is on, someone is transmitting on it.
	 */
	public boolean getChannelState(String channel) {
		checkWorldChange();

		for (IRadioDevice device : devices) {
			if (device.doesTransmitOnChannel(channel)) return true;
		}

		if (anonymousTransmitters.containsKey(channel)) {
			return anonymousTransmitters.get(channel) > 0;
		}

		return false;

	}

	/**
	 * Set anonymous transmitter on.<br>
	 * This is used by portables, because they do not have any object which
	 * could implement the IRadioDevice interface. Do not forget to also turn it
	 * OFF!
	 * 
	 * @param channel channel name
	 */
	public void anonymousTransmitterOn(String channel) {
		checkWorldChange();
		Integer strength = anonymousTransmitters.get(channel);
		if (strength == null) strength = 0;

		strength++;

		anonymousTransmitters.put(channel, strength);
	}

	/**
	 * Set anonymous transmitter off.<br>
	 * This is used by portables, because they do not have any object which
	 * could implement the IRadioDevice interface. Do not call this if you
	 * didn't turn it ON before!
	 * 
	 * @param channel
	 */
	public void anonymousTransmitterOff(String channel) {
		checkWorldChange();
		Integer strength = anonymousTransmitters.get(channel);
		if (strength == null) strength = 0;

		strength--;

		if (strength < 0) strength = 0;
		anonymousTransmitters.put(channel, strength);
	}

	private String worldName = null;

	/**
	 * Clear device lists if the world changed
	 */
	private void checkWorldChange() {
		if(mc.theWorld==null||devices==null||anonymousTransmitters==null)return;
		if (mc.theWorld.worldInfo.getWorldName() != worldName) {
			devices.clear();
			anonymousTransmitters.clear();
			worldName = mc.theWorld.worldInfo.getWorldName();
		}
	}

}
