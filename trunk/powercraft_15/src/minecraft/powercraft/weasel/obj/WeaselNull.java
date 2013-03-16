package powercraft.weasel.obj;


import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.PC_INBT;


/**
 * NULL placeholder for stacks.
 * 
 * @author MightyPork
 */
public class WeaselNull extends WeaselObject {

	/**
	 * Null placeholder in stack.
	 */
	public WeaselNull() {
		super(WeaselObjectType.NULL);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		return this;
	}

	@Override
	public WeaselObject copy() {
		return new WeaselNull();
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public void set(Object obj) {}

	@Override
	public String toString() {
		return "NULL";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof WeaselNull || obj == null;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
