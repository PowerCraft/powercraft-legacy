package net.minecraft.src;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.client.Minecraft;


public class PCtr_TeleporterEntry {
	private static Minecraft mc = ModLoader.getMinecraftInstance();

	public int i = 0, j = 0, k = 0;
	public String identifier;
	public int dimension = 0;

	public PCtr_TeleporterEntry(int pi, int pj, int pk) {
		setPos(pi, pj, pk);
	}

	public PCtr_TeleporterEntry(int pi, int pj, int pk, String id) {
		setPos(pi, pj, pk);
		setIdentifier(id);
		setDimension(mc.theWorld.worldInfo.getDimension());
	}

	public PCtr_TeleporterEntry(int pi, int pj, int pk, String id, int dim) {
		setPos(pi, pj, pk);
		setIdentifier(id);
		setDimension(dim);
	}

	public PCtr_TeleporterEntry(String file) {
		load(file);
	}

	public PCtr_TeleporterEntry copy() {
		return new PCtr_TeleporterEntry(i, j, k, identifier);
	}

	public void load(String file) {
		Properties props = new Properties();

		try {
			props.load(new FileInputStream(file));
		} catch (IOException e) {
			return;
		}
		try {
			setPos(Integer.valueOf(props.getProperty("x")), Integer.valueOf(props.getProperty("y")), Integer.valueOf(props.getProperty("z")));
			setIdentifier(props.getProperty("id"));
			setDimension(props.getProperty("dim"));
		} catch (NumberFormatException nfe) {
			return;
		}

	}

	public void removeFile() {
		PC_Logger.finest("Deleting teleporter's file.");
		File f = new File(getSavePath(mc.theWorld));
		if (!f.exists()) {
			PC_Logger.warning("file does not exist");
			return;
		}
		if (!f.canWrite()) {
			PC_Logger.warning("file not accessible");
			return;
		}
		if (f.isDirectory()) {
			PC_Logger.warning("file is dir");
			return;
		}

		f.delete();
	}

	public void save() {
		(new File(getSaveDir(mc.theWorld))).mkdirs();
		Properties props = new Properties();
		props.setProperty("x", Integer.toString(i));
		props.setProperty("y", Integer.toString(j));
		props.setProperty("z", Integer.toString(k));
		props.setProperty("id", identifier);
		props.setProperty("dim", Integer.toString(dimension));

		try {
			props.store(new FileOutputStream(getSavePath(mc.theWorld)), "Teleporter save file - do not touch!");
		} catch (IOException ioe) {}
	}

	public PC_CoordI getCoord() {
		return new PC_CoordI(i, j, k);
	}

	public static String getSaveDir(World world) {
		if (mc == null || world == null) {
			return null;
		}
		return (((SaveHandler) world.saveHandler).getSaveDirectory()).toString().concat("/teleporter/");
	}

	private String getSavePath(World world) {
		if (mc == null || world == null) {
			return null;
		}
		return (((SaveHandler) world.saveHandler).getSaveDirectory()).toString().concat("/teleporter/").concat(getIdentifier()).concat(".dat");
	}

	public int getDimension() {
		return dimension;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public int getK() {
		return k;
	}

	public int getX() {
		return i;
	}

	public int getY() {
		return j;
	}

	public int getZ() {
		return k;
	}

	private void setDimension(int dim) {
		dimension = dim;
	}

	private void setDimension(String property) {

		if (property == null) {
			dimension = 0;
			return;
		}

		int dim;
		try {
			dim = Integer.parseInt(property);
		} catch (NumberFormatException e) {
			dim = 0;
		}

		dimension = dim;
	}

	public void setIdentifier(String id) {
		identifier = id;
	}

	public void setPos(int i, int j, int k) {
		this.i = i;
		this.j = j;
		this.k = k;
	}
}