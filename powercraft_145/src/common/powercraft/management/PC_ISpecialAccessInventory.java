package powercraft.management;

import net.minecraft.src.ItemStack;

public interface PC_ISpecialAccessInventory {

	public boolean canMachineInsertStackTo(int slot, ItemStack itemstack);

	public boolean insertStackIntoInventory(ItemStack itemstack);

}
