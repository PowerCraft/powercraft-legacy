package net.minecraft.src.weasel;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.PC_INBT;

/**
 * List of variables in the WeaselVM<br>
 * Variable list can be put into stack when CALL is executed,<br>
 * and it can also be written into NBT.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class WeaselVariableMap extends WeaselObject {
	
	private LinkedHashMap<String, WeaselObject> variableMap;
	
	/**
	 * clear the map.
	 */
	public void clear(){
		variableMap.clear();
	}
	
	/**
	 * Unset a variable
	 * @param name variable name
	 */
	public void unset(String name){
		variableMap.remove(name);
	}
	
	/**
	 * Store variable into map
	 * @param name variable name
	 * @param object variable object to store
	 */
	public void set(String name, WeaselObject object){
		
		if(variableMap.get(name) != null){
			if(variableMap.get(name).getType() != object.getType()){
				throw new RuntimeException("Trying to store "+object.getType()+" object into a "+variableMap.get(name).getType()+" variable.");
			}
		}
		
		variableMap.put(name, object);
	}
	
	/**
	 * Get variable from map
	 * @param name variable name
	 * @return variable object
	 */
	public WeaselObject get(String name){
		return variableMap.get(name);
	}

	/**
	 * List of weasel variables
	 */
	public WeaselVariableMap() {
		super(WeaselObjectType.VARIABLE_LIST);
		variableMap = new LinkedHashMap<String, WeaselObject>();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		
		NBTTagList tags = new NBTTagList();
		for(Entry<String,WeaselObject> entry: variableMap.entrySet()){
			NBTTagCompound tag1 = entry.getValue().writeToNBT(new NBTTagCompound());
			tag1.setString("VariableName", entry.getKey());
			tags.appendTag(tag1);
		}
		tag.setTag("VariableMap", tags);
		
		return tag;
		
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		
		NBTTagList tags = tag.getTagList("VariableMap");
		for(int i=0; i<tags.tagCount(); i++){
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			variableMap.put(tag1.getString("VariableName"), WeaselObject.loadObjectFromNBT(tag1));
		}
		
		return this;
		
	}	
	

}
