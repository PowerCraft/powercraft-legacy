package codechicken.nei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;


import net.minecraft.src.GuiContainer;
import net.minecraft.src.ItemStack;

/**
 * This is the main class that handles item property configuration.
 * WARNING: DO NOT access this class until the world has been loaded
 * These methods should be called from INEIConfig implementors
 */
public class API
{

	/**
     * Hide this item from the ItemPanel.
     * @param itemID The ItemID to hide.
     */
    public static void hideItem(int itemID)
    {
    }
    
    /**
     * Collection version of hideItem.
     * @param items A collection of ItemIDs to hide
     */
    public static void hideItems(Collection<Integer> items)
    {
    }
    
    /**
    * Add or replace the name normally shown on the item tooltip
    * @param itemID
    * @param itemDamage
    * @param name The name to set.
    */
	public static void setOverrideName(int itemID, int itemDamage, String name)
	{
	}
   
   /**
    * An advanced damage range setter, capable of handling multiple ranges. Removes the performance hit from simply searching from 0 - 32000.
    * Sets the item to have damages between the ranges specified by the pairs of ints in the int[]s
    * The int[] should have dimension of 2. The int[0] being the first damage and int[1] the last.
    * Damage ranges are inclusive. 
    * @param itemID The item to set the damage ranges for 
    * @param ranges An ArrayList of int[] pairs specifying the damage ranges.
    */
	public static void setItemDamageVariants(int itemID, ArrayList<int[]> ranges)
	{
	}
   
   /**
    * A simplified wrapper version for specific damage values. Potions, Spawn Eggs etc.
    * @param itemID
    * @param damages A list of Integers specifying the valid damage values.
    */
	public static void setItemDamageVariants(int itemID, Collection<Integer> damages)
	{
	}
	
	/**
	 * Another simplified wrapper version of setItemDamageVariants. 
	 * Simply supports searching from 0-maxDamage
	 * Use of this function is not recommended for large damage values as there is a good hit on performance.
	 * @param itemID
	 * @param maxDamage the maximum damage to search to (inclusive)
	 * Setting this to -1 will disable NEI's normal damage value based search.
	 */
    public static void setMaxDamageException(int itemID, int maxDamage)
    {
    }
    
    /**
     * Adds an item with a data compound. 
     * Use this for adding items to the panel that are different depending on their compounds not just their damages.
     * @param item an item with data
     */
	public static void addNBTItem(ItemStack item)
	{
	}
	
	/**
	 * The all important function for mods wanting to add custom Item Subset tags
	 * @param setname The name of the item range Eg. "Items.Tools.Hammers"
	 * @param range A {@link MultiItemRange} specifying the items encompassed by this tag.
	 */
	public static void addSetRange(String setname, MultiItemRange range)
	{
	}
	
	/**
	 * Add more items to an existing Item Subset rather than replacing it
	 * @param setname
	 * @param range
	 */
	public static void addToRange(String setname, MultiItemRange range)
	{
	}	
	
}
