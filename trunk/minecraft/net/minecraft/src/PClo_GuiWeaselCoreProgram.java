package net.minecraft.src;



import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * gui for editing programmable gate's program
 * 
 * @author MightyPork
 */
@SuppressWarnings("javadoc")
public class PClo_GuiWeaselCoreProgram implements PC_IGresBase {

	protected PClo_WeaselPluginCore core;
	protected PC_GresWidget edit;
	protected PC_GresWidget txMsg;
	protected PC_GresWindow w;
	protected PC_GresWidget txRunning;
	protected PC_GresWidget btnPauseResume;
	protected PC_GresWidget btnLaunch;
	protected PC_GresWidget btnStop;
	protected PC_GresWidget btnRestart;

	protected String preUndo = "";


	/**
	 * prog gate GUI
	 * 
	 * @param core gate TE
	 */
	public PClo_GuiWeaselCoreProgram(PClo_WeaselPluginCore core) {
		this.core = core;
		preUndo = core.program;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		w = new PC_GresWindow(PC_Lang.tr("pc.gui.weasel.core.title"));
		w.setMinSize(380, 230);
		w.setAlignH(PC_GresAlign.STRETCH);
		w.setAlignV(PC_GresAlign.TOP);

		PC_GresWidget hg;

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresGap(4, 0));
		hg.add(new PC_GresButton("+").setId(103).setMinWidth(0).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.program")).setId(100).enable(false).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.status")).setId(101).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.settings")).setId(102).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresGap(4, 0));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.close")).setId(0).enable(true).setWidgetMargin(2));


		w.add(hg);

		PC_GresWidget mainHg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP);

		PC_GresWidget leftCol = new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH).setMinWidth(294).setWidgetMargin(1);

		leftCol.add(edit = new PC_GresTextEditMultiline(core.program, 290, 164, PC_GresHighlightHelper.weasel(core, core.getWeaselEngine()),
				PC_GresHighlightHelper.autoAdd).setWidgetMargin(2));
		leftCol.add(txMsg = new PC_GresLabelMultiline("Weasel status: " + (core.getError() == null ? "OK" : core.getError().replace("\n", " ")), 270)
				.setMinRows(1).setMaxRows(1).setWidgetMargin(2).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		mainHg.add(leftCol);

		PC_GresWidget rightCol = new PC_GresLayoutV().setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER).setMinWidth(60).setWidgetMargin(0);

		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.undoAll")).setId(1).setMinWidth(55).setWidgetMargin(2));
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.check")).setId(2).setMinWidth(55).setWidgetMargin(2));
		rightCol.add(btnLaunch = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.launch")).setId(3).setMinWidth(55).setWidgetMargin(2));
		rightCol.add(new PC_GresGap(0, 5));
		rightCol.add(txRunning = new PC_GresLabel("").setMinWidth(55).setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER));
		rightCol.add(btnPauseResume = new PC_GresButton("").setId(4).setMinWidth(55).setWidgetMargin(2));
		rightCol.add(btnRestart = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.restart")).setId(5).setMinWidth(55).setWidgetMargin(2));
		rightCol.add(btnStop = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.stop")).setId(6).setMinWidth(55).setWidgetMargin(2));
		mainHg.add(rightCol);

		w.add(mainHg);

		btnLaunch.enable(false);

		gui.add(w);

		txMsg.setFontRenderer(mod_PCcore.fontRendererSmall);

		actionPerformed(edit, gui);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

//		if (widget.getId() == 100) {
//			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreProgram(core));		
//			return;
//		}
		if (widget.getId() == 101) {
			core.setProgram(edit.getText());
			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreStatus(core));
			return;
		}
		if (widget.getId() == 102) {
			core.setProgram(edit.getText());
			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreSettings(core));
			return;
		}
		if (widget.getId() == 103) {
			core.setProgram(edit.getText());
			PC_Utils.openGres(getPlayer(), (this instanceof PClo_GuiWeaselCoreProgramBig) ? new PClo_GuiWeaselCoreProgram(core)
					: new PClo_GuiWeaselCoreProgramBig(core));
			return;
		}


		if (widget == edit) {
			core.setProgram(edit.getText());
			btnLaunch.enable(false);
		}

		//close
		if (widget.getId() == 0) {
			core.setProgram(edit.getText());
			gui.close();
			return;
		}

		// undo all
		if (widget.getId() == 1) {
			edit.setText(preUndo);
			txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgAllUndone"));
			core.setProgram(edit.getText());
			return;
		}

		// check
		if (widget.getId() == 2) {
			txMsg.setText("");
			try {
				core.checkProgramForErrors(edit.getText());
				txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgNoErrors"));
				btnLaunch.enable(true);

			} catch (Exception e) {
				txMsg.setText(e.getMessage());
				btnLaunch.enable(false);
			}
			return;

		}

		// launch
		if (widget.getId() == 3) {
			txMsg.setText("");
			try {
				core.setAndStartNewProgram(edit.getText());
				//core.world().notifyBlockChange(core.coord().x, core.coord().y, core.coord().z, mod_PClogic.weaselDevice.blockID);
				txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgLaunched"));
			} catch (Exception e) {
				txMsg.setText(e.getMessage());
			}
			return;
		}

		// pause/resume
		if (widget.getId() == 4) {
			core.paused ^= true;
			if (core.paused) {
				txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgPaused"));
			} else {
				txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgResumed"));
			}

			return;
		}

		// restart clear globals
		if (widget.getId() == 5) {
			core.restartDevice();
			txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgRestarted"));
			return;
		}

		// stop
		if (widget.getId() == 6) {
			core.stopProgram();
			txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgHalted"));

			return;
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
	public void updateTick(PC_IGresGui gui) {

		if (core.halted) {
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.halted"));
			btnPauseResume.enable(false);
			btnStop.enable(false);
			btnRestart.enable(true);
			return;
		}

		if (core.hasError()) {
			txMsg.text = core.getError().replace("\n", " ");
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.crashed"));
			btnPauseResume.enable(false);
			btnRestart.enable(true);
			btnStop.enable(true);
			return;
		}

		if (core.paused) {
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.paused"));
			btnPauseResume.setText(PC_Lang.tr("pc.gui.weasel.core.resume"));
			btnPauseResume.enable(true);
			btnRestart.enable(true);
			btnStop.enable(true);
			return;
		}

		if (core.getWeaselEngine().isProgramFinished) {
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.idle"));
			btnPauseResume.setText(PC_Lang.tr("pc.gui.weasel.core.pause"));
			btnPauseResume.enable(true);
			btnRestart.enable(true);
			btnStop.enable(true);
			btnStop.enable(!core.getWeaselEngine().isProgramFinished);
			return;
		}


		txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.running"));
		btnPauseResume.setText(PC_Lang.tr("pc.gui.weasel.core.pause"));
		btnPauseResume.enable(true);
		btnStop.enable(true);
		btnRestart.enable(true);
	}

}
