package net.minecraft.src;

import java.io.File;
import java.util.Hashtable;

import net.minecraft.client.Minecraft;

public class PCtr_TeleporterHelper {

	public static Minecraft mc = ModLoader.getMinecraftInstance();

	public PCtr_TeleporterHelper() {}

	// will be filled with PCtr_TeleporterEntry
	private static Hashtable<String, PCtr_TeleporterEntry> targets = new Hashtable<String, PCtr_TeleporterEntry>();

	private static boolean listsLoaded = false;
	private static World listsWorld = null;
	private static String listSaveDir = null;

	// ------- LOADING ----------
	private static void loadAllLists() {
		listsWorld = mc.theWorld;
		listSaveDir = PCtr_TeleporterEntry.getSaveDir(mc.theWorld);
		listsLoaded = true;

		loadDevices();
		// checkAllDevices();
	}

	private static void loadIfNeeded() {
		if (listsWorld == null || listSaveDir == null || !listsLoaded || listsWorld != mc.theWorld
				|| !listSaveDir.equals(PCtr_TeleporterEntry.getSaveDir(mc.theWorld))) {
			PC_Logger.fine("Loading teleporter list...");
			targets.clear();
			loadAllLists();
		}
	}

	private static void loadDevices() {

		String dirPath = PCtr_TeleporterEntry.getSaveDir(mc.theWorld);
		File dir = new File(dirPath);

		String[] children = dir.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
			return;
		} else {
			for (String filename : children) {
				loadSingleDevice(dirPath + filename);
			}
		}
	}

	private static void loadSingleDevice(String filePath) {
		PCtr_TeleporterEntry entry = new PCtr_TeleporterEntry(filePath);

		PC_CoordI coord = entry.getCoord();

		// fix for tps with no dim set
		if (!isTeleporter(mc.theWorld.getBlockId(coord.x, coord.y, coord.z))) {
			// can also be in ender, but who cares ;)
			entry.dimension = mc.thePlayer.dimension == 0 ? -1 : 0;
		}

		targets.put(new String(entry.getIdentifier()), entry);

		for (PCtr_TeleporterEntry e : targets.values()) {
			e.save();
		}

	}

	// ------ GETTERS ----------

	private static PC_CoordI getTargetCoord(String identifier) {
		loadIfNeeded();
		PCtr_TeleporterEntry e = targets.get(identifier);
		if (e == null) {
			return null;
		} else {
			return e.getCoord();
		}
	}

	public static boolean targetExists(String identifier) {
		loadIfNeeded();

		if (identifier.equals("")) { return false; }

		return targets.get(identifier) != null;
	}

	public static boolean targetExistsExcept(String identifier, PC_CoordI coord) {
		loadIfNeeded();

		if (identifier.equals("")) { return false; }

		PCtr_TeleporterEntry entry = targets.get(identifier);
		return entry != null && !entry.getCoord().equals(coord);
	}

	public static boolean isTargetInThisDimension(String identifier) {
		loadIfNeeded();

		return mc.thePlayer.dimension == getTargetDimension(identifier);
	}

	public static int getTargetDimension(String identifier) {
		loadIfNeeded();

		if (identifier.equals("")) { return 0; }

		PCtr_TeleporterEntry entry = targets.get(identifier);
		if (entry == null) { return 0; }

		return entry.getDimension();
	}

	// ------ BLOCK PLACING AND REMOVAL ---

	public static void registerNewDevice(int i, int j, int k, String identifier) {
		loadIfNeeded();

		int dim = mc.thePlayer.dimension;
		PCtr_TeleporterEntry newDev = new PCtr_TeleporterEntry(i, j, k, identifier, dim);
		newDev.save();
		targets.put(identifier, newDev);
	}

	public static boolean renameDevice(String id1, String id2) {
		loadIfNeeded();

		if (!targetExists(id1) || targetExists(id2)) {

		return false; }

		PCtr_TeleporterEntry dev = targets.get(id1);

		if (dev == null) {

		return false;

		}

		// change device name, save to file, put under new key into the list of
		// targets

		dev.removeFile();

		dev.setIdentifier(id2);

		dev.save();
		targets.put(id2, dev.copy());
		targets.remove(id1);

		return true;
	}

	public static void unregisterDevice(String identifier) {
		loadIfNeeded();

		PCtr_TeleporterEntry dev = targets.get(identifier);

		if (dev == null) { return; }

		dev.removeFile();
		targets.remove(identifier);

		// deactivate devices with this target
		// checkAllDevices();
	}

	// ------------- TESTS -------------
	private static final boolean isTeleporter(int id) {
		return id == mod_PCtransport.teleporter.blockID;
	}

	// ----- ENTITY MANIPULATION ------

	public static boolean teleportEntityTo(Entity entity, String target) {

		PC_CoordI tc = getTargetCoord(target);
		// target invalid
		if (tc == null) { return false; }

		World world = entity.worldObj;

		if (world.getBlockId(tc.x, tc.y, tc.z) != mod_PCtransport.teleporter.blockID) { return false; }

		PCtr_TileEntityTeleporter tet = (PCtr_TileEntityTeleporter) tc.getTileEntity(world);

		if (tet == null) { return false; }

		// we have to find space for the entity, conveyor in good direction
		// preferably.
		int meta, rotation;

		int good[] = { 0, 0, 0, 0 };
		PC_CoordI coords[] = { new PC_CoordI(0, 0, -1), new PC_CoordI(+1, 0, 0), new PC_CoordI(0, 0, +1), new PC_CoordI(-1, 0, 0) };
		String[] side = { "N", "E", "S", "W" };
		for (int i = 0; i < 4; i++) {

			PC_CoordI tmp = tc.offset(coords[i]);

			if (PC_BlockUtils.hasFlag(world, tmp, "BELT") && !PC_BlockUtils.hasFlag(world, tmp, "TELEPORTER")) {

				meta = tmp.getMeta(world);
				rotation = PCtr_BlockConveyor.getRotation_(meta);
				if (rotation == i) {
					// good rotation, 3 points
					good[i] = 3;
				} else if (rotation != ((i + 2) % 4)) {
					// not reverse rotation, 2 points
					good[i] = 2;
				}

			} else if (!PCtr_BlockConveyor.isBlocked(world, tmp.x, tmp.y, tmp.z)) {
				// can pass through, 1 point
				good[i] = 1;
			}

			if (tet.direction.equals(side[i])) {
				good[i] += 1;
			}
			
			if(PC_BlockUtils.hasFlag(world, tmp, "TELEPORTER")) good[i]=0;

		}

		// if some side is good
		if (good[0] + good[1] + good[2] + good[3] > 0) {

			// first find highest rank
			// then decrease requirements
			for (int need = 6; need > 0; need--) {
				// go through the array
				for (int i = 0; i < 4; i++) {
					if (good[i] >= need) {

						// teleport the entity!
						entity.posX = entity.prevPosX = entity.lastTickPosX = tc.x + 0.5F + coords[i].x * 0.7;
						entity.posY = entity.prevPosY = entity.lastTickPosY = tc.y + entity.yOffset + 0.2F;
						entity.posZ = entity.prevPosZ = entity.lastTickPosZ = tc.z + 0.5F + coords[i].z * 0.7;
						entity.setPosition(entity.posX, entity.posY, entity.posZ);

						entity.motionX = coords[i].x * 0.2F;
						entity.motionZ = coords[i].z * 0.2F;

						entity.rotationYaw = (i * 90F) + 180F;

						entity.fallDistance = 0;
						entity.distanceWalkedModified = 0;

						return true;
					}
				}
			}

			return false;

		} else {
			return false;
		}

	}

}
