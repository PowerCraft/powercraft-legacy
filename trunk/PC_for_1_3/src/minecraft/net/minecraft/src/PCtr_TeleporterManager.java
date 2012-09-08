package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class PCtr_TeleporterManager extends PC_PacketHandler implements PC_INBTWD{

	private static List<PCtr_TeleporterData> teleporterData = new ArrayList<PCtr_TeleporterData>();

	private static PCtr_TeleporterManager tm = null;
	
	private static boolean needsSave=false;
	
	public static Minecraft mc = PC_Utils.mc();
	
	public static PCtr_TeleporterManager getTeleporterManager(){
		if(tm==null){
			tm = new PCtr_TeleporterManager();
		}
		return tm;
	}
	
	private PCtr_TeleporterManager(){
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		int size = teleporterData.size();
		tag.setInteger("size", size);
		for(int i=0; i<size; i++){
			NBTTagCompound t = new NBTTagCompound();
			teleporterData.get(i).writeToNBT(t);
			tag.setCompoundTag("index["+i+"]", t);
		}
		needsSave = false;
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		teleporterData.clear();
		int size = tag.getInteger("size");
		for(int i=0; i<size; i++){
			PCtr_TeleporterData td = new PCtr_TeleporterData();
			teleporterData.add(td);
			td.readFromNBT(tag.getCompoundTag("index["+i+"]"));
		}
		needsSave = false;
		return this;
	}

	@Override
	public boolean needsSave() {
		if(needsSave)
			return true;
		for(PCtr_TeleporterData td:teleporterData)
			if(td.needsSave())
				return true;
		return false;
	}

	public static void remove(PCtr_TeleporterData td){
		needsSave=true;
		teleporterData.remove(td);
	}

	public static void add(PCtr_TeleporterData td){
		if(!teleporterData.contains(td))
			teleporterData.add(td);
	}
	
	// ------ GETTERS ----------

	private static PCtr_TeleporterData getTarget(String target){
		for(PCtr_TeleporterData td : tm.teleporterData)
			if(td.getName().equals(target))
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

		if(target==null||target.equals(""))
			return false;
		
		System.out.println("teleport "+entity+" to "+target);
		
		PCtr_TeleporterData tdt = getTarget(target);
		if(tdt== null) {
			System.out.println("Faild bc tdt");
			return false;
		}
		PC_CoordI tc = tdt.pos;
		// target invalid
		if (tc == null) {
			System.out.println("Faild bc tc");
			return false;
		}

		//World world = MinecraftServer.getServer().worldServerForDimension(tdt.dimension);
		World world = entity.worldObj;
		System.out.println(entity.worldObj.worldInfo.getDimension());
		System.out.println(entity.worldObj);
		
		//if (world.getBlockId(tc.x, tc.y, tc.z) != mod_PCtransport.teleporter.blockID) {
		//	System.out.println("Faild bc mod_PCtransport.teleporter.blockID");
		//	return false;
		//}

		PCtr_TeleporterData td = getTeleporterDataAt(tc.x, tc.y, tc.z);
		
		if (td == null) {
			System.out.println("Faild bc td");
			return false;
		}
		
		System.out.println(tc);
		
		//if(entity.worldObj.worldInfo.getDimension()!=tdt.dimension&&entity instanceof EntityPlayerMP)
		//	((EntityPlayerMP)entity).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)entity, tdt.dimension);
        
		
		//if(entity.worldObj!=world)
		//	entity.setWorld(world);
		//System.out.println(entity.worldObj.worldInfo.getDimension());
		//System.out.println(entity.worldObj);
		
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

			if (td.direction.equals(side[i])) {
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
						//entity.motionX = coords[i].x * 0.2F;
						//entity.motionZ = coords[i].z * 0.2F;
						entity.fallDistance = 0;
						entity.distanceWalkedModified = 0;
						entity.rotationYaw = (i * 90F) + 180F;
						if(entity instanceof EntityPlayerMP){
							
							((EntityPlayerMP)entity).setPositionAndUpdate(tc.x + 0.5F + coords[i].x * 0.9, tc.y + 0.01F, tc.z + 0.5F + coords[i].z * 0.9);
						}else if(!(entity instanceof EntityPlayer)){
							entity.posX = entity.prevPosX = entity.lastTickPosX = tc.x + 0.5F + coords[i].x * 0.9;
							entity.posY = entity.prevPosY = entity.lastTickPosY = tc.y + entity.yOffset + 0.01F;
							entity.posZ = entity.prevPosZ = entity.lastTickPosZ = tc.z + 0.5F + coords[i].z * 0.9;
							entity.setPosition(entity.posX, entity.posY, entity.posZ);
						}
						return true;
					}
				}
			}

			return false;

		} else {
			return false;
		}

	}

	public static PCtr_TeleporterData getTeleporterDataAt(int xCoord,
			int yCoord, int zCoord) {
		for(PCtr_TeleporterData td:tm.teleporterData){
			if(td.pos.x==xCoord&&td.pos.y==yCoord&&td.pos.z==zCoord)
				return td;
		}
		return null;
	}

	public static PCtr_TileEntityTeleporter getTeleporterAt(World world, int xCoord,
			int yCoord, int zCoord) {
		TileEntity te = world.getBlockTileEntity(xCoord, yCoord, zCoord);
		if(te instanceof PCtr_TileEntityTeleporter)
			return (PCtr_TileEntityTeleporter)te;
		return null;
	}

	public static List<String> getTargetNames() {
		List<String> names = new ArrayList<String>();
		for(PCtr_TeleporterData td:teleporterData)
			if(!(td.getName()==null||"".equals(td.getName())))
				names.add(td.getName());
		return names;
	}
	
	public static boolean isNameOk(String name){
		return !getTargetNames().contains(name);
	}

	@Override
	public void handleIncomingPacket(World world, Object[] o) {
		int type = (Integer)o[0];
		int x=(Integer)o[1];
		int y=(Integer)o[2];
		int z=(Integer)o[3];
		String name=(String)o[4];
		String target=(String)o[5];
		int dimension=0;
		if(type==0)
			dimension = (Integer)o[6];
		PCtr_TeleporterData td = getTeleporterDataAt(x, y, z);
		if(td==null){
			td = new PCtr_TeleporterData();
			teleporterData.add(td);
		}
		System.out.println("handleIncomingPacket:"+x+":"+y+":"+z+":"+name);
		td.pos.setTo(x, y, z);
		td.setName(name);
		td.defaultTarget = target;
		if(type==0)
			td.dimension = dimension;
	}
	
}