package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PC_DataMemoryManager extends WorldSavedData {

	private List<PC_INBT> list = new ArrayList<PC_INBT>();
	
	public PC_DataMemoryManager(String par1Str) {
		super(par1Str);
	}

	@Override
	public void readFromNBT(NBTTagCompound var1) {
		System.out.println("DM: readFromNBT");
		int size = var1.getInteger("size");
		for(int i=0; i<size; i++){
			String memoryName = var1.getString("memoryName["+i+"]");
			System.out.println(memoryName);
			PC_INBT mem;
			try {
				mem = (PC_INBT)Class.forName(memoryName).newInstance();
				list.add(mem);
				mem.readFromNBT(var1.getCompoundTag("dataMemory["+i+"]"));
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound var1) {
		System.out.println("DM: writeToNBT");
		var1.setInteger("size", list.size());
		int i=0;
		for(PC_INBT mem : list){
			System.out.println(mem.getClass().getName());
			var1.setString("memoryName["+i+"]", mem.getClass().getName());
			NBTTagCompound tc = new NBTTagCompound();
			mem.writeToNBT(tc);
			var1.setCompoundTag("dataMemory["+i+"]", tc);
			i++;
		}
	}

	public void register(PC_INBT mem) {
		list.add(mem);
	}
	
	public void unRegister(PC_INBT mem){
		list.remove(mem);
	}
	
	@Override
	public boolean isDirty(){
		return true;
	}
	
}
