package net.minecraft.src;

import java.util.ArrayList;

import net.minecraft.src.PC_GresTextEditMultiline.Keyword;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;

public class PCmo_GuiMinerConsole implements PC_IGresBase {
	private PCmo_EntityMiner miner;
	private PC_GresTextEditMultiline programBox;
	private PC_GresTextEdit appendBox;

	private PC_GresCheckBox checkBridge;
	private PC_GresCheckBox checkMining;
	private PC_GresCheckBox checkLava;
	private PC_GresCheckBox checkWater;
	private PC_GresCheckBox checkKeepFuel;

	private PC_GresCheckBox checkCobble;
	private PC_GresCheckBox checkGravel;
	private PC_GresCheckBox checkDirt;
	private PC_GresCheckBox checkTorchFloor;
	private PC_GresCheckBox checkCompress;

	private PC_GresLabel commandListLength;
	private PC_GresLabel errorString;

	private PC_GresButton quit;
	private PC_GresWidget pgm_clear;
	private PC_GresWidget pgm_run;
	private PC_GresWidget dir_go;
	private PC_GresButton clear_buffer;

	public PCmo_GuiMinerConsole(PCmo_EntityMiner machine) {
		miner = machine;
	}

	@Override
	public EntityPlayer getPlayer() {
		return null;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget w = new PC_GresWindow(PC_Lang.tr("pc.gui.miner.title")).setAlignH(PC_GresAlign.STRETCH);

		PC_GresWidget hg;
		PC_GresWidget vg;

		w.add(new PC_GresLabel(PC_Lang.tr("pc.gui.miner.programCode")));
		ArrayList<Keyword> keyWords = new ArrayList<Keyword>();
		int keyWordColor = 0xFF00FF;
		int operatorColor = 0x00ff00;
		int numberColor = 0xFFFF00;
		keyWords.add(new Keyword("(", keyWordColor));
		keyWords.add(new Keyword(")", keyWordColor));
		keyWords.add(new Keyword("@", keyWordColor));
		keyWords.add(new Keyword(":", keyWordColor));
		keyWords.add(new Keyword("[+\\-\\*/%=><?!]", operatorColor,true));
		keyWords.add(new Keyword("[FBLRSNEWUDQXBMfblrsnewudqxbm]", keyWordColor,true));
		keyWords.add(new Keyword("[0-9]+", numberColor,true));
		keyWords.add(new Keyword("deposit", keyWordColor));
		keyWords.add(new Keyword("store", keyWordColor));
		keyWords.add(new Keyword("eject", keyWordColor));
		keyWords.add(new Keyword("halt", keyWordColor));
		keyWords.add(new Keyword("die", keyWordColor));
		keyWords.add(new Keyword("toblocks", keyWordColor));
		keyWords.add(new Keyword("blocks", keyWordColor));
		keyWords.add(new Keyword("mining", keyWordColor));
		keyWords.add(new Keyword("mine", keyWordColor));
		keyWords.add(new Keyword("mi", keyWordColor));
		keyWords.add(new Keyword("bridge", keyWordColor));
		keyWords.add(new Keyword("br", keyWordColor));
		keyWords.add(new Keyword("lava", keyWordColor));
		keyWords.add(new Keyword("water", keyWordColor));
		keyWords.add(new Keyword("on", keyWordColor));
		keyWords.add(new Keyword("off", keyWordColor));
		keyWords.add(new Keyword("loop", keyWordColor));
		keyWords.add(new Keyword("and", keyWordColor));
		keyWords.add(new Keyword("RND", keyWordColor));

		w.add(programBox = new PC_GresTextEditMultiline(miner.program, 250, 60, keyWords));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.RIGHT);
		errorString = new PC_GresLabel("");
		errorString.setColor(PC_GresWidget.textColorEnabled, 0x990000);
		errorString.setColor(PC_GresWidget.textColorShadowEnabled, 0x999999);
		hg.add(errorString);
		hg.add(pgm_clear = new PC_GresButton(PC_Lang.tr("pc.gui.miner.clear")).setMinWidth(40));
		hg.add(pgm_run = new PC_GresButton(PC_Lang.tr("pc.gui.miner.run")).setMinWidth(40));
		w.add(hg);

		hg = new PC_GresLayoutH();
		hg.add(appendBox = new PC_GresTextEdit("", 20));
		hg.add(dir_go = new PC_GresButton(PC_Lang.tr("pc.gui.miner.go")).setMinWidth(40));
		w.add(hg);

		hg = new PC_GresLayoutH();
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(checkMining = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.mining")).check(miner.miningEnabled));
		vg.add(checkBridge = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.bridge")).check(miner.bridgeEnabled));
		vg.add(checkLava = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.lavaFill")).check(miner.lavaFillingEnabled));
		vg.add(checkWater = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.waterFill")).check(miner.waterFillingEnabled));
		vg.add(checkKeepFuel = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.keepFuel")).check(miner.keepAllFuel));
		hg.add(vg);

		checkBridge.enable(miner.level >= 3);
		checkLava.enable(miner.level >= 4);
		checkWater.enable(miner.level >= 6);
		pgm_run.enable(miner.program.length() > 0);
		dir_go.enable(false);

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(checkCobble = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.destroyCobble"))
				.check((miner.DESTROY & PCmo_EntityMiner.COBBLE) != 0));
		vg.add(checkGravel = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.destroyGravel"))
				.check((miner.DESTROY & PCmo_EntityMiner.GRAVEL) != 0));
		vg.add(checkDirt = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.destroyDirt"))
				.check((miner.DESTROY & PCmo_EntityMiner.DIRT) != 0));
		vg.add(checkTorchFloor = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.torchesOnFloor")).check(miner.torchesOnlyOnFloor));
		vg.add(checkCompress = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.compress")).check(miner.compressBlocks));
		hg.add(vg);

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH);
		vg.add(commandListLength = new PC_GresLabel(".." + miner.commandList.length()));
		vg.add(clear_buffer = new PC_GresButton(PC_Lang.tr("pc.gui.miner.reset")));
		vg.add(quit = new PC_GresButton(PC_Lang.tr("pc.gui.miner.quit")));
		hg.add(vg);

		w.add(hg);
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		miner.openedGui = false;
		miner.programmingGuiOpen = false;
		miner.miningEnabled = checkMining.isChecked();
		miner.bridgeEnabled = checkBridge.isChecked();
		miner.lavaFillingEnabled = checkLava.isChecked();
		miner.waterFillingEnabled = checkWater.isChecked();
		miner.keepAllFuel = checkKeepFuel.isChecked();
		miner.torchesOnlyOnFloor = checkTorchFloor.isChecked();
		miner.compressBlocks = checkCompress.isChecked();

		miner.DESTROY = (byte) ((checkCobble.isChecked() ? PCmo_EntityMiner.COBBLE : 0)
				| (checkGravel.isChecked() ? PCmo_EntityMiner.GRAVEL : 0) | (checkDirt.isChecked() ? PCmo_EntityMiner.DIRT : 0));
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (!widget.isEnabled()) { return; }

		if (widget == quit) {
			// Close
			miner.program = programBox.getText().trim();
			gui.close();
		} else if (widget == pgm_clear) {
			programBox.setText("");
		} else if (widget == clear_buffer) {
			miner.resetEverything();
		} else if (widget == pgm_run) {
			miner.program = programBox.getText().trim();
			errorString.setText("");
			try {
				miner.runNewProgram();
				gui.close();

			} catch (PCmo_CommandException err) {
				errorString.setText(err.getError());
			}
		} else if (widget == dir_go) {
			errorString.setText("");
			try {
				miner.setCode(appendBox.getText().trim());
				gui.close();

			} catch (PCmo_CommandException err) {
				errorString.setText(err.getError());
			}
		} else if (widget == programBox) {
			pgm_run.enable(programBox.getText().length() > 0);
		} else if (widget == appendBox) {
			dir_go.enable(appendBox.getText().length() > 0);
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}
}
