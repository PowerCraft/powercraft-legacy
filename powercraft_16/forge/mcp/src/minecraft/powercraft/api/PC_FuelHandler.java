package powercraft.api;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import powercraft.api.items.PC_Item;
import cpw.mods.fml.common.IFuelHandler;

/**
 * 
 * Implementation of forge fule handler interface
 * 
 * @author XOR
 *
 */
public class PC_FuelHandler implements IFuelHandler {

	/**
	 * gets the burntime of a itemstack <br>
	 * if item of the itemstack is instanceod {@link PC_Item} 
	 * then if will return the return form PC_Item.getBurnTime(fuel)
	 * @param fuel the burning itemstack
	 * @return the time to burn in ticks
	 */
	@Override
	public int getBurnTime(ItemStack fuel) {

		Item item = fuel.getItem();
		if (item instanceof PC_Item) {
			return ((PC_Item) item).getBurnTime(fuel);
		}
		return 0;
	}


	/**
	 * gets the burntime of the itmestack
	 * @param fuel the fuel itemstack
	 * @return the time to burn
	 */
	public static int getItemBurnTime(ItemStack fuel) {

		return TileEntityFurnace.getItemBurnTime(fuel);
	}

	/**
	 * looks if a itemstack can burn
	 * @param itemstack the itemstack
	 * @return if this itemstack can burn
	 */
	public static boolean isItemFuel(ItemStack itemstack) {

		return getItemBurnTime(itemstack) > 0;
	}

}
