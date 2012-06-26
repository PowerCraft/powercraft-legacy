package net.minecraft.src.weasel.obj;


/**
 * Type of a Weasel data object
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
@SuppressWarnings("javadoc")
public enum WeaselObjectType {

	BOOLEAN(1), INTEGER(2), STRING(3), VARIABLE_LIST(4), STACK(5);

	/**  */
	private final int index;

	private WeaselObjectType(int index) {
		this.index = index;
	}

	public static WeaselObjectType getTypeFromIndex(int index) {
		switch (index) {
			case 1:
				return BOOLEAN;
			case 2:
				return INTEGER;
			case 3:
				return STRING;
			case 4:
				return VARIABLE_LIST;
			case 5:
				return STACK;
			default:
				return null;
		}
	}

	/**
	 * @return type index
	 */
	public int getIndex() {
		return index;
	}


}
