package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class PCnt_TileEntityWeaselRenderer extends TileEntitySpecialRenderer {

	private PCnt_ModelWeasel model;

	/**
	 * radio te renderer
	 */
	public PCnt_TileEntityWeaselRenderer() {
		model = new PCnt_ModelWeasel();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {

		PCnt_TileEntityWeasel tew = (PCnt_TileEntityWeasel) tileentity;

		if (tew == null) {
			return;
		}

		PCnt_WeaselPlugin plugin = tew.getPlugin();

		if (plugin == null) {
			return;
		}

		model.deviceType = plugin.getType();
		model.plugin = plugin;
		model.flag1 = false;
		if (plugin instanceof PCnt_WeaselPluginPort) model.flag1 = ((PCnt_WeaselPluginPort) plugin).renderAsActive();

		PC_Color color = plugin.getColor();

		// push 1
		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, ((float) y), (float) z + 0.5F);


		bindTextureByName(mod_PCnet.getImgDir() + "block_chip.png");

		// push 2
		GL11.glPushMatrix();

		GL11.glScalef(f, -f, -f);

		model.renderStationary();
		float f1 = 0;
		if (plugin instanceof PCnt_WeaselPluginDisplay) {
			f1 = ((PCnt_WeaselPluginDisplay) plugin).rotation * 360 / 16F;
			GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
		} else if (plugin instanceof PCnt_WeaselPluginTouchscreen) {
			f1 = ((PCnt_WeaselPluginTouchscreen) plugin).rotation * 360 / 16F;
			GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
		} else {
			GL11.glRotatef(90 * (tew.getCoord().getMeta(tew.worldObj) & 3), 0, 1, 0);
		}
		model.renderDevice();

		GL11.glColor4d(color.r, color.g, color.b, 1D);

		model.renderColorMark();

		// pop 2
		GL11.glPopMatrix();



		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
		model.renderText(this);

		// pop1
		GL11.glPopMatrix();

	}

}
