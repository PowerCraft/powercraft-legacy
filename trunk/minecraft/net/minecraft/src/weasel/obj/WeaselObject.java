package net.minecraft.src.weasel.obj;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;


/**
 * Weasel data object.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class WeaselObject implements PC_INBT {

	private WeaselObjectType type = null;


	/**
	 * @param type object type
	 */
	public WeaselObject(WeaselObjectType type) {
		this.type = type;
	}

	/**
	 * @return object type
	 */
	public final WeaselObjectType getType() {
		return type;
	}
	
	public abstract WeaselObject copy();
	
	/**
	 * Save an object to {@link NBTTagCompound}, together with it's type (needed for loading).
	 * 
	 * @param object object to save
	 * @param tag tag to save into
	 * @return the tag
	 */
	public static final NBTTagCompound saveObjectToNBT(WeaselObject object, NBTTagCompound tag) {
		tag.setInteger("type", object.getType().index);
		object.writeToNBT(tag);
		return tag;
	}

	/**
	 * Load an object from Compound NBT tag, using the proper WeaselObject
	 * subtype.
	 * 
	 * @param tag the tag with object
	 * @return the object
	 */
	public static final WeaselObject loadObjectFromNBT(NBTTagCompound tag) {
		WeaselObject obj = null;

		switch (WeaselObjectType.getTypeFromIndex(tag.getInteger("type"))) {

			case BOOLEAN:
				obj = new WeaselBoolean();
				obj.readFromNBT(tag);
				break;

			case INTEGER:
				obj = new WeaselInteger();
				obj.readFromNBT(tag);
				break;

			case STRING:
				obj = new WeaselString();
				obj.readFromNBT(tag);
				break;

			case VARIABLE_LIST:
				obj = new WeaselVariableMap();
				obj.readFromNBT(tag);
				break;

			case STACK:
				obj = new WeaselStack();
				obj.readFromNBT(tag);
				break;

		}

		return obj;
	}

	/**
	 * @return the wrapped object
	 */
	public abstract Object get();

	/**
	 * Set the wrapped object, replace current one
	 * 
	 * @param obj
	 */
	public abstract void set(Object obj);

	@Override
	public abstract String toString();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();
}
