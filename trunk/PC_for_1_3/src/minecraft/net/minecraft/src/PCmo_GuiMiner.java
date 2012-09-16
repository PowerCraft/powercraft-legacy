package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresTextEditMultiline.Keyword;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;
import net.minecraft.src.PCnt_NetManager.NetworkMember;
import net.minecraft.src.PCnt_NetManager.WeaselNetwork;

import org.lwjgl.input.Keyboard;

import weasel.obj.WeaselString;


/**
 * Digital Workbench
 * 
 * @author MightyPork
 */
public class PCmo_GuiMiner extends PC_GresBase {

	private PCmo_EntityMiner miner;

	protected String preUndo = "";
	
	private static int lastPanel = 0;



	private PC_GresWindow w;

	private PC_GresWidget vgProgram, vgSettings, vgCargo, vgTerm;

	public PCmo_GuiMiner(EntityPlayer player, TileEntity te){//PCmo_EntityMiner miner) {
		this.miner = null;
		this.player = player;
		preUndo = miner.brain.program;
	}

	private int panelShown = 0;
	private PC_GresWidget edProgram;
	private PC_GresWidget panelwrap;
	private PC_GresWidget btnLaunch;
	private PC_GresWidget btnStop;
	private PC_GresWidget btnRestart;
	private PC_GresWidget btnPauseResume;	
	private PC_GresWidget txRunning;
	private PC_GresWidget txMsg;

	private PC_GresInventory cargoInv;
	private PC_GresInventoryPlayer playerInv;

	private PC_GresWidget btp;
	private PC_GresWidget bts;
	private PC_GresWidget btc;
	

	private PC_GresCheckBox checkBridge;
	private PC_GresCheckBox checkMining;
	private PC_GresCheckBox checkLava;
	private PC_GresCheckBox checkWater;
	private PC_GresCheckBox checkAir;
	private PC_GresCheckBox checkKeepFuel;
	private PC_GresCheckBox checkTorchFloor;
	private PC_GresCheckBox checkTorch;
	private PC_GresCheckBox checkCompress;
	private PC_GresCheckBox checkCobble;

	private PC_GresTextEditMultiline edTerm;

	private PC_GresWidget edTermInput;

	private PC_GresWidget btnTermOk;

	private PC_GresWidget btt;

	private PC_GresInventoryBigSlot[] xtalInv = new PC_GresInventoryBigSlot[8];

	private PC_SlotSelective[] xtalSlot = new PC_SlotSelective[8];


	private void openPanel(int panel) {
		vgSettings.setVisible(false);
		vgCargo.setVisible(false);
		vgProgram.setVisible(false);
		vgTerm.setVisible(false);
		btp.enable(true);
		bts.enable(true);
		btc.enable(true);
		btt.enable(true);
		miner.updateLevel();

		switch (panel) {
			case 0:
				//cargoInv.removeAllSlots();
				for(int i=0; i<8; i++) {
					xtalInv[i].setSlot(null);
				}
				btp.enable(false);
				playerInv.hideSlots();
				playerInv.removeAll();
				w.setText(PC_Lang.tr("pc.gui.miner.program.title"));
				vgProgram.setVisible(true);
				ArrayList<Keyword> kw = PC_GresHighlightHelper.weasel(miner.brain, miner.brain.engine);
				
				List<String> funcnames = miner.getLibraryFunctionNames();
				
				for(String fn:funcnames) {
					kw.add(new Keyword(fn, 0x00d6b7));
				}
				((PC_GresTextEditMultiline) edProgram).setKeywords(kw);
				break;

			case 1:
				//cargoInv.removeAllSlots();
				for(int i=0; i<8; i++) {
					xtalInv[i].setSlot(null);
				}
				btt.enable(false);
				playerInv.hideSlots();
				playerInv.removeAll();
				w.setText(PC_Lang.tr("pc.gui.miner.terminal.title"));
				vgTerm.setVisible(true);
				break;

			case 2:
				for(int i=0; i<8; i++) {
					xtalInv[i].setSlot(null);
				}
				checkBridge.enable(miner.st.level >= PCmo_EntityMiner.LBRIDGE);
				checkLava.enable(miner.st.level >= PCmo_EntityMiner.LLAVA);
				checkWater.enable(miner.st.level >= PCmo_EntityMiner.LWATER);
				checkAir.enable(miner.st.level >= PCmo_EntityMiner.LAIR);
				checkCompress.enable(miner.st.level >= PCmo_EntityMiner.LCOMPRESS);
				checkTorchFloor.enable(miner.st.level >= PCmo_EntityMiner.LTORCH);
				checkTorch.enable(miner.st.level >= PCmo_EntityMiner.LTORCH);
				checkCobble.enable(miner.st.level >= PCmo_EntityMiner.LCOBBLE);
				//cargoInv.removeAllSlots();
				bts.enable(false);
				playerInv.hideSlots();
				playerInv.removeAll();
				w.setText(PC_Lang.tr("pc.gui.miner.settings.title"));
				vgSettings.setVisible(true);

				checkMining.check(miner.cfg.miningEnabled);
				checkBridge.check(miner.cfg.bridgeEnabled);
				checkAir.check(miner.cfg.airFillingEnabled);
				checkLava.check(miner.cfg.lavaFillingEnabled);
				checkWater.check(miner.cfg.waterFillingEnabled);
				checkCompress.check(miner.cfg.compressBlocks);
				checkCobble.check(miner.cfg.cobbleMake);
				checkKeepFuel.check(miner.cfg.keepAllFuel);
				checkTorch.check(miner.cfg.torches);
				checkTorchFloor.check(miner.cfg.torchesOnlyOnFloor);
				break;

			case 3:
				for(int i=0; i<8; i++) {
					xtalInv[i].setSlot(xtalSlot[i]);
				}
				btc.enable(false);
				//cargoInv.fillWithSlots(miner.cargo, 11, 5);
				playerInv.addedToWidget();
				w.setText(PC_Lang.tr("pc.gui.miner.cargo.title"));
				vgCargo.setVisible(true);
				break;
		}
		w.calcChildPositions();
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		w = new PC_GresWindow("hey");

		PC_GresWidget mhg, vg;

		PC_GresWidget h = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		h.add(btp = new PC_GresButton(PC_Lang.tr("pc.gui.miner.program")).setButtonPadding(3, 3).setId(100).setWidgetMargin(2).setMinWidth(0));
		h.add(btt = new PC_GresButton(PC_Lang.tr("pc.gui.miner.terminal")).setButtonPadding(3, 3).setId(101).setWidgetMargin(2).setMinWidth(0));
		h.add(bts = new PC_GresButton(PC_Lang.tr("pc.gui.miner.settings")).setButtonPadding(3, 3).setId(102).setWidgetMargin(2).setMinWidth(0));
		h.add(btc = new PC_GresButton(PC_Lang.tr("pc.gui.miner.cargo")).setButtonPadding(3, 3).setId(103).setWidgetMargin(2).setMinWidth(0));
		h.add(new PC_GresGap(0, 0).setMinWidth(0));
		h.add(new PC_GresButton(PC_Lang.tr("pc.gui.close")).setButtonPadding(3, 3).setId(0).setWidgetMargin(2).setMinWidth(0));
		w.add(h);
		
		panelwrap = new PC_GresLayoutV().setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER).setWidgetMargin(0);


		// Program
		vgProgram = new PC_GresLayoutV().setWidgetMargin(0).setAlignV(PC_GresAlign.TOP);
		vgProgram.setAlignH(PC_GresAlign.RIGHT);

		mhg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP).setWidgetMargin(0);

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH).setAlignV(PC_GresAlign.TOP).setMinWidth(260).setWidgetMargin(1);

		ArrayList<Keyword> kw = PC_GresHighlightHelper.weasel(miner.brain, miner.brain.engine);
	
		List<String> funcnames = miner.getLibraryFunctionNames();
		
		for(String fn:funcnames) {
			kw.add(new Keyword(fn, 0x00d6b7));
		}
		
		vg.add(edProgram = new PC_GresTextEditMultiline(miner.brain.program, 10, 170, kw,
				PC_GresHighlightHelper.autoAdd).setWidgetMargin(2));
		vg.add(txMsg = new PC_GresLabelMultiline("Weasel status: " + (!miner.brain.hasError() ? "OK" : miner.brain.getError().replace("\n", " ")), 250)
				.setMinRows(1).setMaxRows(1).setWidgetMargin(1).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		mhg.add(vg);

		mhg.add(new PC_GresGap(0, 0).setWidgetMargin(3));

		
		vg = new PC_GresLayoutV().setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.STRETCH).setMinWidth(48).setWidgetMargin(2);
		vg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.undoAll")).setButtonPadding(3, 3).setId(1).setWidgetMargin(2).setMinWidth(0));
		vg.add(new PC_GresGap(0, 0).setMinWidth(0));
		vg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.check")).setButtonPadding(3, 3).setId(2).setWidgetMargin(2).setMinWidth(0));
		vg.add(btnLaunch = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.launch")).setButtonPadding(3, 3).setId(3).setWidgetMargin(2)
				.setMinWidth(0));
		vg.add(new PC_GresGap(0, 2));
		vg.add(txRunning = new PC_GresLabel("").setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER));
		vg.add(btnPauseResume = new PC_GresButton("").setButtonPadding(3, 3).setId(4).setWidgetMargin(2).setMinWidth(0));
		vg.add(btnRestart = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.restart")).setButtonPadding(3, 3).setId(5).setWidgetMargin(2)
				.setMinWidth(0));
		vg.add(btnStop = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.stop")).setButtonPadding(3, 3).setId(6).setWidgetMargin(2)
				.setMinWidth(0));
		
		mhg.add(vg);

		vgProgram.add(mhg);

		
		// Settings
		vgSettings = new PC_GresLayoutV().setWidgetMargin(0).setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER);

		mhg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP).setWidgetMargin(0);

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT).setMinWidth(100).setWidgetMargin(1);

		vg.add(checkMining = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.mining")));
		vg.add(checkBridge = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.bridge")));
		vg.add(checkAir = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.airFill")));
		vg.add(checkLava = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.lavaFill")));
		vg.add(checkWater = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.waterFill")));
		vg.add(checkCompress = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.compress")));
		vg.add(checkCobble = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.makeCobble")));
		vg.add(checkKeepFuel = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.keepFuel")));
		vg.add(checkTorch = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.torchPlacing")));
		vg.add(checkTorchFloor = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.torchesOnlyOnFloor")));
		
		
		
		
		
		mhg.add(vg);

		vgSettings.add(mhg);
		
		
		
		vgTerm = new PC_GresLayoutV().setWidgetMargin(0).setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER);

		vgTerm.add(edTerm = new PC_GresTextEditMultiline(miner.brain.termText, 240, 130));

		edTerm.enabled = false;
		edTerm.setFgColor(0x00ee00);
		edTerm.setBgColor(0x000000);
		edTerm.scrollToBottom();
		
		PC_GresWidget hg;
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.STRETCH).setWidgetMargin(0);
		hg.add(edTermInput = new PC_GresTextEdit("", 26, PC_GresInputType.TEXT).setWidgetMargin(1).setMinWidth(199));
		edTermInput.setColor(PC_GresWidget.textColorEnabled, 0x009900);
		edTermInput.setColor(PC_GresWidget.textColorDisabled, 0x009900);
		edTermInput.setColor(PC_GresWidget.textColorClicked, 0x00ff00);
		edTermInput.setColor(PC_GresWidget.textColorHover, 0x00ff00);
		hg.add(btnTermOk = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setMinWidth(40).setWidgetMargin(1));
		vgTerm.add(hg);
		

		// Cargo
		vgCargo = new PC_GresLayoutH().setWidgetMargin(0).setAlignV(PC_GresAlign.TOP);
		vgCargo.setAlignH(PC_GresAlign.CENTER);
		
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(6);

		vg.add(xtalInv[0] = new PC_GresInventoryBigSlot(xtalSlot[0] 
				= new PC_SlotSelective(miner.xtals, 0, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(0)))));
		vg.add(xtalInv[1] = new PC_GresInventoryBigSlot(xtalSlot[1] 
				= new PC_SlotSelective(miner.xtals, 1, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(1)))));
		vg.add(xtalInv[2] = new PC_GresInventoryBigSlot(xtalSlot[2] 
				= new PC_SlotSelective(miner.xtals, 2, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(2)))));
		vg.add(xtalInv[3] = new PC_GresInventoryBigSlot(xtalSlot[3] 
				= new PC_SlotSelective(miner.xtals, 3, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(3)))));

		vgCargo.add(vg);
		

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(1);

		vg.add(cargoInv = new PC_GresInventory(11, 5));
		vg.add(playerInv = new PC_GresInventoryPlayer(true));

		vgCargo.add(vg);
		
		vgCargo.add(new PC_GresGap(2, 0));
		
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.CENTER).setWidgetMargin(6);

		vg.add(xtalInv[7] = new PC_GresInventoryBigSlot(xtalSlot[7] 
				= new PC_SlotSelective(miner.xtals, 7, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(7)))));
		vg.add(xtalInv[6] = new PC_GresInventoryBigSlot(xtalSlot[6] 
				= new PC_SlotSelective(miner.xtals, 6, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(6)))));
		vg.add(xtalInv[5] = new PC_GresInventoryBigSlot(xtalSlot[5] 
				= new PC_SlotSelective(miner.xtals, 5, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(5)))));
		vg.add(xtalInv[4] = new PC_GresInventoryBigSlot(xtalSlot[4] 
				= new PC_SlotSelective(miner.xtals, 4, 0, 0).
				setBackgroundStack(new ItemStack(mod_PCcore.powerCrystal,1,miner.xtals.getCrystalTypeForSlot(4)))));

		vgCargo.add(vg);
		
		

		vgProgram.setVisible(true);
		vgSettings.setVisible(false);
		vgCargo.setVisible(false);
		vgTerm.setVisible(false);

		panelwrap.add(vgProgram);
		panelwrap.add(vgSettings);
		panelwrap.add(vgTerm);
		panelwrap.add(vgCargo);


		w.add(panelwrap);

		w.add(new PC_GresGap(0, 2));

		gui.add(w);

		// init the elements.
		btnLaunch.enable(false);

		txMsg.setFontRenderer(mod_PCcore.fontRendererSmall);

		actionPerformed(edProgram, gui);
		
		openPanel(1);
		openPanel(2);
		openPanel(3);
		openPanel(0);
		
		openPanel(lastPanel);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		miner.cargo.closeChest();
		miner.st.programmingGuiOpen = false;

	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if(widget instanceof PC_GresCheckBox) {
			miner.cfg.miningEnabled = checkMining.isChecked();
			miner.cfg.bridgeEnabled = checkBridge.isChecked();
			miner.cfg.lavaFillingEnabled = checkLava.isChecked();
			miner.cfg.waterFillingEnabled = checkWater.isChecked();
			miner.cfg.airFillingEnabled = checkAir.isChecked();
			miner.cfg.keepAllFuel = checkKeepFuel.isChecked();
			miner.cfg.torches = checkTorch.isChecked();
			miner.cfg.torchesOnlyOnFloor = checkTorchFloor.isChecked();
			miner.cfg.compressBlocks = checkCompress.isChecked();	
			miner.cfg.cobbleMake = checkCobble.isChecked();					
		}
		
		if(widget.getId()==100)
		{
			openPanel(0);
			lastPanel = 0;
			return;
		}
		if(widget.getId()==101)
		{
			openPanel(1);
			lastPanel = 1;
			return;
		}
		if(widget.getId()==102)
		{
			openPanel(2);
			lastPanel = 2;
			return;
		}
		if(widget.getId()==103)
		{
			openPanel(3);
			lastPanel = 3;
			return;
		}
		
		if (widget == btnTermOk) {
			String txt = edTermInput.text.trim();
			
			miner.brain.addInput(txt);

			edTermInput.text = "";

			return;
		}

		if (widget == edProgram) {
			miner.brain.program = edProgram.getText();
			btnLaunch.enable(false);
		}

		//close
		if (widget.getId() == 0) {
			miner.brain.program = edProgram.getText();
			gui.close();
			return;
		}

		// undo all
		if (widget.getId() == 1) {
			edProgram.setText(preUndo);
			miner.brain.program = edProgram.getText();
			txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgAllUndone"));
			return;
		}

		// check
		if (widget.getId() == 2) {
			txMsg.setText("");
			try {
				miner.brain.checkProgramForErrors(edProgram.getText());
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
				miner.brain.program = edProgram.getText();
				miner.brain.launchProgram();
				txMsg.setText(PC_Lang.tr("pc.gui.miner.launched"));
			} catch (Exception e) {
				txMsg.setText(e.getMessage());
			}
			return;
		}

		// pause/resume
		if (widget.getId() == 4) {
			miner.st.pausedWeasel ^= true;
			if (miner.st.pausedWeasel) {
				txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgPaused"));
			} else {
				txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgResumed"));
			}

			return;
		}

		// restart clear globals
		if (widget.getId() == 5) {
			miner.resetEverything();
			miner.brain.restartProgram();
			txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgRestarted"));
			return;
		}

		// stop
		if (widget.getId() == 6) {
			miner.st.halted = true;
			txMsg.setText(PC_Lang.tr("pc.gui.weasel.core.msgHalted"));

			return;
		}

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void updateTick(PC_IGresGui gui) {
		if (!edTerm.getText().equals(miner.brain.termText.trim())) {
			edTerm.setText(miner.brain.termText.trim());
			edTerm.scrollToBottom();
		}

		if (miner.st.halted) {
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.halted"));
			btnPauseResume.enable(false);
			btnStop.enable(false);
			btnRestart.enable(true);
			return;
		}

		if (miner.brain.hasError()) {
			txMsg.text = miner.brain.getError().replace("\n", " ");
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.crashed"));
			btnPauseResume.enable(false);
			btnRestart.enable(true);
			btnStop.enable(true);
			return;
		}

		if (miner.st.pausedWeasel) {
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.paused"));
			btnPauseResume.setText(PC_Lang.tr("pc.gui.weasel.core.resume"));
			btnPauseResume.enable(true);
			btnRestart.enable(true);
			btnStop.enable(true);
			return;
		}

		if (miner.brain.engine.isProgramFinished) {
			txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.idle"));
			btnPauseResume.setText(PC_Lang.tr("pc.gui.weasel.core.pause"));
			btnPauseResume.enable(true);
			btnRestart.enable(true);
			btnStop.enable(true);
			btnStop.enable(!miner.brain.engine.isProgramFinished);
			return;
		}

		txRunning.setText(PC_Lang.tr("pc.gui.weasel.core.running"));
		btnPauseResume.setText(PC_Lang.tr("pc.gui.weasel.core.pause"));
		btnPauseResume.enable(true);
		btnStop.enable(true);
		btnRestart.enable(true);
	}

	@Override
	public List<Slot> getAllSlots(Container c) {
		return null;
	}

	@Override
	public boolean canShiftTransfer() {
		return true;
	}

}
