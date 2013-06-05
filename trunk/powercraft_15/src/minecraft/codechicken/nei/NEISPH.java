package codechicken.nei;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import codechicken.core.inventory.ContainerExtended;
import codechicken.core.inventory.ItemKey;
import codechicken.core.inventory.SlotDummy;
import codechicken.core.packet.PacketCustom;
import codechicken.core.packet.PacketCustom.ICustomPacketHandler.IServerPacketHandler;
import codechicken.core.vec.BlockCoord;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetServerHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import codechicken.core.CommonUtils;
import codechicken.core.IGuiPacketSender;
import codechicken.core.ServerUtils;
import cpw.mods.fml.relauncher.Side;

public class NEISPH implements IServerPacketHandler
{
    @Override
    public void handlePacket(PacketCustom packet, NetServerHandler nethandler, EntityPlayerMP sender)
    {
        if(!NEIServerConfig.authenticatePacket(sender, packet))
            return;
        
        switch(packet.getType())
        {
            case 1:
                handleGiveItem(sender, packet);
            break;
            case 4:
                NEIServerUtils.deleteAllItems(sender);
            break;
            case 5:
                setInventorySlot(sender, packet);
            break;
            case 6:
                NEIServerUtils.toggleMagnetMode(sender);
            break;
            case 7:
                NEIServerUtils.setHourForward(sender.worldObj, packet.readUnsignedByte(), true);
            break;
            case 8:
                NEIServerUtils.healPlayer(sender);
            break;
            case 9:
                NEIServerUtils.toggleRaining(sender.worldObj, true);
            break;
            case 10:
                sendPermissableActionsTo(sender);
                sendBannedBlocksTo(sender);
                sendDisabledPropertiesTo(sender, sender.dimension);
                sendMagnetModeTo(sender, NEIServerUtils.isMagnetMode(sender));
                sendCreativeModeTo(sender, NEIServerUtils.getCreativeMode(sender));
            break;
            case 11:
                sender.sendContainerAndContentsToPlayer(sender.openContainer, sender.openContainer.getInventory());
            break;
            case 12:
                handlePropertyChange(sender, packet);
            break;
            case 13:
                NEIServerUtils.toggleCreativeMode(sender);
            break;
            case 14:
                NEIServerUtils.cycleCreativeInv(sender, packet.readInt());
            break;
            case 15:
                handleMobSpawnerID(sender.worldObj, packet.readCoord(), packet.readString());
            break;
            case 20:
                handleContainerPacket(sender, packet);
            break;
            case 21:
                openEnchantmentGui(sender);
            break;
            case 22:
                modifyEnchantment(sender, packet.readUnsignedByte(), packet.readUnsignedByte(), packet.readBoolean());
            break;
            case 23:
                processCreativeInv(sender, packet.readBoolean());
            break;
            case 24:
                openPotionGui(sender, packet);
            break;
            case 25:
                handleDummySlotSet(sender, packet);
            break;
        }
    }
    
    private void handleDummySlotSet(EntityPlayerMP sender, PacketCustom packet)
    {
        int slotNumber = packet.readShort();
        ItemStack stack = packet.readItemStack(true);
        
        Slot slot = sender.openContainer.getSlot(slotNumber);
        if(slot instanceof SlotDummy)
            slot.putStack(stack);
    }

    private void handleContainerPacket(EntityPlayerMP sender, PacketCustom packet)
    {
        if(sender.openContainer instanceof ContainerExtended)
            ((ContainerExtended)sender.openContainer).handleInputPacket(packet);
    }

    private void handleMobSpawnerID(World world, BlockCoord coord, String mobtype)
    {
        TileEntity tile = world.getBlockTileEntity(coord.x, coord.y, coord.z);
        if(tile instanceof TileEntityMobSpawner)
        {
            ((TileEntityMobSpawner)tile).func_98049_a().setMobID(mobtype);
            tile.onInventoryChanged();
            world.markBlockForUpdate(coord.x, coord.y, coord.z);
        }
    }

    private void handlePropertyChange(EntityPlayerMP sender, PacketCustom packet)
    {
        int id = packet.readUnsignedByte();
        if(NEIServerConfig.canPlayerUseFeature(sender.username, AllowedPropertyMap.idToFeatureClassMap.get(id)))
            handlePropertyChange(sender.dimension, id, packet.readBoolean());
    }

    private void processCreativeInv(EntityPlayerMP sender, boolean open)
    {
        if(open)
        {
            ServerUtils.openSMPContainer(sender, new ContainerCreativeInv(sender, new ExtendedCreativeInv(NEIServerConfig.forPlayer(sender.username), Side.SERVER)), new IGuiPacketSender()
            {
                @Override
                public void sendPacket(EntityPlayerMP player, int windowId)
                {
                    PacketCustom packet = new PacketCustom(channel, 23);
                    packet.writeBoolean(true);
                    packet.writeByte(windowId);
                    packet.sendToPlayer(player);
                }
            });
        }
        else
        {
            sender.closeInventory();
            PacketCustom packet = new PacketCustom(channel, 23);
            packet.writeBoolean(false);
            packet.sendToPlayer(sender);
        }
    }

    private void handlePropertyChange(int dim, int propID, boolean disable)
    {
        NEIServerConfig.setPropertyDisabled(dim, AllowedPropertyMap.idToNameMap.get(propID), disable);
        sendDisabledPropertiesTo(null, dim);
    }

    private void sendDisabledPropertiesTo(EntityPlayerMP player, int dim)
    {
        ArrayList<Integer> disabledProperties = new ArrayList<Integer>();
        for(Entry<String, Integer> prop : AllowedPropertyMap.nameToIDMap.entrySet())
        {
            if(NEIServerConfig.isPropertyDisabled(dim, prop.getKey()))
                disabledProperties.add(prop.getValue());
        }

        PacketCustom packet = new PacketCustom(channel, 12);
        packet.writeByte(disabledProperties.size());
        for(int i : disabledProperties)
        {
            packet.writeByte(i);
        }
        
        if(player != null)
            packet.sendToPlayer(player);
        else
            for(EntityPlayer sendplayer : ServerUtils.getPlayersInDimension(dim))
                packet.sendToPlayer(sendplayer);
    }

    private void handleGiveItem(EntityPlayerMP player, PacketCustom packet)
    {
        boolean infinite = packet.readBoolean();
        boolean doSpawn = packet.readBoolean();
        
        int num = packet.readUnsignedByte();
        LinkedList<String> name = new LinkedList<String>();
        for(int i = 0; i < num; i++)
        {
            name.add(packet.readString());
        }
        
        ItemStack item = packet.readItemStack();
        if(item == null)
        {
            ServerUtils.sendChatTo(player, "\247fNo such item.");
            return;
        }
        item.stackSize = packet.readInt();
        NEIServerUtils.givePlayerItem(player, item, infinite, name, doSpawn);
    }

    private void setInventorySlot(EntityPlayerMP player, PacketCustom packet)
    {        
        boolean container = packet.readBoolean();
        int slot = packet.readShort();
        ItemStack item = packet.readItemStack();
        if(!NEIServerConfig.canPlayerUseFeature(player.username, item == null ? "delete" : "item"))
            return;
        
        NEIServerUtils.setSlotContents(player, slot, item, container);
    }

    private void modifyEnchantment(EntityPlayerMP player, int e, int lvl, boolean add)
    {
        ContainerEnchantmentModifier containerem = (ContainerEnchantmentModifier)player.openContainer;
        if(add)
        {
            containerem.addEnchantment(e, lvl);
        }
        else
        {
            containerem.removeEnchantment(e);
        }
    }

    private void openEnchantmentGui(EntityPlayerMP player)
    {       
        ServerUtils.openSMPContainer(player, new ContainerEnchantmentModifier(player.inventory, player.worldObj, 0, 0, 0), new IGuiPacketSender()
        {
            @Override
            public void sendPacket(EntityPlayerMP player, int windowId)
            {
                PacketCustom packet = new PacketCustom(channel, 21);
                packet.writeByte(windowId);
                packet.sendToPlayer(player);
            }
        });
    }

    private void openPotionGui(EntityPlayerMP player, PacketCustom packet)
    {
        InventoryBasic b = new InventoryBasic("potionStore", true, 9);
        for(int i = 0; i < b.getSizeInventory(); i++)
            b.setInventorySlotContents(i, packet.readItemStack());
        ServerUtils.openSMPContainer(player, new ContainerPotionCreator(player.inventory, b), new IGuiPacketSender()
        {
            @Override
            public void sendPacket(EntityPlayerMP player, int windowId)
            {
                PacketCustom packet = new PacketCustom(channel, 24);
                packet.writeByte(windowId);
                packet.sendToPlayer(player);
            }
        });
    }
    
    public static void sendMagnetModeTo(EntityPlayerMP player, boolean enable)
    {
        PacketCustom packet = new PacketCustom(channel, 6);
        packet.writeBoolean(enable);
        packet.sendToPlayer(player);
    }
    
    public static void sendCreativeModeTo(EntityPlayerMP player, int mode)
    {
        PacketCustom packet = new PacketCustom(channel, 7);
        packet.writeByte(mode);
        packet.sendToPlayer(player);
    }

    private void sendPermissableActionsTo(EntityPlayerMP player)
    {
        LinkedList<Integer> actions = new LinkedList<Integer>();
        for(InterActionMap action : InterActionMap.values())
            if(NEIServerConfig.canPlayerUseFeature(player.username, action.getName()))
                actions.add(action.ordinal());

        PacketCustom packet = new PacketCustom(channel, 10);
        packet.writeByte(actions.size());
        for(int i : actions)
            packet.writeByte(i);

        packet.sendToPlayer(player);
    }
    
    private void sendBannedBlocksTo(EntityPlayerMP player)
    {
        ArrayList<ItemKey> bannedblocks = new ArrayList<ItemKey>();
        for(Entry<ItemKey, HashSet<String>> entry : NEIServerConfig.bannedblocks.entrySet())
        {
            if(!NEIServerConfig.isPlayerInList(player.username, entry.getValue(), true))
            {
                bannedblocks.add(entry.getKey());
            }
        }

        PacketCustom packet = new PacketCustom(channel, 11);
        packet.writeInt(bannedblocks.size());
        for(ItemKey hash : bannedblocks)
        {
            packet.writeShort(hash.item.itemID);
            packet.writeShort(hash.item.getItemDamage());
        }

        packet.sendToPlayer(player);
    }
    
    public static void sendHasServerSideTo(EntityPlayerMP player)
    {
        System.out.println("Sending serverside check to: "+player.username);
        PacketCustom packet = new PacketCustom(channel, 1);
        packet.writeByte(InterActionMap.protocol);
        packet.writeString(CommonUtils.getWorldName(player.worldObj));
        
        packet.sendToPlayer(player);
    }
    
    public static void sendAddMagneticItemTo(EntityPlayerMP player, EntityItem item)
    {
        PacketCustom packet = new PacketCustom(channel, 13);
        packet.writeInt(item.entityId);

        packet.sendToPlayer(player);
    }
    
    public static final String channel = "NEI";
}
