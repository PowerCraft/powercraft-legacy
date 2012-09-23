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

public class PCnt_TeleporterManager extends PC_PacketHandler implements PC_INBTWD{

	private static List<PCnt_TeleporterData> teleporterData = new ArrayList<PCnt_TeleporterData>();

	private static PCnt_TeleporterManager tm = null;
	
	private static boolean needsSave=false;
	
	public static PCnt_TeleporterManager getTeleporterManager(){
		if(tm==null){
			tm = new PCnt_TeleporterManager();
		}
		return tm;
	}
	
	private PCnt_TeleporterManager(){
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
			PCnt_TeleporterData td = new PCnt_TeleporterData();
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
		for(PCnt_TeleporterData td:teleporterData)
			if(td.needsSave())
				return true;
		return false;
	}

	public static void remove(PCnt_TeleporterData td){
		needsSave=true;
		teleporterData.remove(td);
	}

	public static void add(PCnt_TeleporterData td){
		if(!teleporterData.contains(td))
			teleporterData.add(td);
	}
	
	// ------ GETTERS ----------

	private static PCnt_TeleporterData getTarget(String target){
		for(PCnt_TeleporterData td : tm.teleporterData)
			if(td.getName().equals(target))
				return td;
		return null;
	}
	
	private static PC_CoordI getTargetCoord(String identifier) {
		PCnt_TeleporterData e = getTarget(identifier);
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

		PCnt_TeleporterData entry = getTarget(identifier);
		return entry != null && !entry.pos.equals(coord);
	}

	// ------------- TESTS -------------
	private static final boolean isTeleporter(int id) {
		return id == mod_PCnet.teleporter.blockID;
	}
	
	private static PC_CoordI calculatePos(World world, PCnt_TeleporterData tdt, String defaultD){
		int rotation;
		int meta;
		int good = 0;
		PC_CoordI tc = tdt.pos;
		PC_CoordI coords[] = { new PC_CoordI(0, 0, -1), new PC_CoordI(+1, 0, 0), new PC_CoordI(0, 0, +1), new PC_CoordI(-1, 0, 0) };
		String[] side = { "N", "E", "S", "W" };
		
		int hig=0;
		PC_CoordI out=null;
		
		for (int i = 0; i < 4; i++) {

			PC_CoordI tmp = tc.offset(coords[i]);

			good=0;
			
			if (PC_BlockUtils.hasFlag(world, tmp, "BELT") && !PC_BlockUtils.hasFlag(world, tmp, "TELEPORTER")) {

				meta = tmp.getMeta(world);
				rotation = PCtr_BeltBase.getRotation(meta);
				if (rotation == i) {
					// good rotation, 3 points
					good = 3;
				} else if (rotation != ((i + 2) % 4)) {
					// not reverse rotation, 2 points
					good = 2;
				}

			} else if (!PCtr_BeltBase.isBlocked(world, tmp)) {
				// can pass through, 1 point
				good = 1;
			}

			if (defaultD.equals(side[i])) {
				good += 1;
			}

			if (PC_BlockUtils.hasFlag(world, tmp, "TELEPORTER")) {
				good = 0;
			}

			if(hig<good){
				hig = good;
				out = tmp;
			}
		}
		return out;
	}
	
	private static boolean changeEntityWorld(Entity entity, int d){
		System.out.print("changing to level "+entity.worldObj.worldInfo.getDimension()+" to "+d+"... ");
		if(entity instanceof EntityPlayerMP){
			EntityPlayerMP player = (EntityPlayerMP)entity;
			if(player.dimension==d)
				return true;
			//if(d==-1&&!player.mcServer.getAllowNether())
				//return false;
			System.out.print("teleporting... ");
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, d);
			player.removeExperience(0);
			try {
				ModLoader.setPrivateValue(EntityPlayerMP.class, player, 8, -1);
				ModLoader.setPrivateValue(EntityPlayerMP.class, player, 9, -1);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception");
				return false;
			}
			if (player.isEntityAlive())
				player.worldObj.updateEntityWithOptionalForce(player, true);
			System.out.println("OK");
			return true;
		}
		if(entity.worldObj.worldInfo.getDimension() == d){
			System.out.println("OK");
			return true;
		}
		System.out.println("FAIL");
		return false;
	}
	
	private static boolean teleportTo(Entity entity, double par1, double par3, double par5){
		if(entity instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)entity;

            if (!player.serverForThisPlayer.serverShuttingDown)
            {
            	player.serverForThisPlayer.setPlayerLocation(par1, par3, par5, 0.0F, 0.0F);
            	player.fallDistance = 0.0F;
            }
            return true;
		}
        double var7 = entity.posX;
        double var9 = entity.posY;
        double var11 = entity.posZ;
        entity.posX = par1;
        entity.posY = par3;
        entity.posZ = par5;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(entity.posX);
        int var15 = MathHelper.floor_double(entity.posY);
        int var16 = MathHelper.floor_double(entity.posZ);
        int var18;

        entity.setPosition(entity.posX, entity.posY, entity.posZ);

        return true;
    }
	
	// ----- ENTITY MANIPULATION ------

	public static boolean teleportEntityTo(Entity entity, String target) {

		if(target==null||target.equals(""))
			return false;
		if(entity==null||entity.worldObj.isRemote) 
			return false;
		PCnt_TeleporterData tdt = getTarget(target);
		if(tdt== null)
			return false;
		PC_CoordI tc = tdt.pos;
		if (tc == null)
			return false;
		if(!changeEntityWorld(entity, tdt.dimension))
			return false;
		World world = PC_Utils.mcs().worldServerForDimension(tdt.dimension);
		PC_CoordI pos = calculatePos(world, tdt, tdt.direction);
		if (pos == null)
			return false;
		return teleportTo(entity, pos.x+0.5, pos.y, pos.z+0.5);
	}

	public static PCnt_TeleporterData getTeleporterDataAt(int dimension, int xCoord,
			int yCoord, int zCoord) {
		for(PCnt_TeleporterData td:tm.teleporterData){
			if(td.pos.x==xCoord&&td.pos.y==yCoord&&td.pos.z==zCoord&&dimension==td.dimension)
				return td;
		}
		return null;
	}
	
	/*public static PCnt_TeleporterData getTeleporterDataAt(World world, int xCoord,
			int yCoord, int zCoord) {
		return getTeleporterDataAt(world.worldInfo.getDimension(), xCoord, yCoord, zCoord);
	}*/

	public static PCnt_TileEntityTeleporter getTeleporterAt(World world, int xCoord,
			int yCoord, int zCoord) {
		TileEntity te = world.getBlockTileEntity(xCoord, yCoord, zCoord);
		if(te instanceof PCnt_TileEntityTeleporter)
			return (PCnt_TileEntityTeleporter)te;
		return null;
	}

	public static List<String> getTargetNames() {
		List<String> names = new ArrayList<String>();
		for(PCnt_TeleporterData td:teleporterData)
			if(!(td.getName()==null||"".equals(td.getName())))
				names.add(td.getName());
		return names;
	}
	
	public static boolean isNameOk(String name){
		return !getTargetNames().contains(name);
	}

	@Override
	public void handleIncomingPacket(World world, Object[] o) {
		int x=(Integer)o[0];
		int y=(Integer)o[1];
		int z=(Integer)o[2];
		String name=(String)o[3];
		String target=(String)o[4];
		int dimension = (Integer)o[5];
		PCnt_TeleporterData td = getTeleporterDataAt(dimension, x, y, z);
		if(td==null){
			td = new PCnt_TeleporterData();
			teleporterData.add(td);
		}
		System.out.println("handleIncomingPacket:"+dimension+":"+x+":"+y+":"+z+":"+name);
		td.pos.setTo(x, y, z);
		td.setName(name);
		td.defaultTarget = target;
		td.dimension = dimension;
	}
	
}