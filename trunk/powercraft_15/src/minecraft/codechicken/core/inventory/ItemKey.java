package codechicken.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.base.Objects;

public class ItemKey implements Comparable<ItemKey>
{
    public ItemStack item;
    private int hashcode = 0;
    
    public ItemKey(ItemStack k)
    {
        item = k;
    }

    public ItemKey(int id, int damage)
    {
        this(new ItemStack(id, 1, damage));
    }
    
    public ItemKey(int id, int damage, NBTTagCompound compound)
    {
        this(id, damage);
        item.setTagCompound(compound);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof ItemKey))
            return false;
        
        ItemKey k = (ItemKey)obj;
        return item.itemID == k.item.itemID &&
                item.getItemDamage() == k.item.getItemDamage() &&
                Objects.equal(item.stackTagCompound, k.item.stackTagCompound);
    }
    
    @Override
    public int hashCode()
    {
        return hashcode != 0 ? hashcode : (hashcode = Objects.hashCode(item.itemID, item.getItemDamage(), item.stackTagCompound));
    }

    @Override
    public int compareTo(ItemKey o)
    {
        if(item.itemID != o.item.itemID) return Integer.compare(item.itemID, o.item.itemID);
        if(item.getItemDamage() != o.item.getItemDamage()) return Integer.compare(item.getItemDamage(), o.item.getItemDamage());
        return 0;
    }
}
