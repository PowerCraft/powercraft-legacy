package powercraft.management;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import powercraft.management.registry.PC_MSGRegistry;

public class PC_FuelHandler {

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
