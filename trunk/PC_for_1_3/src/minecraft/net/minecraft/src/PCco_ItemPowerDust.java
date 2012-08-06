package net.minecraft.src;



/**
 * Power Dust, fuel crafted of Power Crystals.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_ItemPowerDust extends Item {
	/**
	 * @param i ID
	 */
	public PCco_ItemPowerDust(int i) {
		super(i);
	}

	@Override
	public int getColorFromDamage(int i, int j) {
		return 0xFF3333;
	}

	@Override
	public boolean hasEffect(ItemStack itemstack) {
		return true;
	}
}
