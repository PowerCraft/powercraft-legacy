package powercraft.api.multiblocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.api.PC_IPacketHandler;
import powercraft.api.PC_PacketHandler;

public class PC_MultiblockHelper implements PC_IPacketHandler {

	public static final String PACKETHANDLERNAME = "MultiblockPacketHandler";
	private static HashMap<DiggingInfoKey, Integer> diggings = new HashMap<DiggingInfoKey, Integer>();
	
	public static void init(){
		PC_PacketHandler.registerPacketHandler(PACKETHANDLERNAME, new PC_MultiblockHelper());
	}
	
	private static void removePlayerOutOfList(EntityPlayer entityPlayer){
		Iterator<Entry<DiggingInfoKey, Integer>> i = diggings.entrySet().iterator();
		while(i.hasNext()){
			Entry<DiggingInfoKey, Integer> e = i.next();
			if(e.getKey().player==entityPlayer){
				i.remove();
			}
		}
	}
	
	private static void handlePacketA(World world, EntityPlayer entityPlayer, NBTTagCompound packet){
		boolean digging = packet.getBoolean("digging");
		int dimension = world.getWorldInfo().getVanillaDimension();
		removePlayerOutOfList(entityPlayer);
		if(digging){
			int x = packet.getInteger("x");
			int y = packet.getInteger("y");
			int z = packet.getInteger("z");
			int index = packet.getInteger("index");
			DiggingInfoKey dik = new DiggingInfoKey(entityPlayer, x, y, z, dimension);
			diggings.put(dik, index);
		}
		if(!world.isRemote){
			PC_PacketHandler.sendPacketToAllInDimension(PC_PacketHandler.getPacketHandlerPacket(PACKETHANDLERNAME, packet), dimension);
		}
	}
	
	public static Integer getIndexFromPlayerCoord(World world, int x, int y, int z, EntityPlayer entityPlayer){
		return diggings.get(new DiggingInfoKey(entityPlayer, x, y, z, world.getWorldInfo().getVanillaDimension()));
	}
	
	public static void setPlayerIndex(World world, int x, int y, int z, EntityPlayer entityPlayer, int index){
		removePlayerOutOfList(entityPlayer);
		diggings.put(new DiggingInfoKey(entityPlayer, x, y, z, world.getWorldInfo().getVanillaDimension()), index);
		NBTTagCompound packet = new NBTTagCompound();
		packet.setBoolean("digging", true);
		packet.setInteger("x", x);
		packet.setInteger("y", y);
		packet.setInteger("z", z);
		packet.setInteger("index", index);
		PC_PacketHandler.sendPacketToServer(PC_PacketHandler.getPacketHandlerPacket(PACKETHANDLERNAME, packet));
	}
	
	@Override
	public void handlePacket(World world, EntityPlayer entityPlayer, NBTTagCompound packet) {
		handlePacketA(world, entityPlayer, packet);
	}
	
	private static class DiggingInfoKey{
		
		public EntityPlayer player;
		public int x;
		public int y;
		public int z;
		public int dimension;
		
		public DiggingInfoKey(EntityPlayer player, int x, int y, int z, int dimension) {
			super();
			this.player = player;
			this.x = x;
			this.y = y;
			this.z = z;
			this.dimension = dimension;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + dimension;
			result = prime * result
					+ ((player == null) ? 0 : player.hashCode());
			result = prime * result + x;
			result = prime * result + y;
			result = prime * result + z;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DiggingInfoKey other = (DiggingInfoKey) obj;
			if (dimension != other.dimension)
				return false;
			if (player == null) {
				if (other.player != null)
					return false;
			} else if (!player.equals(other.player))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			if (z != other.z)
				return false;
			return true;
		}
		
	}
	
}
