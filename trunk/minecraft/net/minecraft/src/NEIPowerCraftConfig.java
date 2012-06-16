package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import codechicken.nei.API;
import codechicken.nei.IConfigureNEI;
import codechicken.nei.MultiItemRange;

/**
 * Compatibility plugin for NotEnoughItems.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class NEIPowerCraftConfig implements IConfigureNEI {
	@Override
	public void loadConfig() {
		
		PC_Logger.fine("Loading NEI configuration.");

		// hide items
		for (Integer id : PC_InveditManager.hidden) {
			API.hideItem(id);
			PC_Logger.finer("Hiding item ID = "+id);
		}


		// register damage ranges
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (Integer id : PC_InveditManager.damageRanges.keySet()) {
			list.clear();
			Integer[] range = PC_InveditManager.damageRanges.get(id);

			if (range.length != 2) {
				PC_Logger.severe("Invedit Manager: Invalid damage range for item with ID=" + id);
				continue;
			}

			list.add(new int[] { range[0], range[1] });
			API.setItemDamageVariants(id, list);
			PC_Logger.finer("Setting item damage variants ID = "+id+", damage range="+range[0]+" - "+range[1]);
		}


		// register damage lists
		for (Integer id : PC_InveditManager.damageLists.keySet()) {

			Integer[] damageList = PC_InveditManager.damageLists.get(id);

			Collection<Integer> col = Arrays.asList(damageList);

			if (col == null || col.size() == 0) {
				PC_Logger.severe("Invedit Manager: Invalid damage list for item with ID=" + id);
				continue;
			}

			API.setItemDamageVariants(id, col);
			PC_Logger.finer("Setting item damage variants ID = "+id+", collection:");
			for(Integer dmg : col){
				PC_Logger.finest(""+dmg);
			}
		}


		// register categories
		HashMap<String, HashSet<Integer>> categoryLists = new HashMap<String, HashSet<Integer>>();
		for (Integer id : PC_InveditManager.categories.keySet()) {
			String cat = PC_InveditManager.categories.get(id);

			if (!categoryLists.containsKey(cat)) {
				categoryLists.put(cat, new HashSet<Integer>());
			}
			
			categoryLists.get(cat).add(id);
			PC_Logger.finest("Adding ID = "+id+" to category "+cat);
		}

		for (String catname : categoryLists.keySet()) {
			HashSet<Integer> set = categoryLists.get(catname);

			if (set != null && set.size() > 0) {
				MultiItemRange range = new MultiItemRange();
				for (Integer id : set) {
					range.add(id);
				}
				API.addSetRange("PowerCraft." + catname, range);
			}
		}


	}

}
