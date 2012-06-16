package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * Renderer for deco block non solid kind
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCde_TileEntityWalkableRenderer extends TileEntitySpecialRenderer {

	private PCde_ModelDeco model;

	/**
	 * sensor renderer
	 */
	public PCde_TileEntityWalkableRenderer() {
		model = new PCde_ModelDeco();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f0) {

		PCde_TileEntityWalkable ted = (PCde_TileEntityWalkable) tileentity;
		

		GL11.glPushMatrix();
		float f = 1.0F;

		GL11.glTranslatef((float) x + 0.5F, (float) y+0.5F, (float) z + 0.5F);

		bindTextureByName(mod_PCdeco.getImgDir() + "block_deco.png");

		GL11.glPushMatrix();
		GL11.glScalef(f, -f, -f);

		
		if(ted.type == 0){
			boolean[] fences = PCde_BlockWalkable.getFencesShownLedge(ted.worldObj, new PC_CoordI(ted.xCoord,ted.yCoord,ted.zCoord));
			model.setLedgeFences(fences[0], fences[1], fences[2], fences[3], fences[4]);
		}else if(ted.type == 1){
			int meta = tileentity.worldObj.getBlockMetadata(tileentity.xCoord,tileentity.yCoord,tileentity.zCoord);
			GL11.glRotatef(90F + 90F*meta, 0, 1, 0);
			
			boolean[] fences = PCde_BlockWalkable.getFencesShownStairsRelative(ted.worldObj, new PC_CoordI(ted.xCoord,ted.yCoord,ted.zCoord));
			model.setStairsFences(fences[0], fences[1]);
		}
		
		model.render(100+ted.type);
		
		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
