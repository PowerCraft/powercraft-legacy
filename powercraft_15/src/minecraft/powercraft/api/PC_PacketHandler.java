package powercraft.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.tileentity.PC_TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PC_PacketHandler extends powercraft.launcher.PC_PacketHandler
{
    public static final int PACKETTILEENTITY = 0, PACKETPACKETHANDLER = 1, PACKETGUI = 2, PACKETIDS = 3;

    protected static HashMap<String, PC_IPacketHandler> packetHandler = new HashMap<String, PC_IPacketHandler>();

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        try
        {
            ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(packet.data));
            int type = input.readInt();

            switch (type)
            {
                case PACKETTILEENTITY:
                    handleIncomingTEPacket(input, (EntityPlayer)player);
                    break;

                case PACKETPACKETHANDLER:
                    handleIncomingPacketHandlerPacket(input, (EntityPlayer)player);
                    break;

                case PACKETGUI:
                    handleIncomingGuiPacket(input, (EntityPlayer)player);
                    break;

                case PACKETIDS:
                	handleIncomingIDPacket(input, (EntityPlayer)player);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Neither TE nor PacketHandler nor Gui");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	protected void handleIncomingTEPacket(ObjectInputStream input, EntityPlayer player) throws IOException, ClassNotFoundException
    {
        int x = input.readInt();
        int y = input.readInt();
        int z = input.readInt();
        PC_Struct2<String, Object>[] o = (PC_Struct2<String, Object>[])input.readObject();
        TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);

        if (te == null)
        {
            return;
        }

        if (te instanceof PC_TileEntity)
        {
        	
        	((PC_TileEntity) te).setData(o);

            if (!player.worldObj.isRemote)
            {
                setTileEntityArray(te, o);
            }
        }
        else
        {
            throw new IllegalArgumentException("Not a PC_TileEntity");
        }
    }

    protected void handleIncomingPacketHandlerPacket(ObjectInputStream input, EntityPlayer player) throws IOException, ClassNotFoundException
    {
        String name = (String)input.readObject();
        Object[] o = (Object[])input.readObject();
        PC_IPacketHandler ph = packetHandler.get(name);

        if (ph != null)
        {
            if (ph.handleIncomingPacket(player, o))
            {
                sendToPacketHandlerArray(player.worldObj, name, o);
            }
        }
    }

    protected void handleIncomingGuiPacket(ObjectInputStream input, EntityPlayer player) throws ClassNotFoundException, IOException
    {
    }

    protected void handleIncomingIDPacket(ObjectInputStream input, EntityPlayer player) throws ClassNotFoundException, IOException{
	}
    
    //AlphaI
    @Deprecated
    public static void setTileEntityArray(TileEntity tileEntity, PC_Struct2<String, Object>[] o)
    {
        setTileEntity(tileEntity, o);
    }

    public static void setTileEntity(TileEntity tileEntity, PC_Struct2<String, Object>... o)
    {
    	if(tileEntity.worldObj==null)
    		return;
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;
        int dimension = GameInfo.getWorldDimension(tileEntity.worldObj);

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PACKETTILEENTITY);
            sendData.writeInt(tileEntity.xCoord);
            sendData.writeInt(tileEntity.yCoord);
            sendData.writeInt(tileEntity.zCoord);
            sendData.writeObject(o);
            sendData.writeInt(PACKETTILEENTITY);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (tileEntity.worldObj.isRemote)
        {
            sendToServer(data);
        }
        else
        {
            sendToAllInDimension(dimension, data);
        }
    }

    public static void sendTileEntity(TileEntity tileEntity)
    {
    	if(tileEntity != null){
    		World world = tileEntity.worldObj;
    		if(world!=null){
	    		int dimension = world.getWorldInfo().getDimension();
	    		PacketDispatcher.sendPacketToAllInDimension(tileEntity.getDescriptionPacket(), dimension);
    		}
    	}
    }
    
    //AlphaI
    @Deprecated
    public static void sendToPacketHandlerArray(World world, String name, Object[] o)
    {
        sendToPacketHandler(world, name, o);
    }

    public static void sendToPacketHandler(World world, String name, Object... o)
    {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PACKETPACKETHANDLER);
            sendData.writeObject(name);
            sendData.writeObject(o);
            sendData.writeInt(PACKETPACKETHANDLER);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (world != null)
        {
            if (world.isRemote)
            {
                sendToServer(data);
            }
            else
            {
                sendToAll(data);
            }
        }
    }

    //AlphaI
    @Deprecated
    public static void sendToPacketHandlerArray(boolean onlyDimension, World world, String name, Object[] o)
    {
        sendToPacketHandler(onlyDimension, world, name, o);
    }

    public static void sendToPacketHandler(boolean onlyDimension, World world, String name, Object... o)
    {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PACKETPACKETHANDLER);
            sendData.writeObject(name);
            sendData.writeObject(o);
            sendData.writeInt(PACKETPACKETHANDLER);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (world.isRemote)
        {
            sendToServer(data);
        }
        else
        {
            if (onlyDimension)
            {
                sendToAllInDimension(GameInfo.getWorldDimension(world), data);
            }
            else
            {
                sendToAll(data);
            }
        }
    }
    
    public static void sendToAll(ByteArrayOutputStream data)
    {
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
        PacketDispatcher.sendPacketToAllPlayers(packet);
    }

    public static void sendToPlayer(EntityPlayer player, ByteArrayOutputStream data)
    {
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
        PacketDispatcher.sendPacketToPlayer(packet, (Player)player);
    }

    public static void sendToServer(ByteArrayOutputStream data)
    {
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
        PacketDispatcher.sendPacketToServer(packet);
    }

    public static void sendToAllInDimension(int dimension, ByteArrayOutputStream data)
    {
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
        sendToAllInDimension(dimension, packet);
    }

    public static void sendToAllInDimension(int dimension, Packet packet)
    {
        PacketDispatcher.sendPacketToAllInDimension(packet, dimension);
    }
    
    public static void registerPackethandler(String name, PC_IPacketHandler packethandler)
    {
        PC_PacketHandler.packetHandler.put(name, packethandler);
    }

    public static void registerPackethandlers(Object[] o)
    {
        if (o == null)
        {
            return;
        }

        for (int i = 0; i < o.length; i += 2)
        {
            registerPackethandler((String)o[i], (PC_IPacketHandler)o[i + 1]);
        }
    }
}
