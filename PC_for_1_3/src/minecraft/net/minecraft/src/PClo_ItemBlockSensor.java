package net.minecraft.src;


/**
 * Item Block replacement for sensors (overrides itemNameIS)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemBlockSensor extends ItemBlock {
	/**
	 * @param i id
	 * @param block the block
	 */
	public PClo_ItemBlockSensor(int i, Block block) {
		super(i);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return (new StringBuilder()).append(super.getItemName()).append(".")
				.append(itemstack.getItemDamage() == 0 ? "item" : itemstack.getItemDamage() == 1 ? "living" : "player").toString();
	}

}
