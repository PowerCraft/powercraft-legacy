package net.minecraft.src;

import java.util.Hashtable;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;

/**
 * Teleporter name and coordinate manager.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCtr_TeleporterManagerUNUSED {

	private static Minecraft mc = ModLoader.getMinecraftInstance();

	private static Hashtable<PC_CoordI, String> targets = new Hashtable<PC_CoordI, String>();

	private static World world = null;

	/**
	 * Clear device lists if the world changed
	 */
	private static void checkWorldChange() {
		if (mc.theWorld != world) {
			targets.clear();
			world = mc.theWorld;
		}
	}

	/**
	 * Register target device, or rename target.
	 * 
	 * @param pos device position
	 * @param name device name (identifier)
	 */
	public static void registerTarget(PC_CoordI pos, String name) {
		checkWorldChange();
		targets.put(pos, name);
	}

	/**
	 * Unregister target device. No problem if device is not registered or is not target.
	 * 
	 * @param pos device position
	 */
	public static void unregisterTarget(PC_CoordI pos) {
		checkWorldChange();
		targets.remove(pos);
	}



	private static PC_CoordI getTargetCoord(String name) {
		checkWorldChange();

		for (Entry<PC_CoordI, String> a : targets.entrySet()) {
			if (a.getValue().equals(name)) { return a.getKey(); }
		}

		return null;
	}

	/**
	 * Check if teleporter target exists
	 * 
	 * @param name target name
	 * @return exists
	 */
	public static boolean targetExists(String name) {
		checkWorldChange();

		return targets.contains(name);
	}

	/**
	 * Check if teleporter target exists, excluding device at given coordinates
	 * 
	 * @param name target name
	 * @param coord coordinate to exclude
	 * @return exists
	 */
	public static boolean targetExistsExcept(String name, PC_CoordI coord) {
		checkWorldChange();

		if (name.equals("")) { return false; }

		for (Entry<PC_CoordI, String> a : targets.entrySet()) {
			if (a.getValue().equals(name)) {
				if (a.getKey().equals(coord)) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	// ------------- TESTS -------------
	/**
	 * Check if block is teleporter
	 * 
	 * @param id
	 * @return is teleporter
	 */
	private static final boolean isTeleporter(int id) {
		return id == mod_PCtransport.teleporter.blockID;
	}

	// ----- ENTITY MANIPULATION ------

	/**
	 * Teleport entity to given target device.
	 * 
	 * @param entity entity to teleport
	 * @param target target device name
	 * @return success
	 */
	public static boolean teleportEntityTo(Entity entity, String target) {

		PC_CoordI tc = getTargetCoord(target);
		// target invalid
		if (tc == null) { return false; }

		World world = entity.worldObj;

		if (world.getBlockId(tc.x, tc.y, tc.z) != mod_PCtransport.teleporter.blockID) { return false; }

		// we have to find space for the entity, conveyor in good direction
		// preferably.
		int x, y, z, meta, rotation;

		int good[] = { 0, 0, 0, 0 };

		PC_CoordI coords[] = { new PC_CoordI(0, 0, -1), new PC_CoordI(+1, 0, 0), new PC_CoordI(0, 0, +1), new PC_CoordI(-1, 0, 0) };

		for (int i = 0; i < 4; i++) {

			x = tc.x + coords[i].x;
			y = tc.y + coords[i].y;
			z = tc.z + coords[i].z;

			if (PCtr_BeltBase.isConveyorAt(world, x, y, z)) {
				meta = world.getBlockMetadata(x, y, z);
				rotation = PCtr_BeltBase.getRotation(meta);
				if (rotation == i) {
					// good rotation, 3 points
					good[i] = 3;
				} else if (rotation != ((i + 2) % 4)) {
					// not reverse rotation, 2 points
					good[i] = 2;
				}
			} else if (!PCtr_BeltBase.isBlocked(world, new PC_CoordI(x, y, z))) {
				// can pass through, 1 point
				good[i] = 1;
			}

		}

		// if some side is good
		if (good[0] + good[1] + good[2] + good[3] > 0) {

			// first find highest rank
			// then decrease requirements
			for (int need = 3; need > 0; need--) {
				// go through the array
				for (int i = 0; i < 4; i++) {
					if (good[i] >= need) {

						// teleport the entity!
						entity.posX = entity.prevPosX = entity.lastTickPosX = tc.x + 0.5F + coords[i].x * 0.7;
						entity.posY = entity.prevPosY = entity.lastTickPosY = tc.y + entity.yOffset + 0.25F;
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
