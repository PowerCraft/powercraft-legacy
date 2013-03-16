package powercraft.weasel.obj;


import powercraft.weasel.engine.Calc;
import net.minecraft.nbt.NBTTagCompound;


/**
 * Integer Object
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselDouble extends WeaselObject {

	/** the double */
	public double value = 0;

	/**
	 * double object
	 * 
	 * @param doubl double value
	 */
	public WeaselDouble(int doubl) {
		super(WeaselObjectType.DOUBLE);
		this.value = doubl;
	}

	/**
	 * Create double of any type (almost)
	 * 
	 * @param obj
	 */
	public WeaselDouble(Object obj) {
		super(WeaselObjectType.DOUBLE);
		set(obj);
	}

	/**
	 * double object
	 * 
	 * @param number number representation
	 * @param radix radix 2,8,16
	 */
	public WeaselDouble(String number, int radix) {
		super(WeaselObjectType.DOUBLE);
		this.value = Integer.parseInt(number.substring(2), radix);
	}

	/**
	 * double object, 0
	 */
	public WeaselDouble() {
		super(WeaselObjectType.DOUBLE);
	}

	@Override
	public Double get() {
		return value;
	}

	public Integer getI(){
		return (int)(long)Math.round(value);
	}
	
	@Override
	public final void set(Object obj) {
		this.value = Calc.toDouble(obj);
	}

	@Override
	public WeaselDouble readFromNBT(NBTTagCompound tag) {
		value = tag.getDouble("i");
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setDouble("i", value);
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

		return ((WeaselDouble) obj).value == value;
	}


	@Override
	public int hashCode() {
		return ((Double)value).hashCode();
	}

	@Override
	public String toString() {
		return value + "";
	}

	@Override
	public WeaselDouble copy() {
		return new WeaselDouble(value);
	}
}
