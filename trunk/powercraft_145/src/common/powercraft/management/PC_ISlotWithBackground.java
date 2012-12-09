package powercraft.management;

import net.minecraft.src.ItemStack;

public interface PC_ISlotWithBackground {

	public ItemStack getBackgroundStack();

	public boolean renderTooltipWhenEmpty();

	public boolean renderGrayWhenEmpty();

}
