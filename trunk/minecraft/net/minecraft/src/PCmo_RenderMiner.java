// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst

package net.minecraft.src;


import org.lwjgl.opengl.GL11;


// Referenced classes of package net.minecraft.src:
// Render, ModelMiner, EntityMiner, MathHelper,
// ModelBase, Entity

public class PCmo_RenderMiner extends Render {

	protected ModelBase modelMiner;

	public PCmo_RenderMiner() {
		shadowSize = 0.5F;
		modelMiner = new PCmo_ModelMiner();
	}

	public void renderMiner(PCmo_EntityMiner entityminer, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		float f2 = entityminer.getTimeSinceHit() - f1;
		float f3 = entityminer.getDamageTaken() - f1;
		if (f3 < 0.0F) {
			f3 = 0.0F;
		}
		if (f2 > 0.0F) {
			GL11.glRotatef(((MathHelper.sin(f2) * f2 * f3) / 10F) * entityminer.getForwardDirection(), 0.8F, 0.0F, 0.0F);
		}
		// loadTexture("/terrain.png");
		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		loadTexture(mod_PCmobile.getImgDir() + "miner_base.png");
		GL11.glScalef(-1F, -1F, 1.0F);
		modelMiner.render(entityminer, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		loadTexture(mod_PCmobile.getImgDir() + "miner_overlay_" + (Integer.toString(entityminer.level)) + ".png");
		GL11.glEnable(3042 /* GL_BLEND */);
		GL11.glDisable(3008 /* GL_ALPHA_TEST */);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		modelMiner.render(entityminer, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		if (entityminer.keyboardControlled) {
			loadTexture(mod_PCmobile.getImgDir() + "miner_overlay_keyboard.png");
			modelMiner.render(entityminer, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		}

		GL11.glDisable(3042 /* GL_BLEND */);
		GL11.glEnable(3008 /* GL_ALPHA_TEST */);

		GL11.glPopMatrix();

		// FISHNET CILINDER
		// GL11.glPushMatrix();
		// Tessellator tessellator = Tessellator.instance;
		// Random rr = new Random();
		// rr.setSeed(15);
		// GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		// GL11.glDisable(2896 /*GL_LIGHTING*/);
		// GL11.glTranslatef((float)d, (float)d1, (float)d2);
		// GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		//
		// for(int q=0; q<10; q++){
		// tessellator.startDrawing(2);
		//
		// tessellator.setColorOpaque_I(0);
		//
		// for (double k = 0; k <= 3.1415D*2; k+=(3.1415D*2)/30D)
		// {
		// tessellator.addVertex(Math.sin(k)*0.5D,q*0.2D,Math.cos(k)*0.5D);
		// }
		//
		// tessellator.draw();
		// }
		//
		// tessellator.startDrawing(1);
		// for (double k = 0; k <= 3.1415*2; k+=(3.1415D*2)/30D)
		// {
		//
		//
		// tessellator.setColorOpaque_I(0);
		// tessellator.addVertex(Math.sin(k)*0.5D,0D,Math.cos(k)*0.5D);
		// tessellator.addVertex(Math.sin(k)*0.5D,9*0.2D,Math.cos(k)*0.5D);
		//
		//
		// }
		// tessellator.draw();
		//
		// GL11.glEnable(2896 /*GL_LIGHTING*/);
		// GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		// GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderMiner((PCmo_EntityMiner) entity, d, d1, d2, f, f1);
	}
}
