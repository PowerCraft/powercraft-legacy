package powercraft.management.inventory;

import powercraft.management.PC_Utils.ValueWriting;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class PC_Slot extends Slot {
	
    protected int backgroundIconIndex = -1;
    protected String texture = "/gui/items.png";
	private ItemStack bgStack = null;
	
	public PC_Slot(IInventory inv, int slot) {
		super(inv, slot, 0, 0);
		slotNumber = -1;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack){
		if (inventory instanceof PC_ISpecialAccessInventory){
			return ((PC_ISpecialAccessInventory) inventory).canPlayerInsertStackTo(getSlotIndex(), itemStack);
		}
		return true;
	}

	@Override
	public int getSlotStackLimit(){
		if(inventory instanceof PC_ISpecialAccessInventory){
    		return ((PC_ISpecialAccessInventory) inventory).getSlotStackLimit(getSlotIndex());
    	}
		return inventory.getInventoryStackLimit();
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer entityPlayer){
		if(inventory instanceof PC_ISpecialAccessInventory){
			return ((PC_ISpecialAccessInventory) inventory).canPlayerTakeStack(getSlotIndex(), entityPlayer);
		}
		return true;
	}
	
    public ItemStack getBackgroundStack(){
    	if(inventory instanceof PC_IInventoryBackground){
    		return ((PC_IInventoryBackground) inventory).getBackgroundStack(getSlotIndex());
    	}
        return bgStack;
    }

    public PC_Slot setBackgroundStack(ItemStack stack){
    	if(inventory instanceof PC_IInventoryBackground)
    		return this;
    	if(stack==null){
    		bgStack = null;
    	}else{
    		bgStack = stack.copy();
    	}
        return this;
    }

    public boolean renderTooltipWhenEmpty(){
    	if(inventory instanceof PC_IInventoryBackground){
    		return ((PC_IInventoryBackground) inventory).renderTooltipWhenEmpty(getSlotIndex());
    	}
        return false;
    }

    public boolean renderGrayWhenEmpty(){
    	if(inventory instanceof PC_IInventoryBackground){
    		return ((PC_IInventoryBackground) inventory).renderGrayWhenEmpty(getSlotIndex());
    	}
        return true;
    }
    
    public boolean useAlwaysBackground(){
    	if(inventory instanceof PC_IInventoryBackground){
    		return ((PC_IInventoryBackground) inventory).useAlwaysBackground(getSlotIndex());
    	}
        return false;
    }
    
    public boolean isHandlingSlotClick(){
    	return inventory instanceof PC_IInventoryClickHandler;
    }
    
    public ItemStack slotClick(int mouseKey, int par3, EntityPlayer entityPlayer){
    	if(inventory instanceof PC_IInventoryClickHandler){
    		return ((PC_IInventoryClickHandler) inventory).slotClick(getSlotIndex(), mouseKey, par3, entityPlayer);
    	}
    	return null;
    }
    
    public int getSlotIndex() {
		return (Integer)ValueWriting.getPrivateValue(Slot.class, this, 0);
	}
    
    public int getBackgroundIconIndex()
    {
        return backgroundIconIndex;
    }

    public String getBackgroundIconTexture()
    {
        return (texture == null ? "/gui/items.png" : texture);
    }

    public void setBackgroundIconIndex(int iconIndex)
    {
        backgroundIconIndex = iconIndex;
    }

    public void setBackgroundIconTexture(String textureFilename)
    {
        texture = textureFilename;
    }
    
}
