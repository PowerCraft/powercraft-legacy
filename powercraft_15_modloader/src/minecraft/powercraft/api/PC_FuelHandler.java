package powercraft.api;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemArmor;

public class PC_FuelHandler {
	
	public int getBurnTime(ItemStack fuel) {
		Item item = Item.itemsList[fuel.itemID];
		
		if (item instanceof PC_Item) {
			return ((PC_Item) item).getBurnTime(fuel);
		} else if (item instanceof PC_ItemArmor) {
			return ((PC_ItemArmor) item).getBurnTime(fuel);
		} else if (item instanceof PC_ItemBlock) {
			return ((PC_ItemBlock) item).getBurnTime(fuel);
		}
		
		return 0;
	}
	
}
