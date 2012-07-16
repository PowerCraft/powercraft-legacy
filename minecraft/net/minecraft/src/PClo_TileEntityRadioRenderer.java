package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Radio special renderer
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityRadioRenderer extends TileEntitySpecialRenderer {

	private PClo_ModelRadio model;

	/**
	 * radio te renderer
	 */
	public PClo_TileEntityRadioRenderer() {
		model = new PClo_ModelRadio();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {

		PClo_TileEntityRadio ter = (PClo_TileEntityRadio) tileentity;

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

		bindTextureByName(mod_PClogic.getImgDir() + "block_radio.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);
		model.setType(ter.isTransmitter(), ter.worldObj.getBlockMetadata(ter.xCoord, ter.yCoord, ter.zCoord) == 1); // ter.active);
		
		model.tiny = ter.renderMicro;
		
		model.render();
		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		
		if(!ter.hideLabel) {
			String foo = ter.getChannel();
			PC_Renderer.renderEntityLabelAt(foo, new PC_CoordF(ter.xCoord, ter.yCoord, ter.zCoord), 8, ter.renderMicro?0.5F:1.3F, x, y, z);
		}
	}

}
