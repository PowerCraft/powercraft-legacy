package net.minecraft.src;


import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.client.Minecraft;


public class PCtr_TeleporterHelper implements PC_INBT {

	public static Minecraft mc = ModLoader.getMinecraftInstance();

	public static List<PCtr_TeleporterData> teleporter = new ArrayList<PCtr_TeleporterData>();

	public PCtr_TeleporterHelper() {}
	
	// ------ GETTERS ----------

	private static PCtr_TeleporterData getTarget(String target){
		for(PCtr_TeleporterData td : teleporter)
			if(td.name.equals(target))
				return td;
		return null;
	}
	
	private static PC_CoordI getTargetCoord(String identifier) {
		PCtr_TeleporterData e = getTarget(identifier);
		if (e == null) {
			return null;
		} else {
			return e.pos.copy();
		}
	}

	public static boolean targetExists(String identifier) {

		if (identifier.equals("")) {
			return false;
		}

		return getTarget(identifier) != null;
	}
	
	public static boolean targetExistsExcept(String identifier, PC_CoordI coord) {

		if (identifier.equals("")) {
			return false;
		}

		PCtr_TeleporterData entry = getTarget(identifier);
		return entry != null && !entry.pos.equals(coord);
	}

	public static boolean isTargetInThisDimension(String identifier) {
		return mc.thePlayer.dimension == getTargetDimension(identifier);
	}

	public static int getTargetDimension(String identifier) {
		if (identifier.equals("")) {
			return 0;
		}

		PCtr_TeleporterData entry = getTarget(identifier);
		if (entry == null) {
			return 0;
		}

		return entry.dimension;
	}

	// ------------- TESTS -------------
	private static final boolean isTeleporter(int id) {
		return id == mod_PCtransport.teleporter.blockID;
	}

	// ----- ENTITY MANIPULATION ------

	public static boolean teleportEntityTo(Entity entity, String target) {

		PC_CoordI tc = getTargetCoord(target);
		// target invalid
		if (tc == null) {
			return false;
		}

		World world = entity.worldObj;

		if (world.getBlockId(tc.x, tc.y, tc.z) != mod_PCtransport.teleporter.blockID) {
			return false;
		}

		PCtr_TileEntityTeleporter tet = (PCtr_TileEntityTeleporter) tc.getTileEntity(world);

		if (tet == null) {
			return false;
		}

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
				rotation = PCtr_BeltBase.getRotation(meta);
				if (rotation == i) {
					// good rotation, 3 points
					good[i] = 3;
				} else if (rotation != ((i + 2) % 4)) {
					// not reverse rotation, 2 points
					good[i] = 2;
				}

			} else if (!PCtr_BeltBase.isBlocked(world, tmp)) {
				// can pass through, 1 point
				good[i] = 1;
			}

			if (tet.td.direction.equals(side[i])) {
				good[i] += 1;
			}

			if (PC_BlockUtils.hasFlag(world, tmp, "TELEPORTER")) {
				good[i] = 0;
			}

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
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("size", teleporter.size());
		int i=0;
		for(PCtr_TeleporterData td:teleporter){
			NBTTagCompound tc = new NBTTagCompound();
			td.writeToNBT(tc);
			tag.setCompoundTag("field["+i+"]", tc);
			i++;
		}
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		teleporter.clear();
		int size = tag.getInteger("size");
		for(int i=0; i<size; i++){
			PCtr_TeleporterData td = new PCtr_TeleporterData();
			td.readFromNBT(tag.getCompoundTag("field["+i+"]"));
			teleporter.add(td);
		}
		return this;
	}

	public static PCtr_TeleporterData getTeleporterDataAt(int xCoord,
			int yCoord, int zCoord) {
		for(PCtr_TeleporterData td:teleporter){
			if(td.pos.x==xCoord&&td.pos.y==yCoord&&td.pos.z==zCoord)
				return td;
		}
		return null;
	}

}
