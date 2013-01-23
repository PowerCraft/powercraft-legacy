package codechicken.core;

import java.io.*;
import java.util.HashMap;

import codechicken.core.PacketCustom.ICustomPacketHandler.IClientPacketHandler;
import codechicken.core.PacketCustom.ICustomPacketHandler.IServerPacketHandler;
import codechicken.core.data.MCDataInput;
import codechicken.core.data.MCDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.liquids.LiquidStack;

public final class PacketCustom implements MCDataInput, MCDataOutput
{
	public static interface ICustomPacketHandler 
	{
		public interface IClientPacketHandler extends ICustomPacketHandler
		{
			public void handlePacket(PacketCustom packetCustom, NetClientHandler nethandler, Minecraft mc);
		}
		
		public interface IServerPacketHandler extends ICustomPacketHandler
		{
			public void handlePacket(PacketCustom packetCustom, NetServerHandler nethandler, EntityPlayerMP sender);
		}
	}
	
	private static abstract class CustomPacketHandler implements IPacketHandler
	{
		HashMap<Integer, ICustomPacketHandler> handlermap = new HashMap<Integer, ICustomPacketHandler>();
		
		public CustomPacketHandler(String channel) 
		{
			NetworkRegistry.instance().registerChannel(this, channel, getSide());
		}

		@Override
		public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
		{
			PacketCustom packetCustom = new PacketCustom(packet);
			ICustomPacketHandler handler = handlermap.get(packetCustom.type);
			if(handler != null)
				handle(handler, packetCustom, player);
		}

		public void registerRange(int firstID, int lastID, ICustomPacketHandler handler) 
		{
			for(int i = firstID; i <= lastID; i++)
			{
				handlermap.put(i, handler);
			}
		}
		
		public abstract Side getSide();
		public abstract void handle(ICustomPacketHandler handler, PacketCustom packet, Player player);
	}
	
	private static class ClientPacketHander extends CustomPacketHandler
	{
		public ClientPacketHander(String channel) 
		{
			super(channel);
		}

		@Override
		public Side getSide() 
		{
			return Side.CLIENT;
		}

		@Override
		public void handle(ICustomPacketHandler handler, PacketCustom packet, Player player) 
		{
			((IClientPacketHandler)handler).handlePacket(packet, Minecraft.getMinecraft().getSendQueue(), Minecraft.getMinecraft());
		}
	}
	
	private static class ServerPacketHander extends CustomPacketHandler
	{
		public ServerPacketHander(String channel) 
		{
			super(channel);
		}

		@Override
		public Side getSide() 
		{
			return Side.SERVER;
		}

		@Override
		public void handle(ICustomPacketHandler handler, PacketCustom packet, Player player) 
		{
			((IServerPacketHandler)handler).handlePacket(packet, ((EntityPlayerMP)player).playerNetServerHandler, (EntityPlayerMP)player);
		}
	}
	
	private PacketCustom(Packet250CustomPayload packet)
	{
		incoming = true;
		channel = packet.channel;

		datain = new DataInputStream(new ByteArrayInputStream(packet.data));
		type = readUnsignedByte();
	}
	
	public PacketCustom(String channel, int type)
	{
		this.channel = channel;
		this.type = type;
		incoming = false;
		isChunkDataPacket = false;
		
		dataarrayout = new ByteArrayOutputStream();
		dataout = new DataOutputStream(dataarrayout);
		writeByte(type);
	}

	public int getType()
	{
		return type;
	}
	
	public void setChunkDataPacket()
	{
		isChunkDataPacket = true;
	}
	
	public Packet250CustomPayload toPacket()
	{
		if(type == 0)
		{
			FMLCommonHandler.instance().raiseException(new IllegalStateException("Type not set"), "Custom Packet", true);
			return null;
		}
		if(incoming)
		{
			FMLCommonHandler.instance().raiseException(new IllegalStateException("Tried to write an incoming packet"), "Custom Packet", true);
			return null;
		}
		
		Packet250CustomPayload payload = new Packet250CustomPayload();
		payload.channel = channel;
		payload.isChunkDataPacket = isChunkDataPacket;
		payload.data = dataarrayout.toByteArray();
		payload.length = payload.data.length;
		
		return payload;
	}
	
	public void writeBoolean(boolean b)
	{
		try
		{
			dataout.writeBoolean(b);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeByte(int b)
	{
		try
		{
			dataout.writeByte(b);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeShort(int s)
	{
		try
		{
			dataout.writeShort(s);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeInt(int i)
	{
		try
		{
			dataout.writeInt(i);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeFloat(float f)
	{
		try
		{
			dataout.writeFloat(f);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeDouble(double d)
	{
		try
		{
			dataout.writeDouble(d);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeLong(long l)
	{
		try
		{
			dataout.writeLong(l);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
		
	@Override
	public void writeChar(char c)
	{
	    try
        {
            dataout.writeChar(c);
        }
        catch(IOException e)
        {
            FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
        }
	}
	
	public void writeByteArray(byte[] barray)
	{
		try
		{
			dataout.write(barray);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeCoord(int x, int y, int z)
	{
		writeInt(x);
		writeInt(y);
		writeInt(z);
	}
	
    public void writeCoord(BlockCoord coord)
    {
        writeInt(coord.x);
        writeInt(coord.y);
        writeInt(coord.z);
    }
	
	public void writeString(String s)
	{
		try
		{
			if(s.length() > 65535)
			{
				throw new IOException("String length: "+s.length()+"too long.");
			}
			dataout.writeShort(s.length());
			dataout.writeChars(s);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}
	
	public void writeItemStack(ItemStack spawnstack)
	{
		if (spawnstack == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(spawnstack.itemID);
            writeByte(spawnstack.stackSize);
            writeShort(spawnstack.getItemDamage());
            writeNBTTagCompound(spawnstack.stackTagCompound);
        }
	}
		
	public void writeNBTTagCompound(NBTTagCompound compound)
	{
		try
		{			
			if (compound == null)
	        {
	            writeShort(-1);
	        }
	        else
	        {
	            byte[] var3 = CompressedStreamTools.compress(compound);
	            writeShort((short)var3.length);
	            writeByteArray(var3);
	        }
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
		}
	}

	public void writeLiquidStack(LiquidStack liquid)
	{
		if (liquid == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(liquid.itemID);
            writeInt(liquid.amount);
            writeShort(liquid.itemMeta);
        }
	}

	public boolean readBoolean()
	{
		try
		{
			return datain.readBoolean();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return false;
		}
	}
	
	public int readUnsignedByte()
	{
		return readByte() & 0xFF;
	}
	
	public int readUnsignedShort()
	{
		return readShort() & 0xFFFF;
	}
	
	public byte readByte()
	{
		try
		{
			return datain.readByte();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return 0;
		}
	}
	
	public short readShort()
	{
		try
		{
			return datain.readShort();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return 0;
		}
	}
	
	public int readInt()
	{
		try
		{
			return datain.readInt();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return 0;
		}
	}
	
	public float readFloat()
	{
		try
		{
			return datain.readFloat();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return 0;
		}
	}
	
	public double readDouble()
	{
		try
		{
			return datain.readDouble();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return 0;
		}
	}
	
	public long readLong()
	{
		try
		{
			return datain.readLong();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return 0;
		}
	}
	
	public char readChar()
	{
		try
		{
			return datain.readChar();
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return 0;
		}
	}
	
	public BlockCoord readCoord()
	{
	    return new BlockCoord(readInt(), readInt(), readInt());
	}
	
	public byte[] readByteArray(int length)
	{
		try
		{
			byte[] barray = new byte[length];
			datain.readFully(barray, 0, length);
			return barray;
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return null;
		}
	}
	
	public String readString()
	{
		try
		{
			int length = datain.readUnsignedShort();
			char[] chars = new char[length];
			for(int i = 0; i < length; i++)
			{
				chars[i] = readChar();
			}
			return new String(chars);
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return null;
		}
		
	}
	
	public ItemStack readItemStack()
	{
		ItemStack var2 = null;
        short itemID = readShort();

        if (itemID >= 0)
        {
            byte stackSize = readByte();
            short damage = readShort();
            var2 = new ItemStack(itemID, stackSize, damage);
            var2.stackTagCompound = readNBTTagCompound();
        }

        return var2;
	}
	
	public NBTTagCompound readNBTTagCompound()
	{
		try
		{
			short var2 = readShort();
	
	        if (var2 < 0)
	        {
	            return null;
	        }
	        else
	        {
	            byte[] var3 = readByteArray(var2);
	            return CompressedStreamTools.decompress(var3);
	        }
		}
		catch(IOException e)
		{
			FMLCommonHandler.instance().raiseException(e, "Custom Packet", true);
			return null;
		}
	}
		
	public LiquidStack readLiquidStack()
	{
		LiquidStack var2 = null;
        short liquidID = readShort();

        if (liquidID >= 0)
        {
            int amount = readInt();
            short liquidMeta = readShort();
            var2 = new LiquidStack(liquidID, amount, liquidMeta);
        }

        return var2;
	}

	private String channel;
	private int type;
	private boolean isChunkDataPacket;
	
	private boolean incoming;
	
	private ByteArrayOutputStream dataarrayout;
	private DataOutputStream dataout;
	
	private DataInputStream datain;
	
	private static HashMap<String, CustomPacketHandler> clienthandlermap = new HashMap<String, CustomPacketHandler>();	
	private static HashMap<String, CustomPacketHandler> serverhandlermap = new HashMap<String, CustomPacketHandler>();

	public static void assignHandler(String channel, int firstID, int lastID, ICustomPacketHandler IHandler)
	{
		Side side = IHandler instanceof IClientPacketHandler ? Side.CLIENT : Side.SERVER;
		HashMap<String, CustomPacketHandler> handlerMap = side.isClient() ? clienthandlermap : serverhandlermap;
		CustomPacketHandler handler = handlerMap.get(channel);
			
		if(handler == null)
		{
			if(side.isClient())
				handler = new ClientPacketHander(channel);
			else
				handler = new ServerPacketHander(channel);
			
			handlerMap.put(channel, handler);
		}
		handler.registerRange(firstID, lastID, IHandler);
	}

	public void sendToPlayer(EntityPlayerMP player)
	{
		player.playerNetServerHandler.sendPacketToPlayer(toPacket());
	}

	@SideOnly(Side.CLIENT)
    public void sendToServer()
    {
        Minecraft.getMinecraft().getSendQueue().addToSendQueue(toPacket());
    }
}
