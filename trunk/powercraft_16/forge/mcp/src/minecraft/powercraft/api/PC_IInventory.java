package powercraft.api;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;


public interface PC_IInventory extends IInventory {

	public int getSlotStackLimit(int i);


	public boolean canTakeStack(int i, EntityPlayer entityPlayer);

}
