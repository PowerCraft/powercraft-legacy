package net.minecraft.src.weasel;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;

public class WeaselString extends WeaselObject {
	
	public String string;

	public WeaselString() {
		super(WeaselObjectType.STRING);		
		string = "";		
	}
	
	public WeaselString(String string) {
		super(WeaselObjectType.STRING);		
		string = string;		
	}
	
	public String get(){
		return string;
	}
	
	public void set(String string){
		this.string = string;
	}

	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("s", string);
		return tag;		
	}
	
	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		string = tag.getString("s");
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (!this.getClass().equals(obj.getClass())) { return false; }

		return ((WeaselString) obj).string == string;
	}


	@Override
	public int hashCode() {
		return string.hashCode();
	}

	@Override
	public String toString() {
		return "STRING("+string+")";
	}	

}
