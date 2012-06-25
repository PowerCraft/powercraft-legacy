package net.minecraft.src.weasel;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;


public class WeaselInteger extends WeaselObject {
	
	public int integer = 0;
	
	public WeaselInteger(int integer){
		super(WeaselObjectType.INTEGER);
		this.integer = integer;
	}
	
	public WeaselInteger(){
		super(WeaselObjectType.INTEGER);
	}
	
	public int get(){
		return integer;
	}
	
	public void set(int integer){
		this.integer = integer;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		tag.setInteger("integer", integer);
		return this;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("integer", integer);
		return tag;
	}

}
