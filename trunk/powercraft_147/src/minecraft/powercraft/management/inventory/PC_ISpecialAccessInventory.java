package powercraft.management.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface PC_ISpecialAccessInventory {

	public boolean insertStackIntoInventory(ItemStack stack);

    public boolean needsSpecialInserter();

    public boolean canPlayerInsertStackTo(int slot, ItemStack stack);

    public boolean canMachineInsertStackTo(int slot, ItemStack stack);

    public boolean canDispenseStackFrom(int slot);
    
    public boolean canDropStackFrom(int slot);

	public int getSlotStackLimit(int slotIndex);

	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer);
    
}
