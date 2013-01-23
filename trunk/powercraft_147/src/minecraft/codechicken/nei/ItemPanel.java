package codechicken.nei;

import java.util.ArrayList;
import java.util.List;

import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

public class ItemPanel extends Widget
{
	public class ItemPanelSlot
	{
		public ItemPanelObject contents;
		public int slotIndex;
		
		public ItemPanelSlot(int index)
		{
			contents = visibleitems.get(index);
			slotIndex = index;
		}

		public ItemStack getItemStack()
		{
			return contents instanceof ItemPanelStack ? ((ItemPanelStack)contents).item : null;
		}
	}
	
	public static interface ItemPanelObject
	{
		void draw(GuiContainerManager gui, int x, int y);
		List<String> handleTooltip(List<String> tooltip);	
	}

    public void resize()
    {
        marginLeft = x + (width % 18) / 2;
        marginTop = y + (height % 18) / 2;
        columns = width / 18;
        rows = height / 18;
        itemsPerPage = rows * columns;
        
        numPages = (int)Math.ceil((float)visibleitems.size() / (float)itemsPerPage);
        setPage(page);
    }

    public void draw(GuiContainerManager gui, int mousex, int mousey)
    {
    	if(visibleitems.size() == 0)
    		return;
    	
        int itemIndex = page * itemsPerPage;
        int lastIndex = itemsPerPage * (page + 1);
        int colIndex = 0;
        int rowIndex = 0;
        
        ItemPanelSlot hoverSlot = getSlotMouseOver(mousex, mousey);
        if(hoverSlot != null)
        {
        	int relIndex = hoverSlot.slotIndex-itemIndex;
        	gui.drawRect(marginLeft+(relIndex%columns)*18-1, marginTop+(relIndex/columns)*18-1, 18, 18, 0xee555555);//highlight
        }
        
        while(itemIndex < lastIndex && itemIndex < visibleitems.size())
        {
        	ItemPanelObject item = visibleitems.get(itemIndex);
            
            int posX = marginLeft + colIndex * 18;
            int posY = marginTop + rowIndex * 18;
            
            item.draw(gui, posX, posY);
            
            itemIndex++;
            colIndex++;
            if(colIndex == columns)
            {
                colIndex = 0;
                rowIndex++;
            }
        }
    }
    
    @Override
    public void postDraw(GuiContainerManager gui, int mousex, int mousey)
    {       
        if(mouseDownSlot >= 0 && gui.shouldShowTooltip() && NEIClientConfig.isActionPermissable(InterActionMap.ITEM))
        {
        	ItemPanelSlot mouseOverSlot = getSlotMouseOver(mousex, mousey);
        	ItemStack stack = new ItemPanelSlot(mouseDownSlot).getItemStack();
        	if(stack != null && (mouseOverSlot == null || mouseOverSlot.slotIndex != mouseDownSlot))
        	{
	        	int amount = NEIClientConfig.getItemQuantity();
				if(amount == 0)
					amount = stack.getMaxStackSize();
				
				draggedStack = NEIClientUtils.copyStack(stack, amount);
        	}
        }
        
        if(draggedStack != null)
        {
        	GuiContainerManager.drawItems.zLevel += 100;
        	gui.drawItem(mousex - 8, mousey - 8, draggedStack);
        	GuiContainerManager.drawItems.zLevel -= 100;
        }
    }

    public boolean handleClick(int mousex, int mousey, int button)
    {
    	if(NEIClientUtils.getHeldItem() != null)
    	{
    		if(NEIClientConfig.isActionPermissable(InterActionMap.DELETE))
    		{
    			if(button == 1)
    			{
    				NEIClientUtils.decreaseSlotStack(-999);
    			}
    			else
    			{
    				NEIClientUtils.deleteHeldItem();
    			}
    		}
    		else
    		{
    			NEIClientUtils.dropHeldItem();
    		}
    		return true;
    	}
    	
    	ItemPanelSlot hoverSlot = getSlotMouseOver(mousex, mousey);    	
    	if(hoverSlot != null)
    	{
    		mouseDownSlot = hoverSlot.slotIndex;
    		return true;
    	}
        return false;
    }
    
    @Override
    public void mouseUp(int mousex, int mousey, int button)
    {
    	ItemPanelSlot hoverSlot = getSlotMouseOver(mousex, mousey);    	
    	if(hoverSlot != null && hoverSlot.slotIndex == mouseDownSlot && hoverSlot.getItemStack() != null && draggedStack == null)
    	{
    		ItemStack item = hoverSlot.getItemStack();
    		if(NEIController.manager.window instanceof GuiRecipe || !NEIClientConfig.isActionPermissable(InterActionMap.ITEM))
        	{
        		if(button == 0)
    				GuiCraftingRecipe.openRecipeGui("item", item);
    			else if(button == 1)
    				GuiUsageRecipe.openRecipeGui("item", item);

            	draggedStack = null;
        		mouseDownSlot = -1;
        		return;
        	}
        	
            NEIClientUtils.cheatItem(item, button, -1);
    	}
    	
    	if(draggedStack != null)
    	{
    		Slot overSlot = NEIClientUtils.getGuiContainer().getSlotAtPosition(mousex, mousey);
    		if(overSlot != null && overSlot.isItemValid(draggedStack))
    		{
				int contents = overSlot.getHasStack() ? overSlot.getStack().stackSize : 0;
				if(overSlot.getHasStack() && !NEIClientUtils.areStacksSameType(draggedStack, overSlot.getStack()))
					contents = 0;
				int total = Math.min(contents+draggedStack.stackSize, Math.min(overSlot.getSlotStackLimit(), draggedStack.getMaxStackSize()));
				
				if(total > contents)
				{
					NEIClientUtils.setSlotContents(overSlot.slotNumber, NEIClientUtils.copyStack(draggedStack, total), true);
					ClientPacketHandler.sendSpawnItem(NEIClientUtils.copyStack(draggedStack, total), false, false);
				}
    		}
    	}

    	draggedStack = null;
		mouseDownSlot = -1;
    }
    
    public boolean onMouseWheel(int i, int mousex, int mousey)
    {
    	if(!contains(mousex, mousey))
    		return false;
    	
    	scroll(-i);
    	return true;
    }
    
    @Override
    public boolean handleKeyPress(int keyID, char keyChar)
    {
        if(keyID == NEIClientConfig.getKeyBinding("next"))
        {
        	scroll(1);
        	return true;
        }
        if(keyID == NEIClientConfig.getKeyBinding("prev"))
        {
        	scroll(-1);
        	return true;
        }
        return false;
    }
    
	public ItemStack getStackMouseOver(int mousex, int mousey)
	{
		ItemPanelSlot slot = getSlotMouseOver(mousex, mousey);
		return slot == null ? null : slot.getItemStack();
	}
	
	public ItemPanelSlot getSlotMouseOver(int mousex, int mousey)
	{
		int relX = mousex-marginLeft+1;
		int relY = mousey-marginTop+1;
		
		if(relX < 0 || relY < 0)
			return null;
		
		int col = relX/18;
		int row = relY/18;
		
		int index = itemsPerPage*page+row*columns+col;
		
		if(index >= 0 && index < visibleitems.size() && index < itemsPerPage*(page+1))
			return new ItemPanelSlot(index);
		
		return null;
	}
	
	@Override
	public List<String> handleTooltip(int mx, int my, List<String> tooltip)
	{
		if(getSlotMouseOver(mx, my) == null)
			return tooltip;
		
		return getSlotMouseOver(mx, my).contents.handleTooltip(tooltip);
	}
	
	public void scroll(int i)
	{
		setPage(page+i);
	}
	
	public void setPage(int i)
	{
		if(numPages == 0)
			page = 0;
		else
			page = (i+numPages)%numPages;
	}
	
	public int getPage()
	{
		return page;
	}
	
	public int getNumPages()
	{
		return numPages;
	}
	
    public static ArrayList<ItemPanelObject> visibleitems = new ArrayList<ItemPanelObject>();
    
    public ItemStack draggedStack;
    public int mouseDownSlot = -1;
    
    private int marginLeft;
    private int marginTop;
    private int columns;
    private int rows;
    private int itemsPerPage;
    private int page;
    private int numPages;
}
