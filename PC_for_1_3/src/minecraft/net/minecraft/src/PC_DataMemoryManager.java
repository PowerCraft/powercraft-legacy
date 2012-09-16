package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class PC_DataMemoryManager extends WorldSavedData {
	
	public PC_DataMemoryManager(String par1Str) {
		super(par1Str);
		System.out.println("New DMM");
	}

	@Override
	public void readFromNBT(NBTTagCompound var1) {
		System.out.println("DM: readFromNBT");
		Set<Entry<String,PC_INBTWD>> ent = PC_Module.netList.entrySet();
		Iterator<Entry<String,PC_INBTWD>>it = ent.iterator();
		while(it.hasNext()){
			Entry<String,PC_INBTWD> e = it.next();
			e.getValue().readFromNBT(var1.getCompoundTag(e.getKey()));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound var1) {
		System.out.println("DM: writeToNBT");
		Set<Entry<String,PC_INBTWD>> ent = PC_Module.netList.entrySet();
		Iterator<Entry<String,PC_INBTWD>>it = ent.iterator();
		while(it.hasNext()){
			Entry<String,PC_INBTWD> e = it.next();
			NBTTagCompound tc = new NBTTagCompound();
			e.getValue().writeToNBT(tc);
			var1.setCompoundTag(e.getKey(), tc);
		}
	}
	
	@Override
	public boolean isDirty(){
		for(PC_INBTWD net : PC_Module.netList.values())
			if(net.needsSave())
				return true;
		return false;
	}
	
}
