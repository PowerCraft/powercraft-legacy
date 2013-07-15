package powercraft.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface PC_IInvectory extends IInventory {

	public int getSlotStackLimit(int i);

	public boolean canTakeStack(int i, EntityPlayer entityPlayer);
	
}
