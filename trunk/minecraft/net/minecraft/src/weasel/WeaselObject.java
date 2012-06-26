package net.minecraft.src.weasel;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.PC_Utils;

/**
 * Weasel data object.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public abstract class WeaselObject implements PC_INBT {

	private WeaselObjectType type = null;


	public WeaselObject(WeaselObjectType type) {
		this.type = type;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("type", type.getIndex());
		return tag;
	}

	/**
	 * @return object type
	 */
	public WeaselObjectType getType() {
		return type;
	}

	/**
	 * Save Weasel Object to NBT, wrapped in it's own NBTTagCompound.<br>
	 * It is at the same time added to parent compound tag.
	 * 
	 * @param outerTag
	 * @param wrappingTagName
	 * @return the wrapping tag
	 */
	public NBTTagCompound saveWrappedToNBT(NBTTagCompound outerTag, String wrappingTagName) {
		return PC_Utils.writeWrappedToNBT(outerTag, wrappingTagName, this);
	}

	/**
	 * Get Weasel Object from a wrapping compound tag.
	 * 
	 * @param outerTag
	 * @param wrappingTagName
	 * @return
	 */
	public static WeaselObject loadWrappedObjectFromNBT(NBTTagCompound outerTag, String wrappingTagName) {
		return loadObjectFromNBT(outerTag.getCompoundTag(wrappingTagName));
	}

	/**
	 * Load an object from Compound NBT tag, using the proper WeaselObject subtype.
	 * 
	 * @param tag the tag with object
	 * @return the object
	 */
	public static WeaselObject loadObjectFromNBT(NBTTagCompound tag) {
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

	@Override
	public abstract String toString();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();
}
