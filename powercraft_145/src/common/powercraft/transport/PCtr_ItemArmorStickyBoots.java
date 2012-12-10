package powercraft.transport;

import java.util.List;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemStack;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class PCtr_ItemArmorStickyBoots extends PC_ItemArmor
{
    public PCtr_ItemArmorStickyBoots()
    {
        super(EnumArmorMaterial.IRON, 2, FEET);
        setIconCoord(2, 3);
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
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Sticky Iron Boots", null));
            return names;
		}
		return null;
	}
}
