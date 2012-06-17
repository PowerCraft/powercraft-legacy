package net.minecraft.src;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;

/**
 * Gui for delayer and repeater gates.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_GuiDelayer implements PC_IGresBase {

	private PClo_TileEntityGate gateTE;

	private int ticks;
	private boolean error = false;
	private String errMsg = "";

	private boolean delayer_type;
	private static final boolean FIFO = true, HOLD = false;

	@SuppressWarnings("unused")
	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget edit;
	private PC_GresWidget txConverted;
	private PC_GresWidget txError;

	/**
	 * @param tep Gate tile entity
	 * @param fifo is the delayer of type FIFO (buffered)?
	 */
	public PClo_GuiDelayer(PClo_TileEntityGate tep, boolean fifo) {
		gateTE = tep;
		ticks = fifo ? gateTE.getDelayBufferLength() : gateTE.repeaterGetHoldTime();
		delayer_type = fifo;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		String title = "";
		if (delayer_type == FIFO) title = PC_Lang.tr("tile.PCloLogicGate.buffer.name");
		if (delayer_type == HOLD) title = PC_Lang.tr("tile.PCloLogicGate.slowRepeater.name");

		//window
		PC_GresWindow w = new PC_GresWindow(title);
		w.setAlignH(PC_GresAlign.STRETCH);
		PC_GresWidget hg;
		
		// layout with the input
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.gate.delay")).setMinWidth(120));
		w.add(hg);		

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(edit = new PC_GresTextEdit(PC_Utils.floatToString(ticks * 0.05F), 8, PC_GresInputType.SIGNED_FLOAT).setMinWidth(120));
		w.add(hg);
		
		
		// labels
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));
		w.add(hg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(txConverted = new PC_GresLabelMultiline("", 120).setColor(PC_GresWidget.textColorEnabled, 0x606060));
		w.add(hg);
		
		
		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		gui.add(w);
		

		gui.setPausesGame(true);

		// refresh labels.
		actionPerformed(edit, gui);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 0) {

			if (delayer_type == FIFO) {
				gateTE.bufferResize(ticks);
			} else if (delayer_type == HOLD) {
				gateTE.setRepeaterHoldTime(ticks);
			}

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

			if (!error) errMsg = "";

			txError.setText(PC_Lang.tr(errMsg));
			String conv = "";
			conv += "= " + ticks + " " + PC_Lang.tr("pc.gui.gate.delayer.ticks");
			conv += "\n";
			if(ticks >= 60*20) conv += "= "+PC_Utils.formatTimeTicks(ticks);
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
