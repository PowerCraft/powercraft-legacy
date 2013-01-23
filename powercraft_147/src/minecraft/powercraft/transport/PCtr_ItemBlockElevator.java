package powercraft.transport;

import java.util.List;

import net.minecraft.item.ItemStack;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;

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
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".up", "elevator up", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName() + ".down", "elevator down", null));
            return names;
		}
		return null;
	}
}
