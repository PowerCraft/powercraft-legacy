package powercraft.core;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

public class PCco_ItemPowerDust extends PC_Item {

	public PCco_ItemPowerDust() {
		super(false);
	    setIconCoord(13, 9);
	    setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        return 0xFF3333;
    }

    @Override
    public boolean hasEffect(ItemStack itemstack)
    {
        return true;
    }
	
	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Power Dust", null));
            return names;
		case PC_Utils.MSG_BURN_TIME:
			 return 3600;
		}
		return null;
	}

}
