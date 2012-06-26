package net.minecraft.src.weasel;

import java.util.ArrayList;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.PC_INBT;
import net.minecraft.src.weasel.obj.WeaselObject;

/**
 * Weasel instruction list
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class InstructionList implements PC_INBT {

	private ArrayList<IInstruction> list = new ArrayList<IInstruction>();
	private WeaselVM vm;

	/**
	 * Instruction list for VM
	 * @param vm
	 */
	public InstructionList(WeaselVM vm) {
		this.vm = vm;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		NBTTagList tags = new NBTTagList();

		int index = 0;
		int size = list.size();
		for(index = 0; index < size; index++){
			NBTTagCompound tag1 = list.get(index).writeToNBT(new NBTTagCompound());
			tag1.setInteger("Index", index);
			tags.appendTag(tag1);
		}
		tag.setTag("List", tags);
		tag.setInteger("Size", size);

		return tag;

	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {

		int size = tag.getInteger("Size");
		
		ArrayList<WeaselObject> list = new ArrayList<WeaselObject>(size);

		NBTTagList tags = tag.getTagList("List");

		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			list.set(tag1.getInteger("Index"), WeaselObject.loadObjectFromNBT(tag1));
		}

		return this;

	}

}
