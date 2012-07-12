package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Radio special renderer
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityGateRenderer extends TileEntitySpecialRenderer {

	private PClo_ModelChip model;

	/**
	 * radio te renderer
	 */
	public PClo_TileEntityGateRenderer() {
		model = new PClo_ModelChip();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {
		
		PClo_TileEntityGate teg = (PClo_TileEntityGate) tileentity;
		
		if(teg.gateType != PClo_GateType.PROG) return;

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, ((float) y)+0.0625F*5F, (float) z + 0.5F);
		GL11.glRotatef( -90*(teg.getCoord().getMeta(teg.worldObj) & 3) , 0, 1, 0);

		bindTextureByName(mod_PClogic.getImgDir() + "block_chip.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);

		model.render();
		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		
	}
}
