package net.minecraft.src;

import java.util.LinkedHashMap;

import org.lwjgl.opengl.GL11;

public class PCtr_GuiTeleporterDecide extends GuiScreen {
	private PCtr_TileEntityTeleporter teleporter;

	public PCtr_GuiTeleporterDecide(PCtr_TileEntityTeleporter te) {
		teleporter = te;
	}

	@Override
	public void updateScreen() {}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		controlList.clear();

		LinkedHashMap<Integer, String> btns = new LinkedHashMap<Integer, String>();
		btns.put(0, "pc.gui.teleporter.type.sender");
		btns.put(1, "pc.gui.teleporter.type.target");
		PC_GuiButtonAligner.alignToCenter(controlList, btns, 60, 8, height / 2 + 50 - 34, width / 2);
	}

	@Override
	public void onGuiClosed() {}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }

		if (guibutton.id == 0) {

			teleporter.setSender();

		} else if (guibutton.id == 1) {

			teleporter.setReceiver();
		}

		teleporter.onInventoryChanged();

		mc.displayGuiScreen(null);
		mc.setIngameFocus();

		ModLoader.openGUI(mc.thePlayer, new PCtr_GuiTeleporter(teleporter, true));

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char c, int i) {}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();

		drawGuiDecideBackgroundLayer(f);

		GL11.glPushMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);

		String title = PC_Lang.tr("pc.gui.teleporter.title");
		fontRenderer.drawString(title, width / 2 - (fontRenderer.getStringWidth(title) / 2), (height / 2 - 50) + 20, 0x000000);

		fontRenderer.drawString(PC_Lang.tr("pc.gui.teleporter.selectType"), width / 2 - 100, (height / 2 - 50) + 30 + 4, 0x404040);
		fontRenderer.drawString(PC_Lang.tr("pc.gui.teleporter.selectTypeDescr"), width / 2 - 100, (height / 2 - 50) + 30 + 14, 0x404040);

		GL11.glPopMatrix();

		super.drawScreen(i, j, f);

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	protected void drawGuiDecideBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture("/PowerCraft/core/dialog-small.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - 100) / 2;
		int k = (height - 50) / 2;
		drawTexturedModalRect(j - 100 + 30, k - 50 + 30 + 5, 0, 0, 240, 100);
	}

}
