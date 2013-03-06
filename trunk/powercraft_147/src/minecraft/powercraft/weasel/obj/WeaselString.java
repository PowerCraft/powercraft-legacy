package powercraft.weasel.obj;


import powercraft.weasel.engine.Calc;
import net.minecraft.nbt.NBTTagCompound;


/**
 * String object
 * 
 * @author MightyPork
 * @copy (c) 2012
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
	 * 
	 * @param obj
	 */
	public WeaselString(Object obj) {
		super(WeaselObjectType.STRING);
		set(obj);
	}

	@Override
	public String get() {
		return string;
	}

	@Override
	public void set(Object obj) {
		this.string = Calc.toString(obj);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
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
		if (obj == null) {
			return false;
		}
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		return ((WeaselString) obj).string == string;
	}


	@Override
	public int hashCode() {
		return string.hashCode();
	}

	@Override
	public String toString() {
		return "\"" + string + "\"";
	}

	@Override
	public WeaselString copy() {
		return new WeaselString(new String(string));
	}

}
