package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;

public abstract class PC_NetManager implements PC_INBT {

	private final String netfile;
	
	private String worldSaveDir = null;
	
	private boolean needsSave = false;
	private String worldName = null;
	
	/**
	 * Minecraft instance
	 */
	public static final Minecraft mc = PC_Utils.mc();
	
	public PC_NetManager(String netfile){
		this.netfile = netfile;
	}
	
	/**
	 * Clear device lists if the world changed
	 */
	protected void checkWorldChange() {
		if (mc.theWorld == null) return;
		if (mc.theWorld.worldInfo.getWorldName() != worldName) {
			if (worldSaveDir != null) {
				System.out.println("World changed.");
				saveToFile();
			}
			worldName = mc.theWorld.worldInfo.getWorldName();
			worldSaveDir = mc.theWorld.saveHandler.getSaveDirectoryName();
			loadFromFile();
			needsSave = false;
		} else {
			worldSaveDir = mc.theWorld.saveHandler.getSaveDirectoryName();
		}
	}
	
	/**
	 * Load this bus from file in world folder
	 */
	public void loadFromFile() {
		File file = new File(worldSaveDir + netfile);
		if (file.exists()) {
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

	/**
	 * Save to a file in world folder
	 */
	public void saveToFile() {
		if (worldSaveDir == null) return;
		File file = new File(worldSaveDir + netfile);
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
	
	public void needSave(){
		needsSave = true;
		ModLoader.setInGameHook(mod_PCcore.instance, true, false);
	}
	
	public boolean needsSave(){
		return needsSave;
	}
	
}
