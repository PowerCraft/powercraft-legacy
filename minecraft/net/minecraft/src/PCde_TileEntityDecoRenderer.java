package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * Renderer for prozimity Sensor
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCde_TileEntityDecoRenderer extends TileEntitySpecialRenderer {

	private PCde_ModelDeco model;

	/**
	 * sensor renderer
	 */
	public PCde_TileEntityDecoRenderer() {
		model = new PCde_ModelDeco();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {

		PCde_TileEntityDeco ted = (PCde_TileEntityDeco) tileentity;

		if (ted.type == 1) { return; }

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

		bindTextureByName(mod_PCdeco.getImgDir() + "block_deco.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);

		model.setFrameParts(0, ted.worldObj.getBlockId(ted.xCoord, ted.yCoord + 1, ted.zCoord) == Block.torchWood.blockID);
		model.setFrameParts(1, ted.worldObj.getBlockId(ted.xCoord + 1, ted.yCoord, ted.zCoord) == Block.torchWood.blockID);
		model.setFrameParts(2, ted.worldObj.getBlockId(ted.xCoord, ted.yCoord, ted.zCoord + 1) == Block.torchWood.blockID);
		model.setFrameParts(3, ted.worldObj.getBlockId(ted.xCoord - 1, ted.yCoord, ted.zCoord) == Block.torchWood.blockID);
		model.setFrameParts(4, ted.worldObj.getBlockId(ted.xCoord, ted.yCoord, ted.zCoord - 1) == Block.torchWood.blockID);
		model.render(ted.type);

		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
