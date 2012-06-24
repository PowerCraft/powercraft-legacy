package net.minecraft.src;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import net.minecraft.src.PC_GresMultiTextEdit.Keyword;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


/*public class PCmo_GuiMinerConsole extends GuiScreen {
	private PCmo_EntityMiner miner;
	private PC_GuiTextBox programBox;
	private PC_GuiTextBox appendBox;

	private PC_GuiCheckBox checkBridge;
	private PC_GuiCheckBox checkMining;
	private PC_GuiCheckBox checkLava;
	private PC_GuiCheckBox checkWater;
	private PC_GuiCheckBox checkKeepFuel;

	private PC_GuiCheckBox checkCobble;
	private PC_GuiCheckBox checkGravel;
	private PC_GuiCheckBox checkDirt;
	private PC_GuiCheckBox checkTorchFloor;
	private PC_GuiCheckBox checkCompress;

	private String errorString = "";

	private String TITLE = PC_Lang.tr("pc.gui.miner.title");

	public PCmo_GuiMinerConsole(PCmo_EntityMiner machine) {
		miner = machine;
	}

	@Override
	public void updateScreen() {
		programBox.updateCursorCounter();
		appendBox.updateCursorCounter();
	}

	private static final int QUIT = 0, PGM_CLEAR = 1, PGM_RUN = 2, DIR_GO = 3, CLEAR_BUFFER = 4, INSERT = 5, COPY = 6;

	private static int yCheckboxStart = 45;

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		controlList.clear();

		PC_GuiButtonAligner.alignSingleToRight(controlList, QUIT, "pc.gui.miner.quit", 45, height / 2 + 85, width / 2 + 110);

		LinkedHashMap<Integer, String> btns = new LinkedHashMap<Integer, String>();
		btns.put(PGM_CLEAR, "pc.gui.miner.clear");
		btns.put(PGM_RUN, "pc.gui.miner.run");
		PC_GuiButtonAligner.alignToRight(controlList, btns, 40, 4, height / 2 - 10, width / 2 + 110);

		PC_GuiButtonAligner.alignSingleToRight(controlList, DIR_GO, "pc.gui.miner.go", 26, height / 2 + 15, width / 2 + 110);

		PC_GuiButtonAligner.alignSingleToRight(controlList, CLEAR_BUFFER, "pc.gui.miner.reset", 45, height / 2 + 55, width / 2 + 110);

		GuiButton but;

		String cpy = PC_Lang.tr("pc.gui.miner.copy");
		String pst = PC_Lang.tr("pc.gui.miner.paste");

		int cpy_w = fontRenderer.getStringWidth(cpy);
		int pst_w = fontRenderer.getStringWidth(pst);

		but = (new PC_GuiClickableText(fontRenderer, INSERT, width / 2 + 105 - pst_w, height / 2 - 100, pst));
		controlList.add(but);

		but = (new PC_GuiClickableText(fontRenderer, COPY, width / 2 + 105 - pst_w - 5 - cpy_w, height / 2 - 100, cpy));
		controlList.add(but);

		String s = miner.program;
		programBox = new PC_GuiTextBox(this, fontRenderer, width / 2 - 110, height / 2 - 86, 220, 0, 6, s);
		programBox.isFocused = false;

		appendBox = new PC_GuiTextBox(this, fontRenderer, width / 2 - 110, height / 2 + 16, 210 - fontRenderer.getStringWidth(PC_Lang
				.tr("pc.gui.miner.go")) - 16, -1, 1, "");
		appendBox.isFocused = true;
		appendBox.setTextColors(0x99ff99, 0x669966);

		checkMining = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 110, height / 2 + yCheckboxStart, miner.miningEnabled,
				PC_Lang.tr("pc.gui.miner.opt.mining"));
		checkBridge = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 110, height / 2 + yCheckboxStart + 12 * 1, miner.bridgeEnabled,
				PC_Lang.tr("pc.gui.miner.opt.bridge"));
		checkLava = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 110, height / 2 + yCheckboxStart + 12 * 2, miner.lavaFillingEnabled,
				PC_Lang.tr("pc.gui.miner.opt.lavaFill"));
		checkWater = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 110, height / 2 + yCheckboxStart + 12 * 3,
				miner.waterFillingEnabled, PC_Lang.tr("pc.gui.miner.opt.waterFill"));
		checkKeepFuel = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 110, height / 2 + yCheckboxStart + 12 * 4, miner.keepAllFuel,
				PC_Lang.tr("pc.gui.miner.opt.keepFuel"));

		checkBridge.isEnabled = miner.level >= 3;
		checkLava.isEnabled = miner.level >= 4;
		checkWater.isEnabled = miner.level >= 6;

		checkCobble = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 30, height / 2 + yCheckboxStart,
				((miner.DESTROY & PCmo_EntityMiner.COBBLE) != 0), PC_Lang.tr("pc.gui.miner.opt.destroyCobble"));
		checkGravel = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 30, height / 2 + yCheckboxStart + 12 * 1,
				((miner.DESTROY & PCmo_EntityMiner.GRAVEL) != 0), PC_Lang.tr("pc.gui.miner.opt.destroyGravel"));
		checkDirt = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 30, height / 2 + yCheckboxStart + 12 * 2,
				((miner.DESTROY & PCmo_EntityMiner.DIRT) != 0), PC_Lang.tr("pc.gui.miner.opt.destroyDirt"));
		checkTorchFloor = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 30, height / 2 + yCheckboxStart + 12 * 4,
				miner.torchesOnlyOnFloor, PC_Lang.tr("pc.gui.miner.opt.torchesOnFloor"));
		checkCompress = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 30, height / 2 + yCheckboxStart + 12 * 3, miner.compressBlocks,
				PC_Lang.tr("pc.gui.miner.opt.compress"));

		checkTorchFloor.isEnabled = miner.level >= 3;
		checkCompress.isEnabled = miner.level >= 5;

		((GuiButton) controlList.get(PGM_RUN)).enabled = programBox.getText().trim().length() > 0;

		((GuiButton) controlList.get(DIR_GO)).enabled = false;

	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		miner.openedGui = null;
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
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }

		if (guibutton.id == QUIT) {
			// Close
			miner.program = programBox.getText().trim();
			// miner.runNewProgram();
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}

		if (guibutton.id == INSERT) {
			keyTyped('\026', -1);
		}

		if (guibutton.id == COPY) {
			String copied = "";

			if (programBox.isFocused) {
				copied = programBox.getText();
			}

			if (appendBox.isFocused) {
				copied = appendBox.getText();
			}

			copied = copied.trim();
			if (copied.length() > 0) {
				PC_Logger.finest("copying text: " + copied);
				java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(new java.awt.datatransfer.StringSelection(new String(copied)), null);
			}
		}

		if (guibutton.id == PGM_CLEAR) {
			programBox.setText("");
		}

		if (guibutton.id == CLEAR_BUFFER) {
			miner.resetEverything();
		}

		if (guibutton.id == PGM_RUN) {
			miner.program = programBox.getText().trim();
			try {
				miner.runNewProgram();
				mc.displayGuiScreen(null);
				mc.setIngameFocus();

			} catch (PCmo_CommandException err) {
				errorString = err.getError();
			}
		}

		if (guibutton.id == DIR_GO) {
			try {
				miner.setCode(appendBox.getText().trim());
				mc.displayGuiScreen(null);
				mc.setIngameFocus();

			} catch (PCmo_CommandException err) {
				errorString = err.getError();
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char c, int i) {

		if (i == Keyboard.KEY_ESCAPE) {
			onGuiClosed();
			return;
		}

		programBox.textboxKeyTyped(c, i);
		appendBox.textboxKeyTyped(c, i);

		((GuiButton) controlList.get(PGM_RUN)).enabled = programBox.getText().trim().length() > 0;

		((GuiButton) controlList.get(DIR_GO)).enabled = appendBox.getText().trim().length() > 0;

		// if(c == '\r')
		// {
		// actionPerformed((GuiButton)controlList.get(QUIT));
		// }
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		programBox.mouseClicked(i, j, k);
		appendBox.mouseClicked(i, j, k);

		checkBridge.mouseClicked(i, j, k);
		checkMining.mouseClicked(i, j, k);
		checkWater.mouseClicked(i, j, k);
		checkLava.mouseClicked(i, j, k);
		checkKeepFuel.mouseClicked(i, j, k);

		checkCobble.mouseClicked(i, j, k);
		checkDirt.mouseClicked(i, j, k);
		checkGravel.mouseClicked(i, j, k);

		checkTorchFloor.mouseClicked(i, j, k);
		checkCompress.mouseClicked(i, j, k);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int e = Mouse.getEventDWheel();
		if (e != 0) {
			int i = (Mouse.getEventX() * width) / mc.displayWidth;
			int k = height - (Mouse.getEventY() * height) / mc.displayHeight - 1;

			if (programBox.checkClicked(i, k)) {
				programBox.textboxKeyTyped('\0', e > 0 ? Keyboard.KEY_UP : Keyboard.KEY_DOWN);
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();

		drawGuiRadioBackgroundLayer(f);

		GL11.glPushMatrix();
		GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();

		GL11.glPushMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT *//*);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING *//*);
		GL11.glDisable(2929 /* GL_DEPTH_TEST *//*);

		fontRenderer.drawString(TITLE, width / 2 - (fontRenderer.getStringWidth(TITLE) / 2), (height / 2 - 109), 0x000000);
		fontRenderer.drawString(PC_Lang.tr("pc.gui.miner.programCode"), width / 2 - 110, height / 2 - 96, 0x404040);

		if (errorString.length() > 0) {
			fontRenderer.drawString(errorString, width / 2 - 110 + 1, height / 2 + 5 + 1, 0x999999);
			fontRenderer.drawString(errorString, width / 2 - 110, height / 2 + 5, 0x770000);
		}

		fontRenderer.drawString(".." + miner.commandList.length(), width / 2 + 72, height / 2 + 45, 0x777777);

		programBox.drawTextBox();
		appendBox.drawTextBox();
		// bufferBox.drawTextBox();

		checkBridge.drawCheckBox();
		checkMining.drawCheckBox();
		checkWater.drawCheckBox();
		checkLava.drawCheckBox();
		checkKeepFuel.drawCheckBox();

		checkCobble.drawCheckBox();
		checkDirt.drawCheckBox();
		checkGravel.drawCheckBox();
		checkTorchFloor.drawCheckBox();
		checkCompress.drawCheckBox();

		GL11.glPopMatrix();
		super.drawScreen(i, j, f);
		GL11.glEnable(2896 /* GL_LIGHTING *//*);
		GL11.glEnable(2929 /* GL_DEPTH_TEST *//*);
	}

	protected void drawGuiRadioBackgroundLayer(float f) {

		PC_GresWidget.renderTextureSliced_static(this, new PC_CoordI((width-240)/2, (height-230)/2), mod_PCcore.getImgDir() + "gres/dialog.png",
				new PC_CoordI(240, 230), new PC_CoordI(0, 0), new PC_CoordI(256, 256));

	}
}*/

public class PCmo_GuiMinerConsole implements PC_IGresBase {
	private PCmo_EntityMiner miner;
	private PC_GresMultiTextEdit programBox;
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
		int keyWordColor = 0xffff00;
		int operatorColor = 0x00ff00;
		keyWords.add(new Keyword("(", operatorColor));
		keyWords.add(new Keyword(")", operatorColor));
		keyWords.add(new Keyword("@", operatorColor));
		keyWords.add(new Keyword("+", operatorColor));
		keyWords.add(new Keyword("-", operatorColor));
		keyWords.add(new Keyword("*", operatorColor));
		keyWords.add(new Keyword("/", operatorColor));
		keyWords.add(new Keyword("%", operatorColor));
		keyWords.add(new Keyword("=", operatorColor));
		keyWords.add(new Keyword("<", operatorColor));
		keyWords.add(new Keyword(">", operatorColor));
		keyWords.add(new Keyword(":", operatorColor));
		keyWords.add(new Keyword("?", operatorColor));
		keyWords.add(new Keyword("!", operatorColor));
		keyWords.add(new Keyword("F", keyWordColor));
		keyWords.add(new Keyword("B", keyWordColor));
		keyWords.add(new Keyword("L", keyWordColor));
		keyWords.add(new Keyword("R", keyWordColor));
		keyWords.add(new Keyword("S", keyWordColor));
		keyWords.add(new Keyword("N", keyWordColor));
		keyWords.add(new Keyword("E", keyWordColor));
		keyWords.add(new Keyword("W", keyWordColor));
		keyWords.add(new Keyword("U", keyWordColor));
		keyWords.add(new Keyword("D", keyWordColor));
		keyWords.add(new Keyword("Q", keyWordColor));
		keyWords.add(new Keyword("deposit", keyWordColor));
		keyWords.add(new Keyword("store", keyWordColor));
		keyWords.add(new Keyword("eject", keyWordColor));
		keyWords.add(new Keyword("X", keyWordColor));
		keyWords.add(new Keyword("halt", keyWordColor));
		keyWords.add(new Keyword("die", keyWordColor));
		keyWords.add(new Keyword("toblocks", keyWordColor));
		keyWords.add(new Keyword("blocks", keyWordColor));
		keyWords.add(new Keyword("M", keyWordColor));
		keyWords.add(new Keyword("mining", keyWordColor));
		keyWords.add(new Keyword("mine", keyWordColor));
		keyWords.add(new Keyword("mi", keyWordColor));
		keyWords.add(new Keyword("B", keyWordColor));
		keyWords.add(new Keyword("bridge", keyWordColor));
		keyWords.add(new Keyword("br", keyWordColor));
		keyWords.add(new Keyword("L", keyWordColor));
		keyWords.add(new Keyword("lava", keyWordColor));
		keyWords.add(new Keyword("W", keyWordColor));
		keyWords.add(new Keyword("water", keyWordColor));
		keyWords.add(new Keyword("on", keyWordColor));
		keyWords.add(new Keyword("off", keyWordColor));
		keyWords.add(new Keyword("loop", keyWordColor));
		keyWords.add(new Keyword("and", keyWordColor));
		keyWords.add(new Keyword("RND", keyWordColor));
		w.add(programBox = new PC_GresMultiTextEdit(miner.program, 250, 60, keyWords));
		
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
		pgm_run.enable(miner.program.length()>0);
		dir_go.enable(false);
		
		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(checkCobble = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.destroyCobble")).check((miner.DESTROY & PCmo_EntityMiner.COBBLE) != 0));
		vg.add(checkGravel = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.destroyGravel")).check((miner.DESTROY & PCmo_EntityMiner.GRAVEL) != 0));
		vg.add(checkDirt = new PC_GresCheckBox(PC_Lang.tr("pc.gui.miner.opt.destroyDirt")).check((miner.DESTROY & PCmo_EntityMiner.DIRT) != 0));
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
		}else if (widget == pgm_clear) {
			programBox.setText("");
		}else if (widget == clear_buffer) {
			miner.resetEverything();
		}else if (widget == pgm_run) {
			miner.program = programBox.getText().trim();
			errorString.setText("");
			try {
				miner.runNewProgram();
				gui.close();

			} catch (PCmo_CommandException err) {
				errorString.setText(err.getError());
			}
		}else if (widget == dir_go) {
			errorString.setText("");
			try {
				miner.setCode(appendBox.getText().trim());
				gui.close();

			} catch (PCmo_CommandException err) {
				errorString.setText(err.getError());
			}
		}else if(widget == programBox){
			pgm_run.enable(programBox.getText().length()>0);
		}else if(widget == appendBox){
			dir_go.enable(appendBox.getText().length()>0);
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
	public void onCraftMatrixChanged(IInventory iinventory) {
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}
}
