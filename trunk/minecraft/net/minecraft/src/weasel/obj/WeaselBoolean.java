package net.minecraft.src.weasel.obj;


import net.minecraft.src.NBTTagCompound;


/**
 * Boolean object
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselBoolean extends WeaselObject {

	/** the boolean */
	public boolean bool = false;

	/**
	 * @param bool boolean state
	 */
	public WeaselBoolean(boolean bool) {
		super(WeaselObjectType.BOOLEAN);
		this.bool = bool;
	}

	/**
	 * Create boolean of any type (almost)
	 * 
	 * @param obj
	 */
	public WeaselBoolean(Object obj) {
		super(WeaselObjectType.BOOLEAN);
		set(obj);
	}

	/**
	 * Boolean object, false
	 */
	public WeaselBoolean() {
		super(WeaselObjectType.BOOLEAN);
	}

	@Override
	public Boolean get() {
		return bool;
	}

	@Override
	public void set(Object obj) {

		if (obj instanceof WeaselInteger) {
			this.bool = ((WeaselInteger) obj).get() != 0;
			return;
		}

		if (obj instanceof Double) {
			this.bool = (int) Math.round((Double) obj) != 0;
			return;
		}

		if (obj instanceof Float) {
			this.bool = Math.round((Float) obj) != 0;
			return;
		}

		if (obj instanceof Long) {
			this.bool = ((Long) obj) != 0;
			return;
		}
		if (obj instanceof WeaselBoolean) {
			this.bool = ((WeaselBoolean) obj).get();
			return;
		}

		if (obj instanceof Integer) {
			this.bool = ((Integer) obj) != 0;
			return;
		}

		if (obj == null || !(obj instanceof Boolean)) {
			throw new RuntimeException("Trying to store " + obj + " in a boolean variable.");
		}
		this.bool = (Boolean) obj;
	}

	@Override
	public WeaselBoolean readFromNBT(NBTTagCompound tag) {
		tag.setBoolean("b", bool);
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setBoolean("b", bool);
		return tag;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		return ((WeaselBoolean) obj).bool == bool;
	}


	@Override
	public int hashCode() {
		return bool ? 1 : 0;
	}

	@Override
	public String toString() {
		return "B(" + (bool ? "TRUE" : "FALSE") + ")";
	}

	@Override
	public WeaselObject copy() {
		return new WeaselBoolean(bool);
	}

}
