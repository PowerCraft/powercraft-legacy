package powercraft.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.reflect.PC_ReflectHelper;

public class PC_OreDictionary {

	private static HashMap<String, List<PC_ItemStack>> ores = new HashMap<String, List<PC_ItemStack>>();
	
	public static List<PC_ItemStack> getOres(String name){
        List<ItemStack> l = OreDictionary.getOres(name);
        List<PC_ItemStack> ret = new ArrayList<PC_ItemStack>();
        for(ItemStack is:l){
        	ret.add(new PC_ItemStack(is));
        }
        return ret;
    }
    
    public static String[] getOreNames(){
        return OreDictionary.getOreNames();
    }
	
    public static String getOreName(ItemStack is){
    	String name = OreDictionary.getOreName(OreDictionary.getOreID(is));
    	return name.equals("Unknown")?null:name;
    }
    
    public static void register(String name, PC_ItemStack ore){
    	List<PC_ItemStack> l;
    	if(ores.containsKey(name)){
    		l = ores.get(name);
    	}else{
    		ores.put(name, l = new ArrayList<PC_ItemStack>());
    	}
    	l.add(ore);
    	OreDictionary.registerOre(name, ore.toItemStack());
    }

	public static void unloadOres() {
		HashMap<Integer, ArrayList<ItemStack>> hm = PC_ReflectHelper.getValue(OreDictionary.class, OreDictionary.class, 3, HashMap.class);
		for(Entry<String, List<PC_ItemStack>> e:ores.entrySet()){
			ArrayList<ItemStack> list = hm.get(OreDictionary.getOreID(e.getKey()));
			Iterator<ItemStack> i = list.iterator();
			while(i.hasNext()){
				ItemStack is = i.next();
				for(PC_ItemStack pcis:e.getValue()){
					if(pcis.equals(is)){
						i.remove();
						break;
					}
				}
			}
		}
	}

	public static void loadOres() {
		HashMap<Integer, ArrayList<ItemStack>> hm = PC_ReflectHelper.getValue(OreDictionary.class, OreDictionary.class, 3, HashMap.class);
		for(Entry<String, List<PC_ItemStack>> e:ores.entrySet()){
			ArrayList<ItemStack> list = hm.get(OreDictionary.getOreID(e.getKey()));
			for(PC_ItemStack pcis:e.getValue()){
				list.add(pcis.toItemStack());
			}
		}
	}
    
}
