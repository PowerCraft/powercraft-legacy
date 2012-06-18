package net.minecraft.src;

public class PC_GresContainerManager extends Container {

	public PC_GresContainerManager(){
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void addSlot(Slot slot){
		super.addSlot(slot);
	}
	
	@Override
	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {

	}
	
/*    public ItemStack transferStackInSlot(int par1)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par1 < numRows * 9)
            {
                if (!mergeItemStack(itemstack1, numRows * 9, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, numRows * 9, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }*/
	
	public void setSlot(int id, Slot newSlot){
		if(newSlot!=null){
			inventorySlots.set(id, newSlot);
			inventoryItemStacks.set(id, null);
			newSlot.slotNumber = id;
		}
	}
	
	public void removeSlot(int id){
		int i=id;
		for(; i<inventorySlots.size()-1; i++){
			Slot s = (Slot) inventorySlots.get(i+1);
			if(s!=null)
				s.slotNumber = i;
			inventorySlots.set(i, s);
			inventoryItemStacks.set(i, inventorySlots.get(i+1));
		}
		if(i<inventorySlots.size()){
			inventorySlots.remove(i);
			inventoryItemStacks.remove(i);
		}
	}
	
}
