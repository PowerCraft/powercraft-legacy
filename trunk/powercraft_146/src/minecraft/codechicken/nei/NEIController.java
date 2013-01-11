package codechicken.nei;

import java.awt.Point;
import java.util.LinkedList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import codechicken.nei.api.API;
import codechicken.nei.api.GuiInfo;
import codechicken.nei.api.IInfiniteItemHandler;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerSlotClickHandler;

public class NEIController implements IContainerSlotClickHandler
{
	public static void load()
	{
		GuiContainerManager.addSlotClickHandler(new NEIController());
	}
	
    public static void load(GuiContainer gui)
    {
    	manager = gui.manager;
        if(!NEIClientConfig.isEnabled())
        	return;
        
        fastTransferManager = new FastTransferManger();
        deleteMode = false;
        GuiInfo.clearGuiHandlers();
        if(gui instanceof INEIGuiHandler)
        	API.registerNEIGuiHandler((INEIGuiHandler) gui);
    }
    
    ItemStack firstheld;
    @Override
    public void beforeSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier)
    {
    	firstheld = NEIClientUtils.getHeldItem();
    }
    
    @Override
    public boolean handleSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier, boolean eventconsumed)
    {
    	if(eventconsumed)
    		return eventconsumed;
    	
    	if(deleteMode && slotIndex >= 0 && slot != null)
        {
            if(NEIClientUtils.shiftKey() && button == 0)
            {
                ItemStack itemstack1 = slot.getStack();
                if(itemstack1 != null)
                    NEIClientUtils.deleteItemsOfType(itemstack1);
                
            } 
            else if(button == 1)
                NEIClientUtils.decreaseSlotStack(slot.slotNumber);
            else
                NEIClientUtils.deleteSlotStack(slot.slotNumber);
            return true;
        }
    	
    	if(button == 1 && slot instanceof SlotCrafting)//right click
        {
            for(int i1 = 0; i1 < 64; i1++)//click this slot 64 times
                gui.handleMouseClick(slot, slotIndex, button, 0);
            return true;
        }
    	
    	if(slotIndex >= 0 && NEIClientUtils.shiftKey() && NEIClientUtils.getHeldItem() != null && !slot.getHasStack())
        {
        	ItemStack held = NEIClientUtils.getHeldItem();
            gui.handleMouseClick(slot, slotIndex, button, 0);
            if(slot.isItemValid(held) && !ItemInfo.fastTransferExemptions.contains(slot.getClass()))
            {
            	fastTransferManager.performMassTransfer(gui, pickedUpFromSlot, slotIndex, held);
            }
            
            return true;
        }
    	
    	if(NEIClientUtils.controlKey() 
    			&& slot != null && slot.getStack() != null
    			&& NEIClientConfig.isActionPermissable("item")
    			&& slot.isItemValid(slot.getStack()))
    	{
    		NEIClientUtils.cheatItem(slot.getStack(), button, 1);
    		return true;
    	}
    	
    	if(slotIndex == -999 && NEIClientUtils.shiftKey() && button == 0)
        {
    		fastTransferManager.throwAll(gui, pickedUpFromSlot);
    		return true;
        }
    	
    	if(NEIClientUtils.safeKeyDown(NEIClientUtils.mc().gameSettings.keyBindDrop.keyCode) && slotIndex >= 0)
    	{
    		if(button == 0 && NEIClientUtils.shiftKey())
    		{
        		fastTransferManager.clickSlot(gui, slotIndex);
        		fastTransferManager.throwAll(gui, slotIndex);
        		fastTransferManager.clickSlot(gui, slotIndex);
    		}
    		else
    		{
        		fastTransferManager.clickSlot(gui, slotIndex);
        		fastTransferManager.clickSlot(gui, -999, button);
        		fastTransferManager.clickSlot(gui, slotIndex);
    		}
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    public void afterSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier)
    {
        ItemStack nowHeld = NEIClientUtils.getHeldItem();
        
        if(firstheld != nowHeld)
        	pickedUpFromSlot = slotIndex;
        
        if(NEIClientConfig.isActionPermissable(InterActionMap.ITEM) && NEIClientConfig.hasSMPCounterPart())
        {        
	        if(heldStackInfinite != null && slot != null && slot.inventory == NEIClientUtils.mc().thePlayer.inventory)
	        {        		
	        	ItemStack stack = slot.getStack();
	        	if(stack != null)
	        	{
	        		heldStackInfinite.onPlaceInfinite(stack);
	        	}
	        	NEIClientUtils.setSlotContents(slotIndex, stack, true);
	        }
	        
	        if(firstheld != nowHeld)
	        	heldStackInfinite = null;
	        
	        if(firstheld != nowHeld && nowHeld != null)
	        {
	        	for(IInfiniteItemHandler handler : ItemInfo.infiniteHandlers)
	    		{
	    			if(handler.canHandleItem(nowHeld) && handler.isItemInfinite(nowHeld))
	    			{
	    				handler.onPickup(nowHeld);
	    	        	NEIClientUtils.setSlotContents(-999, nowHeld, true);
	    				heldStackInfinite = handler;
	    				break;
	    			}
	    		}
	        }
        }
    }
    
	public static void updateUnlimitedItems(InventoryPlayer inventory)
	{
		if(!NEIClientConfig.isActionPermissable(InterActionMap.ITEM) || !NEIClientConfig.hasSMPCounterPart())
			return;
				
		LinkedList<ItemStack> beforeStacks = new LinkedList<ItemStack>();
		for(int i = 0; i < inventory.getSizeInventory(); i++)
		{
			beforeStacks.add(NEIClientUtils.copyStack(inventory.getStackInSlot(i)));
		}
		
    	for(int i = 0; i < inventory.getSizeInventory(); i++)
    	{
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack == null)
				continue;
			
			for(IInfiniteItemHandler handler : ItemInfo.infiniteHandlers)
			{
				if(handler.canHandleItem(stack) && handler.isItemInfinite(stack))
				{
					handler.replenishInfiniteStack(inventory, i);
				}
			}
    	}
    	
    	for(int i = 0; i < inventory.getSizeInventory(); i++)
		{
    		ItemStack newstack = inventory.getStackInSlot(i);
    		
			if(!NEIClientUtils.areStacksIdentical(beforeStacks.get(i), newstack))
			{
				inventory.setInventorySlotContents(i, beforeStacks.get(i));//restore in case of SMP fail
				NEIClientUtils.setSlotContents(i, newstack, false);//sends via SMP handler ;)
			}
		}
	}

	public static boolean mouseScrolled(int i)
	{
		Point mousePos = manager.getMousePosition();		
		Slot mouseover = manager.window.getSlotAtPosition(mousePos.x, mousePos.y);
		if(mouseover != null && mouseover.getHasStack())
		{			
			if(i > 0)
			{
				fastTransferManager.transferItem(manager.window, mouseover.slotNumber);
			}
			else
			{
				fastTransferManager.retrieveItem(manager.window, mouseover.slotNumber);
			}
			return true;
		}
		return false;
	}

	public static void processCreativeCycling(InventoryPlayer inventory)
	{
		if(NEIClientConfig.invCreativeMode() && NEIClientUtils.controlKey())
		{
			if(selectedItem != inventory.currentItem)
			{
				if(inventory.currentItem == selectedItem+1 || (inventory.currentItem == 0 && selectedItem == 8))//foward
				{
		    		ClientPacketHandler.sendCreativeScroll(1);
					inventory.currentItem = selectedItem;
				}
				else if(inventory.currentItem == selectedItem-1 || (inventory.currentItem == 8 && selectedItem == 0))
				{
		    		ClientPacketHandler.sendCreativeScroll(-1);
					inventory.currentItem = selectedItem;
				}
			}
		}
		
		selectedItem = inventory.currentItem;	
	}

    public static GuiContainerManager manager;
    public static FastTransferManger fastTransferManager;
    
    public static boolean deleteMode;
    private static int pickedUpFromSlot;
    private static IInfiniteItemHandler heldStackInfinite;
    
    private static int selectedItem;
}
