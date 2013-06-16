package powercraft.hologram;

import net.minecraft.src.ItemStack;
import powercraft.api.block.PC_ItemBlock;

public class PChg_ItemBlockHologramBlockEmpty extends PC_ItemBlock {

	public PChg_ItemBlockHologramBlockEmpty(int id) {
		super(id);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}

}
