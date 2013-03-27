package powercraft.api.hacks;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.BaseMod;

public class PC_ModInfo {

	public static List<PC_ModInfo> modInfos = new ArrayList<PC_ModInfo>();
	
	public String name;
	public String version;
	public String logoFile;
	public List<String> authors;
	public String credits;
	public String link;
	public String description;
	
	public PC_ModInfo() {
		modInfos.add(this);
	}
	
	public PC_ModInfo(BaseMod baseMod){
		name = baseMod.getName();
		version = baseMod.getVersion();
		logoFile = (String)callMethod(baseMod, "getLogoFile", new Class<?>[0], new Object[0]);
		authors = (List<String>)callMethod(baseMod, "getAuthors", new Class<?>[0], new Object[0]);
		credits = (String)callMethod(baseMod, "getCredits", new Class<?>[0], new Object[0]);
		link = (String)callMethod(baseMod, "getLink", new Class<?>[0], new Object[0]);
		description = (String)callMethod(baseMod, "getDescription", new Class<?>[0], new Object[0]);
		modInfos.add(this);
	}
	
	private static Object callMethod(BaseMod baseMod, String name, Class<?>[] classes, Object[] objects) {
		Class<?> c = baseMod.getClass();
		
		while(c!=null){
			
			Method m = null;
			try {
				m = c.getMethod(name, classes);
				m.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if(m!=null){
				try {
					return m.invoke(baseMod, objects);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return null;
			}
			
			c=c.getSuperclass();
		}
		
		return null;
		
	}
	
}
