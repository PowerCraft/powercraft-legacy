package weasel.compiler;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.v2.tokentree.WeaselInstructionPlaceHolder;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionJump;
import weasel.interpreter.bytecode.WeaselInstructionLine;
import weasel.interpreter.bytecode.WeaselInstructionReservate;

public class WeaselInstructionList {

	private List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
	private int line = 0;
	
	public void addWithoutLine(WeaselInstruction instruction){
		instructions.add(instruction);
	}
	
	
	public void add(int line, WeaselInstruction instruction){
		if(line!=this.line){
			instructions.add(new WeaselInstructionLine(line));
			this.line = line;
		}
		instructions.add(instruction);
	}
	
	public void addFirst(int line, WeaselInstructionReservate instruction) {
		WeaselInstruction inst = instructions.get(0);
		if(inst instanceof WeaselInstructionLine){
			int rline = ((WeaselInstructionLine) inst).getLine();
			if(rline==line){
				instructions.add(1, instruction);
				return;
			}
		}
		instructions.add(0, instruction);
		instructions.add(0, new WeaselInstructionLine(line));
	}
	
	public void addAll(WeaselInstructionList instructionList){
		if(instructionList.instructions.isEmpty())
			return;
		line = instructionList.line;
		instructions.addAll(instructionList.instructions);
	}

	public WeaselInstruction[] getInstructions() {
		for(WeaselInstruction instruction:instructions){
			if(instruction instanceof WeaselInstructionJump){
				WeaselInstruction target = ((WeaselInstructionJump) instruction).getTarget();
				int i=0;
				for(WeaselInstruction targetInstruction:instructions){
					if(targetInstruction==target)
						break;
					i++;
				}
				((WeaselInstructionJump) instruction).setTargetIndex(i+1);
			}
		}
		return instructions.toArray(new WeaselInstruction[0]);
	}

	public WeaselInstruction getLast() {
		return instructions.get(instructions.size()-1);
	}

	public void replacePlaceHolderWith(WeaselInstructionList instructions2) {
		List<WeaselInstruction> after = new ArrayList<WeaselInstruction>();
		WeaselInstruction wi;
		while(!((wi = instructions.remove(instructions.size()-1)) instanceof WeaselInstructionPlaceHolder)){
			after.add(0, wi);
		}
		instructions.addAll(instructions2.instructions);
		instructions.addAll(after);
	}
	
	@Override
	public String toString(){
		return ""+instructions;
	}
	
	public int getLine(){
		return line;
	}
	
}
