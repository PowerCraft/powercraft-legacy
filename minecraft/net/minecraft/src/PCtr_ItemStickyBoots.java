package net.minecraft.src;


/**
 * Slime boots
 * 
 * @author MightyPork
 */
public class PCtr_ItemStickyBoots extends ItemArmor {

	public PCtr_ItemStickyBoots(int id, int index) {
		super(id, EnumArmorMaterial.IRON, index, 3);
	}

	@Override
	public int getColorFromDamage(int par1, int par2) {
		return 0x99ff99;
	}

}
