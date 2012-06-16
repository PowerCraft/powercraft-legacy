package net.minecraft.src;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Gui for delayer and repeater gates.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_GuiDelayer extends GuiScreen {

	private PClo_TileEntityGate gateTE;

	private int ticks;
	private boolean error = false;

	private boolean delayer_type;
	private static final boolean FIFO = true, HOLD = false;

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
	public void updateScreen() {
		fieldLength.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		controlList.clear();

		HashMap<Integer, String> btns = new HashMap<Integer, String>();
		btns.put(1, "pc.gui.cancel");
		btns.put(0, "pc.gui.ok");
		PC_GuiButtonAligner.alignToCenter(controlList, btns, 60, 8, height / 2 + 50 - 24, width / 2);

		fieldLength = new GuiTextField(fontRenderer, width / 2 - 50, height / 2 - 3, 100, 20);
		fieldLength.setText(PC_Utils.floatToString(ticks * 0.05F));
		fieldLength.setFocused(true); // focused

		fieldLength.setMaxStringLength(10);
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

			if (delayer_type == FIFO) {
				gateTE.bufferResize(ticks);
			} else if (delayer_type == HOLD) {
				gateTE.setRepeaterHoldTime(ticks);
			}

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
			fieldLength.textboxKeyTyped(c, i);
		}

		try {
			double time = Double.parseDouble(fieldLength.getText());

			ticks = PC_Utils.secsToTicks(time);

			error = (ticks < 2) || (ticks > 37000);

			((GuiButton) controlList.get(0)).enabled = !error;

		} catch (NumberFormatException nfe) {

			((GuiButton) controlList.get(0)).enabled = false;
			error = true;

		} catch (NullPointerException npe) {

			((GuiButton) controlList.get(0)).enabled = false;
			error = true;
		}

		if (c == '\r') {
			actionPerformed((GuiButton) controlList.get(0));
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		fieldLength.mouseClicked(i, j, k);
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

		String title = (delayer_type == FIFO) ? PC_Lang.tr("tile.PCloLogicGate.buffer.name") : PC_Lang
				.tr("tile.PCloLogicGate.slowRepeater.name");

		fontRenderer.drawString(title, width / 2 - (fontRenderer.getStringWidth(title) / 2), (height / 2 - 50) + 20, 0x000000);
		fontRenderer.drawString(PC_Lang.tr("pc.gui.gate.delay"), width / 2 - 50, (height / 2 - 50) + 35, 0x404040);

		if (!error) {
			fontRenderer.drawString("= " + ticks + " t.", width / 2 + 60, height / 2 + 3, 0x606060);
			int secs = PC_Utils.ticksToSecsInt(ticks);
			if (secs >= 60) {
				fontRenderer.drawString("= " + PC_Utils.formatTimeSecs(secs), width / 2 + 60, height / 2 + 3 + 16, 0x606060);
			}

		} else {
			fontRenderer.drawString(PC_Lang.tr("pc.gui.gate.invalid"), width / 2 + 60, height / 2 + 3, 0x990000);
		}

		fieldLength.drawTextBox();

		GL11.glPopMatrix();
		super.drawScreen(i, j, f);
		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private void drawGuiRadioBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture("/PowerCraft/core/dialog-small.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - 100) / 2;
		int k = (height - 50) / 2;
		drawTexturedModalRect(j - 100 + 30, k - 50 + 30 + 5, 0, 0, 240, 100);
	}

	private GuiTextField fieldLength;
}
