package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PC_DataMemoryManager extends WorldSavedData {

	public List<PC_INBT> list = new ArrayList<PC_INBT>();
	
	public PC_DataMemoryManager(String par1Str) {
		super(par1Str);
		setDirty(true);
		try {
			for(Class c : PC_Module.dataMemory.values())
				list.add((PC_INBT)c.newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound var1) {
		int size = var1.getInteger("size");
		for(int i=0; i<size; i++){
			String memoryName = var1.getString("memoryName["+i+"]");
			for(PC_INBT mem:list)
				if(mem.getClass().getName()==memoryName)
					mem.readFromNBT(var1.getCompoundTag("dataMemory["+i+"]"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound var1) {
		var1.setInteger("size", list.size());
		int i=0;
		for(PC_INBT mem : list){
			var1.setString("memoryName["+i+"]", list.getClass().getName());
			NBTTagCompound tc = new NBTTagCompound();
			mem.writeToNBT(tc);
			var1.setCompoundTag("dataMemory["+i+"]", tc);
			i++;
		}
	}

}
