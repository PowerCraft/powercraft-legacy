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
import codechicken.core.inventory.InventoryUtils;
import codechicken.core.inventory.ItemKey;
import codechicken.core.packet.PacketCustom;
import codechicken.core.packet.PacketCustom.ICustomPacketHandler.IClientPacketHandler;
import cpw.mods.fml.relauncher.Side;

public class NEICPH implements IClientPacketHandler
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
            case 24:
                ClientUtils.openSMPGui(packet.readUnsignedByte(), new GuiPotionCreator(mc.thePlayer.inventory));
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
        ArrayList<ItemKey> items = new ArrayList<ItemKey>(num);
        for(int i = 0; i < num; i++)
            items.add(new ItemKey(packet.readUnsignedShort(), packet.readUnsignedShort()));

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
                    prefx = "remote/"+ClientUtils.getServerIP().replace(':', '~');
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
        packet.sendToServer();
    }
    
    public static void sendDeleteAllItems()
    {
        PacketCustom packet = new PacketCustom(channel, 4);        
        packet.sendToServer();
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
        packet.sendToServer();
    }
    
    public static void sendSetSlot(int slot, ItemStack stack, boolean container)
    {
        PacketCustom packet = new PacketCustom(channel, 5);
        packet.writeBoolean(container);
        packet.writeShort(slot);
        packet.writeItemStack(stack);
        packet.sendToServer();
    }
    
    private static void sendRequestLoginInfo()
    {
        PacketCustom packet = new PacketCustom(channel, 10);
        packet.sendToServer();
    }
    
    public static void sendToggleMagnetMode()
    {
        PacketCustom packet = new PacketCustom(channel, 6);
        packet.sendToServer();
    }
    
    public static void sendSetTime(int hour)
    {
        PacketCustom packet = new PacketCustom(channel, 7);
        packet.writeByte(hour);
        packet.sendToServer();
    }
    
    public static void sendHeal()
    {
        PacketCustom packet = new PacketCustom(channel, 8);
        packet.sendToServer();
    }
    
    public static void sendToggleRain()
    {
        PacketCustom packet = new PacketCustom(channel, 9);
        packet.sendToServer();
    }

    public static void sendOpenEnchantmentWindow()
    {
        PacketCustom packet = new PacketCustom(channel, 21);
        packet.sendToServer();
    }
    
    public static void sendModifyEnchantment(int enchID, int level, boolean add)
    {
        PacketCustom packet = new PacketCustom(channel, 22);
        packet.writeByte(enchID);
        packet.writeByte(level);
        packet.writeBoolean(add);
        packet.sendToServer();
    }
    
    public static void sendSetPropertyDisabled(String name, boolean enable)
    {
        PacketCustom packet = new PacketCustom(channel, 12);
        packet.writeByte(AllowedPropertyMap.nameToIDMap.get(name));
        packet.writeBoolean(enable);
        packet.sendToServer();
    }

    public static void sendCycleCreativeMode()
    {
        PacketCustom packet = new PacketCustom(channel, 13);
        packet.sendToServer();
    }
    
    public static final String channel = "NEI";

    public static void sendCreativeInv(boolean open)
    {
        PacketCustom packet = new PacketCustom(channel, 23);
        packet.writeBoolean(open);
        packet.sendToServer();
    }

    public static void sendCreativeScroll(int steps)
    {
        PacketCustom packet = new PacketCustom(channel, 14);
        packet.writeInt(steps);
        packet.sendToServer();
    }

    public static void sendMobSpawnerID(int x, int y, int z, String mobtype)
    {
        PacketCustom packet = new PacketCustom(channel, 15);
        packet.writeCoord(x, y, z);
        packet.writeString(mobtype);
        packet.sendToServer();
    }

    public static PacketCustom createContainerPacket()
    {
        return new PacketCustom(channel, 20);
    }

    public static void sendOpenPotionWindow()
    {
        ItemStack[] potionStore = new ItemStack[9];
        InventoryUtils.readItemStacksFromTag(potionStore, NEIClientConfig.saveCompound.getCompoundTag("potionStore").getTagList("items"));
        PacketCustom packet = new PacketCustom(channel, 24);
        for(int i = 0; i < potionStore.length; i++)
            packet.writeItemStack(potionStore[i]);
        packet.sendToServer();
    }

    public static void sendDummySlotSet(int slotNumber, ItemStack stack)
    {
        PacketCustom packet = new PacketCustom(channel, 25);
        packet.writeShort(slotNumber);
        packet.writeItemStack(stack, true);
        packet.sendToServer();
    }
}
