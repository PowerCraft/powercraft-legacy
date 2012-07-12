package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresTextEditMultiline.Keyword;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;
import weasel.Compiler;


/**
 * gui for editing programmable gate's program
 * 
 * @author MightyPork
 */
public class PClo_GuiProgrammableGate implements PC_IGresBase {

	private PClo_TileEntityGate teg;
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
		int colorLangFunction = 0xff0000;
		int colorBracket = 0xff66ff;
		int colorComments = 0x337ca7;
		int colorString = 0xff3330;
		
		kw.add(new Keyword("[+\\-*/&|^\\*!%<>=]", colorOperator, true));

		kw.add(new Keyword("[\\(\\)\\[\\];]", colorBracket, true));

		kw.add(new Keyword("[0-9]+[\\.]?[0-9]*", colorNumber, true));
		kw.add(new Keyword("0x[0-9a-fA-F]+", colorNumber, true));
		kw.add(new Keyword("0b[0-1]+", colorNumber, true));

		kw.add(new Keyword("#", "\n", colorComments, false));
		kw.add(new Keyword("//", "\n", colorComments, false));
		kw.add(new Keyword("/*", "*/", colorComments, false));
		kw.add(new Keyword("\"", "\"", colorString, false));
		kw.add(new Keyword("'", "'", colorString, false));
		
		// gate functions
		List<String> gateFunc = teg.getProvidedFunctionNames();
		for (String str : gateFunc) {
			kw.add(new Keyword(str, colorMathFunction, false));
		}

		// gate input variables
		List<String> gateVar = teg.getProvidedVariableNames();
		for (String str : gateVar) {
			kw.add(new Keyword(str, colorInputVar, false));
		}

		// math functions
		List<String> jepFunc = Compiler.parserFunctions;

		for (String str : jepFunc) {
			kw.add(new Keyword(str, colorMathFunction, false));
		}

		// math constants
		List<String> jepVar = Compiler.parserConstants;

		for (String str : jepVar) {
			kw.add(new Keyword(str, colorConstant, false));
		}

		// lang functions
		List<String> weaselFunc = Compiler.langKeywords;

		for (String str : weaselFunc) {
			kw.add(new Keyword(str, colorLangFunction, false));
		}

		win.add(edit = new PC_GresTextEditMultiline(teg.program, 280, 154, kw));
		win.add(txError = new PC_GresLabelMultiline("Weasel status: " + (teg.getWeaselError() == null ? "OK" : teg.getWeaselError()), 270).setMinRows(2).setMaxRows(2).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.programGate.close")).setId(0));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.programGate.saveAndClose")).setId(1));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.programGate.checkErrors")).setId(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.programGate.startAndClose")).setId(3));
		win.add(hg);
		gui.add(win);

		actionPerformed(edit, gui);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {


		if (widget.getId() == 0) {
			gui.close();

		} else if (widget.getId() == 1) {
			teg.setProgram(edit.getText());
			gui.close();

		} else if (widget.getId() == 2) {
			txError.setText("");
			try {
				teg.checkProgramForErrors(edit.getText());
				txError.setText("Program has no errors.");
			} catch (Exception e) {
				txError.setText(e.getMessage());
			}

		} else if (widget.getId() == 3) {
			txError.setText("");
			try {
				teg.setAndStartNewProgram(edit.getText());
				teg.worldObj.notifyBlockChange(teg.xCoord, teg.yCoord, teg.zCoord, teg.getBlockType().blockID);
				gui.close();
			} catch (Exception e) {
				txError.setText(e.getMessage());
			}
		}

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

}
