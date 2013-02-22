package powercraft.hologram;

import java.util.List;

import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Utils;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PChg_ItemBlockHologramBlockEmpty extends PC_ItemBlock {

	public PChg_ItemBlockHologramBlockEmpty(int id) {
		super(id);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}
	
	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Hologramblock"));
            return names;
		}
		return null;
	}

}
