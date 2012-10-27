package powercraft.core;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;

public class PCco_ItemPowerDust extends PC_Item implements PC_IFuel {

	/**
	 * @param i ID
	 */
	public PCco_ItemPowerDust(int i) {
		super(i);
		setIconCoord(13, 9);
		setItemName("PCcoPowerDust");
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public int getColorFromDamage(int i, int j) {
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

	@Override
	public String getCraftingToolModule() {
		return mod_PowerCraftCore.getInstance().getNameWithoutPowerCraft();
	}
	
}
