package net.minecraft.src;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;

public class PClo_GuiMicroprocessor implements PC_IGresBase {

	private PClo_TileEntityGate tileEntity;
	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget edit;
	private PC_GresWidget txError;
	
	
	public PClo_GuiMicroprocessor(PClo_TileEntityGate tileEntity){
		this.tileEntity = tileEntity;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("Microprocessor");
		PC_GresWidget hg;
		w.add(edit = new PC_GresTextEdit(tileEntity.programm, 10));
		w.add(txError = new PC_GresLabel(""));
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	private boolean isProgrammOK(String programm){
		programm =programm.trim();
		char c;
		int h=0;
		if(programm.length()<1){
			txError.setText("Error");
			return false;
		}
		if(programm.length()==1){
			c = programm.charAt(0);
			if(c=='l'||c=='L')
				return true;
			if(c=='b'||c=='B')
				return true;
			if(c=='r'||c=='R')
				return true;
			txError.setText("Error unknown char '"+c+"'");
			return false;
		}
		if(programm.charAt(0)=='('){
			h=1;
			for(int i=1; i<programm.length()-1; i++){
				if(programm.charAt(i)=='(')
					h++;
				if(programm.charAt(i)==')'){
					h--;
					if(h==0)
						break;
				}
			}
			if(h>0){
				if(programm.charAt(programm.length()-1)==')')
					return isProgrammOK(programm.substring(1, programm.length()-1));
				txError.setText("Error Klammern");
				return false;
			}
		}
		h=0;
		for(int i=programm.length()-1; i>=0; i--){
			switch(programm.charAt(i)){
			case '(':
				h--;
				break;
			case ')':
				h++;
				break;
			case '&':
			case '|':
			case '^':
				if(h<=0)
					return isProgrammOK(programm.substring(0, i)) & isProgrammOK(programm.substring(i+1));
			}
		}
		if(programm.charAt(0)=='!'){
			return isProgrammOK(programm.substring(1));
		}
		txError.setText("Error");
		return false;
	}
	
	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==buttonCancel)
			gui.close();
		else if(widget==buttonOK){
			txError.setText("");
			if(isProgrammOK(edit.getText())){
				tileEntity.programm = edit.getText();
				gui.close();
			}
		}
		else if(widget==edit){
			txError.setText("");
			isProgrammOK(edit.getText());
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(buttonOK, gui);
	}

}
