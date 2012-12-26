package powercraft.management;

import java.util.HashMap;
import java.util.Map.Entry;

public class PC_ConfigConverter {

	public static PC_Property convert(PC_Property config, String module) {
		PC_Property prop = new PC_Property();
		HashMap<String, PC_Property> hm = config.getPropertys();
		if(hm.containsKey("block")){
			PC_Property blockProp = hm.get("block");
			HashMap<String, PC_Property> blocks = blockProp.getPropertys();
			for(Entry<String, PC_Property> e:blocks.entrySet()){
				try {
					Class c = Class.forName(e.getKey());
					if(c.isAnnotationPresent(PC_Shining.class)){
						prop.setInt(c.getSimpleName()+".defaultID.on", e.getValue().getInt());
						prop.setInt(c.getSimpleName()+".defaultID.off", e.getValue().getInt()+1);
					}else{
						prop.setInt(c.getSimpleName()+".defaultID", e.getValue().getInt());
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}else{
			return config;
		}
		if(hm.containsKey("item")){
			PC_Property blockProp = hm.get("item");
			HashMap<String, PC_Property> blocks = blockProp.getPropertys();
			for(Entry<String, PC_Property> e:blocks.entrySet()){
				prop.setInt(e.getKey().substring(("powercraft."+module+".").length())+".defaultID", e.getValue().getInt());
			}
		}
		return prop;
	}

}
