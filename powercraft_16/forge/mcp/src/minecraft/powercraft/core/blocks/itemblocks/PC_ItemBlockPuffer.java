package powercraft.core.blocks.itemblocks;


import net.minecraft.item.ItemStack;
import powercraft.api.blocks.PC_ItemBlock;
import powercraft.core.blocks.tileentities.PC_TileEntityPuffer;


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
