package net.minecraft.src.weasel.obj;

import net.minecraft.src.NBTTagCompound;

/**
 * String object
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class WeaselString extends WeaselObject {

	/** the string */
	public String string = "";

	/**
	 * String object, ""
	 */
	public WeaselString() {
		super(WeaselObjectType.STRING);
	}

	/**
	 * String object
	 * 
	 * @param string the string
	 */
	public WeaselString(String string) {
		super(WeaselObjectType.STRING);
		this.string = string;
	}
	
	/**
	 * Create string of any type (almost)
	 * @param obj
	 */
	public WeaselString(Object obj) {
		super(WeaselObjectType.INTEGER);
		set(obj);
	}

	@Override
	public String get() {
		return string;
	}

	@Override
	public void set(Object obj) {
		if (obj instanceof WeaselString) {
			this.string = new String(((WeaselString) obj).get());
			return;
		}

		if (obj == null || !(obj instanceof String)) { throw new RuntimeException("Trying to store " + obj + " in a string variable."); }
		this.string = (String) obj;
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("s", string);
		return tag;
	}

	@Override
	public WeaselString readFromNBT(NBTTagCompound tag) {
		string = tag.getString("s");
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (!this.getClass().equals(obj.getClass())) { return false; }

		return ((WeaselString) obj).string == string;
	}


	@Override
	public int hashCode() {
		return string.hashCode();
	}

	@Override
	public String toString() {
		return "S(" + string + ")";
	}

}
