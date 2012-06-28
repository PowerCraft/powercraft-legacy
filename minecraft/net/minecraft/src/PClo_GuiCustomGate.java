package net.minecraft.src;

import net.minecraft.src.PC_GresWidget.PC_GresAlign;
import net.minecraft.src.weasel.Calculator;
import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselVariableMap;

public class PClo_GuiCustomGate implements PC_IGresBase {

	private PClo_TileEntityGate tileEntity;
	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget edit;
	private PC_GresWidget txError;


	public PClo_GuiCustomGate(PClo_TileEntityGate tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("tile.PCloLogicGate.programmable.name"));
		PC_GresWidget hg;
		w.add(edit = new PC_GresTextEdit(tileEntity.programm, 20));
		w.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton("L").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("R").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("B").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("(").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton(")").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("|").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("&").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("^").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("!").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("(?:)").setId(100).setMinWidth(0));
		w.add(hg);
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton(" ").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton(">").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("<").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("==").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton(">=").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("<=").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("!=").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("+").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("*").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("-").setId(100).setMinWidth(0));
		w.add(hg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabelMultiline(PC_Lang.tr("pc.gui.customGate.legend"),230).setAlignH(PC_GresAlign.CENTER).setColor(PC_GresWidget.textColorEnabled, 0x606060));
		w.add(hg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	private boolean isProgrammOK(String program) {
		program = program.trim();
		
		try{
			WeaselVariableMap vars = new WeaselVariableMap();
			vars.setVariable("L",new WeaselBoolean(false));
			vars.setVariable("R",new WeaselBoolean(false));
			vars.setVariable("B",new WeaselBoolean(false));
			vars.setVariable("F",new WeaselBoolean(false));
			
			Calculator.eval("F = "+program, vars);
			
			return true;
			
		}catch(Exception e){
			return false;
		}
		
//		char c;
//		int h = 0;
//		if (program.length() < 1) {
//			txError.setText(PC_Lang.tr("pc.gui.customGate.errProgEq0"));
//			// txError.setText("Error: program need to be longer than 0");
//			return 1;
//		}
//		if (program.length() == 1) {
//			c = program.charAt(0);
//			if (c == 'l' || c == 'L') { return 0; }
//			if (c == 'b' || c == 'B') { return 0; }
//			if (c == 'r' || c == 'R') { return 0; }
//			txError.setText(PC_Lang.tr("pc.gui.customGate.errUnkChar", new String[] { "" + c }));
//			// txError.setText("Error: unknown char '"+c+"'");
//			return 2;
//		}
//		if (program.charAt(0) == '(') {
//			h = 1;
//			for (int i = 1; i < program.length() - 1; i++) {
//				if (program.charAt(i) == '(') {
//					h++;
//				}
//				if (program.charAt(i) == ')') {
//					h--;
//					if (h == 0) {
//						break;
//					}
//				}
//			}
//			if (h > 0) {
//				if (program.charAt(program.length() - 1) == ')') { return isProgrammOK(program.substring(1, program.length() - 1)); }
//				txError.setText(PC_Lang.tr("pc.gui.customGate.errUnclosed"));
//				// txError.setText("Unclosed bracket");
//				return 3;
//			}
//		}
//		h = 0;
//		for (int i = program.length() - 1; i >= 0; i--) {
//			switch (program.charAt(i)) {
//				case '(':
//					h--;
//					break;
//				case ')':
//					h++;
//					break;
//				case '&':
//				case '|':
//				case '^':
//					if (h <= 0) {
//						int e1 = isProgrammOK(program.substring(0, i));
//						int e2 = isProgrammOK(program.substring(i + 1));
//						if (e1 == 0 && e2 == 0) { return 0; }
//						if (e1 == 1) {
//							txError.setText(PC_Lang.tr("pc.gui.customGate.errNoTextBefore", new String[] { "" + program.charAt(i) }));
//							// txError.setText("You need a text before '"+program.charAt(i)+"'");
//							return 6;
//						}
//						if (e2 == 1) {
//							txError.setText(PC_Lang.tr("pc.gui.customGate.errNoTextBehind", new String[] { "" + program.charAt(i) }));
//							// txError.setText("You need a text behind '"+program.charAt(i)+"'");
//							return 6;
//						}
//						return e1 == 0 ? e2 : e1;
//					}
//			}
//		}
//		if (program.charAt(0) == '!') {
//			int e = isProgrammOK(program.substring(1));
//			if (e == 0) { return 0; }
//			if (e == 1) {
//				txError.setText(PC_Lang.tr("pc.gui.customGate.errNoTextBehind", new String[] { "!" }));
//				// txError.setText("You need a text behind '!'");
//				return 5;
//			}
//			return e;
//		}
//		txError.setText(PC_Lang.tr("pc.gui.customGate.errUnkString", new String[] { program }));
//		// txError.setText("Error: unknown string '"+program+"'");
//		return 4;
		
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == buttonCancel) {
			gui.close();
		} else if (widget == buttonOK) {
			txError.setText("");
			if (isProgrammOK(edit.getText())) {
				tileEntity.programm = edit.getText();
				gui.close();
			}
		} else if (widget == edit) {
			txError.setText("");
			if(!isProgrammOK(edit.getText())){
				txError.setText(PC_Lang.tr("pc.gui.customGate.syntaxError"));
			}
		} else if (widget.getId() == 100) {
			
			String txt = new String(widget.getText());
			while(txt.length() > 0){
				char c = txt.charAt(0);
				((PC_GresTextEdit) edit).addKey(c);
				if(txt.length() == 1) break;
				txt = txt.substring(1);
			}			
			
			actionPerformed(edit, gui);
			gui.setFocus(edit);
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

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

}
