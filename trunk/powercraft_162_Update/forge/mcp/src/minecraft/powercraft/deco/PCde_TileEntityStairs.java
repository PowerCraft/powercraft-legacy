package powercraft.deco;

import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_ITileEntityRenderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Utils;

public class PCde_TileEntityStairs extends PC_TileEntity implements PC_ITileEntityRenderer {
	
	private PCde_ModelStairs model = new PCde_ModelStairs();

	@Override
	public void renderTileEntityAt(double x, double y, double z, float f0) {

		float f = 1.0F;

		PC_Renderer.bindTexture(PC_TextureRegistry.getPowerCraftImageDir()+PC_TextureRegistry.getTextureName(PCde_App.instance, "block_deco.png"));

		PC_Renderer.glPushMatrix();
		PC_Renderer.glRotatef(180, 0, 1, 0);
		PC_Renderer.glScalef(f, -f, -f);

		PCde_BlockStairs block = PC_Utils.getBlock(worldObj, xCoord, yCoord, zCoord);
		
		boolean[] fences = block.getFencesShownStairsRelative(worldObj, getCoord());
		model.setStairsFences(fences[0], fences[1]);

		model.render();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();
		
	}

}
