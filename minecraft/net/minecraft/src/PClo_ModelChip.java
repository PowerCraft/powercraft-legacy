package net.minecraft.src;

import org.lwjgl.opengl.GL11;


/**
 * Model for PClo_TileEntityGateRenderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ModelChip extends ModelBase {

	private ModelRenderer core[];
	private ModelRenderer port[];
	private ModelRenderer display[];
	
	/**
	 * Radio block model.
	 */
	public PClo_ModelChip() {

		textureWidth = 128;
		textureHeight = 64;

		core = new ModelRenderer[4];
		
		// the stone pad
		core[0] = new ModelRenderer(this, 9, 20);
		core[0].addBox(-8F, 2F, -8F, 16, 3, 16, 0.0F);

		// body
		core[1] = new ModelRenderer(this, 0, 0);
		core[1].addBox(-4F, -1F, -5F, 8, 2, 10, 0.0F);

		// legs
		core[2] = new ModelRenderer(this, 70, 0);
		core[2].addBox(-5F, 0F, -5F, 10, 3, 10, 0.0F);
		
		// the colour pad
		core[3] = new ModelRenderer(this, 13, 12);
		core[3].addBox(-2F, -1.5F, 2F, 4, 1, 2, 0.0F);
		
		
		
		port = new ModelRenderer[4];
		
		// the stone pad
		port[0] = new ModelRenderer(this, 21, 0);
		port[0].addBox(-8F, 2F, -8F, 16, 3, 16, 0.0F);

		// the piece with light, on
		port[1] = new ModelRenderer(this, 0, 14);
		port[1].addBox(-3F, 0F, -3F, 6, 2, 6, 0.0F);
		
		// the piece with light, off
		port[2] = new ModelRenderer(this, 0, 23);
		port[2].addBox(-3F, 0F, -3F, 6, 2, 6, 0.0F);
		
		// the colour pad
		port[3] = new ModelRenderer(this, 13, 12);
		port[3].addBox(-2F, 0.5F, -6F, 4, 1, 2, 0.0F);


		display = new ModelRenderer[3];
		
		// the stone pad
		display[0] = new ModelRenderer(this, 0, 40);
		display[0].addBox(-8F, -11F, -0.5F, 16, 16, 1, 0.0F);
		
		// the colour pad
		display[1] = new ModelRenderer(this, 13, 12);
		display[1].addBox(-2F, 3.99F, -1F, 4, 1, 2, 0.0F);
		
	}
	
	public int deviceType = PClo_WeaselType.CORE;
	public boolean active = false;
	public PClo_WeaselPlugin plugin;
	public PC_TileEntity tileentity;

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
			if(active) {
				port[1].render(0.0625F);
			}else {
				port[2].render(0.0625F);
			}
		}else
		if(deviceType == PClo_WeaselType.DISPLAY) {
			display[0].render(0.0625F);
		}
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
			display[1].render(0.0625F);			
		}
		
	}

	public void renderText() {
		if(deviceType == PClo_WeaselType.DISPLAY) {
			float f = 0.6666667F;
			GL11.glPushMatrix();
			
			FontRenderer fontrenderer = PC_Utils.mc().fontRenderer;
	        float f3 = 0.01666667F * f;
	        
	        GL11.glTranslatef(0.0F, 0.0F, 0.1F);
	        GL11.glScalef(f3, -f3, f3);
	        GL11.glNormal3f(0.0F, 0.0F, -1F * f3);
	        GL11.glDepthMask(false);
	        
	        int j = 0;
	        String s = ((PClo_WeaselPluginDisplay)plugin).text;
	        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 1, j);
	        
	        GL11.glDepthMask(true);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glPopMatrix();
	        
		}
	}
}
