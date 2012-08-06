package net.minecraft.src;


import org.lwjgl.opengl.GL11;


/**
 * Renderer for prozimity Sensor
 * 
 * @author MightyPork
 * @copy (c) 2012
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

		if (ted.type == 1) {
			return;
		}

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

		bindTextureByName(mod_PCdeco.getImgDir() + "block_deco.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);

		model.setFrameParts(0, needsFullFace(ted.getCoord().offset(0, 1, 0).getId(ted.worldObj)));
		model.setFrameParts(1, needsFullFace(ted.getCoord().offset(1, 0, 0).getId(ted.worldObj)));
		model.setFrameParts(2, needsFullFace(ted.getCoord().offset(0, 0, 1).getId(ted.worldObj)));
		model.setFrameParts(3, needsFullFace(ted.getCoord().offset(-1, 0, 0).getId(ted.worldObj)));
		model.setFrameParts(4, needsFullFace(ted.getCoord().offset(0, 0, -1).getId(ted.worldObj)));
		model.render(ted.type);

		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	private boolean needsFullFace(int id) {
		if (id == Block.torchWood.blockID) return true;
		if (id == Block.torchRedstoneActive.blockID) return true;
		if (id == Block.torchRedstoneIdle.blockID) return true;
		if (id == Block.lever.blockID) return true;
		if (id == Block.button.blockID) return true;
		if (PC_BlockUtils.hasFlag(new ItemStack(id, 1, 0), "ATTACHED")) return true;
		return false;

	}

}
