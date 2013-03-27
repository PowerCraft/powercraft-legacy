package powercraft.api.inventory;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public interface PC_IInventoryClickHandler {

	public ItemStack slotClick(int slot, int mouseKey, int par3, EntityPlayer entityPlayer);
	
}
