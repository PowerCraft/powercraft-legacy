package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Renderer for prozimity Sensor
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntitySensorRenderer extends TileEntitySpecialRenderer {

	private PClo_ModelSensor model;

	/**
	 * sensor renderer
	 */
	public PClo_TileEntitySensorRenderer() {
		model = new PClo_ModelSensor();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {

		PClo_TileEntitySensor ter = (PClo_TileEntitySensor) tileentity;

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

		bindTextureByName(mod_PClogic.getImgDir() + "block_sensor.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);
		model.setType(ter.getGroup(), ter.active);
		model.render();
		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
