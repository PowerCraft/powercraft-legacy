package powercraft.transport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ItemStack;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_LangRegistry.LangEntry;

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
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return getUnlocalizedName() + "." + (itemstack.getItemDamage() == 0 ? "up" : "down");
    }

    @Override
    public List<ItemStack> getItemStacks(List<ItemStack> arrayList)
    {
        arrayList.add(new ItemStack(this, 1, 0));
        arrayList.add(new ItemStack(this, 1, 1));
        return arrayList;
    }
    
	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName() + ".up", "elevator up"));
		names.add(new LangEntry(getUnlocalizedName() + ".down", "elevator down"));
        return names;
	}
}
