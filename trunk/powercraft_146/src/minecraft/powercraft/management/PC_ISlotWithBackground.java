package powercraft.management;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface PC_ISlotWithBackground {

	public ItemStack getBackgroundStack();

	public Slot setBackgroundStack(ItemStack stack);
	
	public boolean renderTooltipWhenEmpty();

	public boolean renderGrayWhenEmpty();

}
