package powercraft.management;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.SaveHandler;

public class PC_PacketHandler
{
    public static final int PACKETTILEENTITY = 0, PACKETPACKETHANDLER = 1, PACKETGUI = 2, PACKETIDS = 3, PACKETREQUESTIDLIST=4;

    protected static HashMap<String, PC_IPacketHandler> packetHandler = new HashMap<String, PC_IPacketHandler>();

    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player)
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
                    
                case PACKETREQUESTIDLIST:
                	handleIncomingIDRequestPacket(input, (EntityPlayer)player);
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

	private void handleIncomingIDRequestPacket(ObjectInputStream input, EntityPlayer player) {
		System.out.println("connectionReceived");
		ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        
        
        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PC_PacketHandler.PACKETIDS);
            sendData.writeObject(CompressedStreamTools.compress(SaveHandler.makeIDTagCompound()));
            sendData.writeObject(PC_GlobalVariables.consts);
            sendData.writeInt(PC_PacketHandler.PACKETIDS);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        sendToPlayer(player, data);
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
    
    public static void setTileEntity(TileEntity tileEntity, PC_Struct2<String, Object>... o)
    {
        setTileEntityArray(tileEntity, o);
    }

    public static void setTileEntityArray(TileEntity tileEntity, PC_Struct2<String, Object>[] o)
    {
    	if(tileEntity.getWorldObj()==null)
    		return;
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;
        int dimension = GameInfo.getWorldDimension(tileEntity.getWorldObj());

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

        if (tileEntity.getWorldObj().isRemote)
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
    		World world = tileEntity.getWorldObj();
    		if(world!=null){
	    		int dimension = world.getWorldInfo().getDimension();
	    		GameInfo.mcs().getConfigurationManager().sendPacketToAllPlayersInDimension(tileEntity.getDescriptionPacket(), dimension);
    		}
    	}
    }
    
    public static void sendToPacketHandler(World world, String name, Object... o)
    {
        sendToPacketHandlerArray(world, name, o);
    }

    public static void sendToPacketHandlerArray(World world, String name, Object[] o)
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

    public static void sendToPacketHandler(boolean onlyDimension, World world, String name, Object... o)
    {
        sendToPacketHandlerArray(onlyDimension, world, name, o);
    }

    public static void sendToPacketHandlerArray(boolean onlyDimension, World world, String name, Object[] o)
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


	public static void requestIDList() {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PACKETREQUESTIDLIST);
            sendData.writeObject("ModLoader");
            sendData.writeInt(PACKETREQUESTIDLIST);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        sendToServer(data);
        
	}
    
    public static void sendToAll(ByteArrayOutputStream data)
    {
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
    	GameInfo.mcs().getConfigurationManager().sendPacketToAllPlayers(packet);
    }

    public static void sendToPlayer(EntityPlayer player, ByteArrayOutputStream data)
    {
    	 Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
    	((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
    }

    public static void sendToServer(ByteArrayOutputStream data)
    {
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
        ModLoader.clientSendPacket(packet);
    }

    public static void sendToAllInDimension(int dimension, ByteArrayOutputStream data)
    {
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
        GameInfo.mcs().getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dimension);
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
