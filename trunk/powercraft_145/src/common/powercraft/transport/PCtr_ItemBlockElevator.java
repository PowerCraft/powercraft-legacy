package powercraft.transport;

import java.util.List;

import net.minecraft.src.ItemStack;
import powercraft.management.PC_ItemBlock;

public class PCtr_ItemBlockElevator extends PC_ItemBlock
{
    public PCtr_ItemBlockElevator(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public String[] getDefaultNames()
    {
        return new String[]
                {
                    getItemName() + ".up", "elevator up",
                    getItemName() + ".down", "elevator down",
                    getItemName(), "elevator"
                };
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
		// TODO Auto-generated method stub
		return null;
	}
}
