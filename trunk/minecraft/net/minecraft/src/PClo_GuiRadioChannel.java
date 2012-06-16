package net.minecraft.src;

import java.util.LinkedHashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


/**
 * GUI for changing radio device channel
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PClo_GuiRadioChannel extends GuiScreen {
	private int type;
	private PC_CoordI pos;
	private String oldChannel;
	private int dim = 0;
	private String TITLE;

	/**
	 * @param dimen Radio device dimension
	 * @param blockPos block position in world
	 * @param s device channel
	 * @param transmitter is transmitter?(true) or receiver? (false)
	 */
	public PClo_GuiRadioChannel(int dimen, PC_CoordI blockPos, String s, boolean transmitter) {
		EditedString = s;
		oldChannel = new String(s);
		type = transmitter ? 0 : 1;
		pos = blockPos;
		dim = dimen;

		if (type == 0) {
			TITLE = PC_Lang.tr("tile.PCloRadio.tx.name");
		} else {
			TITLE = PC_Lang.tr("tile.PCloRadio.rx.name");
		}
	}

	@Override
	public void updateScreen() {
		textInputField.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		controlList.clear();
		LinkedHashMap<Integer, String> btns = new LinkedHashMap<Integer, String>();
		btns.put(1, "pc.gui.cancel");
		btns.put(0, "pc.gui.ok");
		PC_GuiButtonAligner.alignToCenter(controlList, btns, 60, 8, height / 2 + 50 - 24, width / 2);
		String s = EditedString;
		textInputField = new GuiTextField(fontRenderer, width / 2 - 100, height / 2 - 50 + 47, 200, 20);
		textInputField.setText(s);
		textInputField.setFocused(true); // focused
		textInputField.setMaxStringLength(32);
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
			String newChannel = textInputField.getText().trim();


			PClo_TileEntityRadio ter = PClo_BlockRadio.getTE(mc.theWorld, pos.x, pos.y, pos.z);

			if (type == 0) {
				PClo_RadioManager.setTransmitterChannel(dim, pos, oldChannel, newChannel, ter.isActive());
			} else {
				PClo_RadioManager.setReceiverChannel(dim, pos, newChannel);
			}

			ter.channel = newChannel;

			// player is in the right dimen, set it to make sure.
			ter.dim = dim;

			if (type == 1) {
				ter.active = PClo_RadioManager.getSignalStrength(newChannel) > 0;
				if (ter.active) {
					mc.theWorld.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, 1);
				}
			}

			mc.theWorld.scheduleBlockUpdate(pos.x, pos.y, pos.z, mod_PClogic.radio.blockID, 1);

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
		textInputField.textboxKeyTyped(c, i);
		((GuiButton) controlList.get(0)).enabled = textInputField.getText().trim().length() > 0;
		if (c == '\r') {
			actionPerformed((GuiButton) controlList.get(0));
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		textInputField.mouseClicked(i, j, k);
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

		fontRenderer.drawString(TITLE, width / 2 - (fontRenderer.getStringWidth(TITLE) / 2), (height / 2 - 50) + 20, 0x000000);
		fontRenderer.drawString(PC_Lang.tr("pc.gui.radio.channel"), width / 2 - 100, (height / 2 - 50) + 35, 0x404040);
		textInputField.drawTextBox();

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

	private GuiTextField textInputField;
	private final String EditedString;
}
