package net.minecraft.src;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;


/**
 * Wireless Bus
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_RadioBus implements PC_INBTWD {

	private Map<String, Integer> transmitters = new HashMap<String, Integer>();
	private boolean needsSave = false;
	
	/**
	 * Get state of a channel
	 * 
	 * @param channel channel name
	 * @return TRUE if channel is on, someone is transmitting on it.
	 */
	public boolean getChannelState(String channel) {

		if (transmitters.containsKey(channel)) {
			return transmitters.get(channel) > 0;
		}

		return false;

	}

	/**
	 * Set transmitter on.<br>
	 * This is used by portables, because they do not have any object which
	 * could implement the IRadioDevice interface. Do not forget to also turn it
	 * OFF!
	 * 
	 * @param channel channel name
	 */
	public void transmitterOn(String channel) {
		Integer strength = transmitters.get(channel);
		System.out.println("transmitterOn:"+channel+":"+strength);
		if (strength == null) strength = 0;

		strength++;

		transmitters.put(channel, strength);
		
		needsSave = true;
	}

	/**
	 * Set transmitter off.<br>
	 * This is used by portables, because they do not have any object which
	 * could implement the IRadioDevice interface. Do not call this if you
	 * didn't turn it ON before!
	 * 
	 * @param channel
	 */
	public void transmitterOff(String channel) {
		Integer strength = transmitters.get(channel);
		System.out.println("transmitterOff:"+channel+":"+strength);
		if (strength == null) strength = 0;

		strength--;

		if (strength < 0) strength = 0;
		if (strength == 0)
			transmitters.remove(channel);
		else
			transmitters.put(channel, strength);
		
		needsSave = true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		int size = transmitters.size();
		tag.setInteger("transmitters", size);
		Set<Entry<String, Integer>> se = transmitters.entrySet();
		int i=0;
		for(Entry<String, Integer> e:se){
			tag.setString("key["+i+"]", e.getKey());
			tag.setInteger("value["+i+"]", e.getValue());
			i++;
		}
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		transmitters.clear();
		int size = tag.getInteger("transmitters");
		for(int i=0; i<size; i++){
			String key = tag.getString("key["+i+"]");
			int value = tag.getInteger("value["+i+"]");
			transmitters.put(key, value);
		}
		return this;
	}

	@Override
	public boolean needsSave() {
		boolean ns = needsSave;
		needsSave = false;
		return ns;
	}

}
