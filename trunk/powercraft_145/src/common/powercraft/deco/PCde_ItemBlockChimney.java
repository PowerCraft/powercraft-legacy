package powercraft.deco;

import java.util.List;

import net.minecraft.src.ItemStack;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

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
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".type0", "Cobblestone Chimney", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".type1", "Brick Chimney", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".type2", "StoneBrick Chimney", null));
            return names;
		}
		return null;
	}
	
}
