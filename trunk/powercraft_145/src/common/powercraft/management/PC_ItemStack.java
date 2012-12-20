package powercraft.management;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class PC_ItemStack
{
    private Object o;
    private int count;
    private int meta;

    public PC_ItemStack(Object o, int count, int meta)
    {
        this.o = o;
        this.count = count;
        this.meta = meta;
    }

    public PC_ItemStack(ItemStack is)
    {
        this(is.getItem(), is.stackSize, is.getItemDamage());
    }
    
    public PC_ItemStack(Object o)
    {
        this(o, 1, 0);
    }

    public PC_ItemStack(Object o, int count)
    {
        this(o, count, 0);
    }

    public ItemStack toItemStack()
    {
        if (o instanceof Block)
        {
            return new ItemStack((Block)o, count, meta);
        }
        else if (o instanceof Item)
        {
            return new ItemStack((Item)o, count, meta);
        }

        return null;
    }

    public int getID()
    {
        if (o instanceof Block)
        {
            return ((Block)o).blockID;
        }
        else if (o instanceof Item)
        {
            return ((Item)o).shiftedIndex;
        }

        return 0;
    }

    public int getCount()
    {
        return count;
    }

    public int getMeta()
    {
        return meta;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ItemStack || obj instanceof PC_ItemStack)
        {
            int otherID;
            int otherCount;
            int otherMeta;

            if (obj instanceof ItemStack)
            {
                otherID = ((ItemStack)obj).itemID;
                otherCount = ((ItemStack)obj).stackSize;
                otherMeta = ((ItemStack)obj).getItemDamage();
            }
            else
            {
                otherID = ((PC_ItemStack)obj).getID();
                otherCount = ((PC_ItemStack)obj).getCount();
                otherMeta = ((PC_ItemStack)obj).getMeta();
            }

            if (otherID != getID())
            {
                return false;
            }

            if (otherMeta != meta && otherMeta != -1 && meta != -1)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public PC_ItemStack copy()
    {
        return new PC_ItemStack(o, count, meta);
    }
}
