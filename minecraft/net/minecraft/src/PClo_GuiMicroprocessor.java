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
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow("Microprocessor");
		PC_GresWidget hg;
		w.add(edit = new PC_GresTextEdit(tileEntity.programm, 10));
		w.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	private int isProgrammOK(String programm){
		programm =programm.trim();
		char c;
		int h=0;
		if(programm.length()<1){
			txError.setText("Error");
			return 1;
		}
		if(programm.length()==1){
			c = programm.charAt(0);
			if(c=='l'||c=='L')
				return 0;
			if(c=='b'||c=='B')
				return 0;
			if(c=='r'||c=='R')
				return 0;
			txError.setText("Error unknown char '"+c+"'");
			return 2;
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
				return 3;
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
				if(h<=0){
					int e1 = isProgrammOK(programm.substring(0, i));
					int e2 = isProgrammOK(programm.substring(i+1));
					if(e1==0 && e2==0)
						return 0;
					if(e1==1){
						txError.setText("you need an text bevore '"+programm.charAt(i)+"'");
						return 6;
					}
					if(e2==1){
						txError.setText("you need an text behind '"+programm.charAt(i)+"'");
						return 6;
					}
					return e1==0?e2:e1;
				}
			}
		}
		if(programm.charAt(0)=='!'){
			int e = isProgrammOK(programm.substring(1));
			if(e==0)
				return 0;
			if(e==1){
				txError.setText("you need an text behind '!'");
				return 5;
			}
			return e;
		}
		txError.setText("Error unknown string '"+programm+"'");
		return 4;
	}
	
	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget==buttonCancel)
			gui.close();
		else if(widget==buttonOK){
			txError.setText("");
			if(isProgrammOK(edit.getText())==0){
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
