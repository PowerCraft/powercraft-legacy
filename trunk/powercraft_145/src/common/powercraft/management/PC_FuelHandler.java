package powercraft.management;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class PC_FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		Item item = Item.itemsList[fuel.itemID];

        if (item instanceof PC_IMSG)
        {
        	Object o = ((PC_IMSG)item).msg(PC_Utils.MSG_BURN_TIME, fuel);
        	if(o instanceof Integer)
        		return (Integer)o;
        }

        return 0;
	}

}
