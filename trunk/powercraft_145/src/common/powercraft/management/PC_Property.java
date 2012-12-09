package powercraft.management;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class PC_Property {

	private Object value = null;
	private String comment[] = null;
	
	private PC_Property(LineNumberReader lnr, String[] thisComment){
		comment = thisComment;
		HashMap<String, PC_Property> hm = new HashMap<String, PC_Property>();
		value = hm;
		try {
			String line = lnr.readLine();
			String key=null;
			List<String> comment = new ArrayList<String>();
			while(line!=null){
				line = line.trim();
				if(line.startsWith("#")){
					comment.add(line.substring(1));
				}else if(line.endsWith("}")){
					break;
				}else if(line.endsWith("{") && key!=null){
					hm.put(key, new PC_Property(lnr, comment.toArray(new String[0])));
					key=null;
					comment.clear();
				}else if(!line.equals("")){
					int peq = line.indexOf('=');
					if(peq==-1){
						peq = line.indexOf('{');
						if(peq>0){
							key = line.substring(0, peq).trim();
							hm.put(key, new PC_Property(lnr, comment.toArray(new String[0])));
							key=null;
							comment.clear();
						}else{
							key = line;
						}
					}else if(peq>0){
						key = line.substring(0, peq).trim();
						String value = line.substring(peq+1).trim();
						hm.put(key, new PC_Property(value, comment.toArray(new String[0])));
						key=null;
						comment.clear();
					}
				}
				
				line = lnr.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private PC_Property(String s, String...desc){
		comment = desc;
		if(s.startsWith("\"") && s.endsWith("\"")){
			value = s.substring(1, s.length()-1);
		}else if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("ok")){
			value = true;
		}else if(s.equalsIgnoreCase("false")){
			value = false;
		}else{
			try{
				if(s.contains(".")){
					value = Float.parseFloat(s);
				}else{
					value = Integer.parseInt(s);
				}
			}catch(NumberFormatException e){
				value = s;
			}
		}
	}
	
	public PC_Property(Object value, String...desc){
		if(value==null)
			value = new HashMap<String, PC_Property>();
		this.value = value;
		comment = desc;
	}
	
	public String getString(){
		return value.toString();
	}
	
	public int getInt(){
		if(value instanceof Number){
			return ((Number) value).intValue();
		}
		return 0;
	}
	
	public float getFloat(){
		if(value instanceof Number){
			return ((Number) value).floatValue();
		}
		return 0.0f;
	}
	
	public boolean getBoolean() {
		if(value instanceof Boolean){
			return (Boolean) value;
		}
		return false;
	}
	
	public HashMap<String, PC_Property> getPropertys(){
		if(value instanceof HashMap){
			return (HashMap<String, PC_Property>)value;
		}
		return null;
	}
	
	public boolean hasChildren(){
		return value instanceof HashMap;
	}
	
	public void put(String key, PC_Property prop){
		if(hasChildren()){
			getPropertys().put(key, prop);
		}
	}
	
	private void save(OutputStreamWriter osw, String tabs){
		if(hasChildren()){
			HashMap<String, PC_Property> hm = (HashMap<String, PC_Property>)value;
			String[] keys = hm.keySet().toArray(new String[0]);
			Arrays.sort(keys);
			for(String key:keys){
				try {
					PC_Property prop = hm.get(key);
					String[] comment = prop.comment;
					if(comment!=null && comment.length>0){
						for(String c:comment){
							osw.write(tabs + "#"+c+"\n");
						}
					}
					if(prop.hasChildren()){
						osw.write(tabs + key+"{\n");
						prop.save(osw, tabs+"\t");
						osw.write(tabs + "}\n");
					}else{
						if(prop.value instanceof String){
							osw.write(tabs + key+"=\""+(String)prop.value+"\"\n");
						}else{
							osw.write(tabs + key+"="+prop.value.toString()+"\n");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public void save(OutputStream os){
		try {
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			save(osw, "");
			osw.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PC_Property loadFromFile(InputStream is){
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
		PC_Property prop = new PC_Property(lnr, null);
		try {
			lnr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public String[] getComment() {
		return comment;
	}

	public void setValue(Object o, String[] comment) {
		value = o;
		this.comment = comment;
	}

	public void replaceWith(PC_Property prop) {
		if(hasChildren() && prop.hasChildren()){
			HashMap<String, PC_Property> hm = prop.getPropertys();
			HashMap<String, PC_Property> thm = getPropertys();
			for(Entry<String, PC_Property> e:hm.entrySet()){
				if(thm.containsKey(e.getKey())){
					thm.get(e.getKey()).replaceWith(e.getValue());
				}else{
					thm.put(e.getKey(), e.getValue());
				}
			}
		}else{
			value = prop.value;
			comment = prop.comment;
		}
	}
	
}
