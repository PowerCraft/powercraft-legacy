package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Weasel chip renderer
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityWeaselRenderer extends TileEntitySpecialRenderer {

	private PClo_ModelWeasel model;

	/**
	 * radio te renderer
	 */
	public PClo_TileEntityWeaselRenderer() {
		model = new PClo_ModelWeasel();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {
		
		PClo_TileEntityWeasel tew = (PClo_TileEntityWeasel) tileentity;
		
		if(tew == null) return;
		
		PClo_WeaselPlugin plugin = tew.getPlugin();
		
		if(plugin == null) return;
		
		model.deviceType = plugin.getType();
		model.plugin = plugin;
		model.flag1 = false;
		if(plugin instanceof PClo_WeaselPluginPort) model.flag1 = ((PClo_WeaselPluginPort)plugin).renderAsActive();
		
		PC_Color color = plugin.getNetworkColor();

		// push 1
		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, ((float) y), (float) z + 0.5F);
		

		bindTextureByName(mod_PClogic.getImgDir() + "block_chip.png");

		// push 2
		GL11.glPushMatrix();
		
		GL11.glScalef(f, -f, -f);
		
		model.renderStationary();float f1=0;
		if(plugin instanceof PClo_WeaselPluginDisplay) {
			f1 = ((PClo_WeaselPluginDisplay) plugin).rotation * 360 / 16F;
            GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
		}else {
			GL11.glRotatef( 90*(tew.getCoord().getMeta(tew.worldObj) & 3) , 0, 1, 0);		
		}
		model.renderDevice();
		
		GL11.glColor4d(color.r, color.g, color.b, 1D);
		
		model.renderColorMark();
		
		// pop 2
		GL11.glPopMatrix();
		
		

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
		model.renderText();
		
		// pop1
		GL11.glPopMatrix();
		
	}
}
