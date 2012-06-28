package net.minecraft.src.weasel;


import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselObject;
import net.minecraft.src.weasel.obj.WeaselStack;
import net.minecraft.src.weasel.obj.WeaselString;
import net.minecraft.src.weasel.obj.WeaselVariableMap;


public class Test {

	public void run() {
		WeaselEngine engine = new WeaselEngine(new IWeaselControlled() {
			
			@Override
			public boolean hasFunction(String functionName) {
				return false;
			}
			
			@Override
			public WeaselObject getVariable(String name) {
				if(name.equals("hw.num")) return new WeaselInteger(13);
				return null;
			}
			
			@Override
			public WeaselObject callFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
				return null;
			}
		});
		
		WeaselVariableMap map = engine.variables;
		map.setVariable("integer", new WeaselInteger(-13));
		map.setVariable("varint", new WeaselInteger(17));
		map.setVariable("bool", new WeaselBoolean(true));
		map.setVariable("bool_second", new WeaselBoolean(false));
		map.setVariable("string", new WeaselString("Ahoy! ěščř"));
		map.setVariable("string2", new WeaselString("SECOND"));
		

		// variable assign
		Calculator.eval("integer += var * hw.num", engine);
		System.out.println(" = "+map.getVariable("var1"));
		if(true) return;
		
		assert (((WeaselInteger) map.getVariable("var1")).get() == -13 + 17);

		// strings
		Calculator.eval("string+='!'", engine);
		assert (((WeaselInteger) map.getVariable("var1")).get().equals("Ahoy! ěšč!"));


		// NBT
		NBTTagCompound tag;
		tag = map.writeToNBT(new NBTTagCompound());
		WeaselVariableMap map2 = (new WeaselVariableMap()).readFromNBT(tag);
		assert (map.equals(map2));

		WeaselString str1 = new WeaselString("Oh boy!");
		tag = str1.writeToNBT(new NBTTagCompound());
		WeaselString str2 = new WeaselString().readFromNBT(tag);
		assert (str1.equals(str2));

		WeaselInteger int1 = new WeaselInteger(999);
		tag = int1.writeToNBT(new NBTTagCompound());
		WeaselInteger int2 = new WeaselInteger().readFromNBT(tag);
		assert (int1.equals(int2));


		WeaselStack stack = new WeaselStack();
		stack.push(new WeaselInteger(-13));
		stack.push(new WeaselString("a"));
		stack.push(new WeaselString("b"));
		stack.push(new WeaselString("c"));
		stack.push(new WeaselBoolean(0));
		stack.push(new WeaselInteger(999888));
		tag = stack.writeToNBT(new NBTTagCompound());
		WeaselStack stack2 = new WeaselStack().readFromNBT(tag);
		assert (stack.equals(stack2));

	}

}
