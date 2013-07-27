package powercraft.coreOld;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCco_ItemPowerDust extends PC_Item {

	public PCco_ItemPowerDust(int id) {
		super(id, "powerdust");
	    setCreativeTab(CreativeTabs.tabMaterials);
	}
	
    @Override
    public boolean hasEffect(ItemStack itemstack)
    {
        return true;
    }

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Power dust"));
        return names;
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		return 3600;
	}
	
}
