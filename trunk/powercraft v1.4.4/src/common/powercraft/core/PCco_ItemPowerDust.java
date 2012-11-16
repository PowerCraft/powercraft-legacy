package powercraft.core;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;

public class PCco_ItemPowerDust extends PC_Item implements PC_IFuel {

	/**
	 * @param i ID
	 */
	public PCco_ItemPowerDust(int i) {
		super(i, false);
		setIconCoord(13, 9);
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public int getColorFromItemStack(ItemStack itemStack, int pass) {
		return 0xFF3333;
	}

	@Override
	public boolean hasEffect(ItemStack itemstack) {
		return true;
	}
	
	@Override
	public String[] getDefaultNames() {
		return new String[]{getItemName(), "Power Dust"};
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		return 3600;
	}

}
