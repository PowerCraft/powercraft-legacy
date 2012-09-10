package net.minecraft.src.PCnt;


import java.util.List;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;
import net.minecraft.src.PC_Color;
import net.minecraft.src.Tessellator;
import net.minecraft.src.mod_PCcore;

import org.lwjgl.opengl.GL11;


/**
 * Model for PClo_TileEntityGateRenderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCnt_ModelWeasel extends ModelBase {

	private ModelRenderer core[];
	private ModelRenderer port[];
	private ModelRenderer display[];
	private ModelRenderer sound[];
	private ModelRenderer touchscreen[];
	private ModelRenderer diskmanager[];
	private ModelRenderer diskdrive[];
	private ModelRenderer terminal[];

	/**
	 * Radio block model.
	 */
	public PCnt_ModelWeasel() {

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



		touchscreen = new ModelRenderer[2];

		// the bottom pad
		touchscreen[0] = new ModelRenderer(this, 0, 81);
		//legs
		touchscreen[0].addBox(-5F, -1F, -5F, 1, 1, 10, 0.0F);
		touchscreen[0].addBox(4F, -1F, -5F, 1, 1, 10, 0.0F);

		// connections
		touchscreen[0].addBox(-5F, -2F, -0.5F, 1, 1, 1, 0.0F);
		touchscreen[0].addBox(4F, -2F, -0.5F, 1, 1, 1, 0.0F);

		// top,bottom
		touchscreen[0].addBox(-8F, -3F, -0.5F, 16, 1, 1, 0.0F);
		touchscreen[0].addBox(-8F, -16F, -0.5F, 16, 1, 1, 0.0F);

		// sides
		touchscreen[0].addBox(-8F, -15F, -0.5F, 1, 12, 1, 0.0F);
		touchscreen[0].addBox(7F, -15F, -0.5F, 1, 12, 1, 0.0F);

		// the colour piece
		touchscreen[1] = new ModelRenderer(this, 13, 12);
		touchscreen[1].addBox(-2F, -17F, -1F, 4, 1, 2, 0.0F);



		diskmanager = new ModelRenderer[2];

		// box
		diskmanager[0] = new ModelRenderer(this, 0, 99);
		diskmanager[0].addBox(-8F, -13F, -8F, 16, 13, 16, 0.0F);

		// top
		diskmanager[1] = new ModelRenderer(this, 37, 53);
		diskmanager[1].addBox(-6F, -14F, -6F, 12, 1, 12, 0.0F);


		diskdrive = new ModelRenderer[2];

		// box
		diskdrive[0] = new ModelRenderer(this, 64, 99);
		diskdrive[0].addBox(-8F, -13F, -8F, 16, 13, 16, 0.0F);

		// colored
		diskdrive[1] = new ModelRenderer(this, 0, 31);
		diskdrive[1].addBox(-2F, -14F, -2F, 4, 1, 4, 0.0F);


		terminal = new ModelRenderer[4];

		// box
		terminal[0] = new ModelRenderer(this, 39, 67);
		terminal[0].addBox(-7F, -4F, -7F, 14, 4, 14, 0.0F);

		// keyb
		terminal[1] = new ModelRenderer(this, 62, 59);
		terminal[1].addBox(-5F, -5F, -5F, 10, 1, 4, 0.0F);

		//screen
		terminal[2] = new ModelRenderer(this, 39, 85);
		terminal[2].addBox(-6F, -12F, 0F, 12, 8, 6, 0.0F);

		terminal[3] = new ModelRenderer(this, 13, 12);
		terminal[3].addBox(-2F, -13F, 2F, 4, 1, 2, 0.0F);


	}

	/** index of then plugin */
	public int deviceType = PCnt_WeaselType.CORE;
	/** helper flag, eg. "active" */
	public boolean flag1 = false;
	/** instance of the plugin */
	public PCnt_WeaselPlugin plugin;

	/**
	 * Do render.
	 */
	public void renderDevice() {

		if (deviceType == PCnt_WeaselType.CORE || deviceType == PCnt_WeaselType.SLAVE) {
			core[0].render(0.0625F);
			core[1].render(0.0625F);
			if(deviceType == PCnt_WeaselType.CORE) {
				GL11.glColor4f(1.0F, 0.4F, 0.4F, 1.0F);
			}else {				
				GL11.glColor4f(0.8F, 0.8F, 1.0F, 1.0F);
			}
			core[2].render(0.0625F);
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		} else if (deviceType == PCnt_WeaselType.PORT) {
			port[0].render(0.0625F);
			if (flag1) {
				port[1].render(0.0625F);
			} else {
				port[2].render(0.0625F);
			}
		} else if (deviceType == PCnt_WeaselType.DISPLAY) {
			display[0].render(0.0625F);
			display[1].render(0.0625F);
			display[2].render(0.0625F);
			int bg = ((PCnt_WeaselPluginDisplay) plugin).bgcolor;
			PC_Color bgc = PC_Color.fromHex(bg);
			GL11.glColor4d(bgc.r, bgc.g, bgc.b, 1.0F);
			display[3].render(0.0625F);
			GL11.glColor4d(1, 1, 1, 1);
		} else if (deviceType == PCnt_WeaselType.SPEAKER) {
			sound[0].render(0.0625F);
			sound[1].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.TOUCHSCREEN) {
			touchscreen[0].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.DISK_MANAGER) {
			diskmanager[0].render(0.0625F);
			diskmanager[1].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.DISK_DRIVE) {
			diskdrive[0].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.TERMINAL) {
			terminal[0].render(0.0625F);
			terminal[1].render(0.0625F);
			terminal[2].render(0.0625F);
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

		if (deviceType == PCnt_WeaselType.CORE || deviceType == PCnt_WeaselType.SLAVE) {
			core[3].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.PORT) {
			port[3].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.DISPLAY) {
			display[4].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.SPEAKER) {
			sound[2].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.TOUCHSCREEN) {
			touchscreen[1].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.DISK_MANAGER) {
			// none
		} else if (deviceType == PCnt_WeaselType.DISK_DRIVE) {
			diskdrive[1].render(0.0625F);
		} else if (deviceType == PCnt_WeaselType.TERMINAL) {
			terminal[3].render(0.0625F);
		}

	}

	/**
	 * Render the text if there is any
	 * 
	 * @param renderer
	 */
	@SuppressWarnings("static-access")
	public void renderText(PCnt_TileEntityWeaselRenderer renderer) {
		float f = 0.6666667F;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		FontRenderer fontrenderer = mod_PCcore.fontRendererDefault; //PC_Utils.mc().fontRenderer;
		float f3 = 0.01666667F * f;

		GL11.glNormal3f(0.0F, 0.0F, -1F * f3);


		if (deviceType == PCnt_WeaselType.DISPLAY) {

			GL11.glDepthMask(false);
			GL11.glTranslatef(0.0F, 0.0625F * 15, 0.0625F + 0.001F);
			GL11.glScalef(f3, -f3, f3);

			int j = ((PCnt_WeaselPluginDisplay) plugin).color;


			String s = ((PCnt_WeaselPluginDisplay) plugin).text;
			int align = ((PCnt_WeaselPluginDisplay) plugin).align;

			int i = -1;
			int maxlines = 7;
			int screenWidth = 76;

			String[] parts = s.split("\\\\n");

			try {
				String magic = "";

				two:
				for (String part : parts) {

					if (part.trim().length() == 0) {
						i++;
						continue;
					}
					if (part.charAt(part.length() - 1) == '\u00A7') {
						part = part.substring(0, part.length() - 1);
					}
					List<String> lines = fontrenderer.listFormattedStringToWidth(part, screenWidth);

					for (String line : lines) {


						if (line.trim().length() == 0) continue;

						if (line.charAt(line.length() - 1) == '\u00A7') {
							line = line.substring(0, line.length() - 1);
						}
						i++;

						String removedMagicStuff = line.replaceAll("\u00A7.", "");


						int len = fontrenderer.getStringWidth(removedMagicStuff);

						int start = -len / 2;

						if (align == 0) start = -len / 2;
						if (align == -1) start = -screenWidth / 2;
						if (align == 1) start = screenWidth / 2 - len;

						fontrenderer.drawString(magic + line, start, 2 + fontrenderer.FONT_HEIGHT * i, j);

						if (line.contains("\u00A7")) {
							int a = -1;
							while ((a = line.indexOf('\u00A7', a + 1)) != -1) {
								magic += '\u00A7';
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
			} catch (StringIndexOutOfBoundsException e) {}

		} else if (deviceType == PCnt_WeaselType.TOUCHSCREEN) {

			PCnt_WeaselPluginTouchscreen touchscreen = (PCnt_WeaselPluginTouchscreen) plugin;
			Tessellator tessellator = Tessellator.instance;

			GL11.glTranslatef(0.0f, 0.0625f * 9, 0.0f);
			GL11.glScalef(0.0625f * 14 / touchscreen.WIDTH, -0.0625f * 12 / touchscreen.HEIGHT, f3);
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			tessellator.startDrawingQuads();
			double posX, posY, pixelW, pixelH;
			int color;
			pixelW = 1.0D;
			pixelH = 1.0D;
			for (int j = 0; j < touchscreen.HEIGHT; j++) {
				for (int i = 0; i < touchscreen.WIDTH; i++) {
					color = touchscreen.screen[i][j];
					if (color != -1) {
						posX = i - touchscreen.WIDTH * 0.5;
						posY = j - touchscreen.HEIGHT * 0.5;
						tessellator.setColorRGBA((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 255);
						tessellator.addVertex(posX + pixelW, posY, 0.0D);
						tessellator.addVertex(posX, posY, 0.0D);
						tessellator.addVertex(posX, posY + pixelH, 0.0D);
						tessellator.addVertex(posX + pixelW, posY + pixelH, 0.0D);

						tessellator.addVertex(posX, posY, 0.0D);
						tessellator.addVertex(posX + pixelW, posY, 0.0D);
						tessellator.addVertex(posX + pixelW, posY + pixelH, 0.0D);
						tessellator.addVertex(posX, posY + pixelH, 0.0D);

					}
				}
			}
			tessellator.draw();

			/*GL11.glScalef(-1, 1, 1);
			
			tessellator.startDrawingQuads();
			for(int j=0; j<touchscreen.HEIGHT; j++){
				for(int i=0; i<touchscreen.WIDTH; i++){
					color = touchscreen.screen[i][j];
					if(((color>>24) & 0xFF) != 0){
			    		posX = i-touchscreen.WIDTH*0.5;
			    		posY = j-touchscreen.HEIGHT*0.5;
			    		color = touchscreen.screen[i][j];
			    		tessellator.setColorRGBA((color >> 16) & 0xFF,  (color >> 8) & 0xFF, color & 0xFF, 255);
			            tessellator.addVertex(posX + pixelW, posY, 0.0D);
			            tessellator.addVertex(posX, posY, 0.0D);
			            tessellator.addVertex(posX, posY + pixelH, 0.0D);
			            tessellator.addVertex(posX + pixelW, posY + pixelH, 0.0D);
					}
				}
			}
			tessellator.draw();*/

			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
