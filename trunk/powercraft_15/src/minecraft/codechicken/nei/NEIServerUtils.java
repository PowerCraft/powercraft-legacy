package codechicken.nei;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.inventory.Container;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.WorldServer;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import codechicken.core.CommonUtils;
import codechicken.core.ServerUtils;
import codechicken.core.inventory.InventoryUtils;
import codechicken.nei.AllowedPropertyMap;

public class NEIServerUtils
{
    public static boolean isRaining(World world)
    {
        return world.getWorldInfo().isRaining();
    }
    
    public static void toggleRaining(World world, boolean notify)
    {
        boolean raining = !world.isRaining();
        if(raining == false)//turn off
            ((WorldServer)world).provider.resetRainAndThunder();
        else
            world.toggleRain();
        
        if(notify)
            ServerUtils.sendChatToAll("Rain turned "+(raining ? "on" : "off"));
    }
    
    public static void healPlayer(EntityPlayer player)
    {
        player.heal(20);
        player.getFoodStats().addStats(20, 1);
        player.extinguish();
    }

    public static long getTime(World world)
    {
        return world.getWorldInfo().getWorldTime();
    }

    public static void setTime(long l, World world)
    {
        world.getWorldInfo().setWorldTime(l);
    }
    
    public static void setSlotContents(EntityPlayer player, int slot, ItemStack item, boolean containerInv)
    {
        if(slot == -999)
        {
            player.inventory.setItemStack(item);
        }
        else if(containerInv)
        {
            player.openContainer.putStackInSlot(slot, item);
        }
        else
        {
            player.inventory.setInventorySlotContents(slot, item);
        }
    }
    
    public static void deleteAllItems(EntityPlayerMP player)
    {
        for(int i = 0; i < player.openContainer.inventorySlots.size(); i++)
        {
            ((Slot)player.openContainer.inventorySlots.get(i)).putStack(null);
        }
        player.sendContainerAndContentsToPlayer(player.openContainer, player.openContainer.getInventory());
    }
    
    public static void setHourForward(World world, int hour, boolean notify)
    {
        long day = (getTime(world) / 24000L) * 24000L;
        long newTime = day + 24000L + (long)(hour * 1000);
        setTime(newTime, world);
        if(notify)ServerUtils.sendChatToAll("Day "+(getTime(world) / 24000L)+". "+hour+":00");
    }
    
    public static void advanceDisabledTimes(World world)
    {
        int dim = CommonUtils.getDimension(world);
        int hour = (int)(getTime(world)%24000)/1000;
        int newhour = hour;
        while(true)
        {
            int zone = newhour/6;
            try
            {
                if(NEIServerConfig.isPropertyDisabled(dim, AllowedPropertyMap.idToNameMap.get(zone)))
                {
                    newhour = ((zone+1)%4)*6;
                }
                else
                {
                    break;
                }
            }
            catch (NumberFormatException nfe)
            {
                String s = "Time: "+getTime(world)+", Hour: "+hour+", NewHour: "+newhour+", Zone: "+zone+", Map: "+AllowedPropertyMap.idToNameMap.toString();
                ServerUtils.mc().logSevere(s);
                throw nfe;
            }
        }
        if(newhour != hour)
        {
            setHourForward(world, newhour, false);
        }
    }
    
    public static boolean canItemFitInInventory(EntityPlayer player, ItemStack itemstack)
    {
        for(int i = 0; i < player.inventory.getSizeInventory() - 4; i++)
        {
            if(player.inventory.getStackInSlot(i) == null)
            {
                return true;
            }
        }
        if(!itemstack.isItemDamaged())
        {
            if(itemstack.getMaxStackSize() == 1)return false;//would need to go in empty slot
            
            for(int i = 0; i < player.inventory.getSizeInventory(); i++)
            {
                ItemStack invstack = player.inventory.getStackInSlot(i);
                if(invstack != null && invstack.itemID == itemstack.itemID && invstack.isStackable() && invstack.stackSize < invstack.getMaxStackSize() && invstack.stackSize < player.inventory.getInventoryStackLimit() && (!invstack.getHasSubtypes() || invstack.getItemDamage() == itemstack.getItemDamage()))
                {
                    return true;
                }
            }            
        }
        return false;
    }
    
    public static int getSlotForStack(Container c, int firstSlot, int lastSlot, ItemStack item)
    {
        for(int slotIndex = firstSlot; slotIndex < lastSlot; slotIndex++)
        {
            Slot slot = c.getSlot(slotIndex);
            if(slot.getHasStack() && item.isStackable())
            {
                int filled = slot.getStack().stackSize;
                if(filled >= slot.getSlotStackLimit() || filled >= item.getMaxStackSize())
                    continue;
                
                if(NEIClientUtils.areStacksSameType(slot.getStack(), item))
                    return slotIndex;
            }
        }
        
        for(int slotIndex = firstSlot; slotIndex < lastSlot; slotIndex++)
        {
            Slot slot = c.getSlot(slotIndex);
            if(!slot.getHasStack())
                return slotIndex;
        }
        
        return -1;
    }
    
    public static int getSlotForStack(IInventory inv, int firstSlot, int lastSlot, ItemStack item)
    {
        for(int slotIndex = firstSlot; slotIndex < lastSlot; slotIndex++)
        {
            ItemStack slotStack = inv.getStackInSlot(slotIndex);
            if(slotStack != null && item.isStackable())
            {
                int filled = slotStack.stackSize;
                if(filled >= inv.getInventoryStackLimit() || filled >= item.getMaxStackSize())
                    continue;
                
                if(areStacksSameType(slotStack, item))
                    return slotIndex;
            }
        }
        
        for(int slotIndex = firstSlot; slotIndex < lastSlot; slotIndex++)
        {
            if(inv.getStackInSlot(slotIndex) == null)
                return slotIndex;
        }
        
        return -1;
    }
    
    public static void sendNotice(String s, String permission)
    {
        sendNotice(s, permission, -1);
    }
    
    @SuppressWarnings("unchecked")
    public static void sendNotice(String s, String permission, int colour)
    {
        if(NEIServerConfig.canPlayerUseFeature("CONSOLE", permission))
        {
            Logger.getLogger("Minecraft").info(s.replaceAll("\247.", ""));//remove colour tags
        }
        /*if(colour != -1)
            s = colourPrefix(colour)+s;*/
        for(EntityPlayerMP player : (List<EntityPlayerMP>)ServerUtils.mc().getConfigurationManager().playerEntityList)
        {
            if(NEIServerConfig.canPlayerUseFeature(player.username, permission))
            {
                ServerUtils.sendChatTo(player, s);
                //player.playerNetServerHandler.sendPacket(new Packet3Chat(s));
            }
        }
    }
        
    /**
     * @param stack1 The {@link ItemStack} being compared.
     * @param stack2 The {@link ItemStack} to compare to.
     * @return whether the two items are the same in terms of damage and itemID.
     */
    public static boolean areStacksSameType(ItemStack stack1, ItemStack stack2)
    {
        return InventoryUtils.canStack(stack1, stack2);
    }
    /**
     * {@link ItemStack}s with damage -1 are wildcards allowing all damages. Eg all colours of wool are allowed to create Beds.
     * @param stack1 The {@link ItemStack} being compared.
     * @param stack2 The {@link ItemStack} to compare to.
     * @return whether the two items are the same from the perspective of a crafting inventory.
     */
    public static boolean areStacksSameTypeCrafting(ItemStack stack1, ItemStack stack2)
    {
        if(stack1 == null || stack2 == null)
            return false;
        
        return stack1.itemID == stack2.itemID && 
                (stack1.getItemDamage() == stack2.getItemDamage() || stack1.getItemDamage() == -1 || stack2.getItemDamage() == -1 || stack1.getItem().isDamageable());
    }
    /**
     * A simple function for comparing ItemStacks in a compatible with comparators.
     * @param stack1 The {@link ItemStack} being compared.
     * @param stack2 The {@link ItemStack} to compare to.
     * @return The ordering of stack1 relative to stack2.
     */
    public static int compareStacks(ItemStack stack1, ItemStack stack2)
    {
        if(stack1 == stack2)return 0;//catches both null
        if(stack1 == null || stack2 == null)return stack1 == null ? -1 : 1;//null stack goes first
        if(stack1.itemID != stack2.itemID)return stack1.itemID - stack2.itemID;
        if(stack1.stackSize != stack2.stackSize)return stack1.stackSize - stack2.stackSize;
        return stack1.getItemDamage() - stack2.getItemDamage();
    }
    
    public static boolean areStacksIdentical(ItemStack stack1, ItemStack stack2)
    {
        return compareStacks(stack1, stack2) == 0;
    }
    
    public static void givePlayerItem(EntityPlayerMP player, ItemStack stack, boolean infinite, LinkedList<String> name, boolean doGive)
    {
        if(stack.getItem() == null)
        {
            player.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat("\247fNo such item."));
            return;
        }
        StringBuilder namebuilder = new StringBuilder();
        boolean first = true;
        for(String string : name)
        {
            if(!first)
            {
                namebuilder.append(" ");
            }
            namebuilder.append(string.trim());
            first = false;
        }
        String itemname = namebuilder.toString();
        

        int given = 0;
        if(!doGive)
            given = stack.stackSize;
        else
        {        
            if(infinite)
                player.inventory.addItemStackToInventory(stack);
            else
            {            
                int increment = stack.getMaxStackSize();
                for(given = 0; given < stack.stackSize;)
                {
                    int qty = Math.min(stack.stackSize-given, increment);
                    int slotNo = getSlotForStack(player.inventory, 0, 36, stack);
                    
                    if(slotNo == -1)
                        break;
                    
                    ItemStack slotStack = player.inventory.getStackInSlot(slotNo);
                    int current = (slotStack != null ? slotStack.stackSize : 0);
                    qty = Math.min(qty, player.inventory.getInventoryStackLimit() - current);
                    
                    player.inventory.setInventorySlotContents(slotNo, copyStack(stack, qty+current));
                    given+=qty;
                }
            }
        }
        
        if(infinite)
        {
            sendNotice("Giving "+player.username+" infinite "+"\247f"+itemname, "notify-item");
        }
        else
        {
            sendNotice("Giving "+player.username+" "+given+" of "+"\247f"+itemname, "notify-item");
        }
        
        player.openContainer.detectAndSendChanges();
    }
    
    public static ItemStack copyStack(ItemStack itemstack, int i)
    {
        if(itemstack == null)
        {
            return null;
        } else
        {
            itemstack.stackSize += i;
            return itemstack.splitStack(i);
        }
    }

    public static ItemStack copyStack(ItemStack itemstack)
    {
        if(itemstack == null)
        {
            return null;
        } else
        {
            return copyStack(itemstack, itemstack.stackSize);
        }
    }

    public static boolean isMagnetMode(EntityPlayerMP player)
    {
        return NEIServerConfig.forPlayer(player.username).getMagnetMode();    
    }

    public static void toggleMagnetMode(EntityPlayerMP player)
    {
        PlayerSave playerSave = NEIServerConfig.forPlayer(player.username);
        playerSave.setMagnetMode(!playerSave.getMagnetMode());    
        NEISPH.sendMagnetModeTo(player, playerSave.getMagnetMode());        
    }

    public static int getCreativeMode(EntityPlayerMP player)
    {
        if(NEIServerConfig.forPlayer(player.username).getCreativeInv())
            return 2;
        else if(player.theItemInWorldManager.isCreative())
            return 1;
        else
            return 0;
    }

    public static void toggleCreativeMode(EntityPlayerMP player)
    {
        int mode = (getCreativeMode(player)+1)%3;
        
        player.theItemInWorldManager.setGameType(mode == 0 ? EnumGameType.SURVIVAL : EnumGameType.CREATIVE);
        //player.playerNetServerHandler.sendPacket(new Packet70Bed(3, mode == 0 ? 0 : 1));
        
        NEIServerConfig.forPlayer(player.username).setCreativeInv(mode == 2);        
        NEISPH.sendCreativeModeTo(player, mode);
    }

    public static void cycleCreativeInv(EntityPlayerMP player, int steps)
    {
        InventoryPlayer inventory = player.inventory;
        
        //top down [row][col]
        ItemStack[][] slots = new ItemStack[10][9];
        PlayerSave playerSave = NEIServerConfig.forPlayer(player.username);
        
        //get
        for(int hotbar = 0; hotbar < 9; hotbar++)
        {
            slots[9][hotbar] = inventory.mainInventory[hotbar];
        }
        
        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 9; col++)
            {
                slots[row+6][col] = inventory.mainInventory[(row+1)*9+col];
            }
        }
        
        for(int row = 0; row < 6; row++)
        {
            for(int col = 0; col < 9; col++)
            {
                slots[row][col] = playerSave.creativeInv[row*9+col];
            }
        }
        
        ItemStack[][] newslots = new ItemStack[10][];
        
        //put back
        for(int row = 0; row < 10; row++)
        {
            newslots[(row+steps+10)%10] = slots[row];
        }
        
        for(int hotbar = 0; hotbar < 9; hotbar++)
        {
            inventory.mainInventory[hotbar] = newslots[9][hotbar];
        }
        
        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 9; col++)
            {
                inventory.mainInventory[(row+1)*9+col] = newslots[row+6][col];
            }
        }
        
        for(int row = 0; row < 6; row++)
        {
            for(int col = 0; col < 9; col++)
            {
                playerSave.creativeInv[row*9+col] = newslots[row][col];
            }
        }
        
        playerSave.setDirty();
    }
    
    public static List<int[]> getEnchantments(ItemStack itemstack)
    {
        ArrayList<int[]> arraylist = new ArrayList<int[]>();
        if(itemstack != null)
        {
            NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
            if(nbttaglist != null)
            {
                for(int i = 0; i < nbttaglist.tagCount(); i++)
                {
                    short word0 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("id");
                    short word1 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("lvl");
                    int ai[] = {
                        word0, word1
                    };
                    arraylist.add(ai);
                }

            }
        }
        return arraylist;
    }

    public static boolean stackHasEnchantment(ItemStack itemstack, int e)
    {
        List<int[]> allenchantments = getEnchantments(itemstack);
        for(int[] ai : allenchantments)
        {
            if(ai[0] == e)
            {
                return true;
            }
        }
        return false;
    }
    
    public static int getEnchantmentLevel(ItemStack itemstack, int e)
    {
        List<int[]> allenchantments = getEnchantments(itemstack);
        for(int[] ai : allenchantments)
        {
            if(ai[0] == e)
            {
                return ai[1];
            }
        }
        return -1;
    }
    
    public static boolean doesEnchantmentConflict(List<int[]> enchantments, Enchantment e)
    {
        for(int[] ai : enchantments)
        {
            if(!e.canApplyTogether(Enchantment.enchantmentsList[ai[0]]))
            {
                return true;
            }
        }
        return false;
    }
}
