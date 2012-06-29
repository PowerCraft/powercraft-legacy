package net.minecraft.src;

import java.util.ArrayList;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

import net.minecraft.src.PC_GresTextEditMultiline.Keyword;
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
		
		ArrayList<Keyword> kw = new ArrayList<Keyword>();
		int cinput = 0x0000ee;
		int coperation = 0xff9900;
		int cnumber = 0xffff00;
		
		kw.add(new Keyword("L", cinput, false));
		kw.add(new Keyword("R", cinput, false));
		kw.add(new Keyword("B", cinput, false));
		kw.add(new Keyword("[+\\-*&|^\\*!?]", coperation, true));
		kw.add(new Keyword("[0-9]+", cnumber, true));
		
		kw.add(new Keyword("Math", 0x0000ee, false));
		
		w.add(edit = new PC_GresTextEditMultiline(tileEntity.programm, 200, 50,kw));
		w.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton("L").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("R").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("B").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("(").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton(")").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("||").setId(100).setMinWidth(0));
		hg.add(new PC_GresButton("&&").setId(100).setMinWidth(0));
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

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == buttonCancel) {
			gui.close();
		} else if (widget == buttonOK) {
			txError.setText("");
			if (tileEntity.checkProgram(edit.getText())) {
				tileEntity.setProgram(edit.getText());
				gui.close();
			}
		} else if (widget == edit) {
			txError.setText("");
			if(!tileEntity.checkProgram(edit.getText())){
				txError.setText(PC_Lang.tr("pc.gui.customGate.syntaxError"));
			}
		} else if (widget.getId() == 100) {
			
			String txt = new String(widget.getText());
			while(txt.length() > 0){
				char c = txt.charAt(0);
				((PC_GresTextEditMultiline) edit).addKey(c);
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
