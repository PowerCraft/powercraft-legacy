package powercraft.weasel.obj;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import powercraft.weasel.engine.IVariableProvider;
import powercraft.weasel.exception.WeaselRuntimeException;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


/**
 * List of variables in the WeaselVM<br>
 * Variable list can be put into stack when CALL is executed,<br>
 * and it can also be written into NBT.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselVariableMap extends WeaselObject implements IVariableProvider {

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
	public void setVariable(String name, WeaselObject value) {

		if (name == null) throw new WeaselRuntimeException("Variable name cannot be null at " + name + " = " + value);
		if (value == null) throw new WeaselRuntimeException("Variable value cannot be null at " + name + " = " + value);

		if (map.get(name) != null) {
			map.get(name).set(value);
		} else {
			map.put(name, value);
		}

//		if (map.get(name) != null) {
//			if (map.get(name).getType() != set.getType()) {
//				throw new RuntimeException("VARMAP Set - trying to store " + set.getType() + " object into a " + map.get(name).getType() + " variable.");
//			}
//		}

	}

	@Override
	public WeaselObject getVariable(String name) {
		return map.get(name);
	}

	/**
	 * Set variable contents, regardless of previous variable data type
	 * @param name variable name
	 * @param value value to set
	 */
	public void setVariableForce(String name, WeaselObject value) {
		map.put(name, value);
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
		return "VariableMap{" + map + "}";
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

	@Override
	public List<String> getProvidedVariableNames() {
		// This should never be called, it is only to satisfy the interface.
		return (List<String>) map.keySet();
	}


}
