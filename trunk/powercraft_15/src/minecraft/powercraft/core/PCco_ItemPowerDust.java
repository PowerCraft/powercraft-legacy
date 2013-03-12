package powercraft.core;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCco_ItemPowerDust extends PC_Item {

	public PCco_ItemPowerDust(int id) {
		super(id, false);
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
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getUnlocalizedName(), "Power Dust"));
            return names;
		case PC_MSGRegistry.MSG_BURN_TIME:
			 return 3600;
		}
		return null;
	}

}
