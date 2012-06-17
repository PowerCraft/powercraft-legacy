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
	
}
