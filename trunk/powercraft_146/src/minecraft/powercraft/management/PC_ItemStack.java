package powercraft.management;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;

public class PC_ItemStack
{
    private Object o;
    private int count;
    private int meta;
    private NBTTagCompound nbtTag;
    
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
    	}else{
    		this.o = is.getItem();
    	} 
    	count = is.stackSize;
        meta = is.getItemDamage();
        if(is.stackTagCompound!=null){
        	nbtTag = (NBTTagCompound)is.stackTagCompound.copy();
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
    	ItemStack is;
        if (o instanceof Block)
        {
        	is = new ItemStack((Block)o, count, meta);
        }
        else if (o instanceof Item)
        {
        	is = new ItemStack((Item)o, count, meta);
        }else{
        	 return null;
        }

        if(nbtTag!=null){
        	is.stackTagCompound = (NBTTagCompound)nbtTag.copy();
        }
        
        return is;
    }

    public void setNBTTag(NBTTagCompound nbtTag){
    	this.nbtTag = nbtTag;
    }
    
    public NBTTagCompound getNBTTag(){
    	return nbtTag;
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

    public void setCount(int count)
    {
        this.count = count;
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
            NBTTagCompound otherNbtTag;
            
            if (obj instanceof ItemStack)
            {
                otherID = ((ItemStack)obj).itemID;
                otherMeta = ((ItemStack)obj).getItemDamage();
                otherNbtTag = ((ItemStack)obj).stackTagCompound;
            }
            else
            {
                otherID = ((PC_ItemStack)obj).getID();
                otherMeta = ((PC_ItemStack)obj).getMeta();
                otherNbtTag =  ((PC_ItemStack)obj).getNBTTag();
            }
            
            if (otherID == getID())
            {
            	if(otherMeta == meta || otherMeta == -1 || meta == -1){
            		if(otherNbtTag==null && nbtTag==null){
            			return true;
            		}else if(otherNbtTag!=null && nbtTag!=null){
            			return otherNbtTag.equals(nbtTag);
            		}
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
    
}
