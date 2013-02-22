package powercraft.core;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_Item;
import powercraft.management.PC_Utils;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PCco_ItemPowerDust extends PC_Item {

	public PCco_ItemPowerDust(int id) {
		super(id, false);
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
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Power Dust"));
            return names;
		case PC_Utils.MSG_BURN_TIME:
			 return 3600;
		}
		return null;
	}

}
