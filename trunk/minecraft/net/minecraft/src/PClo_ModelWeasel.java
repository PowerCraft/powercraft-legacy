package net.minecraft.src;


import java.util.List;

import org.lwjgl.opengl.GL11;


/**
 * Model for PClo_TileEntityGateRenderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ModelWeasel extends ModelBase {

	private ModelRenderer core[];
	private ModelRenderer port[];
	private ModelRenderer display[];
	private ModelRenderer sound[];

	/**
	 * Radio block model.
	 */
	public PClo_ModelWeasel() {

		textureWidth = 128;
		textureHeight = 128;

		core = new ModelRenderer[4];

		// the stone pad
		core[0] = new ModelRenderer(this, 9, 20);
		core[0].addBox(-8F, -3F, -8F, 16, 3, 16, 0.0F);

		// body
		core[1] = new ModelRenderer(this, 0, 0);
		core[1].addBox(-4F, -6F, -5F, 8, 2, 10, 0.0F);

		// legs
		core[2] = new ModelRenderer(this, 70, 0);
		core[2].addBox(-5F, -5F, -5F, 10, 3, 10, 0.0F);

		// the colour piece
		core[3] = new ModelRenderer(this, 13, 12);
		core[3].addBox(-2F, -6.5F, 2F, 4, 1, 2, 0.0F);



		port = new ModelRenderer[4];

		// the stone pad
		port[0] = new ModelRenderer(this, 21, 0);
		port[0].addBox(-8F, -3F, -8F, 16, 3, 16, 0.0F);

		// the piece with light, on
		port[1] = new ModelRenderer(this, 0, 14);
		port[1].addBox(-3F, -5F, -3F, 6, 2, 6, 0.0F);

		// the piece with light, off
		port[2] = new ModelRenderer(this, 0, 23);
		port[2].addBox(-3F, -5F, -3F, 6, 2, 6, 0.0F);

		// the colour piece
		port[3] = new ModelRenderer(this, 13, 12);
		port[3].addBox(-2F, -4.5F, -6F, 4, 1, 2, 0.0F);



		display = new ModelRenderer[5];

		// the bottom pad
		display[0] = new ModelRenderer(this, 85, 27);
		display[0].addBox(-5F, -1F, -5F, 10, 1, 10, 0.0F);

		// the connection piece
		display[1] = new ModelRenderer(this, 86, 16);
		display[1].addBox(-1F, -2F, -1F, 2, 1, 2, 0.0F);

		// display body
		display[2] = new ModelRenderer(this, 58, 20);
		display[2].addBox(-8F, -16F, -1F, 16, 14, 2, 0.0F);

		// screen
		display[3] = new ModelRenderer(this, 0, 64);
		display[3].addBox(-8F, -16F, -1F, 16, 14, 2, 0.0F);

		// the colour piece
		display[4] = new ModelRenderer(this, 13, 12);
		display[4].addBox(-2F, -17F, -1F, 4, 1, 2, 0.0F);


		sound = new ModelRenderer[3];

		// the stone pad
		sound[0] = new ModelRenderer(this, 64, 39);
		sound[0].addBox(-8F, -3F, -8F, 16, 3, 16, 0.0F);

		// body
		sound[1] = new ModelRenderer(this, 0, 40);
		sound[1].addBox(-6F, -15F, -6F, 12, 12, 12, 0.0F);

		// the colour piece
		sound[2] = new ModelRenderer(this, 0, 31);
		sound[2].addBox(-2F, -15.5F, -2F, 4, 1, 4, 0.0F);


	}

	public int deviceType = PClo_WeaselType.CORE;
	public boolean flag1 = false;
	public PClo_WeaselPlugin plugin;

	/**
	 * Do render.
	 */
	public void renderDevice() {

		if (deviceType == PClo_WeaselType.CORE) {
			core[0].render(0.0625F);
			core[1].render(0.0625F);
			core[2].render(0.0625F);
		} else if (deviceType == PClo_WeaselType.PORT) {
			port[0].render(0.0625F);
			if (flag1) {
				port[1].render(0.0625F);
			} else {
				port[2].render(0.0625F);
			}
		} else if (deviceType == PClo_WeaselType.DISPLAY) {
			display[0].render(0.0625F);
			display[1].render(0.0625F);
			display[2].render(0.0625F);
			int bg = ((PClo_WeaselPluginDisplay) plugin).bgcolor;
			PC_Color bgc = PC_Color.fromHex(bg);
			GL11.glColor4d(bgc.r, bgc.g, bgc.b, 1.0F);
			display[3].render(0.0625F);
			GL11.glColor4d(1,1,1,1);
		} else if (deviceType == PClo_WeaselType.SPEAKER) {
			sound[0].render(0.0625F);
			sound[1].render(0.0625F);
		}
	}

	/**
	 * Do render stationary (nonrotated) piece.
	 */
	public void renderStationary() {

	}

	/**
	 * Render the bit which should change color based of what network it is
	 * connected to.
	 */
	public void renderColorMark() {

		if (deviceType == PClo_WeaselType.CORE) {
			core[3].render(0.0625F);
		} else if (deviceType == PClo_WeaselType.PORT) {
			port[3].render(0.0625F);
		} else if (deviceType == PClo_WeaselType.DISPLAY) {
			display[4].render(0.0625F);
		} else if (deviceType == PClo_WeaselType.SPEAKER) {
			sound[2].render(0.0625F);
		}

	}

	public void renderText(PClo_TileEntityWeaselRenderer renderer) {
		if (deviceType == PClo_WeaselType.DISPLAY) {

			float f = 0.6666667F;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			//GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			FontRenderer fontrenderer = PC_Utils.mc().fontRenderer;
			float f3 = 0.01666667F * f;

			GL11.glTranslatef(0.0F, 0.0625F * 15, 0.0625F + 0.001F);
			GL11.glScalef(f3, -f3, f3);

			GL11.glDepthMask(false);


			GL11.glNormal3f(0.0F, 0.0F, -1F * f3);

			int j = ((PClo_WeaselPluginDisplay) plugin).color;

			
//			
//			/**
//			 * This terrible piece of code was adjusted manually, and it is
//			 * miracle it works. NO NOT TOUCH!
//			 */
//			int bg = ((PClo_WeaselPluginDisplay) plugin).bgcolor;
//			PC_Color bgc = PC_Color.fromHex(bg);
//			renderer.bindTextureByName(mod_PClogic.getImgDir() + "block_chip.png");
//			float ff = 0.001388F;
//			float f1 = 0.001388F;
//			double x1 = -39.5;
//			double y1 = 0;
//			double w = 14 * 5.63;
//			double h = 12 * 5.63;
//			double zlevel = -0.0005;
//			double u = 202.6;
//			double v = 225.16;
//			GL11.glEnable(GL11.GL_DEPTH_TEST);
//			GL11.glColor4d(bgc.r, bgc.g, bgc.b, 1.0F);
//			Tessellator tessellator = Tessellator.instance;
//			tessellator.startDrawingQuads();
//			tessellator.addVertexWithUV(x1 + 0, y1 + h, zlevel, (float) (u + 0) * ff, (float) (v + h) * f1);
//			tessellator.addVertexWithUV(x1 + w, y1 + h, zlevel, (float) (u + w) * ff, (float) (v + h) * f1);
//			tessellator.addVertexWithUV(x1 + w, y1 + 0, zlevel, (float) (u + w) * ff, (float) (v + 0) * f1);
//			tessellator.addVertexWithUV(x1 + 0, y1 + 0, zlevel, (float) (u + 0) * ff, (float) (v + 0) * f1);
//			tessellator.draw();
			
			
			



			String s = ((PClo_WeaselPluginDisplay) plugin).text;
			int align = ((PClo_WeaselPluginDisplay) plugin).align;

			int i = -1;
			int maxlines = 7;
			int screenWidth = 76;

			String[] parts = s.split("\\\\n");

			String magic = "";

			two:
			for (String part : parts) {

				if (part.trim().length() == 0) {
					i++;
					continue;
				}

				List<String> lines = fontrenderer.listFormattedStringToWidth(part, screenWidth);

				for (String line : lines) {

					if (line.trim().length() == 0) continue;
					i++;

					String removedMagicStuff = line.replaceAll("§.", "");


					int len = fontrenderer.getStringWidth(removedMagicStuff);

					int start = -len / 2;

					if (align == 0) start = -len / 2;
					if (align == -1) start = -screenWidth / 2;
					if (align == 1) start = screenWidth / 2 - len;

					fontrenderer.drawString(magic + line, start, 2 + fontrenderer.FONT_HEIGHT * i, j);

					if (line.contains("§")) {
						int a = -1;
						while ((a = line.indexOf('§', a + 1)) != -1) {
							magic += '§';
							if (line.length() > a + 1) {
								char ch = line.charAt(a + 1);
								if (ch == 'r') {
									magic = "";
								} else {
									magic += ch;
								}
							}
						}
					}

					if (i == maxlines) break two;
				}
			}

			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
			//GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}
}
