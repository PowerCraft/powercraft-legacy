package powercraft.api.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public interface PC_IInventory extends IInventory, ISidedInventory {
	
	public boolean canPlayerInsertStackTo(int i, ItemStack itemstack);

    public boolean canPlayerTakeStack(int i, EntityPlayer entityPlayer);
    
    public boolean canDispenseStackFrom(int i);
    
    public boolean canDropStackFrom(int i);

	public int getSlotStackLimit(int i);
	
}
