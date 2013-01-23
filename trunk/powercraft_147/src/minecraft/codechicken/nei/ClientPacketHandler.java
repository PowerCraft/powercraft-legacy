package codechicken.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.world.World;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.core.ClientUtils;
import codechicken.core.NetworkClosedException;
import codechicken.core.PacketCustom;
import codechicken.core.PacketCustom.ICustomPacketHandler.IClientPacketHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientPacketHandler implements IClientPacketHandler
{
	@Override
	public void handlePacket(PacketCustom packet, NetClientHandler nethandler, Minecraft mc)
	{
		switch(packet.getType())
    	{
    		case 1:
    			handleSMPCheck(packet.readUnsignedByte(), packet.readString(), mc.theWorld);
    		break;
    		case 6:
    			NEIClientConfig.setMagnetMode(packet.readBoolean());
    		break;
    		case 7:
    			NEIClientConfig.setCreativeMode(packet.readUnsignedByte());
    		break;
    		case 10:
    			handlePermissableActions(packet);
    		break;
    		case 11:
    			handleBannedBlocks(packet);
    		break;
    		case 12:
    			handleDisabledProperties(packet);
    		break;
    		case 13:
    			ClientHandler.instance().addSMPMagneticItem(packet.readInt(), mc.theWorld);
    		break;
    		case 21:
    			ClientUtils.openSMPGui(packet.readUnsignedByte(), new GuiEnchantmentModifier(mc.thePlayer.inventory, mc.theWorld, 0, 0, 0));
    		break;
    		case 23:
    			if(packet.readBoolean())
    				ClientUtils.openSMPGui(packet.readUnsignedByte(), new GuiExtendedCreativeInv(new ContainerCreativeInv(mc.thePlayer, new ExtendedCreativeInv(null, Side.CLIENT))));
    			else
    				mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
    		break;
    	}
	}

	private void handleDisabledProperties(PacketCustom packet)
	{
		NEIClientConfig.resetDisabledProperties();
		int num = packet.readUnsignedByte();
		for(int i = 0; i < num; i++)
		{
			NEIClientConfig.setPropertyDisabled(packet.readUnsignedByte());
		}
	}
	
	private void handleBannedBlocks(PacketCustom packet)
	{
		int num = packet.readInt();
		ArrayList<ItemHash> items = new ArrayList<ItemHash>(num);
		for(int i = 0; i < num; i++)
		{
			items.add(new ItemHash(packet.readUnsignedShort(), packet.readUnsignedShort()));
		}
		NEIClientConfig.setBannedBlocks(items);
		
		if(NEIClientUtils.getGuiContainer() != null)
			LayoutManager.instance().refresh(NEIClientUtils.getGuiContainer());
	}
	
	private void handlePermissableActions(PacketCustom packet)
	{
		NEIClientConfig.resetPermissableActions();
		int num = packet.readUnsignedByte();
		for(int i = 0; i < num; i++)
		{
			NEIClientConfig.addPermissableAction(InterActionMap.values()[packet.readUnsignedByte()]);
		}
	}

	private void handleSMPCheck(int serverprotocol, String worldName, World world)
	{
		if(serverprotocol > InterActionMap.protocol)
		{
			NEIClientUtils.addChatMessage("NEI version mismatch: Outdated Client");
		}
		else if(serverprotocol < InterActionMap.protocol)
		{
			NEIClientUtils.addChatMessage("NEI version mismatch: Outdated Server");
		}
		else
		{
		    try
		    {
    			String prefx;
    			if(ClientUtils.isLocal())
    			{
    				prefx = "local";
    				worldName = ClientUtils.getWorldSaveName(worldName);
    			}
    			else
    			{
    				try
    				{
    					prefx = "remote/"+ClientUtils.getServerIP().replace(':', '~');
    				}
    				catch(NetworkClosedException e)
    				{
    					return;
    				}
    			}
    			NEIClientConfig.loadWorld(prefx+'/'+worldName);
    			NEIClientConfig.setHasSMPCounterPart(true);
    			sendRequestLoginInfo();
		    }
		    catch(Exception nce)
		    {
		        nce.printStackTrace();
		    }
		}
	}

	public static void sendSpawnItem(ItemStack spawnstack, boolean infinite, boolean doSpawn)
    {    	
    	PacketCustom packet = new PacketCustom(channel, 1);
    	packet.writeBoolean(infinite);
    	packet.writeBoolean(doSpawn);
    	
    	List<String> name = GuiContainerManager.itemDisplayNameMultiline(spawnstack, null, false);
    	packet.writeByte(name.size());
    	for(String s : name)
    	{
    		packet.writeString(s);
    	}
    	
    	packet.writeItemStack(spawnstack);
    	packet.writeInt(spawnstack.stackSize);
    	ClientUtils.sendPacket(packet.toPacket());
    }
    
    public static void sendDeleteAllItems()
	{
    	PacketCustom packet = new PacketCustom(channel, 4);    	
    	ClientUtils.sendPacket(packet.toPacket());
	}
    
	public static void sendStateLoad(ItemStack[] state)
	{
		sendDeleteAllItems();
		for(int slot = 0; slot < state.length; slot++)
    	{
    		ItemStack item = state[slot];
    		if(item == null)
    		{
    			continue;
    		}
    		sendSetSlot(slot, item, false);   		
    	}
		
		PacketCustom packet = new PacketCustom(channel, 11);
    	ClientUtils.sendPacket(packet.toPacket());
	}
    
    public static void sendSetSlot(int slot, ItemStack stack, boolean container)
    {
    	PacketCustom packet = new PacketCustom(channel, 5);
    	packet.writeBoolean(container);
    	packet.writeShort(slot);
    	packet.writeItemStack(stack);
    	ClientUtils.sendPacket(packet.toPacket());
    }
    
    private static void sendRequestLoginInfo()
    {
    	PacketCustom packet = new PacketCustom(channel, 10);
    	ClientUtils.sendPacket(packet.toPacket());
    }
    
    public static void sendToggleMagnetMode()
    {
    	PacketCustom packet = new PacketCustom(channel, 6);
    	ClientUtils.sendPacket(packet.toPacket());
    }
    
    public static void sendSetTime(int hour)
    {
    	PacketCustom packet = new PacketCustom(channel, 7);
    	packet.writeByte(hour);
    	ClientUtils.sendPacket(packet.toPacket());
    }
    
    public static void sendHeal()
    {
    	PacketCustom packet = new PacketCustom(channel, 8);
    	ClientUtils.sendPacket(packet.toPacket());
    }
    
    public static void sendToggleRain()
    {
    	PacketCustom packet = new PacketCustom(channel, 9);
    	ClientUtils.sendPacket(packet.toPacket());
    }    

	public static void sendOpenEnchantmentWindow()
	{
    	PacketCustom packet = new PacketCustom(channel, 21);
    	ClientUtils.sendPacket(packet.toPacket());
	}
	
	public static void sendModifyEnchantment(int enchID, int level, boolean add)
	{
    	PacketCustom packet = new PacketCustom(channel, 22);
    	packet.writeByte(enchID);
    	packet.writeByte(level);
    	packet.writeBoolean(add);
    	ClientUtils.sendPacket(packet.toPacket());
	}
    
	public static void sendSetPropertyDisabled(String name, boolean enable)
	{
    	PacketCustom packet = new PacketCustom(channel, 12);
    	packet.writeByte(AllowedPropertyMap.nameToIDMap.get(name));
    	packet.writeBoolean(enable);
    	ClientUtils.sendPacket(packet.toPacket());
	}

	public static void sendCycleCreativeMode()
	{
    	PacketCustom packet = new PacketCustom(channel, 13);
    	ClientUtils.sendPacket(packet.toPacket());
	}
	
	public static final String channel = "NEI";

	public static void sendCreativeInv(boolean open)
	{
		PacketCustom packet = new PacketCustom(channel, 23);
		packet.writeBoolean(open);
    	ClientUtils.sendPacket(packet.toPacket());
	}

	public static void sendCreativeScroll(int steps)
	{
		PacketCustom packet = new PacketCustom(channel, 14);
		packet.writeInt(steps);
    	ClientUtils.sendPacket(packet.toPacket());
	}

    public static void sendMobSpawnerID(int x, int y, int z, String mobtype)
    {
        PacketCustom packet = new PacketCustom(channel, 15);
        packet.writeCoord(x, y, z);
        packet.writeString(mobtype);
        packet.sendToServer();
    }
}
