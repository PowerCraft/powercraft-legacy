package net.minecraft.src.weasel.obj;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.weasel.IVariableContainer;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;


/**
 * List of variables in the WeaselVM<br>
 * Variable list can be put into stack when CALL is executed,<br>
 * and it can also be written into NBT.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselVariableMap extends WeaselObject implements IVariableContainer {

	/** Variable map */
	public LinkedHashMap<String, WeaselObject> map;	

	/**
	 * List of weasel variables
	 */
	public WeaselVariableMap() {
		super(WeaselObjectType.VARMAP);
		map = new LinkedHashMap<String, WeaselObject>();
	}

	/**
	 * clear the map.
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * Unset a variable
	 * 
	 * @param name variable name
	 */
	public void unsetVariable(String name) {
		map.remove(name);
	}

	@Override
	public void setVariable(String name, Object value) {
		
		if (name == null) throw new WeaselRuntimeException("VARMAP Set - variable name == null. @ " + name + " = " + value);
		if (value == null) throw new WeaselRuntimeException("VARMAP Set - variable value == null. @ " + name + " = " + value);

		WeaselObject set = null;
		if (value instanceof WeaselObject) {
			set = (WeaselObject) value;
		} else if (value instanceof Number) {
			set = new WeaselInteger(value);
		} else if (value instanceof String) {
			set = new WeaselString(value);
		} else if (value instanceof Boolean) {
			set = new WeaselBoolean(value);
		} else {
			throw new WeaselRuntimeException("VARMAP Set - value " + value + " cannot be saved as a WeaselObject to variable map.");
		}

		if (map.get(name) != null) {
			if (map.get(name).getType() != set.getType()) {
				throw new RuntimeException("VARMAP Set - trying to store " + set.getType() + " object into a " + map.get(name).getType() + " variable.");
			}
		}

		map.put(name, set);
	}

	@Override
	public WeaselObject getVariable(String name) {
		return map.get(name);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		NBTTagList tags = new NBTTagList();
		for (Entry<String, WeaselObject> entry : map.entrySet()) {
			NBTTagCompound tag1 = WeaselObject.saveObjectToNBT(entry.getValue(), new NBTTagCompound());
			tag1.setString("VariableName", entry.getKey());
			tags.appendTag(tag1);
		}
		tag.setTag("VariableMap", tags);

		return tag;

	}

	@Override
	public WeaselVariableMap readFromNBT(NBTTagCompound tag) {

		NBTTagList tags = tag.getTagList("VariableMap");
		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			map.put(tag1.getString("VariableName"), WeaselObject.loadObjectFromNBT(tag1));
		}

		return this;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		return ((WeaselVariableMap) obj).map == map;
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public String toString() {
		return "VARMAP(" + map + ")";
	}

	@Override
	public Map<String, WeaselObject> get() {
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Object obj) {
		if (obj == null || !(obj instanceof Map)) {
			throw new RuntimeException("Trying to store " + obj + " in a VariableMap variable.");
		}
		map = (LinkedHashMap<String, WeaselObject>) obj;
	}

	@Override
	public WeaselObject copy() {
		WeaselVariableMap map2 = new WeaselVariableMap();
		map2.set(map.clone());
		return map2;
	}


}
