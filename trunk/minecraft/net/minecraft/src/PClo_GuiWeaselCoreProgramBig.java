package net.minecraft.src;


import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * gui for editing programmable gate's program
 * 
 * @author MightyPork
 */
public class PClo_GuiWeaselCoreProgramBig extends PClo_GuiWeaselCoreProgram {

	private PC_GresWidget btnBack;

	/**
	 * prog gate GUI
	 * 
	 * @param core gate TE
	 */
	public PClo_GuiWeaselCoreProgramBig(PClo_WeaselPluginCore core) {
		super(core);
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		w = new PC_GresWindow(PC_Lang.tr("pc.gui.weasel.core.title"));
		w.padding.setTo(6, 8);
		w.setMinSize(0, 0);
		w.setAlignH(PC_GresAlign.STRETCH);
		w.setAlignV(PC_GresAlign.TOP);

		PC_GresWidget mainHg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP).setWidgetMargin(0);

		PC_GresWidget leftCol = new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH).setMinWidth(330).setWidgetMargin(1);

		leftCol.add(edit = new PC_GresTextEditMultiline(core.program, 10, 195, PC_GresHighlightHelper.weasel(core, core.getWeaselEngine()),
				PC_GresHighlightHelper.autoAdd).setWidgetMargin(2));
		leftCol.add(txMsg = new PC_GresLabelMultiline("Weasel status: " + (core.getError() == null ? "OK" : core.getError().replace("\n", " ")), 270)
				.setMinRows(1).setMaxRows(1).setWidgetMargin(1).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		mainHg.add(leftCol);

		mainHg.add(new PC_GresGap(0, 0).setWidgetMargin(3));

		PC_GresWidget rightCol = new PC_GresLayoutV().setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.STRETCH).setMinWidth(48).setWidgetMargin(2);
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.close")).setButtonPadding(3, 3).setId(0).setWidgetMargin(2).setMinWidth(0));
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.back")).setButtonPadding(3, 3).setId(103).setWidgetMargin(2));
		rightCol.add(new PC_GresGap(0, 3).setMinWidth(0));
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.undoAll")).setButtonPadding(3, 3).setId(1).setWidgetMargin(2).setMinWidth(0));
		rightCol.add(new PC_GresGap(0, 3).setMinWidth(0));
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.check")).setButtonPadding(3, 3).setId(2).setWidgetMargin(2).setMinWidth(0));
		rightCol.add(btnLaunch = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.launch")).setButtonPadding(3, 3).setId(3).setWidgetMargin(2)
				.setMinWidth(0));
		rightCol.add(new PC_GresGap(0, 5));
		rightCol.add(txRunning = new PC_GresLabel("").setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER));
		rightCol.add(btnPauseResume = new PC_GresButton("").setButtonPadding(3, 3).setId(4).setWidgetMargin(2).setMinWidth(0));
		rightCol.add(btnRestart = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.restart")).setButtonPadding(3, 3).setId(5).setWidgetMargin(2)
				.setMinWidth(0));
		rightCol.add(btnStop = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.stop")).setButtonPadding(3, 3).setId(6).setWidgetMargin(2)
				.setMinWidth(0));
		mainHg.add(rightCol);

		w.add(mainHg);

		btnLaunch.enable(false);

		gui.add(w);

		txMsg.setFontRenderer(mod_PCcore.fontRendererSmall);

		actionPerformed(edit, gui);

	}
}
