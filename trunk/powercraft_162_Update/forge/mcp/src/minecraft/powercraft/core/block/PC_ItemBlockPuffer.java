package powercraft.core.block;


import net.minecraft.item.ItemStack;
import powercraft.api.block.PC_ItemBlock;


public class PC_ItemBlockPuffer extends PC_ItemBlock {

	public PC_ItemBlockPuffer(int id) {

		super(id);
		setMaxDamage(PC_TileEntityPuffer.maxEnergy);
	}


	@Override
	public int getDisplayDamage(ItemStack itemStack) {

		return itemStack.getMaxDamage() - itemStack.getItemDamage();
	}

}
