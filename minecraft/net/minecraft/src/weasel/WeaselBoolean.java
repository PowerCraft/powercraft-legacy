package net.minecraft.src.weasel;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;

public class WeaselBoolean extends WeaselObject {
	
	public boolean bool = false;
	
	public WeaselBoolean(boolean bool){
		super(WeaselObjectType.BOOLEAN);
		this.bool = bool;
	}
	public WeaselBoolean(){
		super(WeaselObjectType.BOOLEAN);
	}
	
	public boolean get(){
		return bool;
	}
	
	public void set(boolean bool){
		this.bool = bool;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		tag.setBoolean("bool", bool);
		return this;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setBoolean("bool", bool);
		return tag;
	}

}
