package net.minecraft.src;


import java.util.List;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * GUI for changing radio device channel
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_GuiPulsar extends PC_GresBase {


	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget editDelay;
	private PC_GresWidget editHold;
	private PC_GresWidget txError, txConvDelay, txConvHold;

	private boolean errorDelay = false, errorHold = false;

	private PClo_TileEntityPulsar pulsar;

	private PC_GresCheckBox checkSilent;
	private int delay_ticks;
	private int hold_ticks;

	/**
	 * @param tep Pulsar Tile Entity
	 */
	public PClo_GuiPulsar(EntityPlayer player, TileEntity tep) {
		pulsar = (PClo_TileEntityPulsar)tep;
		delay_ticks = pulsar.delay;
		hold_ticks = pulsar.holdtime;
		this.player = player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		// window
		PC_GresWindow w = new PC_GresWindow(PC_Lang.tr("tile.PCloRedstonePulsar.name"));
		w.setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget hg, vg;

		// layout with the inputs
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.pulsar.delay")));
		vg.add(editDelay = new PC_GresTextEdit(PC_Utils.doubleToString(PC_Utils.ticksToSecs(delay_ticks)), 8, PC_GresInputType.UNSIGNED_FLOAT));
		vg.add(txConvDelay = new PC_GresLabelMultiline("", editDelay.getMinSize().x).setMinRows(2).setColor(PC_GresWidget.textColorEnabled, 0x606060));
		hg.add(vg);

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.pulsar.hold")));
		vg.add(editHold = new PC_GresTextEdit(PC_Utils.doubleToString(PC_Utils.ticksToSecs(hold_ticks)), 8, PC_GresInputType.UNSIGNED_FLOAT));
		vg.add(txConvHold = new PC_GresLabelMultiline("", editDelay.getMinSize().x).setMinRows(2).setColor(PC_GresWidget.textColorEnabled, 0x606060));
		hg.add(vg);

		w.add(hg);


		w.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));

		/*
		 * w.add(new PC_GresGap(0,3));
		 * w.add(new PC_GresImage(mod_PClogic.getImgDir()+"pulsar_hint.png", 0, 0, 131, 20));
		 * w.add(new PC_GresGap(0,3));
		 */

		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(checkSilent = new PC_GresCheckBox(PC_Lang.tr("pc.gui.pulsar.silent")).check(pulsar.silent));
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		gui.add(w);


		gui.setPausesGame(false);

		// refresh labels.
		actionPerformed(editHold, gui);
		actionPerformed(editDelay, gui);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {
			PC_Utils.setTileEntity(getPlayer(), pulsar, "delayTime", delay_ticks, 
					"holdTime", hold_ticks, 
					"silent", checkSilent.isChecked());
			gui.close();

		} else if (widget.getId() == 1) {
			gui.close();

		}



		if (widget == editDelay || widget == editHold) {

			try {
				double time = Double.valueOf(editDelay.getText());
				delay_ticks = PC_Utils.secsToTicks(time);

				errorDelay = (delay_ticks < 2) || (delay_ticks > 150000);

			} catch (NumberFormatException nfe) {

				errorDelay = true;

			} catch (NullPointerException npe) {

				errorDelay = true;

			}

			String conv;

			conv = "";
			if (!errorDelay) {
				conv += "= " + delay_ticks + " " + PC_Lang.tr("pc.gui.gate.delayer.ticks");
				if (delay_ticks >= 60 * 20) {
					conv += "\n= " + PC_Utils.formatTimeTicks(delay_ticks);
				}
			}
			txConvDelay.setText(conv);


			try {
				double time = Double.valueOf(editHold.getText());
				hold_ticks = PC_Utils.secsToTicks(time);

				errorHold = !((hold_ticks < delay_ticks - 1) && hold_ticks >= 1);

			} catch (NumberFormatException nfe) {

				errorHold = true;

			} catch (NullPointerException npe) {

				errorHold = true;
			}

			conv = "";
			if (!errorHold) {
				conv += "= " + hold_ticks + " " + PC_Lang.tr("pc.gui.gate.delayer.ticks");
				if (hold_ticks >= 60 * 20) {
					conv += "\n= " + PC_Utils.formatTimeTicks(hold_ticks);
				}
			}
			txConvHold.setText(conv);

		}

		if (errorDelay) {
			txError.setText(PC_Lang.tr("pc.gui.pulsar.errDelay"));
		} else if (errorHold) {
			txError.setText(PC_Lang.tr("pc.gui.pulsar.errHold"));
		} else {
			txError.setText("");
		}

		buttonOK.enable(!errorDelay && !errorHold);

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
