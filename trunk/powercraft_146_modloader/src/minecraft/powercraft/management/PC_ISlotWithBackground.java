package powercraft.management;

import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public interface PC_ISlotWithBackground {

	public ItemStack getBackgroundStack();

	public Slot setBackgroundStack(ItemStack stack);
	
	public boolean renderTooltipWhenEmpty();

	public boolean renderGrayWhenEmpty();

}
