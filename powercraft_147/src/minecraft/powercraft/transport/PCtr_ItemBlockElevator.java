package powercraft.transport;

import java.util.List;

import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Utils;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PCtr_ItemBlockElevator extends PC_ItemBlock
{
    public PCtr_ItemBlockElevator(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i)
    {
        return i == 0 ? 0 : 1;
    }

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
        return getItemName() + "." + (itemstack.getItemDamage() == 0 ? "up" : "down");
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this, 1, 0));
        arrayList.add(new ItemStack(this, 1, 1));
        return arrayList;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName() + ".up", "elevator up"));
			names.add(new LangEntry(getItemName() + ".down", "elevator down"));
            return names;
		}
		return null;
	}
}
