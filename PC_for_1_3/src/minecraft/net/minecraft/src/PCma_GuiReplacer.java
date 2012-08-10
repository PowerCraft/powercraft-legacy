package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;

import org.lwjgl.input.Keyboard;


/**
 * Block replacing machine GUI
 * 
 * @author COR19, Rapus, MightyPork
 * @copy (c) 2012
 */
public class PCma_GuiReplacer implements PC_IGresBase {

	private PCma_TileEntityReplacer teReplacer;

	private PC_GresWidget textedit[] = new PC_GresTextEdit[3];
	private PC_GresButton button[] = new PC_GresButton[2];
	private PC_GresLabel errorLabel;
	private PC_GresInventoryBigSlot slot;

	private boolean valid;

	private PC_GresCheckBox checkFrame;

	private List<Slot> lSlot = new ArrayList<Slot>();
	
	/**
	 * replacer gres gui
	 * 
	 * @param teReplacer replacer TE
	 * @param entityplayer player
	 */
	public PCma_GuiReplacer(PCma_TileEntityReplacer teReplacer, EntityPlayer entityplayer) {
		this.teReplacer = teReplacer;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(PC_Lang.tr("pc.gui.blockReplacer.title")).setWidthForInventory().setAlignH(PC_GresAlign.CENTER);

		PC_GresWidget hg;
		PC_GresWidget vg;

		hg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP);

		PC_GresWidget hg1;

		hg1 = new PC_GresLayoutH().setWidgetMargin(1).setAlignV(PC_GresAlign.CENTER);
		hg1.add(new PC_GresLabel("X"));
		hg1.add(textedit[0] = new PC_GresTextEdit("" + teReplacer.coordOffset.x, 3, PC_GresInputType.INT).setWidgetMargin(1));
		vg = new PC_GresLayoutV().setWidgetMargin(1);
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(44, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3)
				.setId(102).setWidgetMargin(0));
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(50, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3)
				.setId(101).setWidgetMargin(0));
		hg1.add(vg);
		hg.add(hg1);

		hg.add(new PC_GresGap(3, 0));

		hg1 = new PC_GresLayoutH().setWidgetMargin(1).setAlignV(PC_GresAlign.CENTER);
		hg1.add(new PC_GresLabel("Y"));
		hg1.add(textedit[1] = new PC_GresTextEdit("" + teReplacer.coordOffset.y, 3, PC_GresInputType.INT).setWidgetMargin(1));
		vg = new PC_GresLayoutV().setWidgetMargin(1);
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(44, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3)
				.setId(202).setWidgetMargin(0));
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(50, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3)
				.setId(201).setWidgetMargin(0));
		hg1.add(vg);
		hg.add(hg1);

		hg.add(new PC_GresGap(3, 0));

		hg1 = new PC_GresLayoutH().setWidgetMargin(1).setAlignV(PC_GresAlign.CENTER);
		hg1.add(new PC_GresLabel("Z"));
		hg1.add(textedit[2] = new PC_GresTextEdit("" + teReplacer.coordOffset.z, 3, PC_GresInputType.INT).setWidgetMargin(1));
		vg = new PC_GresLayoutV().setWidgetMargin(1);
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(44, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3)
				.setId(302).setWidgetMargin(0));
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(50, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3)
				.setId(301).setWidgetMargin(0));
		hg1.add(vg);
		hg.add(hg1);



		w.add(hg);

		// w.add(new PC_GresGap(0, 6));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(1);
		hg.add(errorLabel = (PC_GresLabel) new PC_GresLabel("").setWidgetMargin(1));
		errorLabel.setColor(PC_GresWidget.textColorEnabled, 0x990000);
		w.add(hg);

		w.add(slot = new PC_GresInventoryBigSlot(lSlot.get(0)));

		w.add(new PC_GresInventoryPlayer(true));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(checkFrame = new PC_GresCheckBox(PC_Lang.tr("pc.gui.blockReplacer.particleFrame")));
		checkFrame.check(teReplacer.aidEnabled);
		hg.add(button[1] = new PC_GresButton(PC_Lang.tr("pc.gui.ok")));
		w.add(hg);

		gui.add(w);

		w.calcChildPositions();
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == slot) {
			teReplacer.extraMeta = -1;
		} else if (widget == button[0]) {

			gui.close();

		} else if (widget == button[1]) {

			int x = Integer.parseInt(textedit[0].getText());
			int y = Integer.parseInt(textedit[1].getText());
			int z = Integer.parseInt(textedit[2].getText());

			teReplacer.coordOffset.setTo(x, y, z);
			teReplacer.aidEnabled = checkFrame.isChecked();

			gui.close();

		} else if (widget == textedit[0] || widget == textedit[1] || widget == textedit[2]) {
			valid = false;

			try {

				for (int count = 0; count <= 2; count++) {
					if (!textedit[count].getText().equals("") && !textedit[count].getText().equals("-")) {

						if (Math.abs(Integer.valueOf(textedit[count].getText())) > 16) {
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

			if (valid) {
				errorLabel.setText("");
				int x = Integer.parseInt(textedit[0].getText());
				int y = Integer.parseInt(textedit[1].getText());
				int z = Integer.parseInt(textedit[2].getText());

				teReplacer.coordOffset.setTo(x, y, z);
			}

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

				if (edit == null) {
					return;
				}

				number = edit.getText();
				try {
					num = Integer.parseInt(number);
				} catch (NumberFormatException e) {
					return;
				}

				if (id % 100 == 1) {
					num--;
				}
				if (id % 100 == 2) {
					num++;
				}

				if (num < -16) {
					num = -16;
				}
				if (num > 16) {
					num = 16;
				}

				edit.setText(num + "");
				actionPerformed(edit, gui);

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

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public List<Slot> getAllSlots(Container c) {
		lSlot.add(new Slot(teReplacer, 0, 0, 0));
		return lSlot;
	}

	@Override
	public boolean canShiftTransfer() {
		return true;
	}

}
