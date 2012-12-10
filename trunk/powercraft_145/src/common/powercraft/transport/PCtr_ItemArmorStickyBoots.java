package powercraft.transport;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemStack;
import powercraft.management.PC_ItemArmor;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class PCtr_ItemArmorStickyBoots extends PC_ItemArmor
{
    public PCtr_ItemArmorStickyBoots(int id)
    {
        super(id, EnumArmorMaterial.IRON, 2, FEET);
        setIconCoord(2, 3);
    }

    @Override
    public String getDefaultName()
    {
        return "Sticky Iron Boots";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0x99ff99;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColor(ItemStack par1ItemStack)
    {
        return 0x99ff99;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
