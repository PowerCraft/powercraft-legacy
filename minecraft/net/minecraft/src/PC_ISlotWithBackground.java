package net.minecraft.src;

/**
 * 
 * 
 * @author MightyPork
 *
 */
public interface PC_ISlotWithBackground {

	public abstract ItemStack getBackgroundStack();

	public abstract Slot setBackgroundStack(ItemStack stack);
	
	public abstract boolean renderTooltipWhenEmpty();
	
	public abstract boolean renderGrayWhenEmpty();

}
