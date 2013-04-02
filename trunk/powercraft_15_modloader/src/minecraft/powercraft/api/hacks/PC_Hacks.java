package powercraft.api.hacks;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import powercraft.api.item.PC_ItemArmor;

public class PC_Hacks {
	
	public static String getArmorTextureFile(ItemStack itemStack, String _default) {
		Item item = itemStack.getItem();
		if (item instanceof PC_ItemArmor) {
			return ((PC_ItemArmor) item).getArmorTextureFile(itemStack);
		}
		return _default;
	}
	
}
