package powercraft.api;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

@SuppressWarnings("unused") 
public class PC_PacketHandler implements IPacketHandler {

	public static final int BLOCKDATA = 1, BLOCKMESSAGE = 2, GUIOPEN = 3, PACKETHANDLER = 4, PERMISSION_THINGS=5;

	private static HashMap<String, PC_IPacketHandler> packetHandlers = new HashMap<String, PC_IPacketHandler>();

	public static void registerPacketHandler(String name, PC_IPacketHandler packetHandler){
		packetHandlers.put(name, packetHandler);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {

		DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		EntityPlayer entityPlayer = ((EntityPlayer) player);
		World world = entityPlayer.worldObj;
		try {
			int packetType = dataInputStream.readInt();
			switch (packetType) {
				case BLOCKDATA:
					packetBlockData(world, entityPlayer, dataInputStream);
					break;
				case BLOCKMESSAGE:
					packetBlockMessage(world, entityPlayer, dataInputStream);
					break;
				case GUIOPEN:
					packetGuiOpen(world, entityPlayer, dataInputStream);
					break;
				case PACKETHANDLER:
					packetPacketHandler(world, entityPlayer, dataInputStream);
					break;
				case PERMISSION_THINGS:
					packetPermissionThings(world, entityPlayer, dataInputStream);
					break;
				default:
					PC_Logger.severe("Unknown packet type %s", packetType);
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while reading packet");
		}
	}

	protected void packetPacketHandler(World world, EntityPlayer entityPlayer, DataInputStream dataInputStream) throws IOException {
		String packetHandlerName = dataInputStream.readUTF();
		PC_IPacketHandler packetHandler = packetHandlers.get(packetHandlerName);
		if(packetHandler==null){
			PC_Logger.severe("Can't find packethandler %s", packetHandlerName);
		}else{
			packetHandler.handlePacket(world, entityPlayer, readNBTTagCompound(dataInputStream));
		}
	}

	protected void packetGuiOpen(World world, EntityPlayer player, DataInputStream dataInputStream) throws IOException {

		PC_Logger.severe("Client %s tries to open gui", player.username);
	}

	protected void packetBlockData(World world, EntityPlayer player, DataInputStream dataInputStream) throws IOException {

		int x = dataInputStream.readInt();
		int y = dataInputStream.readInt();
		int z = dataInputStream.readInt();
		Block block = PC_Utils.getBlock(world, x, y, z);
		if (block instanceof PC_Block) ((PC_Block) block).loadFromClientNBTPacket(world, x, y, z, readNBTTagCompound(dataInputStream), player);
	}


	protected void packetBlockMessage(World world, EntityPlayer player, DataInputStream dataInputStream) throws IOException {

		int x = dataInputStream.readInt();
		int y = dataInputStream.readInt();
		int z = dataInputStream.readInt();
		Block block = PC_Utils.getBlock(world, x, y, z);
		((PC_Block) block).onBlockMessage(world, x, y, z, player, readNBTTagCompound(dataInputStream));
	}

	protected void packetPermissionThings(World world, EntityPlayer player, DataInputStream dataInputStream) throws IOException {
		
		int x = dataInputStream.readInt();
		int y = dataInputStream.readInt();
		int z = dataInputStream.readInt();
		Block block = PC_Utils.getBlock(world, x, y, z);
		((PC_Block) block).handleClientPermissionThings(world, x, y, z, readNBTTagCompound(dataInputStream), player);
	}

	public static Packet250CustomPayload getPowerCraftPacket(byte[] byteArray) {

		return new Packet250CustomPayload("PowerCraft", byteArray);
	}

	public static Packet250CustomPayload getPacketHandlerPacket(String packetHandler, NBTTagCompound nbtTagCompound){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeInt(PACKETHANDLER);
			dataOutputStream.writeUTF(packetHandler);
			writeNBTTagCompound(dataOutputStream, nbtTagCompound);
			return getPowerCraftPacket(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while generating packet");
		}
		return null;
	}
	
	public static Packet250CustomPayload getBlockMessagePacket(World world, int x, int y, int z, NBTTagCompound nbtTagCompound){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeInt(BLOCKMESSAGE);
			dataOutputStream.writeInt(x);
			dataOutputStream.writeInt(y);
			dataOutputStream.writeInt(z);
			writeNBTTagCompound(dataOutputStream, nbtTagCompound);
			return getPowerCraftPacket(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while generating packet");
		}
		return null;
	}
	
	public static Packet250CustomPayload getBlockDataPacket(World world, int x, int y, int z) {

		Block block = PC_Utils.getBlock(world, x, y, z);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeInt(BLOCKDATA);
			dataOutputStream.writeInt(x);
			dataOutputStream.writeInt(y);
			dataOutputStream.writeInt(z);
			NBTTagCompound nbtTagCompound = new NBTTagCompound("save");
			((PC_Block) block).saveToNBTPacket(world, x, y, z, nbtTagCompound);
			writeNBTTagCompound(dataOutputStream, nbtTagCompound);
			return getPowerCraftPacket(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while generating packet");
		}
		return null;
	}


	public static Packet250CustomPayload getGuiPacket(PC_TileEntity tileEntity, int windowId) {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeInt(GUIOPEN);
			dataOutputStream.writeInt(tileEntity.xCoord);
			dataOutputStream.writeInt(tileEntity.yCoord);
			dataOutputStream.writeInt(tileEntity.zCoord);
			dataOutputStream.writeInt(windowId);
			NBTTagCompound nbtTagCompound = new NBTTagCompound("save");
			tileEntity.saveToGuiNBTPacket(nbtTagCompound);
			writeNBTTagCompound(dataOutputStream, nbtTagCompound);
			return getPowerCraftPacket(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while generating packet");
		}
		return null;
	}


	public static Packet250CustomPayload getGuiPacket(String guiOpenHandlerName, int windowId) {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeInt(GUIOPEN);
			dataOutputStream.writeInt(0);
			dataOutputStream.writeInt(-1);
			dataOutputStream.writeUTF(guiOpenHandlerName);
			dataOutputStream.writeInt(windowId);
			return getPowerCraftPacket(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while generating packet");
		}
		return null;
	}


	public static Packet250CustomPayload getGuiPacket(int itemID, int windowId) {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeInt(GUIOPEN);
			dataOutputStream.writeInt(0);
			dataOutputStream.writeInt(-2);
			dataOutputStream.writeInt(itemID);
			dataOutputStream.writeInt(windowId);
			return getPowerCraftPacket(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while generating packet");
		}
		return null;
	}


	public static Packet250CustomPayload getPermissionThingsPacket(World world, int x, int y, int z, NBTTagCompound nbtTagCompound) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
		try {
			dataOutputStream.writeInt(PERMISSION_THINGS);
			dataOutputStream.writeInt(x);
			dataOutputStream.writeInt(y);
			dataOutputStream.writeInt(z);
			writeNBTTagCompound(dataOutputStream, nbtTagCompound);
			return getPowerCraftPacket(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			PC_Logger.severe("Error while generating packet");
		}
		return null;
	}
	
	public static void sendPacketToAllInDimension(Packet250CustomPayload packet, int dimension) {

		PacketDispatcher.sendPacketToAllInDimension(packet, dimension);
	}


	public static void sendPacketToPlayer(Packet250CustomPayload packet, EntityPlayer player) {

		PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
	}

	public static void sendPacketToServer(Packet250CustomPayload packet) {
		PacketDispatcher.sendPacketToServer(packet);
	}

	public static NBTTagCompound readNBTTagCompound(DataInputStream dataInputStream) throws IOException {

		int size = dataInputStream.readInt();
		byte[] buffer = new byte[size];
		int pos = 0;
		while (dataInputStream.read(buffer, pos, size - pos) < size - pos) {}
		return CompressedStreamTools.decompress(buffer);
	}


	public static void writeNBTTagCompound(DataOutputStream dataOutputStream, NBTTagCompound nbtTagCompound) throws IOException {

		byte[] buffer = CompressedStreamTools.compress(nbtTagCompound);
		dataOutputStream.writeInt(buffer.length);
		dataOutputStream.write(buffer);
	}

}
