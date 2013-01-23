package powercraft.transport;

import java.util.List;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

public class PCtr_ItemArmorStickyBoots extends PC_ItemArmor
{
    public PCtr_ItemArmorStickyBoots(int id)
    {
        super(id, EnumArmorMaterial.IRON, 2, FEET);
        setIconCoord(2, 3);
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0x99ff99;
    }
    
    @Override
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
