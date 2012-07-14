package weasel.obj;


import weasel.Calc;
import net.minecraft.src.NBTTagCompound;


/**
 * Integer Object
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselInteger extends WeaselObject {

	/** the integer */
	public int integer = 0;

	/**
	 * Integer object
	 * 
	 * @param integer integer value
	 */
	public WeaselInteger(int integer) {
		super(WeaselObjectType.INTEGER);
		this.integer = integer;
	}

	/**
	 * Create integer of any type (almost)
	 * 
	 * @param obj
	 */
	public WeaselInteger(Object obj) {
		super(WeaselObjectType.INTEGER);
		set(obj);
	}

	/**
	 * Integer object
	 * 
	 * @param number number representation
	 * @param radix radix 2,8,16
	 */
	public WeaselInteger(String number, int radix) {
		super(WeaselObjectType.INTEGER);
		this.integer = Integer.parseInt(number.substring(2), radix);
	}

	/**
	 * Integer object, 0
	 */
	public WeaselInteger() {
		super(WeaselObjectType.INTEGER);
	}

	@Override
	public Integer get() {
		return integer;
	}

	@Override
	public void set(Object obj) {		
		this.integer = Calc.toInteger(obj);		
	}

	@Override
	public WeaselInteger readFromNBT(NBTTagCompound tag) {
		integer = tag.getInteger("i");
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("i", integer);
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

		return ((WeaselInteger) obj).integer == integer;
	}


	@Override
	public int hashCode() {
		return integer;
	}

	@Override
	public String toString() {
		return integer + "";
	}

	@Override
	public WeaselInteger copy() {
		return new WeaselInteger(integer);
	}
}
