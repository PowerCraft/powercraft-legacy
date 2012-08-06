package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Renderer of optical device
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_TileEntityOpticalRenderer extends TileEntitySpecialRenderer {
	private PCma_ModelMirror modelMirror;
	private PCma_ModelPrism modelPrism;

	/**
	 * init optical renderer
	 */
	public PCma_TileEntityOpticalRenderer() {
		modelMirror = new PCma_ModelMirror();
		modelPrism = new PCma_ModelPrism();
	}

	/**
	 * @param tileentity the device TE
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 * @param f0 rotation, ignored
	 */
	private void renderTileEntityOpticalAt(PCma_TileEntityOptical tileentity, double x, double y, double z, float f0) {
		if (tileentity.isMirror()) {

			World world = tileentity.worldObj;

			modelMirror.bottomSticks.showModel = false;
			modelMirror.ceilingSticks.showModel = false;
			modelMirror.stickXplus.showModel = false;
			modelMirror.stickXminus.showModel = false;
			modelMirror.stickZplus.showModel = false;
			modelMirror.stickZminus.showModel = false;
			modelMirror.stickZminus.showModel = false;

			modelMirror.signBoard.showModel = true;

			int i, j, k;

			i = tileentity.xCoord;
			j = tileentity.yCoord;
			k = tileentity.zCoord;

			if (world.getBlockMaterial(i, j - 1, k).isSolid()) {
				modelMirror.bottomSticks.showModel = true;
			} else if (world.getBlockMaterial(i, j + 1, k).isSolid()) {
				modelMirror.ceilingSticks.showModel = true;
			} else if (world.getBlockMaterial(i + 1, j, k).isSolid()) {
				modelMirror.stickXplus.showModel = true;
			} else if (world.getBlockMaterial(i - 1, j, k).isSolid()) {
				modelMirror.stickXminus.showModel = true;
			} else if (world.getBlockMaterial(i, j, k + 1).isSolid()) {
				modelMirror.stickZplus.showModel = true;
			} else if (world.getBlockMaterial(i, j, k - 1).isSolid()) {
				modelMirror.stickZminus.showModel = true;
			}

			GL11.glPushMatrix();
			float f = 0.6666667F;

			GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F /* *f0 */, (float) z + 0.5F);
			float f1 = (tileentity.getBlockMetadata() * 360) / 16F;

			bindTextureByName(mod_PCmachines.getImgDir() + "mirror.png");

			GL11.glPushMatrix();
			GL11.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(f, -f, -f);

			int color = tileentity.getMirrorColor();

			if (color != -1) {

				double red = PC_Color.red(PC_Color.crystal_colors[color]);
				double green = PC_Color.green(PC_Color.crystal_colors[color]);
				double blue = PC_Color.blue(PC_Color.crystal_colors[color]);

				GL11.glColor4d(red, green, blue, 0.5D);

			}

			modelMirror.renderMirrorNoSideSticks();
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glScalef(f, -f, -f);
			modelMirror.renderMirrorSideSticks();
			GL11.glPopMatrix();

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();

		} else {

			modelPrism.mainCrystal.showModel = true;

			for (int a = 0; a <= 9; a++) {
				modelPrism.sides[a].showModel = tileentity.getPrismSide(a);
			}

			GL11.glPushMatrix();
			float f = 1.0F;

			GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

			bindTextureByName(mod_PCmachines.getImgDir() + "prism.png");

			GL11.glPushMatrix();
			GL11.glScalef(f, -f, -f);

			GL11.glEnable(3042 /* GL_BLEND */);
			GL11.glDisable(3008 /* GL_ALPHA_TEST */);
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			modelPrism.renderPrism();

			GL11.glDisable(GL11.GL_NORMALIZE);
			GL11.glDisable(3042 /* GL_BLEND */);
			GL11.glEnable(3008 /* GL_ALPHA_TEST */);

			GL11.glPopMatrix();

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		renderTileEntityOpticalAt((PCma_TileEntityOptical) tileEntity, x, y, z, f);
	}
}
