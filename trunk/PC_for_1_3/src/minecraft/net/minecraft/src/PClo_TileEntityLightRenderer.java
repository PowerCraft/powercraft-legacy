package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Radio special renderer
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityLightRenderer extends TileEntitySpecialRenderer {

	private PClo_ModelLight model;

	/**
	 * radio te renderer
	 */
	public PClo_TileEntityLightRenderer() {
		model = new PClo_ModelLight();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {

		PClo_TileEntityLight tel = (PClo_TileEntityLight) tileentity;

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

		bindTextureByName(mod_PClogic.getImgDir() + "block_light.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);

		PC_Color clr = tel.getFullColor(tel.isActive());
		if(clr!=null)
			GL11.glColor4d(clr.r, clr.g, clr.b, 1.0D);
		else
			GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);

		int meta = tel.getCoord().getMeta(tel.worldObj);
		switch (meta) {
			case 0:
				break;
			case 1:
				GL11.glRotatef(-90, 1, 0, 0);
				break;
			case 2:
				GL11.glRotatef(90, 1, 0, 0);
				break;
			case 3:
				GL11.glRotatef(-90, 0, 0, 1);
				break;
			case 4:
				GL11.glRotatef(90, 0, 0, 1);
				break;
			case 5:
				GL11.glRotatef(180, 1, 0, 0);
				break;
		}

		if (tel.isHuge) {
			model.renderHuge();
		} else {
			model.renderNormal();
		}

		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
