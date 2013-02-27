package powercraft.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import powercraft.management.item.PC_ItemStack;

public class PC_OreDictionary {

	private static HashMap<String, List<PC_ItemStack>> ores = new HashMap<String, List<PC_ItemStack>>();
	
	public static List<PC_ItemStack> getOres(String name){
        return ores.get(name);
    }
    
    public static String[] getOreNames(){
        return ores.keySet().toArray(new String[ores.keySet().size()]);
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
