package codechicken.nei;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

/**
 * A container class for multiple Item Ranges.
 *
 */
public class MultiItemRange
{
	public boolean isItemInRange(int itemid, int damage)
	{
		return false;
	}
	
	/**
	 * Constructs a {@link MultiItemRange} from the specified string. 
	 * Look for the references in {@link DropDownFile} and the NEISubsetConfig for reference on format.
	 * @param rangestring
	 */
	public MultiItemRange(String rangestring)
	{

	}
	
	public MultiItemRange()
	{
	}
	
	public MultiItemRange add(ItemRange range)
	{
		return null;
	}
	
	public MultiItemRange add(Collection<?> ranges)
	{				
		
		return this;
	}
	
	public MultiItemRange add(MultiItemRange range)
	{
		return add(range.ranges);
	}
	
	public MultiItemRange add(int itemID)
	{
		return add(new ItemRange(itemID));
	}
	
	public MultiItemRange add(int itemID, int damageStart, int damageEnd)
	{
		return add(new ItemRange(itemID, damageStart, damageEnd));
	}
	
	public MultiItemRange add(int itemIDFirst, int itemIDLast)
	{
		return add(new ItemRange(itemIDFirst, itemIDLast));
	}	

	public MultiItemRange add(Item item, int damageStart, int damageEnd)
	{
		return add(item.shiftedIndex, damageStart, damageEnd);
	}
	
	public MultiItemRange add(Block block, int damageStart, int damageEnd)
	{
		return add(block.blockID, damageStart, damageEnd);
	}
	
	public MultiItemRange add(Item item)
	{
		return add(item.shiftedIndex);
	}
	
	public MultiItemRange add(Block block)
	{
		return add(block.blockID);
	}
	
	public MultiItemRange add(ItemStack item)
	{
		if(item.getItem().isDamageable())
		{
			return add(item.itemID);
		}
		else
		{
			return add(item.itemID, item.getItemDamage(), item.getItemDamage());
		}
	}
	
	public int getNumSlots()
	{
		return 0;
	}
	
	public void slotClicked(int slot, int button, boolean doubleclick)
	{
	}
	
	public void hideAllItems()
	{
	}
	
	public void showAllItems()
	{
	}
	
	public int getWidth()
	{
		return 0;
	}
	
	public void resetHashes()
	{
	}

	
	public void addItemIfInRange(int item, int damage, NBTTagCompound compound)
	{

	}
	
	public ArrayList<ItemRange> ranges;
	public byte state;
    
    protected int lastslotclicked = -1;
    protected long lastslotclicktime;
}
