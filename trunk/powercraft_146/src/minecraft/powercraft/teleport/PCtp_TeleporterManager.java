package powercraft.teleport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet34EntityTeleport;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IDataHandler;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_PacketHandler;
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
			int entityID = (Integer)o[1];
			PC_VecF pos = (PC_VecF)o[2];
			Entity entity = player.worldObj.getEntityByID(entityID);
			entity.setLocationAndAngles(pos.x, pos.y, pos.z, 0, 0);
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
			if(td.dimension == dimension && pos.equals(td.pos))
				return td;
		}
		return null;
	}

	

	public static boolean teleportEntityTo(Entity entity, PCtp_TeleporterData td) {

		return false;
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
