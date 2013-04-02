package powercraft.api.inventory;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ISidedInventory;
import net.minecraft.src.ItemStack;

public interface PC_IInventory extends IInventory, ISidedInventory {
	
	public boolean canPlayerInsertStackTo(int i, ItemStack itemstack);
	
	public boolean canPlayerTakeStack(int i, EntityPlayer entityPlayer);
	
	public boolean canDispenseStackFrom(int i);
	
	public boolean canDropStackFrom(int i);
	
	public int getSlotStackLimit(int i);
	
}
