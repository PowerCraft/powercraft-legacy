package net.minecraft.src;

import java.util.LinkedHashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;



/**
 * Pulsar settings GUI
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PClo_GuiPulsar extends GuiScreen {
	private PClo_TileEntityPulsar pulsar;

	private PC_GuiCheckBox checkSilent;
	private int delay_ticks;
	private int hold_ticks;
	private boolean errorHold = false;
	private boolean errorDelay = false;

	/**
	 * @param tep Pulsar Tile Entity
	 */
	public PClo_GuiPulsar(PClo_TileEntityPulsar tep) {
		pulsar = tep;
		delay_ticks = pulsar.delay;
		hold_ticks = pulsar.holdtime;
	}

	@Override
	public void updateScreen() {
		fieldDelay.updateCursorCounter();
		fieldHold.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		controlList.clear();

		LinkedHashMap<Integer, String> btns = new LinkedHashMap<Integer, String>();
		btns.put(1, "pc.gui.cancel");
		btns.put(0, "pc.gui.ok");
		PC_GuiButtonAligner.alignToRight(controlList, btns, 50, 8, height / 2 + 75 - 30, width / 2 + 100);

		String s = PC_Utils.doubleToString(PC_Utils.ticksToSecs(delay_ticks));

		fieldDelay = new GuiTextField(fontRenderer, width / 2 - 100, height / 2 - 75 + 55, 98, 20);
		fieldDelay.setText(s);
		fieldDelay.setFocused(true); // focused

		fieldDelay.setMaxStringLength(10);

		s = PC_Utils.doubleToString(PC_Utils.ticksToSecs(hold_ticks));

		fieldHold = new GuiTextField(fontRenderer, width / 2 + 2, height / 2 - 75 + 55, 98, 20);
		fieldHold.setText(s);
		// fieldHold.setFocused(true); //focused
		fieldHold.setMaxStringLength(10);

		checkSilent = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 100, (height / 2 + 30), pulsar.silent,
				PC_Lang.tr("pc.gui.pulsar.silent"));
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }
		if (guibutton.id == 1) {
			// remove the gui
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		} else if (guibutton.id == 0) {
			pulsar.setDelay(delay_ticks);
			pulsar.setHoldTime(hold_ticks);
			pulsar.setSilent(checkSilent.isChecked());
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char c, int i) {

		if (PC_KeyUtils.filterKeyFloat(c, i)) {
			fieldDelay.textboxKeyTyped(c, i);
			fieldHold.textboxKeyTyped(c, i);
		}

		try {
			double time = Double.valueOf(fieldDelay.getText());
			delay_ticks = PC_Utils.secsToTicks(time);

			errorDelay = (delay_ticks < 2) || (delay_ticks > 150000);

			((GuiButton) controlList.get(0)).enabled = !errorDelay;

		} catch (NumberFormatException nfe) {

			((GuiButton) controlList.get(0)).enabled = false;
			errorDelay = true;

		} catch (NullPointerException npe) {

			((GuiButton) controlList.get(0)).enabled = false;
			errorDelay = true;
		}

		try {
			double time = Double.valueOf(fieldHold.getText());
			hold_ticks = PC_Utils.secsToTicks(time);

			errorHold = !((hold_ticks < delay_ticks - 1) && hold_ticks >= 1);
			((GuiButton) controlList.get(0)).enabled = !errorHold && !errorDelay;

		} catch (NumberFormatException nfe) {

			((GuiButton) controlList.get(0)).enabled = false;
			errorHold = true;

		} catch (NullPointerException npe) {

			((GuiButton) controlList.get(0)).enabled = false;
			errorHold = true;
		}

		if (c == '\r') {
			actionPerformed((GuiButton) controlList.get(0));
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		fieldDelay.mouseClicked(i, j, k);
		fieldHold.mouseClicked(i, j, k);

		checkSilent.mouseClicked(i, j, k);
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
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);

		String title = PC_Lang.tr("tile.PCloRedstonePulsar.name");
		fontRenderer.drawString(title, width / 2 - (fontRenderer.getStringWidth(title) / 2), (height / 2 - 75) + 20, 0x000000);
		fontRenderer.drawString(PC_Lang.tr("pc.gui.pulsar.delay"), width / 2 - 100, (height / 2 - 75) + 45, 0x404040);
		fontRenderer.drawString(PC_Lang.tr("pc.gui.pulsar.hold"), width / 2 + 3, (height / 2 - 75) + 45, 0x404040);
		if (!errorDelay && !errorHold) {

			String info = delay_ticks + " " + PC_Lang.tr("pc.gui.pulsar.ticks");
			int secs = PC_Utils.ticksToSecsInt(delay_ticks);
			if (secs >= 60) {
				info = PC_Utils.formatTimeTicks(delay_ticks);
			}

			fontRenderer.drawString(info, width / 2 - 100, (height / 2 - 75 + 55 + 20 + 5), 0x606060);

		} else {
			if (errorDelay) {
				fontRenderer.drawString(PC_Lang.tr("pc.gui.pulsar.errDelay"), width / 2 - 100, (height / 2 - 75 + 55 + 20 + 5), 0x990000);
			} else if (errorHold) {
				fontRenderer.drawString(PC_Lang.tr("pc.gui.pulsar.errHold"), width / 2 - 100, (height / 2 - 75 + 55 + 20 + 5), 0x990000);
			}
		}

		fieldDelay.drawTextBox();
		fieldHold.drawTextBox();
		checkSilent.drawCheckBox();

		GL11.glPopMatrix();
		super.drawScreen(i, j, f);
		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private void drawGuiRadioBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture("/PowerCraft/core/dialog-medium.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width) / 2;
		int k = (height) / 2;
		drawTexturedModalRect(j - 120, k - 75, 0, 0, 240, 150);
	}

	private GuiTextField fieldDelay;
	private GuiTextField fieldHold;
}
