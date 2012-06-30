package net.minecraft.src;


import java.util.Random;

import org.lwjgl.opengl.GL11;



/**
 * Render fishing machine
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_RenderFishingMachine extends Render {

	/** the machine model */
	protected ModelBase modelMachine;
	/** the screw model */
	protected ModelBase modelScrew;

	/**
	 * fishing machine renderer
	 */
	public PCma_RenderFishingMachine() {
		shadowSize = 0;
		modelMachine = new PCma_ModelFishingMachine();
		modelScrew = new PCma_ModelFishingMachineScrew();
	}

	/**
	 * render at
	 * 
	 * @param entityfisher the fishing machine entity
	 * @param d relative x
	 * @param d1 relative y
	 * @param d2 relative z
	 * @param f rotation of the screw (machine ignores it)
	 * @param f1 something ignored
	 */
	private void renderFishingMachine(PCma_EntityFishingMachine entityfisher, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		loadTexture(mod_PCmachines.getImgDir() + "fisher.png");
		GL11.glScalef(-1F, -1F, 1.0F);
		modelMachine.render(entityfisher, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		loadTexture(mod_PCmachines.getImgDir() + "fisher.png");
		GL11.glScalef(-1F, -1F, 1.0F);
		modelScrew.render(entityfisher, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glPopMatrix();

		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.instance;
		Random rr = new Random();
		rr.setSeed(15);
		GL11.glDisable(3553 /* GL_TEXTURE_2D */);
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glTranslatef((float) d, (float) d1, (float) d2);

		double diameter = 0.6D;

		for (int q = 0; q < 24; q++) {
			tessellator.startDrawing(2);

			tessellator.setColorOpaque_I(0x000022);

			for (double k = 0; k <= 3.1415D * 2; k += (3.1415D * 2) / 20D) {
				tessellator.addVertex(Math.sin(k) * diameter, -q * 0.2D, Math.cos(k) * diameter);
			}

			tessellator.draw();
		}

		tessellator.startDrawing(1);
		for (double k = 0; k <= 3.1415 * 2; k += (3.1415D * 2) / 20D) {

			tessellator.setColorOpaque_I(0x000022);
			tessellator.addVertex(Math.sin(k) * diameter, 0D, Math.cos(k) * diameter);
			tessellator.addVertex(Math.sin(k) * diameter, -23 * 0.2D, Math.cos(k) * diameter);

		}
		tessellator.draw();

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(3553 /* GL_TEXTURE_2D */);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderFishingMachine((PCma_EntityFishingMachine) entity, d, d1, d2, f, f1);
	}
}
