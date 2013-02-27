package powercraft.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.ItemStack;

import powercraft.management.item.PC_ItemStack;

public class PC_OreDictionary {

	private static HashMap<String, List<PC_ItemStack>> ores = new HashMap<String, List<PC_ItemStack>>();
	
	public static List<PC_ItemStack> getOres(String name){
        return ores.get(name);
    }
    
    public static String[] getOreNames(){
        return ores.keySet().toArray(new String[ores.keySet().size()]);
    }
	
    public static String getOreName(ItemStack is){
    	for(Entry<String, List<PC_ItemStack>> e:ores.entrySet()){
    		for(PC_ItemStack itemStack:e.getValue()){
    			if(itemStack.equals(is)){
    				return e.getKey();
    			}
    		}
    	}
    	return null;
    }
    
    public static void register(String name, PC_ItemStack ore){
    	List<PC_ItemStack> l;
    	if(ores.containsKey(name)){
    		l = ores.get(name);
    	}else{
    		ores.put(name, l = new ArrayList<PC_ItemStack>());
    	}
    	l.add(ore);
    }
    
    
    
}
