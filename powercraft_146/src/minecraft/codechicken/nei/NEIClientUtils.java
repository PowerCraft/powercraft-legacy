package codechicken.nei;

import java.io.*;
import java.text.*;
import java.util.*;
import org.lwjgl.input.Keyboard;

import codechicken.nei.api.GuiInfo;
import codechicken.nei.api.IInfiniteItemHandler;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.asm.NEIModContainer;
import codechicken.packager.Packager;

import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

public class NEIClientUtils extends NEIServerUtils
{	
    public static Minecraft mc()
    {
    	return Minecraft.getMinecraft();
    }

    public static void reportException(Exception exception)
    {
        try
        {
        	exception.printStackTrace();
    	    String s = "nei " +new SimpleDateFormat("d-M-y").format(new Date()) + " at "+ new SimpleDateFormat("H.m.s.S").format(new Date()) +".txt";
            File file = new File(Minecraft.getMinecraftDir(), s);
            PrintWriter printwriter = new PrintWriter(new FileWriter(file));
            printwriter.print("[code]NEI Version: "+NEIModContainer.class.getAnnotation(Packager.class).getVersion()+"\n");
            exception.printStackTrace(printwriter);
            printwriter.println("[/code]");
            printwriter.close();
            addChatMessage("Error written to "+s);
        }
        catch(Exception exception1)
        {
            System.out.println("Error during safeReportException:");
            exception1.printStackTrace();
        }
    }
	
	public static void addChatMessage(String s)
	{
		if(mc().ingameGUI != null)
			mc().ingameGUI.getChatGUI().printChatMessage(s);
	}

    public static void deleteHeldItem()
    {
    	deleteSlotStack(-999);
    }
    
	public static void dropHeldItem()
	{
		mc().playerController.windowClick(((GuiContainer)mc().currentScreen).inventorySlots.windowId, -999, 0, 0, mc().thePlayer);
	}

	public static void deleteSlotStack(int slotNumber)
	{
		setSlotContents(slotNumber, null, true);
	}
	
	public static void decreaseSlotStack(int slotNumber)
	{
		ItemStack stack = slotNumber == -999 ? getHeldItem() : mc().thePlayer.openContainer.getSlot(slotNumber).getStack();
		if(stack == null)
			return;
		
		if(stack.stackSize == 1)
			deleteSlotStack(slotNumber);
		else
		{
			stack = stack.copy();
			stack.stackSize--;
			setSlotContents(slotNumber, stack, true);
		}
	}
	
	public static void deleteEverything()
	{
		ClientPacketHandler.sendDeleteAllItems();
	}
    
    public static void deleteItemsOfType(ItemStack itemstack)
    {
    	Container c = getGuiContainer().inventorySlots;
        for(int i = 0; i < c.inventorySlots.size(); i++)
        {
            Slot slot = c.getSlot(i);
            if(slot == null)
            {
                continue;
            }
            ItemStack itemstack1 = slot.getStack();
            if(itemstack1 != null && itemstack1.itemID == itemstack.itemID && itemstack1.getItemDamage() == itemstack.getItemDamage())
            {
            	setSlotContents(i, null, true);
                slot.putStack((ItemStack)null);
            }
        }
    }
	
	public static ItemStack getHeldItem()
    {
        return mc().thePlayer.inventory.getItemStack();
    }

	public static void setSlotContents(int slot, ItemStack item, boolean containerInv)
	{
		ClientPacketHandler.sendSetSlot(slot, item, containerInv);
		
		if(slot == -999)
		{
			mc().thePlayer.inventory.setItemStack(item);
		}
	}
	
	/**
	 * 
	 * @param typeStack
	 * @param button
	 * @param mode -1 = normal cheats, 0 = no infinites, 1 = replenish stack
	 */
	public static void cheatItem(ItemStack typeStack, int button, int mode)
	{
		if(!NEIClientConfig.isActionPermissable("item"))
			return;
		
		if(mode == -1 && button == 0 && NEIClientUtils.shiftKey())
		{
			for(IInfiniteItemHandler handler : ItemInfo.infiniteHandlers)
			{
				if(!handler.canHandleItem(typeStack))
					continue;
				
				ItemStack stack = handler.getInfiniteItem(typeStack);
				if(stack != null)
				{
					giveStack(stack, stack.stackSize, true);
					return;
				}
			}
            cheatItem(typeStack, button, 0);
		}
		else if(button == 1)
		{
			giveStack(typeStack, 1);
		}
		else
		{
			if(mode == 1 && typeStack.stackSize < typeStack.getMaxStackSize())
			{
				giveStack(typeStack, typeStack.getMaxStackSize() - typeStack.stackSize);
			}
			else
			{
				int amount = NEIClientConfig.getItemQuantity();
				if(amount == 0)
					amount = typeStack.getMaxStackSize();
				giveStack(typeStack, amount);
			}
		}
	}
	
	public static void giveStack(ItemStack itemstack)
    {
        giveStack(itemstack, itemstack.stackSize);
    }
	
	public static void giveStack(ItemStack itemstack, int i)
    {
        giveStack(itemstack, i, false);
    }

    public static void giveStack(ItemStack itemstack, int i, boolean infinite)
    {
        ItemStack itemstack1 = copyStack(itemstack, i);
    	if(NEIClientConfig.hasSMPCounterPart())
    	{
    		ItemStack typestack = copyStack(itemstack1, 1);
    		if(!infinite && !canItemFitInInventory(mc().thePlayer, itemstack1) && (mc().currentScreen instanceof GuiContainer))
    		{	
    			GuiContainer gui = getGuiContainer();
	    		int increment = typestack.getMaxStackSize();
	    		int given;
	        	for(given = 0; given < itemstack1.stackSize; )
	        	{
	        		int qty = Math.min(itemstack1.stackSize-given, increment);
	        		int slotNo = -1;
	        		for(INEIGuiHandler handler : GuiInfo.guiHandlers)
	        		{
	        			slotNo = handler.getItemSpawnSlot(gui, typestack);
	        			if(slotNo >= 0)
	        				break;
	        		}
	        		if(slotNo == -1)
	        			break;
	        		
	        		Slot slot = gui.inventorySlots.getSlot(slotNo);
	        		int current = (slot.getHasStack() ? slot.getStack().stackSize : 0);
	        		qty = Math.min(qty, slot.getSlotStackLimit() - current);
	        		
	        		ItemStack newStack = copyStack(typestack, qty+current);
	        		slot.putStack(newStack);
	                setSlotContents(slotNo, newStack, true);
	                given+=qty;
	        	}
    			ClientPacketHandler.sendSpawnItem(copyStack(typestack, given), infinite, false);
    		}
    		else
    		{    		
    			ClientPacketHandler.sendSpawnItem(itemstack1, infinite, true);
    		}
    	}
    	else
    	{
    		for(int given = 0; given < itemstack1.stackSize; )
        	{
        		int qty = Math.min(itemstack1.stackSize-given, itemstack1.getMaxStackSize());
        		sendCommand(NEIClientConfig.getStringSetting("command.give"), mc().thePlayer.username, itemstack1.itemID, qty, itemstack1.getItemDamage());
                given+=qty;
        	}
    	}
    }

    public static void updateUnlimitedItems()
    {
        ItemStack itemstack = getHeldItem();
        if(itemstack != null && itemstack.stackSize > 64)
        {
            itemstack.stackSize = 1;
        }
        
        ItemStack aitemstack[] = mc().thePlayer.inventory.mainInventory;
        for(int slot = 0; slot < aitemstack.length; slot++)
        {
            ItemStack itemstack1 = aitemstack[slot];
            if(itemstack1 == null)
            {
                continue;
            }
            if(itemstack1.stackSize < 0 || itemstack1.stackSize > 64)
            {
                itemstack1.stackSize = 111;
            }
            if(itemstack1.getItemDamage() > -32000 && itemstack1.getItemDamage() < -30000)
            {
                itemstack1.setItemDamage(-32000);
            }
        }
    }
    	
	public static boolean isValidItem(ItemStack test)
	{
		for(ItemStack stack : ItemList.items)
		{
			if(areStacksIdentical(stack, test))
				return true;
		}
		return false;
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

	public static boolean shiftKey()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	public static boolean controlKey()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
	}
	
	public static int getCreativeMode()
	{
		if(NEIClientConfig.invCreativeMode())
			return 2;
		else if(mc().playerController.isInCreativeMode())
			return 1;
		else
			return 0;
	}
		
	public static void cycleCreativeMode()
	{
		int mode = getCreativeMode();
		if(NEIClientConfig.hasSMPCounterPart())
		{
			ClientPacketHandler.sendCycleCreativeMode();
		}
		else
		{
			sendCommand(NEIClientConfig.getStringSetting("command.creative"), mode^1, mc().thePlayer.username);
		}
	}

    public static long getTime()
    {
        return mc().theWorld.getWorldInfo().getWorldTime();
    }

    public static void setTime(long l)
    {
        mc().theWorld.getWorldInfo().setWorldTime(l);
    }

    public static void setHourForward(int hour)
    {
		long day = (getTime() / 24000L) * 24000L;
        long newTime = day + 24000L + (long)(hour * 1000);
        
		if(NEIClientConfig.hasSMPCounterPart())
		{
			ClientPacketHandler.sendSetTime(hour);
		}
		else
		{	        
	        sendCommand(NEIClientConfig.getStringSetting("command.time"), newTime);
		}
    }
		
    public static void sendCommand(String command, Object... args)
	{        
		NumberFormat numberformat = NumberFormat.getIntegerInstance();
        numberformat.setGroupingUsed(false);
        MessageFormat messageformat = new MessageFormat(command);
        for(int i = 0; i < args.length; i++)
        {
        	if(args[i] instanceof Integer || args[i] instanceof Long)
        	{
                messageformat.setFormatByArgumentIndex(i, numberformat);
        	}
        }
        mc().thePlayer.sendChatMessage(messageformat.format(args));
	}

	public static boolean isRaining()
    {
        return mc().theWorld.getWorldInfo().isRaining();
    }

    public static void toggleRaining()
    {
		if(NEIClientConfig.hasSMPCounterPart())
		{
			ClientPacketHandler.sendToggleRain();
		}
		else
		{
			sendCommand(NEIClientConfig.getStringSetting("command.rain"), isRaining() ? 0 : 1);
		}
    }
    
	public static void healPlayer()
	{
		if(NEIClientConfig.hasSMPCounterPart())
		{
			ClientPacketHandler.sendHeal();
		}
		else
		{
			sendCommand(NEIClientConfig.getStringSetting("command.heal"), mc().thePlayer.username);
		}	
	}

	public static void toggleMagnetMode()
    {
		if(NEIClientConfig.hasSMPCounterPart())
		{
			ClientPacketHandler.sendToggleMagnetMode();
		}
    }
		
	public static ArrayList<int[]> concatIntegersToRanges(List<Integer> damages)
	{
		ArrayList<int[]> ranges = new ArrayList<int[]>();
		if(damages.size() == 0)return ranges;
		
		Collections.sort(damages);
		int start = -1;
		int next = 0;
		for(Integer i : damages)
		{
			if(start == -1)
			{
				start = next = i;
				continue;
			}
			if(next+1 != i)
			{
				ranges.add(new int[]{start, next});
				start = next = i;
				continue;
			}
			next = i;
		}
		ranges.add(new int[]{start, next});
		return ranges;
	}
	
	public static ArrayList<int[]> addIntegersToRanges(List<int[]> ranges, List<Integer> damages)
	{
		for(int[] range : ranges)
        {
        	for(int integer = range[0]; integer <= range[1]; integer++)
        	{
        		damages.add(integer);
        	}
        }
		return concatIntegersToRanges(damages);
	}
	
	public static void dumpIDs()
	{
		try
		{
			boolean blocks = NEIClientConfig.getBooleanSetting("ID dump.blockIDs");
			boolean items = NEIClientConfig.getBooleanSetting("ID dump.itemIDs");
			boolean unusedblocks = NEIClientConfig.getBooleanSetting("ID dump.unused blockIDs");
			boolean unuseditems = NEIClientConfig.getBooleanSetting("ID dump.unused itemIDs");
			
			String s = "IDMap dump " +new SimpleDateFormat("d-M-y").format(new Date()) + " at "+ new SimpleDateFormat("H.m.s.S").format(new Date()) +".txt";
	        
			File writeTo = new File(Minecraft.getMinecraftDir(), s);
			if(!writeTo.exists())
				writeTo.createNewFile();
			PrintWriter writer = new PrintWriter(writeTo);
			
			for(int i = 1; i < Item.itemsList.length; i++)
			{
				if(i < Block.blocksList.length && Block.blocksList[i] != null && Block.blocksList[i].blockID != 0)
				{
					if(!blocks)
						continue;
					
					Block block = Block.blocksList[i];
					String name = block.getBlockName();
					if(name == null)
						name = block.getClass().getCanonicalName();
					
					writer.println("Block. Name: "+name+". ID: "+i);
				}
				else if(Item.itemsList[i] != null)
				{
					if(!items)
						continue;
					
					Item item = Item.itemsList[i];
					String name = item.getItemName();
					if(name == null)
						name = item.getClass().getCanonicalName();
					
					writer.println("Item. Name: "+name+". ID: "+i);
				}
				else if(i < Block.blocksList.length)
				{
					if(!unusedblocks)
						continue;
					
					writer.println("Block. Unused ID: "+i);
				}
				else
				{
					if(!unuseditems)
						continue;
					
					writer.println("Item. Unused ID: "+i);
				}
			}
			writer.close();
			addChatMessage("Dumped IDMap to "+s);
		}
		catch(Exception e)
		{
			reportException(e);
		}
	}

	public static boolean safeKeyDown(int keyCode)
	{
		try
		{		
			return Keyboard.isKeyDown(keyCode);
		}
		catch (IndexOutOfBoundsException e) 
		{
			return false;
		}
	}
	
	public static void setItemQuantity(int i)
	{
		NEIClientConfig.setItemQuantity(i);
		LayoutManager.quantity.text = Integer.toString(i);
	}

	public static GuiContainer getGuiContainer()
	{
		if(mc().currentScreen instanceof GuiContainer)
			return (GuiContainer) mc().currentScreen;
		
		return null;
	}

	public static void overlayScreen(GuiScreen gui)
	{
		mc().currentScreen = null;
		mc().displayGuiScreen(gui);
	}

	public static boolean altKey()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
	}
}
