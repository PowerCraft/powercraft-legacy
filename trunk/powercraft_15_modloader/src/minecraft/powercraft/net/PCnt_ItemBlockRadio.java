package powercraft.net;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;

public class PCnt_ItemBlockRadio extends PC_ItemBlock {

	/**
	 * @param i ID
	 */
	public PCnt_ItemBlockRadio(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return super.getUnlocalizedName() + "." + (itemstack.getItemDamage() == 0 ? "tx" : "rx");
	}

	@Override
	public Icon getIconFromDamage(int i) {
		return Block.blocksList[getBlockID()].getIcon(1, 0);
	}

	@Override
	public boolean isFull3D() {
		return false;
	}
	
	
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, 0));
		arrayList.add(new ItemStack(this, 1, 1));
		return arrayList;
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName() + ".tx", "Redstone Radio Transmitter"));
		names.add(new LangEntry(getUnlocalizedName() + ".rx", "Redstone Radio Receiver"));
        return names;
	}
	
}
