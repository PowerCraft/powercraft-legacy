package net.minecraft.src;



/**
 * Power Crystal replacement ItemBlock.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_ItemBlockPowerCrystal extends ItemBlock {
	/**
	 * @param i id
	 */
	public PCco_ItemBlockPowerCrystal(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return super.getItemName() + ".color" + Integer.toString(itemstack.getItemDamage());
	}

	@Override
	public int getColorFromDamage(int meta, int pass) {
		return PC_Color.crystal_colors[MathHelper.clamp_int(meta, 0, 7)];
	}

	@Override
	public boolean hasEffect(ItemStack itemstack) {
		return true;
	}

	@Override
	public EnumRarity getRarity(ItemStack itemstack) {
		return EnumRarity.rare;
	}
}
