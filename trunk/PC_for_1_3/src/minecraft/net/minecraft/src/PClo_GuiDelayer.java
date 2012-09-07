package net.minecraft.src;


import java.util.List;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * Gui for delayer and repeater gates.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_GuiDelayer extends PC_GresBase {

	private PClo_TileEntityGate gateTE;

	private int ticks;
	private boolean error = false;
	private String errMsg = "";

	private int delayer_type;
	/** fifo index */
	public static final int FIFO = 1;
	/** hold index */
	public static final int HOLD = 0;

	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget edit;
	private PC_GresWidget txConverted;
	private PC_GresWidget txError;

	/**
	 * @param tep Gate tile entity
	 * @param type 0 = HOLD, 1 = FIFO
	 */
	public PClo_GuiDelayer(EntityPlayer player, TileEntity tep) {
		gateTE = (PClo_TileEntityGate)tep;
		ticks = gateTE.gateType == FIFO ? gateTE.getDelayBufferLength() : gateTE.repeaterGetHoldTime();
		delayer_type = gateTE.gateType;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		String title = "";
		if (delayer_type == FIFO) {
			title = PC_Lang.tr("tile.PCloLogicGate.buffer.name");
		}
		if (delayer_type == HOLD) {
			title = PC_Lang.tr("tile.PCloLogicGate.slowRepeater.name");
		}

		// window
		PC_GresWindow w = new PC_GresWindow(title);
		w.setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget hg;
		PC_GresWidget vg;

		// layout with the input
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.gate.delay")));
		vg.add(edit = new PC_GresTextEdit(PC_Utils.floatToString(ticks * 0.05F), 8, PC_GresInputType.SIGNED_FLOAT).setMinWidth(120));
		vg.add(txConverted = new PC_GresLabelMultiline("", 120).setMinRows(2).setColor(PC_GresWidget.textColorEnabled, 0x606060));
		w.add(vg);


		// labels
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));
		w.add(hg);


		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		gui.add(w);


		gui.setPausesGame(false);

		// refresh labels.
		actionPerformed(edit, gui);

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {

			PC_Utils.setTileEntity(PC_Utils.mc().thePlayer, gateTE, "ticks", delayer_type, ticks);

			gui.close();

		} else if (widget.getId() == 1) {
			gui.close();
		}

		if (widget == edit) {
			try {

				double time = Double.parseDouble(edit.getText());

				ticks = PC_Utils.secsToTicks(time);

				error = (ticks < 2) || (ticks > 37000);
				errMsg = "pc.gui.gate.delayer.errRange";

				buttonOK.enabled = !error;

			} catch (NumberFormatException nfe) {

				buttonOK.enabled = false;
				error = true;
				errMsg = "pc.gui.gate.delayer.errNumFormat";

			} catch (NullPointerException npe) {

				buttonOK.enabled = false;
				error = true;

				errMsg = "pc.gui.gate.delayer.errNumFormat";

			}

			if (!error) {
				errMsg = "";
			}

			txError.setText(PC_Lang.tr(errMsg));
			String conv = "";
			if (!error) {
				conv += "= " + ticks + " " + PC_Lang.tr("pc.gui.gate.delayer.ticks");
				if (ticks >= 60 * 20) {
					conv += "\n= " + PC_Utils.formatTimeTicks(ticks);
				}
			}
			txConverted.setText(conv);

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
