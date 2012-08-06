package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Render thrown item
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_RenderThrownItem extends Render {
	private float scalef;

	/**
	 * @param f scale
	 */
	public PCma_RenderThrownItem(float f) {
		scalef = f;
	}

	/**
	 * render with scale=1;
	 */
	public PCma_RenderThrownItem() {
		scalef = 1.0F;
	}

	/**
	 * Do render the item
	 * 
	 * @param entity thrown item entity
	 * @param d x
	 * @param d1 y
	 * @param d2 z
	 * @param f ?
	 * @param f1 ?
	 */
	private void doRenderThrown(PCma_IThrownItem entity, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		float f2 = scalef;
		GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		byte byte0 = entity.getIconIndex();
		loadTexture("/gui/items.png");
		Tessellator tessellator = Tessellator.instance;
		float f3 = ((byte0 % 16) * 16 + 0) / 256F;
		float f4 = ((byte0 % 16) * 16 + 16) / 256F;
		float f5 = ((byte0 / 16) * 16 + 0) / 256F;
		float f6 = ((byte0 / 16) * 16 + 16) / 256F;
		float f7 = 1.0F;
		float f8 = 0.5F;
		float f9 = 0.25F;
		GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
		tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
		tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
		tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
		tessellator.draw();
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		doRenderThrown((PCma_IThrownItem) entity, d, d1, d2, f, f1);
	}
}
