package codechicken.nei;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.forge.GuiContainerManager;

public class ItemList
{
	public static ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	private static boolean matching = false;
	private static boolean loading = false;
	private static boolean research = false;
	private static boolean reload = false;
	
	public static class ItemMatcher
	{
		Pattern patternMatch;
		SubSetRangeTag tagMatch;
		
		public ItemMatcher(Pattern pattern, SubSetRangeTag tag)
		{
			patternMatch = pattern;
			tagMatch = tag;
		}

		public boolean matches(ItemStack item)
		{
			if(tagMatch != null)
				return tagMatch.isItemInRange(item.itemID, 0);
			else
				return patternMatch == null || patternMatch.toString().equals("") || patternMatch.matcher(GuiContainerManager.concatenatedDisplayName(item, true).toLowerCase()).find();
		}
	}
	
	public static ItemMatcher getSearchMatcher()
	{
		String matchstring = NEIClientConfig.getSearchExpression();
        SubSetRangeTag tagMatch = null;
        Pattern patternMatch = null;
        if(matchstring.startsWith("@") && matchstring.length() > 1)
        {
        	tagMatch = DropDownFile.dropDownInstance.getTag(matchstring.substring(1), false);
        }
        else
        {
        	matchstring = matchstring.replace(".", "");
        	matchstring = matchstring.replace("?", ".");
        	matchstring = matchstring.replace("*", ".+?");
        	matchstring = matchstring.toLowerCase();
        	try
        	{
        		patternMatch = Pattern.compile(matchstring);
        	}
        	catch(PatternSyntaxException e)
        	{
        		patternMatch = Pattern.compile("");
        	}
    	}
        
        return new ItemMatcher(patternMatch, tagMatch);
	}
	
	public static boolean itemMatchesSearch(ItemStack item)
	{
		return getSearchMatcher().matches(item);
	}
	
	public static class ThreadMatchSearch extends Thread
	{
		public ThreadMatchSearch()
		{
			super("NEI Item Searching Thread");
		}
		
		@Override
		public void run()
		{
			matching = true;
			startMatch:
			while(matching)
			{
				ArrayList<ItemPanelObject> visibleitems = new ArrayList<ItemPanelObject>();
				ItemMatcher matcher = getSearchMatcher();
				for(ItemStack item : items)
				{
					if(research)
					{
						research = false;
						continue startMatch;
					}
					
					if(item.hasTagCompound() ? NEIClientConfig.vishash.isItemHidden(item.itemID, item.stackTagCompound) : NEIClientConfig.vishash.isItemHidden(item.itemID, item.getItemDamage()))
						continue;
					
					if(!NEIClientConfig.canGetItem(new ItemHash(item)))
						continue;
					
					if(matcher.matches(item))
						visibleitems.add(new ItemPanelStack(item));
				}
				ItemPanel.visibleitems = visibleitems;
				matching = false;
			}
		}
	}
	
	public static class ThreadLoadItems extends Thread
	{
		public ThreadLoadItems()
		{
			super("NEI Item Loading Thread");
		}
		
		@Override
		public void run()
		{
			loading = true;
			startSearch:
			while(loading)
			{
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				ArrayList<ItemStack> sublist = new ArrayList<ItemStack>();
				
				DropDownFile.dropDownInstance.resetHashes();
				for(int itemID = 0; itemID < Item.itemsList.length; itemID++)
		        {
					if(reload)
					{
						reload = false;
						continue startSearch;
					}
					
		            Item item = Item.itemsList[itemID];
		            if(item == null || ItemInfo.isHidden(item.itemID))
		                continue;
		            
		            sublist.clear();
		            item.getSubItems(itemID, null, sublist);
		            ArrayList<int[]> damageranges = ItemInfo.getItemDamageVariants(item.itemID);
		            if(sublist.size() > 0)
		            {
			            ArrayList<Integer> discreteDamages = new ArrayList<Integer>();
		            	for(ItemStack stack : sublist)
		            	{
			            	if(stack.hasTagCompound())
			    			{
			            		stack = stack.copy();
			                    items.add(stack);              
			                    DropDownFile.dropDownInstance.addItemIfInRange(itemID, stack.getItemDamage(), stack.stackTagCompound);	   
			    			}
			    			else
			    			{
			    				discreteDamages.add(stack.getItemDamage());
			    			}
		            	}
		            	
		            	if(damageranges == ItemInfo.defaultDamageRange)
		            		damageranges = NEIClientUtils.concatIntegersToRanges(discreteDamages);
		            	else
		            		damageranges = NEIClientUtils.addIntegersToRanges(damageranges, discreteDamages);
		            }
		            
		            boolean skipDamage0 = false;
		            ArrayList<ItemStack> datalist = ItemInfo.getItemCompounds(itemID);
		            if(datalist != null && datalist.size() > 0 && NEIClientConfig.isActionPermissable("nbt"))
		            {
	                	skipDamage0 = true;
	                	
		            	for(ItemStack stack : datalist)
		                {
		            		stack = stack.copy();
		                    items.add(stack);	                    
		                    DropDownFile.dropDownInstance.addItemIfInRange(itemID, stack.getItemDamage(), stack.stackTagCompound);	                    
		                }
		            }
		            
		            HashSet<String> damageIconSet = new HashSet<String>();
		            for(int[] damagerange : damageranges)
		            {
		            	for(int damage = damagerange[0]; damage <= damagerange[1]; damage++)
		            	{
		            		ItemStack itemstack = new ItemStack(item, 1, damage);
		                    try
		                    {
		                    	int l = item.getIconIndex(itemstack);
		                    	String name = GuiContainerManager.concatenatedDisplayName(itemstack, false);
		                    	//if(name.equals("Unnamed"))
		                			//continue;
		                    	
		                        String s = name+"@"+l;
		                        if(!damageIconSet.contains(s))
		                        {
		                            damageIconSet.add(s);
		                            if(damage == 0 && skipDamage0)
		                            	continue;
		                            
			                        items.add(itemstack);
				                    DropDownFile.dropDownInstance.addItemIfInRange(itemID, itemstack.getItemDamage(), null);	 
		                        }
		                            
		                    }
		                    catch(Exception e)
		                    {}
		            	}
		            }
		        }
				loading = false;
				ItemList.items = items;
			}
	        DropDownFile.dropDownInstance.updateState();
	        updateSearch();
		}
	}
	
	public static boolean isMatching()
	{
		return matching;
	}
	
	public static void updateSearch()
	{
		if(matching)
			research = true;
		else
			new ThreadMatchSearch().start();
	}
	
	public static void loadItems()
	{
		if(loading)
			reload = true;
		else
			new ThreadLoadItems().start();
	}
}
