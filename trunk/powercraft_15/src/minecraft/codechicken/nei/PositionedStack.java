package codechicken.nei;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.item.ItemStack;

/**
 * Simply an {@link ItemStack} with position.
 * Mainly used in the recipe handlers.
 */
public class PositionedStack
{	
	/**
	 * 
	 * @param object an ItemStack or ItemStack[]
	 * @param x
	 * @param y
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public PositionedStack(Object object, int x, int y)
	{
		if(object instanceof ItemStack)
		{
			this.items = new ItemStack[]{(ItemStack) object};
		}
		else if(object instanceof ItemStack[])
		{
			this.items = (ItemStack[])object;
		}
		else if(object instanceof List)
		{
			this.items = (ItemStack[]) ((List) object).toArray(new ItemStack[0]);
		}
		else
		{
			FMLCommonHandler.instance().raiseException(new ClassCastException("not an ItemStack or ItemStack[]"), "NEI", false);
		}
		generatePermutations();
		if(items.length == 0)
		{
			System.out.println("No items in recipe");
		}
		setPermutationToRender(0);
		this.relx = x;
		this.rely = y;
	}

	private void generatePermutations()
	{
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for(ItemStack item : items)
		{
			ItemStack permutation = item.copy();
			stacks.add(permutation);
			if(item.getItemDamage() == -1)
			{
			    permutation.setItemDamage(0);
				for(int damage = 1; damage <= 15; damage++)
				{
					permutation = item.copy();
					permutation.setItemDamage(damage);
					
					if(NEIClientUtils.isValidItem(permutation))
						stacks.add(permutation);
				}
			}
		}
		items = stacks.toArray(new ItemStack[0]);
	}

	public void setMaxSize(int i)
	{
		for(ItemStack item : items)
		{
			if(item.stackSize > i)
			{
				item.stackSize = i;
			}
		}
	}
	
	public PositionedStack copy()
	{
		return new PositionedStack(items, relx, rely);
	}
	
	public void setPermutationToRender(int index)
	{
		item = items[index].copy();
		if(item.getItemDamage() == -1)
			item.setItemDamage(0);
	}
	
	public int relx;
	public int rely;
	public ItemStack items[];
	//compatibility dummy
	public ItemStack item;
}
