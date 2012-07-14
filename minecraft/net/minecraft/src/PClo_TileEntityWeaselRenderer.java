package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Weasel chip renderer
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityWeaselRenderer extends TileEntitySpecialRenderer {

	private PClo_ModelChip model;

	/**
	 * radio te renderer
	 */
	public PClo_TileEntityWeaselRenderer() {
		model = new PClo_ModelChip();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {
		
		PClo_TileEntityWeasel tew = (PClo_TileEntityWeasel) tileentity;
		
		if(tew == null) return;
		
		PClo_WeaselPlugin plugin = tew.getPlugin();
		
		if(plugin == null) return;
		
		model.deviceType = plugin.getType();
		model.plugin = plugin;
		model.tileentity = (PC_TileEntity)tileentity;
		model.active = false;
		if(plugin instanceof PClo_WeaselPluginPort) model.active = ((PClo_WeaselPluginPort)plugin).renderAsActive();
		
		PC_Color color = plugin.getNetworkColor();

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, ((float) y)+0.0625F*5F, (float) z + 0.5F);
		GL11.glRotatef( -90*(tew.getCoord().getMeta(tew.worldObj) & 3) , 0, 1, 0);

		bindTextureByName(mod_PClogic.getImgDir() + "block_chip.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);

		model.renderDevice();
		
		GL11.glColor4d(color.r, color.g, color.b, 1D);
		
		model.renderColorMark();
		
		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		model.renderText();
		
		GL11.glPopMatrix();
		
	}
}
