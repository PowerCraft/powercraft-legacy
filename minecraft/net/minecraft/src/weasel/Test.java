package net.minecraft.src.weasel;


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




		WeaselEngine engine = new WeaselEngine(new IWeaselControlled() {

			@Override
			public boolean hasFunction(String functionName) {
				return false;
			}

			@Override
			public WeaselObject getVariable(String name) {
				if (name.equals("hw.num")) return new WeaselInteger(13);
				return null;
			}

			@Override
			public WeaselObject callFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
				return null;
			}

			@Override
			public void setVariable(String name, Object object) {}

		});

		try {

			engine.setVariable("nn", 99);
			engine.setVariable("out", 0);

			Calculator.eval("out = hw.num*nn - 1", engine);

			System.out.println("=" + engine.getVariable("out"));

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
