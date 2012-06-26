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
		tag.setBoolean("b", bool);
		return this;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setBoolean("b", bool);
		return tag;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (!this.getClass().equals(obj.getClass())) { return false; }

		return ((WeaselBoolean) obj).bool == bool;
	}


	@Override
	public int hashCode() {
		return bool?1:0;
	}

	@Override
	public String toString() {
		return "BOOL("+(bool?"1":"0")+")";
	}	
}
