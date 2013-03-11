package powercraft.weasel.obj;


import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import powercraft.weasel.exception.WeaselRuntimeException;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


/**
 * List of variables in the WeaselVM<br>
 * Variable list can be put into stack when CALL is executed,<br>
 * and it can also be written into NBT.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class WeaselStack extends WeaselObject {

	private Stack<WeaselObject> stack;

	/**
	 * clear the map.
	 */
	public void clear() {
		stack.clear();
	}

	/**
	 * Check if the stack is empty
	 * 
	 * @return is empty
	 */
	public boolean empty() {
		return stack.empty();
	}

	/**
	 * push object on stack
	 * 
	 * @param obj
	 */
	public void push(WeaselObject obj) {

		if (stack.size() > 3000) {
			throw new WeaselRuntimeException("Stack overflow - recursion too deep.");
		}

		if (obj == null) {
			stack.push(new WeaselNull());
		} else {
			stack.push(obj.copy());
		}
	}

	/**
	 * pop object from stack
	 * 
	 * @return the object
	 * @throws WeaselRuntimeException when stack is empty
	 */
	public WeaselObject pop() {
		try {

			WeaselObject obj = stack.pop();
			if (obj instanceof WeaselNull) return null;
			return obj;

		} catch (EmptyStackException e) {
			throw new WeaselRuntimeException("Calling POP on empty stack.");

		}
	}

	/**
	 * Look at the topmost object without removing.
	 * 
	 * @return the object on top of the stack
	 */
	public WeaselObject peek() {
		return stack.peek();
	}

	/**
	 * List of weasel variables
	 */
	public WeaselStack() {
		super(WeaselObjectType.STACK);
		stack = new Stack<WeaselObject>();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		NBTTagList tags = new NBTTagList();

		List<WeaselObject> list = new ArrayList<WeaselObject>(stack);

		for (WeaselObject obj : list) {
			NBTTagCompound tag1 = WeaselObject.saveObjectToNBT(obj, new NBTTagCompound());
			tags.appendTag(tag1);
		}
		tag.setTag("Stack", tags);

		return tag;

	}

	@Override
	public WeaselStack readFromNBT(NBTTagCompound tag) {


		NBTTagList tags = tag.getTagList("Stack");

		stack.clear();
		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			stack.push(WeaselObject.loadObjectFromNBT(tag1));
		}

		return this;

	}


	@Override
	public String toString() {
		return "Stack{" + stack + "}";
	}


	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}


	@Override
	public int hashCode() {
		return stack.hashCode();
	}


	@Override
	public Stack<WeaselObject> get() {
		return stack;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void set(Object obj) {
		if (obj == null || !(obj instanceof Stack)) {
			throw new RuntimeException("Trying to store " + obj + " in a Stack variable.");
		}
		stack = (Stack<WeaselObject>) obj;
	}

	@Override
	public WeaselStack copy() {
		WeaselObject obj = new WeaselStack();
		obj.set(stack.clone());
		return (WeaselStack) obj;
	}


}
