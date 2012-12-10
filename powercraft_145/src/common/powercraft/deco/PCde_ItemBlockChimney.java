package powercraft.deco;

import java.util.List;

import net.minecraft.src.ItemStack;
import powercraft.core.PC_ItemBlock;

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
	public String[] getDefaultNames() {
		return new String[]{
				getItemName(), "Chimney",
				getItemName() + ".type0", "Cobblestone Chimney",
				getItemName() + ".type1", "Brick Chimney",
				getItemName() + ".type2", "StoneBrick Chimney",
		};
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
	
}
