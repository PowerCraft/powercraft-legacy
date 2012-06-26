package net.minecraft.src.weasel;

import java.util.ArrayList;
import java.util.Stack;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.PC_CoordI;
import net.minecraft.src.PC_INBT;

/**
 * List of variables in the WeaselVM<br>
 * Variable list can be put into stack when CALL is executed,<br>
 * and it can also be written into NBT.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class WeaselStack extends WeaselObject {
	
	private Stack<WeaselObject> stack;
	
	/**
	 * clear the map.
	 */
	public void clear(){
		stack.clear();
	}


	/**
	 * List of weasel variables
	 */
	public WeaselStack() {
		super(WeaselObjectType.VARIABLE_LIST);
		stack = new Stack<WeaselObject>();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		
		NBTTagList tags = new NBTTagList();
		
		int posFromTop = 0;
		int size = stack.size();
		while(!stack.empty()){
			NBTTagCompound tag1 = stack.pop().writeToNBT(new NBTTagCompound());
			tag1.setInteger("sIndex", posFromTop++);
			tags.appendTag(tag1);
		}
		tag.setTag("Stack", tags);
		tag.setInteger("sSize", size);
		
		return tag;
		
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		
		int size = tag.getInteger("sSize");
		ArrayList<WeaselObject> list = new ArrayList<WeaselObject>(size);
		
		NBTTagList tags = tag.getTagList("Stack");
		
		for(int i=0; i<tags.tagCount(); i++){
			NBTTagCompound tag1 = (NBTTagCompound) tags.tagAt(i);
			list.set(tag1.getInteger("sIndex"),WeaselObject.loadObjectFromNBT(tag1));
		}
		
		for(WeaselObject obj : list){
			stack.push(obj);
		}
		
		return this;
		
	}


	@Override
	public String toString() {
		return "STACK("+stack+")";
	}


	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}


	@Override
	public int hashCode() {
		return stack.hashCode();
	}	
	

}
