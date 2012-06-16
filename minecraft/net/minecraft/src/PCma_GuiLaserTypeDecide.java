package net.minecraft.src;

import java.util.LinkedHashMap;

import org.lwjgl.opengl.GL11;

/**
 * GUI where player decides about Laser Subtype
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_GuiLaserTypeDecide extends GuiScreen {
	private PCma_TileEntityLaser laser;

	/**
	 * @param te laser tile entity
	 */
	public PCma_GuiLaserTypeDecide(PCma_TileEntityLaser te) {
		laser = te;
	}

	@Override
	public void updateScreen() {}


	@Override
	public void initGui() {
		controlList.clear();
		LinkedHashMap<Integer, String> btns = new LinkedHashMap<Integer, String>();
		btns.put(0, "pc.gui.laserTypeDecide.sensor");
		btns.put(1, "pc.gui.laserTypeDecide.redstoneSender");
		btns.put(2, "pc.gui.laserTypeDecide.redstoneReceiver");
		PC_GuiButtonAligner.alignToCenter(controlList, btns, 70, 3, height / 2 + 50 - 34, width / 2);
	}

	@Override
	public void onGuiClosed() {}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }

		switch (guibutton.id) {
			case 0:
				laser.setType(PCma_LaserType.SENSOR);
				break;
			case 1:
				laser.setType(PCma_LaserType.RS_SEND);
				break;
			case 2:
				laser.setType(PCma_LaserType.RS_RECEIVE);
				break;
		}

		mc.displayGuiScreen(null);
		mc.setIngameFocus();

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


		String title = PC_Lang.tr("pc.gui.laserTypeDecide.title");
		fontRenderer.drawString(title, (width - fontRenderer.getStringWidth(title)) / 2, (height / 2 - 50) + 20, 0x000000);

		GL11.glPopMatrix();

		super.drawScreen(i, j, f);

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private void drawGuiDecideBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture("/PowerCraft/core/dialog-small.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - 100) / 2;
		int k = (height - 50) / 2;
		drawTexturedModalRect(j - 100 + 30, k - 50 + 30 + 5, 0, 0, 240, 100);
	}

}
