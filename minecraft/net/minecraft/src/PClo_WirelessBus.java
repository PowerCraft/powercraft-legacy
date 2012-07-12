package net.minecraft.src;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselVariableMap;

import net.minecraft.client.Minecraft;


/**
 * Wireless Bus
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_WirelessBus implements PC_INBT {

	private Minecraft mc = ModLoader.getMinecraftInstance();
	public boolean needsSave = false;

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

	private WeaselVariableMap weaselDataBus = new WeaselVariableMap();

	/**
	 * Set a weasel variable into the global weasel bus. You should use some prefixes in order to prevent cross-system conflicts.
	 * @param name variable key - name
	 * @param value variable value
	 * @throws WeaselRuntimeException if you are trying to store incompatible variable type
	 */
	public void setWeaselVariable(String name, WeaselObject value) throws WeaselRuntimeException {
		weaselDataBus.setVariableForce(name, value);
		needsSave = true;
	}

	/**
	 * Get state of a weasel variable.
	 * @param name variable name
	 * @return variable value.
	 */
	public WeaselObject getWeaselVariable(String name) {
		return weaselDataBus.getVariable(name);
	}



	/**
	 * Connect transmitter to the redstone bus
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
	 * @param device
	 */
	public void disconnectFromRedstoneBus(IRadioDevice device) {
		checkWorldChange();
		devices.remove(device);
	}

	/**
	 * Get state of a channel
	 * @param channel channel name
	 * @return TRUE if channel is on, someone is transmitting on it.
	 */
	public boolean getChannelState(String channel) {
		checkWorldChange();

		for (IRadioDevice device : devices) {
			if (device.doesTransmitOnChannel(channel)) return true;
		}
		
		if(anonymousTransmitters.containsKey(channel)) {
			return anonymousTransmitters.get(channel)>0;
		}

		return false;

	}

	/**
	 * Set anonymous transmitter on.<br>
	 * This is used by portables, because they do not have any object which could implement the IRadioDevice interface.
	 * Do not forget to also turn it OFF!
	 * @param channel channel name
	 */
	public void anonymousTransmitterOn(String channel) {
		checkWorldChange();
		Integer strength = anonymousTransmitters.get(channel);
		if (strength == null) strength = 0;

		strength++;

		anonymousTransmitters.put(channel, strength);
		needsSave=true;
	}

	/**
	 * Set anonymous transmitter off.<br>
	 * This is used by portables, because they do not have any object which could implement the IRadioDevice interface.
	 * Do not call this if you didn't turn it ON before!
	 * @param channel
	 */
	public void anonymousTransmitterOff(String channel) {
		checkWorldChange();
		Integer strength = anonymousTransmitters.get(channel);
		if (strength == null) strength = 0;

		strength--;

		if (strength < 0) strength = 0;
		anonymousTransmitters.put(channel, strength);
		needsSave=false;
	}

	private String worldName = null;
	private String worldSaveDir = null;

	/**
	 * Clear device lists if the world changed
	 */
	private void checkWorldChange() {
		if (mc.theWorld.worldInfo.getWorldName() != worldName) {
			if(worldSaveDir != null) {
				saveToFile();
			}
			devices.clear();
			anonymousTransmitters.clear();
			weaselDataBus.clear();
			worldName = mc.theWorld.worldInfo.getWorldName();
			worldSaveDir = (((SaveHandler) mc.theWorld.saveHandler).getSaveDirectory()).toString();
			loadFromFile();
			needsSave = false;
		}else {
			worldSaveDir = (((SaveHandler) mc.theWorld.saveHandler).getSaveDirectory()).toString();
		}
	}

	public void loadFromFile() {
		File file = new File(worldSaveDir+"/wireless_bus.dat");
		if(file.exists()) {
			// load it
			NBTTagCompound tag = new NBTTagCompound();
			try {
				tag.load(new DataInputStream(new FileInputStream(file)));
				needsSave = false;				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			readFromNBT(tag);
			needsSave = false;
		}
	}

	public void saveToFile() {
		if(worldSaveDir == null) return;
		File file = new File(worldSaveDir+"/wireless_bus.dat");
		NBTTagCompound tag = writeToNBT(new NBTTagCompound());
		try {
			tag.write(new DataOutputStream(new FileOutputStream(file)));
			needsSave = false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		// these are used only by the portables, and therefore we do not want to save them.
//		NBTTagList anonymous = new NBTTagList();
//		for (Entry<String, Integer> entry : anonymousTransmitters.entrySet()) {
//			NBTTagCompound entrytag = new NBTTagCompound();
//			entrytag.setString("C", entry.getKey());
//			entrytag.setInteger("S", entry.getValue());
//			anonymous.appendTag(entrytag);
//		}
//
//		tag.setTag("Anonymous", anonymous);

		tag.setCompoundTag("WeaselDataBus", weaselDataBus.writeToNBT(new NBTTagCompound()));

		return tag;
	}

	@Override
	public PClo_WirelessBus readFromNBT(NBTTagCompound tag) {

		// these are used only by the portables, and therefore we do not want to save them.
//		NBTTagList anonymous = tag.getTagList("Anonymous");
//		for (int i = 0; i < anonymous.tagCount(); i++) {
//			NBTTagCompound entrytag = (NBTTagCompound) anonymous.tagAt(i);
//			anonymousTransmitters.put(entrytag.getString("C"), entrytag.getInteger("S"));
//		}

		weaselDataBus.readFromNBT(tag.getCompoundTag("WeaselDataBus"));

		return this;
	}
}
