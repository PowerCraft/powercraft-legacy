package powercraft.weasel.obj;


import powercraft.weasel.engine.Calc;
import net.minecraft.nbt.NBTTagCompound;


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
		this.bool = Calc.toBoolean(obj);
	}

	@Override
	public WeaselBoolean readFromNBT(NBTTagCompound tag) {
		bool = tag.getBoolean("b");
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
		return (bool ? "TRUE" : "FALSE");
	}

	@Override
	public WeaselBoolean copy() {
		return new WeaselBoolean(bool);
	}

}
