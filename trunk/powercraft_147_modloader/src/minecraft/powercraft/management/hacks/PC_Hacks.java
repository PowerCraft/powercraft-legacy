package powercraft.management.hacks;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import powercraft.management.PC_Block;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_ItemBlock;

public class PC_Hacks {

	public static String getTextureFile(ItemStack itemStack, String _default){
		Item item = itemStack.getItem();
		if(item instanceof PC_Item){
			return ((PC_Item)item).getTextureFile();
		}else if(item instanceof PC_ItemArmor){
			return ((PC_ItemArmor)item).getTextureFile();
		}else if(item instanceof PC_ItemBlock){
			return ((PC_ItemBlock)item).getTextureFile();
		}else if(item instanceof ItemBlock){
			Block b = Block.blocksList[((ItemBlock) item).getBlockID()];
			if(b instanceof PC_Block){
				return ((PC_Block) b).getTextureFile();
			}
		}
		return _default;
	}
	
	public static String getArmorTextureFile(ItemStack itemStack, String _default){
		Item item = itemStack.getItem();
		if(item instanceof PC_ItemArmor){
			return ((PC_ItemArmor) item).getArmorTextureFile(itemStack);
		}
		return _default;
	}
	
}
