package weasel.obj;

import java.util.HashMap;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import weasel.exception.WeaselRuntimeException;


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

	/**
	 * Get a completely separate copy of this object.
	 * 
	 * @return the copy
	 */
	public abstract WeaselObject copy();

	/**
	 * Save an object to {@link NBTTagCompound}, together with it's type (needed
	 * for loading).
	 * 
	 * @param object object to save
	 * @param tag tag to save into
	 * @return the tag
	 */
	public static final NBTTagCompound saveObjectToNBT(WeaselObject object, NBTTagCompound tag) {
		tag.setString("type", object.getType().toString());
		object.writeToNBT(tag);
		return tag;
	}
	
	private static int cnt = 0;

	/**
	 * Load an object from Compound NBT tag, using the proper WeaselObject
	 * subtype.
	 * 
	 * @param tag the tag with object
	 * @return the object
	 */
	public static final WeaselObject loadObjectFromNBT(NBTTagCompound tag) {
		WeaselObject obj = null;

		switch (WeaselObjectType.getTypeFromName(tag.getString("type"))) {

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

			case VARMAP:
				obj = new WeaselVariableMap();
				obj.readFromNBT(tag);
				break;

			case STACK:
				obj = new WeaselStack();
				obj.readFromNBT(tag);
				break;

			case NULL:
				obj = new WeaselNull();
				break;
		}

		return obj;
	}

	/**
	 * Create an adequate wrapper for given value.
	 * 
	 * @param value the value object (primitive)
	 * @return the WeaselObject representing given value. If value already was a
	 *         weasel object, returns it unchanged.
	 */
	public static WeaselObject getWrapperForValue(Object value) {
		
		if(value == null) return null;
		
		if (value instanceof Number) {
			return new WeaselInteger(value);
		}

		if (value instanceof String) {
			return new WeaselString(value);
		}

		if (value instanceof Boolean) {
			return new WeaselBoolean(value);
		}

		if (value instanceof WeaselObject) return (WeaselObject) value;

		throw new WeaselRuntimeException("WOBJ - can not create a wrapper for " + value + ", (" + value.getClass().getSimpleName() + ")");

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

	/**
	 * Type of a weasel object
	 * 
	 * @author MightyPork
	 *
	 */
	@SuppressWarnings("javadoc")
	public static enum WeaselObjectType {

		BOOLEAN, INTEGER, STRING, VARMAP, STACK, NULL;

		private static HashMap<String,WeaselObjectType> members = new HashMap<String, WeaselObjectType>();

		/**
		 * Get enum type for type name
		 * 
		 * @param index type name
		 * @return corresponding enum type
		 */
		public static WeaselObjectType getTypeFromName(String name) {
			return members.get(name);
		}
		
		static {
		      for (WeaselObjectType type : WeaselObjectType.values()) {
		            members.put(type.toString(), type);
		      }
		}

	}

}
