package powercraft.deco;

import java.util.List;

import net.minecraft.item.ItemStack;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

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
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + ".type" + itemstack.getItemDamage();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, 0));
		arrayList.add(new ItemStack(this, 1, 1));
		arrayList.add(new ItemStack(this, 1, 2));
		arrayList.add(new ItemStack(this, 1, 3));
		arrayList.add(new ItemStack(this, 1, 4));
		arrayList.add(new ItemStack(this, 1, 5));
		arrayList.add(new ItemStack(this, 1, 6));
		arrayList.add(new ItemStack(this, 1, 7));
		arrayList.add(new ItemStack(this, 1, 8));
		arrayList.add(new ItemStack(this, 1, 9));
		arrayList.add(new ItemStack(this, 1, 10));
		arrayList.add(new ItemStack(this, 1, 11));
		arrayList.add(new ItemStack(this, 1, 12));
		arrayList.add(new ItemStack(this, 1, 13));
		arrayList.add(new ItemStack(this, 1, 14));
		arrayList.add(new ItemStack(this, 1, 15));
		
		return arrayList;
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getUnlocalizedName() + ".type0", "Cobblestone Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type1", "Brick Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type2", "StoneBrick Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type3", "CrackedStoneBrick Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type4", "ChiseledStoneBrick Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type5", "Sandstone Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type6", "ChiseledSandstone Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type7", "SmothSandstone Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type8", "NetherBrick Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type9", "QuartzBlock Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type10", "ChiseledQuartzBlock Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type11", "PillarQuartzBlock Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type12", "MossStone Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type13", "MossyStoneBrick Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type14", "Clay Chimney"));
			names.add(new LangEntry(getUnlocalizedName() + ".type15", "IronBlock Chimney"));
			
            return names;
		}
		return null;
	}
	
}
