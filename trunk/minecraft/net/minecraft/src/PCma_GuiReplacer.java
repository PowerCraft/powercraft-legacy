package net.minecraft.src;

import java.util.ArrayList;

import net.minecraft.src.PC_GresMultiTextEdit.Keyword;
import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;
import org.lwjgl.input.Keyboard;


public class PCma_GuiReplacer implements PC_IGresBase {

	private PCma_TileEntityReplacer teReplacer;

	private PC_GresTextEdit textedit[] = new PC_GresTextEdit[3];
	private PC_GresButton button[] = new PC_GresButton[2];
	private PC_GresLabel errorLabel;

	private boolean valid;


	public PCma_GuiReplacer(PCma_TileEntityReplacer teReplacer) {
		this.teReplacer = teReplacer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(225, 50, PC_Lang.tr("pc.gui.blockReplacer.title")).setAlignH(PC_GresAlign.STRETCH);

		PC_GresWidget hg;
		PC_GresWidget vg;

		PC_GresWidget lblx, lbly, lblz;



		hg = new PC_GresLayoutH();
		hg.add(lblx = new PC_GresLabel("X:"));
		hg.add(textedit[0] = (PC_GresTextEdit) new PC_GresTextEdit("" + teReplacer.coordOffset[0], 4, PC_GresInputType.INT).setMinWidth(100));
		hg.add(new PC_GresButton("-").setId(101).setMinWidth(16));
		hg.add(new PC_GresButton("+").setId(102).setMinWidth(16));
		w.add(hg);

		hg = new PC_GresLayoutH();
		hg.add(lbly = new PC_GresLabel("Y:"));
		hg.add(textedit[1] = (PC_GresTextEdit) new PC_GresTextEdit("" + teReplacer.coordOffset[1], 4, PC_GresInputType.INT).setMinWidth(100));
		hg.add(new PC_GresButton("-").setId(201).setMinWidth(16));
		hg.add(new PC_GresButton("+").setId(202).setMinWidth(16));
		w.add(hg);

		hg = new PC_GresLayoutH();
		hg.add(lblz = new PC_GresLabel("Z:"));
		hg.add(textedit[2] = (PC_GresTextEdit) new PC_GresTextEdit("" + teReplacer.coordOffset[2], 4, PC_GresInputType.INT).setMinWidth(100));
		hg.add(new PC_GresButton("-").setId(301).setMinWidth(16));
		hg.add(new PC_GresButton("+").setId(302).setMinWidth(16));
		w.add(hg);

		int labelmaxw = 0;
		labelmaxw = Math.max(labelmaxw, lblx.getMinSize().x);
		labelmaxw = Math.max(labelmaxw, lbly.getMinSize().x);
		labelmaxw = Math.max(labelmaxw, lblz.getMinSize().x);

		lblx.setMinWidth(labelmaxw);
		lbly.setMinWidth(labelmaxw);
		lblz.setMinWidth(labelmaxw);

		w.add(new PC_GresGap(0, 6));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(errorLabel = new PC_GresLabel(""));
		errorLabel.setColor(PC_GresWidget.textColorEnabled, 0x990000);
		w.add(hg);


		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(button[0] = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")));
		hg.add(button[1] = new PC_GresButton(PC_Lang.tr("pc.gui.ok")));
		w.add(hg);

		ArrayList<Keyword> keywordlist = new ArrayList<Keyword>();
		keywordlist.add(new Keyword("if", 0xff00ff00));
		keywordlist.add(new Keyword("then", 0xff00ff00));
		keywordlist.add(new Keyword("else", 0xff00ff00));
		keywordlist.add(new Keyword("end", 0xff00ff00));
		w.add(new PC_GresMultiTextEdit("", 10, 60, keywordlist));
		
		gui.add(w);
		w.calcChildPositions();
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == button[0]) {

			gui.close();

		} else if (widget == button[1]) {

			for (int count = 0; count <= 2; count++)
				teReplacer.coordOffset[count] = Integer.valueOf(textedit[count].getText());
			gui.close();

		} else if (widget == textedit[0] || widget == textedit[1] || widget == textedit[2]) {
			valid = false;

			try {

				for (int count = 0; count <= 2; count++) {
					if (!textedit[count].getText().equals("") && !textedit[count].getText().equals("-")) {

						if (Integer.valueOf(textedit[count].getText()) < -16 || Integer.valueOf(textedit[count].getText()) > 16) {
							errorLabel.setText(PC_Lang.tr("pc.gui.blockReplacer.errWrongValue"));
							button[1].enable(false);
							return;
						} else {
							if (Integer.valueOf(textedit[count].getText()) != 0) {
								valid = true;
							}
						}

					} else {
						errorLabel.setText(PC_Lang.tr("pc.gui.blockReplacer.errWrongValue"));
						button[1].enable(false);
						return;
					}
				}

			} catch (NumberFormatException nfe) {
				valid = false;
			}

			if (!valid) {
				errorLabel.setText(PC_Lang.tr("pc.gui.blockReplacer.err3zeros"));
			}

			if (valid) errorLabel.setText("");

			button[1].enable(valid);
		} else {

			if (widget instanceof PC_GresButton) {
				int id = widget.getId();
				String number;
				PC_GresWidget edit = null;
				int num;

				if (id == 101 || id == 102) {
					edit = textedit[0];
				} else if (id == 201 || id == 202) {
					edit = textedit[1];
				} else if (id == 301 || id == 302) {
					edit = textedit[2];
				}

				if (edit == null) return;

				number = edit.getText();
				try {
					num = Integer.parseInt(number);
				} catch (NumberFormatException e) {
					return;
				}

				if (id % 100 == 1) num--;
				if (id % 100 == 2) num++;

				if (num < -16) num = -16;
				if (num > 16) num = 16;

				edit.setText(num + "");

			}

		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(button[1], gui);
	}

}
