package net.minecraft.src;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.src.PC_GresTextEditMultiline.Keyword;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * gui for editing programmable gate's program
 * 
 * @author MightyPork
 */
public class PClo_GuiProgrammableGate implements PC_IGresBase {

	private PClo_TileEntityGate teg;
	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget edit;
	private PC_GresWidget txError;
	private PC_GresWindow win;


	/**
	 * prog gate GUI
	 * 
	 * @param tileEntity gate TE
	 */
	public PClo_GuiProgrammableGate(PClo_TileEntityGate tileEntity) {
		this.teg = tileEntity;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	private String shortenErrMsg(String err) {
		err = err.trim();

		err = err.replace("java.lang.", "");

		if (err.startsWith("Unexpected \"<EOF>\"")) {
			return "Expression is not closed.";
		}

		if (err.startsWith("Lexical error")) {
			err = err.replace("Lexical", "Syntax");
			err = err.replaceAll("\\.\\s.*$", ".");
			return err;
		}

		return err;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		win = new PC_GresWindow(PC_Lang.tr("tile.PCloLogicGate.programmable.name"));

		PC_GresWidget hg;

		ArrayList<Keyword> kw = new ArrayList<Keyword>();
		int colorInputVar = 0x00BB00;
		int colorOperator = 0xff9900;
		int colorNumber = 0xffff00;
		int colorConstant = 0x990099;
		int colorMathFunction = 0xff3030;
		int colorBracket = 0xff66ff;

		kw.add(new Keyword("L", colorInputVar, false));
		kw.add(new Keyword("R", colorInputVar, false));
		kw.add(new Keyword("B", colorInputVar, false));
		kw.add(new Keyword("[+\\-*&|^\\*!%<>=]", colorOperator, true));
		kw.add(new Keyword("[\\(\\)\\[\\]]", colorBracket, true));
		kw.add(new Keyword("[0-9]+[\\.]?[0-9]*", colorNumber, true));
		kw.add(new Keyword("0x[0-9a-fA-F]+", colorNumber, true));
		kw.add(new Keyword("0b[0-1]+", colorNumber, true));

		String[] jepkw = teg.evaluator.getKeywords();

		for (String str : jepkw) {
			kw.add(new Keyword(str, colorMathFunction, false));
		}

		String[] jepcs = teg.evaluator.getConstants();

		for (String str : jepcs) {
			kw.add(new Keyword(str, colorConstant, false));
		}

		win.add(edit = new PC_GresTextEditMultiline(teg.program, 270, 60, kw));
		win.add(txError = new PC_GresLabelMultiline("", 270).setColor(PC_GresWidget.textColorEnabled, 0x990000));



		Map<String, String> hintMap = new LinkedHashMap<String, String>();
		hintMap.put("L", "L");
		hintMap.put("B", "B");
		hintMap.put("R", "R");
		hintMap.put("(", "(");
		hintMap.put(")", ")");
		hintMap.put(",", ", ");
		hintMap.put("+", " + ");
		hintMap.put("-", " - ");
		hintMap.put("%", " % ");
		hintMap.put(" ", " ");
		hintMap.put(">", ">");
		hintMap.put("<", "<");
		hintMap.put("=", "=");
		hintMap.put("||", " || ");
		hintMap.put("&&", "&&");
		hintMap.put("!", "!");
		hintMap.put("not", "not(");
		hintMap.put("xor", "xor(");
		hintMap.put("and", "and(");
		hintMap.put("or", "or(");
		hintMap.put("nor", "nor(");
		hintMap.put("nxor", "nxor(");
		hintMap.put("odd", "odd(");
		hintMap.put("even", "even(");
		hintMap.put("sum", "sum(");
		hintMap.put("if", "if(");
		hintMap.put("sum", "sum(");
		hintMap.put("min", "min(");
		hintMap.put("max", "max(");
		hintMap.put("num", "num(");
		hintMap.put("bool", "bool(");
		hintMap.put("byte", "byte(");
		hintMap.put("bw.not", "bw.not(");
		hintMap.put("bw.or", "bw.or(");
		hintMap.put("bw.and", "bw.and(");
		hintMap.put("bw.xor", "bw.xor(");
		hintMap.put("<<", "bw.lshift(");
		hintMap.put(">>", "bw.rshift(");

		int widthCounter = 0;

		PC_GresLayoutV vg = new PC_GresLayoutV();

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
		for (Entry<String, String> entry : hintMap.entrySet()) {
			PC_GresWidget widget = new PC_GresButton(entry.getKey()).setButtonPadding(3, 4).setColor(PC_GresWidget.textColorEnabled, 0x404040).setColor(PC_GresWidget.textColorHover, 0x000000).setColor(PC_GresWidget.textColorActive, 0x0000ff)
					.setTag(entry.getValue()).setId(100).setWidgetMargin(1).setMinWidth(0);

			widthCounter += widget.calcSize().x + widget.widgetMargin;

			hg.add(widget);

			if (widthCounter >= 256) {
				widthCounter = 0;
				vg.add(hg);
				hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);
			}

		}
		if (hg.childs.size() > 0) vg.add(hg);
		win.add(vg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		win.add(hg);
		gui.add(win);

		actionPerformed(edit, gui);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		boolean edited = false;

		if (widget == buttonCancel) {
			gui.close();
		} else if (widget == buttonOK) {
			edited = true;
			txError.setText("");
			String err = teg.checkProgram(edit.getText());
			if (err == null) {
				teg.setProgram(edit.getText());
				teg.worldObj.scheduleBlockUpdate(teg.xCoord, teg.yCoord, teg.zCoord, teg.getBlockType().blockID, 1);
				gui.close();
			} else {
				txError.setText(shortenErrMsg(err));
			}
		} else if (widget == edit) {
			edited = true;
			txError.setText("");
			String err = teg.checkProgram(edit.getText());
			if (err != null) {
				txError.setText(shortenErrMsg(err));
			}
		} else if (widget.getId() == 100) {

			String txt = new String(widget.getTag());
			while (txt.length() > 0) {
				char c = txt.charAt(0);
				((PC_GresTextEditMultiline) edit).addKey(c);
				if (txt.length() == 1) break;
				txt = txt.substring(1);
			}
			edited = true;

			txError.setText("");
			String err = teg.checkProgram(edit.getText());
			if (err != null) {
				txError.setText(shortenErrMsg(err));
			}
			gui.setFocus(edit);
		}

		if (edited) win.calcSize();
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
