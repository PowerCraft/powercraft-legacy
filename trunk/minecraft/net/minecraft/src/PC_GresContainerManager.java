package net.minecraft.src;

public class PC_GresContainerManager extends Container {
	
	public EntityPlayer thePlayer;
	PC_IGresGui gresGui;
	
	private static final int playerSlots = 9*4;
	public Slot[][] inventoryPlayerUpper = new Slot[9][3];
	public Slot[][] inventoryPlayerLower = new Slot[9][1];
	
	public PC_GresContainerManager(EntityPlayer player){
		thePlayer = player;	
		this.gresGui = gresGui;
		
		//lower player inventory
		for (int i = 0; i < 9; i++){
			inventoryPlayerLower[i][0] = new Slot(player.inventory, i, 0, 0);
		}
		
		//upper player inventory
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 3; j++){
				inventoryPlayerUpper[i][j] = new Slot(player.inventory, i + j * 9 + 9, 0, 0);
			}
		}
		
	}
	
	public void setGresGui(PC_IGresGui gresGui){
		this.gresGui = gresGui;
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
	
    public ItemStack transferStackInSlot(int slotIndex)
    {
    	System.out.println("slot index clicked: "+slotIndex);
    	if(true) return null;
    	
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < playerSlots)
            {
                if (gresGui.canShiftTransfer() && !mergeItemStack(itemstack1, playerSlots, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, playerSlots, false))
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
    }
	
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
