package powercraft.api;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import powercraft.api.items.PC_Item;
import cpw.mods.fml.common.IFuelHandler;


public class PC_FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {

		Item item = fuel.getItem();
		if (item instanceof PC_Item) {
			return ((PC_Item) item).getBurnTime(fuel);
		}
		return 0;
	}


	public static int getItemBurnTime(ItemStack fuel) {

		return TileEntityFurnace.getItemBurnTime(fuel);
	}


	public static boolean isItemFuel(ItemStack itemstack) {

		return getItemBurnTime(itemstack) > 0;
	}

}
