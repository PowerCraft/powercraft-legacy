package powercraft.deco;

import java.util.List;

import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Utils;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PCde_ItemBlockChimney extends PC_ItemBlock {

	/**
	 * @param i ID
	 */
	public PCde_ItemBlockChimney(int id) {
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}
	
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return getItemName() + ".type" + itemstack.getItemDamage();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, 0));
		arrayList.add(new ItemStack(this, 1, 1));
		arrayList.add(new ItemStack(this, 1, 2));
		return arrayList;
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName() + ".type0", "Cobblestone Chimney"));
			names.add(new LangEntry(getItemName() + ".type1", "Brick Chimney"));
			names.add(new LangEntry(getItemName() + ".type2", "StoneBrick Chimney"));
            return names;
		}
		return null;
	}
	
}
