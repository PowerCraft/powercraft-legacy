package net.minecraft.src.weasel;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;

public abstract class WeaselObject implements PC_INBT {
	public static enum WeaselObjectType {
		STRING(1), INTEGER(2), BOOLEAN(3), VARIABLE_LIST(4), STACK(5);

		public final int index;

		private WeaselObjectType(int index) {
			this.index = index;
		}
	}

	private WeaselObjectType type = null;

	public WeaselObject(WeaselObjectType type) {

	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("type", type.index);
	}

	public static WeaselObject loadObjectFromNBT(NBTTagCompound tag) {
		WeaselObject obj = null;

		switch (tag.getInteger("type")) {

/*			case 1:
				obj = new WeaselString();
				obj.readFromNBT(tag);
				break;

			case 2:
				obj = new WeaselInteger();
				obj.readFromNBT(tag);
				break;

			case 3:
				obj = new WeaselBoolean();
				obj.readFromNBT(tag);
				break;*/

			case 4:
				obj = new WeaselVariableList();
				obj.readFromNBT(tag);
				break;

/*			case 5:
				obj = new WeaselStack();
				obj.readFromNBT(tag);
				break;*/

		}

		return obj;
	}
}
