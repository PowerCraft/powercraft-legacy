package powercraft.management;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;

public class PC_Configuration {

	private PC_Property baseProp;
	
	public PC_Configuration(){
		this(new PC_Property(null));
	}
	
	public PC_Configuration(PC_Property baseProp){
		this.baseProp = baseProp;
	}
	
	public PC_Property getProperty(String key, Object defaultValue, String[] comment){
		String[] keys = key.split("\\.");
		PC_Property prop = baseProp;
		for(int i=0; i<keys.length; i++){
			if(prop.hasChildren()){
				HashMap<String, PC_Property> hm = prop.getPropertys();
				if(hm.containsKey(keys[i])){
					prop = hm.get(keys[i]);
				}else{
					if(i==keys.length-1){
						hm.put(keys[i], prop = new PC_Property(defaultValue, comment));
					}else{
						hm.put(keys[i], prop = new PC_Property(null));
					}
				}
			}else{
				break;
			}
		}
		return prop;
	}
	
	public int getInt(String key){
		return getInt(key, 0);
	}
	
	public int getInt(String key, int defaultValue, String... comment){
		return getProperty(key, defaultValue, comment).getInt();
	}
	
	public float getFloat(String key){
		return getFloat(key, 0.0f);
	}
	
	public float getFloat(String key, float defaultValue, String... comment){
		return getProperty(key, defaultValue, comment).getFloat();
	}
	
	public boolean getBoolean(String key){
		return getBoolean(key, false);
	}
	
	public boolean getBoolean(String key, boolean defaultValue, String... comment){
		return getProperty(key, defaultValue, comment).getBoolean();
	}
	
	public String getString(String key){
		return getString(key, "");
	}
	
	public String getString(String key, String defaultValue, String... comment){
		return getProperty(key, defaultValue, comment).getString();
	}
	
	public void replaceWith(PC_Configuration conf){
		baseProp.replaceWith(conf.baseProp);
	}
	
	public static PC_Configuration load(InputStream is){
		return new PC_Configuration(PC_Property.loadFromFile(is));
	}

	public void save(OutputStream os) {
		baseProp.save(os);
	}

	public PC_Property getProperty() {
		return baseProp;
	}
	
}
