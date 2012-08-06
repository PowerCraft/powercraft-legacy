package codechicken.nei;

import net.minecraft.src.ItemStack;

/**
 * 
 * Implement this to show custom recipes in the usage viewer.
 * See the default vanilla ones for more info.
 *
 */
public interface ICraftingHandler extends IRecipeHandler
{
	/**
	 * 
	 * @param result An {@link ItemStack} representing the result that matching recipes must produce.
	 * @return An instance of {@link ICraftingHandler} configured with a list of recipes that produce this item.
	 */
	public ICraftingHandler getRecipeHandler(ItemStack result);
}
