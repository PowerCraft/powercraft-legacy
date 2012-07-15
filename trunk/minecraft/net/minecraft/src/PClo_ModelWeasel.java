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
		textureHeight = 64;

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

		

		display = new ModelRenderer[4];
		
		// the bottom pad
		display[0] = new ModelRenderer(this, 85, 27);
		display[0].addBox(-5F, -1F, -5F, 10, 1, 10, 0.0F);
		
		// the connection piece
		display[1] = new ModelRenderer(this, 86, 16);
		display[1].addBox(-1F, -2F, -1F, 2, 1, 2, 0.0F);
		
		// screen
		display[2] = new ModelRenderer(this, 58, 20);
		display[2].addBox(-8F, -16F, -1F, 16, 14, 2, 0.0F);
		
		// the colour piece
		display[3] = new ModelRenderer(this, 13, 12);
		display[3].addBox(-2F, -17F, -1F, 4, 1, 2, 0.0F);
		
		
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
		
		if(deviceType == PClo_WeaselType.CORE) {
			core[0].render(0.0625F);
			core[1].render(0.0625F);
			core[2].render(0.0625F);
		}else		
		if(deviceType == PClo_WeaselType.PORT) {
			port[0].render(0.0625F);
			if(flag1) {
				port[1].render(0.0625F);
			}else {
				port[2].render(0.0625F);
			}
		}else
		if(deviceType == PClo_WeaselType.DISPLAY) {
			display[0].render(0.0625F);
			display[1].render(0.0625F);
			display[2].render(0.0625F);
		}else
		if(deviceType == PClo_WeaselType.SPEAKER) {
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
	 * Render the bit which should change color based of what network it is connected to.
	 */
	public void renderColorMark() {
		
		if(deviceType == PClo_WeaselType.CORE) {
			core[3].render(0.0625F);			
		}else		
		if(deviceType == PClo_WeaselType.PORT) {
			port[3].render(0.0625F);			
		}else		
		if(deviceType == PClo_WeaselType.DISPLAY) {
			display[3].render(0.0625F);			
		}else		
		if(deviceType == PClo_WeaselType.SPEAKER) {
			sound[2].render(0.0625F);			
		}
		
	}

	public void renderText() {
		if(deviceType == PClo_WeaselType.DISPLAY) {
			
			float f = 0.6666667F;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			//GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			FontRenderer fontrenderer = PC_Utils.mc().fontRenderer;
	        float f3 = 0.01666667F * f;
	        
	        GL11.glTranslatef(0.0F, 0.0625F*15, 0.0625F+0.0001F);
	        GL11.glScalef(f3, -f3, f3);
	        GL11.glNormal3f(0.0F, 0.0F, -1F * f3);
	        GL11.glDepthMask(false);
	        
	        int j = ((PClo_WeaselPluginDisplay)plugin).color;
	        String s = ((PClo_WeaselPluginDisplay)plugin).text;
	        int align = ((PClo_WeaselPluginDisplay)plugin).align;
	        
	        int i = 0;
	        int maxlines = 8;
	        int screenWidth = 76;
	        String[] parts = s.split("\\n");
	        
	        two:
	    	for(String part: parts) {	        
		        List<String> lines = fontrenderer.listFormattedStringToWidth(part, screenWidth);
		        
		        for(String line: lines) {
		        	if(line.trim().length() == 0) continue;
		        	int len = fontrenderer.getStringWidth(line);
		        	
		        	int start = -len/2;
		        	
		        	if(align == 0) start = -len /2;
		        	if(align == -1) start = -screenWidth/2;
		        	if(align == 1) start = screenWidth/2 - len;
		        	
		        	fontrenderer.drawString(line, start, 2 + fontrenderer.FONT_HEIGHT*i, j);
		        	i++;
		        	if(i == maxlines) break two;
		        }
		        i++;
	        }
	        
	        GL11.glDepthMask(true);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glEnable(GL11.GL_LIGHTING);
			//GL11.glDisable(GL11.GL_BLEND);
	        GL11.glPopMatrix();
		}
	}
}
