package powercraft.net;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemBlock;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_MSGRegistry;

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
	public String getItemNameIS(ItemStack itemstack) {
		return super.getItemName() + "." + (itemstack.getItemDamage() == 0 ? "tx" : "rx");
	}

	@Override
	public int getIconFromDamage(int i) {
		return Block.blocksList[getBlockID()].getBlockTextureFromSideAndMetadata(1, 0);
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
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName() + ".tx", "Redstone Radio Transmitter"));
			names.add(new LangEntry(getItemName() + ".rx", "Redstone Radio Receiver"));
            return names;
		}
		return null;
	}

}
