package net.minecraft.src.weasel;

import java.util.ArrayList;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PC_INBT;

public class InstructionList implements PC_INBT {
	
	private ArrayList<IInstruction> list = new ArrayList<IInstruction>();
	private WeaselVM vm;
	
	public InstructionList(WeaselVM vm) {
		this.vm = vm;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		return null;
	}

}
