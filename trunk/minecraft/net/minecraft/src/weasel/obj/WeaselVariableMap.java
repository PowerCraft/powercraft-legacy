package net.minecraft.src.weasel.obj;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

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

	/** Variable map */
	public LinkedHashMap<String, WeaselObject> map;

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
	public void unset(String name) {
		map.remove(name);
	}

	/**
	 * Store variable into map
	 * 
	 * @param name variable name
	 * @param object variable object to store
	 */
	public void set(String name, WeaselObject object) {

		if (map.get(name) != null) {
			if (map.get(name).getType() != object.getType()) { throw new RuntimeException("Trying to store " + object.getType()
					+ " object into a " + map.get(name).getType() + " variable."); }
		}

		map.put(name, object);
	}

	/**
	 * Get variable from map
	 * 
	 * @param name variable name
	 * @return variable object
	 */
	public WeaselObject get(String name) {
		return map.get(name);
	}

	/**
	 * List of weasel variables
	 */
	public WeaselVariableMap() {
		super(WeaselObjectType.VARIABLE_LIST);
		map = new LinkedHashMap<String, WeaselObject>();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		NBTTagList tags = new NBTTagList();
		for (Entry<String, WeaselObject> entry : map.entrySet()) {
			NBTTagCompound tag1 = entry.getValue().writeToNBT(new NBTTagCompound());
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
		if (obj == null) { return false; }
		if (!this.getClass().equals(obj.getClass())) { return false; }

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
	public Object get() {
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Object obj) {
		if (obj == null || !(obj instanceof Map)) { throw new RuntimeException("Trying to store " + obj + " in a VariableMap variable."); }
		map = (LinkedHashMap<String, WeaselObject>) obj;
	}


}
