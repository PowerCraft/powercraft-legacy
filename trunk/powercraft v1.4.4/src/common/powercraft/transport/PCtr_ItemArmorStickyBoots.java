package powercraft.transport;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemStack;
import powercraft.core.PC_ItemArmor;
import powercraft.core.PC_Utils;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class PCtr_ItemArmorStickyBoots extends PC_ItemArmor {

	public PCtr_ItemArmorStickyBoots(int id) {
		super(id, EnumArmorMaterial.IRON, PC_Utils.addArmor("pcslime"), FEET);
		setIconCoord(2, 3); 
	}

	@Override
	public String getDefaultName() {
		return "Sticky Iron Boots";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return 0x99ff99;
	}
	
}
