package net.minecraft.src;



import org.lwjgl.opengl.GL11;


/**
 * Weasel chip renderer
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCnt_TileEntityWeaselRenderer_UNUSED extends TileEntitySpecialRenderer {

	private PCnt_ModelWeasel_UNUSED model;

	/**
	 * radio te renderer
	 */
	public PCnt_TileEntityWeaselRenderer_UNUSED() {
		model = new PCnt_ModelWeasel_UNUSED();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {

		PCnt_TileEntityWeasel_UNUSED tew = (PCnt_TileEntityWeasel_UNUSED) tileentity;

		if (tew == null) {
			return;
		}

		PCnt_WeaselPlugin_UNUSED plugin = tew.getPlugin();

		if (plugin == null) {
			return;
		}

		model.deviceType = plugin.getType();
		model.plugin = plugin;
		model.flag1 = false;
		if (plugin instanceof PCnt_WeaselPluginPort_UNUSED) model.flag1 = ((PCnt_WeaselPluginPort_UNUSED) plugin).renderAsActive();

		PC_Color color = tew.getColor();

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
		if (plugin instanceof PCnt_WeaselPluginDisplay_UNUSED) {
			f1 = ((PCnt_WeaselPluginDisplay_UNUSED) plugin).rotation * 360 / 16F;
			GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
		} else if (plugin instanceof PCnt_WeaselPluginTouchscreen_UNUSED) {
			f1 = ((PCnt_WeaselPluginTouchscreen_UNUSED) plugin).rotation * 360 / 16F;
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
