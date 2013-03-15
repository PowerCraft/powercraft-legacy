package powercraft.net;

import java.util.List;

import net.minecraft.item.ItemStack;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCnt_ItemBlockSensor extends PC_ItemBlock {

	public PCnt_ItemBlockSensor(int id) {
		super(id);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return (new StringBuilder()).append(super.getUnlocalizedName()).append(".")
				.append(itemstack.getItemDamage() == 0 ? "item" : itemstack.getItemDamage() == 1 ? "living" : "player").toString();
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
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getUnlocalizedName() + ".item", "Item Proximity Detector"));
			names.add(new LangEntry(getUnlocalizedName() + ".living", "Mob Proximity Detector"));
			names.add(new LangEntry(getUnlocalizedName() + ".player", "Player Proximity Detector"));
            return names;
		}
		return null;
	}

}
