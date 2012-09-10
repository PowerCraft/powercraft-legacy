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
	
	public static Minecraft mc = PC_Utils.mc();
	
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

	public static boolean isTargetInThisDimension(String identifier) {
		return mc.thePlayer.dimension == getTargetDimension(identifier);
	}

	public static int getTargetDimension(String identifier) {
		if (identifier.equals("")) {
			return 0;
		}

		PCnt_TeleporterData entry = getTarget(identifier);
		if (entry == null) {
			return 0;
		}

		return entry.dimension;
	}

	// ------------- TESTS -------------
	private static final boolean isTeleporter(int id) {
		return id == mod_PCnet.teleporter.blockID;
	}

	// ----- ENTITY MANIPULATION ------

	public static boolean teleportEntityTo(Entity entity, String target) {

		if(target==null||target.equals(""))
			return false;
		if(!entity.worldObj.isRemote) return false;
		
		System.out.println("teleport "+entity+" to "+target);
		
		PCnt_TeleporterData tdt = getTarget(target);
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
		
		//if (world.getBlockId(tc.x, tc.y, tc.z) != mod_PCtransport.teleporter.blockID) {
		//	System.out.println("Faild bc mod_PCtransport.teleporter.blockID");
		//	return false;
		//}

		PCnt_TeleporterData td = getTeleporterDataAt(tc.x, tc.y, tc.z);
		
		if (td == null) {
			System.out.println("Faild bc td");
			return false;
		}
		
		System.out.println(tc);

		if(teleportTo(entity, td.pos.x, td.pos.y, td.pos.z)){
			return true;
		}else{
		/*
		if(entity.worldObj.worldInfo.getDimension()!=tdt.dimension&&entity instanceof EntityPlayerMP){
			System.out.println("tp dimension");
			((EntityPlayerMP)entity).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)entity, tdt.dimension);
		}
		
		if(entity.worldObj!=world){
			System.out.println("Set World");
			entity.setWorld(MinecraftServer.getServer().worldServerForDimension(tdt.dimension));
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
	*/
			return false;
		}
	}
	
    public static boolean teleportTo(Entity entity, double par1, double par3, double par5){
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

        if (entity.worldObj.blockExists(var14, var15, var16))
        {
            boolean var17 = false;

            while (!var17 && var15 > 0)
            {
                var18 = entity.worldObj.getBlockId(var14, var15 - 1, var16);

                if (var18 != 0 && Block.blocksList[var18].blockMaterial.blocksMovement())
                {
                    var17 = true;
                }
                else
                {
                    --entity.posY;
                    --var15;
                }
            }

            if (var17)
            {
                entity.setPosition(entity.posX, entity.posY, entity.posZ);

                if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty() && !entity.worldObj.isAnyLiquid(entity.boundingBox))
                {
                    var13 = true;
                }
            }
        }

        if (!var13)
        {
            entity.setPosition(var7, var9, var11);
            return false;
        }
        else
        {
            short var30 = 128;

            for (var18 = 0; var18 < var30; ++var18)
            {
                double var19 = (double)var18 / ((double)var30 - 1.0D);
                float var21 = (entity.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (entity.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (entity.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (entity.posX - var7) * var19 + (entity.rand.nextDouble() - 0.5D) * (double)entity.width * 2.0D;
                double var26 = var9 + (entity.posY - var9) * var19 + entity.rand.nextDouble() * (double)entity.height;
                double var28 = var11 + (entity.posZ - var11) * var19 + (entity.rand.nextDouble() - 0.5D) * (double)entity.width * 2.0D;
                entity.worldObj.spawnParticle("portal", var24, var26, var28, (double)var21, (double)var22, (double)var23);
            }
            return true;
        }
    }

	public static PCnt_TeleporterData getTeleporterDataAt(int xCoord,
			int yCoord, int zCoord) {
		for(PCnt_TeleporterData td:tm.teleporterData){
			if(td.pos.x==xCoord&&td.pos.y==yCoord&&td.pos.z==zCoord)
				return td;
		}
		return null;
	}

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
		int type = (Integer)o[0];
		int x=(Integer)o[1];
		int y=(Integer)o[2];
		int z=(Integer)o[3];
		String name=(String)o[4];
		String target=(String)o[5];
		int dimension=0;
		if(type==0)
			dimension = (Integer)o[6];
		PCnt_TeleporterData td = getTeleporterDataAt(x, y, z);
		if(td==null){
			td = new PCnt_TeleporterData();
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