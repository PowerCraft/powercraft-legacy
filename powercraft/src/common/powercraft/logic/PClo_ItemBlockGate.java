package powercraft.logic;

import java.util.List;

import net.minecraft.src.ItemStack;
import powercraft.core.PC_ItemBlock;

public class PClo_ItemBlockGate extends PC_ItemBlock {

	public PClo_ItemBlockGate(int id){
		super(id);
	}
	
	@Override
	public String[] getDefaultNames() {
		return new String[]{
				getItemName()+".gate0", "Gate AND2",
				getItemName(), "Gate"
		};
	}

	@Override
	public String getCraftingToolModule(){
		return mod_PowerCraftLogic.getInstance().getNameWithoutPowerCraft();
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		for(int i=0; i<PClo_BlockGate.getMaxGates; i++)
			arrayList.add(new ItemStack(this, 1, i));
		return arrayList;
	}
	
}
