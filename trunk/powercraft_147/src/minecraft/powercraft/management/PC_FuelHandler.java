package powercraft.management;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.management.registry.PC_MSGRegistry;
import cpw.mods.fml.common.IFuelHandler;

public class PC_FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		Item item = Item.itemsList[fuel.itemID];

        if (item instanceof PC_IMSG)
        {
        	Object o = ((PC_IMSG)item).msg(PC_MSGRegistry.MSG_BURN_TIME, fuel);
        	if(o instanceof Integer)
        		return (Integer)o;
        }

        return 0;
	}

}
