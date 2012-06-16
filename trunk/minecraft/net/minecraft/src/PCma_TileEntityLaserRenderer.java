package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * Renderer for lasers
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_TileEntityLaserRenderer extends TileEntitySpecialRenderer {
	private PCma_ModelLaser modelLaser;

	/**
	 * the laser renderer
	 */
	public PCma_TileEntityLaserRenderer() {
		modelLaser = new PCma_ModelLaser();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f0) {

		PCma_TileEntityLaser tel = (PCma_TileEntityLaser) tileEntity;

		modelLaser.laserParts[0].showModel = modelLaser.laserParts[1].showModel = modelLaser.laserParts[2].showModel = modelLaser.laserParts[3].showModel = tel
				.isKiller();

		modelLaser.laserParts[7].showModel = tel.isKiller();
		modelLaser.laserParts[8].showModel = tel.isSensor();
		modelLaser.laserParts[9].showModel = tel.isRsTransmitter();
		modelLaser.laserParts[10].showModel = tel.isRsReceiver();

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F /* *f0 */, (float) z + 0.5F);

		int[] meta2angle = { 0, 0, 270, 90, 180, 0 };

		float f1 = meta2angle[tel.getBlockMetadata()];

		bindTextureByName(mod_PCmachines.getImgDir() + "laser.png");

		GL11.glPushMatrix();
		GL11.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(f, -f, -f);
		modelLaser.renderLaser();
		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();

	}
}
