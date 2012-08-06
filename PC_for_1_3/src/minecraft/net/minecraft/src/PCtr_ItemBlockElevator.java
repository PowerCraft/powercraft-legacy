package net.minecraft.src;


/**
 * elevator block item
 * 
 * @author MightyPork
 */
public class PCtr_ItemBlockElevator extends ItemBlock {

	/**
	 * @param i ID
	 */
	public PCtr_ItemBlockElevator(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getIconFromDamage(int i) {
		return 23;
	}

	@Override
	public int getMetadata(int i) {
		return i == 0 ? 0 : 1;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return (new StringBuilder()).append(super.getItemName()).append(".").append(itemstack.getItemDamage() == 0 ? "up" : "down").toString();
	}
}
