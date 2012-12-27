package powercraft.teleport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecF;
import powercraft.management.PC_VecI;

public class PCtp_TeleporterManager implements PC_IDataHandler, PC_IPacketHandler {

	private static List<PCtp_TeleporterData> teleoprter = new ArrayList<PCtp_TeleporterData>();
	private static boolean needSave;
	
	@Override
	public void load(NBTTagCompound nbtTag) {
		teleoprter.clear();
		int num = nbtTag.getInteger("count");
		for(int i=0; i<num; i++){
			PCtp_TeleporterData td = new PCtp_TeleporterData();
			SaveHandler.loadFromNBT(nbtTag, "value["+i+"]", td);
			teleoprter.add(td);
		}
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbtTag) {
		needSave = false;
		nbtTag.setInteger("count", teleoprter.size());
		int i=0;
		for(PCtp_TeleporterData td:teleoprter){
			SaveHandler.saveToNBT(nbtTag, "value["+i+"]", td);
			i++;
		}
		return nbtTag;
	}

	@Override
	public boolean needSave() {
		return needSave;
	}
	
	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		String msg = (String)o[0];
		if(msg.equals("set")){
			PCtp_TeleporterData td = (PCtp_TeleporterData)o[1];
			String target = (String)o[2];
			PCtp_TeleporterData td2 = getTargetByName(target);
			if(td2==null){
				td.defaultTarget = null;
				td.defaultTargetDimension = 0;
			}else{
				td.defaultTarget = td2.pos;
				td.defaultTargetDimension = td2.dimension;
			}
			td2 = getTeleporterData(td.dimension, td.pos);
			td2.setTo(td);
		}else if(msg.equals("teleport")){
			System.out.println("Teleport");
			int entityID = (Integer)o[1];
			PC_VecF pos = (PC_VecF)o[2];
			Entity entity = player.worldObj.getEntityByID(entityID);
			if(entity!=null){
				entity.setLocationAndAngles(pos.x, pos.y, pos.z, 0, 0);
				entity.motionX = 0;
				entity.motionY = 0;
				entity.motionZ = 0;
				player.worldObj.updateEntityWithOptionalForce(entity, true);
			}
		}
		return false;
	}

	public static void openGui(EntityPlayer entityPlayer, int x, int y, int z){
		int dimension = entityPlayer.dimension;
		openGui(entityPlayer, getTeleporterData(dimension, new PC_VecI(x, y, z)));
	}
	
	public static void openGui(EntityPlayer entityPlayer, PCtp_TeleporterData td) {
		List<String> names = getTargetNames();
		String defaultTarget=null;
		if(td.defaultTarget!=null){
			PCtp_TeleporterData td2 = getTeleporterData(td.defaultTargetDimension, td.defaultTarget);
			defaultTarget = td2.name;
		}
		Gres.openGres("Teleporter", entityPlayer, td, names, defaultTarget);
	}
	
	public static void registerTeleporterData(int dimension, PC_VecI pos, PCtp_TeleporterData td){
		releaseTeleporterData(dimension, pos);
		td.dimension = dimension;
		td.pos = pos;
		teleoprter.add(td);
		needSave = true;
	}
	
	public static void releaseTeleporterData(int dimension, PC_VecI pos){
		teleoprter.remove(getTeleporterData(dimension, pos));
		needSave = true;
	}
	
	public static PCtp_TeleporterData getTeleporterData(int dimension, PC_VecI pos){
		for(PCtp_TeleporterData td:teleoprter){
			if(td.dimension == dimension && pos.equals(td.pos)){
				needSave = true;
				return td;
			}
		}
		return null;
	}

	private static PC_VecI calculatePos(World world, PCtp_TeleporterData to){
		int rotation;
		int meta;
		int good = 0;
		PC_VecI tc = to.pos;
		PC_VecI coords[] = { new PC_VecI(0, 0, -1), new PC_VecI(+1, 0, 0), new PC_VecI(0, 0, +1), new PC_VecI(-1, 0, 0) };
		
		int hig=0;
		PC_VecI out=null;
		
		for (int i = 0; i < 4; i++) {

			PC_VecI tmp = tc.offset(coords[i]);

			good=0;
			
			Block b = GameInfo.getBlock(world, tmp);
			
			if(b instanceof PC_Block){
				PC_Block pcb = (PC_Block)b;
				if("Transport".equalsIgnoreCase(pcb.getModule().getName())){
					meta = GameInfo.getMD(world, tmp);
					rotation = 0;
					switch (meta)
			        {
			            case 1:
			            case 7:
			            	rotation = 1;

			            case 8:
			            case 14:
			            	rotation = 2;

			            case 9:
			            case 15:
			            	rotation = 3;
			        }
					if (rotation == i) {
						// good rotation, 3 points
						good = 3;
					} else if (rotation != ((i + 2) % 4)) {
						// not reverse rotation, 2 points
						good = 2;
					}
				}
			}

			if (to.direction == i) {
				good += 1;
			}

			if(b instanceof PC_Block){
				PC_Block pcb = (PC_Block)b;
				if("Teleport".equalsIgnoreCase(pcb.getModule().getName())){
					good = 0;
				}
			}

			if(hig<good){
				hig = good;
				out = tmp;
			}
		}
		return out;
	}
	
	private static boolean teleportTo(Entity entity, PC_Struct2<PC_VecI, Integer> s){
		if(entity instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)entity;

            if (!player.playerNetServerHandler.connectionClosed)
            {
            	player.playerNetServerHandler.setPlayerLocation(s.a.x+0.5, s.a.y, s.a.z+0.5, s.b, 0.0F);
            	player.fallDistance = 0.0F;
            }
            return true;
		}

        entity.setLocationAndAngles(s.a.x + 0.5, s.a.y + 0.1, s.a.z + 0.5, 0, 0);
		entity.motionX = 0;
		entity.motionY = 0;
		entity.motionZ = 0;
		PC_PacketHandler.sendToPacketHandler(true, entity.worldObj, "Teleporter", "teleport", entity.entityId, 
				new PC_VecF((float)entity.posX, (float)entity.posY, (float)entity.posZ), s.b);
		entity.worldObj.updateEntityWithOptionalForce(entity, true);
        return true;
    }

	public static boolean teleportEntityTo(Entity entity, PCtp_TeleporterData td) {
		if(td.defaultTarget == null)
			return false;
		PCtp_TeleporterData to = getTeleporterData(td.defaultTargetDimension, td.defaultTarget);
		if(to==null)
			return false;
		PC_Struct2<PC_VecI, Integer> s = calculatePos(entity.worldObj, to);;
		return teleportTo(entity, s);
	}
	
	public static List<String> getTargetNames() {
		List<String> names = new ArrayList<String>();
		for(PCtp_TeleporterData td:teleoprter){
			if(td.name!=null && !td.name.equals(""))
				names.add(td.name);
		}
		return names;
	}

	public static boolean isNameOk(String text) {
		return !getTargetNames().contains(text);
	}

	public static PCtp_TeleporterData getTargetByName(String name) {
		for(PCtp_TeleporterData td:teleoprter){
			if(name.equals(td.name)){
				return td;
			}
		}
		return null;
	}
	
}
