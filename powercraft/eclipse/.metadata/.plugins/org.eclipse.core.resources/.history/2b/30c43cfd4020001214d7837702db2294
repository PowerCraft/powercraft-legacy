package powercraft.core;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class PC_FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		Item item = Item.itemsList[fuel.itemID];
		if(item instanceof PC_IFuel)
			return ((PC_IFuel)item).getBurnTime(fuel);
		return 0;
	}

}
