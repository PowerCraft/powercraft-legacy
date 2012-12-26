package powercraft.management;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class PC_ItemStack implements Comparable<PC_ItemStack>
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
    	if(is.getItem() instanceof ItemBlock){
    		this.o = Block.blocksList[((ItemBlock)is.getItem()).getBlockID()];
            this.count = is.stackSize;
            this.meta = is.getItemDamage();
    	}else{
    		this.o = is.getItem();
            this.count = is.stackSize;
            this.meta = is.getItemDamage();
    	}
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
            int otherMeta;
            
            if (obj instanceof ItemStack)
            {
                otherID = ((ItemStack)obj).itemID;
                otherMeta = ((ItemStack)obj).getItemDamage();
            }
            else
            {
                otherID = ((PC_ItemStack)obj).getID();
                otherMeta = ((PC_ItemStack)obj).getMeta();
            }
            
            if (otherID == getID())
            {
            	if(otherMeta == meta || otherMeta == -1 || meta == -1){
            		return true;
            	}
            }
            
            return false;
            
        }

        return false;
    }

    public PC_ItemStack copy()
    {
        return new PC_ItemStack(o, count, meta);
    }

	@Override
	public String toString() {
		return "PC_ItemStack("+Item.itemsList[getID()].getItemName()+", "+count+", "+meta+")";
	}

	@Override
	public int compareTo(PC_ItemStack o) {
		int otherID = o.getID();
		if(otherID==getID()){
			 int otherMeta = o.getMeta();
			 if(otherMeta==-1 || meta==-1)
				 return 0;
			 return meta-otherMeta;
		}else{
			return getID()-otherID;
		}
	}
    
}
