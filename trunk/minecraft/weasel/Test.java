package weasel;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.NBTTagCompound;

import weasel.exception.SyntaxError;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;



public class Test {

	public void run() {
		if(true) return;

		IWeaselHardware hardware = new IWeaselHardware() {

			@Override
			public boolean doesProvideFunction(String functionName) {
				if (functionName.equals("print")) {
					return true;
				}
				return false;
			}

			@Override
			public WeaselObject callProvidedFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {

				if (functionName.equals("print")) {
					System.out.println(args[0].get());
					return new WeaselInteger(((String) args[0].get()).length());

				}

				return null;
			}

			@Override
			public WeaselObject getVariable(String name) {
				return null;
			}

			@Override
			public void setVariable(String name, Object object) {}

			@Override
			public List<String> getProvidedFunctionNames() {
				List<String> list = new ArrayList<String>(1);
				list.add("print");
				return list;
			}

			@Override
			public List<String> getProvidedVariableNames() {
				List<String> list = new ArrayList<String>(0);
				return list;
			}


		};

		final class PB {
			private StringBuilder sb = new StringBuilder();

			public void a(String line) {
				sb.append(line).append("\n");
			}

			@Override
			public String toString() {
				return sb.toString();
			}
		}

		PB pb = new PB();
		pb.a("global quee = 98;");
		pb.a("for(a=quee/2 + 1:0){");
		pb.a("	print(str(a));");
		pb.a("	if(a == 13) continue;");
		pb.a("	print('mars!');");
		pb.a("}");
		pb.a("");
		pb.a("print('end');");
		pb.a("");
		pb.a("");
		pb.a("");
		pb.a("");
		pb.a("");
		pb.a("");
		pb.a("");
		
		
//		pb.a("global state = false;");
//		pb.a("");
//		pb.a("function pinChanged(){");
//		pb.a("	state = !state;");
//		pb.a("	if(state){");
//		pb.a("		print('ON');");
//		pb.a("	}else{");
//		pb.a("		print('OFF');");
//		pb.a("	}");
//		pb.a("}");
//		pb.a("print('program end.');");
//		pb.a("end;");

//		pb.a("print('*** APPLES & ORANGES ® ***');");
//		pb.a("print('Program for Weasel with terminal connected as hardware');");
//		pb.a("if(!isset(apples)) global apples = 4;");
//		pb.a("if(!isset(oranges)) global oranges = 20;");
//		pb.a("if(!isset('counter')){");
//		pb.a("	global counter = 0;");
//		pb.a("	print('BUMP');");
//		pb.a("}else{");
//		pb.a("	counter++;");
//		pb.a("}");
//		pb.a("print('Counter = '+str(counter));");
//		pb.a("fruits = showFruits();");
//		pb.a("print('Total fruits = ' + str(fruits));");
//		pb.a("function showFruits(){");
//		pb.a("	print('apples = ' + str(apples));");
//		pb.a("	print('oranges = ' + str(oranges));");
//		pb.a("	return apples+oranges;");
//		pb.a("}");
//		pb.a("");
//		pb.a("function say(what){");
//		pb.a("	return print(str(what));");
//		pb.a("}");
//		pb.a("");
//		pb.a("");
//		pb.a("print('Restarting...');");
//		pb.a("print('');");
//		pb.a("restart;");


		String program = pb + "";


		System.out.println(program);

		try {

			List<Instruction> list = null;
			//long time = System.currentTimeMillis();

			list = (new Compiler()).compile(program);

			//System.out.println("TIME CONSUMED = " + (System.currentTimeMillis() - time));

			for (Instruction in : list) {
				System.out.println(in);
			}

			System.out.println();
			System.out.println("TOTAL INSTRUCTIONS = " + list.size());

			System.out.println();

			WeaselEngine engine = new WeaselEngine(hardware);
			engine.instructionList.addAll(list);

			try {
				
				engine.run(511);
				
			} catch (WeaselRuntimeException we) {
				System.out.println("ERROR: " + we.getMessage());
			}
			
			

		} catch (SyntaxError e) {
			System.out.println("ERROR: " + e.getMessage());
		}

		System.exit(0);

	}

}
