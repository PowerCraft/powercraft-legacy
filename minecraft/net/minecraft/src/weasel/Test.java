package net.minecraft.src.weasel;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselStack;
import net.minecraft.src.weasel.obj.WeaselString;
import net.minecraft.src.weasel.obj.WeaselVariableMap;

public class Test {
	
	public void run(){
		WeaselVariableMap map = new WeaselVariableMap();
		map.set("var1",new WeaselInteger(-13));
		map.set("var2",new WeaselInteger(17));
		map.set("var3",new WeaselInteger(99));
		map.set("bool",new WeaselBoolean(true));
		map.set("bool_second",new WeaselBoolean(false));
		map.set("string",new WeaselString("Ahoy! ěščř"));
		map.set("string2",new WeaselString("SECOND"));
		
		// variable assign
		Calculator.eval("var1 += var2 * var3", map);		
		assert(((WeaselInteger)map.get("var1")).get() == -13 + 17);
		
		// strings
		Calculator.eval("string+='!'", map);		
		assert(((WeaselInteger)map.get("var1")).get().equals("Ahoy! ěšč!"));
		
		
		// NBT
		NBTTagCompound tag;
		tag = map.writeToNBT(new NBTTagCompound());
		WeaselVariableMap map2 = (new WeaselVariableMap()).readFromNBT(tag);		
		assert(map.equals(map2));
		
		WeaselString str1 = new WeaselString("Oh boy!");
		tag = str1.writeToNBT(new NBTTagCompound());
		WeaselString str2 = new WeaselString().readFromNBT(tag);
		assert(str1.equals(str2));
		
		WeaselInteger int1 = new WeaselInteger(999);
		tag = int1.writeToNBT(new NBTTagCompound());
		WeaselInteger int2 = new WeaselInteger().readFromNBT(tag);
		assert(int1.equals(int2));
		
		
		WeaselStack stack = new WeaselStack();
		stack.push(new WeaselInteger(-13));
		stack.push(new WeaselString("a"));
		stack.push(new WeaselString("b"));
		stack.push(new WeaselString("c"));
		stack.push(new WeaselBoolean(0));
		stack.push(new WeaselInteger(999888));		
		tag = stack.writeToNBT(new NBTTagCompound());
		WeaselStack stack2 = new WeaselStack().readFromNBT(tag);		
		assert(stack.equals(stack2));
		
	}
	
}
