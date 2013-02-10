package powercraft.hologram;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import powercraft.logic.PClo_GateType;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

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
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Hologramblock", null));
            return names;
		}
		return null;
	}

}
