package net.minecraft.src.weasel;


import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.lang.InstructionAssignRetval;
import net.minecraft.src.weasel.lang.InstructionCall;
import net.minecraft.src.weasel.lang.InstructionEnd;
import net.minecraft.src.weasel.lang.InstructionFunction;
import net.minecraft.src.weasel.lang.InstructionGoto;
import net.minecraft.src.weasel.lang.InstructionIf;
import net.minecraft.src.weasel.lang.InstructionLabel;
import net.minecraft.src.weasel.lang.InstructionReturn;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselObject;


public class Test {

	public void run() {


		// extracting function calls from an expression

//		List<String> tmpEvals = new ArrayList<String>();
//		
//		int tmpCounter = 0;
//		String expression = "variable = Math.floor(X*16)";
//		
//		expression = expression.replaceAll("\\s", "");
//		System.out.println("ee: " +expression);
//		
//		StringBuffer sb = new StringBuffer();
//		Pattern fnPattern = Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)\\(([^(]*?)\\)");
//		
//		int functionsFoundThisTurn=-1;
//		while(functionsFoundThisTurn != 0) {
//			functionsFoundThisTurn = 0;
//			Matcher funcMatcher = fnPattern.matcher(expression);
//			
//			funcMatcher = fnPattern.matcher(expression);
//			
//			sb = new StringBuffer();
//			while(funcMatcher.find()){
//			    String name = funcMatcher.group(1);
//			    String args = funcMatcher.group(2);
//			    String tmpvar = "_tmp" + tmpCounter++;
//			    tmpEvals.add(tmpvar+"="+name+"("+args+")");
//			    funcMatcher.appendReplacement(sb, tmpvar);
//			    functionsFoundThisTurn++;
//			}
//			funcMatcher.appendTail(sb);
//			
//			expression = sb.toString();	
//			System.out.println("expr2: "+expression);
//		}
//		
//		
//		for(String line : tmpEvals) {
//			System.out.println(line);
//		}
//		
//		System.out.println("Expression remaining: "+expression);
//		Matcher varMatcher = Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)([^a-zA-Z_0-9.()]|$)").matcher(expression);
//		while(varMatcher.find()){
//		    String name = varMatcher.group(1);
//		    System.out.println("varNeeded: "+name);
//		}

		
		IWeaselHardware hardware = new IWeaselHardware() {

			@Override
			public boolean hasFunction(String functionName) {
				if(functionName.equals("print")) {
					return true;
				}
				return false;
			}
			
			@Override
			public int getFunctionArgumentCount(String functionName) {
				
				if(functionName.equals("print")) {
					return 1;
				}
				
				
				return 0;
			}

			@Override
			public WeaselObject callFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
				
				if(functionName.equals("print")) {
					
					System.out.println(args[0].get());
					return new WeaselInteger(((String)args[0].get()).length());
					
				}
				
				return null;
			}

			@Override
			public WeaselObject getVariable(String name) {
				if (name.equals("hw.num")) return new WeaselInteger(10);
				return null;
			}

			@Override
			public void setVariable(String name, Object object) {}



		};


		try {
			
			WeaselEngine engine = new WeaselEngine(hardware);

			engine.setVariable("abc", 99);
			engine.setVariable("out", 0);

			InstructionList prog = engine.instructionList;
			
			prog.append(new InstructionIf("abc - 99 == out","yes","no"));
			prog.append(new InstructionLabel("yes"));
			prog.append(new InstructionCall("say", "\"YES!\""));	
			prog.append(new InstructionAssignRetval("eee"));
			prog.append(new InstructionCall("print","\"eee is equal to \"+str(eee)"));
			prog.append(new InstructionGoto("end"));
			prog.append(new InstructionLabel("no"));
			prog.append(new InstructionCall("print", "\"NO!\""));
			prog.append(new InstructionLabel("end"));
			prog.append(new InstructionEnd());
			
			prog.append(new InstructionFunction("say", "what"));
			prog.append(new InstructionCall("print", "str(what)"));
			prog.append(new InstructionReturn("17"));
			
			engine.run(100);

		} catch (RuntimeException re) {
			System.out.println(re.getMessage());
		}


//		WeaselVariableMap map = engine.variables;
//		map.setVariable("integer", new WeaselInteger(-13));
//		map.setVariable("varint", new WeaselInteger(17));
//		map.setVariable("bool", new WeaselBoolean(true));
//		map.setVariable("bool_second", new WeaselBoolean(false));
//		map.setVariable("string", new WeaselString("Ahoy! ěščř"));
//		map.setVariable("string2", new WeaselString("SECOND"));
//		
//
//		// variable assign
//		Calculator.eval("integer += var * hw.num", engine);
//		System.out.println(" = "+map.getVariable("var1"));
//		if(true) return;
//		
//		assert (((WeaselInteger) map.getVariable("var1")).get() == -13 + 17);
//
//		// strings
//		Calculator.eval("string+='!'", engine);
//		assert (((WeaselInteger) map.getVariable("var1")).get().equals("Ahoy! ěšč!"));
//
//
//		// NBT
//		NBTTagCompound tag;
//		tag = map.writeToNBT(new NBTTagCompound());
//		WeaselVariableMap map2 = (new WeaselVariableMap()).readFromNBT(tag);
//		assert (map.equals(map2));
//
//		WeaselString str1 = new WeaselString("Oh boy!");
//		tag = str1.writeToNBT(new NBTTagCompound());
//		WeaselString str2 = new WeaselString().readFromNBT(tag);
//		assert (str1.equals(str2));
//
//		WeaselInteger int1 = new WeaselInteger(999);
//		tag = int1.writeToNBT(new NBTTagCompound());
//		WeaselInteger int2 = new WeaselInteger().readFromNBT(tag);
//		assert (int1.equals(int2));
//
//
//		WeaselStack stack = new WeaselStack();
//		stack.push(new WeaselInteger(-13));
//		stack.push(new WeaselString("a"));
//		stack.push(new WeaselString("b"));
//		stack.push(new WeaselString("c"));
//		stack.push(new WeaselBoolean(0));
//		stack.push(new WeaselInteger(999888));
//		tag = stack.writeToNBT(new NBTTagCompound());
//		WeaselStack stack2 = new WeaselStack().readFromNBT(tag);
//		assert (stack.equals(stack2));

	}

}
